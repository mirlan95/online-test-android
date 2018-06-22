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

    private var mStudentNumber:String? = null
    private var mExamName:String? = null
    private var mLastName:String? = null
    private var mFirstName:String? = null

    private lateinit var lastNameText: TextView
    private lateinit var firstNameText: TextView
    private lateinit var examNameText: TextView
    private lateinit var numberText: TextView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_last)

        examResult = intent.getIntExtra("res",1)
        mLastName = intent.getStringExtra("lastName")
        mFirstName = intent.getStringExtra("firstName")
        mStudentNumber = intent.getStringExtra("number")
        mExamName = intent.getStringExtra("examName")

        examNameText = findViewById(R.id.examName_text)
        lastNameText = findViewById(R.id.lastName_text)
        firstNameText = findViewById(R.id.firstName_text)
        numberText = findViewById(R.id.number_text)

        examNameText.text = mExamName
        lastNameText.text = mLastName
        firstNameText.text = mFirstName
        numberText.text = mStudentNumber
        result_text.text = examResult.toString()

    }

    override fun onBackPressed() {

        val alert =  AlertDialog.Builder(this)
                .setTitle("Тиркемени жабуу")
                .setMessage("Сиз чын эле тиркемени жабууну каалайсызбы?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    super.onBackPressed()
                    finishAffinity()
                    System.exit(0)
                }
        alert.show()
    }

}
