package com.example.quizapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.example.quizapp.Constant
import com.example.quizapp.Model.ListItem
import com.example.quizapp.Network.Apiinterface
import com.example.quizapp.databinding.ActivityQuestionactBinding
import retrofit.Call
import retrofit.GsonConverterFactory
import retrofit.Retrofit

class questionact : AppCompatActivity() {

    private var binding : ActivityQuestionactBinding? = null

    var cat : String = "SQL";
    var diff : String = "Easy";
    val lim : Int = 10;
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityQuestionactBinding.inflate(layoutInflater)
        setContentView(binding?.root)

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

        }
    }

    override fun onDestroy() {
        binding = null
        super.onDestroy()
    }
}