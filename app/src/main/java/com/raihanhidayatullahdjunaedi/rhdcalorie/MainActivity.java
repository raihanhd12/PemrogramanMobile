package com.raihanhidayatullahdjunaedi.rhdcalorie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.raihanhidayatullahdjunaedi.rhdcalorie.helper.SqliteHelper;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Locale;

import rhdcalorie.R;

public class MainActivity extends AppCompatActivity {

    public static String kaloriId;
    public static String navigation = "";

    TextView textMasuk, textKeluar, textTotal;
    ListView listKalori;
    SwipeRefreshLayout swipeRefresh;
    FloatingActionButton fab, fabSearch;
    ArrayList <HashMap <String, String> > kaloriArrayList = new ArrayList<>();
    CardView cvPengeluaran, cvPemasukan;

    String queryGetAllKalori, querySumTotal, queryGetBerat;
    SQLiteOpenHelper sqliteHelper;
    Cursor cursor;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        kaloriId = "";
        queryGetAllKalori = "";
        querySumTotal = "";

        sqliteHelper   = new SqliteHelper(this);
        textMasuk      = findViewById(R.id.text_masuk);
        textKeluar     = findViewById(R.id.text_keluar);
        textTotal      = findViewById(R.id.text_kalori_total);
        listKalori     = findViewById(R.id.list_semua_kalori);
        swipeRefresh   = findViewById(R.id.swipe_refresh);
        fab            = findViewById(R.id.fab);
        cvPengeluaran  = findViewById(R.id.cv_pengeluaran);
        cvPemasukan    = findViewById(R.id.cv_pemasukan);
        fabSearch      = findViewById(R.id.fab_search);


//        jalankan jika swiperefresh on
        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                onResume();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                go to add activity
                startActivity(new Intent(MainActivity.this, AddActivity.class));
            }
        });

        fabSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                navigation = "";
//                go to add activity
                startActivity(new Intent(MainActivity.this, SearchActivity.class));
            }
        });

        cvPengeluaran.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation = "pengeluaran";
//                go to pengeluaran activity
                startActivity(new Intent(MainActivity.this, InOutOnclick.class));
            }
        });

        cvPemasukan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigation = "pemasukan";
//                go to pemasukan activity
                startActivity(new Intent(MainActivity.this, InOutOnclick.class));
            }
        });

    }

    @Override
    public void onResume(){
        super.onResume();
        queryGetAllKalori  =
                "SELECT kalori_id, kalori_type, kalori_total, kalori_info, strftime('%d/%m/%Y', kalori_date) AS tgl " +
                        "FROM tb_kalori ORDER BY kalori_id DESC";
        querySumTotal   =
                "SELECT SUM(kalori_total) AS total, " +
                        "(SELECT SUM(kalori_total) FROM tb_kalori WHERE kalori_type='Kalori Masuk') AS masuk, " +
                        "(SELECT SUM(kalori_total) FROM tb_kalori WHERE kalori_type='Kalori Keluar') AS keluar " +
                        "FROM tb_kalori";
        queryGetBerat   =
                "SELECT berat_badan " +
                "FROM tb_kalori";
        KaloriAdapter();
    }

    private void KaloriAdapter(){
        kaloriArrayList.clear();
        listKalori.setAdapter(null);

        NumberFormat formatRp = NumberFormat.getInstance(Locale.GERMANY);
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        cursor            = db.rawQuery( queryGetAllKalori, null);
        cursor.moveToFirst();

        int i;
        for (i = 0; i < cursor.getCount(); i++){
            cursor.moveToPosition(i);
            HashMap<String, String> map = new HashMap<>();
            map.put("kalori_id",    cursor.getString(0) );
            map.put("kalori_type",  cursor.getString(1) );
            map.put("kalori_total", cursor.getString(2) );
            map.put("kalori_total_rp", formatRp.format(cursor.getDouble(2) ));
            map.put("kalori_info",  cursor.getString(3) );
            map.put("kalori_date",  cursor.getString(4) );
            kaloriArrayList.add(map);
        }

//        jika index kalori tidak ada
        if (i == 0){
            Toast.makeText(getApplicationContext(), "Belum ada data kalori saat ini", Toast.LENGTH_LONG).show();
        }

        SimpleAdapter simpleadapter = new SimpleAdapter(this,
                kaloriArrayList,
                R.layout.list_kalori,
                new String[] { "kalori_id", "kalori_type", "kalori_total_rp", "kalori_info", "kalori_date"},
                new int[] {R.id.text_kalori_id, R.id.text_kalori_type, R.id.text_kalori_total, R.id.text_kalori_info, R.id.text_kalori_date});

        listKalori.setAdapter(simpleadapter);

//        jika salah satu list data kalori diklik
        listKalori.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                kaloriId = ((TextView) view.findViewById(R.id.text_kalori_id)).getText().toString();
                KaloriListMenu();
            }
        });
        KaloriTotal();
    }

//    view dialog edit and hapus, whoaa in file operation_menu.xml
    private void KaloriListMenu(){
        final Dialog dialog = new Dialog(MainActivity.this);

        dialog.setContentView(R.layout.operation_menu);
        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.WRAP_CONTENT);

        TextView textEdit  = dialog.findViewById(R.id.text_edit);
        TextView textHapus = dialog.findViewById(R.id.text_hapus);
        dialog.show();

        textEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                go to edit activity
                startActivity(new Intent(MainActivity.this, EditActivity.class));
            }
        });
        textHapus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
//                go to confirm delete action
                KaloriDelete();
            }
        });
    }

    private void KaloriTotal(){
        NumberFormat formatRp = NumberFormat.getInstance(Locale.GERMANY);
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        cursor = db.rawQuery( querySumTotal, null);
        cursor.moveToFirst();

        textMasuk.setText(formatRp.format(cursor.getDouble(1)) );
        textKeluar.setText(formatRp.format(cursor.getDouble(2)) );
        EditText editBerat = (EditText) findViewById(R.id.edit_beratbadan);
        double berat = Double.valueOf(editBerat.getText().toString());


        textTotal.setText( formatRp.format((cursor.getDouble(1) - cursor.getDouble(2)) + (berat * 7700)) );

//        if after refresh swipe_refresh berhenti berputar
        swipeRefresh.setRefreshing(false);
    }

    private void KaloriDelete(){
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
        alertdialog.setTitle("Konfirmasi");
        alertdialog.setMessage("Hapus data kalori ini?");
        alertdialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

//                delete data action
                SQLiteDatabase database = sqliteHelper.getWritableDatabase();
                database.execSQL("DELETE FROM tb_kalori WHERE kalori_id = '" + kaloriId + "'");
                Toast.makeText(getApplicationContext(), "Data kalori berhasil dihapus", Toast.LENGTH_LONG).show();
                KaloriAdapter();
            }
        });
        alertdialog.setNegativeButton("Tidak", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alertdialog.show();
    }

//    public void beratDone(View view){
//        Intent reload = new Intent(getApplicationContext(),MainActivity.class);
//        startActivity(reload);
//    }
}
