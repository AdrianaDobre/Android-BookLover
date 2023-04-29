package com.example.booklover

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.booklover.databinding.FragmentAddBookBinding
import com.example.booklover.databinding.FragmentProfileBinding
import com.example.booklover.models.Book
import com.example.booklover.models.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class Profile : Fragment() {
    lateinit var databaseReference: DatabaseReference
    lateinit var userId: String
    lateinit var binding: FragmentProfileBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentProfileBinding.inflate(inflater, container, false);
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = (activity as MainActivity).userId
        databaseReference = (activity as MainActivity).databaseReference

        val userReference = databaseReference.child("users").child(userId)

        userReference.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                if (snapshot.exists()) {
                    val data: User? = snapshot.getValue(User::class.java)
                    if (data != null) {
                        binding.fullNameProfile.text = SpannableStringBuilder(data.firstName + " " + data.lastName)
                        binding.emailProfile.text = SpannableStringBuilder(data.email)
                        binding.email.text = SpannableStringBuilder(data.email)
                        binding.firstName.text = SpannableStringBuilder(data.firstName)
                        binding.lastName.text = SpannableStringBuilder(data.lastName)
                        binding.phoneNumberEdittext.text = SpannableStringBuilder(data.phone)
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(BookDetailsFragment.TAG, "Error retrieving data about the user: ${error.message}")
            }
        })

        binding.editButton.setOnClickListener {
            val bookUpdated = hashMapOf<String, Any>(
                "email" to binding.email.text.toString(),
                "firstName" to binding.firstName.text.toString(),
                "lastName" to binding.lastName.text.toString(),
                "phone" to binding.phoneNumberEdittext.text.toString(),
            )
            userReference.updateChildren(bookUpdated)
                .addOnSuccessListener {
                }
                .addOnFailureListener { error ->
                    Log.e(BookDetailsFragment.TAG, "Error updating the user: ${error.message}")
                }
            requireActivity().supportFragmentManager.popBackStack()
        }
    }
}