package com.legolambnon.rejestrpolowu;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;

public class CatalogueActivity extends AppCompatActivity {

    private static final int MY_PERMISSIONS_REQUEST_READ_CONTACTS = 80;
    public DatabaseHelper db;
    ListView listView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_catalogue);

        db = new DatabaseHelper(this);

        if (ContextCompat.checkSelfPermission(CatalogueActivity.this,
                Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

                // No explanation needed, we can request the permission.

                ActivityCompat.requestPermissions(CatalogueActivity.this,
                        new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                        MY_PERMISSIONS_REQUEST_READ_CONTACTS);

                // MY_PERMISSIONS_REQUEST_READ_CONTACTS is an
                // app-defined int constant. The callback method gets the
                // result of the request.
        }


        listView = (ListView) findViewById(R.id.listViewCatalogue);
        listView.setAdapter(new ViewAdapter(this));
        listClick();
    }

    @Override
    public void onResume()
    {
        super.onResume();
        listView.setAdapter(new ViewAdapter(this));
    }

    class SingleRow
    {
        String species;
        String length;
        String date;
        String fishery;
        String image;
        String ID;
        SingleRow(String species, String length, String date, String fishery, String image)
        {

            this.species = species;
            this.length = length;
            this.date = date;
            this.fishery = fishery;
            this.image = image;
        }
    }

    class ViewAdapter extends BaseAdapter
    {
        ArrayList<SingleRow> list;
        Context context;
        ViewAdapter(Context c)
        {
            context = c;
            list = new ArrayList<SingleRow>();
            Cursor res = db.getAllData();
            String[] species = getData(1);
            String[] length = getData(2);
            String[] date = getData(4);
            String[] fishery = getData(6);
            String[] image = getData(9);
            for(int i = 0 ; i<res.getCount() ; i++)
            {
                list.add(new SingleRow(species[i],length[i],date[i],fishery[i],image[i]));
            }

        }
        @Override
        public int getCount() {
            return list.size();
        }

        @Override
        public Object getItem(int i) {
            return list.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(int i, View view, ViewGroup viewGroup) {
            LayoutInflater inflater= (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View row = inflater.inflate(R.layout.single_row,viewGroup,false);
            TextView species = row.findViewById(R.id.txtSpecies);
            TextView fishery = row.findViewById(R.id.txtFishery);
            TextView date = row.findViewById(R.id.txtDate);
            TextView length = row.findViewById(R.id.txtLength);
            ImageView image = row.findViewById(R.id.catalogueImage);


            SingleRow temp=list.get(i);
            species.setText(temp.species);
            fishery.setText(temp.fishery);
            date.setText(temp.date);
            length.setText(temp.length);

            /*
            Uri imageUri = Uri.parse(temp.image);
            InputStream inputStream;

            try {
                inputStream = getContentResolver().openInputStream(imageUri);
                Bitmap bitImage = BitmapFactory.decodeStream(inputStream);

                image.setImageBitmap(bitImage);
            } catch (FileNotFoundException e) {
                e.printStackTrace();

            }
            */
            if(temp.image!=null)
                Glide.with(context).load(Uri.parse(temp.image)).override(300,300).into(image);
            //image.setImageDrawable(resize(getImage(Uri.parse(temp.image))));


            return row;
        }
    }

    public String [] getData(int id){
        Cursor res = db.getAllData();
        String[] listItems = new String [res.getCount()];
        if(res.getCount() == 0) {
            //showMessage("Error","Brak danych");
            return listItems;
        }

        int i=0;
        while(res.moveToNext()){
            listItems[i] = res.getString(id);
            i++;
        }
        return listItems;
    }

    private Drawable resize(Bitmap image) {

        Bitmap bitmapResized = Bitmap.createScaledBitmap(image,
                (int) (image.getWidth() * 0.5), (int) (image.getHeight() * 0.5), false);
        return new BitmapDrawable(getResources(), bitmapResized);
    }

    public void listClick()
    {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent intent = new Intent(CatalogueActivity.this, CatalogueEntryActivity.class);
                intent.putExtra("ID", Integer.toString(i+1));
                startActivity(intent);
            }
        });
    }

    public Bitmap getImage(Uri uri){
        InputStream inputStream;

        try {
            inputStream = getContentResolver().openInputStream(uri);

            Bitmap image = BitmapFactory.decodeStream(inputStream);
            return image;
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Toast.makeText(this, "Nie można otworzyć zdjęcia", Toast.LENGTH_LONG).show();
            return null;
        }
    }


    public void showMessage(String title, String message)
    {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setCancelable(true);
        builder.setTitle(title);
        builder.setMessage(message);
        builder.setCancelable(true);
        builder.show();
        builder.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
    }
}
