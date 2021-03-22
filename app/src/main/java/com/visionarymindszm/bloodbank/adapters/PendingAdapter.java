package com.visionarymindszm.bloodbank.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.visionarymindszm.bloodbank.R;
import com.visionarymindszm.bloodbank.models.PendingListModel;

import java.util.List;
import java.util.Objects;

public class PendingAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    List<PendingListModel> pendingListModels;
    private PendingAdapter.RecyclerViewClickListener mListener;

    public PendingAdapter(List<PendingListModel> pendingListModels, RecyclerViewClickListener mListener) {
        this.pendingListModels = pendingListModels;
        this.mListener = mListener;
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.card_pending_requests, parent, false);
        return new PendingAdapter.PendingViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        PendingAdapter.PendingViewHolder pendingViewHolder = (PendingAdapter.PendingViewHolder) holder;
        pendingViewHolder.dateOfTransfusion.setText(pendingListModels.get(position).getDateOfBlood());
        pendingViewHolder.receiver_name.setText(pendingListModels.get(position).getName());
        pendingViewHolder.hospitalName.setText(pendingListModels.get(position).getHospital());
        pendingViewHolder.status.setText("Status "+pendingListModels.get(position).getApprovalStatus());
        pendingViewHolder.reasonForBloodRequest.setText(pendingListModels.get(position).getReasonOfBlood());
        if (Objects.equals(pendingListModels.get(position).getUserType(), "receiver")){
            pendingViewHolder.takeAction.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return pendingListModels.size();
    }

    public  class PendingViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        TextView receiver_name, hospitalName, dateOfTransfusion, status, reasonForBloodRequest;
        Button takeAction;
        private RecyclerViewClickListener mListener;
        public PendingViewHolder(@NonNull View itemView, RecyclerViewClickListener listener) {
            super(itemView);
            hospitalName = itemView.findViewById(R.id.hospitalName);
            receiver_name = itemView.findViewById(R.id.receiver_name);
            dateOfTransfusion = itemView.findViewById(R.id.dateOfTransfusion);
            status = itemView.findViewById(R.id.status);
            reasonForBloodRequest = itemView.findViewById(R.id.reasonForBloodRequest);
            takeAction = itemView.findViewById(R.id.takeAction);
            mListener = listener;
            takeAction.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            mListener.onRowClick(takeAction, getAdapterPosition());
        }
    }


    public interface RecyclerViewClickListener{
        void onRowClick(View view, int position);
    }
}

