package com.sajarora.skintracker;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by sajarora on 3/11/17.
 */

public class SelectableImageView extends View {

    public static final String TAG = SelectableImageView.class.getSimpleName();

    private Paint bufferedPaint;
    private Bitmap buffer;
    private Canvas board;
    private Path currPath;
    private boolean isFingerDown;

    public SelectableImageView(Context context) {
        super(context);
        initialize();
    }

    public SelectableImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialize();
    }

    public SelectableImageView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialize();
    }

    private void initialize() {
        bufferedPaint = new Paint(Paint.DITHER_FLAG);
    }

    public void setImageFromBitmap(Bitmap image){
        buffer = Bitmap.createBitmap(getWidth(), image.getHeight(),
                Bitmap.Config.ARGB_8888);
        board = new Canvas(buffer);
        board.drawBitmap(image, 0, 0, bufferedPaint);
        invalidate();
    }

    public void drawBitmap(Bitmap bitmap){
        board.drawBitmap(bitmap, 0, 0, bufferedPaint);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        if (buffer != null){
            canvas.drawBitmap(buffer, 0, 0, bufferedPaint);
        }
    }

    class Coordinate {
        int x;
        int y;

        public Coordinate(int x, int y) {
            this.x = x;
            this.y = y;
        }
    }
    class Path {
        Coordinate start;
        Coordinate end;

        public Path(Coordinate startPos, Coordinate endPos) {
            this.start = startPos;
            this.end = endPos;
        }
    }

    Coordinate startPos;
    List<Path> paths = new ArrayList<>();

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN && isEnabled()){
            if (!isFingerDown){
                //note the start x and y
                startPos = new Coordinate((int) event.getX(), (int) event.getY());
            }
            isFingerDown = true;
        } else if (event.getAction() == MotionEvent.ACTION_UP){
            paths.add(new Path(startPos, new Coordinate((int)event.getX(), (int)event.getY())));
            isFingerDown = false;
            Log.d(TAG, "Added path");
        }
        return super.onTouchEvent(event);
    }
}
