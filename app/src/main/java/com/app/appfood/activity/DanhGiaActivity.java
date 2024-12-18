package com.app.appfood.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

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

public class DanhGiaActivity extends AppCompatActivity {

    private MessageDAO messageDAO;
    private List<Message> messageList;
    private MessageAdapter messageAdapter;
    private RecyclerView recyclerView;
    private EditText edtMessageInput;
    private ImageButton btnSend;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_danh_gia);

        // Thiết lập insets cho giao diện
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Khởi tạo các thành phần
        recyclerView = findViewById(R.id.recyclerViewMessages);
        edtMessageInput = findViewById(R.id.edtMessageInput);
        btnSend = findViewById(R.id.btnSend);

        // Khởi tạo DAO và lấy dữ liệu
        messageDAO = new MessageDAO(this);
        messageList = messageDAO.getAllMessages();

        // Thiết lập RecyclerView
        messageAdapter = new MessageAdapter(this, messageList);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.setAdapter(messageAdapter);

        // Xử lý sự kiện gửi tin nhắn
        btnSend.setOnClickListener(v -> {
            String content = edtMessageInput.getText().toString().trim();
            if (!content.isEmpty()) {
                // Tạo tin nhắn mới
                Message newMessage = new Message(content, true);
                messageDAO.insertMessage(newMessage); // Lưu vào database

                // Thêm vào danh sách và thông báo adapter cập nhật
                messageList.add(newMessage);
                messageAdapter.notifyItemInserted(messageList.size() - 1);

                // Cuộn xuống tin nhắn mới
                recyclerView.post(() -> recyclerView.scrollToPosition(messageList.size() - 1));

                edtMessageInput.setText(""); // Xóa nội dung input
            }
        });

    }
}
