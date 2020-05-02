package com.example.parkingapp

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.android.synthetic.main.activity_home.*


class HomeActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onStart() {
        super.onStart()
        /*val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            val personName: String? = account.displayName
            val personGivenName: String? = account.givenName
            val personFamilyName: String? = account.familyName
            val personEmail: String? = account.email
            val personId: String? = account.id
            val personPhoto: Uri? = account.photoUrl
        }*/

        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            val personName: String? = account.displayName
            val personGivenName: String? = account.givenName
            val personFamilyName: String? = account.familyName
            val personEmail: String? = account.email
            val personId: String? = account.id
            val personPhoto: Uri? = account.photoUrl
            try {
                val myUrl = "http://192.168.43.100/users/" + personEmail +"?" +
                        "group_id=121&" +
                        "user_id=" + personEmail +
                        "&firstname=" + personGivenName +
                        "&lastname=" + personFamilyName
                var  myMethod = "POST"

                getAPIData(myUrl, myMethod)
            }
            catch (e: Exception) {
                Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show()
            }

        }
    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        updateUI()

        test_button.setOnClickListener {
            val intent = Intent(this, SignOutActivity::class.java)
            startActivity(intent)
        }

        buttonAddCustomer.setOnClickListener {
            val account = GoogleSignIn.getLastSignedInAccount(this)
            if (account != null) {
                val personName: String? = account.displayName
                val personGivenName: String? = account.givenName
                val personFamilyName: String? = account.familyName
                val personEmail: String? = account.email
                val personId: String? = account.id
                val personPhoto: Uri? = account.photoUrl
                try {
                    val myUrl = "http://192.168.5.100/users/" + personEmail +"?" +
                            "group_id=121&" +
                            "user_id=" + personEmail +
                            "&firstname=" + personGivenName +
                            "&lastname=" + personFamilyName
                    val myMethod = "POST"

                    getAPIData(myUrl, myMethod)
                }
                catch (e: Exception) {
                    Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show()
                }

            }
        }
    }

    private fun getAPIData(myUrl: String, myMethod: String) {
        //val myUrl = "http://192.168.1.103/parking_app/api.py"
        try {
            val task = MyAsyncTaskHome(this)
            //task.execute(myUrl)
            var testResponse = task.execute(myUrl,myMethod)
            Toast.makeText(this, testResponse.get().toString(), Toast.LENGTH_SHORT).show()
        }
        catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()
        }

    }


    private fun updateUI() {
        viewManager = LinearLayoutManager(this)
        viewAdapter = RecyclerAdapter()

        recyclerView = findViewById<RecyclerView>(R.id.my_recycler_view).apply {
            setHasFixedSize(true)
            layoutManager = viewManager
            adapter = viewAdapter

        }

    }

}

