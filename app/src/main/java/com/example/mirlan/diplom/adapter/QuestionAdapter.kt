package com.example.mirlan.diplom.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import com.example.mirlan.diplom.Activity.QuestionActivity
import com.example.mirlan.diplom.Activity.QuestionActivity.Companion.radioPositionIsChecked
import com.example.mirlan.diplom.Activity.QuestionActivity.Companion.resultArray
import com.example.mirlan.diplom.R
import com.example.mirlan.diplom.model.Answer
import com.example.mirlan.diplom.model.Question
import com.squareup.picasso.Picasso

class QuestionAdapter(private val questionList: ArrayList<Question>, private val answerList: List<Answer>):RecyclerView.Adapter<QuestionAdapter.ViewHolder>(){

    private val radioList = arrayListOf<String>()


    val context: Context = QuestionActivity.applicationContext()

    companion object {

     //   var sum:Int = 0

    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val pos: Int = position + 1

        radioPositionIsChecked[position] = 1
        holder.questionText?.text = ("" + pos + ") " + questionList[position].question)

        if(questionList[position].img!=null)
        Picasso.get().load(questionList[position].img).into(holder.img)
        else holder.img.visibility = View.GONE

        getAnswersForOnePosition(questionList[position].questionId)

        if (radioList[0] != "no") {
            holder.radio1?.text = radioList[0]
            holder.linear1.visibility = View.VISIBLE
        }
        if (radioList[1] != "no") {
            holder.radio2?.text = radioList[1]
            holder.linear2.visibility = View.VISIBLE
        }
        if (radioList[2] != "no") {
            holder.radio3.text = radioList[2]
            holder.linear3.visibility = View.VISIBLE
        }
        if (radioList[3] != "no") {
            holder.radio4.text = radioList[3]
            holder.linear4.visibility = View.VISIBLE
        }
        if (radioList[4] != "no") {
            holder.radio5.text = radioList[4]
            holder.linear5.visibility = View.VISIBLE
        }


        holder.radio1.setOnClickListener {

           // holder.radio1.isChecked = radioPositionIsChecked[position] == 1
            holder.radio2.isChecked = false//
           // Toast.makeText(context,"pos $position",Toast.LENGTH_LONG).show()
            holder.radio3.isChecked = false
            holder.radio4.isChecked = false
            holder.radio5.isChecked = false


            if(getRightAnswer(holder.questionText.text.removeRange(0,3),holder.radio1.text,position)){
                resultArray[position] = 1

            }else resultArray[position] = 0
           /// Toast.makeText(context,"position" + position + holder.radioGroup.checkedRadioButtonId,Toast.LENGTH_LONG).show()
           // getTestResult()

    }
        holder.radio2.setOnClickListener{
           // holder.radio2.isChecked = radioPositionIsChecked[position] == 1
            holder.radio1.isChecked = false
            holder.radio3.isChecked = false
            holder.radio4.isChecked = false
            holder.radio5.isChecked = false

            if(getRightAnswer(holder.questionText.text.removeRange(0,3), holder.radio2.text, position)){
                resultArray[position] = 1
            }else resultArray[position] = 0
           // Toast.makeText(context,"h"+holder.radioGroup.checkedRadioButtonId,Toast.LENGTH_SHORT).show()
            //getTestResult()
        }
        holder.radio3.setOnClickListener {

            holder.radio1.isChecked = false//
            holder.radio2.isChecked = false
            holder.radio4.isChecked = false
            holder.radio5.isChecked = false
            if(getRightAnswer(holder.questionText.getText().removeRange(0,3), holder.radio3.getText(), position)){
                resultArray[position] = 1
            }else resultArray[position] = 0
            //getTestResult()
        }
        holder.radio4.setOnClickListener{

            holder.radio1.isChecked = false// Toast.makeText(context,"SD" + checkedId,Toast.LENGTH_LONG).show()
            holder.radio2.isChecked = false
            holder.radio3.isChecked = false
            holder.radio5.isChecked = false
            if(getRightAnswer(holder.questionText.getText().removeRange(0,3), holder.radio4.getText(), position)){
                resultArray[position] = 1
            }else resultArray[position] = 0
            //getTestResult()
        }
        holder.radio5.setOnClickListener{

            holder.radio1.isChecked = false
            holder.radio2.isChecked = false
            holder.radio3.isChecked = false
            holder.radio4.isChecked = false
            if(getRightAnswer(holder.questionText.text.removeRange(0,3), holder.radio5.getText(), position)){
                resultArray[position] = 1
            }else resultArray[position] = 0
            //getTestResult()
        }
        //notifyItemChanged(position)
       // notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder{

        val view = LayoutInflater.from(parent?.context).inflate(R.layout.question_item,parent,false)

        return ViewHolder(view)
    }

    class ViewHolder(itemView: View):RecyclerView.ViewHolder(itemView){

        val questionText = itemView.findViewById<TextView>(R.id.question_text)!!
        val radioGroup = itemView.findViewById<RadioGroup>(R.id.radiogroup)!!
        val radio1 = itemView.findViewById<RadioButton>(R.id.radio1)!!
        val radio2 = itemView.findViewById<RadioButton>(R.id.radio2)!!
        val radio3 = itemView.findViewById<RadioButton>(R.id.radio3)!!
        val radio4 = itemView.findViewById<RadioButton>(R.id.radio4)!!
        val radio5 = itemView.findViewById<RadioButton>(R.id.radio5)!!
        val linear1 = itemView.findViewById<LinearLayout>(R.id.linear1)!!
        val linear2 = itemView.findViewById<LinearLayout>(R.id.linear2)!!
        val linear3 = itemView.findViewById<LinearLayout>(R.id.linear3)!!
        val linear4 = itemView.findViewById<LinearLayout>(R.id.linear4)!!
        val linear5 = itemView.findViewById<LinearLayout>(R.id.linear5)!!
        var img = itemView.findViewById<ImageView>(R.id.imgView)!!

    }
    override fun getItemCount(): Int {
        return questionList.size
    }

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }
    private fun getAnswersForOnePosition(questionId: Int) {

        var k = answerList.size
       // Toast.makeText(context,"K = " + questionList.size,Toast.LENGTH_LONG).show()
        k--
        var c = 0
        for(i in 0..5){
            radioList.add(i,"no")
        }
        for (i in 0..k) {
            if (answerList[i].questionId == questionId) {
                radioList.add(c, answerList[i].answer.toString())
                c++
            }
        }
    }
    private fun getRightAnswer(text: CharSequence, text1: CharSequence, position: Int): Boolean {
        var k = questionList.size
        var t = answerList.size
        var qId: Int
        var ansId:Int
        k--
        t--
        for(i in 0..k){
            if(questionList[i].question == text.toString()){
                 qId = questionList[i].questionId
                for(j in 0..t){
                    if(answerList[j].answer == text1.toString() && answerList[j].questionId == qId){
                        if(answerList[j].answer == text1.toString()){
                            ansId = answerList[j].answerId
                            initExamData(qId,ansId,position)
                        }
                        if(answerList[j].right == 1)return true
                    }
                }
            }
        }

        return false
    }

    private fun initExamData(qId: Int, ansId: Int,position: Int) {

       QuestionActivity.resultList[position] = "i:"+qId.toString() + ",i:" + ansId.toString() + ";\n"

    }





}