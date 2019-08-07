package com.appsforyou.hrincidentnew;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
Intent reportintent,viewintent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
         reportintent = new Intent(MainActivity.this,ReportActivity.class);
         viewintent = new Intent(MainActivity.this,ViewActivity.class);
    }

   public boolean onCreateOptions(Menu menu){
        getMenuInflater().inflate(R.menu.options,menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem){
        Toast.makeText(this,"SelectedItem: "+menuItem.getTitle(),Toast.LENGTH_LONG).show();
        switch (menuItem.getItemId()){
            case R.id.report:
                startActivity(reportintent);
                return true;
            case R.id.view:
                startActivity(viewintent);
                return true;
                default:
                    return super.onOptionsItemSelected(menuItem);
        }
    }
}
