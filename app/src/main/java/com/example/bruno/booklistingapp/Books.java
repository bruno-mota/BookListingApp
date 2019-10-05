package com.example.bruno.booklistingapp;
import android.os.Parcel;
import android.os.Parcelable;
/**
 * Created by Bruno on 12/29/2017.
 */
public class Books implements Parcelable {
    private String mTitle;
    private String mAuthor;
    public Books(String title, String auhor){
        mTitle = title;
        mAuthor = auhor;
    }
    protected Books(Parcel in) {
        mTitle = in.readString();
        mAuthor = in.readString();
    }
    public String getTitle() {
        return mTitle;
    }
    public String getAuthor() {
        return mAuthor;
    }
    @Override
    public int describeContents() {
        return 0;
    }
    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(mAuthor);
        parcel.writeString(mTitle);

    }
    public static final Creator<Books> CREATOR = new Creator<Books>() {
        @Override
        public Books createFromParcel(Parcel in) {
            return new Books(in);
        }
        @Override
        public Books[] newArray(int size) {
            return new Books[size];
        }
    };
}
