package com.example.gemswin.screancasttest;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.Toast;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class TakeAttendence extends Activity {
    Button b,submit;
    int []f;
    PrefManager pref;
    JSONParser jParser = new JSONParser();
    JSONObject json;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_take_attendence);

        AlertDialogManager alert = new AlertDialogManager();
        ConnectionDetector    cd = new ConnectionDetector(getApplicationContext());

        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(TakeAttendence.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }
        submit = (Button)findViewById(R.id.sbatten);
        pref = new PrefManager(getApplicationContext());
        f = new int[Integer.parseInt(pref.getNumber()) + 1];
        if(AttendActivity.attflag==4) {
            for (int i = 1; i <= Integer.parseInt(pref.getNumber()); i++) f[i] = 0;
            // int f[ Integer.parseInt(pref.getNumber())];
            for (int i = 1; i <= Integer.parseInt(pref.getNumber()); i++) {

                GridLayout linear1 = (GridLayout) findViewById(R.id.linearlayout);
                b = new Button(this);
                b.setTextColor(Color.parseColor("#37BC61"));
                b.setText(Integer.toString(i));
                b.setTextSize(10);
                b.setLayoutParams(new LinearLayout.LayoutParams(135, LinearLayout.LayoutParams.WRAP_CONTENT));
                linear1.addView(b);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Button l = (Button) v;
                        if (f[Integer.parseInt(l.getText().toString())] == 0) {
                            l.setTextColor(Color.parseColor("#ff4c4c"));
                            f[Integer.parseInt(l.getText().toString())] = 1;
                        } else {
                            l.setTextColor(Color.parseColor("#37BC61"));
                            f[Integer.parseInt(l.getText().toString())] = 0;
                        }

                       // Toast.makeText(getApplicationContext(), "Yipee.." + l.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
            submit.setOnClickListener(new View.OnClickListener() {






                @Override
                public void onClick(View v) {

                    new subAtten().execute();

                }
            });
        }
        else{
            for (int i = 1; i <= Integer.parseInt(pref.getNumber()); i++) f[i] = 1;
            // int f[ Integer.parseInt(pref.getNumber())];
            for (int i = 1; i <= Integer.parseInt(pref.getNumber()); i++) {

                GridLayout linear1 = (GridLayout) findViewById(R.id.linearlayout);
                b = new Button(this);
                b.setTextColor(Color.parseColor("#ff4c4c"));
                b.setText(Integer.toString(i));
                b.setTextSize(10);
                b.setLayoutParams(new LinearLayout.LayoutParams(135, LinearLayout.LayoutParams.WRAP_CONTENT));
                linear1.addView(b);
                b.setOnClickListener(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        Button l = (Button) v;
                        if (f[Integer.parseInt(l.getText().toString())] == 1) {
                            l.setTextColor(Color.parseColor("#37BC61"));
                            f[Integer.parseInt(l.getText().toString())] = 0;
                        } else {
                            l.setTextColor(Color.parseColor("#ff4c4c"));
                            f[Integer.parseInt(l.getText().toString())] = 1;
                        }

                        Toast.makeText(getApplicationContext(), "Yipee.." + l.getText().toString(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
            submit.setOnClickListener(new View.OnClickListener() {

                @Override
                public void onClick(View v) {

                    new subAtten().execute();

                }
            });
        }



    }

    private class subAtten extends AsyncTask<String, Void, String> {

        SweetAlertDialog dialog = new SweetAlertDialog(TakeAttendence.this, SweetAlertDialog.PROGRESS_TYPE);
        protected void onPreExecute() {
            // this.dialog.setMessage("Uploading file...");
            //this.dialog.show();
            dialog.getProgressHelper().setBarColor(Color.parseColor("#FFF176"));
            dialog.setTitleText("Loading...");
            dialog.setCancelable(false);
            this.dialog.show();
        }



        @Override
        protected String doInBackground(String... urls) {


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("class", pref.getStudentClass()));
            params.add(new BasicNameValuePair("class_date", pref.getStudentClass()+"_date"));

            Calendar c = Calendar.getInstance();

            SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
            String formattedDate = df.format(c.getTime());
            params.add(new BasicNameValuePair("date", formattedDate));
            // params.add(new BasicNameValuePair("ip", downloadupdate));
           for(int i = 1; i<= Integer.parseInt(pref.getNumber()) ;i++) {

               if (f[i] == 0) {

                   params.add(new BasicNameValuePair("atten[]", String.valueOf(i)));
                   // params.add(new BasicNameValuePair("table",studentsClass ));
                   //  params.add(new BasicNameValuePair("name", nameString));
               }
               else{
                   params.add(new BasicNameValuePair("abs[]", String.valueOf(i)));
               }
           }


                    String log1 = "http://176.32.230.250/anshuli.com/ScreenCast/submitAtten.php";


                    json = jParser.makeHttpRequest(log1, "POST", params);




            //visible
            return null;


        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {  //gone
            // //System.out.println("RESULT : " + result);

            this.dialog.dismiss();
            try {
                // Checking for SUCCESS TAG



                //Toast.makeText(MainActivity.this, (CharSequence) json, 1).show();
              String account = json.getString("client");
                if (account.equals("FAILED"))
                    Toast.makeText(TakeAttendence.this, "Attendence Not Marked.", Toast.LENGTH_SHORT).show();
                else if (account.equals("SUCCESS")) {
                    { Toast.makeText(TakeAttendence.this,"Sucessfully marked attendence.",Toast.LENGTH_SHORT).show();
                        Intent o = new Intent(TakeAttendence.this,AttendActivity.class);
                        startActivity(o);
                        finish();}
                }


            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Network connection Error!!!", Toast.LENGTH_LONG).show();

            }


        }
    }
}
