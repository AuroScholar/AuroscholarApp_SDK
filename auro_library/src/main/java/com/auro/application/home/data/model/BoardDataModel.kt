package com.auro.application.home.data.model

data class BoardDataModel(var status:String?=null,var error:String?=null,var message:String?=null,var result:List<BoardData>?=null)
class BoardData(var ID:Int?=null,var TranslatedName:String?=null,var Name:String?=null)