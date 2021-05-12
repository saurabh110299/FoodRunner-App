package com.example.foodrunner.adapter

import android.content.Context
import android.icu.text.SimpleDateFormat
import android.os.Build
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrunner.R
import com.example.foodrunner.database.order.OrderEntity
import com.example.foodrunner.model.Menu
import com.example.foodrunner.model.OrderDetail
import java.util.*
import kotlin.collections.ArrayList

class OrderHistoryRecyclerAdapter(
    val context: Context,
    private val orderHistoryList: ArrayList<OrderDetail>
) :
    RecyclerView.Adapter<OrderHistoryRecyclerAdapter.OrderHistoryViewHolder>() {


    override fun onCreateViewHolder(p0: ViewGroup, p1: Int): OrderHistoryViewHolder {
        val view =
            LayoutInflater.from(context)
                .inflate(R.layout.recycler_order_history_single_row, p0, false)
        return OrderHistoryViewHolder(view)
    }


    override fun getItemCount(): Int {
        return orderHistoryList.size
    }


    override fun onBindViewHolder(holder: OrderHistoryViewHolder, position: Int) {
        val orderHistoryObject = orderHistoryList[position]
        holder.txtResName.text = orderHistoryObject.res_name
        holder.txtDate.text = orderHistoryObject.date_of_order

        setUpRecycler(holder.recyclerResHistory, orderHistoryObject)


    }


    class OrderHistoryViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val txtResName: TextView = view.findViewById(R.id.txtResName)
        val txtDate: TextView = view.findViewById(R.id.txtDate)
        val recyclerResHistory: RecyclerView = view.findViewById(R.id.recyclerOrderList)
    }


    private fun setUpRecycler(recyclerResHistory: RecyclerView, orderHistoryList: OrderDetail) {
        val foodItemsList = ArrayList<Menu>()
        for (i in 0 until orderHistoryList.foodItems.length()) {
            val foodJson = orderHistoryList.foodItems.getJSONObject(i)
            foodItemsList.add(
                Menu(
                    foodJson.getString("food_item_id"),
                    foodJson.getString("name"),
                    foodJson.getString("cost")
                )
            )
        }
        val cartItemAdapter = CartRecyclerAdapter(context, foodItemsList)
        val LayoutManager = LinearLayoutManager(context)
        recyclerResHistory.layoutManager = LayoutManager
        recyclerResHistory.adapter = cartItemAdapter
    }


}