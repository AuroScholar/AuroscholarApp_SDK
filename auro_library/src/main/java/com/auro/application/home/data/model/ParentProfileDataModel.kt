package com.auro.application.home.data.model

data class ParentProfileDataModel(var status:String?=null,var error:String?=null,var message:String?=null,var result:List<ParentProfileModel>?=null)

class ParentProfileModel(var parent_id:Int?=null,var user_id:Int?=null,var mobile_no:String?=null,var email_id:String?=null,
                         var registration_date:String?=null,var STATUS:Int?=null,var total_child:Int?=null,var total_kyc_approved:Int?=null,
                         var full_name:String?=null,var state_id:Int?=null,
                         var district_id:Int?=null,var gender:String?=null,var profile_pic:String?=null,var state_name:String?=null,var district_name:String?=null)