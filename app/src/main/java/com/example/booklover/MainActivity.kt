package com.example.booklover

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.ActionBar
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.booklover.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var userId: String
    lateinit var databaseReference: DatabaseReference
    lateinit var sharedPreferences: SharedPreferences
    val KEY_SPLASH = "splash"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sharedPreferences = getSharedPreferences("Preferences", Context.MODE_PRIVATE)

        databaseReference = FirebaseDatabase.getInstance()
            .getReferenceFromUrl("https://booklover-96666-default-rtdb.firebaseio.com/")

        userId = intent.getStringExtra("userId").toString()

        if (!sharedPreferences.getBoolean(KEY_SPLASH, false)) {
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_layout, SplashFragment::class.java, null)
                .commit()
            sharedPreferences.edit().putBoolean(KEY_SPLASH, true).apply()
        } else {
            binding.bottomNavigationView.visibility = View.VISIBLE
            supportFragmentManager.beginTransaction()
                .add(R.id.frame_layout, BooksFragment::class.java, null)
                .commit()
        }

        binding.bottomNavigationView.setOnItemSelectedListener {
            when(it.itemId){
                R.id.home -> replaceFragment(BooksFragment())
                R.id.profile -> replaceFragment(Profile())
                else ->{
                }
            }
            true
        }

    }
    private fun replaceFragment(fragment : Fragment){
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.frame_layout,fragment)
        fragmentTransaction.commit()
    }
}