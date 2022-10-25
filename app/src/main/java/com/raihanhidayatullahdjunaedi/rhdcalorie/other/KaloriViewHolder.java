package com.raihanhidayatullahdjunaedi.rhdcalorie.other;

import android.view.View;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import rhdcalorie.R;

class KaloriViewHolder extends RecyclerView.ViewHolder {
    TextView textId, textTotal,textDate,textInfo,textType;

    KaloriViewHolder(@NonNull View itemView) {
        super(itemView);
        textId    = itemView.findViewById(R.id.text_kalori_id);
        textTotal = itemView.findViewById(R.id.text_kalori_total);
        textDate  = itemView.findViewById(R.id.text_kalori_date);
        textInfo  = itemView.findViewById(R.id.text_kalori_info);
        textType  = itemView.findViewById(R.id.text_kalori_type);
    }
}