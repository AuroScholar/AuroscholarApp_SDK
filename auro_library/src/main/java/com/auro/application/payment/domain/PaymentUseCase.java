package com.auro.application.payment.domain;

import com.auro.application.R;
import com.auro.application.core.application.AuroApp;
import com.auro.application.core.common.AppConstant;
import com.auro.application.core.common.ValidationModel;
import com.auro.application.core.database.AuroAppPref;
import com.auro.application.core.database.PrefModel;
import com.auro.application.home.data.model.DemographicResModel;
import com.auro.application.home.data.model.Details;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class PaymentUseCase {

    public ValidationModel isValidIFSCode(String str)
    {
        // Regex to check valid IFSC Code.
        String regex = "^[A-Z]{4}0[A-Z0-9]{6}$";

        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        Details details = prefModel.getLanguageMasterDynamic().getDetails();
        if (str == null) {
            return new ValidationModel(false,details.getPlease_enter_ifsc_code());
        }

        // Pattern class contains matcher()
        // method to find matching between
        // the given string and
        // the regular expression.
        if(!Pattern.matches(regex, str)){
            return new ValidationModel(false,details.getPlease_enter_valid_ifsc_code());
        }


        // Return if the string
        // matched the ReGex
        return new ValidationModel(true, AppConstant.ifscCode.VALID);
    }

    public  ValidationModel isValidBankAccountNumber(String accountnumber,String ifscCode,String account2,String beneficiary_name){
        // Regex to check valid IFSC Code.

        PrefModel prefModel = AuroAppPref.INSTANCE.getModelInstance();
        Details details = prefModel.getLanguageMasterDynamic().getDetails();
        String regex = "[0-9]{9,18}";


        // If the string is empty
        // return false
        if (accountnumber == null) {
            return new ValidationModel(false, details.getPlease_enter_bank_account_number() != null ? details.getPlease_enter_bank_account_number() : AuroApp.getAppContext().getResources().getString(R.string.please_enter_bank_account_number));
        }




        if(!Pattern.matches(regex, accountnumber)){
            return new ValidationModel(false, details.getPlease_enter_valid_account_number() != null ? details.getPlease_enter_valid_account_number() : AuroApp.getAppContext().getResources().getString(R.string.please_enter_valid_account_number));
        }

        if(!Pattern.matches(regex, account2)){
            return new ValidationModel(false, details.getPlease_enter_valid_confirm_account_number() != null ? details.getPlease_enter_valid_confirm_account_number() : AuroApp.getAppContext().getResources().getString(R.string.please_enter_valid_confirm_account_number));
        }

        if(!accountnumber.equals(account2)){
            return new ValidationModel(false, details.getAccount_number_mismatch() != null ? details.getAccount_number_mismatch() : AuroApp.getAppContext().getResources().getString(R.string.account_number_mismatch));
        }


        if(beneficiary_name == null || beneficiary_name.equals("")){
            return new ValidationModel(false,details.getPleaseEnterBeneficiarryName() != null ? details.getPleaseEnterBeneficiarryName() : AuroApp.getAppContext().getResources().getString(R.string.please_enter_beneficiarry_name));
        }


        // Regex to check valid IFSC Code.
        String regexifsc = "^[A-Z]{4}0[A-Z0-9]{6}$";


        if (ifscCode == null) {
            return new ValidationModel(false, details.getPlease_enter_ifsc_code() != null ? details.getPlease_enter_ifsc_code() : AuroApp.getAppContext().getResources().getString(R.string.please_enter_ifsc_code));
        }

        // Pattern class contains matcher()
        // method to find matching between
        // the given string and
        // the regular expression.
        if(!Pattern.matches(regexifsc, ifscCode)){
            return new ValidationModel(false,details.getPlease_enter_valid_ifsc_code() != null ? details.getPlease_enter_valid_ifsc_code() : AuroApp.getAppContext().getResources().getString(R.string.please_enter_valid_ifsc_code));
        }





        // Return if the string
        // matched the ReGex
        return new ValidationModel(true, AppConstant.bankAccountNumber.VALID);
    }

    public ValidationModel isVlaidPhoneNumber(String phonenumber){

        Details details=AuroAppPref.INSTANCE.getModelInstance().getLanguageMasterDynamic().getDetails();

        String regex = "[a-zA-Z]+";

        // If the string is empty
        // return false
        if (phonenumber.isEmpty()) {
            return new ValidationModel(false,details.getPlease_enter_the_mobile_number() != null ?details.getPlease_enter_the_mobile_number() :AuroApp.getAppContext().getResources().getString(R.string.please_enter_the_mobile_number));//please_enter_the_mobile_number
        }

        if(phonenumber.length() < 10 ){

            return new ValidationModel(false, details.getPlease_enter_valid_phone_number() != null ?details.getPlease_enter_valid_phone_number() : AuroApp.getAppContext().getResources().getString(R.string.please_enter_valid_phone_number));
        }
/*
        if(Pattern.matches(regex,  phonenumber.toString())){
            return new ValidationModel(false, AppConstant.phoneNumber.VALIDPHONENUMBER);
        }
*/

        // Return if the string
        // matched the ReGex
        return new ValidationModel(true, AppConstant.phoneNumber.VALID);

    }

    public ValidationModel isVlaidMatchBankAccount(String account1,String account2){
        if(!account1.equals(account2)){
            return new ValidationModel(false, AppConstant.bankAccountNumber.BANKACCOUNTMATCH);
        }
        return new ValidationModel(true, AppConstant.bankAccountNumber.BANKACCOUNTMATCH);

    }



}
