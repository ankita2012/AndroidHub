package com.example.dailytracker.recyclerView;

import android.widget.TextView;

import java.util.ArrayList;

public class MyListData extends ListItem {

    private String description;
    private int imgId;

    int id;
    String amount;
    String trans_type;
    String remarks;
    String date;

    public MyListData(String description, int imgId) {
        this.description = description;
        this.imgId = imgId;
    }

    public MyListData(int id,String amount, String trans_type, String remarks, String date){
        this.id = id;
        this.amount=amount;
        this.trans_type = trans_type;
        this.remarks = remarks;
        this.date = date;
    }

    public MyListData(int id,String amount, String trans_type, String remarks){
        this.id = id;
        this.amount=amount;
        this.trans_type = trans_type;
        this.remarks = remarks;
    }

    public MyListData(String trans_type, String amount, String remarks, String date){
        this.amount=amount;
        this.trans_type = trans_type;
        this.remarks = remarks;
        this.date = date;
    }

    public MyListData(String trans_type, String amount, String remarks){
        this.amount=amount;
        this.trans_type = trans_type;
        this.remarks = remarks;
    }

    public MyListData() {
    }

    public String getDescription() {
        return description;
    }
    public void setDescription(String description) {
        this.description = description;
    }

    public int getImgId() {
        return imgId;
    }
    public void setImgId(int imgId) {
        this.imgId = imgId;
    }

    public int getID(){
        return this.id;
    }

    public void setID(int id){
        this.id = id;
    }

    public String getAmount(){
        return this.amount;
    }

    public void setAmount(String amount){
        this.amount = amount;
    }

    public String getTrans_type(){
        return this.trans_type;
    }

    public void setTrans_type(String trans_type){
        this.trans_type = trans_type;
    }

    public String getRemarks(){
        return this.remarks;
    }

    public void setRemarks(String remarks){
        this.remarks = remarks;
    }

    public String getDate(){
        return this.date;
    }

    public void setDate(String date){
        this.date = date;
    }

    @Override
    public int getType() {
        return TYPE_GENERAL;
    }
}
