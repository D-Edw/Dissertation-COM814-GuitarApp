package com.example.dissertation814.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.dissertation814.R;
import com.example.dissertation814.models.Diagram;

import java.util.List;

public class DiagramAdapter extends BaseAdapter {

    private List<Diagram> list;
    private Context context;
    private OnItemClickListener mListener;

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener){
        mListener = listener;
    }

    public DiagramAdapter(List<Diagram> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int position) {
        return list.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        Diagram diagram = list.get(position);

        View view = convertView;

        if (view == null) {
            view = LayoutInflater.from(context).inflate(R.layout.diagram_card_view, parent, false);
        }

        ImageView diagramImage = view.findViewById(R.id.diagramImageView);
        TextView diagramName = view.findViewById(R.id.diagramNameTextView);

        int image = diagram.getDiagramRes();
        String name = diagram.getDiagramName();

        diagramImage.setImageResource(image);
        diagramName.setText(name);

        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mListener != null){
                    int position2 = list.indexOf(list.get(position));
                    mListener.onItemClick(position2);
                }
            }
        });

        return view;
    }
}
