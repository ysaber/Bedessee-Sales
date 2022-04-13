package com.thebedesseegroup.sales.main;

import com.google.gson.annotations.SerializedName;

/**
 * Object model for the side menu to parse from json file
 */
public class SideMenu {

    @SerializedName("STATUS CODE")
    private String mStatusCode;

    @SerializedName("SIDE MENU DISPLAY")
    private String mMenuTitle;

    @SerializedName("MENU SORT")
    private String mSort;

    @SerializedName("COLOUR")
    private String mColour;

    @SerializedName("COUNT")
    private String mCount;


    public SideMenu(String colour, String count, String menuTitle, String sort, String statusCode) {
        mColour = colour;
        mCount = count;
        mMenuTitle = menuTitle;
        mSort = sort;
        mStatusCode = statusCode;
    }

    public String getColour() {
        return mColour;
    }

    public void setColour(String colour) {
        mColour = colour;
    }

    public String getCount() {
        return mCount;
    }

    public void setCount(String count) {
        mCount = count;
    }

    public String getMenuTitle() {
        return mMenuTitle;
    }

    public void setMenuTitle(String menuTitle) {
        mMenuTitle = menuTitle;
    }

    public String getSort() {
        return mSort;
    }

    public void setSort(String sort) {
        mSort = sort;
    }

    public String getStatusCode() {
        return mStatusCode;
    }

    public void setStatusCode(String statusCode) {
        mStatusCode = statusCode;
    }

    //    {"STATUS CODE":"SOON"  ,"SIDE MENU DISPLAY":"ARRIVING SOON  ","MENU SORT":"01","COLOUR":"#663300","COUNT":"2"},
//    {"STATUS CODE":"SPEC"  ,"SIDE MENU DISPLAY":"SPECIAL        ","MENU SORT":"02","COLOUR":"#669999","COUNT":" "},
//    {"STATUS CODE":"NEW"   ,"SIDE MENU DISPLAY":"NEW PRODUCT    ","MENU SORT":"03","COLOUR":"#0000FF","COUNT":" "},
//    {"STATUS CODE":"CLEAR" ,"SIDE MENU DISPLAY":"CLEAR OUT      ","MENU SORT":"04","COLOUR":"#FFFF00","COUNT":" "},
//    {"STATUS CODE":"$$$"   ,"SIDE MENU DISPLAY":"NEW LOWER PRICE","MENU SORT":"05","COLOUR":"#CC6600","COUNT":" "},
//    {"STATUS CODE":"LOWQ"  ,"SIDE MENU DISPLAY":"LOW STOCK ALERT","MENU SORT":"06","COLOUR":"#CC99FF","COUNT":" "},
//    {"STATUS CODE":"SOAP"  ,"SIDE MENU DISPLAY":"SOAP PRODUCTS  ","MENU SORT":"07","COLOUR":"#FF3399","COUNT":" "},
//    {"STATUS CODE":"REST"  ,"SIDE MENU DISPLAY":"RESTAURANT PROD","MENU SORT":"08","COLOUR":"#990000","COUNT":" "}





}
