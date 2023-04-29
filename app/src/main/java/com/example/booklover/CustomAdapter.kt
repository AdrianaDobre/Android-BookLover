package com.example.booklover

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.booklover.databinding.BookItemBinding
import com.example.booklover.models.Book
import com.google.firebase.database.FirebaseDatabase

class CustomAdapter(
    private var onItemClick: (Book) -> Unit,
    private var onCheckBox: (String) -> Unit
) : RecyclerView.Adapter<CustomAdapter.BookHolder>(){
    lateinit var binding: BookItemBinding
    var books = mutableListOf<Book>()
    var userId: String = ""

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
        binding = BookItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return BookHolder(binding)
    }

    override fun onBindViewHolder(holder: BookHolder, position: Int) {
        val item = books[position]
        holder.title.text = item.title
        holder.description.text = item.description
        holder.checkBox.isChecked = item.selected
        holder.constraint.setOnClickListener {
            onItemClick(item)
        }
        holder.checkBox.setOnCheckedChangeListener { _, isChecked ->
            onCheckBox(userId)
            item.selected = isChecked
            val databaseReference = FirebaseDatabase.getInstance().getReferenceFromUrl("https://booklover-96666-default-rtdb.firebaseio.com/")
            item.id?.let { databaseReference.child("users").child(userId).child("books").child(it).child("selected").setValue(isChecked) }
        }
    }


    override fun getItemCount(): Int {
        return books.size
    }

    fun update(list: List<Book>, userId: String) {
        books.clear();
        books.addAll(list);
        this.userId = userId
        notifyDataSetChanged()
    }

    class BookHolder(binding: BookItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.title
        val description = binding.description
        val checkBox = binding.checkBox
        val constraint = binding.constraint
    }

}