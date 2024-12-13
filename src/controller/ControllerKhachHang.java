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
    
    public void saveKhachHangToDB(String maKh, String tenKh, String gioiTinh, String sdt, String diaChi) {
        controllerSQL.luuKhachHang(maKh, tenKh, gioiTinh, sdt, diaChi);
    }
    
    public void deleteKhachHangToDB(String makh){
        controllerSQL.xoaKhachHang(makh);
    }
    
    public void updateKhachHangToDB(String maKh, String tenKh, String gioiTinh, String sdt, String diaChi){
        controllerSQL.suaKhachHang(maKh, tenKh, gioiTinh, sdt, diaChi);
    }
    
    public boolean checkTrungMaKH(String makh){
        boolean check = controllerSQL.checkMaKh(makh);
        return check;
    }
    
}
