package com.example.booklover

import android.os.Bundle
import android.text.SpannableStringBuilder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.booklover.databinding.FragmentBookDetailsBinding
import com.example.booklover.models.Book
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class BookDetailsFragment : Fragment() {
    lateinit var binding: FragmentBookDetailsBinding
    lateinit var databaseReference: DatabaseReference
    lateinit var userId: String
    lateinit var bookId: String

    companion object {
        const val TAG = "BookDetails"
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBookDetailsBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        userId = (activity as MainActivity).userId
        databaseReference = (activity as MainActivity).databaseReference
        val bundle = this.arguments
        if (bundle != null) {
            bookId = bundle.getString(BOOK_ID).toString()
            if (bookId != "") {
                val bookReference =
                    databaseReference.child("users").child(userId).child("books").child(bookId)

                if (isAdded) {
                    bookReference.addValueEventListener(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.exists()) {
                                val data: Book? = snapshot.getValue(Book::class.java)
                                if (data != null) {
                                    binding.title.text = SpannableStringBuilder(data.title)
                                    binding.description.text =
                                        SpannableStringBuilder(data.description)
                                    binding.author.text = SpannableStringBuilder(data.author)
                                    binding.comment.text = SpannableStringBuilder(data.comment)
                                    binding.review.text =
                                        SpannableStringBuilder(data.review.toString())
                                }
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            Log.e(TAG, "Error retrieving data about the book: ${error.message}")
                        }
                    })


                    binding.editButton.setOnClickListener {
                        val bookUpdated = hashMapOf<String, Any>(
                            "title" to binding.title.text.toString(),
                            "description" to binding.description.text.toString(),
                            "author" to binding.author.text.toString(),
                            "comment" to binding.comment.text.toString(),
                            "review" to binding.review.text.toString().toInt()
                        )
                        bookReference.updateChildren(bookUpdated)
                            .addOnSuccessListener {
                            }
                            .addOnFailureListener { error ->
                                Log.e(TAG, "Error updating the book: ${error.message}")
                            }

                        requireActivity().supportFragmentManager.popBackStack()
                    }

                    binding.deleteButton.setOnClickListener {
                        bookReference.removeValue()
                            .addOnSuccessListener {
                            }
                            .addOnFailureListener { error ->
                                Log.e(TAG, "Error deleting the book: ${error.message}")
                            }
                        requireActivity().supportFragmentManager.popBackStack()
                    }
                }
            }
        }
    }
}