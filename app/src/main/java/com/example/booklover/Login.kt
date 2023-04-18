package com.example.booklover

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.booklover.databinding.ActivityLoginBinding
import com.example.booklover.models.User
import com.google.firebase.database.*
import org.w3c.dom.Text

class Login : AppCompatActivity() {
    lateinit var binding : ActivityLoginBinding
    val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://booklover-96666-default-rtdb.firebaseio.com/")

    companion object{
        const val TAG = "Login"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)
//        setContentView(R.layout.activity_login)

        val email: EditText = binding.email
        val password: EditText = binding.password
        val loginBtn: Button = binding.LoginBtn
        val registerNowBtn: TextView = binding.registerNowBtn

        loginBtn.setOnClickListener {
            val textEmailAddress: String = email.text.toString()
            val passwordTxt: String = password.text.toString()

            if (textEmailAddress.isEmpty() || passwordTxt.isEmpty()){
                Toast.makeText(this, "Please enter your email or password", Toast.LENGTH_SHORT).show()
            }
            else{
                val usersReference = databaseReference.child("users")

                usersReference.orderByChild("email").equalTo(textEmailAddress).addListenerForSingleValueEvent(object: ValueEventListener{
                    override fun onDataChange(snapshot: DataSnapshot) {
                        for(userSnapshot in snapshot.children){
                            val user = userSnapshot.getValue(User::class.java)
                            if (user != null && user.password == passwordTxt){
                                Toast.makeText(this@Login, "Successfully logged in", Toast.LENGTH_SHORT).show()

                                startActivity(Intent(this@Login, MainActivity::class.java))
                                finish()
                            }
                            else{
                                Toast.makeText(applicationContext, "Invalid email or password", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(Register.TAG, "Error checking if user has an account or not: ${error.message}")
                    }

                })

            }

        }

        registerNowBtn.setOnClickListener {
            startActivity(Intent(this, Register::class.java))
        }
    }
}