package com.auro.application.home.data.model

data class DistrictDataModel(var status:String?=null,var error:String?=null,var message:String?=null,var districts:List<DistrictData>?=null)
class DistrictData(var district_name:String?=null,var district_id:String?=null)