package com.example.mirlan.diplom.Activity

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.CountDownTimer
import android.support.v7.app.AlertDialog
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.example.mirlan.diplom.R
import com.example.mirlan.diplom.adapter.QuestionAdapter
import com.example.mirlan.diplom.api.ApiInterface
import com.example.mirlan.diplom.model.Answer
import com.example.mirlan.diplom.model.Question
import com.example.mirlan.diplom.utils.NetworkConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_question.*
import kotlinx.android.synthetic.main.recycler_item_row.*
import kotlinx.coroutines.experimental.delay
import kotlinx.coroutines.experimental.launch
import java.util.concurrent.TimeUnit
import kotlin.collections.ArrayList

class QuestionActivity : AppCompatActivity() {

    private var examId:Int = 1
    private var examTime:Int = 50
    private var sum:Int = 0

    private var stNumber:String? = null
    private var mExamName:String? = null
    private var lastName:String? = null
    private var firstName:String? = null

    private lateinit var sendResultBtn:Button
    private lateinit var prevButton:Button
    private lateinit var nextButton:Button

    private lateinit var lastNameText:TextView
    private lateinit var firstNameText:TextView
    private lateinit var examNameText:TextView
    private lateinit var numberText:TextView

    private var questionList: ArrayList<Question>? = null
    private var answerList: ArrayList<Answer>? =null
    private var questionAdapter:QuestionAdapter? = null
    private var studentId:Int = 0

    private var layoutManager: LinearLayoutManager? = null

    private var mCompositeDisposable: CompositeDisposable? = null
    private var mCompositeDisposable2: CompositeDisposable? = null
    private var mCompositeDisposable3: Disposable? = null

    init {
        instance = this
    }
    private val client by lazy {
        ApiInterface.create()
    }

    companion object {

        var resultArray = arrayListOf<Int>()
        var resultList = arrayListOf<String>()
        @SuppressLint("StaticFieldLeak")
        private var instance: QuestionActivity? = null

        fun applicationContext() : Context {
            return instance!!.applicationContext
        }
    }
    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_question)

        mCompositeDisposable = CompositeDisposable()
        mCompositeDisposable2 = CompositeDisposable()

        examId = intent.getIntExtra("key",1)
        examTime = intent.getIntExtra("time",50)
        studentId = intent.getIntExtra("id",1)
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

        sendResultBtn = findViewById(R.id.btn_send)
        prevButton = findViewById(R.id.btn_prev)
        nextButton = findViewById(R.id.btn_next)
        //init recyclerView

        initRecyclerView()

        //progresDialog
        DisplayProgressDialog()

        getQuestion()

        sendResultBtn.setOnClickListener{

            getTestResult()
            showDialog()

        }
        nextButton.setOnClickListener{

            recyclerView_question.layoutManager.scrollToPosition(layoutManager?.findLastVisibleItemPosition()!! + 1)

        }
        prevButton.setOnClickListener{
            recyclerView_question.layoutManager.scrollToPosition(layoutManager?.findFirstVisibleItemPosition()!! - 1)

        }
    }
    private fun initRecyclerView(){

        recyclerView_question.setHasFixedSize(true)
        layoutManager = LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        recyclerView_question.layoutManager = layoutManager
    }
    private fun getQuestion(){

        //if(!NetworkConnection.isNetworkConnected(this))// getQuestion()

        mCompositeDisposable?.add(ApiInterface.create().getQuestions(examId)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError))

    }

    private fun handleResponse(mQuestionList: List<Question>){

        questionList = ArrayList(mQuestionList)
        getAnswer(mQuestionList)
        setNullArray()
       // Toast.makeText(this,"QUESTIONLIST " + mQuestionList.size.toString(),Toast.LENGTH_LONG).show()

    }

    private fun handleError(errorThrowable: Throwable){

        pDialog.dismiss()
        Toast.makeText(this, "Error  ${errorThrowable.localizedMessage} $examId", Toast.LENGTH_SHORT).show()

    }

    private fun getAnswer(mQuestionList: List<Question>) {

        val listOfInt = arrayListOf<Int>()
        var k = mQuestionList.size
        k--
        for (i in 0..k) {
            listOfInt.add(i, mQuestionList[i].questionId)
           // Toast.makeText(this,"getAnswerFun" + listOfInt[i].toString(),Toast.LENGTH_LONG).show()
        }
        listOfInt.add(k+1,0)
        // if(!NetworkConnection.isNetworkConnected(this)) getAnswer()

        mCompositeDisposable2?.add(ApiInterface.create().getAnswers(listOfInt)
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::answerResponse, this::answerError))
    }

    private fun answerResponse(answerArray: List<Answer>){

       // Toast.makeText(this,"ANSWERLIST " + answerArray.size.toString(),Toast.LENGTH_LONG).show()
        answerList = ArrayList(answerArray)
        questionAdapter = QuestionAdapter(questionList!!,answerList!!)
        recyclerView_question.adapter = questionAdapter
        //start timer
        pDialog.dismiss()
        startTime(examTime.toLong())
        btn_send.visibility = View.VISIBLE

    }
    private fun answerError(errorThrowable: Throwable) {

        pDialog.dismiss()
        Toast.makeText(this, "Error ${errorThrowable.localizedMessage}", Toast.LENGTH_SHORT).show()

    }

    private fun sendResult(){
    val testResult:Int = (100 / questionList?.size!!) * sum
        //if(testResult==0)testResult = -1
        var examData:String?=null
        examData = "a:" + studentId.toString() + ":{\n" +
                "s:16:'focusLostCounter' \n" +
                "s:1:'0'\n"+
                "s:14:'focusLostTimer'\n" +
                "s:1:'0'\n"
        for(i in 0..questionList!!.size){
            examData += resultList[i]
        }

        examData+="}"
      //  examData+=resultArray.toString()

                mCompositeDisposable3 = client.sendResult(testResult,studentId,examId,examData)//QuestionAdapter.resultAnswer.toString())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe({
                    pDialog.dismiss()
                    val intent = Intent(this, LastActivity::class.java)
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
                    intent.putExtra("res",testResult)
                    intent.putExtra("number",stNumber)
                    intent.putExtra("lastName",lastName)
                    intent.putExtra("firstName",firstName)
                    intent.putExtra("examName",mExamName)
                    startActivity(intent)

                },{
                    toast("Again")
                })
    }
    private fun setNullArray(){
        val len:Int = questionList!!.size
        for(i in 0..len) {
            resultArray.add(i,0)
            resultList.add(i,"")

        }
    }

    private fun showDialog(){
        // Late initialize an alert dialog object
        lateinit var dialog: AlertDialog


        // Initialize a new instance of alert dialog builder object
        val builder = AlertDialog.Builder(this)

        // Set a title for alert dialog
        builder.setTitle("Сынакты бутуруу")

        // Set a message for alert dialog
        builder.setMessage("Эгер сынакты бутургон болсонуз YES баскычын басыныз")

        // On click listener for dialog buttons
        val dialogClickListener = DialogInterface.OnClickListener{ _, which ->
            when(which){
                DialogInterface.BUTTON_POSITIVE -> {
                    //toast("Жооптор базага жонотулду!")
                    pDialog.show()
                    sendResult()
                }
                DialogInterface.BUTTON_NEGATIVE -> toast("Back")
                DialogInterface.BUTTON_NEUTRAL -> toast("Cancel")
            }
        }

        // Set the alert dialog positive/yes button
        builder.setPositiveButton("YES",dialogClickListener)

        // Set the alert dialog negative/no button
        builder.setNegativeButton("NO",dialogClickListener)

        // Set the alert dialog neutral/cancel button
        builder.setNeutralButton("CANCEL",dialogClickListener)

        // Initialize the AlertDialog using builder object
        dialog = builder.create()

        // Finally, display the alert dialog
        dialog.show()
    }

    private fun Context.toast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun startTime(examTim: Long) {

         var min = examTim - 1
         var examTime = examTim
         examTime *= 60000
         var c = 0
         var t = 60
         val timer = object: CountDownTimer(examTime,1000){

             override fun onTick(millisUntilFinished: Long) {
                   c++
                   if(c==60) {
                       min--
                       c = 0
                       t = 60
                   }
                 millisUntilFinished / 1000
                 t--
                   timer_text.text = (" " + min + "min:" + " sec:"  + t)
             }

             override fun onFinish() {
                 pDialog.show()
                 sendResult()
             }

         }
        timer.start()
    }
    private fun startOnstopTimer(){
        val timer = object: CountDownTimer(5000,1000){
            override fun onFinish() {
                sendResult()
            }

            override fun onTick(millisUntilFinished: Long) {
            millisUntilFinished / 1000
            }

        }
        timer.start()
    }
    private fun getTestResult() {

        var len = questionList!!.size
        len--
        sum = 0
        for (i in 0..len) sum += resultArray[i]
        //  Toast.makeText(context,"sum = $sum",Toast.LENGTH_SHORT).show()
    }

    lateinit var pDialog: ProgressDialog
    private fun DisplayProgressDialog() {

        pDialog = ProgressDialog(this@QuestionActivity)
        pDialog.setMessage("Loading..")
        pDialog.setCancelable(false)
        pDialog.isIndeterminate = false
        pDialog.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        Toast.makeText(this,"onDestroy",Toast.LENGTH_LONG).show()
        sendResult()
        mCompositeDisposable?.clear()
        mCompositeDisposable2?.clear()

    }

    override fun onPause() {
        super.onPause()
        Toast.makeText(this,"onPause",Toast.LENGTH_LONG).show()
        //sendResult()
        //mCompositeDisposable3!!.dispose()
        Log.e("ONSTOP","pause")
    }

    override fun onResume() {


        Toast.makeText(this,"onResume",Toast.LENGTH_LONG).show()
       // sendResult()
        super.onResume()

    }

    override fun onStop() {
        Toast.makeText(this,"onStop",Toast.LENGTH_LONG).show()
        super.onStop()
        startOnstopTimer()
        //endResult()
        //super.finishAffinity()
        //super.finish()
       //super.finishAct
    }

    override fun onBackPressed() {

        val alert =  AlertDialog.Builder(this)
                .setTitle("Really Exit?")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes) { _, _ ->
                    pDialog.show()
                    sendResult()
                    super.onBackPressed()
                    finishAffinity()
                    System.exit(0)
                }
        alert.show()
    }
}




