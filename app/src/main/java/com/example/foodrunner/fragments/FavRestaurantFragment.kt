package com.example.foodrunner.fragments

import android.content.Context
import android.os.AsyncTask
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.foodrunner.R
import com.example.foodrunner.adapter.FavouriteRecyclerAdapter
import com.example.foodrunner.database.FavouriteDatabase
import com.example.foodrunner.database.FavouriteEntity

class FavRestaurantFragment : Fragment() {
     lateinit var recyclerFavourite:RecyclerView
     lateinit var layoutManager:RecyclerView.LayoutManager
     lateinit var recyclerAdapter: FavouriteRecyclerAdapter
     var dbFavouriteList= listOf<FavouriteEntity>()


     override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= inflater.inflate(R.layout.fragment_fav_restaurant, container, false)

        recyclerFavourite=view.findViewById(R.id.recyclerFavourite)
         layoutManager=LinearLayoutManager(activity)
         dbFavouriteList=RetrieveFavourites(activity as Context).execute().get()

         if(activity!=null)
         {

             recyclerAdapter= FavouriteRecyclerAdapter(activity as Context,dbFavouriteList)
             recyclerFavourite.adapter=recyclerAdapter
             recyclerFavourite.layoutManager=layoutManager
         }



         return view
    }

    class RetrieveFavourites(val  context: Context): AsyncTask<Void, Void, List<FavouriteEntity>>()
    {
        override fun doInBackground(vararg params: Void?): List<FavouriteEntity> {
            val db= Room.databaseBuilder(context, FavouriteDatabase::class.java,"favourite-db").build()

            return db.favouriteDao().getAllFavourites()
        }

    }

}