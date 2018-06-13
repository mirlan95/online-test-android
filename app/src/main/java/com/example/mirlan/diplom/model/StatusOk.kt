package com.example.mirlan.diplom.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class StatusOk{
    @SerializedName("error")
    @Expose
    var err: Boolean = false

    @SerializedName("message")
    @Expose
    var msg: String ?= null

    @SerializedName("student_id")
    @Expose
    var studentId: Int = 0

    @SerializedName("number")
    @Expose
    var studentNumber: String? = null

    @SerializedName("first_name")
    @Expose
    var firstName: String? = null

    @SerializedName("last_name")
    @Expose
    var lastName: String? = null
}