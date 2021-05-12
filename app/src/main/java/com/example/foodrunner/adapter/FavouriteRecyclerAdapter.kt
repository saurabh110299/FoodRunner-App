package com.example.foodrunner.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrunner.R
import com.example.foodrunner.activity.DescriptionActivity
import com.example.foodrunner.database.FavouriteEntity
import com.example.foodrunner.model.Restaurants
import com.squareup.picasso.Picasso

class FavouriteRecyclerAdapter(val context: Context, val itemList:List<FavouriteEntity>):
    RecyclerView.Adapter<FavouriteRecyclerAdapter.FavouriteViewHolder>() {

    class FavouriteViewHolder(view: View): RecyclerView.ViewHolder(view){
        val imgRestaurantImage: ImageView =view.findViewById(R.id.imgRestaurantImage)
        val txtRestaurantName: TextView =view.findViewById(R.id.txtRestaurantName)
        val txtPrice: TextView =view.findViewById(R.id.txtPrice)
        val imgAddToFavourite: ImageView =view.findViewById(R.id.imgAddToFavourite)
        val txtRestaurantRating: TextView =view.findViewById(R.id.txtRestaurantRating)
        val llContent: CardView =view.findViewById(R.id.llContent)


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FavouriteViewHolder {
        val view= LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_home_single_row,parent,false)

        return FavouriteViewHolder(view)
    }

    override fun getItemCount(): Int {
     return itemList.size
    }

    override fun onBindViewHolder(holder: FavouriteViewHolder, position: Int) {

        val favourite=itemList[position]
        holder.txtRestaurantName.text=favourite.restName
        holder.txtPrice.text=favourite.cost_for_one
        holder.txtRestaurantRating.text=favourite.restRating
        holder.imgAddToFavourite.setImageResource(R.drawable.ic_filled_favorite)
        Picasso.get().load(favourite.restImage).into(holder.imgRestaurantImage)

        holder.llContent.setOnClickListener {
            val menuIntent = Intent(context, DescriptionActivity::class.java)
            menuIntent.putExtra("res_id", favourite.res_id)
            menuIntent.putExtra("res_name", favourite.restName)
            context.startActivity(menuIntent)
        }

    }

}