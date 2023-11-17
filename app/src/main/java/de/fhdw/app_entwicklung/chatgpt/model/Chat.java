package de.fhdw.app_entwicklung.chatgpt.model;

import android.os.Parcel;
import android.os.Parcelable;

import org.jetbrains.annotations.NotNull;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Chat implements Parcelable {

    private final List<Message> messages = new ArrayList<>();
    private java.time.LocalDateTime creationDate;

    public java.time.LocalDateTime getCreationDate(){
        return this.creationDate;
    }

    public Chat() {
        this.creationDate = LocalDateTime.now();
    }

    public Chat(java.time.LocalDateTime creationDate) {
        this.creationDate = creationDate;
    }

    protected Chat(Parcel in) {
        in.readList(messages, Message.class.getClassLoader());
    }

    public void addMessage(Message message) {
        messages.add(message);
    }

    public List<Message> getMessages() {
        return Collections.unmodifiableList(messages);
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeList(messages);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Chat> CREATOR = new Creator<Chat>() {
        @Override
        public Chat createFromParcel(Parcel in) {
            return new Chat(in);
        }

        @Override
        public Chat[] newArray(int size) {
            return new Chat[size];
        }
    };


    @NotNull
    @Override
    public String toString(){
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss / dd-MM-yyyy");
        String formattedDate = creationDate.format(formatter);
        formattedDate = formattedDate.split("/")[0] + "⏱️ / " + formattedDate.split("/")[1];
        return formattedDate;
    }
}