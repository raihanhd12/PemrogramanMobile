package com.raihanhidayatullahdjunaedi.rhdcalorie.other;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Filter;
import android.widget.Filterable;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.raihanhidayatullahdjunaedi.rhdcalorie.MainActivity;

import com.raihanhidayatullahdjunaedi.rhdcalorie.entity.Kalori;
import com.raihanhidayatullahdjunaedi.rhdcalorie.helper.SqliteHelper;

import java.util.ArrayList;

import rhdcalorie.R;

public class KaloriAdapter extends RecyclerView.Adapter<KaloriViewHolder> implements Filterable {

    private Context context;
    private ArrayList<Kalori> listKalori;
    private ArrayList<Kalori> mArrayList;
    private SqliteHelper sqlitehelper;
    private OnItemClickCallback onItemClickCallback;
    private CustomFilter mFilter;

    public KaloriAdapter(Context context, ArrayList<Kalori> listKalori) {
        this.context    = context;
        this.listKalori    = listKalori;
        this.mArrayList = listKalori;
        sqlitehelper    = new SqliteHelper(context);
    }

    @NonNull
    @Override
    public KaloriViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_kalori_in_out, parent, false);
        return new KaloriViewHolder(view);
    }

    public void setOnItemClickCallback(OnItemClickCallback onItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback;
    }

    public void setOnItemCustomFilter(CustomFilter CustomFilter) {
        this.mFilter = CustomFilter;
    }

    @Override
    public void onBindViewHolder(@NonNull final KaloriViewHolder holder, int position) {
        final Kalori kalori = listKalori.get(position);

        holder.textId.setText(kalori.getKalori_id());
        holder.textType.setText(kalori.getKalori_type());
        holder.textInfo.setText(kalori.getKalori_info());
        holder.textTotal.setText(kalori.getKalori_total());
        holder.textDate.setText(kalori.getKalori_date());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onItemClickCallback.onItemClicked(v);
            }
        });

        if(!MainActivity.navigation.equals("")){
            holder.textType.setTextColor(MainActivity.navigation.equals("pengeluaran") ? Color.parseColor("#E92539") : Color.parseColor("#2CA748"));
        }
    }

    @Override
    public int getItemCount() {
        return listKalori.size();
    }

    @Override
    public Filter getFilter() {
        return mFilter;
    }

    public interface OnItemClickCallback {
        void onItemClicked(View v);
    }

    public abstract static class CustomFilter extends Filter{
        protected abstract void CustomFilter(KaloriAdapter kaloriadapter);
        @Override
        protected abstract FilterResults performFiltering(CharSequence constraint);
        @Override
        protected abstract void publishResults(CharSequence constraint, FilterResults results);
    }

}
