package com.sajarora.skintracker;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.karumi.dexter.listener.single.PermissionListener;
import com.soundcloud.android.crop.Crop;

import java.io.File;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link IFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FaceAnalysisFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class FaceAnalysisFragment extends Fragment {

    private static final String TAG = FaceAnalysisFragment.class.getSimpleName();
    private static final int REQUEST_NEW_FACE = 1001;
    private IFragmentInteractionListener mListener;
    private View mLayout;

    public FaceAnalysisFragment() {
        // Required empty public constructor
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
}
