package com.auro.application.util.authenticate;

import android.app.Activity;
import android.app.Person;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.auro.application.util.AppLogger;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.common.api.internal.OnConnectionFailedListener;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;

import java.util.concurrent.Executor;

public class GoogleSignInHelper  implements  GoogleApiClient.OnConnectionFailedListener{

    private final static String TAG = "GoogleSignInHelper";
    private final static int RC_SIGN_IN = 100;
    private final GoogleSignInOptions gso;


    private static GoogleSignInHelper sInstance;

    private GoogleApiClient googleApiClient;
    private Activity context;
    private OnGoogleSignInListener loginResultCallback;
    private ResultCallback<Status> logoutResultCallback;




    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    /**
     * Interface to listen the Google login
     */
    public interface OnGoogleSignInListener {
        void OnGSignSuccess(GoogleSignInAccount googleSignInAccount);

        void OnGSignError(GoogleSignInResult errorMessage);
    }

/*    public static GooglePlusSignInHelper getInstance() {
        if (sInstance == null) {
            sInstance = new GooglePlusSignInHelper();
        }
        return sInstance;
    }*/

    public static GoogleSignInHelper getFireBaseInstance(){
        if (sInstance == null) {
            sInstance = new GoogleSignInHelper();
        }
        return sInstance;
    }

    private GoogleSignInHelper() {
        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail() //for email
                .build();
    }

    public void initialize(FragmentActivity  activity, OnGoogleSignInListener onGoogleSignInListener)
    {
        loginResultCallback = onGoogleSignInListener;
        context = activity;
        googleApiClient = new GoogleApiClient.Builder(activity)
                .enableAutoManage(activity,this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    public boolean isConnected() {
        boolean isConnected = googleApiClient.isConnected();

        Log.i(TAG, "isConnected()" + isConnected);
        return isConnected;
    }

    public void signIn(Activity activity) {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        activity.startActivityForResult(intent, RC_SIGN_IN);
    }

    public void signOut(){
        Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        if (logoutResultCallback != null) {
                            logoutResultCallback.onResult(status);
                        }
                    }
                });
    }


    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleSignInResult(result);
        }
    }

    private void handleSignInResult(GoogleSignInResult result) {
        if (result.isSuccess()) {

            GoogleSignInAccount acct = result.getSignInAccount();

            if (loginResultCallback != null) {
                loginResultCallback.OnGSignSuccess(acct);
            }

        } else {

            if (loginResultCallback != null) {
                loginResultCallback.OnGSignError(result);
            }

        }
    }

    public void setLogoutResultCallback(ResultCallback<Status> callback) {
        logoutResultCallback = callback;
    }

}
