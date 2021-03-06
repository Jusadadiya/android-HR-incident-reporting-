package com.appsforyou.hrincidentnew;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class ReportActivity extends AppCompatActivity {
    //Declaring instance of intent
    Intent reportintent, viewintent;
    Button report;
    //declaring cam request id to open camera
    static final int CAM_REQUEST=1;
    /*Declaring the instance of SQLite database */
    SQLiteDatabase db;
    Intent intent1;
    //declaring file outputstream instance to store image to disk
    FileOutputStream fstream;

    // objects to link with front end
    RadioButton rbmale;
    RadioButton rbfemale;
    EditText date, empId, empName, dept, position;
    Spinner shift, injurytype, injury;
    File newFile;
    Uri pictureUri;
    Uri photoUri;
    Uri mImageCaptureUri;
    String strEmpNum, strDate, strEmpName, strGender, strShift, strDepartment, strPosition, strIncidentType, strInjuryPart;
    String lastIdInt;


    // Create a menuInflater to inflate the options
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
            reportintent = new Intent(ReportActivity.this, ReportActivity.class);
            startActivity(reportintent);
            return  false;
        }
         else if (id == R.id.view){
            viewintent = new Intent(ReportActivity.this, ViewActivity.class);
            startActivity(viewintent);
            return  false;
        }
        return super.onOptionsItemSelected(menuItem);
    }
    /*oncreate method that will load when the reportactivity page opens and will load fill all the data into spinner
      and will populate using adapter the data from the database to the gui elements*/
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_report);

        //binding gui instances with a variable
        date=(EditText) findViewById(R.id.editText2);
        final RadioGroup rb=(RadioGroup) findViewById(R.id.genderGroup);
        rbmale=(RadioButton) findViewById(R.id.maleradio);
        rbfemale=(RadioButton) findViewById(R.id.femaleradio);
        empId = (EditText) findViewById(R.id.empnumEdit);
        empName = (EditText) findViewById(R.id.nameEdit);
        dept = (EditText) findViewById(R.id.departEdit);
        position = (EditText) findViewById(R.id.positionEdit);
        final ImageView img=(ImageView) findViewById(R.id.image);
        shift=(Spinner) findViewById(R.id.shiftSpinner);
        injurytype=(Spinner) findViewById(R.id.injuryType);
        injury=(Spinner) findViewById(R.id.injuryPart);
        //setting male as the default gender
        rbmale.setChecked(true);
        //this code sets GUI options uneditable so that user does not edit information that is not required to be entered
        empName.setEnabled(false);
        dept.setEnabled(false);
        position.setEnabled(false);

        // this listener will autofill employee details from database based on user input of employee ID
        empId.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }
            /*this method will execute when a text input is changed in employee number field and will validate the user input and will
             populate fields from tbl_employee*/
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
            /*this method will execute after a text input is changed in employee number field and will validate the user input and will
            populate fields from tbl_employee*/
            @Override
            public void afterTextChanged(Editable editable) {
                try {
                    if (Integer.parseInt(empId.getText().toString()) > 0 && Integer.parseInt(empId.getText().toString()) < 6) {
                        Cursor c = db.rawQuery("SELECT empName,empDep,empPosition FROM tbl_Employee WHERE empID=" + Integer.parseInt(empId.getText().toString()) + "", null);

                        if (c.moveToFirst()) {
                            do {
                                // Passing values from columns
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
        //this will fetch the current date and set it to the date field by default on load of page
        getCurrentDate();
        //this will create database
        createDb();
        //initializing the button and binding it from its GUI element to its object
        report = (Button) findViewById(R.id.reportBtn);

        // Create tbl_Employee table and insert five records
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_Employee(empID INTEGER PRIMARY KEY AUTOINCREMENT,empName VARCHAR,empDep VARCHAR,empPosition VARCHAR)");
        db.execSQL("INSERT into tbl_Employee(empName,empDep,empPosition) values('Jay','IT','Project Manager'),('William','IT','Tester')," +
                "('Wen','Management','Manager'),('Himani','Management','Associate'),('Khushi','Customer Service','Head Of Department')");

        // Create tbl_BodyParts table and insert body parts data
        db.execSQL("CREATE TABLE IF NOT EXISTS tbl_BodyParts(partID INTEGER PRIMARY KEY AUTOINCREMENT,part VARCHAR)");
        db.execSQL("INSERT into tbl_BodyParts(part) values('Ankle-left'),('Ankle-right'),('Arm-Both'),('Arm-Left Upper'),('Arm-Right Upper'),('Back-All'),('Back-Lower'),('Back-Middle'),('Back-Upper'),('Chest'),('Ear-Both'),('Ear-Left'),('Ear-Right'),('Elbow-right'),('Elbow-Left'),('Eye-both'),('Eye-Left'),('Eye-Right'),('Face'),('Feet Both')," +
                "('Foot left'),('Foot right'),('forearm Left'),('forearm right'),('Hand left'),('Hand Palm Right'),('Hand Palm Left'),('Hand-right'),('Hands-both'),('Head rear'),('Head front'),('Head left'),('Head Right'),('Hip left'),('Hip Right'),('Index Finger Left'),('Index finger Right'),('Knee left'),('Knee Right'),('leg left'),('Leg both'),('Leg Right')," +
                "('Middle finger left'),('Middle finger right'),('Mouth'),('Neck'),('Nose'),('Shoulder right'),('Shoulder left'),('Thumb left'),('Thumb Right'),('Wrist left'),('Wrist right'),('Other'),('Abdomen'),('Multiple'),('N/A'),('Internal')");

        // Create tbl_IncidentHistory table
        // Primary Key: incidentId, which is auto increment
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

        /* View data from database*/
        //code to populate bodypart drop down list
        Cursor c = db.rawQuery("SELECT part FROM tbl_BodyParts", null);
        List<String> categories = new ArrayList<>();
        while (c.moveToNext()){
                // Passing values
            String part = c.getString(0);
            categories.add(part);
        }
        ArrayAdapter<String> bodyPartAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, categories);
        injury.setAdapter(bodyPartAdapter);

        //this event will store data to a table and validate data as well as open camera
        // to take picture as well as send email with all data entered by user
        report.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // get all input values from the gui element on click of button
                String strDate = date.getText().toString();
                strEmpNum = empId.getText().toString();
                strEmpName = empName.getText().toString();
                strGender = "";
                if(rbmale.isChecked()){

                    strGender=rbmale.getText().toString();
                }
                else if(rbfemale.isChecked()){
                    strGender=rbfemale.getText().toString();
                }
                strShift = shift.getSelectedItem().toString();
                strDepartment = dept.getText().toString();
                strPosition = position.getText().toString();
                strIncidentType = injurytype.getSelectedItem().toString();
                strInjuryPart = injury.getSelectedItem().toString();

                // Validation to check empty fields
                if(strEmpNum.length()==0 || strGender.length()==0 || strShift.length()==0 || strIncidentType.length()==0 || strInjuryPart.length()==0){
                    Toast.makeText(getApplicationContext(), "Please Input All Information", Toast.LENGTH_LONG).show();
                    return;
                }
                //validation to check employee number greater than data in employee table
                try {
                    if(Integer.parseInt(strEmpNum) < 0 || Integer.parseInt(strEmpNum) > 6){
                        Toast.makeText(getApplicationContext(), "The employee number is not correct!", Toast.LENGTH_LONG).show();
                        return;
                    }
                }
                catch(Exception e1) {
                    Toast.makeText(getApplicationContext(), "The employee number is not correct!", Toast.LENGTH_LONG).show();
                    return;
                }

                // Insert the record into the table
                db.execSQL("INSERT INTO tbl_IncidentHistory(incidentDate, empNum, empName, gender, shift, department, position, incidentType, injuredPart) " +
                        "VALUES('" + strDate + "','" + strEmpNum + "','" + strEmpName + "','" + strGender + "','" + strShift + "','" + strDepartment + "','" + strPosition + "','" + strIncidentType + "', '" + strInjuryPart + "');");
                Toast.makeText(getApplicationContext(), "Record Successfully Stored", Toast.LENGTH_LONG).show();

                Cursor lastId = db.rawQuery("SELECT incidentId FROM tbl_IncidentHistory", null);
                while (lastId.moveToLast()){
                    // Passing values
                    lastIdInt = lastId.getString(0);
                    break;
                }

                /**
                 *  creation of intent to call the camera app
                 */
                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                File f = new File(android.os.Environment.getExternalStorageDirectory(), "HRphoto.jpg");
                mImageCaptureUri = Uri.fromFile(f);
                startActivityForResult(camera_intent,CAM_REQUEST);
            }
        });
    }

    Intent i = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

    public void onActivity(int requestCode,int resultCode, Intent data){

        super.onActivityResult(requestCode,resultCode,data);
        Bitmap tempImage;
        ImageView img=(ImageView) findViewById(R.id.image);
        //captures photo using the camera
        if (requestCode==CAM_REQUEST && resultCode==RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            Uri tempUri = getImageUri(getApplicationContext(), photo);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            File finalFile = new File(getRealPathFromURI(tempUri));

            System.out.println(mImageCaptureUri);
        }
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data)
    {
        File photoFile = null;
        Uri tempUri = null;

        if (requestCode==CAM_REQUEST && resultCode==RESULT_OK) {
            Bundle extras = data.getExtras();
            Bitmap photo = (Bitmap) extras.get("data");
            //img.setImageBitmap(photo);

            // CALL THIS METHOD TO GET THE URI FROM THE BITMAP
            tempUri = getImageUri(getApplicationContext(), photo);

            // CALL THIS METHOD TO GET THE ACTUAL PATH
            //File finalFile = new File(getRealPathFromURI(tempUri));

        }

        /**
         *  creating the email body
         */

        Intent emailIntent = new Intent(Intent.ACTION_SEND);
                emailIntent.setType("application/image");
                emailIntent.putExtra(Intent.EXTRA_EMAIL, new String[]{"tmathew@conestogac.on.ca"});
                emailIntent.putExtra(Intent.EXTRA_SUBJECT,"HR incident reporting");
                emailIntent.putExtra(Intent.EXTRA_TEXT, "Incident Id: "+ lastIdInt +"" +"\n"+
                        "Incident Date: "+ strDate +""+"\n"+
                        "Employee Number: "+ strEmpNum +"" +"\n"+
                        "Employee Name: "+strEmpName+"" +"\n"+
                        "Gender: "+ strGender +""+"\n"+
                        "Shift: "+ strShift+""+"\n"+
                        "Department: "+strDepartment+""+"\n"+
                        "Position: "+strPosition+""+"\n"+
                        "Incident Type: "+ strIncidentType +""+"\n"+
                        "Injured Body Part: "+ strInjuryPart+"" );
                //photoUri = FileProvider.getUriForFile(ReportActivity.this, "com.appsforyou", photoFile);
                emailIntent.putExtra(Intent.EXTRA_STREAM, tempUri);
                startActivity(Intent.createChooser(emailIntent, "Send mail."));
    }

    /**
     * changing image format from Bitmap to JPEG and getting the path
     * @param inContext
     * @param inImage
     * @return
     */
    public Uri getImageUri(Context inContext, Bitmap inImage) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        inImage.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = "";
        path = MediaStore.Images.Media.insertImage(getContentResolver(), inImage, "Title", "");
        return Uri.parse(path);
    }

    public String getRealPathFromURI(Uri uri) {
        Cursor cursor = getContentResolver().query(uri, null, null, null, null);
        cursor.moveToFirst();
        int idx = cursor.getColumnIndex(MediaStore.Images.ImageColumns.DATA);
        return cursor.getString(idx);
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
        strDate = mdformat.format(calendar.getTime());
        display(strDate);
    }

    //this method is used to display current date in edittext box labeled incident date
    private void display(String num) {
        EditText datetext = (EditText) findViewById(R.id.editText2);
        datetext.setEnabled(false);
        datetext.setText(num);
    }
}
