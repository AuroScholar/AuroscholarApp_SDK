package com.auroscholar.final_auroscholarapp_sdk

data class SDKDataModel(var status:String?=null,var message:String?=null,var error:Boolean?=null,var response_code:Int?=null,var user_details:List<SDKChildModel>?=null)

public class SDKChildModel(var user_id:String?=null,var user_name:String?=null, var student_name:String?=null, var profile_pic:String?=null,var user_prefered_language_id:String?=null, var partner_source:String?=null, var kyc_status:String?=null,
                           var grade:String?=null, var mobile_verified:String?=null, var mobile_no: String?=null, var partner_logo:String?=null, var is_mapped:Int?=null)