package com.auro.application.home.data.model

data class StateDataModel(var status:String?=null,var error:String?=null,var message:String?=null,var states:List<StateData>?=null)

class StateData(var state_name:String?=null,var state_id:String?=null)