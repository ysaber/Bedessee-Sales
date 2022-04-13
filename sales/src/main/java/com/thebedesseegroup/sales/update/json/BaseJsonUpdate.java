package com.thebedesseegroup.sales.update.json;

import android.app.Activity;
import android.content.Context;
import android.os.AsyncTask;
import android.os.Message;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.stream.JsonReader;
import com.thebedesseegroup.sales.sharedprefs.SharedPrefsManager;
import com.thebedesseegroup.sales.update.UpdateActivity;
import com.thebedesseegroup.sales.utilities.Utilities;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.List;

/**
 * TODO: Document me...
 */
public abstract class BaseJsonUpdate extends AsyncTask<String, String, Void> {

    protected Context mContext;
    protected String result;
    protected long totalSize;
    protected OnDownloadJsonCompleteListener mListener;

    protected BaseJsonUpdate(Context context) {
        mContext = context;
    }

    public void setOnJsonDownloadCompleteListener(OnDownloadJsonCompleteListener listener) {
        mListener = listener;
    }

    public abstract String getFilename();

    @Override
    protected Void doInBackground(String... params) {

        try {

            String dirpath;
            String dirSubpath = "";

            if (params.length > 0) {

                final String customFilepath = params[0];

                if (!TextUtils.isEmpty(customFilepath)) {
                    dirSubpath = customFilepath + "/";

                }
            }

            final SharedPrefsManager sharedPrefs = new SharedPrefsManager(mContext);
            final String sugarSyncPath = sharedPrefs.getSugarSyncDir();

            dirpath = sugarSyncPath + "/data/" + dirSubpath;

            final String filepath = dirpath + getFilename();

            final File file = new File(filepath);

            final FileInputStream ois = new FileInputStream(file);
            final BufferedInputStream is = new BufferedInputStream(ois);

            int size = is.available();
            byte[] buffer = new byte[size];

            is.read(buffer);

            is.close();
            ois.close();

            result = new String(buffer, "UTF-8");


        } catch (FileNotFoundException e) {
            if(mContext instanceof Activity) {
                ((Activity) mContext).runOnUiThread(new Runnable() {
                    public void run() {
                        Utilities.longToast(mContext, getFilename() + " FILE NOT FOUND");
                        Utilities.longToast(mContext, "HAVE YOU SETUP SUGAR SYNC?");
                    }
                });
                ((Activity) mContext).finish();
            }
        } catch (UnsupportedEncodingException e) {

            ((Activity)mContext).runOnUiThread(new Runnable() {
                public void run() {
                    Utilities.longToast(mContext, "UNSUPPORTED ENCODING EXCEPTION ---CONTACT YUSUF");
                }
            });
            ((Activity)mContext).finish();
        } catch (IOException e) {

            ((Activity)mContext).runOnUiThread(new Runnable() {
                public void run() {
                    Utilities.longToast(mContext, "ERROR READING FILE " + getFilename() + " ---CONTACT HENRY OR YUSUF");
                }
            });
            ((Activity)mContext).finish();
        }
        return null;
    }


    public List<?> readJsonStream(final BufferedInputStream bufferedInputStream, final Gson gson) throws IOException {
        final JsonReader reader = new JsonReader(new InputStreamReader(bufferedInputStream, "UTF-8"));
        final List<Message> messages = new ArrayList<>();
        reader.beginArray();
        while (reader.hasNext()) {
            Message message = gson.fromJson(reader, Message.class);
            messages.add(message);
        }
        reader.endArray();
        reader.close();
        return messages;
    }


    @Override
    protected void onProgressUpdate(String... values) {
        UpdateActivity.setProgress(values[0] + "%");
    }



    public interface OnDownloadJsonCompleteListener {
        void onComplete();
    }

}
