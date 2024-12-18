package com.app.appfood.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteStatement;
import android.util.Log;

import com.app.appfood.database.DBHelper;
import com.app.appfood.model.ThanhToan;

import java.util.ArrayList;
import java.util.List;

public class ThanhToanDao {

    private SQLiteDatabase db;


    public ThanhToanDao(Context context) {
        db = new DBHelper(context).getWritableDatabase();
    }

    // 0: chờ xác nhận
    // 1: đã xác nhận
    // 2: đã giao

    // Thêm một bản ghi thanh toán
    public long addThanhToan(ThanhToan thanhToan) {
        SQLiteDatabase db = this.db; // Sử dụng kết nối cơ sở dữ liệu hiện tại

        // Thêm bản ghi mới vào bảng ThanhToan
        ContentValues values = new ContentValues();
        values.put("tenNguoi", thanhToan.getTenNguoi());
        values.put("sdt", thanhToan.getSdt());
        values.put("diaChi", thanhToan.getDiaChi());
        values.put("tongTien", thanhToan.getTongTien());
        values.put("trangThai", 0); // Mặc định trạng thái "Chờ xác nhận"
        values.put("maSP", thanhToan.getMaSP()); // Mã sản phẩm
        values.put("tenSP", thanhToan.getTenSP()); // Tên sản phẩm
        values.put("soLuong", thanhToan.getSoLuong()); // Số lượng sản phẩm
        values.put("ngayThanhToan", thanhToan.getNgayThanhToan()); // Ngày thanh toán

        long result = -1;
        try {
            // Thực hiện chèn dữ liệu vào bảng ThanhToan
            result = db.insert("ThanhToan", null, values);
        } catch (Exception e) {
            Log.e("ThanhToanDao", "Lỗi khi thêm thanh toán: " + e.getMessage());
        }

        return result; // Trả về long (ID của bản ghi được chèn, hoặc -1 nếu thất bại)
    }


    // Lấy tất cả các bản ghi thanh toán
    public List<ThanhToan> getAllThanhToan() {
        List<ThanhToan> list = new ArrayList<>();
        String query = "SELECT * FROM ThanhToan ORDER BY id DESC";
        Cursor cursor = db.rawQuery(query, null);

        if (cursor != null && cursor.moveToFirst()) {
            do {
                int id = cursor.getInt(0);
                String tenNguoi = cursor.getString(1);
                String sdt = cursor.getString(2);
                String diaChi = cursor.getString(3);
                double tongTien = cursor.getDouble(4);
                int trangThai = cursor.getInt(5);
                int soLuong = cursor.getInt(6);
                int maSP = cursor.getInt(7);
                String tenSP = cursor.getString(8);

                ThanhToan thanhToan = new ThanhToan(tenNguoi, sdt, diaChi, tongTien, tenSP, soLuong, maSP); // Truyền soLuong vào constructor
                thanhToan.setId(id);  // Thiết lập id cho đối tượng
                thanhToan.setTrangThai(trangThai); // Thiết lập trạng thái
                list.add(thanhToan);
            } while (cursor.moveToNext());
            cursor.close();
        }
        return list;
    }

    public boolean updateTrangThai(int id, String trangThai) {
        ContentValues values = new ContentValues();

        // Chuyển trạng thái chuỗi thành giá trị số tương ứng
        int trangThaiInt = 0; // Mặc định là 0 (Đang giao)
        if ("Đang giao".equals(trangThai)) {
            trangThaiInt = 1;
        } else if ("Đã giao".equals(trangThai)) {
            trangThaiInt = 2;
        }

        values.put("trangThai", trangThaiInt); // Cập nhật trạng thái mới

        // Cập nhật trạng thái trong bảng ThanhToan theo id
        int rowsAffected = db.update("ThanhToan", values, "id = ?", new String[]{String.valueOf(id)});
        return rowsAffected > 0; // Trả về true nếu cập nhật thành công
    }

    // Phương thức tính doanh thu
    public double getDoanhThuTrongKhoangThoiGian(String startDate, String endDate) {
        double totalRevenue = 0;
        Log.d("TKDoanhThu", "Getting revenue from " + startDate + " to " + endDate);

        String query = "SELECT SUM(tongTien) FROM ThanhToan WHERE ngayThanhToan BETWEEN ? AND ?";
        Cursor cursor = db.rawQuery(query, new String[]{startDate, endDate});

        if (cursor != null && cursor.moveToFirst()) {
            totalRevenue = cursor.getDouble(0);
            Log.d("TKDoanhThu", "Total revenue: " + totalRevenue);
            cursor.close();
        } else {
            Log.d("TKDoanhThu", "No data found for the given dates.");
        }

        return totalRevenue;
    }



    // Xóa một bản ghi thanh toán theo id
    public void deleteThanhToan(int id) {
        db.delete("ThanhToan", "id" + " = ?", new String[]{String.valueOf(id)});
    }

    // Cập nhật thông tin thanh toán (ví dụ, tên người, số điện thoại, địa chỉ, tổng tiền)
    public boolean updateThanhToan(ThanhToan thanhToan) {
        ContentValues values = new ContentValues();
        values.put("tenNguoi", thanhToan.getTenNguoi());
        values.put("sdt", thanhToan.getSdt());
        values.put("diaChi", thanhToan.getDiaChi());
        values.put("tongTien", thanhToan.getTongTien());
        values.put("trangThai", thanhToan.getTrangThai()); // Cập nhật trạng thái
        values.put("maSP", thanhToan.getMaSP()); // Cập nhật mã sản phẩm
        values.put("tenSP", thanhToan.getTenSP()); // Cập nhật tên sản phẩm

        // Cập nhật thông tin trong bảng ThanhToan theo id
        int rowsAffected = db.update("ThanhToan", values, "id = ?", new String[]{String.valueOf(thanhToan.getId())});
        return rowsAffected > 0; // Trả về true nếu cập nhật thành công
    }
}

