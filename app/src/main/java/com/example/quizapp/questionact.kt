package com.example.quizapp

import android.app.Dialog
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.annotation.RequiresApi
import com.example.quizapp.Constant
import com.example.quizapp.Model.ListItem
import com.example.quizapp.Network.Apiinterface
import com.example.quizapp.databinding.ActivityQuestionactBinding
import retrofit.*

class questionact : AppCompatActivity() {

    private var binding : ActivityQuestionactBinding? = null
    private var mProgressDialog : Dialog? = null


    var cat : String = "SQL";
    var diff : String = "Easy";
    val lim : Int = 10;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionactBinding.inflate(layoutInflater)
        setContentView(binding?.root)

        getquestiondata()
    }

    private fun getquestiondata(){
        if (Constant.isNetworkAvailable(this@questionact)) {

            val retrofit : Retrofit = Retrofit.Builder()
                .baseUrl(Constant.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            val services : Apiinterface = retrofit.create(Apiinterface::class.java)

            val questionlist : Call<ArrayList<ListItem>> = services.getquestion(
                Constant.APP_ID,
                cat, diff, lim
            )

            showCustomProgressDialog()

            questionlist.enqueue(object : Callback<ArrayList<ListItem>> {
                @RequiresApi(Build.VERSION_CODES.N)
                override fun onResponse(
                    response: Response<ArrayList<ListItem>>?,
                    retrofit: Retrofit?)
                {
                    if (response!!.isSuccess) {
                        hideProgressDialog()
                        binding?.tvQuestion?.text = response.body()?.get(0)?.question
                        binding?.tvOptionOne?.text = response.body()?.get(0)?.answers?.answer_a
                        binding?.tvOptionTwo?.text = response.body()?.get(0)?.answers?.answer_b
                        binding?.tvOptionThree?.text = response.body()?.get(0)?.answers?.answer_c
                        binding?.tvOptionFour?.text = response.body()?.get(0)?.answers?.answer_d

                        binding?.explain?.text = response.body()?.get(0)?.correct_answers?.answer_a_correct +" "+
                                response.body()?.get(0)?.correct_answers?.answer_b_correct+" " +
                                response.body()?.get(0)?.correct_answers?.answer_c_correct+ " " +
                                response.body()?.get(0)?.correct_answers?.answer_d_correct
                    } else {
                        // If the response is not success then we check the response code.
                        val sc = response.code()

                        when (sc) {
                            400 -> {
                                Log.e("Error 400", "Bad Request")
                            }
                            404 -> {
                                Log.e("Error 404", "Not Found")
                            }
                            else -> {
                                Log.e("Error", "Generic Error")
                            }
                        }
                    }
                }

                override fun onFailure(t: Throwable?) {
                    TODO("Not yet implemented")
                }
            })
        } else {
                Toast.makeText(
                    this@questionact,
                    "No internet connection available.",
                    Toast.LENGTH_SHORT
                ).show()
            }
    }
    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }

    private fun showCustomProgressDialog() {
        mProgressDialog = Dialog(this)

        mProgressDialog!!.setContentView(R.layout.dialog_custome_progress)

        //Start the dialog and display it on screen.
        mProgressDialog!!.show()
    }
    private fun hideProgressDialog() {
        if (mProgressDialog != null) {
            mProgressDialog!!.dismiss()
        }
    }

}