package com.appsforyou.hrincidentnew;

import androidx.appcompat.app.AppCompatActivity;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import android.app.AlertDialog;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
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

        final EditText empId = (EditText) findViewById(R.id.empnumEdit);
        final EditText empName = (EditText) findViewById(R.id.nameEdit);
        final EditText dept = (EditText) findViewById(R.id.departEdit);
        final EditText position = (EditText) findViewById(R.id.positionEdit);
        empName.setEnabled(false);
        dept.setEnabled(false);
        position.setEnabled(false);

        // this listener will autofill employee details from database based on user input of employee ID
        empId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                try {
                    if (Integer.parseInt(empId.getText().toString()) > 0 && Integer.parseInt(empId.getText().toString()) < 6) {
                        Cursor c = db.rawQuery("SELECT empName,empDep,empPosition FROM tbl_Employee WHERE empID=" + Integer.parseInt(empId.getText().toString()) + "", null);

                        if (c.moveToFirst()) {
                            do {
                                // Passing values
                                String name = c.getString(0);
                                String empDep = c.getString(1);
                                String empPosition = c.getString(2);
                                // setting values to textfields
                                empName.setText(name);
                                dept.setText(empDep);
                                position.setText(empPosition);
                            } while (c.moveToNext());
                        }
                    } else {
                        Toast.makeText(ReportActivity.this, "Invalid ID Please Try Again!", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(Exception e1) {
                    e1.printStackTrace();
                    Log.e("Memory exceptions","exceptions"+e1);
                }
            }


            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (Integer.parseInt(empId.getText().toString()) > 0 && Integer.parseInt(empId.getText().toString()) < 6) {
                        Cursor c = db.rawQuery("SELECT empName,empDep,empPosition FROM tbl_Employee WHERE empID=" + Integer.parseInt(empId.getText().toString()) + "", null);

                        if (c.moveToFirst()) {
                            do {
                                // Passing values
                                String name = c.getString(0);
                                String empDep = c.getString(1);
                                String empPosition = c.getString(2);
                                // setting values to textfields
                                empName.setText(name);
                                dept.setText(empDep);
                                position.setText(empPosition);
                            } while (c.moveToNext());
                        }
                    } else {
                        Toast.makeText(ReportActivity.this, "Invalid ID Please Try Again!", Toast.LENGTH_SHORT).show();
                    }
                }
                catch(Exception e1) {
                    e1.printStackTrace();
                    Log.e("Memory exceptions","exceptions"+e1);
                }
            }
        });

        getCurrentDate();
        createDb();
        report = (Button) findViewById(R.id.reportBtn);

        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_Employee(empID INTEGER PRIMARY KEY AUTOINCREMENT,empName VARCHAR,empDep VARCHAR,empPosition VARCHAR)");
        db.execSQL("INSERT into tbl_Employee(empName,empDep,empPosition) values('Jay','IT','Project Manager'),('William','IT','Tester')," +
                "('Wen','Management','Manager'),('Himani','Management','Associate'),('Khushi','Customer Service','Head Of Department')");

        /* View data from database*/
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {



                db.close();
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
