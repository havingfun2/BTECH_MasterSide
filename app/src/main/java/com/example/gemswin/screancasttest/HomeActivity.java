package com.example.gemswin.screancasttest;


import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
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


@SuppressWarnings("deprecation")
public class HomeActivity extends Activity {

    Button update;
    Button regHome;
    Button search;

    NiftyDialogBuilder dialogReg,dialogRegupdate;
   JSONParser jParser5 = new JSONParser();
    JSONParser jParser6 = new JSONParser();
    JSONObject json;
    JSONObject json1;
    String classupdate,portupdate;
    PrefManager pref;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        pref = new PrefManager(getApplicationContext());
        AlertDialogManager alert = new AlertDialogManager();
        ConnectionDetector    cd = new ConnectionDetector(getApplicationContext());

        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(HomeActivity.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }
        update = (Button) findViewById(R.id.update);
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogUpdate();
            }
        });
    }
    protected void DialogUpdate () {



        dialogReg= NiftyDialogBuilder.getInstance(HomeActivity.this);
        dialogReg
                .withTitle("Update Port")                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def
                .withDividerColor("#11000000")
                .withDialogColor("#FFF176")
                .isCancelableOnTouchOutside(false)
                .withDuration(700)
                .withEffect(Effectstype.Fliph)
                .withButton1Text("SUBMIT")
                .withButton2Text("Cancel")
                .setCustomView(R.layout.dialogupdate, dialogReg.getContext())
                .setButton1Click(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        AlertDialogManager alert = new AlertDialogManager();
                        ConnectionDetector    cd = new ConnectionDetector(getApplicationContext());

                        // Check if Internet present
                        if (!cd.isConnectingToInternet()) {
                            // Internet Connection is not present
                            alert.showAlertDialog(HomeActivity.this,
                                    "Internet Connection Error",
                                    "Please connect to working Internet connection", false);
                            // stop executing code by return
                            return;
                        }
                        portupdate = ((EditText) dialogReg.findViewById(R.id.portupdate)).getText().toString();
                        classupdate = ((EditText) dialogReg.findViewById(R.id.classSelect)).getText().toString();


                        if(!portupdate.equals("") && !classupdate.equals(""))
                            new Update().execute();
                        else
                            Toast.makeText(HomeActivity.this,"Please enter all the fileds.",Toast.LENGTH_SHORT).show();
                        dialogReg.dismiss();
                    }
                })
                .setButton2Click(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        dialogReg.dismiss();
                    }
                })
                .show();
    }



    private class Update extends AsyncTask<String, Void, String> {

        SweetAlertDialog dialog = new SweetAlertDialog(HomeActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        protected void onPreExecute() {
            // this.dialog.setMessage("Uploading file...");
            //this.dialog.show();
            dialog.getProgressHelper().setBarColor(Color.parseColor("#FFF176"));
            dialog.setTitleText("Creating Session...");
            dialog.setCancelable(false);
            this.dialog.show();
        }




        @Override
        protected String doInBackground(String... urls)
        {


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // params.add(new BasicNameValuePair("ip", downloadupdate));
            params.add(new BasicNameValuePair("port", portupdate));
            params.add(new BasicNameValuePair("class", classupdate));
            //  params.add(new BasicNameValuePair("name", nameString));
            pref.setBatch(classupdate);
            String log = "http://176.32.230.250/anshuli.com/ScreenCast/updateClient.php";


            json = jParser5.makeHttpRequest(log, "POST", params);






            //visible
            return null;



        }
        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result)
        {  //gone
            // //System.out.println("RESULT : " + result);

            this.dialog.dismiss();
            try {
                // Checking for SUCCESS TAG




                //Toast.makeText(MainActivity.this, (CharSequence) json, 1).show();

                String account= json.getString("client");
                if(account.equals("FAILED"))
                    Toast.makeText(HomeActivity.this, "Port Updation Failed", Toast.LENGTH_SHORT).show();
                else if(account.equals("SUCCESS")) {

                    pref.setClass(classupdate);
                    pref.setSerialNo(portupdate);
                    Toast.makeText(HomeActivity.this, "Successfully Updated.", Toast.LENGTH_SHORT).show();


                    Intent intent=new Intent(HomeActivity.this,MainActivity.class);
                    startActivity(intent);
                    finish();
                }

            }
            catch (Exception e)
            {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(),"Network connection Error!!!",Toast.LENGTH_LONG).show();
            }






        }
    }

}
