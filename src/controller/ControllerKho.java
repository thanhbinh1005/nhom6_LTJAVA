/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.ArrayList;
import model.SanPham;

/**
 *
 * @author Admin
 */
public class ControllerKho {
    public static ArrayList<SanPham> timKiemSanPham(ArrayList<SanPham> dsSanPham, String maSanPham, String tenSanPham,
                                                    int soluongMin, int soluongMax, double giaMin, double giaMax) {
        ArrayList<SanPham> ketQua = new ArrayList<>();

        for (SanPham sanPham : dsSanPham) {
            boolean matches = true;

            // Kiểm tra mã sản phẩm
            if (!maSanPham.isEmpty() && !sanPham.getMaSanPham().equals(maSanPham)) {
                matches = false;
            }

            // Kiểm tra tên sản phẩm
            if (!tenSanPham.isEmpty() && !sanPham.getTenSanPham().toLowerCase().contains(tenSanPham.toLowerCase())) {
                matches = false;
            }

            // Kiểm tra số lượng
            if (soluongMin > 0 && sanPham.getSoLuong() < soluongMin) {
                matches = false;
            }
            if (soluongMax > 0 && sanPham.getSoLuong() > soluongMax) {
                matches = false;
            }

            // Kiểm tra giá
            if (giaMin > 0 && sanPham.getGia() < giaMin) {
                matches = false;
            }
            if (giaMax > 0 && sanPham.getGia() > giaMax) {
                matches = false;
            }

            // Nếu sản phẩm phù hợp tất cả điều kiện, thêm vào kết quả
            if (matches) {
                ketQua.add(sanPham);
            }
        }

        return ketQua;
    }
    
    
}
