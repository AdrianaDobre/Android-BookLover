package com.example.booklover

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.widget.Toast
import android.widget.Toolbar
import androidx.appcompat.app.ActionBarDrawerToggle
import com.example.booklover.databinding.ActivityMainBinding
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var toggle: ActionBarDrawerToggle
    lateinit var userId: String
    lateinit var databaseReference: DatabaseReference
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://booklover-96666-default-rtdb.firebaseio.com/")

        userId = intent.getStringExtra("userId").toString()

        binding.apply {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, BooksFragment::class.java, null)
                .commit()

            toggle = ActionBarDrawerToggle(this@MainActivity,drawerLayout, R.string.open_menu, R.string.close_menu)

            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()

            supportActionBar?.setDisplayHomeAsUpEnabled(true)

            navigationView.setNavigationItemSelectedListener {
                when(it.itemId){
                    R.id.firstOption->{
                        Toast.makeText(this@MainActivity, "First option", Toast.LENGTH_SHORT).show()
                    }
                    R.id.secondOption->{
                        Toast.makeText(this@MainActivity, "Second option", Toast.LENGTH_SHORT).show()
                    }
                    R.id.thirdOption->{
                        Toast.makeText(this@MainActivity, "Third option", Toast.LENGTH_SHORT).show()
                    }
                    R.id.lastOption->{
                        Toast.makeText(this@MainActivity, "Last option", Toast.LENGTH_SHORT).show()
                    }
                }
                true
            }
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (toggle.onOptionsItemSelected(item)){
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}