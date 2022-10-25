package com.raihanhidayatullahdjunaedi.rhdcalorie.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class SqliteHelper extends SQLiteOpenHelper {

    public SqliteHelper(Context context) {
        super(context, "rhdcalorie", null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Jalankan query
        db.execSQL(
            "CREATE TABLE tb_kalori (kalori_id INTEGER PRIMARY KEY AUTOINCREMENT NOT NULL, kalori_type TEXT," +
                "kalori_total DOUBLE, berat_badan DOUBLE, kalori_info TEXT, kalori_date DATE DEFAULT CURRENT_DATE );"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Hapus tabel jika tabel tb_kalori ada
        db.execSQL("DROP TABLE IF EXISTS tb_kalori");
    }
}
