package de.fhdw.app_entwicklung.chatgpt.model;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

import de.fhdw.app_entwicklung.chatgpt.R;

public class ChatAdapter extends RecyclerView.Adapter<ChatAdapter.ViewHolder> {
    private final List<Message> messages;
    private final Context context;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private final TextView chatLeft;
        private final Context context;

        public ViewHolder(View view, Context context, List<Message> messages) {
            super(view);
            chatLeft = view.findViewById(R.id.left_chat_text);
            chatLeft.setOnLongClickListener(view1 -> {
                showOptionsDialog(messages.get(getAdapterPosition()));
                return true;
            });
            this.context = context;
        }

        public void bind(Message message) {
            if (message.author.equals(Author.Assistant) || message.author.equals(Author.System)) {
                chatLeft.setText(message.message);
                chatLeft.setBackgroundTintList(context.getColorStateList(R.color.secondary));
            } else {
                chatLeft.setText(message.message);
                chatLeft.setBackgroundTintList(context.getColorStateList(R.color.primary));
            }

        }

        private void showOptionsDialog(Message message) {
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle(R.string.options);
            builder.setItems(new CharSequence[]{"Teilen", "Info"}, (dialog, which) -> {
                if (which == 0) {
                    shareMessage(message.message);
                } else if (which == 1){
                    showInfoDialog(message);
                }
            });
            builder.show();
        }

        private void shareMessage(String messageText) {
            Intent shareIntent = new Intent(Intent.ACTION_SEND);
            shareIntent.setType("text/plain");
            shareIntent.putExtra(Intent.EXTRA_TEXT, messageText);
            context.startActivity(Intent.createChooser(shareIntent, ""));
        }

        private void showInfoDialog(Message message) {
            Instant instant = message.date.toInstant();
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("EEEE dd.MM.yyyy HH:mm:ss").withZone(ZoneId.of("Europe/Berlin"));
            AlertDialog.Builder builder = new AlertDialog.Builder(itemView.getContext());
            builder.setTitle(R.string.date_time);
            builder.setMessage(formatter.format(instant));
            builder.show();
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
        return new ViewHolder(view, context, messages);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Message message = messages.get(position);
        holder.bind(message);
    }

    @Override
    public int getItemCount() {
        return messages.size();
    }
}
