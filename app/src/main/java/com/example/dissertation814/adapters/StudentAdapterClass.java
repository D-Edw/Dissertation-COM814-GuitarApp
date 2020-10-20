package com.example.dissertation814.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.example.dissertation814.R;
import com.example.dissertation814.models.Student;

import java.util.ArrayList;

public class StudentAdapterClass extends RecyclerView.Adapter<StudentAdapterClass.MyViewHolder> {

    private ArrayList<Student> list;

    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        //click method for update button
        void onUpdateClick(int position);
        //click method for delete button
        void onDeleteClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public StudentAdapterClass(ArrayList<Student> list){

        this.list = list;
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
         View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_holder, viewGroup, false);
         return new MyViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {
        myViewHolder.name.setText(list.get(i).getStudentName());
        myViewHolder.email.setText(list.get(i).getStudentEmail());

    }

    @Override
    public int getItemCount() {

        return list.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        TextView name, email;
        ImageButton update, delete;
        MyViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.studentName);
            email = itemView.findViewById(R.id.studentEmail);
            update = itemView.findViewById(R.id.updateButton);
            delete = itemView.findViewById(R.id.deleteButton);

            //update click listener
            update.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onUpdateClick(position);
                        }
                    }

                }
            });

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
