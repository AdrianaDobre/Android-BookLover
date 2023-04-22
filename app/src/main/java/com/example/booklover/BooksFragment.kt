package com.example.booklover

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.booklover.databinding.FragmentBooksBinding
import com.example.booklover.models.Book
import com.google.firebase.database.*

const val BOOK_ID = "book_id"

class BooksFragment : Fragment() {
    lateinit var binding: FragmentBooksBinding
    lateinit var userId: String
    lateinit var adapter: CustomAdapter
    lateinit var databaseReference: DatabaseReference

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBooksBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        databaseReference = (activity as MainActivity).databaseReference
        adapter = CustomAdapter (
            onItemClick = { book ->
            val bundle = Bundle()
            bundle.putString(BOOK_ID,book.id ?: "")

            val fragment = BookDetailsFragment()
            fragment.arguments = bundle

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, null)
                .addToBackStack(null)
                .commit()
        },
            onCheckBox = { })

        binding.recyclerView.adapter = adapter

        binding.floatingAdd.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AddBookFragment::class.java, null)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onResume() {
        super.onResume()
        userId = (activity as MainActivity).userId
        val booksReference = databaseReference.child("users").child(userId).child("books")

        booksReference.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val items = mutableListOf<Book>()
                for (snapshot in dataSnapshot.children) {
                    val item = snapshot.getValue(Book::class.java)
                    if (item != null) {
                        item.id = snapshot.key
                        item.selected = snapshot.child("selected").value.toString().toBoolean()
                        item.let { items.add(it) }
                    }
                }
                adapter.update(items, userId)
                requireActivity().supportFragmentManager.popBackStack()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(Register.TAG, "Failed to load books: ${error.message}")
            }
        })
    }
}