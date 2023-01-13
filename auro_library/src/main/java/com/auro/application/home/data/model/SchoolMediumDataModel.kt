package com.auro.application.home.data.model

data class SchoolMediumDataModel(var status:String?=null,var error:String?=null,var message:String?=null,var result:List<SchoolMediumData>?=null)
class SchoolMediumData(var ID:Int?=null,var Name:String?=null,var TranslatedName:String?=null)