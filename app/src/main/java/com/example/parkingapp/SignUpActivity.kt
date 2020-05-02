package com.example.parkingapp

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.gson.Gson
import com.google.gson.annotations.SerializedName
import kotlinx.android.synthetic.main.activity_sign_up.*
import java.lang.Boolean.FALSE
import java.lang.Boolean.TRUE
import java.sql.Types.NULL

data class Json_Base (

    @SerializedName("sr_id") val sr_id : Int,
    @SerializedName("group_id") val group_id : Int,
    @SerializedName("user_id") val user_id : String,
    @SerializedName("firstname") val firstname : String,
    @SerializedName("lastname") val lastname : String,
    @SerializedName("phone_no") val phone_no : String,
    @SerializedName("email") val email : String,
    @SerializedName("user_type") val user_type : String,
    @SerializedName("reg_date") val reg_date : String
)

class SignUpActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        val sharedPreferences = getSharedPreferences("SP_INFO", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        if(sharedPreferences.getBoolean("LOGGED_IN", FALSE)) {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        buttonSubmitUserDetails.setOnClickListener {
            val account = GoogleSignIn.getLastSignedInAccount(this)
            if (account != null) {
                val personEmail: String? = account.email
                val myUrl = getString(R.string.main_url) + "/users/" + personEmail
                val myMethod = "GET"
                var response = getAPIData(myUrl, myMethod)
                var obj = Gson().fromJson(response, Json_Base::class.java)

                Toast.makeText(this,response.toString() + "ddd",Toast.LENGTH_LONG)
                editor.putBoolean("LOGGED_IN", TRUE)
                editor.apply()
                editor.commit()

            }
        }
    }


    private fun getAPIData(myUrl: String, myMethod: String): String {
        var myResponse : String = ""
        try {
            val task = MyAsyncTaskSignUp(this)
            //task.execute(myUrl)
            var testResponse = task.execute(myUrl, myMethod)
            Toast.makeText(this, testResponse.get().toString(), Toast.LENGTH_SHORT).show()
            myResponse = testResponse.get().toString()
        }
        catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()
        }

        return myResponse
    }
}
