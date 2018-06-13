package com.example.mirlan.diplom.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StatusBack {
    @SerializedName("message")
    @Expose
    var msg: String? = null
}