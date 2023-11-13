package de.fhdw.app_entwicklung.chatgpt.roomDB;

import android.content.Context;

import androidx.room.Room;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.json.JsonMapper;

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

    public Object saveAllChats(List<de.fhdw.app_entwicklung.chatgpt.model.Chat> chatsToSave){
        try{

            List<Chat> chatsForDB = new ArrayList<>();

            for (de.fhdw.app_entwicklung.chatgpt.model.Chat c : chatsToSave) {
                chatsForDB.add(convertChat(c));
            }

            // Delete all Chats for avoiding duplicates
            for (Chat c : chatsForDB)
                chatDB.chatDAO().deleteChats(c);

            // Insert all Chats in Database
            for (Chat c : chatsForDB)
                chatDB.chatDAO().insertAll(c);

            return 0;
        }
        catch(Exception e){
            return e;
        }
    }

    private Chat convertChat(de.fhdw.app_entwicklung.chatgpt.model.Chat input) throws JsonProcessingException {

        JsonMapper jm = new JsonMapper();
        Chat output = new Chat();

        output.creationDate = jm.writeValueAsString(input.getCreationDate());
        output.jsonMessages = jm.writeValueAsString(input.getMessages());

        return output;
    }
}
