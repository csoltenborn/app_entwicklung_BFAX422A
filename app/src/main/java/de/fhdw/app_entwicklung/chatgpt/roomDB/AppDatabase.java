package de.fhdw.app_entwicklung.chatgpt.roomDB;

import androidx.room.Database;
import androidx.room.RoomDatabase;

@Database(entities = {Chat.class}, version = 1)
public abstract class AppDatabase extends RoomDatabase {
    public abstract ChatDAO chatDAO();
}
