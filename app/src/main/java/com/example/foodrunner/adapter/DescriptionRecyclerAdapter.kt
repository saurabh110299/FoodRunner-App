package com.example.foodrunner.adapter

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.AsyncTask
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.example.foodrunner.R
import com.example.foodrunner.activity.CartActivity
import com.example.foodrunner.activity.DescriptionActivity
import com.example.foodrunner.database.FavouriteDatabase
import com.example.foodrunner.database.FavouriteEntity
import com.example.foodrunner.database.order.OrderDatabase
import com.example.foodrunner.database.order.OrderEntity
import com.example.foodrunner.model.Menu

class DescriptionRecyclerAdapter(val context: Context, val itemList: ArrayList<Menu>) :
    RecyclerView.Adapter<DescriptionRecyclerAdapter.DescriptionViewHolder>() {

    companion object {
        @SuppressLint("StaticFieldLeak")

        var totalPrice: Int = 0
    }


    class DescriptionViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtDishNum: TextView = view.findViewById(R.id.txtDishNum)
        val txtDishName: TextView = view.findViewById(R.id.txtDishName)
        val txtDishCost: TextView = view.findViewById(R.id.txtDishCost)
        val btnAdd: Button = view.findViewById(R.id.btnAdd)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DescriptionViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_description_single_row, parent, false)
        return DescriptionViewHolder(view)
    }

    override fun getItemCount(): Int {
        return itemList.size

    }

    override fun onBindViewHolder(holder: DescriptionViewHolder, position: Int) {
        val menuItem = itemList[position]
        holder.txtDishNum.text = (position + 1).toString()
        holder.txtDishName.text = menuItem.name
        holder.txtDishCost.text = menuItem.cost_for_one

        val orderEntity = OrderEntity(
            menuItem.id,
            menuItem.name,
            menuItem.cost_for_one.toInt()
        )

        if (DBAsyncTask(context, orderEntity, 1).execute()
                .get()
        ) {
            holder.btnAdd.text = "Remove"
            val buttonColor = ContextCompat.getColor(context, R.color.buttonColor)
            holder.btnAdd.setBackgroundColor(buttonColor)

        } else {
            holder.btnAdd.text = "Add"
            val buttonColor = ContextCompat.getColor(context, R.color.appColor)
            holder.btnAdd.setBackgroundColor(buttonColor)
        }

        holder.btnAdd.setOnClickListener {

            if (!DBAsyncTask(context, orderEntity, 1).execute()
                    .get()
            ) {

                val result = DBAsyncTask(context, orderEntity, 2).execute().get()
                if (result) {
                    holder.btnAdd.text = "Remove"
                    val buttonColor = ContextCompat.getColor(context, R.color.buttonColor)
                    holder.btnAdd.setBackgroundColor(buttonColor)
                    DescriptionActivity.btnProceed.visibility = View.VISIBLE
                    totalPrice += menuItem.cost_for_one.toInt()
                    DescriptionActivity.btnProceed.setOnClickListener{
                        val cartIntent=Intent(context,CartActivity::class.java)
                        cartIntent.putExtra("totalPrice", totalPrice)
                        context.startActivity(cartIntent)

                     }

                } else {
                    Toast.makeText(context, "Some Error Occured!", Toast.LENGTH_SHORT).show()
                }

            } else {

                val result = DBAsyncTask(context, orderEntity, 3).execute().get()
                if (result) {
                    holder.btnAdd.text = "Add"
                    val buttonColor = ContextCompat.getColor(context, R.color.appColor)
                    holder.btnAdd.setBackgroundColor(buttonColor)
                    totalPrice -= menuItem.cost_for_one.toInt()

                    val emptyResult = DBAsyncTask(context, orderEntity, 4).execute().get()
                    if (emptyResult) {
                        DescriptionActivity.btnProceed.visibility = View.GONE
                    }

                } else {
                    Toast.makeText(context, "Some Error Occured!", Toast.LENGTH_SHORT).show()
                }

            }


        }

    }

    class DBAsyncTask(val context: Context, val orderEntity: OrderEntity, val mode: Int) :
        AsyncTask<Void, Void, Boolean>() {

        val db =
            Room.databaseBuilder(context, OrderDatabase::class.java, "order-db").build()

        override fun doInBackground(vararg params: Void?): Boolean {

            when (mode) {

                1 -> {

                    val food: OrderEntity? =
                        db.orderDao().getOrderById(orderEntity.food_id)
                    db.close()
                    return food != null
                }

                2 -> {

                    db.orderDao().insertOrder(orderEntity)
                    db.close()
                    return true
                }

                3 -> {

                    db.orderDao().deleteOrder(orderEntity)
                    db.close()
                    return true
                }


                4 -> {

                    return db.orderDao().getAllOrders().isEmpty()
                }


                5->{
                    db.orderDao().deleteAll()
                    db.close()
                    return db.orderDao().getAllOrders().isEmpty()
                }
            }
            return false
        }

    }



}