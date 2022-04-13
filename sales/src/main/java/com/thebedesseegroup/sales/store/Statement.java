package com.thebedesseegroup.sales.store;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.TextView;

import com.thebedesseegroup.sales.R;
import com.thebedesseegroup.sales.order.GMailUtils;
import com.thebedesseegroup.sales.sharedprefs.SharedPrefsManager;
import com.thebedesseegroup.sales.utilities.Utilities;

import java.io.File;

/**
 * Activity containing WebView to show Store's statement.
 */
public class Statement extends Activity {

    public static final int REQUEST_CODE = 88;

    public enum DocType {

        STATEMENT("/custstmt/"),
        R336("/r336/"),
        RECEIPT("/cust_sml/");

        private String mDir;

        DocType(String dir) {
            mDir = dir;
        }

        public String getDir() {
            return mDir;
        }
    }

    final public static String KEY_STATEMENT_URL = "statement_url";
    final public static String KEY_DOC_TYPE = "doc_type";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.layout_webview);

        String ua = "Mozilla/5.0 (X11; U; Linux i686; en-US; rv:1.9.0.4) Gecko/20100101 Firefox/4.0";

        final WebView webView = (WebView) findViewById(R.id.webview);
        webView.getSettings().setBuiltInZoomControls(true);
        webView.getSettings().setSupportZoom(true);
        webView.getSettings().setLoadWithOverviewMode(true);
        webView.getSettings().setUseWideViewPort(true);
        webView.setWebViewClient(new WebViewClient());
        webView.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
        webView.getSettings().setUserAgentString(ua);

        final String url = getIntent().getExtras().getString(KEY_STATEMENT_URL);

        if (url == null) {
            Utilities.shortToast(Statement.this, "Document not found");
            setResult(REQUEST_CODE);
            finish();
        } else {

            final String[] urlParts = url.split("/");
            final String fileName = urlParts[urlParts.length - 1];

            final DocType docType = (DocType) getIntent().getExtras().get(KEY_DOC_TYPE);

            final String sugarSyncDir = new SharedPrefsManager(Statement.this).getSugarSyncDir();


            final String urlToLoad;

            switch (docType) {
                case RECEIPT: {
                    ((TextView)findViewById(R.id.btn_email_statement)).setText("EMAIL THIS RECEIPT");
                    if (new File(sugarSyncDir + docType.getDir() + fileName).exists()) {
                        urlToLoad = "file:///" + sugarSyncDir + docType.getDir() + fileName;
                    } else {
                        urlToLoad = null;
                    }
                } break;

                case STATEMENT: {
                    ((TextView)findViewById(R.id.btn_email_statement)).setText("EMAIL THIS STATEMENT");
                    if (new File(sugarSyncDir + docType.getDir() + fileName).exists()) {
                        urlToLoad = "file:///" + sugarSyncDir + docType.getDir() + fileName;
                    } else {
                        urlToLoad = null;
                    }
                }
                break;

                case R336:
                    ((TextView)findViewById(R.id.btn_email_statement)).setText("EMAIL THIS REPORT");
                    if (new File(sugarSyncDir + docType.getDir() + "SLD-" + fileName).exists()) {
                        urlToLoad = "file:///" + sugarSyncDir + docType.getDir() + "SLD-" + fileName;
                    } else {
                        urlToLoad = null;
                    }
                    break;

                default: {
                    urlToLoad = null;
                }
            }

            if (urlToLoad != null) {
                webView.loadUrl(urlToLoad);

                findViewById(R.id.btn_email_statement).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        GMailUtils.sendAttachment(Statement.this, "STATEMENT", urlToLoad);
                    }
                });
            } else {
                Utilities.shortToast(Statement.this, "Document not found");
                setResult(REQUEST_CODE);
                finish();
            }


            WebViewClient client = new WebViewClient() {
                @Override
                public void onPageFinished(WebView v, String url) {
                    findViewById(R.id.progressBar).setVisibility(View.GONE);
                    webView.zoomIn();
                }
            };
            webView.setWebViewClient(client);


            findViewById(R.id.imageView_close).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    finish();
                }
            });

            int[] dimens = Utilities.getScreenDimensInPx(null);

            getWindow().setLayout((int) (dimens[0] * .95), (int) (dimens[1] * .95));
        }
    }


    public static void show(final Activity activity, final DocType docType, final String url) {
        final Intent intent = new Intent(activity, Statement.class);
        intent.putExtra(KEY_DOC_TYPE, docType);
        intent.putExtra(KEY_STATEMENT_URL, url);
        activity.startActivityForResult(intent, REQUEST_CODE);
    }

}
