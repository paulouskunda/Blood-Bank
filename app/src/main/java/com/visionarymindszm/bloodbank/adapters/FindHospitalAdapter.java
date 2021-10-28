package com.visionarymindszm.bloodbank.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.visionarymindszm.bloodbank.R;
import com.visionarymindszm.bloodbank.models.FindHospitalModel;
import com.visionarymindszm.bloodbank.screens.FindHospital;

import java.util.List;

public class FindHospitalAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    List<FindHospitalModel> findHospitalModelList;
    RecyclerViewClickListener mListener;

    public FindHospitalAdapter(List<FindHospitalModel> findHospitalModelList, RecyclerViewClickListener mListener) {
        this.findHospitalModelList = findHospitalModelList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_hospital_item, parent, false);
        return new FindHospitalHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        FindHospitalAdapter.FindHospitalHolder findHospitalHolder = (FindHospitalHolder) holder;
        findHospitalHolder.cardHospitalAddress.setText(findHospitalModelList.get(position).getHospitalAddress());
        findHospitalHolder.cardHospitalName.setText(findHospitalModelList.get(position).getHospitalName());
    }

    @Override
    public int getItemCount() {
        return findHospitalModelList.size();
    }
    public class FindHospitalHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView cardHospitalName, cardHospitalAddress;
        Button directions;
        private RecyclerViewClickListener mListener;
        public FindHospitalHolder(@NonNull View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            cardHospitalName = itemView.findViewById(R.id.cardHospitalName);
            cardHospitalAddress = itemView.findViewById(R.id.cardHospitalAddress);
            directions = itemView.findViewById(R.id.directions);
            this.mListener = listener;
            directions.setOnClickListener(this);

        }

        @Override
        public void onClick(View view) {
            mListener.onRowClick(directions, getAdapterPosition());
        }
    }

    public interface RecyclerViewClickListener{
        void onRowClick(View view, int position);
    }}
