package com.example.foodrunner.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodrunner.model.Menu
import com.example.foodrunner.R
import com.example.foodrunner.adapter.DescriptionRecyclerAdapter
import com.example.foodrunner.database.order.OrderDatabase
import com.example.foodrunner.database.order.OrderEntity

class DescriptionActivity : AppCompatActivity() {

    lateinit var descriptionToolbar: Toolbar
    lateinit var recyclerDescription: RecyclerView
//    lateinit var btnProceed: Button
    lateinit var progressLayout:RelativeLayout
    lateinit var progressBar:ProgressBar
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var menuRecyclerAdapter: DescriptionRecyclerAdapter
    val menuList = arrayListOf<Menu>()

    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var btnProceed: Button
        var resId: String? = ""
        var resName: String? = ""
    }



    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_description)

        descriptionToolbar = findViewById(R.id.descriptionToolbar)



        progressLayout=findViewById(R.id.progressLayout)
        progressBar=findViewById(R.id.progressBar)
        btnProceed=findViewById(R.id.btnProceed) as Button

        recyclerDescription = findViewById(R.id.recyclerDescription)
        layoutManager = LinearLayoutManager(this@DescriptionActivity)

        progressLayout.visibility=View.VISIBLE
        btnProceed.visibility = View.GONE


        if (intent != null) {
            resId = intent.getStringExtra("res_id")
            resName=intent.getStringExtra("res_name")
        } else {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some Unexpected Error Occured",
                Toast.LENGTH_SHORT
            ).show()
        }

        if (resId == ""||resName=="") {
            finish()
            Toast.makeText(
                this@DescriptionActivity,
                "Some Unexpected Error Occured",
                Toast.LENGTH_SHORT
            ).show()

        }

        setSupportActionBar(descriptionToolbar)
        supportActionBar?.title=resName





        val queue = Volley.newRequestQueue(this@DescriptionActivity)
        val url = "http://13.235.250.119/v2/restaurants/fetch_result/${resId}"

        val jsonRequest = object : JsonObjectRequest(Method.GET, url, null,
            Response.Listener {

                val data = it.getJSONObject("data")
                val success = data.getBoolean("success")
                if (success) {
                    progressLayout.visibility=View.GONE

                    val menuArray = data.getJSONArray("data")
                    for (i in 0 until menuArray.length()) {

                        val deleteResult=deleteAllEntries(this@DescriptionActivity).execute().get()
                        if (deleteResult)
                        {
                            btnProceed.visibility=View.GONE
                            DescriptionRecyclerAdapter.totalPrice=0
                        }
                        else{
                            btnProceed.visibility=View.VISIBLE
                        }


                        val menuJsonObject = menuArray.getJSONObject(i)
                        val menuObject = Menu(
                            menuJsonObject.getString("id"),
                            menuJsonObject.getString("name"),
                            menuJsonObject.getString("cost_for_one")
//                            menuJsonObject.getString("restaurant_id")
                        )
                        menuList.add(menuObject)
                        menuRecyclerAdapter =
                            DescriptionRecyclerAdapter(this@DescriptionActivity, menuList)
                        recyclerDescription.adapter = menuRecyclerAdapter
                        recyclerDescription.layoutManager = layoutManager
//                        recyclerDescription.addItemDecoration(
//                            DividerItemDecoration(
//                                recyclerDescription.context,
//                                (layoutManager as LinearLayoutManager).orientation
//                            )
//                        )
                    }
                } else {
                    Toast.makeText(
                        this@DescriptionActivity,
                        "Some Error Occured",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            },
            Response.ErrorListener {
                Toast.makeText(this@DescriptionActivity, "Volley Error Occured", Toast.LENGTH_SHORT)
                    .show()
            }) {
            override fun getHeaders(): MutableMap<String, String> {

                val headers = HashMap<String, String>()
                headers["Content-type"] = "application/json"
                headers["token"] = "70c7c076310d6e"
                return headers
            }

        }

        queue.add(jsonRequest)

        btnProceed.setOnClickListener{
            val cartIntent=Intent(this@DescriptionActivity,CartActivity::class.java)

            startActivity(cartIntent)
        }




    }

     class deleteAllEntries(val context: Context):AsyncTask<Void,Void,Boolean>(){
        override fun doInBackground(vararg params: Void?): Boolean {
            val db = Room.databaseBuilder(context, OrderDatabase::class.java, "order-db").build()

            db.orderDao().deleteAll()

            return db.orderDao().getAllOrders().isEmpty()

        }

    }

    }