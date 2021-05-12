package com.example.foodrunner.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.TextureView
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.foodrunner.R
import com.example.foodrunner.database.order.OrderEntity
import com.example.foodrunner.model.Menu
import com.example.foodrunner.model.Restaurants

class CartRecyclerAdapter(val context: Context, val itemList:List<Menu>) :RecyclerView.Adapter<CartRecyclerAdapter.CartViewHolder>()  {

    class CartViewHolder(view : View):RecyclerView.ViewHolder(view){
        val txtFoodItem:TextView=view.findViewById(R.id.txtFoodItem)
        val txtFoodPrice:TextView=view.findViewById(R.id.txtFoodPrice)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_cart_single_row, parent, false)
        return CartViewHolder(view)

    }

    override fun getItemCount(): Int {
      return itemList.size
    }

    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val order = itemList[position]
        holder.txtFoodItem.text=order.name
        holder.txtFoodPrice.text=order.cost_for_one

    }
}