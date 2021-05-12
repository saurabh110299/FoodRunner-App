package com.example.foodrunner.fragments

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.media.audiofx.BassBoost
import android.os.Bundle
import android.provider.Settings
import android.provider.Settings.ACTION_WIRELESS_SETTINGS
import android.view.*
import androidx.fragment.app.Fragment
import android.widget.ProgressBar
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.volley.Request
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.foodrunner.R
import com.example.foodrunner.adapter.HomeRecyclerAdapter
import com.example.foodrunner.model.Restaurants
import com.example.foodrunner.util.ConnectionManager
import org.json.JSONException
import java.util.*
import kotlin.collections.HashMap


class HomeFragment : Fragment() {

    lateinit var recyclerHome: RecyclerView
    lateinit var layoutManager: RecyclerView.LayoutManager
    lateinit var recyclerAdapter: HomeRecyclerAdapter
    lateinit var progressLayout:RelativeLayout
    lateinit var progressBar:ProgressBar

    val restaurantList = arrayListOf<Restaurants>()

    val ratingComparator=Comparator<Restaurants>{
            res1,res2->
        if(res1.rating.compareTo(res2.rating,true)==0)
        {
            //sort according to name if rating is same
            res1.name.compareTo(res2.name,true)
        }else{
            res1.rating.compareTo(res2.rating,true)
        }
    }

    val costComparator=Comparator<Restaurants>{
            res1,res2->
        if(res1.cost_for_one.compareTo(res2.cost_for_one,true)==0)
        {
            //sort according to name if cost is same
            res1.name.compareTo(res2.name,true)
        }else{
            res1.cost_for_one.compareTo(res2.cost_for_one,true)
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view = inflater.inflate(R.layout.fragment_home, container, false)
        setHasOptionsMenu(true)

        recyclerHome = view.findViewById(R.id.recyclerHome)
        layoutManager = LinearLayoutManager(activity)
        progressLayout=view.findViewById(R.id.progressLayout)
        progressBar=view.findViewById(R.id.progressBar)

        progressLayout.visibility=View.VISIBLE




        if(ConnectionManager().checkConnectivity(activity as Context)){

            val queue = Volley.newRequestQueue(activity as Context)
            val url = "http://13.235.250.119/v2/restaurants/fetch_result/"
            val jsonObjectRequest = object: JsonObjectRequest(Method.GET, url, null,
                Response.Listener {
                    try {
                        val data=it.getJSONObject("data")
                        val success = data.getBoolean("success")
                        if (success) {
                            progressLayout.visibility=View.GONE

                            val resArray = data.getJSONArray("data")
                            for (i in 0 until resArray.length()) {

                                val ResJsonObject = resArray.getJSONObject(i)
                                val ResObject = Restaurants(
                                    ResJsonObject.getString("id"),
                                    ResJsonObject.getString("name"),
                                    ResJsonObject.getString("rating"),
                                    ResJsonObject.getString("cost_for_one"),
                                    ResJsonObject.getString("image_url")
                                )
                                restaurantList.add(ResObject)

                                recyclerAdapter = HomeRecyclerAdapter(activity as Context, restaurantList)
                                recyclerHome.adapter = recyclerAdapter
                                recyclerHome.layoutManager = layoutManager


                            }

                        }else{
                            Toast.makeText(activity as Context,"Some Error Occured!",Toast.LENGTH_SHORT).show()
                        }

                    }catch (e:JSONException){
                        Toast.makeText(activity as Context,"Some unexpected Error Occured!",Toast.LENGTH_SHORT).show()
                    }
                  },
                Response.ErrorListener {
                    Toast.makeText(activity as Context,"Volley Error Occured!!",Toast.LENGTH_SHORT).show()

                }){
                override fun getHeaders(): MutableMap<String, String> {

                    val headers=HashMap<String,String>()
                    headers["Content-type"]="application/json"
                    headers["token"]="70c7c076310d6e"
                    return headers
                }
            }

            queue.add(jsonObjectRequest)


        }else{
            val dialog = AlertDialog.Builder(activity as Context)
            dialog.setTitle("Error")
                .setMessage("Internet connection is not found")
                .setPositiveButton("Open Setting") { text, listner ->
                val settingsIntent=Intent(Settings.ACTION_WIRELESS_SETTINGS )
                startActivity(settingsIntent)
                activity?.finish()
            }.setNegativeButton("Exit") { text, listner ->
                ActivityCompat.finishAffinity(activity as Activity)
            }.create().show()

                val settingsIntent=Intent(Settings.ACTION_WIRELESS_SETTINGS )
                startActivity(settingsIntent)
                activity?.finish()

        }
                return view
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater?.inflate(R.menu.menu_dashboard,menu)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id=item?.itemId
        if (id==R.id.sortRating){
            Collections.sort(restaurantList,ratingComparator)
            restaurantList.reverse()
        }else if(id==R.id.sortCostLH){
            Collections.sort(restaurantList,costComparator)
        }else if(id==R.id.sortCostHL){
            Collections.sort(restaurantList,costComparator)
            restaurantList.reverse()
        }


        recyclerAdapter.notifyDataSetChanged()

        return super.onOptionsItemSelected(item)
    }




}