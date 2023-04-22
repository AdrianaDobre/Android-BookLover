package com.example.booklover

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.booklover.databinding.FragmentAddBookBinding
import com.example.booklover.models.Book
import com.google.firebase.database.*

class AddBookFragment : Fragment() {
    lateinit var binding: FragmentAddBookBinding
    lateinit var userId: String
    lateinit var databaseReference: DatabaseReference

    companion object{
        const val TAG = "AddBook"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentAddBookBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        userId = (activity as MainActivity).userId
        databaseReference = (activity as MainActivity).databaseReference
        binding.button.setOnClickListener {
            if (binding.title.text.toString().isEmpty() || binding.author.text.toString().isEmpty()){
                Toast.makeText(requireContext(), "Please fill in the title and the author of the book!", Toast.LENGTH_SHORT).show()
            } else{
                val booksReference = databaseReference.child("users").child(userId)

                booksReference.orderByChild("title").equalTo(binding.title.text.toString()).addListenerForSingleValueEvent(object:
                    ValueEventListener {
                    val bookId: String = booksReference.child("books").push().key.toString()

                    override fun onDataChange(snapshot: DataSnapshot) {
                        if (snapshot.exists()){
                            Toast.makeText(requireContext(), "Book already exits", Toast.LENGTH_SHORT).show()
                        }
                        else{
                            booksReference.child("books").child(bookId).child("title").setValue(binding.title.text.toString())
                            booksReference.child("books").child(bookId).child("description").setValue(binding.description.text.toString())
                            booksReference.child("books").child(bookId).child("author").setValue(binding.author.text.toString())
                            booksReference.child("books").child(bookId).child("comment").setValue(binding.comment.text.toString())
                            booksReference.child("books").child(bookId).child("review").setValue(binding.review.text.toString().toInt())
                            booksReference.child("books").child(bookId).child("selected").setValue(false)


                            Toast.makeText(requireContext(), "Book added successfully", Toast.LENGTH_SHORT).show()
                        }
                        requireActivity().supportFragmentManager.popBackStack()
                    }

                    override fun onCancelled(error: DatabaseError) {
                        Log.e(Register.TAG, "Error checking if book is registered: ${error.message}")
                    }

                })
            }

        }
    }
}