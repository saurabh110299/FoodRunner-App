package com.example.foodrunner.database.order

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "order")
data class OrderEntity (
    @PrimaryKey val food_id:String,
    @ColumnInfo(name = "name") val food_name:String,
    @ColumnInfo (name = "cost")val food_cost:Int
)