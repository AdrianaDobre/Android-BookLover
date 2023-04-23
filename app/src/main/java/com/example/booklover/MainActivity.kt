package com.example.booklover

import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.fragment.app.Fragment
import androidx.navigation.NavController
import androidx.navigation.NavOptions
import androidx.navigation.Navigation
import androidx.navigation.findNavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.fragment.NavHostFragment.Companion.findNavController
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import androidx.navigation.ui.setupWithNavController
import com.example.booklover.databinding.ActivityMainBinding
import com.google.android.material.navigation.NavigationView
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var userId: String
    lateinit var databaseReference: DatabaseReference
    private lateinit var drawerLayout: DrawerLayout
    private lateinit var navView: NavigationView
    private lateinit var navController: NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        drawerLayout = binding.drawerLayout
        navView = binding.navigationView

        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://booklover-96666-default-rtdb.firebaseio.com/")

        userId = intent.getStringExtra("userId").toString()

        binding.apply {
//            supportFragmentManager.beginTransaction()
//                .add(R.id.nav_host_fragment, BooksFragment::class.java, null)
//                .commit()

            toggle = ActionBarDrawerToggle(this@MainActivity,drawerLayout, R.string.open_menu, R.string.close_menu)

            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            navView.setNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.firstOption->{
                        navController.navigate(R.id.homeFragment)
                        drawerLayout.closeDrawer(GravityCompat.START)
                    }
                    R.id.secondOption->{
                        navController.navigate(R.id.secondFragment)
                        drawerLayout.closeDrawer(GravityCompat.START)
                    }
                }
                true
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return NavigationUI.navigateUp(navController, drawerLayout)
    }
}