package com.yugasa.yubokotlinsdk.formbuilder

import android.widget.LinearLayout




class FormElement {

    enum class Type {
        TEXT, TEXTVIEW, EMAIL, PASSWORD, PHONE, NUMBER, URL, SPINNER, ZIP, SELECTION, MULTIPLE_SELECTION, DATE, TIME
    }

    private val tag: String? = null
    private val type: Type? = null
    private val title: String? = null
    private val value: String? = null
    private val hint: String? = null
    private val options: List<String>? = null// list of options for single and multi picker
    private val optionsSelected: List<String>? = null // list of selected options for single and multi picker
    private val isRequired = false
    private val isEnabled = false
    private val dateFormat: String? = null
    private val timeFormat: String? = null
    private val dateTimeFormat: String? = null
    private val params: LinearLayout.LayoutParams? = null
//    private val formValidation: FormValidation? = null
    private val errorMessage: String? = null
    private val isSingleLine = false



}