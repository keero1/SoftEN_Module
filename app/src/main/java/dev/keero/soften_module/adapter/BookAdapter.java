package dev.keero.soften_module.adapter;

import android.annotation.SuppressLint;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.Objects;

import dev.keero.soften_module.databinding.BookItemLayoutBinding;
import dev.keero.soften_module.model.Book;
import dev.keero.soften_module.utils.BookItemClickListener;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.ViewHolder>{
    private static final String TAG = "BookAdapter";
    private ArrayList<Book> dataSet;

    // listener
    private BookItemClickListener listener;
    public void setBookItemClickListener(BookItemClickListener listener){
        this.listener = listener;
    }

    //initialize the dataset
    public BookAdapter(ArrayList<Book> dataSet){
        this.dataSet = dataSet;
    }

    // extends RecyclerView.ViewHolder and holds references to the views
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{
        private final BookItemLayoutBinding binding;

        // holds the reference of the single item in the list.
        public ViewHolder(BookItemLayoutBinding binding){
            super(binding.getRoot());
            this.binding = binding;

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view){
            if(listener != null){
                listener.onBookItemClickListener(getAdapterPosition());
            }
        }

        public void setBinding(Book book){

            binding.bookTitle.setText(book.getBookName());
            binding.bookAuthor.setText(book.getBookAuthor());
            binding.bookType.setText(book.getBookType());
        }
    }

    //filter
    @SuppressLint("NotifyDataSetChanged")
    public void setFilteredList(ArrayList<Book> filteredList){
        this.dataSet = Objects.requireNonNull(filteredList);
        notifyDataSetChanged();
    }

    //Create new views (Invoked by layout manager)
    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BookItemLayoutBinding binding = BookItemLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new ViewHolder(binding);
    }

    //Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int position){
        Log.d(TAG, "Element " + position + " set.");
        Book item = dataSet.get(position);
        viewHolder.setBinding(item);
    }

    @Override
    public int getItemCount(){
        return dataSet.size();
    }



}
