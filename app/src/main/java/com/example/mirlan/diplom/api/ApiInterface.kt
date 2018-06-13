package com.example.mirlan.diplom.api

import com.example.mirlan.diplom.model.*
import io.reactivex.Observable
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

/**
 * Created by mirlan on 5/13/18.
 */
public interface ApiInterface {

    @GET("/exams")
    fun getExam() : Observable<List<Exam>>

    @GET("/questions")
    //@FormUrlEncoded
    fun getQuestions(@Query("exam_id")examId: Int):Observable<List<Question>>

    @GET("/answers")
   // @FormUrlEncoded
    fun getAnswers(@Query("arr") arr: ArrayList<Int>?): Observable<List<Answer>>

    @POST("/login")
    @FormUrlEncoded
    fun logIn(@Field("username") mUserName: String,
              @Field("password") mPassword: String,
              @Field("exam_id") mExamId: Int) :Observable<List<StatusOk>>

    @POST("/result")
    @FormUrlEncoded
    fun sendResult(@Field("result") id: Int,
                   @Field("student_id") stuId:Int,
                   @Field("exam_id")exId:Int,
                   @Field("examData") exDate: String ) : Observable<StatusBack>

    companion object Factory{

        fun create(): ApiInterface{
            val requestInterface = Retrofit.Builder()
                    .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                    .addConverterFactory(GsonConverterFactory.create())
                    .baseUrl("http://192.168.1.123:8080")
                    .build()
            return requestInterface.create(ApiInterface::class.java)
        }

    }

}