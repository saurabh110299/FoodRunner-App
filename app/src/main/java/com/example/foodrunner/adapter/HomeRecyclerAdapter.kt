package com.example.foodrunner.adapter

import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.foodrunner.R
import com.example.foodrunner.activity.DescriptionActivity
import com.example.foodrunner.database.FavouriteDatabase
import com.example.foodrunner.database.FavouriteEntity
import com.example.foodrunner.model.Restaurants
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.recycler_home_single_row.view.*

class HomeRecyclerAdapter(val context: Context, val itemList: ArrayList<Restaurants>) :
    RecyclerView.Adapter<HomeRecyclerAdapter.HomeViewHolder>() {

    class HomeViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgRestaurantImage: ImageView = view.findViewById(R.id.imgRestaurantImage)
        val txtRestaurantName: TextView = view.findViewById(R.id.txtRestaurantName)
        val txtPrice: TextView = view.findViewById(R.id.txtPrice)
        val imgAddToFavourite: ImageView = view.findViewById(R.id.imgAddToFavourite)
        val txtRestaurantRating: TextView = view.findViewById(R.id.txtRestaurantRating)
        val llContent:CardView = view.findViewById(R.id.llContent)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HomeViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_home_single_row, parent, false)
        return HomeViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size

    }

    override fun onBindViewHolder(holder: HomeViewHolder, position: Int) {
        val restaurant = itemList[position]
        holder.txtRestaurantName.text = restaurant.name
        holder.txtRestaurantRating.text = restaurant.rating
        holder.txtPrice.text = "Rs ${restaurant.cost_for_one}/person"
        Picasso.get().load(restaurant.image).into(holder.imgRestaurantImage)
        holder.llContent.setOnClickListener {
            val menuIntent = Intent(context, DescriptionActivity::class.java)
            menuIntent.putExtra("res_id", restaurant.res_id)
            menuIntent.putExtra("res_name", restaurant.name)
            context.startActivity(menuIntent)

        }

        val foodEntity = FavouriteEntity(
            restaurant.res_id,
            restaurant.name,
            restaurant.rating,
            restaurant.cost_for_one,
            restaurant.image
        )

        if (DBAsyncTask(context, foodEntity, 1).execute()
                .get()
        ) {
            holder.imgAddToFavourite.setImageResource(R.drawable.ic_filled_favorite)
        }else{
            holder.imgAddToFavourite.setImageResource(R.drawable.ic_favorite)
        }


        holder.imgAddToFavourite.setOnClickListener {


            if (!DBAsyncTask(context, foodEntity, 1).execute()
                    .get()
            ) {
                val async =
                    DBAsyncTask(context, foodEntity, 2).execute()
                val result = async.get()

                if (result) {
                    Toast.makeText(
                        context,
                        "Restaurant added to favourites!",
                        Toast.LENGTH_SHORT
                    ).show()

                    holder.imgAddToFavourite.setImageResource(R.drawable.ic_filled_favorite)

                } else {
                    Toast.makeText(
                        context,
                        "Some error occurred!",
                        Toast.LENGTH_SHORT
                    ).show()
                }


            } else {

                val async =
                    DBAsyncTask(context, foodEntity, 3).execute()
                val result = async.get()

                if (result) {
                    Toast.makeText(
                        context,
                        "Restaurant removed from favourites!",
                        Toast.LENGTH_SHORT
                    ).show()

                    holder.imgAddToFavourite.setImageResource(R.drawable.ic_favorite)
                } else {
                    Toast.makeText(
                        context,
                        "Some error occurred!",
                        Toast.LENGTH_SHORT
                    ).show()
                }

            }


        }
    }

    class DBAsyncTask(val context: Context, val favouriteEntity: FavouriteEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {

        val db =
            Room.databaseBuilder(context, FavouriteDatabase::class.java, "favourite-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            when (mode) {

                1 -> {

                    val food: FavouriteEntity? =
                        db.favouriteDao().getFavouriteById(favouriteEntity.res_id)
                    db.close()
                    return food != null
                }

                2 -> {

                    db.favouriteDao().insertFavourite(favouriteEntity)
                    db.close()
                    return true
                }

                3 -> {

                    db.favouriteDao().deleteFavourite(favouriteEntity)
                    db.close()
                    return true
                }


            }
            return false
        }

    }


}