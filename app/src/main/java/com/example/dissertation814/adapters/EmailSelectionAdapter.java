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

public class EmailSelectionAdapter extends RecyclerView.Adapter<EmailSelectionAdapter.MyViewHolder>{
    ArrayList<Student> list;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        //click method for selection button
        void onSelectClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public EmailSelectionAdapter(ArrayList<Student> list){
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.student_email_card_holder, parent, false);
        return new MyViewHolder(view, mListener);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.name.setText(list.get(position).getStudentName());
        holder.email.setText(list.get(position).getStudentEmail());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name, email;
        ImageButton select;
        public MyViewHolder(@NonNull View itemView, final OnItemClickListener listener) {
            super(itemView);
            name = itemView.findViewById(R.id.studentName2);
            email = itemView.findViewById(R.id.studentEmail2);
            select = itemView.findViewById(R.id.selectImageButton);

            //selection click listener
            select.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (listener != null){
                        int position = getAdapterPosition();
                        if(position != RecyclerView.NO_POSITION){
                            listener.onSelectClick(position);
                        }
                    }
                }
            });
        }
    }


}
