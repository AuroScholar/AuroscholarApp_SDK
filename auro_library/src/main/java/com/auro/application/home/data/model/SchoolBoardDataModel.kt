package com.auro.application.home.data.model

data class SchoolBoardDataModel(var status:String?=null,var error:String?=null,var message:String?=null,var result:List<SchoolBoardData>?=null)
class SchoolBoardData(var ID:Int?=null,var Name:String?=null,var TranslatedName:String?=null)