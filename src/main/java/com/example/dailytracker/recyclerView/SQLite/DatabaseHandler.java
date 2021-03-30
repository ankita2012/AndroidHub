package com.example.dailytracker.recyclerView.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;
import com.example.dailytracker.recyclerView.MyListData;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Random;
import java.util.UUID;

public class DatabaseHandler extends SQLiteOpenHelper {

    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "Tracker";
    private static final String TABLE_TXN = "TXN_DETAILS";
    private static final String KEY_ID = "id";
    private static final String KEY_AMOUNT = "amount";
    private static final String KEY_TRANS_TYPE = "trans_type";
    private static final String KEY_REMARKS = "remarks";
    private static final String KEY_DATE="txn_date";

    public DatabaseHandler(@Nullable Context context, @Nullable String name,
                           @Nullable SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_TXN_TABLE = "CREATE TABLE " + TABLE_TXN + "("
                + KEY_ID + " INTEGER PRIMARY KEY,"
                + KEY_AMOUNT + " TEXT,"
                + KEY_TRANS_TYPE + " TEXT,"
                + KEY_REMARKS + " TEXT, "
                + KEY_DATE + " TEXT" + ")";
        db.execSQL(CREATE_TXN_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_TXN);
        // Create tables again
        onCreate(db);
    }

    public boolean checkForTableExists(){
        String sql = "SELECT name FROM sqlite_master WHERE type='table' AND name='"+TABLE_TXN+"'";
        SQLiteDatabase db = this.getWritableDatabase();
        Cursor mCursor = db.rawQuery(sql, null);
        if (mCursor.getCount() > 0) {
            return true;
        }
        mCursor.close();
        return false;
    }

    // code to add the new RECORD
    public void addTxn(MyListData txnRecords) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        int id=new Random().nextInt(61) + 20;
        values.put(KEY_ID, id); //id generation service
        values.put(KEY_TRANS_TYPE, txnRecords.getTrans_type());
        values.put(KEY_AMOUNT, txnRecords.getAmount());
        values.put(KEY_REMARKS, txnRecords.getRemarks());
        values.put(KEY_DATE, getDateTime());
        Log.i("DB","Records:"+id+" || "+txnRecords.getTrans_type()+" || "
        +txnRecords.getAmount()+" || "+txnRecords.getRemarks()+ " || " +getDateTime());
        // Inserting Row
        long status=db.insert(TABLE_TXN, null, values);
        Log.i("DB","Insert update::"+status);
        //2nd argument is String containing nullColumnHack
        db.close(); // Closing database connection
    }

    // code to get the single row
    MyListData getTxn(int id) {
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = db.query(TABLE_TXN, new String[] { KEY_ID,
                        KEY_AMOUNT, KEY_TRANS_TYPE, KEY_REMARKS, KEY_DATE }, KEY_ID + "=?",
                new String[]
                        { String.valueOf(id) },
                null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        MyListData txn = new MyListData(cursor.getString(2), cursor.getString(3),
                cursor.getString(4));
        return txn;
    }


    // code to get all txns in a list view
    public ArrayList<MyListData> getAllTXNList() {
        ArrayList<MyListData> txnList = new ArrayList<MyListData>();
        // Select All Query
        String selectQuery = "SELECT  "+ KEY_ID+", " + KEY_TRANS_TYPE+", " +KEY_AMOUNT+", "
                +KEY_REMARKS+ ", "+KEY_DATE+
                " FROM " + TABLE_TXN;
        Log.i("DB SELECT",selectQuery);
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                Log.i("info","inside cursor");
                final MyListData contact = new MyListData();
                //contact.setID(Integer.parseInt(String.valueOf(cursor.getColumnIndex(KEY_ID))));

                contact.setID(Integer.parseInt(cursor.getString(0)));
                contact.setTrans_type(String.valueOf(cursor.getString(1)));
                contact.setAmount(String.valueOf(cursor.getString(2)));
                contact.setRemarks(String.valueOf(cursor.getString(3)));
                contact.setDate(String.valueOf(cursor.getString(4)));
                Log.i("DB","inside cursor:::" +cursor.getColumnIndex(KEY_TRANS_TYPE)+
                        " || "+cursor.getColumnIndex(KEY_AMOUNT)+" || "
                        +cursor.getColumnIndex(KEY_REMARKS)+ " || "+cursor.getColumnIndex(KEY_ID)+ " || " +
                        cursor.getColumnIndex(KEY_DATE));
                // Adding contact to list
                txnList.add(contact);
                //Collections.sort(txnList, Collections.reverseOrder());
                Collections.sort(txnList, new Comparator<MyListData>() {
                    @Override
                    public int compare(MyListData o1, MyListData o2) {
                        return o1.getDate().compareTo(o2.getDate());
                    }

                });
                Log.i("DB","txnList size inside getAllList:"+txnList.size());
            }
            while (cursor.moveToNext());

            Log.i("DB","txnList:"+txnList);
        }
        cursor.close();
        // return txn list
        return txnList;
    }



    // Get User Details
    public ArrayList<HashMap<String, String>> GetTxn(){
        SQLiteDatabase db = this.getWritableDatabase();
        ArrayList<HashMap<String, String>> userList = new ArrayList<>();
        String query = "SELECT trans_type, amount, remarks,txn_date  FROM "+ TABLE_TXN;
        Cursor cursor = db.rawQuery(query,null);
        while (cursor.moveToNext()){
            HashMap<String,String> user = new HashMap<>();
            user.put("trans_type",cursor.getString(cursor.getColumnIndex(KEY_TRANS_TYPE)));
            user.put("amount",cursor.getString(cursor.getColumnIndex(KEY_AMOUNT)));
            user.put("remarks",cursor.getString(cursor.getColumnIndex(KEY_REMARKS)));
            user.put("txn_date",cursor.getString(cursor.getColumnIndex(KEY_DATE)));
            userList.add(user);
        }
        return  userList;
    }


    // code to update the single txn
    public int update(MyListData txn) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(KEY_AMOUNT, txn.getAmount());
        values.put(KEY_TRANS_TYPE, txn.getTrans_type());
        values.put(KEY_REMARKS, txn.getRemarks());
        values.put(KEY_DATE, getDateTime());
        Log.i("DB","Inside DBHelper | id: "+txn.getID());
        // updating row
        return db.update(TABLE_TXN, values, KEY_ID + " = ?",
                new String[] { String.valueOf(txn.getID()) });
    }

    // Deleting single TXN
    public int deleteContact(MyListData txn) {
        SQLiteDatabase db = this.getWritableDatabase();
        int status =db.delete(TABLE_TXN, KEY_ID + " = ?",
                new String[] { String.valueOf(txn.getID()) });
        Log.i("DB","delete done:"+status+" || keyid:"+String.valueOf(txn.getID()));
        //db.close();
        return status;
    }

    // Getting txn Count
    public int getTxnCount() {
        String countQuery = "SELECT  * FROM " + TABLE_TXN;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();
        return cursor.getCount();
    }


    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
        Date date = new Date();
        return dateFormat.format(date);
    }

}
