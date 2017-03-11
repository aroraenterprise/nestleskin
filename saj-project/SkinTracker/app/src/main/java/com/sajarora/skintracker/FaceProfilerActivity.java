package com.sajarora.skintracker;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.listener.multi.DialogOnAnyDeniedMultiplePermissionsListener;
import com.karumi.dexter.listener.multi.MultiplePermissionsListener;
import com.sajarora.skintracker.adapter.ProblemAreasAdapter;
import com.sajarora.skintracker.adapter.ProblemImage;
import com.soundcloud.android.crop.Crop;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class FaceProfilerActivity extends AppCompatActivity {

    private static final String TAG = FaceProfilerActivity.class.getSimpleName();
    private ImageView mImageView;
    private MultiplePermissionsListener mCameraPermissionListener;
    private Uri mCurrentImage = null;
    private List<Uri> problemImages = new ArrayList<>();
    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private ProblemAreasAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_face_profiler);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        initialize();
    }

    private void initialize() {
        mCameraPermissionListener = DialogOnAnyDeniedMultiplePermissionsListener.Builder
                .withContext(this)
                .withTitle("Camera permission")
                .withMessage("Camera and Storage permission is needed to take pictures of your face.")
                .withButtonText( android.R.string.ok)
                .withIcon(R.mipmap.ic_launcher)
                .build();

        Dexter.withActivity(this)
                .withPermissions(Manifest.permission.CAMERA,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)
                .withListener(mCameraPermissionListener)
                .check();
        mImageView = (ImageView) findViewById(R.id.face_preview);
        mRecyclerView = (RecyclerView) findViewById(R.id.problem_areas);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new ProblemAreasAdapter();
        mRecyclerView.setAdapter(mAdapter);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                beginCrop(mCurrentImage);
            }
        });

        launchCapture();
    }

    private void launchCapture() {
        Crop.pickImage(this);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.face_profiler, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case R.id.action_done:
                return true;
            case R.id.action_reset:
                mCurrentImage = null;
//                thumb.setImageFromBitmap(null);
                launchCapture();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == Crop.REQUEST_PICK && resultCode == Activity.RESULT_OK) {
            mCurrentImage = data.getData();
            mImageView.setImageURI(mCurrentImage);
            Toast.makeText(this, "Select areas to analyze.", Toast.LENGTH_LONG).show();
        }
        else if (requestCode == Crop.REQUEST_CROP) {
            handleCrop(resultCode, data);
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void handleCrop(int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            Log.d(TAG, "Got cropped image");
            //add image to recycler view
            addImage(Crop.getOutput(data));
        } else if (resultCode == Crop.RESULT_ERROR) {
            Toast.makeText(this, Crop.getError(data).getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void addImage(Uri output) {
        ProblemImage image = new ProblemImage();
        image.image = output;
        Calendar calendar = Calendar.getInstance();
        image.title = (String.format("%1$tA %1$tb %1$td %1$tY at %1$tI:%1$tM %1$Tp", calendar));
        mAdapter.popImage(image);
        Log.d(TAG, "added image");
    }

    private void beginCrop(Uri source) {
        Uri dest = Uri.fromFile(new File(this.getCacheDir(), "cropped" + Integer.toString(mAdapter.getItemCount() + 1)));
        Crop.of(source, dest).asSquare().start(this);
    }


}
