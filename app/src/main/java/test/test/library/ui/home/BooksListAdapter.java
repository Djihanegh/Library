package test.test.library.ui.home;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import test.test.library.R;
import test.test.library.models.Books;

public class BooksListAdapter extends RecyclerView.Adapter<BooksListAdapter.BookViewHolder> {
    
    public ArrayList<Books> books;
    public OnItemClickListener onItemClickListener;
    public OnItemLongClickListener onItemLongClickListener;
    
    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new BookViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.books_list_item, parent, false));
    }
    
    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, final int position) {
        holder.view.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (onItemLongClickListener != null)
                    onItemLongClickListener.onItemSelected(position, view);
                return true;
            }
        });
        holder.view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (onItemClickListener != null)
                    onItemClickListener.onItemSelected(position, view);
            }
        });
        holder.bindView(books.get(position));
    }
    
    @Override
    public int getItemCount() {
        return books.size();
    }
    
    public static class BookViewHolder extends RecyclerView.ViewHolder {
        public View view;
        
        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;
        }
        
        public void bindView(Books book) {
            ((TextView) view.findViewById(R.id.book_title)).setText(book.titre);
            ((TextView) view.findViewById(R.id.book_author)).setText(book.auteur);
            ((TextView) view.findViewById(R.id.book_resume)).setText(book.resume);
            ((TextView) view.findViewById(R.id.book_keywords)).setText(book.motCle);
        }
    }
}
