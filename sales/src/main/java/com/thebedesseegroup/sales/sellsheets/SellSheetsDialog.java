package com.thebedesseegroup.sales.sellsheets;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.WindowManager;
import android.widget.Toast;

import com.thebedesseegroup.sales.R;
import com.thebedesseegroup.sales.utilities.DepthPageTransformer;

/**
 * SellSheets Dialog.
 */
public class SellSheetsDialog extends FragmentActivity {

    public static final int RESULT_CODE = 500;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_catalog_pages);

        // Instantiate a ViewPager and a PagerAdapter.
        ViewPager pager = (ViewPager) findViewById(R.id.pager);
        PagerAdapter pagerAdapter = new SellSheetsPagerAdapter(this, getSupportFragmentManager());
        pager.setAdapter(pagerAdapter);
        pager.setPageTransformer(true, new DepthPageTransformer());

        if (pagerAdapter.getCount() == 0) {
            Toast.makeText(this, "No sell sheets found.", Toast.LENGTH_LONG).show();
            Toast.makeText(this, "HAVE YOU SETUP SUGAR SYNC?", Toast.LENGTH_LONG).show();
            setResult(RESULT_CODE);
            finish();
        }


        findViewById(R.id.imageView_close).setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setResult(RESULT_CODE);
                finish();
            }
        });

        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(getWindow().getAttributes());
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        getWindow().setAttributes(lp);
    }


}
