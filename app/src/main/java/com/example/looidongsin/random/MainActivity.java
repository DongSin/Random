package com.example.looidongsin.random;

import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private  String[] items = new String[]{};
    private ArrayList<String> arrayList;
    private ArrayAdapter<String> adapter;
    private EditText txtInput;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        ListView listView= (ListView) findViewById(R.id.list1);

        SharedPreferences sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);
        String foodPlace = sharedPreferences.getString("foodPlace", "");

        if(foodPlace.equals("")){
            Toast.makeText(this,"Please enter data!", Toast.LENGTH_LONG).show();
        }else {
            items = foodPlace.split(",");
        }

        arrayList = new ArrayList<>(Arrays.asList(items));
        adapter = new ArrayAdapter<>(this,R.layout.list_item,R.id.txtItem,arrayList);
        listView.setAdapter(adapter);


        txtInput= (EditText)findViewById(R.id.editText1);
        Button btAdd=(Button) findViewById(R.id.inputButton);

        btAdd.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                String newItem = txtInput.getText().toString();

                //check for null
                if(arrayList.contains(newItem)){
                    Toast.makeText(getBaseContext(),"Item already exist!", Toast.LENGTH_LONG).show();
                }else if(newItem == null || newItem.trim().equals("")){
                    Toast.makeText(getBaseContext(),"Input is empty!", Toast.LENGTH_LONG).show();
                }else {
                    arrayList.add(newItem);
                    adapter.notifyDataSetChanged();
                }


                txtInput.setText("");
            }
        });

        Button btPick = (Button) findViewById(R.id.pickButton);
        btPick.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                Random num = new Random();
                String pickItem = arrayList.get(num.nextInt(arrayList.size()));
                TextView display = (TextView)findViewById(R.id.display);
                display.setText(pickItem);
            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            public void onItemClick(AdapterView<?> a, View v, int position, long id) {
                AlertDialog.Builder adb=new AlertDialog.Builder(MainActivity.this);
                adb.setTitle("Delete?");
                adb.setMessage("Are you sure you want to delete " + arrayList.get(position));
                final int positionToRemove = position;
                adb.setNegativeButton("Cancel", null);
                adb.setPositiveButton("Ok", new AlertDialog.OnClickListener() {

                    public void onClick(DialogInterface dialog, int which) {
                        arrayList.remove(positionToRemove);
                        adapter.notifyDataSetChanged();
                    }});
                adb.show();
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();

        SharedPreferences sharedPreferences = getSharedPreferences("Data", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Object[] obj = arrayList.toArray();
        items = Arrays.copyOf(obj,obj.length, String[].class);

        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < items.length; i++){
            sb.append(items[i]).append(",");
        }
        editor.putString("foodPlace",sb.toString());

        editor.commit();
    }

}
