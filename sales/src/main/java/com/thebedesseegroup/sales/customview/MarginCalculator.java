package com.thebedesseegroup.sales.customview;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.thebedesseegroup.sales.R;
import com.thebedesseegroup.sales.utilities.Utilities;

import java.text.DecimalFormat;

/**
 * Markup Calculator Activity
 */
public class MarginCalculator extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.markup_calculator);

        final EditText costEdt = (EditText) findViewById(R.id.editText_costprice);
        final EditText sellEdt = (EditText) findViewById(R.id.editText_sellingprice);
        final EditText markupEdt = (EditText) findViewById(R.id.editText_markup);

        final TextView resultSell = (TextView) findViewById(R.id.resultSell);
        final TextView resultCost = (TextView) findViewById(R.id.resultCost);
        final TextView resultMarkup = (TextView) findViewById(R.id.resultMarkup);
        final TextView resultProfit = (TextView) findViewById(R.id.resultProfit);

        final DecimalFormat format = new DecimalFormat("0.00");


        View.OnClickListener qtyClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MarginOnNumberSelectedListener listener = new MarginOnNumberSelectedListener((EditText)v);
                new NumberPad(MarginCalculator.this, listener, false);
            }
        };

        costEdt.setOnClickListener(qtyClickListener);
        sellEdt.setOnClickListener(qtyClickListener);
        markupEdt.setOnClickListener(qtyClickListener);

        findViewById(R.id.btnCalculate).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String costStr = costEdt.getText().toString();
                final String sellStr = sellEdt.getText().toString();
                final String markupStr = markupEdt.getText().toString();


                if(!costStr.isEmpty() && Double.parseDouble(costStr) > 0
                        && !sellStr.isEmpty()  && Double.parseDouble(sellStr) > 0
                        && (markupStr.isEmpty() || Double.parseDouble(markupStr) == 0)) {
                    final double cost = Double.parseDouble(costStr);
                    final double sell = Double.parseDouble(sellStr);
                    final double markup = (1 - (cost / sell)) * 100;

                    resultCost.setText("Cost Price: $" + String.valueOf(format.format(cost)));
                    resultSell.setText("Sell Price: $" + String.valueOf(format.format(sell)));
                    resultMarkup.setText("Margin: " + String.valueOf(format.format(markup)) + "%");
                    resultProfit.setText("Profit: $" + String.valueOf(format.format(sell - cost)));

                } else if (!costStr.isEmpty() && Double.parseDouble(costStr) > 0
                        && (sellStr.isEmpty()  || Double.parseDouble(sellStr) == 0)
                        && !markupStr.isEmpty() && Double.parseDouble(markupStr) > 0) {
                    final double cost = Double.parseDouble(costStr);
                    final double markup = Double.parseDouble(markupStr);
                    final double sell = cost / (1 - (markup / 100));

                    resultCost.setText("Cost Price: $" + String.valueOf(format.format(cost)));
                    resultSell.setText("Sell Price: $" + String.valueOf(format.format(sell)));
                    resultMarkup.setText("Margin: " + String.valueOf(format.format(markup)) + "%");
                    resultProfit.setText("Profit: $" + String.valueOf(format.format(sell - cost)));

                } else if ((costStr.isEmpty() || Double.parseDouble(costStr) == 0)
                        && !sellStr.isEmpty()  && Double.parseDouble(sellStr) > 0
                        && !markupStr.isEmpty() && Double.parseDouble(markupStr) > 0) {
                    final double sell = Double.parseDouble(sellStr);
                    final double markup = Double.parseDouble(markupStr);
                    final double cost = sell * (1 - (markup / 100));

                    resultCost.setText("Cost Price: $" + String.valueOf(format.format(cost)));
                    resultSell.setText("Sell Price: $" + String.valueOf(format.format(sell)));
                    resultMarkup.setText("Margin: " + String.valueOf(format.format(markup)) + "%");
                    resultProfit.setText("Profit: $" + String.valueOf(format.format(sell - cost)));

                } else {
                    Toast.makeText(MarginCalculator.this, "Please enter 2 values. Not more, not less.", Toast.LENGTH_LONG).show();
                }

                resultProfit.requestFocus();

                getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
                Utilities.hideSoftKeyboard(MarginCalculator.this);
            }
        });

        findViewById(R.id.btnReset).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                costEdt.setText(null);
                sellEdt.setText(null);
                markupEdt.setText(null);
                resultCost.setText("Cost Price:");
                resultSell.setText("Sell Price:");
                resultMarkup.setText("Markup:");
                resultProfit.setText("Profit: ");
            }
        });
    }

    private class MarginOnNumberSelectedListener implements NumberPad.OnNumberSelectedListener {

        private EditText mEditText;

        private MarginOnNumberSelectedListener(EditText editText) {
            mEditText = editText;
        }

        @Override
        public void onSelected(int number) {
            final DecimalFormat decimalFormat = new DecimalFormat("#.00");
            if(mEditText.getId() != R.id.editText_markup) {
                mEditText.setText(String.valueOf(decimalFormat.format((double) number / 100)));
            } else {
                mEditText.setText(String.valueOf(decimalFormat.format((double) number)));
            }
        }

        @Override
        public void onSelected(QtySelector.ItemType itemType, int qty) {

        }
    }
}
