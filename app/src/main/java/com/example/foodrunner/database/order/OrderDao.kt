package com.example.foodrunner.database.order

import androidx.room.*

@Dao
interface OrderDao {
    @Insert
    fun insertOrder(orderEntity: OrderEntity)

    @Delete
    fun deleteOrder(orderEntity: OrderEntity)

    @Query("SELECT * FROM `order`")
    fun getAllOrders():List<OrderEntity>

    @Query("SELECT * FROM `order` WHERE food_id=:foodId")
    fun getOrderById(foodId: String):OrderEntity

    @Query("DELETE FROM `order`")
    fun deleteAll()



}