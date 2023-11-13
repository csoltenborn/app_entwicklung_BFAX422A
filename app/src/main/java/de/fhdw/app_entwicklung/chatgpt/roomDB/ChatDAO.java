package de.fhdw.app_entwicklung.chatgpt.roomDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;

import java.util.List;

@Dao
public interface ChatDAO {
    @Query("SELECT * FROM chat")
    List<Chat> getAll();

    @Insert
    void insertAll(Chat... chats);

    @Delete
    void delete(Chat chat);

    @Delete
    void deleteChats(Chat... chats);
}
