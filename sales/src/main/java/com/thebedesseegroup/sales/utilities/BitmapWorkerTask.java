package com.thebedesseegroup.sales.utilities;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.widget.ImageView;

import com.thebedesseegroup.sales.R;

import java.lang.ref.WeakReference;

public class BitmapWorkerTask extends AsyncTask<String, Void, Bitmap> {
    private final WeakReference<ImageView> imageViewReference;
    private String data;
    private int[] mDimens;
    private Context mContext;
    private static Bitmap sDefaultBitmap;

    public BitmapWorkerTask(Context context, ImageView imageView, int [] dimens) {
        imageViewReference = new WeakReference<ImageView>(imageView);
        mDimens = dimens;
        mContext = context;
        if(sDefaultBitmap == null) {
            sDefaultBitmap = BitmapFactory.decodeResource(mContext.getResources(), R.drawable.product_default_image);
        }
    }

    @Override
    protected Bitmap doInBackground(String... params) {
        data = params[0];
        return Utilities.decodeSampledBitmapFromFile(data, mDimens[0], mDimens[1]);
    }

    @Override
    protected void onPostExecute(Bitmap bitmap) {
        if (imageViewReference != null && bitmap != null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(bitmap);
            }
        } else if (imageViewReference != null && bitmap == null) {
            final ImageView imageView = imageViewReference.get();
            if (imageView != null) {
                imageView.setImageBitmap(sDefaultBitmap);
            }
        }
    }
}