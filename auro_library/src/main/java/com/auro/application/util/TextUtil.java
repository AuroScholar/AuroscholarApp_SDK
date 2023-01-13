package com.auro.application.util;

import android.graphics.Typeface;
import android.text.SpannableString;
import android.text.TextUtils;
import android.text.style.StyleSpan;
import android.util.Patterns;
import android.widget.TextView;


import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class TextUtil {

    public static void setBoldText(TextView pTextView, String pStringMsg, int pStartPoint, int pEndPoint) {
        SpannableString spanString = new SpannableString(pStringMsg);
        spanString.setSpan(new StyleSpan(Typeface.BOLD), pStartPoint, pEndPoint, 0);
        pTextView.setText(spanString);
    }

    public static boolean isEmpty(String text) {
        return text == null || text.isEmpty();

    }

    public static String getCamelCase(String text) {
        String upper = text.toUpperCase();
        return upper.charAt(0) + upper.substring(1).toLowerCase();
    }


    public static boolean checkListIsEmpty(List data) {
        if (data != null && !data.isEmpty()) {
            return false;
        } else {
            return true;
        }

    }

    public static String removeLastChar(String str) {
        return str.substring(0, str.length() - 1);
    }

    public static String removeAllSpace(String text) {
        return text.replaceAll("\\s+", "");
    }


    public static boolean isValidEmail(String  target) {



            String regExpn = "^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                    + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                    + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                    + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                    + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$";
            CharSequence inputStr = target;
            Pattern pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE);
            Matcher matcher = pattern.matcher(inputStr);
            if (matcher.matches())
                return true;
            else
                return false;

        //return (!TextUtils.isEmpty(target) && Patterns.EMAIL_ADDRESS.matcher(target).matches());
    }

    public static boolean isValidPhoneNumber(String s)
    {


        Pattern p = Pattern.compile("(0|91)?[7-9][0-9]{9}");

        Matcher m = p.matcher(s);
        return (m.find() && m.group().equals(s));
    }

}
