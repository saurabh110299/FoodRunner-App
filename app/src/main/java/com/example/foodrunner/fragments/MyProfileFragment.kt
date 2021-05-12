package com.example.foodrunner.fragments

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.foodrunner.R

 class MyProfileFragment : Fragment() {


     lateinit var txtProfileName:TextView
     lateinit var txtProfileNumber:TextView
     lateinit var txtProfileEmail:TextView
     lateinit var txtProfileAddress:TextView
     lateinit var sharedPreferences:SharedPreferences
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
         val view= inflater.inflate(R.layout.fragment_my_profile, container, false)

        txtProfileName=view.findViewById(R.id.txtProfileName)
        txtProfileNumber=view.findViewById(R.id.txtProfileNumber)
        txtProfileEmail=view.findViewById(R.id.txtProfileEmail)
        txtProfileAddress=view.findViewById(R.id.txtProfileAddress)

        sharedPreferences=(activity as Activity).getSharedPreferences(getString(R.string.preference_file_name),Context.MODE_PRIVATE)

        txtProfileName.text=sharedPreferences.getString("name","ERROR")
        val phoneNumber="+91-"+sharedPreferences.getString("mobile_number","ERROR")
        txtProfileNumber.text=phoneNumber
        txtProfileEmail.text=sharedPreferences.getString("email","ERROR")
        txtProfileAddress.text=sharedPreferences.getString("address","ERROR")



        return view
    }


}