package com.app.appfood.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;


import com.app.appfood.database.DBHelper;
import com.app.appfood.model.SanPham;

import java.util.ArrayList;
import java.util.List;

public class sanPhamDao {

    private DBHelper dbHelper;

    public sanPhamDao(Context context) {
        dbHelper = new DBHelper(context);
    }

    // Lấy danh sách sản phẩm kèm theo thông tin loại sản phẩm
    public ArrayList<SanPham> getDanhsachSanPham() {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        ArrayList<SanPham> list = new ArrayList<>();

        String query = "SELECT SanPham.*, Loai_SP.TenLoai FROM SanPham " +
                "INNER JOIN Loai_SP ON SanPham.MaLoai = Loai_SP.MaLoai";
        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int maSP = cursor.getInt(0);
                    int maLoai = cursor.getInt(1);
                    String tenSP = cursor.getString(2);
                    double giaBan = cursor.getDouble(3);
                    String moTa = cursor.getString(4);
                    byte[] hinhAnh = cursor.getBlob(5);
                    boolean isConHang = cursor.getInt(6) == 1; // Đọc giá trị boolean từ cơ sở dữ liệu

                    SanPham sanPham = new SanPham(maSP, maLoai, tenSP, giaBan, moTa, hinhAnh, isConHang);
                    list.add(sanPham);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("SanPhamDao", "Lỗi khi truy vấn danh sách sản phẩm: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            if (db != null && db.isOpen()) db.close();
        }
        return list;
    }


    public int getMaSPByName(String tenSanPham) {
        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.query("SanPham", new String[]{"MaSP"}, "TenSP = ?", new String[]{tenSanPham}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            int maSP = cursor.getInt(1);
            cursor.close();
            return maSP;
        }

        if (cursor != null) {
            cursor.close();
        }

        return -1;  // Trả về -1 nếu không tìm thấy sản phẩm
    }

    public List<String> getAllProductNames() {
        List<String> productNames = new ArrayList<>();
        SQLiteDatabase db = null;
        Cursor cursor = null;

        String query = "SELECT TenSP FROM SanPham";  // Truy vấn lấy tất cả tên sản phẩm từ bảng SanPham

        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(query, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    String tenSP = cursor.getString(2);
                    productNames.add(tenSP);  // Thêm tên sản phẩm vào danh sách
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("SanPhamDao", "Lỗi khi lấy danh sách tên sản phẩm: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            if (db != null && db.isOpen()) db.close();
        }

        return productNames;
    }


    // Thêm sản phẩm mới
    public boolean addSanPham(SanPham sanPham) {
        SQLiteDatabase db = dbHelper.getWritableDatabase();

        // Kiểm tra mã loại trước khi thêm
        if (!isMaLoaiValid(sanPham.getMaLoai())) {
            Log.e("SanPhamDao", "Mã loại không hợp lệ: " + sanPham.getMaLoai());
            return false;
        }

        ContentValues values = new ContentValues();
        values.put("MaLoai", sanPham.getMaLoai());
        values.put("TenSP", sanPham.getTenSP());
        values.put("GiaBan", sanPham.getGiaBan());
        values.put("MoTa", sanPham.getMoTa());
        values.put("HinhAnh", sanPham.getHinhAnh());
        values.put("isConHang", sanPham.isConHang() ? 1 : 0); // Thêm trường isConHang

        long result = -1;
        try {
            result = db.insert("SanPham", null, values);
        } catch (Exception e) {
            Log.e("SanPhamDao", "Lỗi khi thêm sản phẩm: " + e.getMessage());
        } finally {
            db.close();
        }

        return result != -1;
    }


    // Cập nhật sản phẩm
    public boolean updateSanPham(SanPham sanPham) {
        // Kiểm tra mã loại có hợp lệ không
        if (!isMaLoaiValid(sanPham.getMaLoai())) {
            Log.e("SPdao", "Mã loại không hợp lệ: " + sanPham.getMaLoai());
            return false;
        }

        SQLiteDatabase db = dbHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put("MaLoai", sanPham.getMaLoai());
        values.put("TenSP", sanPham.getTenSP());
        values.put("GiaBan", sanPham.getGiaBan());
        values.put("MoTa", sanPham.getMoTa());
        values.put("isConHang", sanPham.isConHang() ? 1 : 0); // Thêm cập nhật isConHang

        int rowsAffected = db.update("SanPham", values, "MaSP = ?", new String[]{String.valueOf(sanPham.getMaSP())});
        Log.d("SPdao", "Cập nhật sản phẩm với MaSP = " + sanPham.getMaSP() + ", rowsAffected = " + rowsAffected);

        return rowsAffected > 0;
    }


    // Xóa sản phẩm
    public boolean deleteSanPham(int maSP) {
        SQLiteDatabase db = null;
        try {
            db = dbHelper.getWritableDatabase();
            int rowsDeleted = db.delete("SanPham", "MaSP = ?", new String[]{String.valueOf(maSP)});
            return rowsDeleted > 0;
        } catch (Exception e) {
            Log.e("SanPhamDao", "Xóa sản phẩm thất bại: " + e.getMessage());
            return false;
        } finally {
            if (db != null && db.isOpen()) db.close();  // Đảm bảo đóng db
        }
    }

    // Kiểm tra mã loại có hợp lệ không
    public boolean isMaLoaiValid(int maLoai) {
        Cursor cursor = null;
        try {
            SQLiteDatabase db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery("SELECT COUNT(*) FROM Loai_SP WHERE MaLoai = ?", new String[]{String.valueOf(maLoai)});
            if (cursor.moveToFirst()) {
                return cursor.getInt(0) > 0; // Nếu có ít nhất 1 dòng thì mã loại hợp lệ
            }
        } catch (Exception e) {
            Log.e("SanPhamDao", "Kiểm tra mã loại thất bại: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
        }
        return false;
    }


    // Lấy sản phẩm theo ID
    public SanPham getSanPhamById(int productId) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        SanPham sanPham = null;

        String query = "SELECT SanPham.*, Loai_SP.TenLoai FROM SanPham " +
                "INNER JOIN Loai_SP ON SanPham.MaLoai = Loai_SP.MaLoai WHERE SanPham.MaSP = ?";
        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(query, new String[]{String.valueOf(productId)});

            if (cursor != null && cursor.moveToFirst()) {
                int maSP = cursor.getInt(0);
                int maLoai = cursor.getInt(1);
                String tenSP = cursor.getString(2);
                double giaBan = cursor.getDouble(3);
                String moTa = cursor.getString(4);
                byte[] hinhAnh = cursor.getBlob(5);
                boolean isConHang = cursor.getInt(6) == 1;

                sanPham = new SanPham(maSP, maLoai, tenSP, giaBan, moTa, hinhAnh, isConHang);
            }
        } catch (Exception e) {
            Log.e("SanPhamDao", "Lỗi khi lấy sản phẩm theo ID: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            if (db != null && db.isOpen()) db.close();
        }
        return sanPham;
    }


    // Lấy danh sách sản phẩm theo mã loại
    public ArrayList<SanPham> getSanPhamListByLoai(int maLoai) {
        SQLiteDatabase db = null;
        Cursor cursor = null;
        ArrayList<SanPham> list = new ArrayList<>();

        String query = "SELECT SanPham.*, Loai_SP.TenLoai FROM SanPham " +
                "INNER JOIN Loai_SP ON SanPham.MaLoai = Loai_SP.MaLoai WHERE SanPham.MaLoai = ?";
        try {
            db = dbHelper.getReadableDatabase();
            cursor = db.rawQuery(query, new String[]{String.valueOf(maLoai)});

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int maSP = cursor.getInt(0);
                    int maLoaiSP = cursor.getInt(1);
                    String tenSP = cursor.getString(2);
                    double giaBan = cursor.getDouble(3);
                    String moTa = cursor.getString(4);
                    byte[] hinhAnh = cursor.getBlob(5);
                    boolean isConHang = cursor.getInt(6) == 1; // Đọc giá trị isConHang từ cơ sở dữ liệu

                    SanPham sanPham = new SanPham(maSP, maLoaiSP, tenSP, giaBan, moTa, hinhAnh, isConHang);
                    list.add(sanPham);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            Log.e("SanPhamDao", "Lỗi khi lấy danh sách sản phẩm theo loại: " + e.getMessage());
        } finally {
            if (cursor != null) cursor.close();
            if (db != null && db.isOpen()) db.close();
        }
        return list;
    }


    // Tìm kiếm sản phẩm theo tên
    public List<SanPham> searchProducts(String query) {
        List<SanPham> results = new ArrayList<>();
        SQLiteDatabase db = dbHelper.getReadableDatabase();

        String selection = "TenSP LIKE ?";
        String[] selectionArgs = new String[]{"%" + query + "%"};

        Cursor cursor = db.query("SanPham", null, selection, selectionArgs, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {
                int maSP = cursor.getInt(0);
                int maLoai = cursor.getInt(1);
                String tenSP = cursor.getString(2);
                double giaBan = cursor.getDouble(3);
                String moTa = cursor.getString(4);
                byte[] hinhAnh = cursor.getBlob(5);
                boolean isConHang = cursor.getInt(6) == 1; // Đọc giá trị isConHang từ cơ sở dữ liệu

                SanPham sanPham = new SanPham(maSP, maLoai, tenSP, giaBan, moTa, hinhAnh, isConHang);
                results.add(sanPham);
            } while (cursor.moveToNext());
            cursor.close(); // Đảm bảo đóng con trỏ sau khi truy vấn xong
        }
        db.close();
        return results;
    }


}
