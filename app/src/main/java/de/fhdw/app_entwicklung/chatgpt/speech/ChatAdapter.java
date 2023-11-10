package de.fhdw.app_entwicklung.chatgpt.speech;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import de.fhdw.app_entwicklung.chatgpt.R;
import de.fhdw.app_entwicklung.chatgpt.model.Author;
import de.fhdw.app_entwicklung.chatgpt.model.Message;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private final List<Message> messages;
    private final Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView chatLeft;
        private Context context;

        public ViewHolder(View view, Context context) {
            super(view);
            chatLeft = (TextView) view.findViewById(R.id.left_chat_text);
            this.context = context;
            //chatRight = (TextView) view.findViewById(R.id.right_chat_text);
        }

        public void bind(Message message) {
            if (message.author.equals(Author.Assistant) || message.author.equals(Author.System)) {
                chatLeft.setText(message.message);
                chatLeft.setBackgroundTintList(context.getColorStateList(R.color.primary));
            } else {
                chatLeft.setText(message.message);
                chatLeft.setBackgroundTintList(context.getColorStateList(R.color.secondary));
            }

        }
    }

    public ChatAdapter(List<Message> messages, Context context) {
        this.messages = messages;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item, parent, false);
        return new ViewHolder(view, context);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.bind(message);
        Log.d("Adapter", "bind msg");
    }

    @Override
    public int getItemCount() {
        Log.d("Adapter", "getItemCount: " + messages.size());
        return messages.size();
    }
}
