package com.appsforyou.hrincidentnew;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class ViewActivity extends AppCompatActivity {
    // objects to link with front end
    Intent reportintent, viewintent;
    SQLiteDatabase db;
    private TableLayout tableLayout;
    private TableRow row1;

    // Create a menu
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        getMenuInflater().inflate(R.menu.options, menu);
        return true;
    }
    // define two options in the menu
    @Override
    public boolean onOptionsItemSelected(MenuItem menuItem) {
        int id = menuItem.getItemId();
        if (id == R.id.report){
            reportintent = new Intent(ViewActivity.this, ReportActivity.class);
            startActivity(reportintent);
            return  false;
        }
        else if (id == R.id.view){
            viewintent = new Intent(ViewActivity.this, ViewActivity.class);
            startActivity(viewintent);
            return  false;
        }
        return super.onOptionsItemSelected(menuItem);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view);

        // Open the database
        db = openOrCreateDatabase("HRIncidents", Context.MODE_PRIVATE, null);

        // Create tbl_IncidentHistory table if not exist
        db.execSQL("CREATE TABLE IF NOT EXISTS " +
                "tbl_IncidentHistory(incidentId INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
                "incidentDate VARCHAR NOT NULL , " +
                "empNum VARCHAR NOT NULL , " +
                "empName VARCHAR NOT NULL , " +
                "gender VARCHAR NOT NULL , " +
                "shift VARCHAR NOT NULL , " +
                "department VARCHAR NOT NULL , " +
                "position VARCHAR NOT NULL , " +
                "incidentType VARCHAR NOT NULL , " +
                "injuredPart VARCHAR NOT NULL);");

        // Retrieve all data from tbl_IncidentHistory table
        Cursor csl = db.rawQuery("SELECT * FROM tbl_IncidentHistory", null);
        if (csl.getCount() == 0){
            Toast.makeText(getApplicationContext(), "Incident History Is Empty", Toast.LENGTH_LONG).show();
        }

        // Show all history data in the table
        tableLayout = (TableLayout)findViewById(R.id.table1);
        row1 = (TableRow) findViewById(R.id.row1);
        row1.setBackgroundResource (android.R.drawable.edit_text);

        // generate the row dynamically
        while(csl.moveToNext()){
            TableRow tableRow = new TableRow(ViewActivity.this);
            // define the layout for values of table row
            tableRow.setBackgroundResource (android.R.drawable.edit_text);
            for(int j=0; j<10; j++){
                TextView tv = new TextView(ViewActivity.this);
                tv.setText(csl.getString(j));
                tv.setPadding(30,0,0,0);
                tableRow.addView(tv);
            }
            tableLayout.addView(tableRow);
        }
        csl.close();
    }
}
