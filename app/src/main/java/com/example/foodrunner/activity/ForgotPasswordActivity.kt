package com.example.foodrunner.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodrunner.R
import com.example.foodrunner.util.ConnectionManager
import org.json.JSONObject

class ForgotPasswordActivity : AppCompatActivity() {

    lateinit var txtDispMessage: TextView
    lateinit var etMobileNumber: EditText
    lateinit var etEmail: EditText
    lateinit var btnNext: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        btnNext = findViewById(R.id.btnNext)

        btnNext.setOnClickListener {

            val number = etMobileNumber.text.toString()
            val email = etEmail.text.toString()

            val queue = Volley.newRequestQueue(this@ForgotPasswordActivity)
            val url = "http://13.235.250.119/v2/forgot_password/fetch_result "
            val jsonParams = JSONObject()
            jsonParams.put("mobile_number", number)
            jsonParams.put("email", email)

            if (ConnectionManager().checkConnectivity(this@ForgotPasswordActivity)) {
                val jsonObjectRequest =
                    object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {
                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {
                            val firstTry = data.getBoolean("first_try")
                            if (firstTry) {
                                val builder = AlertDialog.Builder(this@ForgotPasswordActivity)
                                builder.setTitle("Information")
                                builder.setMessage("Please check your registered Email for the OTP.")
                                builder.setCancelable(false)
                                builder.setPositiveButton("Ok") { _, _ ->
                                    val intent = Intent(
                                        this@ForgotPasswordActivity,
                                        VerificationActivity::class.java
                                    )
                                    intent.putExtra("user_mobile", number)
                                    startActivity(intent)
                                }
                                builder.create().show()
                            } else {
                                val builder = AlertDialog.Builder(this@ForgotPasswordActivity)
                                builder.setTitle("Information")
                                builder.setMessage("Please refer to the previous email for the OTP.")
                                builder.setCancelable(false)
                                builder.setPositiveButton("Ok") { _, _ ->
                                    val intent = Intent(
                                        this@ForgotPasswordActivity,
                                        VerificationActivity::class.java
                                    )
                                    intent.putExtra("user_mobile", number)
                                    startActivity(intent)
                                }
                                builder.create().show()
                            }
                        } else {
                            Toast.makeText(
                                this@ForgotPasswordActivity,
                                "Mobile number not registered!",
                                Toast.LENGTH_SHORT
                            ).show()

                        }
                    }, Response.ErrorListener {
                        Toast.makeText(
                            this@ForgotPasswordActivity,
                            "Volley Error Occured",
                            Toast.LENGTH_SHORT
                        )
                            .show()

                    }) {
                        override fun getHeaders(): MutableMap<String, String> {

                            val headers = HashMap<String, String>()
                            headers["Content-type"] = "application/json"
                            headers["token"] = "70c7c076310d6e"
                            return headers
                        }
                    }
                queue.add(jsonObjectRequest)

            }
            else{
                val dialog = AlertDialog.Builder(this@ForgotPasswordActivity)
                dialog.setTitle("Error")
                dialog.setMessage("Internet Connection Not Found")
                dialog.setPositiveButton("open setting")
                { text, listner ->
                    val settingIntent = Intent(Settings.ACTION_WIRELESS_SETTINGS)
                    startActivity(settingIntent)
                    finish()
                }
                dialog.setNegativeButton("exit")
                { text, listner ->
                    ActivityCompat.finishAffinity(this@ForgotPasswordActivity)
                }


                dialog.create()
                dialog.show()

            }

                }

    }
}