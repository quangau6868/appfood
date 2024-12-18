package com.app.appfood.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.app.appfood.R;
import com.app.appfood.model.Message;

import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder> {

    private final Context context;
    private final List<Message> messageList;

    public MessageAdapter(Context context, List<Message> messageList) {
        this.context = context;
        this.messageList = messageList;
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder holder, int position) {
        Message message = messageList.get(position);
        holder.txtMessage.setText(message.getContent());

        // Đổi giao diện dựa trên loại tin nhắn
        if (message.isFromUser()) {
            // Tin nhắn của người dùng
            holder.txtMessage.setBackgroundResource(R.drawable.bg_message_user);
            holder.txtMessage.setTextColor(context.getResources().getColor(R.color.buttonColor));  // Set color for user message
            // Căn lề phải cho cả tin nhắn và phản hồi của người dùng
            holder.messageLayout.setGravity(Gravity.END);
            holder.txtAdminResponse.setGravity(Gravity.END);  // Phản hồi admin căn lề phải
        } else {
            // Tin nhắn của admin
            holder.txtMessage.setBackgroundResource(R.drawable.bg_message_admin);
            holder.txtMessage.setTextColor(context.getResources().getColor(R.color.blue)); // Set color for admin message
            // Căn lề trái cho cả tin nhắn và phản hồi của admin
            holder.messageLayout.setGravity(Gravity.START);
            holder.txtAdminResponse.setGravity(Gravity.START);  // Phản hồi admin căn lề trái
        }

        // Hiển thị phản hồi từ admin nếu có
        if (message.getAdminResponse() != null && !message.getAdminResponse().isEmpty()) {
            holder.txtAdminResponse.setVisibility(View.VISIBLE);
            holder.txtAdminResponse.setText(message.getAdminResponse());
        } else {
            holder.txtAdminResponse.setVisibility(View.GONE);
        }
    }


    @Override
    public int getItemCount() {
        return messageList.size();
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        TextView txtMessage;
        TextView txtAdminResponse;  // TextView để hiển thị phản hồi từ admin
        LinearLayout messageLayout;  // Layout chứa tin nhắn và phản hồi

        public MessageViewHolder(@NonNull View itemView) {
            super(itemView);
            txtMessage = itemView.findViewById(R.id.txtMessage);
            txtAdminResponse = itemView.findViewById(R.id.txtAdminResponse);
            messageLayout = itemView.findViewById(R.id.messageLayout);  // Khởi tạo LinearLayout cho tin nhắn và phản hồi
        }
    }
}