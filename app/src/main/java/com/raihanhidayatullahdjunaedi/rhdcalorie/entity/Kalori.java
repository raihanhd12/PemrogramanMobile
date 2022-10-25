package com.raihanhidayatullahdjunaedi.rhdcalorie.entity;

public class Kalori {
    private String kalori_id;
    private String kalori_type;
    private String kalori_total;
    private String kalori_info;
    private String kalori_date;
    private String berat_badan;

    public Kalori(String kalori_id, String kalori_type, String kalori_total, String kalori_info, String kalori_date) {
        this.kalori_id      = kalori_id;
        this.kalori_type    = kalori_type;
        this.kalori_total   = kalori_total;
        this.kalori_info    = kalori_info;
        this.kalori_date    = kalori_date;
    }

    public Kalori(String kalori_type, String kalori_total, String kalori_info, String kalori_date) {
        this.kalori_type    = kalori_type;
        this.kalori_total   = kalori_total;
        this.kalori_info    = kalori_info;
        this.kalori_date    = kalori_date;
    }

    public Kalori(String berat_badan) {
        this.berat_badan    = berat_badan;
    }

    public String getKalori_id() {
        return kalori_id;
    }

    public void setKalori_id(String kalori_id) {
        this.kalori_id = kalori_id;
    }

    public String getKalori_type() {
        return kalori_type;
    }

    public void setKalori_type(String kalori_type) {
        this.kalori_type = kalori_type;
    }

    public String getKalori_total() {
        return kalori_total;
    }

    public void setKalori_total(String kalori_total) {
        this.kalori_total = kalori_total;
    }

    public String getKalori_info() {
        return kalori_info;
    }

    public void setKalori_info(String kalori_info) {
        this.kalori_info = kalori_info;
    }

    public String getKalori_date() {
        return kalori_date;
    }

    public void setKalori_date(String kalori_date) {
        this.kalori_date = kalori_date;
    }

    public String getBerat_badan() {
        return berat_badan;
    }

    public void setBerat_badan(String berat_badan) {
        this.berat_badan = berat_badan;
    }
}
