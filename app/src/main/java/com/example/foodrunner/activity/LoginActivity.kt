package com.example.foodrunner.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodrunner.R
import com.example.foodrunner.util.ConnectionManager
import org.json.JSONObject

class LoginActivity : AppCompatActivity() {

    lateinit var imgLogo: ImageView
    lateinit var etNumber: EditText
    lateinit var btnLogin: Button
    lateinit var etPassword: EditText
    lateinit var txtForgot: TextView
    lateinit var txtSignUp: TextView
    lateinit var sharedPreferences:SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etNumber = findViewById(R.id.etNumber)
        etPassword = findViewById(R.id.etPassword)
        btnLogin = findViewById(R.id.btnLogin)
        txtForgot = findViewById(R.id.txtForgot)
        txtSignUp = findViewById(R.id.txtSignUp)

        sharedPreferences=getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)
        val isLoggedIn=sharedPreferences.getBoolean("isLoggedIn",false)



        if(isLoggedIn){
            gotoAllRestaurantPage()
        }



        btnLogin.setOnClickListener {
            val number = etNumber.text.toString()
            val password = etPassword.text.toString()

            val queue = Volley.newRequestQueue(this@LoginActivity)
            val url = "http://13.235.250.119/v2/login/fetch_result"
            val jsonParams = JSONObject()
            jsonParams.put("mobile_number", number)
            jsonParams.put("password", password)


            if (ConnectionManager().checkConnectivity(this@LoginActivity)){
                val jsonObjectRequest =
                    object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {

                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {

                            sharedPreferences.edit().putBoolean("isLoggedIn",true).apply()

                            val logArray=data.getJSONObject("data")

                            sharedPreferences.edit().putString("user_id",logArray.getString("user_id")).apply()
                            sharedPreferences.edit().putString("name",logArray.getString("name")).apply()
                            sharedPreferences.edit().putString("email",logArray.getString("email")).apply()
                            sharedPreferences.edit().putString("mobile_number",logArray.getString("mobile_number")).apply()
                            sharedPreferences.edit().putString("address",logArray.getString("address")).apply()

                            gotoAllRestaurantPage()

                        } else {
                            Toast.makeText(
                                this@LoginActivity,
                                "Incorrect Credentials",
                                Toast.LENGTH_SHORT
                            ).show()


                        }


                    }, Response.ErrorListener {
                        Toast.makeText(this@LoginActivity, "Volley Error Occured", Toast.LENGTH_SHORT)
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
                val dialog = AlertDialog.Builder(this@LoginActivity)
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
                    ActivityCompat.finishAffinity(this@LoginActivity)
                }


                dialog.create()
                dialog.show()

            }

        }


        txtForgot.setOnClickListener {
            val forgotIntent = Intent(
                this@LoginActivity,
                ForgotPasswordActivity::class.java
            )
            startActivity(forgotIntent)
        }



        txtSignUp.setOnClickListener {
            val registerIntent = Intent(this@LoginActivity, RegistrationActivity::class.java)
            startActivity(registerIntent)

        }

    }

    fun gotoAllRestaurantPage(){
        val drawerIntent = Intent(
            this@LoginActivity,
            AllRestaurantsActivity::class.java
        )
        startActivity(drawerIntent)
        finish()

    }


}