package com.example.dissertation814.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.example.dissertation814.R;
import com.example.dissertation814.models.PdfPage;

import java.util.List;

public class CreateAdapter extends PagerAdapter {

    private List<PdfPage> pagesList;
    private LayoutInflater layoutInflater;
    private Context context;
    private onItemClickListener mListener;

    public interface onItemClickListener{
        //click method for edit button
        void onEditClick(int position);
        //click method for delete button
        void onDeleteClick(int position);
    }

    public void setOnItemClickListener(onItemClickListener listener){
        mListener = listener;
    }

    public CreateAdapter(List<PdfPage> pagesList, Context context) {
        this.pagesList = pagesList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return pagesList.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view.equals(object);
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, final int position) {
        layoutInflater = LayoutInflater.from(context);
        View view = null;

        if(pagesList.get(position).getPageType() == "Text") {
            view = layoutInflater.inflate(R.layout.create_text_cardview, container, false);

            TextView inputText = view.findViewById(R.id.inputTextView);
            ImageButton editButton = view.findViewById(R.id.imageButtonEdit);
            ImageButton deleteButton = view.findViewById(R.id.imageButtonDelete);

            inputText.setText(pagesList.get(position).getPageText());

            //edit click listener
            editButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null){
                        int position2 = pagesList.indexOf(pagesList.get(position));
                        mListener.onEditClick(position2);
                    }
                }
            });

            //delete click listener
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null){
                        int position3 = pagesList.indexOf(pagesList.get(position));
                        mListener.onDeleteClick(position3);
                    }
                }
            });

            container.addView(view, 0);


        }else if(pagesList.get(position).getPageType() == "Image"){
            view = layoutInflater.inflate(R.layout.create_image_cardview, container, false);

            ImageView inputImage = view.findViewById(R.id.inputImageView);
            ImageButton deleteButton = view.findViewById(R.id.imageButtonDelete);

            //image getter
            inputImage.setImageResource(pagesList.get(position).getPageImage());

            //delete click listener
            deleteButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (mListener != null){
                        int position4 = pagesList.indexOf(pagesList.get(position));
                        mListener.onDeleteClick(position4);
                    }
                }
            });

            container.addView(view, 0);
        }

        assert view != null;
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View)object);
    }
}
