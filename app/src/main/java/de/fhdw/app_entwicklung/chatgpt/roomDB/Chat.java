package de.fhdw.app_entwicklung.chatgpt.roomDB;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import de.fhdw.app_entwicklung.chatgpt.model.Message;

@Entity
public class Chat {
    @PrimaryKey
    private final java.time.LocalDateTime creationDate = LocalDateTime.now();
    @ColumnInfo(name="messages")
    private final List<Message> messages = new ArrayList<>();
}
