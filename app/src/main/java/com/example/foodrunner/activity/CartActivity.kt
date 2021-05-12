package com.example.foodrunner.activity

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.View
import android.widget.Button
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodrunner.R
import com.example.foodrunner.activity.DescriptionActivity.Companion.resId
import com.example.foodrunner.activity.DescriptionActivity.Companion.resName
import com.example.foodrunner.adapter.CartRecyclerAdapter
import com.example.foodrunner.adapter.DescriptionRecyclerAdapter
import com.example.foodrunner.adapter.FavouriteRecyclerAdapter
import com.example.foodrunner.database.FavouriteEntity
import com.example.foodrunner.database.order.OrderDatabase
import com.example.foodrunner.database.order.OrderEntity
import com.example.foodrunner.fragments.HomeFragment
import org.json.JSONArray
import org.json.JSONObject

class CartActivity : AppCompatActivity() {
    lateinit var btnPlaceOrder: Button
    lateinit var recyclerCart: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: CartRecyclerAdapter
    var dbOrderList = listOf<OrderEntity>()
    var orderList = ArrayList<com.example.foodrunner.model.Menu>()
    lateinit var rlCart:RelativeLayout
    lateinit var txtCartResName:TextView
    var finalPrice: Int = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_cart)

        btnPlaceOrder = findViewById(R.id.btnPlaceOrder)
        recyclerCart = findViewById(R.id.recyclerCart)
        layoutManager = LinearLayoutManager(this@CartActivity)
        dbOrderList = orderPlaced(this@CartActivity).execute().get()
        rlCart=findViewById(R.id.rlCart)
        txtCartResName=findViewById(R.id.txtCartResName)


        txtCartResName.text="Ordering from: $resName"


        for (i in 0 until  dbOrderList.size) {

            val food=com.example.foodrunner.model.Menu(
                dbOrderList[i].food_id,
                dbOrderList[i].food_name,
                dbOrderList[i].food_cost.toString()
            )

            orderList.add(i,food)
         }


        if (this@CartActivity != null) {

            recyclerAdapter = CartRecyclerAdapter(this@CartActivity, orderList)
            recyclerCart.adapter = recyclerAdapter
            recyclerCart.layoutManager = layoutManager
        }



        if (intent != null) {
            finalPrice = intent.getIntExtra("totalPrice", 0)

        } else {
            finish()
            Toast.makeText(
                this@CartActivity,
                "Some Unexpected Error Occured",
                Toast.LENGTH_SHORT
            ).show()
        }

        btnPlaceOrder.text = "Place Order(Total Rs. ${finalPrice})"
        btnPlaceOrder.setOnClickListener {
            rlCart.visibility=View.GONE
            sendServerRequest()

        }


    }

    class orderPlaced(val context: Context) : AsyncTask<Void, Void, List<OrderEntity>>() {
        override fun doInBackground(vararg params: Void?): List<OrderEntity> {

            val db = Room.databaseBuilder(context, OrderDatabase::class.java, "order-db").build()

            return db.orderDao().getAllOrders()
        }


    }

    class deleteAllEntries(val context: Context) : AsyncTask<Void, Void, Boolean>() {
        override fun doInBackground(vararg params: Void?): Boolean {
            val db = Room.databaseBuilder(context, OrderDatabase::class.java, "order-db").build()

            db.orderDao().deleteAll()

            return db.orderDao().getAllOrders().isEmpty()

        }

    }


    private fun sendServerRequest() {
        val queue = Volley.newRequestQueue(this)

        val jsonParams = JSONObject()
        jsonParams.put(
            "user_id",
            this@CartActivity.getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE).getString(
                "user_id",
                null
            ) as String
        )
        jsonParams.put("restaurant_id",resId)
        jsonParams.put("total_cost", finalPrice)

        val foodArray = JSONArray()
        for (i in 0 until dbOrderList.size) {
            val foodId = JSONObject()
            foodId.put("food_item_id", dbOrderList[i].food_id)
            foodArray.put(i, foodId)
        }
        jsonParams.put("food", foodArray)
        val url = "http://13.235.250.119/v2/place_order/fetch_result/"

        val jsonObjectRequest =
            object : JsonObjectRequest(Method.POST, url, jsonParams, Response.Listener {

                try {
                    val data = it.getJSONObject("data")
                    val success = data.getBoolean("success")
                    if (success) {
                        deleteAllEntries(this@CartActivity).execute().get()

                        val dialog = Dialog(
                            this@CartActivity,
                            android.R.style.Theme_Black_NoTitleBar_Fullscreen
                        )
                        dialog.setContentView(R.layout.order_placed_dialog_screen)
                        dialog.show()
                        dialog.setCancelable(false)
                        val btnOk = dialog.findViewById<Button>(R.id.btnOk)
                        btnOk.setOnClickListener {
                            dialog.dismiss()
                            startActivity(
                                Intent(
                                    this@CartActivity,
                                    AllRestaurantsActivity::class.java
                                )
                            )
                            ActivityCompat.finishAffinity(this@CartActivity)
                        }

                    } else {
                        Toast.makeText(this@CartActivity, "Some Error occurred", Toast.LENGTH_SHORT)
                            .show()
                    }

                } catch (e: Exception) {
                    e.printStackTrace()
                }

            }, Response.ErrorListener {

                Toast.makeText(this@CartActivity, it.message, Toast.LENGTH_SHORT).show()
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


}
