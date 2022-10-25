package com.raihanhidayatullahdjunaedi.rhdcalorie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.raihanhidayatullahdjunaedi.rhdcalorie.entity.Kalori;
import com.raihanhidayatullahdjunaedi.rhdcalorie.helper.SqliteHelper;
import com.raihanhidayatullahdjunaedi.rhdcalorie.other.KaloriAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import rhdcalorie.R;

public class InOutOnclick extends AppCompatActivity {

    private static final String TAG = InOutOnclick.class.getSimpleName();

    FrameLayout fLayout;
    RecyclerView kaloriView;
    TextView textJudul;
    ImageView imgIc;
    
    SQLiteOpenHelper sqliteHelper;
    LinearLayoutManager linierlayoutmanager;
    private ArrayList<Kalori> kaloriArrayList = new ArrayList<>();
    private KaloriAdapter mAdapter;
    String queryGetAllKaloriPengeluaran, queryGetAllKaloriPemasukan;

    public String kaloriId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_in_out_onclick);

        fLayout = findViewById(R.id.activity_to_do);
        kaloriView = findViewById(R.id.kalori_list);
        textJudul = findViewById(R.id.txt_judul);
        imgIc   = findViewById(R.id.img_ic);

        sqliteHelper            = new SqliteHelper(this);
        linierlayoutmanager = new LinearLayoutManager(this);
        textJudul.setText(MainActivity.navigation.equals("pengeluaran") ? "Kalori Keluar" : "Kalori Masuk");
        Resources res = getResources();
        imgIc.setImageDrawable(res.getDrawable(MainActivity.navigation.equals("pengeluaran") ?
                R.mipmap.caloriedown : R.mipmap.calorieup));
    }

    @Override
    public void onResume(){
        super.onResume();
        queryGetAllKaloriPemasukan =
                "SELECT kalori_id, kalori_type, kalori_total, kalori_info, kalori_date, " +
                        "strftime('%d/%m/%Y', kalori_date) AS tgl " +
                        "FROM tb_kalori WHERE kalori_type = 'Kalori Masuk' ORDER BY kalori_id DESC";
        queryGetAllKaloriPengeluaran =
                "SELECT kalori_id, kalori_type, kalori_total, kalori_info, kalori_date, " +
                        "strftime('%d/%m/%Y', kalori_date) AS tgl " +
                        "FROM tb_kalori WHERE kalori_type = 'Kalori Keluar' ORDER BY kalori_id DESC";
        KaloriAdapter();
    }

    private void KaloriAdapter(){
        kaloriView.setLayoutManager(linierlayoutmanager);
        kaloriView.setHasFixedSize(true);
        kaloriArrayList = listKalori();

        if(kaloriArrayList.size() > 0){
            kaloriView.setVisibility(View.VISIBLE);
            mAdapter = new KaloriAdapter(this, kaloriArrayList);
            kaloriView.setAdapter(mAdapter);

            mAdapter.setOnItemClickCallback(new KaloriAdapter.OnItemClickCallback() {
                @Override
                public void onItemClicked(View v) {
                    MainActivity.kaloriId = ((TextView) v.findViewById(R.id.text_kalori_id)).getText().toString();
                    KaloriListMenu();
                }
            });
        }else {
            kaloriView.setVisibility(View.GONE);
            Toast.makeText(this, "Belum ada data. Ayo tambah sekarang!", Toast.LENGTH_LONG).show();
        }
    }

//    view dialog edit and hapus, whoaa in file operation_menu.xml
    public void KaloriListMenu(){
        final Dialog dialog = new Dialog(InOutOnclick.this);

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
                startActivity(new Intent(InOutOnclick.this, EditActivity.class));
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

    public void KaloriDelete(){
        AlertDialog.Builder alertdialog = new AlertDialog.Builder(this);
        alertdialog.setTitle("Konfirmasi");
        alertdialog.setMessage("Hapus data kalori ini?");
        alertdialog.setPositiveButton("Ya", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();

//                delete data action
                SQLiteDatabase database = sqliteHelper.getWritableDatabase();
                database.execSQL("DELETE FROM tb_kalori WHERE kalori_id = '" + MainActivity.kaloriId + "'");
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
    
    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(sqliteHelper != null){
            sqliteHelper.close();
        }
    }

    public ArrayList<Kalori> listKalori(){
        NumberFormat formatRp = NumberFormat.getInstance(Locale.GERMANY);
        SQLiteDatabase db = sqliteHelper.getReadableDatabase();
        ArrayList<Kalori> storeKalori = new ArrayList<>();
        Cursor cursor;
        String queryDefinition = MainActivity.navigation.equals("pengeluaran") ?
                queryGetAllKaloriPengeluaran : queryGetAllKaloriPemasukan;
        cursor = db.rawQuery(queryDefinition, null);

        if(cursor.moveToFirst()){
            do{
                String kalori_id = cursor.getString(0);
                String kalori_type = cursor.getString(1);
                String kalori_total = cursor.getString(2);
                String kalori_info = cursor.getString(3);
//                String kalori_date = cursor.getString(4);
                String kalori_date_fr = cursor.getString(5);
                storeKalori.add(new Kalori(kalori_id, kalori_type, kalori_total, kalori_info, kalori_date_fr));
            } while (cursor.moveToNext());
        }
        cursor.close();
        return storeKalori;
    }
}
