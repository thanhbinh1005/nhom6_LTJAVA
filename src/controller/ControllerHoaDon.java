/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package controller;

import model.HoaDon;

public class ControllerHoaDon {
    private ControllerSQL controllerSQL;
    
    public ControllerHoaDon(ControllerSQL controllerSQL) {
        this.controllerSQL = controllerSQL;
    }

    
    public void saveHoaDon1(HoaDon hoaDon){
        controllerSQL.luuHoaDon(hoaDon);
    }
    
    public void saveHoaDon2(HoaDon hoaDon){
        controllerSQL.luuChiTietHoaDon(hoaDon);
    }
    
    public void deleteHoaDon(String mahoadon){
     controllerSQL.deleteHoaDon(mahoadon);
    }
}
