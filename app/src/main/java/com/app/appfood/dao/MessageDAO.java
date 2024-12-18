package com.app.appfood.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.app.appfood.database.DBHelper;
import com.app.appfood.model.Message;

import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    private SQLiteDatabase database;

    // Tên bảng và các cột
    private static final String TABLE_MESSAGES = "messages";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_CONTENT = "content";
    private static final String COLUMN_IS_FROM_USER = "isFromUser";
    private static final String COLUMN_TIMESTAMP = "timestamp";
    private static final String COLUMN_ADMIN_RESPONSE = "adminResponse"; // Thêm trường phản hồi của admin

    public MessageDAO(Context context) {
        DBHelper dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    // Thêm tin nhắn
    public void insertMessage(Message message) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_CONTENT, message.getContent());
        values.put(COLUMN_IS_FROM_USER, message.isFromUser() ? 1 : 0);

        // Nếu có phản hồi từ admin, lưu vào CSDL
        if (message.getAdminResponse() != null) {
            values.put(COLUMN_ADMIN_RESPONSE, message.getAdminResponse());
        }

        database.insert(TABLE_MESSAGES, null, values);
    }

    // Lấy danh sách tất cả tin nhắn
    public List<Message> getAllMessages() {
        List<Message> messages = new ArrayList<>();
        Cursor cursor = database.query(TABLE_MESSAGES, null, null, null, null, null, COLUMN_TIMESTAMP + " ASC");

        if (cursor != null) {
            while (cursor.moveToNext()) {
                Message message = new Message(
                        cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_CONTENT)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COLUMN_IS_FROM_USER)) == 1
                );

                // Lấy phản hồi của admin nếu có
                String adminResponse = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_ADMIN_RESPONSE));
                message.setAdminResponse(adminResponse);

                messages.add(message);
            }
            cursor.close();
        }
        return messages;
    }
}
