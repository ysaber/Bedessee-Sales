package com.thebedesseegroup.sales.update;

import android.app.Activity;
import android.app.DownloadManager;
import android.content.Context;
import android.os.Bundle;
import android.widget.TextView;

import com.thebedesseegroup.sales.R;
import com.thebedesseegroup.sales.mixpanel.MixPanelManager;
import com.thebedesseegroup.sales.update.json.BaseJsonUpdate;
import com.thebedesseegroup.sales.update.json.UpdateAppInfo;
import com.thebedesseegroup.sales.update.json.UpdateBrands;
import com.thebedesseegroup.sales.update.json.UpdateCategories;
import com.thebedesseegroup.sales.update.json.UpdateCustSpecPrice;
import com.thebedesseegroup.sales.update.json.UpdateProducts;
import com.thebedesseegroup.sales.update.json.UpdateSalesmenStores;
import com.thebedesseegroup.sales.update.json.UpdateSideMenu;
import com.thebedesseegroup.sales.update.json.UpdateUsers;

import java.util.ArrayList;

/**
 * Activity used for updating all data. In order for this to work, an
 * {@link com.thebedesseegroup.sales.update.UpdateActivity.UpdateType}
 * must be passed in via {@link android.content.Intent#putExtra(String, java.io.Serializable)} using
 * {@link com.thebedesseegroup.sales.update.UpdateActivity#sUpdateTypeKey} as the key.
 */
public class UpdateActivity extends Activity {

    final public static int REQUEST_CODE = 1;
    final public static String KEY_UPDATE_DIR = "key_update_dir";
    private String mDir = "";

    private int mCurrentUpdateCount = 0;
    private int mTotalUpdateCount = 0;

    /**
     * Enum to determine what to update. This is passed in from MainActivity when starting
     * this Activity.
     */
    public enum UpdateType {
        PRODUCT_INFO("Product info"),
        SALESMEN_STORES("Stores list"),
        BRANDS("Brands info"),
        APP_INFO("App info"),
        USERS("Users"),
        SIDE_MENU("Side Menu"),
        CATEGORY("Category info"),
        CUST_SPEC_PRICE_LIST("Customer Specific Price List");

        private String mDescription;

        UpdateType(String description) {
            mDescription = description;
        }

        public String getDescription() {
            return mDescription;
        }
    }

    /**
     * Update queue.
     */
    private ArrayList<UpdateType> mUpdateTypesList;

    /**
     * Key used for passing in an {@link com.thebedesseegroup.sales.update.UpdateActivity.UpdateType}
     */
    public static String sUpdateTypeKey = "update_type_key";

    /**
     * Title.
     */
    private static TextView mTitle;

    /**
     * TextView for displaying text during an update.
     */
    private static TextView mTextView;

    /**
     * DownloadManager.
     */
    public static DownloadManager sDownloadManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        initViews();


        new Thread(new Runnable() {
            @Override
            public void run() {
                MixPanelManager.trackScreenView(UpdateActivity.this, "Daily Update screen");

                final Bundle extras = getIntent().getExtras();

                if (!extras.isEmpty()) {

                    if (extras.containsKey(sUpdateTypeKey)) {
                        //noinspection unchecked
                        mUpdateTypesList = (ArrayList<UpdateType>) extras.get(sUpdateTypeKey);
                    } else {
                        mUpdateTypesList = new ArrayList<>();
                        mUpdateTypesList.add(UpdateActivity.UpdateType.APP_INFO);
                        mUpdateTypesList.add(UpdateType.USERS);
                        mUpdateTypesList.add(UpdateType.CUST_SPEC_PRICE_LIST);
                        mUpdateTypesList.add(UpdateType.SIDE_MENU);
                        mUpdateTypesList.add(UpdateActivity.UpdateType.BRANDS);
                        mUpdateTypesList.add(UpdateActivity.UpdateType.CATEGORY);
                        mUpdateTypesList.add(UpdateActivity.UpdateType.SALESMEN_STORES);
                        mUpdateTypesList.add(UpdateActivity.UpdateType.PRODUCT_INFO);
                    }

                    if (extras.containsKey(KEY_UPDATE_DIR)) {
                        mDir = extras.getString(KEY_UPDATE_DIR);
                    }

                }

                sDownloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);

                mTotalUpdateCount = mUpdateTypesList.size();

                downloadQueuedUpdate(mUpdateTypesList.get(0));
            }
        }).run();


    }


    private void downloadQueuedUpdate(final UpdateType updateType) {

        mCurrentUpdateCount++;

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                mTitle.setText("Updating: " + updateType.getDescription() + " (" + mCurrentUpdateCount + "/" + mTotalUpdateCount + ")");
            }
        });

        switch (updateType) {

            case PRODUCT_INFO:
                /**Download product info */
                final UpdateProducts updateProducts = new UpdateProducts(UpdateActivity.this);
                updateProducts.setOnJsonDownloadCompleteListener(new BaseJsonUpdate.OnDownloadJsonCompleteListener() {
                    @Override
                    public void onComplete() {
                        setResult(1);
                        allClear();
                    }
                });
                updateProducts.execute(mDir);
                break;

            case SALESMEN_STORES:
                final UpdateSalesmenStores updateSalesmenStores = new UpdateSalesmenStores(this);
                updateSalesmenStores.setOnJsonDownloadCompleteListener(new BaseJsonUpdate.OnDownloadJsonCompleteListener() {
                    @Override
                    public void onComplete() {
                        setResult(100);
                        allClear();
                    }
                });
                updateSalesmenStores.execute(mDir);
                break;

            case BRANDS:
                final UpdateBrands updateBrands = new UpdateBrands(this);
                updateBrands.setOnJsonDownloadCompleteListener(new BaseJsonUpdate.OnDownloadJsonCompleteListener() {
                    @Override
                    public void onComplete() {
                        setResult(1);
                        allClear();
                    }
                });
                updateBrands.execute(mDir);
                break;

            case USERS:
                final UpdateUsers updateUsers = new UpdateUsers(this);
                updateUsers.setOnUpdateUsersCompleteListener(new UpdateUsers.OnUpdateUsersCompleteListener() {
                    @Override
                    public void onComplete() {

                        setResult(1);
                        allClear();
                    }

                    @Override
                    public void onError() {

                    }
                });
                updateUsers.execute(mDir);
                break;

            case CATEGORY:
                final UpdateCategories updateCategories = new UpdateCategories(this);
                updateCategories.setOnJsonDownloadCompleteListener(new BaseJsonUpdate.OnDownloadJsonCompleteListener() {
                    @Override
                    public void onComplete() {
                        setResult(1);
                        allClear();
                    }
                });
                updateCategories.execute(mDir);
                break;

            case APP_INFO:
                final UpdateAppInfo updateAppInfo = new UpdateAppInfo(this);
                updateAppInfo.setOnJsonDownloadCompleteListener(new BaseJsonUpdate.OnDownloadJsonCompleteListener() {
                    @Override
                    public void onComplete() {
                        setResult(1);
                        allClear();
                    }
                });
                updateAppInfo.execute(mDir);
                break;

            case SIDE_MENU:
                final UpdateSideMenu updateSideMenu = new UpdateSideMenu(this);
                updateSideMenu.setOnJsonDownloadCompleteListener(new BaseJsonUpdate.OnDownloadJsonCompleteListener() {
                    @Override
                    public void onComplete() {
                        setResult(1);
                        allClear();
                    }
                });
                updateSideMenu.execute(mDir);
                break;

            case CUST_SPEC_PRICE_LIST:
                final UpdateCustSpecPrice updateCustSpecPrice = new UpdateCustSpecPrice(this);
                updateCustSpecPrice.setOnJsonDownloadCompleteListener(new BaseJsonUpdate.OnDownloadJsonCompleteListener() {
                    @Override
                    public void onComplete() {
                        setResult(1);
                        allClear();
                    }
                });
                updateCustSpecPrice.execute(mDir);
                break;

            default:
                break;
        }
    }


    public void allClear() {

        /** Remove the 0th element, which is the just-downloaded update */
        if(!mUpdateTypesList.isEmpty()) {
            mUpdateTypesList.remove(0);
        }

        if(!mUpdateTypesList.isEmpty()) {
            downloadQueuedUpdate(mUpdateTypesList.get(0));
        } else {
            finish();
        }
    }


    private void initViews() {
        mTitle = (TextView) findViewById(R.id.title);
        mTextView = (TextView) findViewById(R.id.textViewUpdating);
    }


    /**
     * Override back functionality to do nothing.
     */
    @Override
    public void onBackPressed() { }


    /**
     * Sets progress.
     *
     * @param text Text to set
     */
    public static void setProgress(String text) {
        mTextView.setText(text);
    }

}