package com.example.dailytracker.recyclerView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Debug;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.dailytracker.R;
import com.example.dailytracker.recyclerView.SQLite.DatabaseHandler;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.textfield.TextInputEditText;
import java.util.ArrayList;


public class RecyclerMain extends AppCompatActivity {

    private DatabaseHandler dbHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recycler_main);

        dbHandler = new DatabaseHandler(RecyclerMain.this,"",
                null,1);
        // Log.d("debug", String.valueOf(dbHandler.checkForTableExists()));
        ArrayList<MyListData> userList = (ArrayList<MyListData>) dbHandler.getAllTXNList();
        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        if(userList.size()>0)
        {
            recyclerView.setVisibility(View.VISIBLE);
            MyListAdapter adapter  = new MyListAdapter(userList, RecyclerMain.this);
            recyclerView.setAdapter(adapter);
        }
        else {
            recyclerView.setVisibility(View.GONE);
            Toast.makeText(this, "There is no txn in the database. Start adding now", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_recycler, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.add:
                callAddMenuItem();
                break;
            default:
                break;
        }
        return true;
    }


    private void callAddMenuItem()
    {
        final EditText taskEditText = new EditText(RecyclerMain.this);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.MATCH_PARENT);
        taskEditText.setLayoutParams(lp);


        final MaterialAlertDialogBuilder alertbox=new MaterialAlertDialogBuilder(RecyclerMain.this);
        final View customAlertDialogView = LayoutInflater.from(RecyclerMain.this)
                .inflate(R.layout.customalertlayout, null, false);

        alertbox.setView(customAlertDialogView)
                .setTitle("Transaction Details")
                .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        TextInputEditText trans_remarks = (TextInputEditText) customAlertDialogView.findViewById(R.id.trans_remarks);
                        TextInputEditText trans_amt = (TextInputEditText) customAlertDialogView.findViewById(R.id.trans_amt);

                       // MaterialRadioButton radiobutton_credit= (MaterialRadioButton) customAlertDialogView.findViewById(R.id.credit);
                        // MaterialRadioButton radiobutton_debit= (MaterialRadioButton) customAlertDialogView.findViewById(R.id.debit);
                        RadioGroup radio_transtype=(RadioGroup) customAlertDialogView.findViewById(R.id.radioGroup);

                        String trans_remarks1 = trans_remarks.getText().toString();
                        String trans_amt1 = trans_amt.getText().toString();

                        RadioButton rb = (RadioButton) radio_transtype.findViewById(radio_transtype.getCheckedRadioButtonId());

                        if (TextUtils.isEmpty(trans_remarks1)) {
                            Toast.makeText(RecyclerMain.this, "Please enter remarks", Toast.LENGTH_LONG).show();
                        }
                        else
                        {
                            Toast.makeText(RecyclerMain.this, "Transaction type:"+rb.getText()+" | "+trans_amt1+" for "+trans_remarks1, Toast.LENGTH_LONG).show();
                            //DATABASE HANDLING PART
                            Log.e("error","Transaction type:"+rb.getText()+" | "+trans_amt1+" for "+trans_remarks1);
                            MyListData txnPOJO=new MyListData((String) rb.getText(),trans_amt1, trans_remarks1);

                            DatabaseHandler dbHandler = new DatabaseHandler(RecyclerMain.this,"", null,1);
                            dbHandler.addTxn(txnPOJO);
                            finish();
                            startActivity(getIntent());
                        }

                    }
                })
                .setNeutralButton("LATER", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        Toast.makeText(RecyclerMain.this, "Task cancelled", Toast.LENGTH_SHORT).show();
                        dialogInterface.dismiss();
                    }
                }).show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (dbHandler != null) {
            dbHandler.close();
        }
    }

}