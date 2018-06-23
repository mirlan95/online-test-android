package com.example.mirlan.diplom.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by mirlan on 5/14/18.
 */
class Answer {
    @SerializedName("answer_id")
    @Expose
    var answerId: Int = 0

    @SerializedName("answer")
    @Expose
    var answer: String? = null

    @SerializedName("right_ok")
    @Expose
    var right: Int = 0

    @SerializedName("question_id")
    @Expose
    var questionId: Int = 0
}