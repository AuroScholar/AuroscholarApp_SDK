package com.auro.application.home.data

data class FaqCategoryDataModel(var status:String?=null,var error:String?=null,var FaqCategoryList:List<FaqCatData>?=null)
class FaqCatData (var CategoryId:Int?=null,var CategoryName:String?=null,var TranslateCategoryName:String?=null, var isChecked:Boolean?=false)