package com.sajarora.skintracker.adapter;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.sajarora.skintracker.FaceAnalysisFragment;
import com.sajarora.skintracker.R;
import com.sajarora.skintracker.custom.DayAxisValueFormatter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sajarora on 3/11/17.
 */
public class FaceShotsAdapter extends RecyclerView.Adapter<FaceShotsAdapter.ViewHolder>{

    public int id = 0;
    public List<FaceAnalysisFragment.FaceShot> images;

    public FaceShotsAdapter(FaceAnalysisFragment.FaceShot[] faceShots) {
        images = new ArrayList<>();
        for (int i = 0; i < faceShots.length; i++){
            images.add(faceShots[i]);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_face_shot, parent, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        FaceAnalysisFragment.FaceShot image = images.get(position);
        holder.shot = image;
        holder.chart.getAxisLeft().setAxisMinimum(4000);
        holder.chart.getAxisLeft().setAxisMaximum(21000);
        if (image.image == null)
            holder.preview.setImageURI(image.imageUri);
        else
            holder.preview.setImageDrawable(image.image);
        holder.title.setText(image.title);
        holder.chart.setData(new BarData(image.dataset));
        holder.chart.invalidate();
    }

    @Override
    public int getItemCount() {
        return images.size();
    }

    public void popImage(FaceAnalysisFragment.FaceShot image) {
        images.add(image);
        notifyDataSetChanged();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        public BarChart chart;
        public TextView title;
        public ImageView preview;
        public FaceAnalysisFragment.FaceShot shot;

        public ViewHolder(View itemView) {
            super(itemView);
            title = (TextView)itemView.findViewById(R.id.title);
            preview = (ImageView)itemView.findViewById(R.id.preview);
            chart = (BarChart) itemView.findViewById(R.id.chart);
            chart.setDrawBarShadow(false);
            chart.setDrawValueAboveBar(true);

            chart.getDescription().setEnabled(false);

            // if more than 60 entries are displayed in the chart, no values will be
            // drawn
            chart.setMaxVisibleValueCount(60);

            // scaling can now only be done on x- and y-axis separately
            chart.setPinchZoom(false);

            chart.setDrawGridBackground(false);
            chart.getAxisRight().setEnabled(false);
            chart.getAxisLeft().setAxisMinimum(4000);
            chart.getAxisLeft().setAxisMaximum(21000);
            chart.animateY(3000);

            IAxisValueFormatter xAxisFormatter = new DayAxisValueFormatter(chart);

            XAxis xAxis = chart.getXAxis();
            xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
            xAxis.setDrawGridLines(false);
            xAxis.setGranularity(1f); // only intervals of 1 day
            xAxis.setLabelCount(7);
            xAxis.setValueFormatter(xAxisFormatter);

            Legend l = chart.getLegend();
            l.setVerticalAlignment(Legend.LegendVerticalAlignment.BOTTOM);
            l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.LEFT);
            l.setOrientation(Legend.LegendOrientation.HORIZONTAL);
            l.setDrawInside(false);
            l.setForm(Legend.LegendForm.SQUARE);
            l.setFormSize(9f);
            l.setTextSize(11f);
            l.setXEntrySpace(4f);

            itemView.findViewById(R.id.btnCalorieCount).setOnClickListener(this);
            itemView.findViewById(R.id.btnStepCount).setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v.getId() == R.id.btnCalorieCount){
                this.chart.getAxisLeft().setAxisMinimum(1000);
                this.chart.getAxisLeft().setAxisMaximum(1700);
                chart.animateY(3000);
                this.chart.setData(new BarData(this.shot.calorieDataset));
            } else {
                this.chart.getAxisLeft().setAxisMinimum(4000);
                this.chart.getAxisLeft().setAxisMaximum(21000);
                chart.animateY(3000);
                this.chart.setData(new BarData(this.shot.dataset));
            }
            this.chart.invalidate();
        }
    }
}
