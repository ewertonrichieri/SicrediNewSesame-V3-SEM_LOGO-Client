package com.anyvision.facekeyexample.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import com.anyvision.facekeyexample.R;
import java.util.ArrayList;

public class SolicitHistAprovAdapter extends RecyclerView.Adapter<SolicitHistAprovAdapter.ViewHolder> {

    private ArrayList<String> dataset;
    private Context context;

    public SolicitHistAprovAdapter(ArrayList<String> dataset, Context context) {
        this.dataset = dataset;
        this.context = context;
    }

    @NonNull
    @Override
    public SolicitHistAprovAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.recycler_solicit_items_approved, parent, false);
        return new ViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull SolicitHistAprovAdapter.ViewHolder holder, int position) {
        holder.textView.setText(dataset.get(position));
    }

    @Override
    public int getItemCount() {

        return dataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewRecycler_solicitApproved);
        }
    }
}
