package com.example.foodrunner.model

import org.json.JSONArray

data class OrderDetail (
    val order_id:Int,
    val res_name:String,
    val date_of_order:String,
    val foodItems:JSONArray
)