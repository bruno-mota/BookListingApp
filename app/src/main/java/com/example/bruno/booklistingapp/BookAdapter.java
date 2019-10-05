package com.example.bruno.booklistingapp;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import java.util.ArrayList;
public class BookAdapter extends ArrayAdapter<Books> {
    public BookAdapter(Context context, ArrayList<Books> books){
        super(context, 0 , books);
    }
    @Override
    public View getView(int position, View convertview, ViewGroup parent){
        View listView = convertview;
        if(listView == null){
            listView = LayoutInflater.from(getContext()).inflate(R.layout.book_list, parent, false);
        }
        Books books = getItem(position);
        TextView titleView = (TextView)listView.findViewById(R.id.title);
        titleView.setText(books.getTitle());
        TextView authorView = (TextView)listView.findViewById(R.id.author);
        authorView.setText(books.getAuthor());
        return listView;
    }
}
