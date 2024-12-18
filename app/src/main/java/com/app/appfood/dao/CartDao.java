package com.app.appfood.dao;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.app.appfood.database.DBHelper;
import com.app.appfood.model.CartItem;
import com.app.appfood.model.SanPham;

import java.util.ArrayList;
import java.util.List;

public class CartDao {

    private SQLiteDatabase database;
    private DBHelper dbHelper;

    public CartDao(Context context) {
        dbHelper = new DBHelper(context);
        database = dbHelper.getWritableDatabase();
    }

    public void addProductToCart(SanPham product) {
        ContentValues values = new ContentValues();
        values.put("MaSP", product.getMaSP());
        values.put("tenSanPham", product.getTenSP());
        values.put("gia", product.getGiaBan());
        values.put("soLuong", product.getSoLuong());
        values.put("hinhAnh", product.getHinhAnh());

        // Chèn sản phẩm vào bảng giỏ hàng
        long result = database.insert("GioHang", null, values);
        if (result == -1) {
            System.out.println("Không thể thêm sản phẩm vào giỏ hàng.");
        }
    }

    // Lấy danh sách sản phẩm trong giỏ hàng
    public List<CartItem> layGioHang() {
        List<CartItem> gioHang = new ArrayList<>();
        Cursor cursor = null;

        try {
            String selectQuery = "SELECT * FROM GioHang";
            cursor = database.rawQuery(selectQuery, null);

            if (cursor != null && cursor.moveToFirst()) {
                do {
                    int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                    int maSanPham = cursor.getInt(cursor.getColumnIndexOrThrow("MaSP"));
                    String tenSanPham = cursor.getString(cursor.getColumnIndexOrThrow("tenSanPham"));
                    double gia = cursor.getDouble(cursor.getColumnIndexOrThrow("gia"));
                    int soLuong = cursor.getInt(cursor.getColumnIndexOrThrow("soLuong"));
                    byte[] hinhAnh = cursor.getBlob(cursor.getColumnIndexOrThrow("hinhAnh"));

                    CartItem cartItem = new CartItem(id, maSanPham, tenSanPham, soLuong, gia, hinhAnh);
                    gioHang.add(cartItem);
                } while (cursor.moveToNext());
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return gioHang;
    }

    // Xóa sản phẩm khỏi giỏ hàng
    public boolean xoaSanPhamKhoiGioHang(int idSanPham) {
        int rowsDeleted = database.delete("GioHang", "id = ?", new String[]{String.valueOf(idSanPham)});
        return rowsDeleted > 0;
    }

    // Cập nhật số lượng sản phẩm trong giỏ hàng
    public boolean capNhatSoLuongSanPham(CartItem cartItem) {
        ContentValues values = new ContentValues();
        values.put("soLuong", cartItem.getSoLuong());

        int rowsUpdated = database.update("GioHang", values, "id = ?", new String[]{String.valueOf(cartItem.getId())});
        return rowsUpdated > 0;
    }

    // Xóa toàn bộ giỏ hàng
    public void xoaToanBoGioHang() {
        database.delete("GioHang", null, null);
    }

    // Kiểm tra giỏ hàng có rỗng không
    public boolean kiemTraGioHangRong() {
        return layGioHang().isEmpty();
    }

    // Lấy sản phẩm trong giỏ hàng theo mã sản phẩm
    public CartItem laySanPhamTuGioHang(int idSanPham) {
        Cursor cursor = null;
        CartItem cartItem = null;

        try {
            String query = "SELECT * FROM GioHang WHERE maSanPham = ?";
            cursor = database.rawQuery(query, new String[]{String.valueOf(idSanPham)});

            if (cursor != null && cursor.moveToFirst()) {
                int id = cursor.getInt(cursor.getColumnIndexOrThrow("id"));
                int maSanPham = cursor.getInt(cursor.getColumnIndexOrThrow("MaSP"));
                String tenSanPham = cursor.getString(cursor.getColumnIndexOrThrow("tenSanPham"));
                double gia = cursor.getDouble(cursor.getColumnIndexOrThrow("gia"));
                int soLuong = cursor.getInt(cursor.getColumnIndexOrThrow("soLuong"));
                byte[] hinhAnh = cursor.getBlob(cursor.getColumnIndexOrThrow("hinhAnh"));

                cartItem = new CartItem(id, maSanPham, tenSanPham, soLuong, gia, hinhAnh);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }

        return cartItem;
    }

    // Đóng kết nối cơ sở dữ liệu
    public void dongKetNoi() {
        if (database != null && database.isOpen()) {
            database.close();
        }
    }
}