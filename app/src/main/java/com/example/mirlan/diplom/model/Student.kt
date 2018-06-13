package com.example.mirlan.diplom.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class Student {
    @SerializedName("student_id")
    @Expose
    var answerId: Int = 0
}