package com.app.appfood.model;

public class CartItem {
    private int id; // ID giỏ hàng
    private int maSanPham; // Khóa ngoại: Mã sản phẩm
    private String tenSanPham; // Tên sản phẩm
    private int soLuong; // Số lượng sản phẩm trong giỏ
    private double gia; // Giá sản phẩm
    private int soLuongToiDa; // Số lượng tối đa có thể mua
    private byte[] hinhAnh; // Hình ảnh sản phẩm

    public CartItem(int id, int maSanPham, String tenSanPham, int soLuong, double gia, byte[] hinhAnh) {
        this.id = id;
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.soLuong = soLuong;
        this.gia = gia;
        this.hinhAnh = hinhAnh;
    }

    // Getter và Setter
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(int maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public int getSoLuongToiDa() {
        return soLuongToiDa;
    }

    public void setSoLuongToiDa(int soLuongToiDa) {
        this.soLuongToiDa = soLuongToiDa;
    }

    public byte[] getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(byte[] hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    @Override
    public String toString() {
        return tenSanPham;  // Trả về tên sản phẩm khi hiển thị trong Spinner
    }
}
