package com.auro.application.home.presentation.view.fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.application.base_component.BaseDialog;
import com.auro.application.core.application.di.component.ViewModelFactory;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.databinding.DialogInviteLayoutBinding;
import com.auro.application.home.data.model.Details;
import com.auro.application.home.data.model.LanguageMasterDynamic;
import com.auro.application.home.presentation.viewmodel.InviteFriendViewModel;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.permission.PermissionHandler;
import com.auro.application.util.permission.PermissionUtil;
import com.auro.application.util.permission.Permissions;

import java.util.ArrayList;

import javax.inject.Inject;
import javax.inject.Named;

import static android.app.Activity.RESULT_OK;

public class InviteFriendDialog  extends BaseDialog implements View.OnClickListener {

    @Inject
    @Named("InviteFriendDialog")
    ViewModelFactory viewModelFactory;
    private static final int REQUEST_CODE_PICK_CONTACTS = 1;

    Context mcontext;
    private Uri uriContact;
    private String contactID;
    private static final String TAG = InviteFriendDialog.class.getSimpleName();


    DialogInviteLayoutBinding binding;
    InviteFriendViewModel viewModel;


    public InviteFriendDialog(Context mcontext) {
        this.mcontext = mcontext;

    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding = DataBindingUtil.inflate(inflater, getLayout(), container, false);
        ((AuroApp) getActivity().getApplication()).getAppComponent().doInjection(this);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(InviteFriendViewModel.class);
        binding.setLifecycleOwner(this);
        setRetainInstance(true);
        init();
        setListener();
        LanguageMasterDynamic model = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic();
        Details details = model.getDetails();
        binding.txtInviteBy.setText(details.getInviteBy());
        binding.editContact.setText(details.getEnterPhoneNumber());
        binding.btninvite.setText(details.getInvite());
        binding.txtor.setText(details.getOr());
        binding.txtShareWithOther.setText(details.getShareWithOther());
       // setStyle(DialogFragment.STYLE_NO_TITLE, R.style.MyDialog);


        return binding.getRoot();
    }

    @Override
    protected void init() {
        binding.txtShareWithOther.setOnClickListener(this);
        binding.rlClose.setOnClickListener(this);
        binding.icContact.setOnClickListener(this);
        binding.icClose.setOnClickListener(this);

    }

    @Override
    protected void setToolbar() {

    }

    @Override
    protected void setListener() {

    }

    @Override
    protected int getLayout() {
        return R.layout.dialog_invite_layout;
    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.icClose) {
            dismiss();
        } else if (id == R.id.txtShareWithOther) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, "This is my text to send.");
            sendIntent.setType("text/plain");

            Intent shareIntent = Intent.createChooser(sendIntent, null);
            dismiss();
            mcontext.startActivity(shareIntent);
        } else if (id == R.id.icContact) {
            askPermission();
           // mlistener.performAddButton();

        }else if(id == R.id.rlClose){
            dismiss();
        }
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_PICK_CONTACTS && resultCode == RESULT_OK) {
            Log.d(TAG, "Response: " + data.toString());
            uriContact = data.getData();
            retrieveContactNumber();
        }
    }
    @SuppressLint("Range")
    private void retrieveContactNumber() {

        String contactNumber = null;

        // getting contacts ID
        Cursor cursorID = getActivity().getContentResolver().query(uriContact,
                new String[]{ContactsContract.Contacts._ID},
                null, null, null);

        if (cursorID.moveToFirst()) {

            contactID = cursorID.getString(cursorID.getColumnIndex(ContactsContract.Contacts._ID));
        }

        cursorID.close();

        Log.e(TAG, "Contact ID: " + contactID);

        // Using the contact ID now we will get contact phone number
        Cursor cursorPhone = getActivity().getContentResolver().query(ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
                new String[]{ContactsContract.CommonDataKinds.Phone.NUMBER},

                ContactsContract.CommonDataKinds.Phone.CONTACT_ID + " = ? AND " +
                        ContactsContract.CommonDataKinds.Phone.TYPE + " = " +
                        ContactsContract.CommonDataKinds.Phone.TYPE_MOBILE,

                new String[]{contactID},
                null);

        if (cursorPhone.moveToFirst()) {
            contactNumber = cursorPhone.getString(cursorPhone.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER));
            binding.editContact.setText(contactNumber);
        }

        cursorPhone.close();

    }
    private void askPermission() {
//        String rationale = getString(R.string.permission_error_msg);
//        Permissions.Options options = new Permissions.Options()
//                .setRationaleDialogTitle("Info")
//                .setSettingsDialogTitle("Warning");
//        Permissions.check(getActivity(), PermissionUtil.mContactPermission, rationale, options, new PermissionHandler() {
//            @Override
//            public void onGranted() {
                startActivityForResult(new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI), REQUEST_CODE_PICK_CONTACTS);
//            }
//
//            @Override
//            public void onDenied(Context context, ArrayList<String> deniedPermissions) {
//                // permission denied, block the feature.
//                ViewUtil.showSnackBar(binding.getRoot(), rationale);
//            }
//        });
    }
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        setRetainInstance(true);
    }

}
