/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

import java.io.Serializable;

/**
 *
 * @author MSI
 */

public class KhachHang implements Serializable{
    private String maKhachHang;
    private String matkhau;
    private String hoTenKhachHang;
    private String chucVu;
    private String gioiTinh;
    private String soDienThoai;
    private String diaChi;

    public KhachHang(String maKhachHang, String hoTenKhachHang, String gioiTinh, String soDienThoai, String diaChi) {
        this.maKhachHang = maKhachHang;
//        this.matkhau = matkhau;
        this.hoTenKhachHang = hoTenKhachHang;
//        this.chucVu = chucVu;
        this.gioiTinh = gioiTinh;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
    }

    public KhachHang(String maKhachHang, String matkhau, String hoTenKhachHang, String chucVu, String gioiTinh, String soDienThoai, String diaChi) {
        this.maKhachHang = maKhachHang;
        this.matkhau = matkhau;
        this.hoTenKhachHang = hoTenKhachHang;
        this.chucVu = chucVu;
        this.gioiTinh = gioiTinh;
        this.soDienThoai = soDienThoai;
        this.diaChi = diaChi;
    }
    
    
    
    public String getMatkhau() {
        return matkhau;
    }

    public void setMatkhau(String matkhau) {
        this.matkhau = matkhau;
    }

    


    public KhachHang() {
    }

    public String getChucVu() {
        return chucVu;
    }

    public void setChucVu(String chucVu) {
        this.chucVu = chucVu;
    }

    
    public String getGioiTinh() {
        return gioiTinh;
    }

    public void setGioiTinh(String gioiTinh) {
        this.gioiTinh = gioiTinh;
    }
    
    public String getMaKhachHang() {
        return maKhachHang;
    }

    public void setMaKhachHang(String maKhachHang) {
        this.maKhachHang = maKhachHang;
    }

    public String getHoTenKhachHang() {
        return hoTenKhachHang;
    }

    public void setHoTenKhachHang(String tenKhachHang) {
        this.hoTenKhachHang = tenKhachHang;
    }

    public String getSoDienThoai() {
        return soDienThoai;
    }

    public void setSoDienThoai(String soDienThoai) {
        this.soDienThoai = soDienThoai;
    }

    public String getDiaChi() {
        return diaChi;
    }

    public void setDiaChi(String diaChi) {
        this.diaChi = diaChi;
    }
    

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("KhachHang{");
        sb.append("maKhachHang=").append(maKhachHang);
        sb.append(", matkhau=").append(matkhau);
        sb.append(", hoTenKhachHang=").append(hoTenKhachHang);
        sb.append(", chucVu=").append(chucVu);
        sb.append(", gioiTinh=").append(gioiTinh);
        sb.append(", soDienThoai=").append(soDienThoai);
        sb.append(", diaChi=").append(diaChi);
        sb.append('}');
        return sb.toString();
    }
  
}


