package de.fhdw.app_entwicklung.chatgpt.model;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

public class Message implements Parcelable {
    public final Date date;
    public final Author author;
    public final String message;
    public final String name;
    public final String color;

    public Message(Author author, String message, String name, String color) {
        this(new Date(), author, message, name, color);
    }

    public Message(Date date, Author author, String message, String name, String color) {
        this.date = date;
        this.author = author;
        this.message = message;
        this.name = name;
        this.color = color;
    }

    protected Message(Parcel in) {
        this(new Date(in.readLong()), Author.valueOf(in.readString()), in.readString(), in.readString(), in.readString());
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeLong(date.getTime());
        dest.writeString(author.name());
        dest.writeString(message);
        dest.writeString(name);
        dest.writeString(color);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };
}