/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package model;

/**
 *
 * @author VU HOANG
 */
public class NguyenLieu {
     private int NL_ID;
    private String NL_TEN;
    private String NL_DONVITINH;
    private int NL_SOLUONG;

    public NguyenLieu() {
    }

    public NguyenLieu(int NL_ID, String NL_TEN, String NL_DONVITINH, int NL_SOLUONG) {
        this.NL_ID = NL_ID;
        this.NL_TEN = NL_TEN;
        this.NL_DONVITINH = NL_DONVITINH;
        this.NL_SOLUONG = NL_SOLUONG;
    }

    public int getNL_ID() {
        return NL_ID;
    }

    public void setNL_ID(int NL_ID) {
        this.NL_ID = NL_ID;
    }

    public String getNL_TEN() {
        return NL_TEN;
    }

    public void setNL_TEN(String NL_TEN) {
        this.NL_TEN = NL_TEN;
    }

    public String getNL_DONVITINH() {
        return NL_DONVITINH;
    }

    public void setNL_DONVITINH(String NL_DONVITINH) {
        this.NL_DONVITINH = NL_DONVITINH;
    }

    public int getNL_SOLUONG() {
        return NL_SOLUONG;
    }

    public void setNL_SOLUONG(int NL_SOLUONG) {
        this.NL_SOLUONG = NL_SOLUONG;
    }
}
