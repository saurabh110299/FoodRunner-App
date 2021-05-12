package com.example.foodrunner.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "favourites")
data class FavouriteEntity (
    @PrimaryKey val res_id:String,
    @ColumnInfo(name="name") val restName:String,
    @ColumnInfo(name="rating") val restRating:String,
    @ColumnInfo(name = "cost") val cost_for_one:String,
    @ColumnInfo(name = "image") val restImage:String
)