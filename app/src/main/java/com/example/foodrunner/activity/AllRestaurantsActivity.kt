package com.example.foodrunner.activity

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.widget.FrameLayout
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AlertDialog
import androidx.coordinatorlayout.widget.CoordinatorLayout
import androidx.core.app.ActivityCompat
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.example.foodrunner.R
import com.example.foodrunner.fragments.*
import com.google.android.material.navigation.NavigationView

class AllRestaurantsActivity : AppCompatActivity() {

    lateinit var drawerLayout: DrawerLayout
    lateinit var coordinatorLayout: CoordinatorLayout
    lateinit var toolbar: androidx.appcompat.widget.Toolbar
    lateinit var frameLayout: FrameLayout
    lateinit var navigationView: NavigationView
    var previousMenuItem: MenuItem? = null
    lateinit var sharedPreferences: SharedPreferences
    lateinit var drawerName:TextView
    lateinit var drawerNumber:TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_restaurants)

        drawerLayout = findViewById(R.id.drawerLayout)
        coordinatorLayout = findViewById(R.id.coordinatorLayout)
        toolbar = findViewById(R.id.toolbar)
        frameLayout = findViewById(R.id.frame)
        navigationView = findViewById(R.id.navigationView)
        sharedPreferences=getSharedPreferences(getString(R.string.preference_file_name), Context.MODE_PRIVATE)

//        drawerName=findViewById(R.id.drawerName)
//        drawerNumber=findViewById(R.id.drawerNumber)
//
//        drawerName.text=sharedPreferences.getString("name","ERROR")
//        drawerNumber.text=sharedPreferences.getString("mobile_number","ERROR")

        val convertView=LayoutInflater.from(this@AllRestaurantsActivity).inflate(R.layout.drawer_header,null)

        drawerName=convertView.findViewById(R.id.drawerName)
        drawerNumber=convertView.findViewById(R.id.drawerNumber)

        drawerName.text=sharedPreferences.getString("name","ERROR")
        val phoneNumber="+91-"+sharedPreferences.getString("mobile_number","ERROR")
        drawerNumber.text=phoneNumber
        navigationView.addHeaderView(convertView)

        setupToolbar()
        openHome()



        val actionBarDrawerToggle = ActionBarDrawerToggle(
            this,
            drawerLayout,
            R.string.open_drawer,
            R.string.close_drawer
        )

        drawerLayout.addDrawerListener(actionBarDrawerToggle)
        actionBarDrawerToggle.syncState()



        navigationView.setNavigationItemSelectedListener {
            if(previousMenuItem!=null)
            {
                previousMenuItem?.isChecked=false
            }
            it.isCheckable=true
            it.isChecked=true
            previousMenuItem=it





            when (it.itemId) {
                R.id.home -> {
                    openHome()
                    drawerLayout.closeDrawers()
                }

                R.id.myProfile -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            MyProfileFragment()
                        )
                        .commit()

                    supportActionBar?.title = "My Profile"

                    drawerLayout.closeDrawers()


                }

                R.id.favRestaurant -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            FavRestaurantFragment()
                        )
                        .commit()

                    supportActionBar?.title = "Favourite Restaurants"

                    drawerLayout.closeDrawers()


                }

                R.id.orderHistory -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            OrderHistoryFragment()
                        )
                        .commit()

                    supportActionBar?.title = "My Previous Orders"

                    drawerLayout.closeDrawers()


                }

                R.id.faqs -> {
                    supportFragmentManager.beginTransaction()
                        .replace(
                            R.id.frame,
                            FaqFragment()
                        )
                        .commit()

                    supportActionBar?.title = "Frequently Asked Questions"

                    drawerLayout.closeDrawers()


                }

                R.id.logOut -> {

                    val dialog = AlertDialog.Builder(this@AllRestaurantsActivity)
                    dialog.setTitle("Confirmation")
                    dialog.setMessage("Are you sure you want to log out?")
                    dialog.setPositiveButton("YES") { text, listener ->
                        sharedPreferences.edit().clear().apply()
                        val logoutIntent = Intent(this, LoginActivity::class.java)
                        startActivity(logoutIntent)
                        ActivityCompat.finishAffinity(this)

                    }
                    dialog.setNegativeButton("NO") { text, listener ->
                        openHome()
                    }
                    dialog.create()
                    dialog.show()

                }
            }



            return@setNavigationItemSelectedListener true
        }

    }


    fun setupToolbar() {
        setSupportActionBar(toolbar)
        supportActionBar?.title = "All Restaurants"
        supportActionBar?.setHomeButtonEnabled(true)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {

        val id = item.itemId
        if (id == android.R.id.home) {
            drawerLayout.openDrawer(GravityCompat.START)
        }
        return super.onOptionsItemSelected(item)
    }

    fun openHome() {
        val fragment = HomeFragment()
        val transaction = supportFragmentManager.beginTransaction()

        transaction.replace(R.id.frame, fragment)
            .commit()

        supportActionBar?.title = "All Restaurants"
        navigationView.setCheckedItem(R.id.home)
        drawerLayout.closeDrawers()
    }

    override fun onBackPressed() {
        val frag = supportFragmentManager.findFragmentById(R.id.frame)

        when (frag) {
            !is HomeFragment -> openHome()

            else ->
                super.onBackPressed()
        }


    }
}