package com.thebedesseegroup.sales.customview;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.thebedesseegroup.sales.admin.AdminSettings;
import com.thebedesseegroup.sales.mixpanel.MixPanelManager;
import com.thebedesseegroup.sales.sharedprefs.SharedPrefsManager;
import com.thebedesseegroup.sales.update.UpdateActivity;
import com.thebedesseegroup.sales.utilities.Utilities;

/**
 * Utilities Spinner used in MainActivity.
 */
public class UtilitiesSpinner extends Spinner {

    public UtilitiesSpinner(Context context) {
        super(context);
        init(context);
    }

    public UtilitiesSpinner(Context context, int mode) {
        super(context, mode);
        init(context);
    }

    public UtilitiesSpinner(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public UtilitiesSpinner(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    public UtilitiesSpinner(Context context, AttributeSet attrs, int defStyle, int mode) {
        super(context, attrs, defStyle, mode);
        init(context);
    }


    /**
     * Initialize the utilities spinner.
     *
     * @param context Context
     */
    private void init(final Context context) {
        final String [] utilsStrings = {"Utilities", "Reg. Calc.", "Margin Calc.", "Google Hangouts", "GMail", "Load old update", "Install new app version", "Admin menu"};

        final ArrayAdapter<String> utilsAdapter = new ArrayAdapter<String>(context, android.R.layout.simple_spinner_dropdown_item, utilsStrings);
        setAdapter(utilsAdapter);
        setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                switch (position) {
                    case 0:
                        break;

                    case 1:
                        /** Launch reg calc */
                        Utilities.launchRegularCalculator(context);
                        break;

                    case 2:
                        /** Launch markup calc */
                        context.startActivity(new Intent(context, MarginCalculator.class));
                        break;

                    case 3:
                        /** Launch hangouts */
                        final Intent LaunchIntent = context.getPackageManager().getLaunchIntentForPackage("com.google.android.talk");
                        context.startActivity(LaunchIntent);
                        break;

                    case 4:
                        /** Launch GMail */
                        final Intent intent = context.getPackageManager().getLaunchIntentForPackage("com.google.android.gm");
                        context.startActivity(intent);
                        break;

                    case 5:
                        /** Load prev update */
                        showOldUpdateSelector();
                        break;

                    case 6:
                        /** Install new app version */
                        Utilities.installAp((Activity) context);
                        break;

                    case 7:
                        /** Admin menu */
                        final int secretPin = new SharedPrefsManager(context).getAdminPin();
                        final NumberPad.OnNumberSelectedListener onNumberSelectedListener = new NumberPad.OnNumberSelectedListener() {
                            @Override
                            public void onSelected(int number) {
                                if (number == secretPin) {
                                    MixPanelManager.trackButtonClick(context, "Button click: Admin settings");
                                    ((Activity)context).startActivity(new Intent(context, AdminSettings.class));
                                } else {
                                    Utilities.shortToast(context, "Sorry, wrong pin!");
                                }
                            }

                            @Override
                            public void onSelected(QtySelector.ItemType itemType, int qty) {

                            }
                        };

                        new NumberPad(context, onNumberSelectedListener, false);
                        break;

                    default:
                        break;
                }
                //reset spinner back to position 0
                parent.setSelection(0);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }


    private void showOldUpdateSelector() {

        final Context context = getContext();

        final AlertDialog alertDialog = new AlertDialog.Builder(context).create();
        alertDialog.setCancelable(true);

        final RadioGroup radioGroup = new RadioGroup(context);
        radioGroup.setPadding(16, 16, 16, 16);


        final TextView title = new TextView(context);
        title.setTextSize(18);
        title.setPadding(16, 16, 16, 16);
        title.setText("Select update to load");
        radioGroup.addView(title);

        final String [] days = { "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "R&D"};

        for (final String day : days) {
            final RadioButton btn = new RadioButton(context);
            btn.setTextSize(14);
            btn.setText(day);
            radioGroup.addView(btn);
        }

        alertDialog.setView(radioGroup);

        alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, "LOAD",
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int which) {

                        String updateDir = "";

                        final int radioButtonID = radioGroup.getCheckedRadioButtonId();
                        final View radioButton = radioGroup.findViewById(radioButtonID);
                        final int selectedPosition = radioGroup.indexOfChild(radioButton);

//                        final int selectedPosition = radioGroup.get

                        switch (selectedPosition) {

                            case 1: {
                                updateDir = "01-MON";
                            } break;

                            case 2: {
                                updateDir = "02-TUE";
                            } break;

                            case 3: {
                                updateDir = "03-WED";
                            } break;

                            case 4: {
                                updateDir = "04-THUR";
                            } break;

                            case 5: {
                                updateDir = "05-FRI";
                            } break;

                            case 6: {
                                updateDir = "R&D";
                            } break;

                        }

                        final Intent updateIntent = new Intent(context, UpdateActivity.class);
                        updateIntent.putExtra(UpdateActivity.KEY_UPDATE_DIR, updateDir);
                        context.startActivity(updateIntent);

                    }
                });
        alertDialog.setButton(AlertDialog.BUTTON_NEGATIVE, "CANCEL",
                new DialogInterface.OnClickListener() {
                    public void onClick(final DialogInterface dialog, final int which) {
                        alertDialog.dismiss();
                    }
                });
        alertDialog.show();

    }

}
