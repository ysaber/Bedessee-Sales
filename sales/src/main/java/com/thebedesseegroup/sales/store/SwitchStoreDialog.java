package com.thebedesseegroup.sales.store;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;

/**
 * TODO: Document me...
 */
public class SwitchStoreDialog {

    public static AlertDialog build(final Context context, final DialogInterface.OnClickListener onClickListener) {

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();

        alertDialog.setTitle("Changing stores will remove all items from your shopping cart.");
        alertDialog.setMessage("Are you sure you want to continue?");

        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "Yes", onClickListener);

        alertDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                alertDialog.dismiss();
            }
        });

        return alertDialog;
    }

}
