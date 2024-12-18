package com.app.appfood.model;

public class LoaiSanPham {
    private int MaLoai;
    private String TenLoai;

    public LoaiSanPham() {
    }

    public LoaiSanPham(int maLoai, String tenLoai) {
        MaLoai = maLoai;
        TenLoai = tenLoai;
    }

    public LoaiSanPham(String tenLoai) {
        TenLoai = tenLoai;
    }

    public int getMaLoai() {
        return MaLoai;
    }

    public void setMaLoai(int maLoai) {
        MaLoai = maLoai;
    }

    public String getTenLoai() {
        return TenLoai;
    }

    public void setTenLoai(String tenLoai) {
        TenLoai = tenLoai;
    }
}
