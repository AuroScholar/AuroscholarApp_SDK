package com.auro.application.home.data.model

data class ReferralPopUpDataModel(var status:String?=null,var error:String?=null,var message:String?=null,var show_refferal_popup:String?=null,var data:List<ReferralPopUpModel>?=null)

class ReferralPopUpModel(var user_id:Int?=null,var profile_pic:String?=null,var student_name:String?=null,
                         var mobile_no:String?=null)