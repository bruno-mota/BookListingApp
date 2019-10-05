package com.example.bruno.booklistingapp;
import android.text.TextUtils;
import android.util.Log;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import static com.example.bruno.booklistingapp.BookSearch.LOG_TAG;
/**
 * Created by Bruno on 12/29/2017.
 */
public class QueryUtils {
    private QueryUtils(){
    }
    public static List<Books> fetchBooks(String URLrequest){
        URL url = createURL(URLrequest);
        String json = null;
        try{
            json = makeHttpRequest(url);
        }catch(IOException e){
            Log.e(LOG_TAG, "Error making http request", e);
        }
        List<Books> books = extractFromJSON(json);
        return books;
    }
    private static URL createURL(String jsonurl){
        URL url = null;
        try{
            url = new URL(jsonurl);
        }catch(MalformedURLException e){
            Log.e(LOG_TAG, "Couldn't build url", e);
        }
        return url;
    }
    private static String makeHttpRequest(URL url) throws  IOException{
        String json = "";
        if(url == null){
            return json;
        }
        HttpURLConnection connection = null;
        InputStream inputStream = null;
        try{
            connection = (HttpURLConnection) url.openConnection();
            connection.setReadTimeout(10000);
            connection.setConnectTimeout(15000);
            connection.setRequestMethod("GET");
            connection.connect();
            if (connection.getResponseCode() == 200) {
                inputStream = connection.getInputStream();
                json = readFromStream(inputStream);
            } else {
                Log.e(LOG_TAG, "Error response code: " + connection.getResponseCode());
            }
        }
        catch(IOException e){
            Log.e(LOG_TAG, "Problem getting JSON results", e);
        }
        finally {
            if (connection != null) {
                connection.disconnect();
            }
            if (inputStream != null) {
                // Closing the input stream could throw an IOException, which is why
                // the makeHttpRequest(URL url) method signature specifies than an IOException
                // could be thrown.
                inputStream.close();
            }
        }
        return json;
    }
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder result = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                result.append(line);
                line = reader.readLine();
            }
        }
        return result.toString();
    }
    private static List<Books> extractFromJSON(String bookJson){
        if (TextUtils.isEmpty(bookJson)) {
            return null;
        }
        List<Books> books = new ArrayList<>();
        try {
            JSONObject root = new JSONObject(bookJson);
            JSONArray jsonItems = root.optJSONArray("items");
            if(jsonItems != null) {
                for (int i = 0; i < jsonItems.length(); i++) {
                    JSONObject jsonObject = jsonItems.getJSONObject(i);
                    JSONObject jsonVolume = jsonObject.getJSONObject("volumeInfo");
                    String title = jsonVolume.getString("title");
                    if (jsonVolume.has("authors")) {
                        JSONArray jsonAuthor = jsonVolume.getJSONArray("authors");
                        String author = jsonAuthor.getString(0);
                        Books book = new Books(title, author);
                        books.add(book);
                    }
                }
            }


        }catch(JSONException e){
            Log.e("QueryUtils", "Problem parsing", e);
        }
        return books;
    }
}
