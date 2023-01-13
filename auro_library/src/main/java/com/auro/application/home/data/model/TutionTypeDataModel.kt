package com.auro.application.home.data.model

data class TutionTypeDataModel(var status:String?=null,var error:String?=null,var message:String?=null,var result:List<TutionData>?=null)
class TutionData(var ID:Int?=null,var TranslatedName:String?=null, var Name:String?=null)