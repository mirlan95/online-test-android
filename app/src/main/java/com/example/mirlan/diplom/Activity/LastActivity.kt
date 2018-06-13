package com.example.mirlan.diplom.Activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.widget.TextView
import android.widget.Toast
import com.example.mirlan.diplom.R
import kotlinx.android.synthetic.main.activity_last.*

class LastActivity : AppCompatActivity() {

    private var examResult:Int=0

    private var stNumber:String? = null
    private var mExamName:String? = null
    private var lastName:String? = null
    private var firstName:String? = null

    private lateinit var lastNameText: TextView
    private lateinit var firstNameText: TextView
    private lateinit var examNameText: TextView
    private lateinit var numberText: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_last)

        examResult = intent.getIntExtra("res",1)
        lastName = intent.getStringExtra("lastName")
        firstName = intent.getStringExtra("firstName")
        stNumber = intent.getStringExtra("number")
        mExamName = intent.getStringExtra("examName")

        examNameText = findViewById(R.id.examName_text)
        lastNameText = findViewById(R.id.lastName_text)
        firstNameText = findViewById(R.id.firstName_text)
        numberText = findViewById(R.id.number_text)

        examNameText.text = "Эгзамендин аты: $mExamName"
        lastNameText.text = "Фамилиясы: $lastName"
        firstNameText.text = "Аты: $firstName"
        numberText.text = "Студенттик номери: $stNumber"

        result_text.text = "Your result:$examResult"

    }

    override fun onBackPressed() {

        val alert =  AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, {
                    _, _ ->
                    super.onBackPressed()
                    finishAffinity()
                    System.exit(0)
                })
        alert.show()
    }

}
