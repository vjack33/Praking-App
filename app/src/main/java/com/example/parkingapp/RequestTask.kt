package com.example.parkingapp

import android.app.ProgressDialog
import android.content.Context
import android.os.AsyncTask
import android.view.View
import android.widget.Toast
import kotlinx.android.synthetic.main.activity_home.*
import java.io.BufferedReader
import java.io.InputStreamReader
import java.lang.ref.WeakReference
import java.net.HttpURLConnection
import java.net.URL
import java.util.*


class MyAsyncTaskHome internal constructor(context: HomeActivity) : AsyncTask<String, String, String?>() {

    private var resp: String? = null
    private val activityReference: WeakReference<HomeActivity> = WeakReference(context)

    override fun onPreExecute() {
        val activity = activityReference.get()
        if (activity == null || activity.isFinishing) return
        activity.progressBar.visibility = View.VISIBLE
    }

    override fun doInBackground(vararg params: String?): String? {
        //publishProgress("Connecting...") // Calls onProgressUpdate()
        try {
            val myUrl = URL(params[0])
            val connection: HttpURLConnection = myUrl.openConnection() as HttpURLConnection
            connection.requestMethod = params[1]
            connection.readTimeout = 5000
            connection.connectTimeout = 5000
            var response = connection.responseCode
            //var data = connection.toString()
            connection.connect()

            val streamReader = InputStreamReader(connection.inputStream)
            val reader = BufferedReader(streamReader)
            val stringBuilder = reader.readText()

            reader.close()
            streamReader.close()
            var result = stringBuilder
            resp = result + response +"\n\n"+ params[0] + "\n\n"+params[1]

        } catch (e: InterruptedException) {
            e.printStackTrace()
            resp = "ERROR: " + e.message
        } catch (e: Exception) {
            e.printStackTrace()
            resp = "ERROR: " + e.message
        }
        return resp
    }

    override fun onPostExecute(result: String?) {

        val activity = activityReference.get()
        if (activity == null || activity.isFinishing) return
            activity.progressBar.visibility = View.GONE

    }

    override fun onProgressUpdate(vararg text: String?) {

        val activity = activityReference.get()
        if (activity == null || activity.isFinishing) return
        Toast.makeText(activity, text.firstOrNull(), Toast.LENGTH_SHORT).show()
    }
}

class MyAsyncTaskSignUp internal constructor(context: SignUpActivity) : AsyncTask<String, String, String?>() {

    private var resp: String? = null
    private val activityReference: WeakReference<SignUpActivity> = WeakReference(context)

    override fun onPreExecute() {
        val activity = activityReference.get()
        if (activity == null || activity.isFinishing) return
        activity.progressBar.visibility = View.VISIBLE
    }

    override fun doInBackground(vararg params: String?): String? {
        //publishProgress("Connecting...") // Calls onProgressUpdate()
        try {
            val myUrl = URL(params[0])
            val connection: HttpURLConnection = myUrl.openConnection() as HttpURLConnection
            connection.requestMethod = params[1]
            connection.readTimeout = 5000
            connection.connectTimeout = 5000
            var response = connection.responseCode
            //var data = connection.toString()
            connection.connect()

            val streamReader = InputStreamReader(connection.inputStream)
            val reader = BufferedReader(streamReader)
            val stringBuilder = reader.readText()

            reader.close()
            streamReader.close()
            var result = stringBuilder
            resp = result + response +"\n\n"+ params[0] + "\n\n"+params[1]

        } catch (e: InterruptedException) {
            e.printStackTrace()
            resp = "ERROR: " + e.message
        } catch (e: Exception) {
            e.printStackTrace()
            resp = "ERROR: " + e.message
        }
        return resp
    }

    override fun onPostExecute(result: String?) {

        val activity = activityReference.get()
        if (activity == null || activity.isFinishing) return
        activity.progressBar.visibility = View.GONE

    }

    override fun onProgressUpdate(vararg text: String?) {

        val activity = activityReference.get()
        if (activity == null || activity.isFinishing) return
        Toast.makeText(activity, text.firstOrNull(), Toast.LENGTH_SHORT).show()
    }
}
