package com.example.bruno.booklistingapp;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.List;
public class BookSearch extends AppCompatActivity {
    private BookAdapter madapter;
    private ListView bookListView;
    private static String REQUEST_URL;
    static final String SEARCH_STATE = "searchResults";
    private TextView emptyData;
    public static final String LOG_TAG = BookSearch.class.getName();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_search);
        bookListView = (ListView)findViewById(R.id.list);
        emptyData = (TextView)findViewById(R.id.empty);
        emptyData.setVisibility(View.GONE);
        madapter = new BookAdapter(this, new ArrayList<Books>());
        bookListView.setAdapter(madapter);
        final EditText searchBox = (EditText)findViewById(R.id.search_box);
        final Button searchButton = (Button)findViewById(R.id.button);
        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isInternetOn()) {
                    REQUEST_URL = "https://www.googleapis.com/books/v1/volumes?q=";
                    String searchURL = searchBox.getText().toString();
                    if (searchURL.isEmpty()) {
                        emptyData.setVisibility(View.VISIBLE);
                        return;
                    }
                    REQUEST_URL = REQUEST_URL + searchURL;
                    Log.e(LOG_TAG, REQUEST_URL);
                    BookAsyncTask task = new BookAsyncTask();
                    task.execute(REQUEST_URL);
                } else {
                    Toast.makeText(BookSearch.this, R.string.is_empty, Toast.LENGTH_LONG).show();
                }
            }
        });
        if(savedInstanceState!=null) {
            Books[] books =(Books[]) savedInstanceState.getParcelableArray(SEARCH_STATE);
            Log.e(LOG_TAG, "Does it reach here?");
            madapter.addAll(books);
        }
    }
    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        super.onSaveInstanceState(savedInstanceState);
        Books[] books = new Books[madapter.getCount()];
        for(int i = 0; i<books.length; i++){
            books[i] = madapter.getItem(i);
        }
        savedInstanceState.putParcelableArray(SEARCH_STATE, (Parcelable[]) books);
    }
    private boolean isInternetOn(){
        ConnectivityManager connectivityManager =
                (ConnectivityManager)getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        boolean isConnected = networkInfo != null && networkInfo.isConnectedOrConnecting();
        return isConnected;
    }
    private class BookAsyncTask extends AsyncTask<String, Void, List<Books>> {
        @Override
        protected List<Books> doInBackground(String... url) {
            if (url.length < 1 || url[0] == null) {
                return null;
            }
            List<Books> list = QueryUtils.fetchBooks(url[0]);
            return list;
        }
        @Override
        protected void onPostExecute(List<Books> data) {
            madapter.clear();
            if(data.isEmpty()){
                emptyData.setVisibility(View.VISIBLE);
            }else{
                emptyData.setVisibility(View.GONE);
            }
            if (data != null && !data.isEmpty()) {
                madapter.addAll(data);
            }
        }
    }
}
