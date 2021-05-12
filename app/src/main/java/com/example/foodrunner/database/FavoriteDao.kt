package com.example.foodrunner.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query

@Dao
interface FavoriteDao {
    @Insert
    fun insertFavourite(favouriteEntity:FavouriteEntity)

    @Delete
    fun deleteFavourite(favouriteEntity: FavouriteEntity)

    @Query("SELECT * FROM favourites")
    fun getAllFavourites():List<FavouriteEntity>

    @Query("SELECT * FROM favourites WHERE res_id=:resId")
    fun getFavouriteById(resId:String):FavouriteEntity

}