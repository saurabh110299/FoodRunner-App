package com.example.foodrunner.database.order

import androidx.room.Database
import androidx.room.RoomDatabase
import com.example.foodrunner.database.order.OrderDao
import com.example.foodrunner.database.order.OrderEntity

@Database(entities = [OrderEntity::class],version = 1)
abstract class OrderDatabase:RoomDatabase() {

    abstract fun orderDao(): OrderDao
}