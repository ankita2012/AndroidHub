package com.example.dailytracker;

import android.content.DialogInterface;
import android.os.Bundle;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.AppCompatActivity;

import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.dailytracker.ui.main.SectionsPagerAdapter;
import com.google.android.material.textfield.TextInputEditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        SectionsPagerAdapter sectionsPagerAdapter = new SectionsPagerAdapter(this, getSupportFragmentManager());
        ViewPager viewPager = findViewById(R.id.view_pager);
        viewPager.setAdapter(sectionsPagerAdapter);

        TabLayout tabs = findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPager);

        FloatingActionButton fab = findViewById(R.id.fab);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                final EditText taskEditText = new EditText(MainActivity.this);
                LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.MATCH_PARENT);
                taskEditText.setLayoutParams(lp);


                final MaterialAlertDialogBuilder alertbox=new MaterialAlertDialogBuilder(MainActivity.this);
               final View customAlertDialogView = LayoutInflater.from(MainActivity.this)
               .inflate(R.layout.customalertlayout, null, false);

               alertbox.setView(customAlertDialogView)
               .setTitle("Transaction Details")
                       .setPositiveButton("GOT IT", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {

                               TextInputEditText trans_remarks = (TextInputEditText) customAlertDialogView.findViewById(R.id.trans_remarks);
                               //TextInputEditText trans_type = (TextInputEditText) customAlertDialogView.findViewById(R.id.trans_type);
                               TextInputEditText trans_amt = (TextInputEditText) customAlertDialogView.findViewById(R.id.trans_amt);

                               String trans_remarks1 = trans_remarks.getText().toString();
                               //String trans_type1 = trans_type.getText().toString();
                               String trans_amt1 = trans_amt.getText().toString();

                               Toast.makeText(MainActivity.this, trans_amt1+" for "+trans_remarks1,Toast.LENGTH_SHORT).show();
                           }
                       })
                       .setNeutralButton("LATER", new DialogInterface.OnClickListener() {
                           @Override
                           public void onClick(DialogInterface dialogInterface, int i) {

                               dialogInterface.dismiss();
                           }
                       })
                       .show();

            }
        });

    }
}