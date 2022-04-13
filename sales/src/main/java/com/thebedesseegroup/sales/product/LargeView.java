package com.thebedesseegroup.sales.product;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Window;
import android.view.WindowManager;

import com.thebedesseegroup.sales.utilities.Utilities;

import java.io.IOException;

import it.sephiroth.android.library.imagezoom.ImageViewTouch;
import it.sephiroth.android.library.imagezoom.ImageViewTouchBase;

/**
 * Activity where user can see a full view of an image and can pinch to zoom.
 */
public class LargeView extends Activity {

    final public static String KEY_IMAGE_URI = "image_uri";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);

        final ImageViewTouch imageViewTouch = new ImageViewTouch(this, null);

        imageViewTouch.setDisplayType(ImageViewTouchBase.DisplayType.FIT_TO_SCREEN);

        try {
            final Uri imageUri = (Uri)getIntent().getExtras().get(KEY_IMAGE_URI);
            final Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), imageUri);
            imageViewTouch.setImageBitmap(bitmap, null, 1, 3);
        } catch (IOException e) {
            Utilities.shortToast(this, "Oops, couldn't load image!");
        }


        setContentView(imageViewTouch);
    }


    public static void launch(final Context context, final Uri uri) {
        final Intent intent = new Intent(context, LargeView.class);
        intent.putExtra(KEY_IMAGE_URI, uri);
        context.startActivity(intent);
    }

}
