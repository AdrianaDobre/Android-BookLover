package com.example.booklover

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.annotation.NonNull
import com.example.booklover.databinding.ActivityRegisterBinding
import com.google.firebase.database.*

class Register : AppCompatActivity() {
    lateinit var binding: ActivityRegisterBinding
    val databaseReference: DatabaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://booklover-96666-default-rtdb.firebaseio.com/")

    companion object{
        const val TAG = "Register"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
//        setContentView(R.layout.activity_register)
        setContentView(binding.root)

        val firstName: EditText = binding.firstName
        val lastName: EditText = binding.lastName
        val email: EditText = binding.email
        val password: EditText = binding.password
        val conPassword: EditText = binding.conPassword
        val phone: EditText = binding.phone

        val registerBtn: Button = binding.registerBtn
        val loginNowBtn: TextView = binding.loginNowBtn

        registerBtn.setOnClickListener {
            val firstNameTxt: String = firstName.text.toString()
            val lastNameTxt: String = lastName.text.toString()
            val emailTxt: String = email.text.toString()
            val passwordTxt: String = password.text.toString()
            val conPasswordTxt: String = conPassword.text.toString()
            val phoneTxt: String = phone.text.toString()

            if(firstNameTxt.isEmpty() || lastNameTxt.isEmpty() || emailTxt.isEmpty() || passwordTxt.isEmpty() || conPasswordTxt.isEmpty()){
                Toast.makeText(this, "Please fill in all the fields", Toast.LENGTH_SHORT).show()
            }
            else if (!passwordTxt.equals(conPasswordTxt)){
                Toast.makeText(this, "The passwords are not matching", Toast.LENGTH_SHORT).show()
            }
            else {
                val usersReference = databaseReference.child("users")

                usersReference.orderByChild("email").equalTo(emailTxt).addListenerForSingleValueEvent(object: ValueEventListener {
                    val userId: String = usersReference.push().key.toString()

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            Toast.makeText(this@Register, "User already registered", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            databaseReference.child("users").child(userId).child("firstName").setValue(firstNameTxt)
                            databaseReference.child("users").child(userId).child("lastName").setValue(lastNameTxt)
                            databaseReference.child("users").child(userId).child("email").setValue(emailTxt)
                            databaseReference.child("users").child(userId).child("password").setValue(passwordTxt)
                            databaseReference.child("users").child(userId).child("phone").setValue(phoneTxt)

                            Toast.makeText(this@Register, "User registered successfully", Toast.LENGTH_SHORT).show()
                            finish()
                        }
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(TAG, "Error checking if user is registered: ${error.message}")
                    }

                })
            }
        }

        loginNowBtn.setOnClickListener {
            finish()
        }

    }
}