package com.app.appfood.admin;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.app.appfood.R;
import com.app.appfood.adapter.MessageAdapter;
import com.app.appfood.dao.MessageDAO;
import com.app.appfood.model.Message;

import java.util.List;

public class AdminMessageActivity extends AppCompatActivity {

    private MessageDAO messageDAO;
    private List<Message> messageList;
    private MessageAdapter messageAdapter;
    private RecyclerView recyclerView;
    private EditText edtAdminMessage;
    private Button btnSendAdminMessage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_admin_message);

        // Thiết lập insets cho giao diện
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo các thành phần giao diện
        recyclerView = findViewById(R.id.recyclerViewMessages);
        edtAdminMessage = findViewById(R.id.edtAdminMessage);
        btnSendAdminMessage = findViewById(R.id.btnSendAdminMessage);

        // Khởi tạo DAO và lấy danh sách tin nhắn
        messageDAO = new MessageDAO(this);
        messageList = messageDAO.getAllMessages();

        // Thiết lập RecyclerView
        messageAdapter = new MessageAdapter(this, messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        // Xử lý sự kiện khi admin gửi tin nhắn
        btnSendAdminMessage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String adminMessageContent = edtAdminMessage.getText().toString().trim();
                if (!adminMessageContent.isEmpty()) {
                    // Tạo tin nhắn phản hồi từ admin
                    Message adminMessage = new Message(adminMessageContent, false); // false cho admin
                    messageDAO.insertMessage(adminMessage); // Lưu vào database
                    messageList.add(adminMessage); // Thêm vào danh sách tin nhắn
                    messageAdapter.notifyItemInserted(messageList.size() - 1); // Cập nhật giao diện
                    recyclerView.scrollToPosition(messageList.size() - 1); // Cuộn xuống tin nhắn mới
                    edtAdminMessage.setText(""); // Xóa nội dung input
                }
            }
        });
    }
}
