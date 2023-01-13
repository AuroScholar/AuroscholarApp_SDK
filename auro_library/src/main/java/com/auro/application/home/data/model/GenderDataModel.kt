package com.auro.application.home.data.model

data class GenderDataModel(var status:String?=null,var error:String?=null,var message:String?=null,var result:List<GenderData>?=null)
class GenderData(var ID:Int?=null,var TranslatedName:String?=null,var Name:String?=null)