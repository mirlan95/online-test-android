package com.example.mirlan.diplom.Activity


import android.app.ProgressDialog
import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.View
import android.widget.Button
import android.widget.Toast
import com.example.mirlan.diplom.R
import com.example.mirlan.diplom.R.id.recyclerView
import com.example.mirlan.diplom.adapter.ExamAdapter
import com.example.mirlan.diplom.api.ApiInterface
import com.example.mirlan.diplom.model.Exam
import com.example.mirlan.diplom.utils.NetworkConnection
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.schedulers.Schedulers
import kotlinx.android.synthetic.main.activity_login.view.*
import kotlinx.android.synthetic.main.activity_main.*

@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(),ExamAdapter.Listener {

    private var mCompositeDisposable: CompositeDisposable? = null

    private var mExamArrayList: ArrayList<Exam>? = null

    private var mExamAdapter: ExamAdapter? = null

    private lateinit var mInternetBtn:Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        mInternetBtn = findViewById(R.id.internet_btn)

        mCompositeDisposable = CompositeDisposable()

        initRecyclerView()

        displayProgressDialog()

        getExamDate()

        mInternetBtn.setOnClickListener {

            pDialog.show()
            getExamDate()

        }

    }

    private fun initRecyclerView(){

        recyclerView.setHasFixedSize(true)
        val layoutManager : RecyclerView.LayoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
    }

    private fun getExamDate() {

        if (!NetworkConnection.isNetworkConnected(this)) {

            Toast.makeText(this, "Error Internet connection!", Toast.LENGTH_SHORT).show()
            recyclerView.visibility = View.GONE
            mInternetBtn.visibility = View.VISIBLE
            pDialog.dismiss()
        } else{
            recyclerView.visibility = View.VISIBLE
            mInternetBtn.visibility = View.GONE

        }

        val apiInterface = ApiInterface.create()

        mCompositeDisposable?.add(apiInterface.getExam()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribeOn(Schedulers.io())
                .subscribe(this::handleResponse,this::handleError))

    }

    private fun handleResponse(mExamList: List<Exam>){

        pDialog.dismiss()

        mExamArrayList  = ArrayList(mExamList)
        mExamAdapter = ExamAdapter(mExamArrayList!!,this)
        recyclerView.adapter = mExamAdapter

    }

    private fun handleError(errorThrowable: Throwable){

        pDialog.dismiss()
        recyclerView.visibility = View.GONE
        mInternetBtn.visibility = View.VISIBLE
        Toast.makeText(this, "Error ${errorThrowable.localizedMessage}", Toast.LENGTH_SHORT).show()
    }

    override fun onItemClick(mExam: Exam) {

        val intent = Intent(this, LoginActivity::class.java)
        intent.putExtra("key",mExam.examId)
        intent.putExtra("time",mExam.time)
        intent.putExtra("examName",mExam.exam)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        mCompositeDisposable?.clear()
    }

    lateinit var pDialog: ProgressDialog
    private fun displayProgressDialog() {

        pDialog = ProgressDialog(this@MainActivity)
        pDialog.setMessage("Loading..")
        pDialog.setCancelable(false)
        pDialog.isIndeterminate = false
        pDialog.show()
    }
}

