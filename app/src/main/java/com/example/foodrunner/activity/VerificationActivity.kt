package com.example.foodrunner.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodrunner.R
import com.example.foodrunner.util.ConnectionManager
import org.json.JSONObject

class VerificationActivity : AppCompatActivity() {

    lateinit var etOtp:EditText
    lateinit var etNewPass:EditText
    lateinit var etConfirmPass:EditText
    lateinit var btnSubmit:Button
    lateinit var mobileNumber:String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verification)

        etOtp=findViewById(R.id.etOtp)
        etNewPass=findViewById(R.id.etNewPass)
        etConfirmPass=findViewById(R.id.etConfirmPass)
        btnSubmit=findViewById(R.id.btnSubmit)

        if(intent!=null)
            mobileNumber=intent.getStringExtra("user_mobile")


        btnSubmit.setOnClickListener{



            val otp=etOtp.text.toString()
            val newPass=etNewPass.text.toString()
            val confirmPass=etConfirmPass.text.toString()

            if(otp!="" && newPass==confirmPass && newPass!="" && confirmPass!="") {
                val queue = Volley.newRequestQueue(this@VerificationActivity)
                val url = "http://13.235.250.119/v2/forgot_password/fetch_result "
                val jsonParams = JSONObject()
                jsonParams.put("mobile_number",mobileNumber)
                jsonParams.put("password", newPass)
                jsonParams.put("otp", otp)

                if (ConnectionManager().checkConnectivity(this@VerificationActivity)){
                    val jsonObjectRequest =
                        object : JsonObjectRequest(
                            Method.POST, url, jsonParams,Response.Listener {
                                val data = it.getJSONObject("data")
                                val success = data.getBoolean("success")
                                if (success){
                                    val intent = Intent(
                                        this@VerificationActivity,
                                        LoginActivity::class.java
                                    )
                                    Toast.makeText(
                                        this@VerificationActivity,
                                        data.getString("successMessage"),
                                        Toast.LENGTH_SHORT
                                    ).show()
                                    startActivity(intent)

                                }else{
                                    Toast.makeText(
                                        this@VerificationActivity,
                                        " Some Error Occured",
                                        Toast.LENGTH_SHORT
                                    ).show()

                                }
                            },Response.ErrorListener {
                                Toast.makeText(
                                    this@VerificationActivity,
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
                    val dialog = AlertDialog.Builder(this@VerificationActivity)
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
                        ActivityCompat.finishAffinity(this@VerificationActivity)
                    }


                    dialog.create()
                    dialog.show()

                }



            }
            else{
                Toast.makeText(this@VerificationActivity,"Incorrect Information Entered",Toast.LENGTH_SHORT).show()
            }

        }
    }
}