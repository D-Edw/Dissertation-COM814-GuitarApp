package com.example.dissertation814.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.dissertation814.R;
import com.example.dissertation814.models.PdfFile;

import java.util.ArrayList;

public class PdfFileAdapter extends RecyclerView.Adapter<PdfFileAdapter.MyViewHolder> {

    private static OnItemClickListener mListener;
    //array list of files
    ArrayList<PdfFile> pdfList;

    public interface OnItemClickListener{
        void onShareClick(int position);
        void onOpenClick(int position);
        void onDeleteClick(int position);
    }

    public static void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public PdfFileAdapter(ArrayList<PdfFile> pdfList){
        this.pdfList = pdfList;
    }


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.file_card_holder, parent, false);
        return new MyViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        PdfFile p = pdfList.get(position);

        holder.name.setText(p.getPdfFileName());

    }

    @Override
    public int getItemCount() {
        return pdfList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        Button name;
        ImageButton share, delete;

        public MyViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            //name = itemView.findViewById(R.id.fileName);
            share = itemView.findViewById(R.id.shareButton);
            name = itemView.findViewById(R.id.fileName);
            delete = itemView.findViewById(R.id.deleteButton);

            //share click listener
            share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onShareClick(position);
                        }
                    }
                }
            });

            //open click listener
            name.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onOpenClick(position);
                        }
                    }
                }
            });

            //delete click listener
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onDeleteClick(position);
                        }
                    }
                }
            });
        }
    }
}
