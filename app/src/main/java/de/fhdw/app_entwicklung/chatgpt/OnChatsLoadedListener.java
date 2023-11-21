package de.fhdw.app_entwicklung.chatgpt;

import java.util.List;

import de.fhdw.app_entwicklung.chatgpt.model.Chat;

public interface OnChatsLoadedListener {
    void onChatsLoaded(List<Chat> chats);

    void onError(Exception e);
}