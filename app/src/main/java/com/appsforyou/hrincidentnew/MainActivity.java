package com.appsforyou.hrincidentnew;

import android.Manifest;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    Intent reportintent, viewintent;

    // Create a menuInflater to inflate the options
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }

    // define two options in the menu and navigate using that menu
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
       int id = menuItem.getItemId();
       if (id == R.id.report){
           reportintent = new Intent(MainActivity.this, ReportActivity.class);
            startActivity(reportintent);
            return  false;
       }
        else if (id == R.id.view){
            viewintent = new Intent(MainActivity.this, ViewActivity.class);
            startActivity(viewintent);
            return  false;
        }
       return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // asking for camera permission from the user on start of application and permission to write to external storage
        if(Build.VERSION.SDK_INT >= 23){
            requestPermissions(new String[]{Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 2);
        }

    }

}



