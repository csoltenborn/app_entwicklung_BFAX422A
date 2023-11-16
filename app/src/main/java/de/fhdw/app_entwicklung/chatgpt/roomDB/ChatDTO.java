package de.fhdw.app_entwicklung.chatgpt.roomDB;

import android.content.Context;
import android.util.Log;

import androidx.room.Room;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.json.JsonMapper;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.util.ArrayList;
import java.util.List;

import de.fhdw.app_entwicklung.chatgpt.MainActivity;
import de.fhdw.app_entwicklung.chatgpt.model.Message;

public class ChatDTO {

    private static AppDatabase chatDB;

    public ChatDTO(Context AppContext) {
        if (chatDB == null) {
            chatDB = Room.databaseBuilder(AppContext, AppDatabase.class, "chatDB").build();
        }
    }

    public void getAllChats(OnChatsLoadedListener listener) {
        MainActivity.backgroundExecutorService.execute(() -> {
            try {
                List<Chat> dbChats = chatDB.chatDAO().getAll();

                if (dbChats == null) {
                    listener.onError(new Exception("DB_IS_EMPTY"));
                    return;
                }

                List<de.fhdw.app_entwicklung.chatgpt.model.Chat> returnList = new ArrayList<>();

                for (Chat c : dbChats) {
                    /*Init working Variables*/
                    String jsonMessages = "";
                    List<Message> messages;

                    /*Deserializing creation Date*/
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern(" yyyy-MM-dd HH:mm:ss ");
                    String dateString = c.creationDate.replace('"', ' ');
                    LocalDateTime dtToSave = LocalDateTime.parse(dateString, formatter);

                    /*Init toAdd Instance*/
                    de.fhdw.app_entwicklung.chatgpt.model.Chat toAdd =
                            new de.fhdw.app_entwicklung.chatgpt.model.Chat(dtToSave);

                    /*get Messages from JSON*/
                    jsonMessages = c.jsonMessages;
                    JsonMapper jm = new JsonMapper();
                    messages = jm.readValue(jsonMessages, new TypeReference<List<Message>>() {});

                    /*Add Messages to temp Chat instance*/
                    for (Message m : messages) {
                        toAdd.addMessage(m);
                    }

                    returnList.add(toAdd);
                }

                // Calling Listener in Main Thread after loading Data successfully
                MainActivity.uiThreadHandler.post(() -> listener.onChatsLoaded(returnList));

            } catch (Exception e) {
                // Error handling and calling Listener in main Thread
                MainActivity.uiThreadHandler.post(() -> listener.onError(e));
            }
        });
    }

    public Object saveAllChats(List<de.fhdw.app_entwicklung.chatgpt.model.Chat> chatsToSave) {
        try {
            List<Chat> chatsForDB = new ArrayList<>();

            for (de.fhdw.app_entwicklung.chatgpt.model.Chat c : chatsToSave) {
                if(c.getMessages().size() > 0)
                    chatsForDB.add(convertChat(c));
            }

            MainActivity.backgroundExecutorService.execute(() -> {
                try {
                    // Delete all Chats for avoiding duplicates
                    chatDB.chatDAO().deleteChats(chatsForDB.toArray(new Chat[0]));

                    // Insert all Chats in Database
                    chatDB.chatDAO().insertAll(chatsForDB.toArray(new Chat[0]));

                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            });

            return 0;
        } catch (Exception e) {
            return e;
        }
    }

    private Chat convertChat(de.fhdw.app_entwicklung.chatgpt.model.Chat input) throws JsonProcessingException {
        JsonMapper jm = new JsonMapper();
        Chat output = new Chat();

        output.creationDate = jm.writeValueAsString(serializeLocalDateTime(input.getCreationDate()));
        List<Message> msgsToSave = input.getMessages();
        if(msgsToSave != null)
            output.jsonMessages = jm.writeValueAsString(msgsToSave);

        return output;
    }

    public interface OnChatsLoadedListener {
        void onChatsLoaded(List<de.fhdw.app_entwicklung.chatgpt.model.Chat> chats);

        void onError(Exception e);
    }

    private String serializeLocalDateTime(LocalDateTime localDateTime) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        return localDateTime.format(formatter);
    }
}
