package com.raihanhidayatullahdjunaedi.rhdcalorie;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.raihanhidayatullahdjunaedi.rhdcalorie.entity.Kalori;
import com.raihanhidayatullahdjunaedi.rhdcalorie.helper.SqliteHelper;
import com.raihanhidayatullahdjunaedi.rhdcalorie.other.KaloriAdapter;

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

import rhdcalorie.R;

public class SearchActivity extends AppCompatActivity {

    RecyclerView kaloriView;
    EditText editSearch;

    SQLiteOpenHelper sqliteHelper;
    LinearLayoutManager linierlayoutmanager;
    private ArrayList<Kalori> kaloriArrayList = new ArrayList<>();
    private ArrayList<Kalori> filteredList = new ArrayList<>();;
    private KaloriAdapter mAdapter;
    String queryGetAllKalori;

    public String kaloriId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        kaloriView = findViewById(R.id.kalori_list);
        editSearch = findViewById(R.id.edit_search);

        sqliteHelper        = new SqliteHelper(this);
        linierlayoutmanager = new LinearLayoutManager(this);

        editSearch.requestFocus();
        editSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(!s.toString().equals("")){
                    mAdapter.getFilter().filter(s.toString());
                }else{
                    onResume();
                }
            }
            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    @Override
    public void onResume(){
        super.onResume();
        queryGetAllKalori =
                "SELECT kalori_id, kalori_type, kalori_total, kalori_info, kalori_date, strftime('%d/%m/%Y', kalori_date) AS tgl " +
                        "FROM tb_kalori ORDER BY kalori_id DESC";
        KaloriAdapter();
    }

    private void KaloriAdapter(){
        kaloriView.setLayoutManager(linierlayoutmanager);
        kaloriView.setHasFixedSize(true);
        kaloriArrayList = listKalori();

        if(kaloriArrayList.size() > 0){
            kaloriView.setVisibility(View.VISIBLE);
            filteredList.clear();

            filteredList.addAll(kaloriArrayList);
            mAdapter = new KaloriAdapter(this, filteredList);
            kaloriView.setAdapter(mAdapter);

            mAdapter.setOnItemCustomFilter(new KaloriAdapter.CustomFilter() {
                @Override
                protected void CustomFilter(KaloriAdapter kaloriadapter) {
                    mAdapter = kaloriadapter;
                }

                @Override
                protected FilterResults performFiltering(CharSequence constraint) {
                    filteredList.clear();
                    final FilterResults results = new FilterResults();
                    if (constraint.length() == 0) {
                        filteredList.addAll(kaloriArrayList);
                    } else {
                        final String filterPattern = constraint.toString().toLowerCase().trim();
                        for (final Kalori mKalori : kaloriArrayList) {
                            if (mKalori.getKalori_info().toLowerCase().contains(filterPattern)
                                    || mKalori.getKalori_date().toLowerCase().contains(filterPattern)) {
                                filteredList.add(mKalori);
                            }
                        }
                    }
                    System.out.println("Count Number " + filteredList.size());
                    results.values = filteredList;
                    results.count = filteredList.size();
                    return results;
                }

                @Override
                protected void publishResults(CharSequence constraint, FilterResults results) {
                    mAdapter.notifyDataSetChanged();
                }
            });

            mAdapter.setOnItemClickCallback(new KaloriAdapter.OnItemClickCallback() {
                @Override
                public void onItemClicked(View v) {
                    MainActivity.kaloriId = ((TextView) v.findViewById(R.id.text_kalori_id)).getText().toString();
                    KaloriListMenu();
                }
            });
        }else {
            kaloriView.setVisibility(View.GONE);
            Toast.makeText(this, "Belum ada data tentang kalori kamu, ayo tambah data kalorimu", Toast.LENGTH_LONG).show();
        }
    }

    //    view dialog edit and hapus, whoaa in file operation_menu.xml
    public void KaloriListMenu(){
        final Dialog dialog = new Dialog(SearchActivity.this);

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
                startActivity(new Intent(SearchActivity.this, EditActivity.class));
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
                Toast.makeText(getApplicationContext(), "Kalori berhasil dihapus", Toast.LENGTH_LONG).show();
                KaloriAdapter();
            }
        });
        alertdialog.setNegativeButton("Enggak", new DialogInterface.OnClickListener() {
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
        cursor = db.rawQuery(queryGetAllKalori, null);

        if(cursor.moveToFirst()){
            do{
                String kalori_id = cursor.getString(0);
                String kalori_type = cursor.getString(1);
                String kalori_total = formatRp.format(cursor.getDouble(2));
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
