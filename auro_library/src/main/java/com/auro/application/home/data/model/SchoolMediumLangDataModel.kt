package com.auro.application.home.data.model

data class SchoolMediumLangDataModel(var status:String?=null,var error:String?=null,var message:String?=null,var result:List<SchoolLangData>?=null)
class SchoolLangData(var language_id:Int?=null,var translated_language:String?=null, var language_name:String?=null)