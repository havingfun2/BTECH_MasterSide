package com.example.gemswin.screancasttest;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;
import com.opencsv.CSVWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;

public class checkActivity extends AppCompatActivity {

    Button csv;
    NiftyDialogBuilder dialogchkdate;
    String csv_name;
    FileWriter mFileWriter;
    CSVWriter writer;
    String [] data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check);

getSupportActionBar().hide();
        AlertDialogManager alert = new AlertDialogManager();
        ConnectionDetector    cd = new ConnectionDetector(getApplicationContext());

        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(checkActivity.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }
        csv = (Button)findViewById(R.id.csv);

        csv.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {

                DialogBoxCheck();

            }
        });


        //  getActionBar().hide();
        init();
    }
    public void init() {
        TableLayout stk = (TableLayout) findViewById(R.id.table_main);
        TableRow tbrow0 = new TableRow(this);
        TextView tv0 = new TextView(this);
        tv0.setText(" Sl.No ");
        tv0.setTextSize(20);
        tv0.setTextColor(Color.BLACK);
        tbrow0.addView(tv0);
        TextView tv1 = new TextView(this);
        tv1.setText(" ROLL NUMBER ");
        tv1.setTextSize(20);
        tv1.setTextColor(Color.BLACK);
        tbrow0.addView(tv1);
        TextView tv2 = new TextView(this);
        tv2.setText(" ATTENDENCE ");
        tv2.setTextSize(20);
        tv2.setTextColor(Color.BLACK);
        tbrow0.addView(tv2);
        /*TextView tv3 = new TextView(this);
        tv3.setText(" Stock Remaining ");
        tv3.setTextColor(Color.WHITE);
        tbrow0.addView(tv3);*/
        stk.addView(tbrow0);
        data = new String[AttendActivity.roll_name.size()];
        for (int i = 0; i < AttendActivity.roll_name.size(); i++) {
            TableRow tbrow = new TableRow(this);
            TextView t1v = new TextView(this);
            int p = i+1;
            t1v.setText("" + p );
            t1v.setTextColor(Color.BLACK);
            t1v.setTextSize(15);
            t1v.setGravity(Gravity.CENTER);
            tbrow.addView(t1v);
            TextView t2v = new TextView(this);
            t2v.setText(AttendActivity.putClass + "-" + AttendActivity.roll_name.get(i));
            t2v.setTextSize(15);
            t2v.setTextColor(Color.BLACK);
            t2v.setGravity(Gravity.CENTER);
            tbrow.addView(t2v);
            TextView t3v = new TextView(this);
            t3v.setText(AttendActivity.atten_name.get(i));
            t3v.setTextSize(15);
            t3v.setTextColor(Color.BLACK);
            t3v.setGravity(Gravity.CENTER);
            tbrow.addView(t3v);
           /* TextView t4v = new TextView(this);
            t4v.setText("" + i * 15 / 32 * 10);
            t4v.setTextColor(Color.WHITE);
            t4v.setGravity(Gravity.CENTER);
            tbrow.addView(t4v);*/
            stk.addView(tbrow);

            data[i]= AttendActivity.putClass + "-" + AttendActivity.roll_name.get(i) + "-" + AttendActivity.atten_name.get(i) ;
        }

    }

    protected void DialogBoxCheck() {
        // TODO Auto-generated method stub

        dialogchkdate= NiftyDialogBuilder.getInstance(checkActivity.this);
        // dialogReg = new Dialog(FileBrowser.this);

        dialogchkdate
                .withTitle("Download & Share")                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def
                .withDividerColor("#11000000")
                .withDialogColor("#FFF176")
                .isCancelableOnTouchOutside(false)
                .withDuration(700)
                .withEffect(Effectstype.Slideleft)
                .withButton1Text("SUBMIT")
                .withButton2Text("Cancel")
                .setCustomView(R.layout.dialog_csv, dialogchkdate.getContext())
                .setButton1Click(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        AlertDialogManager alert = new AlertDialogManager();
                        ConnectionDetector    cd = new ConnectionDetector(getApplicationContext());

                        // Check if Internet present
                        if (!cd.isConnectingToInternet()) {
                            // Internet Connection is not present
                            alert.showAlertDialog(checkActivity.this,
                                    "Internet Connection Error",
                                    "Please connect to working Internet connection", false);
                            // stop executing code by return
                            return;
                        }
                        csv_name = ((EditText) dialogchkdate.findViewById(R.id.studentcsv)).getText().toString();

                        String baseDir = android.os.Environment.getExternalStorageDirectory().getAbsolutePath();
                        //  String fileName = "AnalysisData.csv";
                        String filePath = baseDir + File.separator + csv_name + ".csv";
                        File f = new File(filePath);


                        if (f.exists() && !f.isDirectory()) {
                            try {
                                mFileWriter = new FileWriter(filePath, true);
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                            writer = new CSVWriter(mFileWriter);
                        } else {
                            try {
                                writer = new CSVWriter(new FileWriter(filePath));
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                        // String[] data = {"Ship Name","Scientist Name", "..."};

                        writer.writeNext(data);

                        try {
                            writer.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                        Intent intentShareFile = new Intent(Intent.ACTION_SEND);
                        // File fileWithinMyDir = new File(myFilePath);
                        intentShareFile.setType("application/csv");
                        intentShareFile.putExtra(Intent.EXTRA_STREAM, Uri.parse("file://" + filePath));

                        intentShareFile.putExtra(Intent.EXTRA_SUBJECT,
                                "Attendence file for" + " " + AttendActivity.putClass);
                        intentShareFile.putExtra(Intent.EXTRA_TEXT, "Attendence file for" + " " + AttendActivity.putClass);

                        startActivity(Intent.createChooser(intentShareFile, "Attendence file for" + " " + AttendActivity.putClass));


                        dialogchkdate.dismiss();
                    }
                })
                .setButton2Click(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        dialogchkdate.dismiss();
                    }
                })
                .show();
    }
}
