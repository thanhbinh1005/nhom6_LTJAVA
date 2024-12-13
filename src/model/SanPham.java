package model;

import java.io.Serializable;


public class SanPham implements Serializable{
    private String maSanPham;
    private String tenSanPham;
    private double gia;
    private int soLuong;

    public SanPham(String maSanPham, String tenSanPham, int soLuong,double gia) {
        this.maSanPham = maSanPham;
        this.tenSanPham = tenSanPham;
        this.gia = gia;
        this.soLuong = soLuong;
    }

    public SanPham() {
    }
    

    public String getMaSanPham() {
        return maSanPham;
    }

    public void setMaSanPham(String maSanPham) {
        this.maSanPham = maSanPham;
    }

    public String getTenSanPham() {
        return tenSanPham;
    }

    public void setTenSanPham(String tenSanPham) {
        this.tenSanPham = tenSanPham;
    }

    public double getGia() {
        return gia;
    }

    public void setGia(double gia) {
        this.gia = gia;
    }

    public int getSoLuong() {
        return soLuong;
    }

    public void setSoLuong(int soLuong) {
        this.soLuong = soLuong;
    }

    @Override
    public String toString() {
        return  "maSanPham=" + maSanPham + ", tenSanPham=" + tenSanPham + ", gia=" + gia + ", soLuong=" + soLuong ;
    }

    
    
}


