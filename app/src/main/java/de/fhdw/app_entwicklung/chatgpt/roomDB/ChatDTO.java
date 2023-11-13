package de.fhdw.app_entwicklung.chatgpt.roomDB;

import android.content.Context;

import androidx.room.Room;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.time.chrono.ChronoLocalDateTime;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.util.List;

import de.fhdw.app_entwicklung.chatgpt.model.Message;

public class ChatDTO {

    private static AppDatabase chatDB;

    public ChatDTO(Context AppContext) {
        if (chatDB == null)
            chatDB = Room.databaseBuilder(AppContext, AppDatabase.class, "chatDB").build();
    }

    public Object getAllChats() throws Exception {

        try {
            List<Chat> dbChats;
            List<de.fhdw.app_entwicklung.chatgpt.model.Chat> returnList;

            dbChats = chatDB.chatDAO().getAll();

            if (dbChats == null)
                throw new Exception("DB_IS_EMPTY");


            returnList = new ArrayList<>();

            String jsonMessages;
            List<Message> messages;
            String dateString;
            LocalDateTime dtToSave;

            for (Chat c : dbChats) {

                /*Init working Variables*/
                jsonMessages = "";
                messages = new ArrayList<Message>();
                dtToSave = LocalDateTime.parse(c.creationDate);

                /*Init toAdd Instance*/
                de.fhdw.app_entwicklung.chatgpt.model.Chat toAdd =
                        new de.fhdw.app_entwicklung.chatgpt.model.Chat(dtToSave);

                /**/
                jsonMessages = c.jsonMessages;
                JsonMapper jm = new JsonMapper();
                messages = jm.convertValue(jsonMessages, List.class);

                for (Message m : messages) {
                    toAdd.addMessage(m);
                }

                returnList.add(toAdd);
            }

            return returnList;
        }
        catch (Exception e) {
            return e;
        }

    }
}
