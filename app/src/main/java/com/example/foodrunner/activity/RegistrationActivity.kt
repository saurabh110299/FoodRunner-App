package com.example.foodrunner.activity

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodrunner.R
import org.json.JSONObject

class RegistrationActivity : AppCompatActivity() {

    lateinit var etName: EditText
    lateinit var etEmail: EditText
    lateinit var etMobile: EditText
    lateinit var etDelivery: EditText
    lateinit var etPassword: EditText
    lateinit var etConfirmPass: EditText
    lateinit var btnRegister: Button
    lateinit var sharedPreferences: SharedPreferences


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_registeration)

        etName = findViewById(R.id.etName)
        etEmail = findViewById(R.id.etEmail)
        etMobile = findViewById(R.id.etMobile)
        etDelivery = findViewById(R.id.etDelivery)
        etPassword = findViewById(R.id.etPassword)
        etConfirmPass = findViewById(R.id.etConfirmPass)
        btnRegister = findViewById(R.id.btnRegister)

        sharedPreferences =
            getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)



        btnRegister.setOnClickListener {
            val pass = etPassword.text.toString()
            val confirmpass = etConfirmPass.text.toString()

            if ((pass == confirmpass)) {

                val queue = Volley.newRequestQueue(this@RegistrationActivity)
                val url = "http://13.235.250.119/v2/register/fetch_result"
                val jsonParams = JSONObject()
                jsonParams.put("name", etName.text.toString())
                jsonParams.put("mobile_number", etMobile.text.toString())
                jsonParams.put("password", etPassword.text.toString())
                jsonParams.put("address", etDelivery.text.toString())
                jsonParams.put("email", etEmail.text.toString())

                val jsonObjectRequest =
                    object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {

                        val data = it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {

                            sharedPreferences.edit().putBoolean("isLoggedIn", true).apply()

                            val logArray = data.getJSONObject("data")

                            sharedPreferences.edit()
                                .putString("user_id", logArray.getString("user_id")).apply()
                            sharedPreferences.edit().putString("name", logArray.getString("name"))
                                .apply()
                            sharedPreferences.edit().putString("email", logArray.getString("email"))
                                .apply()
                            sharedPreferences.edit()
                                .putString("mobile_number", logArray.getString("mobile_number"))
                                .apply()
                            sharedPreferences.edit()
                                .putString("address", logArray.getString("address")).apply()

                            val drawerIntent = Intent(
                                this@RegistrationActivity,
                                AllRestaurantsActivity::class.java
                            )
                            startActivity(drawerIntent)
                            finish()


                        } else {
                            Toast.makeText(
                                this@RegistrationActivity,
                                "Some Error Occured 1",
                                Toast.LENGTH_SHORT
                            ).show()


                        }


                    }, Response.ErrorListener {
                        Toast.makeText(
                            this@RegistrationActivity,
                            "Some Error Occured",
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


            } else {
                    Toast.makeText(
                       this@RegistrationActivity,
                       "Confirm Password should be same as Password",
                       Toast.LENGTH_SHORT
                   ).show()

            }


        }


    }
}