package com.auro.application.home.data.model

data class SchoolTypeDataModel(var status:String?=null,var error:String?=null,var message:String?=null,var result:List<SchoolTypeData>?=null)
class SchoolTypeData(var ID:Int?=null,var TranslatedName:String?=null, var Name:String?=null)