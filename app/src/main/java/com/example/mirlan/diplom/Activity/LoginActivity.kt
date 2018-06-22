package com.example.mirlan.diplom.Activity

import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.example.mirlan.diplom.R
import com.example.mirlan.diplom.api.ApiInterface
import com.example.mirlan.diplom.utils.NetworkConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

@Suppress("DEPRECATION")
class LoginActivity : AppCompatActivity() {

    private lateinit var mUserName:EditText
    private lateinit var mPassword:EditText
    private var mCompositeDisposable: Disposable? = null
    private var mExamId:Int = 1
    private var mExamTime:Int = 50
    private var mExamName:String?=null

    private val client by lazy {
        ApiInterface.create()
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mExamId = intent.getIntExtra("key",1)
        mExamTime = intent.getIntExtra("time",50)
        mExamName =intent.getStringExtra("examName")

        mPassword = findViewById(R.id.edittext_password)
        mUserName = findViewById(R.id.edittext_username)

        findViewById<Button>(R.id.button_login)?.setOnClickListener{

            if(isUserNameValid() && mPassword.text.toString().isNotEmpty()){

                displayProgressDialog()
                logIn()

            }else if(!isUserNameValid()) {

                mUserName.error = "Invalid username!"

            }else {
                mPassword.error = "Password is empty!"
            }
        }
    }

    private fun logIn() {

        if(!NetworkConnection.isNetworkConnected(this))
            Toast.makeText(this,"Check your connection!",Toast.LENGTH_LONG).show()
        mCompositeDisposable = client.logIn(mUserName.text.toString(),mPassword.text.toString(),mExamId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(
                        { result ->

                            if (pDialog.isShowing)
                                pDialog.dismiss()

                            if(!result[0].err){
                            val intent = Intent(this, QuestionActivity::class.java)
                                intent.putExtra("key",mExamId)
                                intent.putExtra("time",mExamTime)
                                intent.putExtra("number",mUserName.text.toString())
                                intent.putExtra("id", result[0].studentId)
                                intent.putExtra("lastName",result[0].lastName)
                                intent.putExtra("firstName",result[0].firstName)
                                intent.putExtra("examName",mExamName)
                                startActivity(intent)}
                            else  Toast.makeText(this,"Invalid password or username!",Toast.LENGTH_LONG).show()
                        },
                        {
                            if (pDialog.isShowing)
                                pDialog.dismiss()

                            Toast.makeText(this,"Invalid password or username!",Toast.LENGTH_LONG).show()
                        })

    }

    private fun isUserNameValid(): Boolean {
        if(mUserName.length() == 10)return true
        return false
    }
    private lateinit var pDialog: ProgressDialog
    private fun displayProgressDialog() {

        pDialog = ProgressDialog(this@LoginActivity)
        pDialog.setMessage("Loading..")
        pDialog.setCancelable(false)
        pDialog.isIndeterminate = false
        pDialog.show()
    }

    override fun onPause() {
        super.onPause()
        mCompositeDisposable?.dispose()
    }

}
