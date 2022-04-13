package com.thebedesseegroup.sales.main;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckedTextView;

import java.util.ArrayList;

public class MainMenuAdapter extends ArrayAdapter<SideMenu> {

    private ArrayList<SideMenu> mMenuItems;

    public MainMenuAdapter(Context context, int resource, ArrayList<SideMenu> menuItems) {
        super(context, resource, menuItems);
        mMenuItems = menuItems;
     }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        CheckedTextView v = (CheckedTextView) LayoutInflater.from(getContext()).inflate(android.R.layout.simple_spinner_dropdown_item, parent, false);

        final SideMenu sideMenu = mMenuItems.get(position);

        String text = sideMenu.getMenuTitle();

        v.setText(text);

        try {
            v.setTextColor(Color.parseColor(sideMenu.getColour()));
        } catch (IllegalArgumentException e) {
            v.setTextColor(Color.BLACK);
        }

//        if(text.equals(Status.LOW.getDescription())) {
//            v.setTextColor(Color.RED);
//        }
//
//        if(text.equals(Status.LOWQ.getDescription())) {
//            v.setTextColor(Color.BLUE);
//        }
//
//        if(text.equals(Status.NEW.getDescription())) {
//            v.setTextColor(Color.parseColor("#336600"));
//        }
//
//        if(text.equals(Status.SOAP.getDescription())) {
//            v.setTextColor(Color.parseColor("#CC6600"));
//        }
//
//        if(text.equals(Status.REST.getDescription())) {
//            v.setTextColor(Color.MAGENTA);
//        }
//
//        if(text.equals(Status.SOON.getDescription())) {
//            v.setTextColor(Color.parseColor("#401616"));
//        }
//
//        if(text.equals(Status.SPEC.getDescription())) {
//            v.setTextColor(Color.parseColor("#660066"));
//        }
//        if(text.equals(Status.LAST.getDescription())) {
//            v.setTextColor(Color.parseColor("#CCCC00"));
//        }

        return v;
    }

    @Override
    public int getCount() {
        return mMenuItems.size();
    }

//    @Override
//    public int getViewTypeCount() {
//        return 2;
//    }

}
