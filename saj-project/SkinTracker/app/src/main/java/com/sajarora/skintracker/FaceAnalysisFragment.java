package com.sajarora.skintracker;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.sajarora.skintracker.adapter.FaceShotsAdapter;

import java.util.ArrayList;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FaceAnalysisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FaceAnalysisFragment extends Fragment {

    private LinearLayoutManager mLayoutManager;
    private FaceShotsAdapter mAdapter;
    private RecyclerView mRecyclerView;

    public class FaceShot {
        public String title;
        public Drawable image;
        public Uri imageUri;
        public BarDataSet dataset;
        public BarDataSet calorieDataset;

        @TargetApi(Build.VERSION_CODES.LOLLIPOP)
        public FaceShot(int drawabaleId, String title, BarDataSet dataset, BarDataSet calorieDataset) {
            this.image = getActivity().getResources().getDrawable(drawabaleId, getActivity().getTheme());
            this.title = title;
            this.dataset = dataset;
            this.calorieDataset = calorieDataset;
        }

        public FaceShot(Uri uri, String title, BarDataSet dataset, BarDataSet calorieDataset) {
            this.imageUri = uri;
            this.title = title;
            this.dataset = dataset;
            this.calorieDataset = calorieDataset;
        }
    }

    FaceShot[] faceShots = new FaceShot[4];

    private static final String TAG = FaceAnalysisFragment.class.getSimpleName();
    private static final int REQUEST_NEW_FACE = 1001;
    private IFragmentInteractionListener mListener;
    private View mLayout;

    public FaceAnalysisFragment() {
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @return A new instance of fragment TrackerFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static FaceAnalysisFragment newInstance() {
        FaceAnalysisFragment fragment = new FaceAnalysisFragment();
        Bundle args = new Bundle();

        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        initialize();
    }

    private void initialize() {
        FloatingActionButton fab = (FloatingActionButton)mLayout.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                launchFaceProfilerActivity();
            }
        });

        faceShots[0] = new FaceShot(
                R.drawable.ic_acne_baseline,
                "Initial: Feb 18th, 2017",
                generateStepData(8000, 9000, "Steps Taken"),
                generateStepData(1600, 1700, "Calories Consumed")
        );
        faceShots[1] = new FaceShot(
                R.drawable.ic_acne_week_1,
                "Week 1: Feb 25th, 2017",
                generateStepData(8000, 10000, "Steps Taken"),
                generateStepData(1400, 1600, "Calories Consumed")
        );
        faceShots[2] = new FaceShot(
                R.drawable.ic_acne_week_2,
                "Week 2: March 4th, 2017",
                generateStepData(10000, 12000, "Steps Taken"),
                generateStepData(1400, 1600, "Calories Consumed")
        );
        faceShots[3] = new FaceShot(
                R.drawable.ic_acne_final,
                "Week 3: March 11th, 2017",
                generateStepData(9000, 20000, "Steps Taken"),
                generateStepData(1300, 1500, "Calories Consumed")
        );

        // Required empty public constructor
        mRecyclerView = (RecyclerView) mLayout.findViewById(R.id.faceshots);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new FaceShotsAdapter(faceShots);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == REQUEST_NEW_FACE){
            Bundle bundle = data.getExtras();
            String title = bundle.getString("Title");
            int average = bundle.getInt("Average");
            mAdapter.popImage(new FaceShot(R.drawable.ic_acne_baseline, title, generateStepData(12000, 150000, "Steps Taken"),
                    generateStepData(1400, 1800, "Calories Consumed")));
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void launchFaceProfilerActivity() {
        startActivityForResult(new Intent(getActivity(), FaceProfilerActivity.class), REQUEST_NEW_FACE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        mLayout = inflater.inflate(R.layout.fragment_face_analysis, container, false);
        return mLayout;
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof IFragmentInteractionListener) {
            mListener = (IFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    private BarDataSet generateStepData(int min, int max, String title){
        Random r = new Random();
        ArrayList<BarEntry> entries = new ArrayList<>();
        for (float i = 0f; i < 7; i++){
            entries.add(new BarEntry(i, r.nextInt(max - min) + min));
        }
        BarDataSet set = new BarDataSet(entries, title);
        set.setColor(ColorTemplate.COLORFUL_COLORS[r.nextInt(4)], 130);
        return set;
    }
}
