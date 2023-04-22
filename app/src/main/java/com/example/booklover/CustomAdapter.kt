package com.example.booklover

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.booklover.databinding.BookItemBinding
import com.example.booklover.models.Book

class CustomAdapter(
    private var onItemClick: (Book) -> Unit
) : RecyclerView.Adapter<CustomAdapter.BookHolder>(){
    lateinit var binding: BookItemBinding
    var books = mutableListOf<Book>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BookHolder {
        binding = BookItemBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return BookHolder(binding)
    }

    override fun onBindViewHolder(holder: BookHolder, position: Int) {
        val item = books[position]
        holder.title.text = item.title
        holder.description.text = item.description
        holder.constraint.setOnClickListener {
            onItemClick(item)
        }
    }


    override fun getItemCount(): Int {
        return books.size
    }

    fun update(list: List<Book>) {
        books.clear();
        books.addAll(list);
        notifyDataSetChanged()
    }

    class BookHolder(binding: BookItemBinding) : RecyclerView.ViewHolder(binding.root) {
        val title = binding.title
        val description = binding.description
        val constraint = binding.constraint
    }

}