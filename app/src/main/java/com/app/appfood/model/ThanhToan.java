package com.app.appfood.model;

public class ThanhToan {

    private int id;
    private String tenNguoi;
    private String sdt;
    private String diaChi;
    private double tongTien;
    private int trangThai;
    private int maSP;  // Mã sản phẩm
    private String tenSP;
    private int soLuong;
    private String ngayThanhToan;

    public ThanhToan(String tenNguoi, String sdt, String diaChi, double tongTien, String tenSP, int soLuong, int maSP) {
        this.tenNguoi = tenNguoi;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.tongTien = tongTien;
        this.tenSP = tenSP;
        this.soLuong = soLuong;
        this.maSP = maSP;
    }

    public ThanhToan(String tenNguoi, String sdt, String diaChi, double tongTien, int maSP, String tenSP, int soLuong, String ngayThanhToan) {
        this.tenNguoi = tenNguoi;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.tongTien = tongTien;
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.soLuong = soLuong;
        this.ngayThanhToan = ngayThanhToan;
    }

    public ThanhToan(String tenNguoi, String sdt, String diaChi, double tongTien, String tenSP, int soLuong) {
        this.tenNguoi = tenNguoi;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.tongTien = tongTien;
        this.tenSP = tenSP;
        this.soLuong = soLuong;
    }

    public ThanhToan(int id, String tenNguoi, String sdt, String diaChi, double tongTien, int trangThai) {
        this.id = id;
        this.tenNguoi = tenNguoi;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.tongTien = tongTien;
        this.trangThai = trangThai;
    }

    public ThanhToan(int id, String tenNguoi, String sdt, String diaChi, double tongTien) {
        this.id = id;
        this.tenNguoi = tenNguoi;
        this.sdt = sdt;
        this.diaChi = diaChi;
        this.tongTien = tongTien;
    }

    // Getter và Setter

    public String getNgayThanhToan() {
        return ngayThanhToan;
    }

    public void setNgayThanhToan(String ngayThanhToan) {
        this.ngayThanhToan = ngayThanhToan;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    public int getMaSP() {
        return maSP;
    }

    public void setMaSP(int maSP) {
        this.maSP = maSP;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public int getTrangThai() {
        return trangThai;
    }

    public void setTrangThai(int trangThai) {
        this.trangThai = trangThai;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTenNguoi() {
        return tenNguoi;
    }

    public void setTenNguoi(String tenNguoi) {
        this.tenNguoi = tenNguoi;
    }

    public String getSdt() {
        return sdt;
    }

    public void setSdt(String sdt) {
        this.sdt = sdt;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }
}

