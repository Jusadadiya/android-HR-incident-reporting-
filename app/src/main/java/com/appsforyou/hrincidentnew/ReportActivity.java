package com.appsforyou.hrincidentnew;

import androidx.appcompat.app.AppCompatActivity;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class ReportActivity extends AppCompatActivity {
    Button report;
    /*Declaring the instance of SQLite database */
    SQLiteDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        EditText empId = (EditText) findViewById(R.id.empnumEdit);
        EditText empName = (EditText) findViewById(R.id.nameEdit);

        getCurrentDate();
        createDb();
        report = (Button) findViewById(R.id.reportBtn);

        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_Employee(empID INTEGER PRIMARY KEY AUTOINCREMENT,empName VARCHAR,empDep VARCHAR,empPosition VARCHAR)");
        db.execSQL("INSERT into tbl_Employee(empName,empDep,empPosition) values('Jay','IT','Project Manager'),('William','IT','Tester')," +
                "('Wen','Management','Manager'),('Himani','Management','Associate'),('Khushi','Customer Service','Head Of Department')");
        /* View all data from database*/

        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                EditText empId = (EditText) findViewById(R.id.empnumEdit);
                EditText empName = (EditText) findViewById(R.id.nameEdit);

               /* Cursor curs1=db.rawQuery("SELECT * FROM tbl_Employee WHERE empID="+Integer.parseInt(empId.getText().toString())+"",null);
                Toast.makeText(ReportActivity.this,"Hi, ",Toast.LENGTH_SHORT).show();
                // empName.setText(curs1.getString(curs1.getColumnIndex("empName")));
                Toast.makeText(ReportActivity.this,"Hi, "+curs1.getString(0),Toast.LENGTH_SHORT).show();

                curs1.close();*/
            }
        });
    }
    //use this method to create db and store data in it
    public void createDb() {
        /*creating the database HRIncident */
        db = openOrCreateDatabase("HRIncidents", Context.MODE_PRIVATE, null);
    }
    //this below method is used to get current date of incident from device
    public void getCurrentDate() {
        Calendar calendar = Calendar.getInstance();
        SimpleDateFormat mdformat = new SimpleDateFormat("MM / dd / yyyy ");
        String strDate = mdformat.format(calendar.getTime());
        display(strDate);
    }
    //this method is used to display current date in edittext box labeled incident date
    private void display(String num) {
        EditText datetext = (EditText) findViewById(R.id.editText2);
        datetext.setEnabled(false);
        datetext.setText(num);
    }
}
