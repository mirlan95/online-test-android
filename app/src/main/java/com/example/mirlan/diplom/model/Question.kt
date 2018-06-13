package com.example.mirlan.diplom.model

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

/**
 * Created by mirlan on 5/14/18.
 */
 class Question {
    @SerializedName("question_id")
    @Expose
    var questionId: Int = 0

    @SerializedName("lesson_id")
    @Expose
    var lessonId: Int = 0

    @SerializedName("theme_id")
    @Expose
    var themeId: Int = 0

    @SerializedName("question")
    @Expose
    var question: String? = null

    @SerializedName("image")
    @Expose
    var image: String?= null
   @SerializedName("img")
   @Expose
   var img: String?= null

    @SerializedName("level")
    @Expose
    var level: Int = 0
   override fun toString(): String {
      return questionId.toString()
   }
}

