package com.dev.aman.qrcodescanner.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dev.aman.qrcodescanner.DBHelper.DatabaseHelper;
import com.dev.aman.qrcodescanner.R;
import com.dev.aman.qrcodescanner.model.QrModel;

import java.util.ArrayList;

public class QrListAdapter extends RecyclerView.Adapter<QrListAdapter.QrListAdapterViewHolder> {

    private ArrayList<QrModel> arrayList;
    DatabaseHelper dbHelper;
    Context context;
    public QrListAdapter(ArrayList<QrModel> list, DatabaseHelper helper, Context ctxt) {
        arrayList = list;
        dbHelper = helper;
        context = ctxt;
    }

    @NonNull
    @Override
    public QrListAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int i) {
        LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
        View view = layoutInflater.inflate(R.layout.single_list_recycler, parent, false);
        return new QrListAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final QrListAdapterViewHolder holder, final int position) {
        holder.content.setText(arrayList.get(position).getContents());
        holder.date.setText(arrayList.get(position).getDate());
        holder.time.setText(arrayList.get(position).getTime());
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbHelper.deleteData(arrayList.get(position).getId());
                notifyItemRemoved(position);
                Toast.makeText(view.getContext(), "Item Deleted", Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public int getItemCount() {
        return arrayList.size();
    }

    class QrListAdapterViewHolder extends RecyclerView.ViewHolder{
        View view;
        TextView content, date, time;
        ImageView delete;
        QrListAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            view = itemView;

            content = view.findViewById(R.id.contents);
            date = view.findViewById(R.id.date);
            time = view.findViewById(R.id.time);
            delete = view.findViewById(R.id.delete);
        }


    }
}
