package com.auro.application.home.data.model

data class PrivateTutionDataModel(var status:String?=null,var error:String?=null,var message:String?=null,var result:List<PrivateTutionData>?=null)
class PrivateTutionData(var ID:Int?=null,var TranslatedName:String?=null, var Name:String?=null)