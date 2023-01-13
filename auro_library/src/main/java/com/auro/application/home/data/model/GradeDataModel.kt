package com.auro.application.home.data.model

data class GradeDataModel(var status:String?=null,var error:String?=null,var result:List<GradeData>?=null)
class GradeData(var grade_id:String?=null)