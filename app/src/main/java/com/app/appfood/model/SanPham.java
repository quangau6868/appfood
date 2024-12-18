package com.app.appfood.model;

public class SanPham {
    private int maSP;
    private int maLoai;
    private String tenSP;
    private double giaBan;
    private String moTa;
    private int soLuong;
    private byte[] hinhAnh;
    private String tenLoai;
    private boolean isConHang;

    // Constructor với URL/đường dẫn ảnh
    public SanPham(int maLoai, String tenSP, double giaBan, String moTa, byte[] hinhAnh, boolean isConHang) {
        this.maLoai = maLoai;
        this.tenSP = tenSP;
        this.giaBan = giaBan;
        this.moTa = moTa;
        this.hinhAnh = hinhAnh;
        this.isConHang = isConHang;
    }

    public SanPham(int maSP, int maLoai, String tenSP, double giaBan, String moTa, byte[] hinhAnh, boolean isConHang) {
        this.maSP = maSP;
        this.maLoai = maLoai;
        this.tenSP = tenSP;
        this.giaBan = giaBan;
        this.moTa = moTa;
        this.hinhAnh = hinhAnh;
        this.isConHang = isConHang;
    }

    public SanPham(String tenSP, double giaBan, int soLuong, byte[] hinhAnh) {
        this.tenSP = tenSP;
        this.giaBan = giaBan;
        this.soLuong = soLuong;
        this.hinhAnh = hinhAnh;
    }

    public SanPham(int maSP, String tenSP, double giaBan, int soLuong, byte[] hinhAnh) {
        this.maSP = maSP;
        this.tenSP = tenSP;
        this.giaBan = giaBan;
        this.soLuong = soLuong;
        this.hinhAnh = hinhAnh;
    }

    // Constructor cho việc thêm sản phẩm mà không có maSP
    public SanPham(int maLoai, String tenSP, double giaBan, String moTa, byte[] hinhAnh) {
        this.maLoai = maLoai;
        this.tenSP = tenSP;
        this.giaBan = giaBan;
        this.moTa = moTa;
        this.hinhAnh = hinhAnh;
    }


    // Constructor với maSP, maLoai, tên sản phẩm, giá bán, mô tả, và đường dẫn ảnh
    public SanPham(int maSP, int maLoai, String tenSP, double giaBan, String moTa, byte[] hinhAnh) {
        this.maSP = maSP;
        this.maLoai = maLoai;
        this.tenSP = tenSP;
        this.giaBan = giaBan;
        this.moTa = moTa;
        this.hinhAnh = hinhAnh;
    }

    public boolean isConHang() {
        return isConHang;
    }

    public void setConHang(boolean conHang) {
        isConHang = conHang;
    }

    public int getMaSP() {
        return maSP;
    }

    public void setMaSP(int maSP) {
        this.maSP = maSP;
    }

    public int getMaLoai() {
        return maLoai;
    }

    public void setMaLoai(int maLoai) {
        this.maLoai = maLoai;
    }

    public String getTenSP() {
        return tenSP;
    }

    public void setTenSP(String tenSP) {
        this.tenSP = tenSP;
    }

    public double getGiaBan() {
        return giaBan;
    }

    public void setGiaBan(double giaBan) {
        this.giaBan = giaBan;
    }

    public String getMoTa() {
        return moTa;
    }

    public void setMoTa(String moTa) {
        this.moTa = moTa;
    }

    public byte[] getHinhAnh() {
        return hinhAnh;
    }

    public void setHinhAnh(byte[] hinhAnh) {
        this.hinhAnh = hinhAnh;
    }

    public String getTenLoai() {
        return tenLoai;
    }

    public void setTenLoai(String tenLoai) {
        this.tenLoai = tenLoai;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }
}

