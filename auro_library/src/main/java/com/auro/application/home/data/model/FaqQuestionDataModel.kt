package com.auro.application.home.data

data class FaqQuestionDataModel(var status:String?=null,var error:String?=null,var FaqList:List<FaqQuesData>?=null)
class FaqQuesData(var faq_cat:String?=null,var FAQID:Int?=null,var question:String?=null,var answer:String?=null,var TransalatedQuestion:String?=null,var TransalateAnswer:String?=null)