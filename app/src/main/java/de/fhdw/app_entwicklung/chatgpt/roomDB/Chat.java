package de.fhdw.app_entwicklung.chatgpt.roomDB;

import androidx.room.ColumnInfo;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity
public class Chat {

    @PrimaryKey(autoGenerate = true)
    public int cid;
    @ColumnInfo(name="creationDate")
    public String creationDate;
    @ColumnInfo(name="jsonMessages")
    public String jsonMessages;
}
