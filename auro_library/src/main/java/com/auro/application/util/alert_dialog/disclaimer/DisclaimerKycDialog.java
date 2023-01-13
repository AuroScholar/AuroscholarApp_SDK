package com.auro.application.util.alert_dialog.disclaimer;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.method.LinkMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.Window;
import android.widget.TextView;

import androidx.databinding.DataBindingUtil;

import com.auro.application.R;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.databinding.AmParentDialogBinding;
import com.auro.application.home.data.model.response.InstructionsResModel;
import com.auro.application.home.presentation.view.activity.DashBoardMainActivity;
import com.auro.application.home.data.model.Details;
import com.auro.application.util.MySpannable;
import com.auro.application.util.ViewUtil;
import com.auro.application.util.strings.AppStringDynamic;


public class DisclaimerKycDialog extends Dialog {

    public Activity activity;
    private AmParentDialogBinding binding;
    PrefModel prefModel;
    InstructionsResModel instructionsResModel;

    public DisclaimerKycDialog(Activity context) {
        super(context);
        this.activity = context;

    }

    public void setInstructionData(InstructionsResModel instructionsResModel) {
        this.instructionsResModel = instructionsResModel;

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        binding = DataBindingUtil.inflate(LayoutInflater.from(getContext()), R.layout.am_parent_dialog, null, false);
        setContentView(binding.getRoot());
        prefModel = AuroAppPref.INSTANCE.getModelInstance();
        binding.parentCheckRow.setVisibility(View.GONE);
        AppStringDynamic.setParentStrings(binding);
        binding.tvParent.setVisibility(View.VISIBLE);
        binding.RlKycCheck.setVisibility(View.VISIBLE);
        binding.closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!prefModel.isPreKycDisclaimer()) {
                    dismiss();
                    //((DashBoardMainActivity) activity).onBackPressed();
                    Intent i = new Intent(activity, DashBoardMainActivity.class);
                   // i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    activity.startActivity(i);
                }
            }
        });

        binding.RPAccept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (binding.checkIsKyc.isChecked()) {
                    dismiss();
                    prefModel.setPreKycDisclaimer(false);
                    AuroAppPref.INSTANCE.setPref(prefModel);
                } else {
                    ViewUtil.showSnackBar(binding.getRoot(),AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails().getPlease_select_checkbox());
                }


            }
        });

        try {
            Details details = AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();
            binding.tvMessageKyc.setText(instructionsResModel.getData());
            binding.RPAccept.setText(details.getContinueExit());
            binding.tvParent.setText(details.getParentSection());
            makeTextViewResizable(binding.tvMessageKyc, 3, "...See More", true);
        } catch (Exception e) {
            binding.tvMessageKyc.setText(R.string.kyc_disclaimer_text_one);
            makeTextViewResizable(binding.tvMessageKyc, 3, "...See More", true);
        }
    }

    public static void makeTextViewResizable(final TextView tv, final int maxLine, final String expandText, final boolean viewMore) {

        if (tv.getTag() == null) {
            tv.setTag(tv.getText());
        }
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {

            @SuppressWarnings("deprecation")
            @Override
            public void onGlobalLayout() {


                try {
                    ViewTreeObserver obs = tv.getViewTreeObserver();
                    obs.removeGlobalOnLayoutListener(this);
                    if (maxLine == 0) {
                        int lineEndIndex = tv.getLayout().getLineEnd(0);
                        String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                        tv.setText(text);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());
                        tv.setText(
                                addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                        viewMore), TextView.BufferType.SPANNABLE);
                    } else if (maxLine > 0 && tv.getLineCount() >= maxLine) {
                        int lineEndIndex = tv.getLayout().getLineEnd(maxLine - 1);
                        String text = tv.getText().subSequence(0, lineEndIndex - expandText.length() + 1) + " " + expandText;
                        tv.setText(text);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());
                        tv.setText(
                                addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, maxLine, expandText,
                                        viewMore), TextView.BufferType.SPANNABLE);
                    } else {
                        int lineEndIndex = tv.getLayout().getLineEnd(tv.getLayout().getLineCount() - 1);
                        String text = tv.getText().subSequence(0, lineEndIndex) + " " + expandText;
                        tv.setText(text);
                        tv.setMovementMethod(LinkMovementMethod.getInstance());
                        tv.setText(
                                addClickablePartTextViewResizable(Html.fromHtml(tv.getText().toString()), tv, lineEndIndex, expandText,
                                        viewMore), TextView.BufferType.SPANNABLE);
                    }
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        });

    }

    private static SpannableStringBuilder addClickablePartTextViewResizable(final Spanned strSpanned, final TextView tv,
                                                                            final int maxLine, final String spanableText, final boolean viewMore) {
        String str = strSpanned.toString();
        SpannableStringBuilder ssb = new SpannableStringBuilder(strSpanned);

        if (str.contains(spanableText)) {


            ssb.setSpan(new MySpannable(false) {
                @Override
                public void onClick(View widget) {
                    if (viewMore) {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, -1, "See Less", false);
                    } else {
                        tv.setLayoutParams(tv.getLayoutParams());
                        tv.setText(tv.getTag().toString(), TextView.BufferType.SPANNABLE);
                        tv.invalidate();
                        makeTextViewResizable(tv, 3, "...See More", true);
                    }
                }
            }, str.indexOf(spanableText), str.indexOf(spanableText) + spanableText.length(), 0);

        }
        return ssb;

    }
}