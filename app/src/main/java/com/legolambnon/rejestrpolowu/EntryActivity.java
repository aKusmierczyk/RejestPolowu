package com.legolambnon.rejestrpolowu;

import android.app.DatePickerDialog;


import java.text.DateFormat;
import java.util.Calendar;


import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;


public class EntryActivity extends AppCompatActivity {

    DatabaseHelper db;
    private static final String TAG = "Entry activity";

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimePickerDialog;
    private Spinner spin_species;
    Button btnDate, btnSave, btnLoad, btnTime;
    EditText txtLength, txtWeight, txtBait, txtArea, txtWeather;
    String  species, date, time;
    int hour, minutes;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_entry);

        db = new DatabaseHelper(this);


        txtLength = (EditText) (findViewById(R.id.get_length));
        txtWeight = (EditText) (findViewById(R.id.get_weight));
        txtBait = (EditText) (findViewById(R.id.get_bait));
        txtArea = (EditText) (findViewById(R.id.get_area));
        txtWeather = (EditText) (findViewById(R.id.get_weather));

        btnSave = (Button) findViewById(R.id.btnSave);
        btnTime = (Button) (findViewById(R.id.btnTime));

        spin_species = (Spinner) findViewById(R.id.spin_species);
        spin_species.setPrompt("Wybierz gatunek");

        ArrayAdapter adapter = ArrayAdapter.createFromResource(this, R.array.species, android.R.layout.simple_spinner_item);
        spin_species.setAdapter(adapter);

        spin_species.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                species = spin_species.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }


        });

        btnDate = (Button) (findViewById(R.id.btnDate));
        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        EntryActivity.this,
                        android.R.style.Theme_DeviceDefault_Light_Dialog,
                        mDateSetListener,
                        year, month, day);

                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override

            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                int mMonth= month+1;
                Log.d(TAG, "onDateSet: date: " + year + "/" + mMonth + "/" + day);

                date = day + "/" + mMonth + "/" + year;
                btnDate.setText(date);
            }

        };

        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                minutes = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(EntryActivity.this,
                        mTimePickerDialog,
                        hour, minutes, true);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                timePickerDialog.show();
            }
        });

        mTimePickerDialog = new TimePickerDialog.OnTimeSetListener(){

            @Override
            public void onTimeSet(TimePicker timePicker, int i, int i1) {
                Log.d(TAG, "onTimeSet: time: " + i + "/" + i1);
                time = i + ":" +i1;
                btnTime.setText(time);
            }
        };

        AddData();
    }


    /*
    public void ShowTimePickerDialog(){
        btnTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                minutes = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(EntryActivity.this,
                        mTimePickerDialog,
                        hour, minutes, true);
                timePickerDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                timePickerDialog.show();

                mTimePickerDialog = new TimePickerDialog.OnTimeSetListener(){

                    @Override
                    public void onTimeSet(TimePicker timePicker, int i, int i1) {
                        Log.d(TAG, "onTimeSet: time: " + i + "/" + i1);
                        time = i + ":" +i1;
                        btnTime.setText(time);
                    }
                };
            }
        });
    }
    */

    public void AddData(){
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder confirmation = new AlertDialog.Builder(EntryActivity.this);
                confirmation.setMessage("Czy chcesz zapisać?").setCancelable(false)
                        .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                boolean isInserted = db.insertData(spin_species.getSelectedItem().toString(),
                                        txtLength.getText().toString(),
                                        txtWeight.getText().toString(),
                                        date,
                                        txtBait.getText().toString(),
                                        txtArea.getText().toString(),
                                        time,
                                        txtWeather.getText().toString());

                                if(isInserted == true){
                                    Toast.makeText(EntryActivity.this, "Zapisano", Toast.LENGTH_LONG).show();
                                }
                                else{
                                    Toast.makeText(EntryActivity.this, "Blad zapisu", Toast.LENGTH_LONG).show();
                                }
                            }
                        })
                        .setNegativeButton("Nie", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.cancel();
                            }
                        });
                AlertDialog alert = confirmation.create();
                alert.setTitle("Potwierdzenie");
                alert.show();

            }
        });
    }


    /*
    public void viewAll(){
        btnLoad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Cursor res = db.getAllData();
                if(res.getCount() == 0) {
                    showMessage("Error","Brak danych");
                    return;
                }
                StringBuffer buffer = new StringBuffer();
                while(res.moveToNext()) {
                    buffer.append("Id :"+res.getString(0)+"\n");
                    buffer.append("Gatunek :"+res.getString(1)+"\n");
                    buffer.append("Długość :"+res.getString(2)+"\n");
                    buffer.append("Waga :"+res.getString(3)+"\n");
                    buffer.append("Data :"+res.getString(4)+"\n");
                    buffer.append("Przynęta :"+res.getString(5)+"\n");
                    buffer.append("Łowisko :"+res.getString(6)+"\n\n");
                    buffer.append("Godzina :"+res.getString(7)+"\n\n");
                    buffer.append("Pogoda :"+res.getString(8)+"\n\n");
                }
                showMessage("Wpisy",buffer.toString());
            }
        });
    }
    */

    public void showMessage(String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
    }

}
