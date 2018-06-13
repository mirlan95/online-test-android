package com.example.mirlan.diplom.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by mirlan on 5/13/18.
 */
//data class Exam(val name : String, val version : String, val apiLevel : String)
class Exam{
    @SerializedName("exam_id")
    @Expose
    var examId: Int = 0

    @SerializedName("lesson_id")
    @Expose
    var lessonId: Int = 0

    @SerializedName("exam")
    @Expose
    var exam: String?= null

    @SerializedName("data")
    @Expose
    var date: String?=null

    @SerializedName("time")
    @Expose
    var time: Int = 0

    @SerializedName("enabled")
    @Expose
    var enabled: Int = 0
}
