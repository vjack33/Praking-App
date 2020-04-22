package com.example.parkingapp

import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.gms.auth.api.signin.GoogleSignIn
import kotlinx.android.synthetic.main.activity_home.*
import java.sql.Types.NULL
import kotlin.concurrent.thread


class HomeActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var viewAdapter: RecyclerView.Adapter<*>
    private lateinit var viewManager: RecyclerView.LayoutManager


    override fun onStart() {
        super.onStart()
        val account = GoogleSignIn.getLastSignedInAccount(this)
        if (account != null) {
            val personName: String? = account.displayName
            val personGivenName: String? = account.givenName
            val personFamilyName: String? = account.familyName
            val personEmail: String? = account.email
            val personId: String? = account.id
            val personPhoto: Uri? = account.photoUrl
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
            var customerName = editTextCustomerName.text
            var customerPhoneNo = editTextcustomerPhoneNo.text
            var customerCarNo = editTextCustomerCarNo.text
            var customerLicense = editTextCustomerLicense.text
            val myUrl = "http://192.168.1.100/parking_app/create_job.php?" +
                    "group_id=121&user_id=1&" +
                    "customer_name=" + customerName +
                    "&customer_phone=" + customerPhoneNo +
                    "&customer_car_no=" + customerCarNo +
                    "&customer_license=" + customerLicense
            getAPIData(myUrl)
        }
    }

    private fun getAPIData(myUrl : String) {
        //val myUrl = "http://192.168.1.103/parking_app/api.py"
        try {
            val task = MyAsyncTask(this)
            //task.execute(myUrl)
            var testResponse = task.execute(myUrl)
            Toast.makeText(this, testResponse.get().toString(), Toast.LENGTH_SHORT).show()
        }
        catch (e: InterruptedException) {
            e.printStackTrace()
            Toast.makeText(this, "ERROR", Toast.LENGTH_SHORT).show()
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

