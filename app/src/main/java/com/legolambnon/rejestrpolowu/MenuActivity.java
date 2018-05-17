package com.legolambnon.rejestrpolowu;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;


public class MenuActivity extends AppCompatActivity {

    ImageView imgFb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        Button btnEntry = (Button)(findViewById(R.id.btnEntry));
        btnEntry.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent (MenuActivity.this , EntryActivity.class);
                startActivity(intent);
            }
        });

        Button btnCatalogue = (Button)(findViewById(R.id.btnCatalogue));
        btnCatalogue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MenuActivity.this, CatalogueActivity.class);
                startActivity(intent);
            }
        });

        imgFb = (ImageView)(findViewById(R.id.imgFb));
        imgFb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.facebook.com/rejestrpolowu/"));
                startActivity(browserIntent);
            }
        });
    }
}
