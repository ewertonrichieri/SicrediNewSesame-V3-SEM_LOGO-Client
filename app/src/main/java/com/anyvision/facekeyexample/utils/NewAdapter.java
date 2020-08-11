package com.anyvision.facekeyexample.utils;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;
import com.anyvision.facekeyexample.R;
import java.util.ArrayList;

public class NewAdapter extends RecyclerView.Adapter<NewAdapter.ViewHolder> {

    private ArrayList<String> dataset;
    private Context context;

    public NewAdapter(ArrayList<String> dataset, Context context){
        this.dataset = dataset;
        this.context = context;
    }

    @NonNull
    @Override
    public NewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LinearLayout linearLayout = (LinearLayout) LayoutInflater.from(context).inflate(R.layout.recycler_solicitation_extension_main, parent, false);
        return new ViewHolder(linearLayout);
    }

    @Override
    public void onBindViewHolder(@NonNull NewAdapter.ViewHolder holder, int position) {
    holder.textView.setText(dataset.get(position));
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{
     TextView textView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textViewRecycler_Main);
        }
    }

//    public void uploadListItems(ArrayList<String> listItems){
//        final SolicitationDiffCallback diffCallback = new SolicitationDiffCallback(this.dataset, listItems);
//        final DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(diffCallback);
//
//        this.dataset.clear();
//        this.dataset.addAll(listItems);
//        diffResult.dispatchUpdatesTo(this);
//    }
}
