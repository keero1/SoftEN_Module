package dev.keero.soften_module.adapter;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import dev.keero.soften_module.databinding.BookItemLayoutBinding;
import dev.keero.soften_module.model.Book;
import dev.keero.soften_module.utils.ItemClickListener;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {
    private static final String TAG = "CartAdapter";
    private final ArrayList<Book> dataSet; // ArrayList to store book IDs
    private ItemClickListener listener;

    public void setItemClickListener(ItemClickListener listener){
        this.listener = listener;
    }

    public CartAdapter(ArrayList<Book> dataSet) {
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
                listener.onItemClickListener(getAdapterPosition());
            }
        }

        public void setBinding(Book book){

            binding.bookTitle.setText(book.getBookName());
            binding.bookAuthor.setText(book.getBookAuthor());
            binding.bookType.setText(book.getBookType());
        }
    }
    //Create new views (Invoked by layout manager)
    @NonNull
    @Override
    public CartAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        BookItemLayoutBinding binding = BookItemLayoutBinding.inflate(
                LayoutInflater.from(parent.getContext()), parent, false);
        return new CartAdapter.ViewHolder(binding);
    }

    //Replace the contents of a view (invoked by the layout manager)
    @Override
    public void onBindViewHolder(CartAdapter.ViewHolder viewHolder, final int position){
        Log.d(TAG, "Element " + position + " set.");
        Book item = dataSet.get(position);
        viewHolder.setBinding(item);
    }

    @Override
    public int getItemCount(){
        return dataSet.size();
    }


}
