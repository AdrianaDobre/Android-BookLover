package com.example.booklover

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.Fragment
import com.example.booklover.databinding.FragmentBooksBinding
import com.example.booklover.models.Book
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

const val BOOK_ID = "book_id"

class BooksFragment : Fragment() {
    lateinit var binding: FragmentBooksBinding
    lateinit var userId: String
    lateinit var adapter: CustomAdapter
    lateinit var searchView: SearchView
    lateinit var databaseReference: DatabaseReference
    private lateinit var bookList: MutableList<Book>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentBooksBinding.inflate(inflater, container, false);
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bookList = mutableListOf()
        searchView = binding.searchView
        searchView.clearFocus()
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                return false;
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                filterList(newText)
                return true;
            }

        })
        databaseReference = (activity as MainActivity).databaseReference
        adapter = CustomAdapter (
            onItemClick = { book ->
            val bundle = Bundle()
            bundle.putString(BOOK_ID,book.id ?: "")

            val fragment = BookDetailsFragment()
            fragment.arguments = bundle

            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, fragment, null)
                .addToBackStack(null)
                .commit()
        },
            onCheckBox = { })

        binding.recyclerView.adapter = adapter

        binding.floatingAdd.setOnClickListener{
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.frame_layout, AddBookFragment::class.java, null)
                .addToBackStack(null)
                .commit()
        }

        binding.shareBooks.setOnClickListener{
            val titleList = bookList.filter { it.title != null }.map { it.title!! }
            val titles = titleList.joinToString("\n")
            val shareIntent = Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT,titles)
                type = "text/plain"
            }
            startActivity(Intent.createChooser(shareIntent, "Share books"))
        }
    }

    private fun filterList(text: String?) {
        val filteredList = bookList.filter { it.title?.contains(text ?: "", ignoreCase = true) ?: false }
        if (filteredList.isNotEmpty()){
            adapter.update(filteredList, userId)
        }
    }

    override fun onResume() {
        super.onResume()
        userId = (activity as MainActivity).userId
        val booksReference = databaseReference.child("users").child(userId).child("books")

        booksReference.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                bookList = mutableListOf<Book>()
                for (snapshot in dataSnapshot.children) {
                    val item = snapshot.getValue(Book::class.java)
                    if (item != null) {
                        item.id = snapshot.key
                        item.selected = snapshot.child("selected").value.toString().toBoolean()
                        item.let { bookList.add(it) }
                    }
                }
                adapter.update(bookList, userId)
//                requireActivity().supportFragmentManager.popBackStack()
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e(Register.TAG, "Failed to load books: ${error.message}")
            }
        })
    }
}