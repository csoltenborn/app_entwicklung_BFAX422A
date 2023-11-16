package de.fhdw.app_entwicklung.chatgpt.roomDB;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.DeleteTable;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface ChatDAO {
    @Query("SELECT * FROM chat")
    List<Chat> getAll();

    @Insert
    void insertAll(Chat... chats);

    @Query("DELETE FROM chat")
    void deleteAllChats();

    @Query("UPDATE Chat SET cid = 0;")
    void resetAutoIncrement();


}
