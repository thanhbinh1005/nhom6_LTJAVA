/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import java.util.ArrayList;
import model.HoaDon;
import model.SanPham;

/**
 *
 * @author Admin
 */
public class ControllerDoanhThu {
    public static ArrayList<SanPham> capNhatBanRaLuu(HoaDon newHD, ArrayList<SanPham> dsBanRa) {
        ArrayList<SanPham> ketQua = new ArrayList<>();
        for (SanPham sanPhamMua : newHD.getDsMuaSanPham()) {
            boolean found = false;
            
            // Kiểm tra xem sản phẩm đã tồn tại trong dsBanRa chưa
            for (SanPham spBanRa : dsBanRa) {
                if (spBanRa.getMaSanPham().equals(sanPhamMua.getMaSanPham())) {
                    spBanRa.setSoLuong(spBanRa.getSoLuong() + sanPhamMua.getSoLuong());
                    found = true;
                    break;
                }
            }

            // Nếu chưa tồn tại, thêm mới vào dsBanRa
            if (!found) {
                dsBanRa.add(new SanPham(
                    sanPhamMua.getMaSanPham(),
                    sanPhamMua.getTenSanPham(),
                    sanPhamMua.getSoLuong(),
                    sanPhamMua.getGia()
                ));
            }
        }
        ketQua.addAll(dsBanRa);
        return ketQua;
    }
}
