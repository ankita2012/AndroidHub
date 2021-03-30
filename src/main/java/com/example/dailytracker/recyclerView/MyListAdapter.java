package com.example.dailytracker.recyclerView;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Build;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.recyclerview.widget.RecyclerView;
import com.example.dailytracker.R;
import com.example.dailytracker.recyclerView.SQLite.DatabaseHandler;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;
import java.util.Objects;


public class MyListAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> implements Filterable  {
    @NonNull
    //private MyListData[] listdata;
    ArrayList<MyListData> list;
    ArrayList<MyListData> list2;
    private Context context;
    private DatabaseHandler mDatabase;

    public MyListAdapter(ArrayList<MyListData> list, Context cont){
        this.list = list;
        this.list2=list;
        this.context = cont;
        mDatabase = new DatabaseHandler(context,"", null,1);
    }


    @Override
    public int getItemViewType(int position) {
        //Log.i("info","list.get(position).getType():"+list.get(position).getType());
        Log.i("info","return value"+position % 2 * 2);
        //return list.get(position).getType();
        return position % 2 * 2;
    }


    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder=null;
        switch (viewType)
        {
            //case ListItem.TYPE_GENERAL:
            case 0:
                LayoutInflater layoutInflater = LayoutInflater.from(parent.getContext());
            View listItemView= layoutInflater.inflate(R.layout.list_item, parent, false);
                viewHolder = new ViewHolder0(listItemView);
            break;

            case 2:
                    //ListItem.TYPE_DATE:
                View headerView = LayoutInflater.from(parent.getContext()).inflate(R.layout.header_layout,
                    parent, false);
                viewHolder=new ViewHolder2(headerView);
            break;
        }
        return viewHolder;
    }



    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        switch (holder.getItemViewType())
        {
            case 0:
                    //ListItem.TYPE_GENERAL:
                final MyListData myListData=list.get(position);
            Log.i("info","Arraylist:"+myListData+"|"+position);
            Log.i("info","type:"+myListData.getTrans_type()+"|"+myListData.getID()+"|"
                    +myListData.getDate());
            final int id;
            TextView transType= ((ViewHolder0) holder).transType;
            TextView transAmount=((ViewHolder0) holder).transAmount;
            TextView transRemarks=((ViewHolder0) holder).transRemarks;
            TextView transID=((ViewHolder0) holder).transID;
            TextView date=((ViewHolder0) holder).transDate;
            transType.setText(myListData.getTrans_type());
            transAmount.setText(myListData.getAmount());
            transRemarks.setText(myListData.getRemarks());
            date.setText(myListData.getDate());

            Log.i("info","Inside ADAPTER CLASS::"+myListData.getID()+" || "
                    +myListData.getTrans_type()+" || "
                    +myListData.getAmount()+" || "+myListData.getRemarks()+"|"+myListData.getDate());

            ImageView editTrans=((ViewHolder0) holder).editTrans;
            ImageView deleteTrans=((ViewHolder0) holder).deleteTrans;

            editTrans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.e("info","Inside editTrans");
                    editTaskDialog(myListData);
                }
            });

            deleteTrans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("error","Inside deleteTrans");
                    int status=mDatabase.deleteContact(new MyListData(myListData.getID(),
                            myListData.getTrans_type(), myListData.getAmount(), myListData.getRemarks()));
                    if(status>0)
                    {
                        Log.i("info","DELETE DONE!!");
                        Toast.makeText(context, "DELETE DONE!!", Toast.LENGTH_LONG).show();
                    }
                    else
                    {
                        Log.e("error","DELETE NOT DONE!!");
                    }
                    ((Activity) context).finish();
                    context.startActivity(((Activity) context).getIntent());
                }
            });
            break;
            case 2:
                    //ListItem.TYPE_DATE:
                Log.i("info","inside type date");
                final MyListData myListData1=list.get(position);
                //MyListData dateItem=list.get(position);
                HeaderPOJO header=new HeaderPOJO();
                header.setHeader(myListData1.getDate());
                TextView date_display=((ViewHolder2) holder).date_display;
                DateFormat originalFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss", Locale.ENGLISH);
                DateFormat targetFormat = new SimpleDateFormat("yyyy-MM-dd");
                String formattedDate="";
                try {
                    Date date1 = originalFormat.parse(header.getHeader());
                    formattedDate= targetFormat.format(date1);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                LocalDate currentDate = LocalDate.parse(formattedDate);
                Month month = currentDate.getMonth();
                Log.i("info","Month:"+month);
                date_display.setText(String.valueOf(month));
            break;
        }
    }


    private void editTaskDialog(final MyListData contacts) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View subView = inflater.inflate(R.layout.edit_records, null);

        Log.i("info","Inside editTaskDialog");
        final EditText transType= subView.findViewById(R.id.editType);
        final EditText transAmount = subView.findViewById(R.id.editAmount);
        final EditText transRemarks = subView.findViewById(R.id.editRemarks);
        final TextView transID= subView.findViewById(R.id.displayID);

        if (contacts != null) {
            transType.setText(contacts.getTrans_type());
            transAmount.setText(contacts.getAmount());
            transRemarks.setText(contacts.getRemarks());
            transID.setText(String.valueOf(contacts.getID()));
        }

        Log.i("info","Inside editTaskDialog111: "+contacts.getTrans_type()+
                contacts.getAmount()+ contacts.getRemarks()+ contacts.getID());
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle("Edit contact");
        builder.setView(subView);
        builder.create();
        builder.setPositiveButton("EDIT CONTACT", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String type = transType.getText().toString();
                final String amount = transAmount.getText().toString();
                final String remarks = transRemarks.getText().toString();
                final int id= Integer.parseInt(transID.getText().toString());
                Log.i("info","Inside editTaskDialog setPositive: "
                        +type+"|"+amount+"|"+remarks+"|"+id);
                if (TextUtils.isEmpty(type)) {
                    Toast.makeText(context, "Type Cannot be null.", Toast.LENGTH_LONG).show();
                }
                else {
                    MyListData updateList = new MyListData(Objects.requireNonNull(contacts).getID(),
                            type, amount, remarks);
                    int update1 = mDatabase.update(updateList);
                    if(update1>0)
                    {
                        Log.i("info","UPDATE DONE!!");
                        Toast.makeText(context, "UPDATE DONE!!", Toast.LENGTH_LONG).show();
                    }
                    ((Activity) context).finish();
                    context.startActivity(((Activity) context).getIntent());
                }
            }
        });
        builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Toast.makeText(context, "Task cancelled",Toast.LENGTH_LONG).show();
            }
        });
        builder.show();
    }


    @Override
    public int getItemCount() {
        //return listdata.length;
        return list.size();
    }

    @Override
    public Filter getFilter() {
        return new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence charSequence) {
                String charString = charSequence.toString();
                if (charString.isEmpty()) {
                    list = list2;
                }
                else {
                    ArrayList<MyListData> filteredList = new ArrayList<>();
                    for (MyListData contacts : list2) {
                        if (contacts.getTrans_type().toLowerCase().contains(charString)) {
                            filteredList.add(contacts);
                        }
                    }
                    list = filteredList;
                }
                FilterResults filterResults = new FilterResults();
                filterResults.values = list;
                return filterResults;
            }
            @Override
            protected void publishResults(CharSequence charSequence, FilterResults filterResults) {
                list = (ArrayList<MyListData>) filterResults.values;
                notifyDataSetChanged();
            }
        };
    }


    class ViewHolder0 extends RecyclerView.ViewHolder {
        public TextView transType;
        public TextView transAmount;
        public TextView transRemarks;
        public TextView transID;
        public TextView transDate;
        public ImageView editTrans;
        public ImageView deleteTrans;
        public ViewHolder0(@NonNull View itemView) {
            super(itemView);
            this.transType = (TextView) itemView.findViewById(R.id.transType);
            this.transAmount = (TextView) itemView.findViewById(R.id.transAmount);
            this.transRemarks = (TextView) itemView.findViewById(R.id.transRemarks);
            this.transDate=(TextView) itemView.findViewById(R.id.transDate);
            this.transID = (TextView) itemView.findViewById(R.id.transID);
            this.editTrans = (ImageView) itemView.findViewById(R.id.edit);
            this.deleteTrans = (ImageView) itemView.findViewById(R.id.delete);
        }
    }

    class ViewHolder2 extends RecyclerView.ViewHolder
    {
        private TextView date_display;
        ViewHolder2(@NonNull View itemView) {
            super(itemView);
            this.date_display = (TextView) itemView.findViewById(R.id.date_display);
        }
    }
}
