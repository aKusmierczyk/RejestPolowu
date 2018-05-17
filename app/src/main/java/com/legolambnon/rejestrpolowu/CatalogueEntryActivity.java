package com.legolambnon.rejestrpolowu;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Environment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TimePicker;
import android.widget.Toast;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Calendar;


public class CatalogueEntryActivity extends AppCompatActivity {

    public static final int IMAGE_GALLERY_REQUEST = 20;
    DatabaseHelper db;
    EditText editSpecies,editLength,editWeight,editDate,editFishery,editBait,editTime,editWeather;
    Button btnDate, btnHour, btnDelete, btnUpdate, btnAddPhoto, btnShowPhoto;
    private ImageView imgView;
    private String date, time;
    private int hour, minutes;

    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private TimePickerDialog.OnTimeSetListener mTimePickerDialog;
    private static final String TAG = "CatalogueEntryActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue_entry);

        db = new DatabaseHelper(this);

        editSpecies = (EditText)(findViewById(R.id.editSpecies));
        editLength = (EditText)(findViewById(R.id.editLength));
        editWeight = (EditText)(findViewById(R.id.editWeight));
        editFishery = (EditText)(findViewById(R.id.editFishery));
        editBait = (EditText)(findViewById(R.id.editBait));
        editWeather = (EditText)(findViewById(R.id.editWeather));

        btnDate = (Button) (findViewById(R.id.btnDate2));
        btnHour = (Button) (findViewById(R.id.btnHour2));
        btnUpdate = (Button) (findViewById(R.id.btnUpdate));
        btnDelete = (Button)(findViewById(R.id.btnDelete));
        btnShowPhoto = (Button)(findViewById(R.id.btnShowPhoto));
        btnAddPhoto = (Button)(findViewById(R.id.btnAddPhoto));

        //imgView = (ImageView)(findViewById(R.id.imageView));
        //entryId();

        btnDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        CatalogueEntryActivity.this,
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

        btnHour.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar c = Calendar.getInstance();
                hour = c.get(Calendar.HOUR_OF_DAY);
                minutes = c.get(Calendar.MINUTE);
                TimePickerDialog timePickerDialog = new TimePickerDialog(CatalogueEntryActivity.this,
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
                btnHour.setText(time);
            }
        };

        setText();
        showPhoto();
        updateEntry();
        deleteEntry();
    }

    public int entryId(){
        Bundle bundle = getIntent().getExtras();
        if(bundle != null){
            String temp = bundle.getString("ID");
            int id = Integer.parseInt(temp);
            return id;
        }
        else{
            Toast.makeText(CatalogueEntryActivity.this, "Brak danych",Toast.LENGTH_LONG).show();
            return 0;
        }
    }

    public String getData(int i, int id){
        Cursor res = db.getAllData();
        String item = "";
        if(res.getCount() == 0) {
            showMessage("Error","Brak danych");
            return item;
        }
        res.move(i);
        item = res.getString(id);

        return item;
    }

    void setText()
    {
        String species = getData(entryId(), 1);
        editSpecies.setText(species);
        String length = getData(entryId(), 2);
        editLength.setText(length);
        String weight = getData(entryId(), 3);
        editWeight.setText(weight);
        String date = getData(entryId(), 4);
        btnDate.setText(date);
        String fishery = getData(entryId(), 6);
        editFishery.setText(fishery);
        String bait = getData(entryId(), 5);
        editBait.setText(bait);
        String time = getData(entryId(), 7);
        btnHour.setText(time);
        String weather = getData(entryId(), 8);
        editWeather.setText(weather);


    }

    public void deleteEntry()
    {
        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                AlertDialog.Builder confirmation = new AlertDialog.Builder(CatalogueEntryActivity.this);
                confirmation.setMessage("Czy chcesz usunąć wpis?").setCancelable(false)
                        .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                Integer deleteRows = db.deleteData(getData(entryId(),0));
                                if(deleteRows > 0)
                                    Toast.makeText(CatalogueEntryActivity.this, "Usunięto wpis",Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(CatalogueEntryActivity.this, "Nie usunięto",Toast.LENGTH_LONG).show();
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


    public void updateEntry()
    {
        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder confirmation = new AlertDialog.Builder(CatalogueEntryActivity.this);
                confirmation.setMessage("Czy chcesz zapisać zmiany?").setCancelable(false)
                        .setPositiveButton("Tak", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {

                                String id = getData(entryId(), 0);
                                String species = editSpecies.getText().toString();
                                String length = editLength.getText().toString();
                                String weight = editWeight.getText().toString();
                                String date = btnDate.getText().toString();
                                String fishery = editFishery.getText().toString();
                                String bait = editBait.getText().toString();
                                String time = btnHour.getText().toString();
                                String weather = editWeather.getText().toString();

                                boolean isUpdated = db.updateData(id, species, length, weight, date, bait, fishery, time, weather);
                                if (isUpdated == true)
                                    Toast.makeText(CatalogueEntryActivity.this, "Zapisano zmiany", Toast.LENGTH_LONG).show();
                                else
                                    Toast.makeText(CatalogueEntryActivity.this, "Błąd zapisu", Toast.LENGTH_LONG).show();

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

    public void onImageGalleryClicked(View v){
        Intent photoPickerIntent = new Intent(Intent.ACTION_PICK);
        File pictureDirectory = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        String pictureDirectoryPath = pictureDirectory.getPath();
        Uri data = Uri.parse(pictureDirectoryPath);
        photoPickerIntent.setDataAndType(data, "image/*");

        startActivityForResult(photoPickerIntent, IMAGE_GALLERY_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
       if(resultCode == RESULT_OK){
           if(requestCode == IMAGE_GALLERY_REQUEST){
               //adres zdjęcia
               Uri imageUri = data.getData();

               InputStream inputStream;


               String id = getData(entryId(), 0);
               boolean isInserted = db.insertImageUri(id, imageUri.toString());
               if(isInserted == true)
                   Toast.makeText(CatalogueEntryActivity.this, "Dodano zdjęcie" , Toast.LENGTH_LONG).show();
               else
                   Toast.makeText(CatalogueEntryActivity.this, "Błąd podczas dodawania zdjęcia", Toast.LENGTH_LONG).show();

               try {
                   inputStream = getContentResolver().openInputStream(imageUri);
                   Bitmap image = BitmapFactory.decodeStream(inputStream);

                   //imgView.setImageDrawable(resize(image));
                   //ustaw zdjecie 1
                    //imgView.setImageBitmap(image);
               } catch (FileNotFoundException e) {
                   e.printStackTrace();
                   Toast.makeText(this, "Nie można otworzyć zdjęcia", Toast.LENGTH_LONG).show();
               }
           }
       }
    }

    public void showPhoto() {
        btnShowPhoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String temp = getData(entryId(),9);
                if(temp != null && !temp.isEmpty())
                {
                    Uri uri = Uri.parse(getData(entryId(), 9));
                    Intent intent = new Intent(Intent.ACTION_VIEW, uri);

                    startActivity(intent);
                }
            }
        });
    }

    /*
    private Drawable resize(Bitmap image) {

        Bitmap bitmapResized = Bitmap.createScaledBitmap(image,
                (int) (image.getWidth() * 0.5), (int) (image.getHeight() * 0.5), false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }
    */

    public void showMessage(String title, String message)
    {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.show();
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
    }
}
