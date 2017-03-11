package com.sajarora.skintracker.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.net.Uri;
import android.provider.MediaStore;

import java.io.IOException;

/**
 * Created by sajarora on 3/11/17.
 */
public class ProblemImage {
    public int averageColor;
    public String comment;
    public String title;
    public Uri image;
    public int redIntensity;

    public ProblemImage(Context context, Uri output, String title) {
        this.image = output;
        this.title = title;
        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(context.getContentResolver(), output);
            long redBucket = 0;
            long greenBucket = 0;
            long blueBucket = 0;
            long pixelCount = 0;

            for (int y = 0; y < bitmap.getHeight(); y++)
            {
                for (int x = 0; x < bitmap.getWidth(); x++)
                {
                    int c = bitmap.getPixel(x, y);

                    pixelCount++;
                    redBucket += Color.red(c);
                    greenBucket += Color.green(c);
                    blueBucket += Color.blue(c);
                    // does alpha matter?
                }
            }
            this.averageColor = Color.rgb((int)(redBucket / pixelCount),
                    (int)(greenBucket / pixelCount),
                    (int)(blueBucket / pixelCount));
            this.redIntensity = (Color.red(this.averageColor) - 245) * -1;
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
}
