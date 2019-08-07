package com.appsforyou.hrincidentnew;

import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MainActivity extends AppCompatActivity {
    Intent reportintent, viewintent;
    Button report, viewIncident;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        report = (Button) findViewById(R.id.button);
        viewIncident=(Button) findViewById(R.id.button2);
        reportintent = new Intent(MainActivity.this, ReportActivity.class);
        viewintent = new Intent(MainActivity.this, ViewActivity.class);
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(reportintent);
            }
        });
        viewIncident.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(viewintent);
            }
        });
    }

}
   /*public boolean onCreateOptions(Menu menu){
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
}*/
