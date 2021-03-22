package com.visionarymindszm.bloodbank.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.visionarymindszm.bloodbank.R;
import com.visionarymindszm.bloodbank.models.DonorsListModel;

import java.util.List;


public class DonorListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<DonorsListModel> donorsListModelList;
    private RecyclerViewClickListener mListener;

    public DonorListAdapter(List<DonorsListModel> donorsListModelList, RecyclerViewClickListener mListener) {
        this.donorsListModelList = donorsListModelList;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_donor_list_item, parent, false);
        return new DonorViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        DonorListAdapter.DonorViewHolder donorViewHolder = (DonorViewHolder) holder;
        donorViewHolder.cardName.setText(donorsListModelList.get(position).getDonorName());
        String locatedMe = donorsListModelList.get(position).getDonorTown() +", "+ donorsListModelList.get(position).getDonorAddress();
        donorViewHolder.cardLocation.setText(locatedMe);
    }

    @Override
    public int getItemCount() {
        return donorsListModelList.size();
    }

    public  class DonorViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView cardName, cardLocation;
        CardView cardDonor;
        private RecyclerViewClickListener mListener;
        public DonorViewHolder(@NonNull View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            cardLocation = itemView.findViewById(R.id.cardLocation);
            cardName = itemView.findViewById(R.id.cardHospitalName);
            cardDonor = itemView.findViewById(R.id.cardDonor);
            mListener = listener;
            cardDonor.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onRowClick(cardDonor, getAdapterPosition());
        }
    }


    public interface RecyclerViewClickListener{
        void onRowClick(View view, int position);
    }
}
