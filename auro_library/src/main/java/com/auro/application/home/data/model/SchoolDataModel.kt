package com.auro.application.home.data.model

data class SchoolDataModel(var status:String?=null,var error:String?=null,var message:String?=null,var schools:List<SchoolData>?=null)

class SchoolData(var SCHOOL_NAME:String?=null,var ID:Int?=null)