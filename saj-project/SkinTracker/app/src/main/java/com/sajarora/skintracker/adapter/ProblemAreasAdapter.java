package com.sajarora.skintracker.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.sajarora.skintracker.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sajarora on 3/11/17.
 */
public class ProblemAreasAdapter extends RecyclerView.Adapter<ProblemAreasAdapter.ViewHolder>{

    public int id = 0;
    public List<ProblemImage> images;

    public ProblemAreasAdapter() {
        images = new ArrayList<>();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_problem_image, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        ProblemImage image = images.get(position);
        holder.thumb.setImageURI(image.image);
        holder.title.setText(image.title);
        holder.comment.setText(image.comment);
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void popImage(ProblemImage image) {
        images.add(image);
        notifyDataSetChanged();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title;
        public EditText comment;
        public ImageView thumb;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.text);
            comment = (EditText)itemView.findViewById(R.id.etComments);
            thumb = (ImageView)itemView.findViewById(R.id.thumb);
        }
    }
}
