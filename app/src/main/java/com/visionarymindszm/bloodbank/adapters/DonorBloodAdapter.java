package com.visionarymindszm.bloodbank.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;
import com.visionarymindszm.bloodbank.R;
import com.visionarymindszm.bloodbank.models.DonateBloodModel;
import java.util.List;

public class DonorBloodAdapter  extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<DonateBloodModel> donateBloodModelList;
    private RecyclerViewClickListener mListener;
    public DonorBloodAdapter(List<DonateBloodModel> donateBloodModelList) {
        this.donateBloodModelList = donateBloodModelList;
//        this.mListener = mListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.donate_blood_card_layout, parent, false);
        return new DonorBloodAdapter.DonorViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DonorBloodAdapter.DonorViewHolder donorViewHolder = (DonorViewHolder) holder;
        donorViewHolder.dateCardDonation.setText(donateBloodModelList.get(position).getDonDate());
        donorViewHolder.hospitalCardDonation.setText(donateBloodModelList.get(position).getDonHos());
        donorViewHolder.reasonCardDonation.setText(donateBloodModelList.get(position).getDonReason());
    }

    @Override
    public int getItemCount() {
        return donateBloodModelList.size();
    }

    public  class DonorViewHolder extends RecyclerView.ViewHolder {

        TextView dateCardDonation, hospitalCardDonation, reasonCardDonation;
        public DonorViewHolder(@NonNull View itemView) {
            super(itemView);
            hospitalCardDonation = itemView.findViewById(R.id.hospitalCardDonation);
            dateCardDonation = itemView.findViewById(R.id.dateCardDonation);
            reasonCardDonation = itemView.findViewById(R.id.reasonCardDonation);
        }


    }

    public interface RecyclerViewClickListener{
        void onRowClick(View view, int position);
    }
}
