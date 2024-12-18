package com.app.appfood.model;

public class Comment {
    private int id;
    private int maSP;
    private String tenNguoiDung;
    private String ngayDanhGia;
    private int sao;
    private String noiDung;
    private double tongTien;
    private int thanhToanId;

    public Comment(String tenNguoiDung, String ngayDanhGia, int sao, String noiDung, double tongTien) {
        this.tenNguoiDung = tenNguoiDung;
        this.ngayDanhGia = ngayDanhGia;
        this.sao = sao;
        this.noiDung = noiDung;
        this.tongTien = tongTien;
    }

    public Comment(int maSP, String tenNguoiDung, String ngayDanhGia, int sao, String noiDung, double tongTien, int thanhToanId) {
        this.maSP = maSP;
        this.tenNguoiDung = tenNguoiDung;
        this.ngayDanhGia = ngayDanhGia;
        this.sao = sao;
        this.noiDung = noiDung;
        this.tongTien = tongTien;
        this.thanhToanId = thanhToanId;
    }

    public Comment(int id, int maSP, String tenNguoiDung, String ngayDanhGia, int sao, String noiDung, double tongTien) {
        this.id = id;
        this.maSP = maSP;
        this.tenNguoiDung = tenNguoiDung;
        this.ngayDanhGia = ngayDanhGia;
        this.sao = sao;
        this.noiDung = noiDung;
        this.tongTien = tongTien;
    }

    public int getThanhToanId() {
        return thanhToanId;
    }

    public void setThanhToanId(int thanhToanId) {
        this.thanhToanId = thanhToanId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getMaSP() {
        return maSP;
    }

    public void setMaSP(int maSP) {
        this.maSP = maSP;
    }

    public String getTenNguoiDung() {
        return tenNguoiDung;
    }

    public void setTenNguoiDung(String tenNguoiDung) {
        this.tenNguoiDung = tenNguoiDung;
    }

    public String getNgayDanhGia() {
        return ngayDanhGia;
    }

    public void setNgayDanhGia(String ngayDanhGia) {
        this.ngayDanhGia = ngayDanhGia;
    }

    public int getSao() {
        return sao;
    }

    public void setSao(int sao) {
        this.sao = sao;
    }

    public String getNoiDung() {
        return noiDung;
    }

    public void setNoiDung(String noiDung) {
        this.noiDung = noiDung;
    }

    public double getTongTien() {
        return tongTien;
    }

    public void setTongTien(double tongTien) {
        this.tongTien = tongTien;
    }
}
