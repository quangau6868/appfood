package com.app.appfood.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.app.appfood.database.DBHelper;
import com.app.appfood.model.Comment;

import java.util.ArrayList;
import java.util.List;

public class CommentDao {

    private DBHelper dbHelper;

    public CommentDao(Context context) {
        dbHelper = new DBHelper(context);
    }

    // Thêm đánh giá (Comment) vào cơ sở dữ liệu
    public void addComment(Comment comment) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("maSP", comment.getMaSP());
        values.put("tenNguoiDung", comment.getTenNguoiDung());
        values.put("ngayDanhGia", comment.getNgayDanhGia());
        values.put("sao", comment.getSao());
        values.put("noiDung", comment.getNoiDung());
        values.put("tongTien", comment.getTongTien());
        values.put("ThanhToanId", comment.getThanhToanId());  // Thêm ThanhToanId vào cột

        try {
            long result = db.insert("Comment", null, values);
            if (result == -1) {
                Log.e("CommentDao", "Failed to insert comment into database");
            } else {
                comment.setId((int) result); // Gán id cho đối tượng comment
                Log.d("CommentDao", "Comment inserted successfully with ID: " + result);
            }
        } catch (Exception e) {
            Log.e("CommentDao", "Error inserting comment", e);
        }
    }

    public List<Comment> getCommentsByProductId(int productId) {
        List<Comment> comments = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT tenNguoiDung, ngayDanhGia, sao, noiDung, tongTien FROM Comment WHERE maSP = ?", new String[]{String.valueOf(productId)});

        if (cursor != null && cursor.moveToFirst()) {
            do {
                String tenNguoiDung = cursor.getString(0);
                String ngayDanhGia = cursor.getString(1);
                int sao = cursor.getInt(2);
                String noiDung = cursor.getString(3);
                double tongTien = cursor.getDouble(4);

                Comment comment = new Comment(tenNguoiDung, ngayDanhGia, sao, noiDung, tongTien);
                comments.add(comment);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return comments;
    }


}
