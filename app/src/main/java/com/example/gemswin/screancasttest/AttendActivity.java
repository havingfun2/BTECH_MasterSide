package com.example.gemswin.screancasttest;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class AttendActivity extends Activity {
    Button newattreg,putatt,checkatt,checkattdate;
    JSONParser jParser8 = new JSONParser();
    JSONObject json8;
    RadioGroup rg;
    JSONParser jParser6 = new JSONParser();
    JSONObject json1;
    PrefManager pref;
    JSONParser jParser7 = new JSONParser();
    JSONObject json2;
    NiftyDialogBuilder dialognewatt;
    NiftyDialogBuilder dialogputatt,dialogputdate,dialogchkdate;
    static  String studentsClass,putClass;
    View putDate;
    String numStudents,abs,pre;
    String subject;
    int flag;
    static int attflag;
    static ArrayList<String> roll_name;
    static ArrayList<String> atten_name;
    static ArrayList<String> absent;
    static ArrayList<String> present;
    String [] spl,spl1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_attend);

        AlertDialogManager alert = new AlertDialogManager();
        ConnectionDetector    cd = new ConnectionDetector(getApplicationContext());

        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(AttendActivity.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }
        pref = new PrefManager(getApplicationContext());
        newattreg = (Button) findViewById(R.id.newattreg);
        putatt = (Button) findViewById(R.id.putatt);
        checkatt = (Button) findViewById(R.id.checkatt);
        checkattdate = (Button) findViewById(R.id.checkattdate);


        newattreg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBox();
            }
        });

        putatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogBoxPut();

            }
        });

        checkatt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                roll_name = new ArrayList<String>();
                atten_name = new ArrayList<String>();
                DialogBoxCheck();

            }
        });
        checkattdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                absent = new ArrayList<String>();
                present = new ArrayList<String>();
                DialogBoxPutDate();

            }
        });
    }


    protected void DialogBox() {
        // TODO Auto-generated method stub




        dialognewatt= NiftyDialogBuilder.getInstance(AttendActivity.this);
        // dialogReg = new Dialog(FileBrowser.this);

        dialognewatt
                .withTitle("New Registration")                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def

                .withDialogColor("#FFF176")
                .isCancelableOnTouchOutside(false)
                .withDuration(700)
                .withEffect(Effectstype.Slideleft)
                .withButton1Text("SUBMIT")
                .withButton2Text("Cancel")
                .setCustomView(R.layout.dialogatt, dialognewatt.getContext())
                .setButton1Click(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        AlertDialogManager alert = new AlertDialogManager();
                        ConnectionDetector    cd = new ConnectionDetector(getApplicationContext());

                        // Check if Internet present
                        if (!cd.isConnectingToInternet()) {
                            // Internet Connection is not present
                            alert.showAlertDialog(AttendActivity.this,
                                    "Internet Connection Error",
                                    "Please connect to working Internet connection", false);
                            // stop executing code by return
                            return;
                        }
                        studentsClass = ((EditText) dialognewatt.findViewById(R.id.studentsClass)).getText().toString();
                        numStudents = ((EditText) dialognewatt.findViewById(R.id.numStudents)).getText().toString();
                        //          subject = ((EditText) dialognewatt.findViewById(R.id.subject)).getText().toString();
                        new newAttendence().execute();


                        dialognewatt.dismiss();
                    }
                })
                .setButton2Click(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        dialognewatt.dismiss();
                    }
                })
                .show();

    }

    protected void DialogBoxCheck() {

        dialogchkdate= NiftyDialogBuilder.getInstance(AttendActivity.this);
        // dialogReg = new Dialog(FileBrowser.this);

        dialogchkdate
                .withTitle("View Attendence")                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def
                .withDividerColor("#11000000")
                .withDialogColor("#FFF176")
                .isCancelableOnTouchOutside(false)
                .withDuration(700)
                .withEffect(Effectstype.SlideBottom)
                .withButton1Text("SUBMIT")
                .withButton2Text("Cancel")
                .setCustomView(R.layout.dialogchk, dialogchkdate.getContext())
                .setButton1Click(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {


                        putClass = ((EditText) dialogchkdate.findViewById(R.id.studentchk)).getText().toString();
                        pref.setStudentsClass(putClass);

                        //numStudents = ((EditText) dialogputatt.findViewById(R.id.numStudents)).getText().toString();
                        //          subject = ((EditText) dialognewatt.findViewById(R.id.subject)).getText().toString();

                        if(!putClass.equals("") )
                            new checkAttendence().execute();
                        else
                            Toast.makeText(AttendActivity.this,"Please enter all the fileds.",Toast.LENGTH_SHORT).show();




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
    protected void DialogBoxPut() {



        //dialogputatt.show();

        dialogputatt= NiftyDialogBuilder.getInstance(AttendActivity.this);
        // dialogReg = new Dialog(FileBrowser.this);

        dialogputatt
                .withTitle("Take Attendence")                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def
                .withDividerColor("#11000000")
                .withDialogColor("#FFF176")
                .isCancelableOnTouchOutside(false)
                .withDuration(700)
                .withEffect(Effectstype.Slidetop)
                .withButton1Text("SUBMIT")
                .withButton2Text("Cancel")
                .setCustomView(R.layout.dialogput, dialogputatt.getContext())
                .setButton1Click(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        if (attflag != 0) {
                            if (attflag == 4 || attflag == 5) {
                                putClass = ((EditText) dialogputatt.findViewById(R.id.studentput)).getText().toString();
                                pref.setStudentsClass(putClass);

                                new putAttendence().execute();
                                dialogputatt.dismiss();
                            } else
                                Toast.makeText(AttendActivity.this, "Please Select Attendence Prefrence", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(AttendActivity.this, "Please Select Attendence Prefrence", Toast.LENGTH_SHORT).show();

                        }

                    }
                })
                .setButton2Click(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        dialogputatt.dismiss();
                    }
                })
                .show();
        rg = (RadioGroup)dialogputatt. findViewById(R.id.rg);


        rg.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                if (checkedId == R.id.markpre) {
                    attflag = 5;
                } else if (checkedId == R.id.markabs) {
                    attflag = 4;
                }
            }

        });

    }

    protected void DialogBoxPutDate() {
        // TODO Auto-generated method stub


        dialogputdate= NiftyDialogBuilder.getInstance(AttendActivity.this);
        // dialogReg = new Dialog(FileBrowser.this);

        dialogputdate
                .withTitle("View Attendence By Date")                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def
                .withDividerColor("#11000000")
                .withDialogColor("#FFF176")
                .isCancelableOnTouchOutside(false)
                .withDuration(700)
                .withEffect(Effectstype.Slideright)
                .withButton1Text("SUBMIT")
                .withButton2Text("Cancel")
                .setCustomView(R.layout.dialogputdate, dialogputdate.getContext())
                .setButton1Click(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        putClass = ((EditText) dialogputdate.findViewById(R.id.studentputdate)).getText().toString();
                        //pref.setStudentsClass(putClass);
                        putDate = dialogputdate.findViewById(R.id.dateputdate);
                        //if(flag==2) {
                        pref.setDate(((EditText) putDate).getText().toString());
                        new checkAttendenceDate().execute();
                        //}
                        dialogputdate.dismiss();
                    }
                })
                .setButton2Click(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        dialogputdate.dismiss();
                    }
                })
                .show();
    }

    private class newAttendence extends AsyncTask<String, Void, String> {

        SweetAlertDialog dialog = new SweetAlertDialog(AttendActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        protected void onPreExecute() {
            // this.dialog.setMessage("Uploading file...");
            //this.dialog.show();
            dialog.getProgressHelper().setBarColor(Color.parseColor("#FFF176"));
            dialog.setTitleText("Registering...");
            dialog.setCancelable(false);
            this.dialog.show();
        }


        @Override
        protected String doInBackground(String... urls) {


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // params.add(new BasicNameValuePair("ip", downloadupdate));

            params.add(new BasicNameValuePair("class",studentsClass ));
            params.add(new BasicNameValuePair("class_date",studentsClass + "_date"));
            params.add(new BasicNameValuePair("number",numStudents ));
          // params.add(new BasicNameValuePair("table",studentsClass ));
            //  params.add(new BasicNameValuePair("name", nameString));

            String log1 = "http://176.32.230.250/anshuli.com/ScreenCast/newattreg.php";


            json1 = jParser6.makeHttpRequest(log1, "POST", params);


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
                String account = json1.getString("client");
                if (account.equals("FAILED"))
                    Toast.makeText(AttendActivity.this, "Class Not Registered", Toast.LENGTH_SHORT).show();
                else if (account.equals("SUCCESS")) {
                    Toast.makeText(AttendActivity.this, "Class Registered.", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Network connection Error!!!", Toast.LENGTH_LONG).show();

            }


        }
    }

    private class putAttendence extends AsyncTask<String, Void, String> {

        SweetAlertDialog dialog = new SweetAlertDialog(AttendActivity.this, SweetAlertDialog.PROGRESS_TYPE);
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
            // params.add(new BasicNameValuePair("ip", downloadupdate));

            params.add(new BasicNameValuePair("class",putClass ));
            // params.add(new BasicNameValuePair("table",studentsClass ));
            //  params.add(new BasicNameValuePair("name", nameString));

            String log1 = "http://176.32.230.250/anshuli.com/ScreenCast/putattreg.php";


            json2 = jParser7.makeHttpRequest(log1, "POST", params);


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




                JSONArray account = json2.getJSONArray("ROLL");
          //      for (int i = 0; i < account.length(); i++) {
                    json2 = account.getJSONObject(account.length()-1);

                    String IpString = json2.getString("ROLL_NUMBER");
                    pref.setNumber(IpString);



//                }

                if (IpString != null)
                    Toast.makeText(getApplicationContext(), "Roll Numbers Fetched", Toast.LENGTH_LONG).show();
                Intent i = new Intent(AttendActivity.this,TakeAttendence.class);
                startActivity(i);

            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Roll Numbers Not Fetched", Toast.LENGTH_LONG).show();

            }


        }
    }

    private class checkAttendence extends AsyncTask<String, Void, String> {

        SweetAlertDialog dialog = new SweetAlertDialog(AttendActivity.this, SweetAlertDialog.PROGRESS_TYPE);
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
            // params.add(new BasicNameValuePair("ip", downloadupdate));

            params.add(new BasicNameValuePair("class",putClass ));
            // params.add(new BasicNameValuePair("table",studentsClass ));
            //  params.add(new BasicNameValuePair("name", nameString));

            String log1 = "http://176.32.230.250/anshuli.com/ScreenCast/checkAtten.php";


            json2 = jParser7.makeHttpRequest(log1, "POST", params);


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




                JSONArray account = json2.getJSONArray("ROLL");
                      for (int i = 0; i < account.length(); i++) {

                          json2 = account.getJSONObject(i);

                          String IpString = json2.getString("ROLL_NUMBER");

                          roll_name.add(IpString);
                          String attendence = json2.getString("ATTENDENCE");

                          atten_name.add(attendence);

                          //pref.setNumber(IpString);

                      }


                if (roll_name != null && atten_name != null)
                    Toast.makeText(getApplicationContext(), "Roll Numbers Fetched", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(AttendActivity.this,checkActivity.class);
                     startActivity(i);



            } catch (Exception e) {
                e.printStackTrace();
               // Toast.makeText(getApplicationContext(), "Roll Numbers Not Fetched", Toast.LENGTH_LONG).show();

            }


        }
    }

    private class checkAttendenceDate extends AsyncTask<String, Void, String> {

        SweetAlertDialog dialog = new SweetAlertDialog(AttendActivity.this, SweetAlertDialog.PROGRESS_TYPE);
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
            // params.add(new BasicNameValuePair("ip", downloadupdate));

            params.add(new BasicNameValuePair("class",putClass+"_date" ));
            params.add(new BasicNameValuePair("date",pref.getDate() ));
            // params.add(new BasicNameValuePair("table",studentsClass ));
            //  params.add(new BasicNameValuePair("name", nameString));

            String log1 = "http://176.32.230.250/anshuli.com/ScreenCast/getAttenDate.php";


            json8 = jParser8.makeHttpRequest(log1, "POST", params);


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




                JSONArray account = json8.getJSONArray("IPs");
                for (int i = 0; i < account.length(); i++) {

                    json8 = account.getJSONObject(i);

                     abs = json8.getString("ABSENT");

                   // roll_name.add(IpString);
                     pre = json8.getString("PRESENT");

//                    atten_name.add(attendence);

                    //pref.setNumber(IpString);

                }

                for(int i=0;i<abs.length();i++){
                    spl= abs.split(",");
                   //
                }
                for(int i=0;i<spl.length;i++){
                    absent.add(spl[i]);
                }
                for(int i=0;i<pre.length();i++){
                    spl1= pre.split(",");

                }
                for(int i=0;i<spl1.length;i++){
                    present.add(spl1[i]);
                }
                if (absent != null && present != null)
                    Toast.makeText(getApplicationContext(), "Roll Numbers Fetched", Toast.LENGTH_LONG).show();
                Intent i = new Intent(AttendActivity.this,checkAttendActivity.class);
                startActivity(i);



            } catch (Exception e) {
                e.printStackTrace();
                // Toast.makeText(getApplicationContext(), "Roll Numbers Not Fetched", Toast.LENGTH_LONG).show();

            }


        }
    }
}
