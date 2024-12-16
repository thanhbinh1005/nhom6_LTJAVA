/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

/**
 *
 * @author Admin
 */
public class controllerSanPham {
    private ControllerSQL controllerSQL;

    public controllerSanPham() {
        controllerSQL = new ControllerSQL();
    }
    
    public void saveSanPhamToDB(String maSp, String tenSp, int soluong, double gia) {
        controllerSQL.luuSanPham(maSp,tenSp,soluong,gia);
    }
    
    public void deleteSanPhamToDB(String makh){
        controllerSQL.xoaSanPham(makh);
    }
    
    public void updateSanPhamToDB(String maSp, String tenSp, int soluong, double gia){
        controllerSQL.suaSanPham( maSp,  tenSp,  soluong,  gia);
    }
    
    public boolean checkTrungMaSP(String masp){
        boolean check = controllerSQL.checkMaSP(masp);
        return check;
    }
    
    public void giamSoLuong(String maSP,int old, int newSlg){
        controllerSQL.capNhatSPkhiLuuHD(maSP, old, newSlg);
    }
    
    public void tangSoLuong(String maSP,int old, int newSlg){
        controllerSQL.capNhatSPkhiXoaHD(maSP, old, newSlg);
    }
}
