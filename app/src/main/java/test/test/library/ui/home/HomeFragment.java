package test.test.library.ui.home;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import java.util.zip.Inflater;

import test.test.library.R;
import test.test.library.database.DBHelper;
import test.test.library.models.Books;

public class HomeFragment extends Fragment {
    
    private RecyclerView recyclerView;
    private Button button;
    private BooksListAdapter adapter;
    private DBHelper database;
    private int currentPosition;
    
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_home, container, false);
        recyclerView = root.findViewById(R.id.books_list);
        recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(),
                DividerItemDecoration.HORIZONTAL));
        registerForContextMenu(recyclerView);
        button = root.findViewById(R.id.create_book_button);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateBookDialog();
            }
        });
        return root;
    }
    
    @Override
    public void onResume() {
        super.onResume();
        adapter = new BooksListAdapter();
        adapter.onItemLongClickListener = new OnItemLongClickListener() {
            @Override
            public boolean onItemSelected(int position, View view) {
                currentPosition = position;
                view.showContextMenu();
                return false;
            }
        };
        refresh();
    }
    
    private void refresh() {
        adapter.books = database.ListAllBooks();
        adapter.notifyDataSetChanged();
    }
    
    private void showCreateBookDialog() {
        final View linearLayout = LayoutInflater.from(requireContext()).inflate(R.layout.add_book_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setCancelable(true)
                .setView(linearLayout)
                .create();
        dialog.show();
        linearLayout.findViewById(R.id.dialog_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.insertBooks(new Books(
                        0,
                        "no name",
                        ((EditText) linearLayout.findViewById(R.id.add_book_author)).getText().toString(),
                        ((EditText) linearLayout.findViewById(R.id.add_book_title)).getText().toString(),
                        ((EditText) linearLayout.findViewById(R.id.add_book_keywords)).getText().toString(),
                        ((EditText) linearLayout.findViewById(R.id.add_book_resume)).getText().toString()
                ));
                refresh();
                dialog.dismiss();
                Toast.makeText(requireContext(), getString(R.string.book_add_success), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void showUpdateBookDialog(int position) {
        final View linearLayout = LayoutInflater.from(requireContext()).inflate(R.layout.add_book_dialog, null);
        final AlertDialog dialog = new AlertDialog.Builder(requireContext())
                .setCancelable(true)
                .setView(linearLayout)
                .create();
        dialog.show();
        ((TextView) linearLayout.findViewById(R.id.dialog_title)).setText(R.string.update_book);
        ((Button) linearLayout.findViewById(R.id.dialog_button)).setText(R.string.update_book);
        final EditText author = ((EditText) linearLayout.findViewById(R.id.add_book_author));
        final EditText title = ((EditText) linearLayout.findViewById(R.id.add_book_title));
        final EditText keywords = ((EditText) linearLayout.findViewById(R.id.add_book_keywords));
        final EditText resume = ((EditText) linearLayout.findViewById(R.id.add_book_resume));
        final Books book = adapter.books.get(position);
        author.setText(book.auteur);
        title.setText(book.titre);
        keywords.setText(book.motCle);
        resume.setText(book.resume);
        
        linearLayout.findViewById(R.id.dialog_button).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                database.updateBooks(new Books(
                        book.id,
                        "no name",
                        author.getText().toString(),
                        title.getText().toString(),
                        keywords.getText().toString(),
                        resume.getText().toString()
                ));
                refresh();
                dialog.dismiss();
                Toast.makeText(requireContext(), getString(R.string.book_add_success), Toast.LENGTH_SHORT).show();
            }
        });
    }
    
    private void showDeleteBookDialog(final int position) {
        AlertDialog alertDialog = new AlertDialog
                .Builder(requireContext())
                .setTitle(R.string.delete_book)
                .setCancelable(true)
                .setPositiveButton(R.string.delete_book, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        database.deleteBook(adapter.books.get(position).id);
                        refresh();
                    }
                })
                .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.dismiss();
                    }
                }).create();
        alertDialog.show();
    }
    
    @Override
    public void onCreateContextMenu(@NonNull ContextMenu menu, @NonNull View v, @Nullable ContextMenu.ContextMenuInfo menuInfo) {
        final MenuInflater inflater = requireActivity().getMenuInflater();
        inflater.inflate(R.menu.context_menu, menu);
    }
    
    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_update:
                showUpdateBookDialog(currentPosition);
                break;
            case R.id.action_delete:
                showDeleteBookDialog(currentPosition);
                break;
        }
        return true;
    }
}