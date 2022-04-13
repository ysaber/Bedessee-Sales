package com.thebedesseegroup.sales.customview;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewParent;
import android.widget.Button;
import android.widget.EditText;

import com.thebedesseegroup.sales.R;
import com.thebedesseegroup.sales.utilities.Utilities;

/**
 * Implementation of AlertDialog that allows the user to select a number from a number pad.
 *
 * The client must provide a OnNumberSelectedListener.
 */
public class NumberPad {

    public NumberPad(final Context context, final OnNumberSelectedListener onNumberSelectedListener, final boolean isQty) {

        @SuppressLint("InflateParams")
        final View view = LayoutInflater.from(context).inflate(R.layout.qtypad, null);

        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setCancelable(true)
                .setView(view);

        final AlertDialog alert = builder.create();

        final EditText edtQty = (EditText) view.findViewById(R.id.qty);

        view.findViewById(R.id.btnDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String displayedQty = edtQty.getText().toString();

                try {
                    final int result = displayedQty.isEmpty() ? 0 : Integer.parseInt(displayedQty);

                    onNumberSelectedListener.onSelected(result);

                    alert.dismiss();
                } catch (NumberFormatException e) {
                    Utilities.shortToast(context, "Invalid number. Max is " + Integer.MAX_VALUE);
                }


            }
        });

        View.OnClickListener btnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final String currQty = edtQty.getText().toString();
                String btnText = ((Button) v).getText().toString();

                if(v.getId() == R.id.btnDelete) {
                    if(edtQty.getText().length() > 0) {
                        edtQty.setText(currQty.substring(0, currQty.length() - 1));
                    }
                } else {
                    edtQty.setText(currQty + btnText);
                }
            }
        };

        view.findViewById(R.id.btn1).setOnClickListener(btnClickListener);
        view.findViewById(R.id.btn2).setOnClickListener(btnClickListener);
        view.findViewById(R.id.btn3).setOnClickListener(btnClickListener);
        view.findViewById(R.id.btn4).setOnClickListener(btnClickListener);
        view.findViewById(R.id.btn5).setOnClickListener(btnClickListener);
        view.findViewById(R.id.btn6).setOnClickListener(btnClickListener);
        view.findViewById(R.id.btn7).setOnClickListener(btnClickListener);
        view.findViewById(R.id.btn8).setOnClickListener(btnClickListener);
        view.findViewById(R.id.btn9).setOnClickListener(btnClickListener);
        view.findViewById(R.id.btn0).setOnClickListener(btnClickListener);
        view.findViewById(R.id.btnDelete).setOnClickListener(btnClickListener);


        alert.show();

        if(isQty) {
            view.findViewById(R.id.btnDone).setVisibility(View.GONE);
            view.findViewById(R.id.qty_row).setVisibility(View.VISIBLE);

            final Button buttonPiece = (Button) view.findViewById(R.id.btnPiece);

            buttonPiece.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String displayedQty = edtQty.getText().toString();

                    final int result = displayedQty.isEmpty() ? 0 : Integer.parseInt(displayedQty);

                    onNumberSelectedListener.onSelected(QtySelector.ItemType.PIECE, result);

                    alert.dismiss();
                }
            });

            final Button buttonCase = (Button) view.findViewById(R.id.btnCase);
            buttonCase.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    final String displayedQty = edtQty.getText().toString();

                    final int result = displayedQty.isEmpty() ? 0 : Integer.parseInt(displayedQty);

                    onNumberSelectedListener.onSelected(QtySelector.ItemType.CASE, result);

                    alert.dismiss();
                }
            });
        }

        forceWrapContent(view);
    }


    /**
     * Stolen from the internet.
     *
     * @param v View
     */
    private void forceWrapContent(View v) {
        // Start with the provided view
        View current = v;

        // Travel up the tree until fail, modifying the LayoutParams
        do {
            // Get the parent
            ViewParent parent = current.getParent();

            // Check if the parent exists
            if (parent != null) {
                // Get the view
                try {
                    current = (View) parent;
                } catch (ClassCastException e) {
                    // This will happen when at the top view, it cannot be cast to a View
                    break;
                }

                // Modify the layout
                current.getLayoutParams().width = ViewGroup.LayoutParams.WRAP_CONTENT;
            }
        } while (current.getParent() != null);


        // Request a layout to be re-done
        current.requestLayout();
    }


    public interface OnNumberSelectedListener {
        public void onSelected(int number);
        public void onSelected(QtySelector.ItemType itemType, int qty);
    }

}
