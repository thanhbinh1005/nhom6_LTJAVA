/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author Admin
 */
public class ControllerKhachHang {
    private ControllerSQL controllerSQL;

    public ControllerKhachHang() {
        controllerSQL = new ControllerSQL();
    }
    
    public void saveKhachHangToDB(String maKhachHang, String matkhau, String hoTenKhachHang, String chucVu, String gioiTinh, String soDienThoai, String diaChi) {
        controllerSQL.luuKhachHang(maKhachHang, matkhau,hoTenKhachHang,chucVu, gioiTinh, soDienThoai, diaChi);
    }
    
    public void deleteKhachHangToDB(String makh){
        controllerSQL.xoaKhachHang(makh);
    }
    
    public void updateKhachHangToDB(String maKhachHang, String matkhau, String hoTenKhachHang, String gioiTinh, String soDienThoai, String diaChi){
        controllerSQL.suaKhachHang(maKhachHang, matkhau,hoTenKhachHang, gioiTinh, soDienThoai, diaChi);
    }
    
    public boolean checkTrungMaKH(String makh){
        boolean check = controllerSQL.checkMaKh(makh);
        return check;
    }
    
}
