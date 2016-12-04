package com.example.gemswin.screancasttest;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.SurfaceTexture;
import android.net.wifi.WifiManager;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.text.format.Formatter;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.TextureView;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.gemswin.screancasttest.ScreenCastLib.MediaCodecFactory;
import com.example.gemswin.screancasttest.ScreenCastLib.VideoChunk;
import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class MainActivity extends Activity {
    Button button, canvas, download, sync, resetIP, doubt, openBrowser,attendence;
    String downloadIp, filename,portIP;
    String saveIP = "-1", saveRoll = "-1", flag = "0";
    NiftyDialogBuilder dialogRegupdate;
    String itemValue;
    int portFTP;
    JSONParser jParser5 = new JSONParser();
    JSONParser jParserStatus = new JSONParser();
    JSONObject json;
    JSONParser jParser6 = new JSONParser();
    JSONObject json1;
    JSONParser jParser7 = new JSONParser();
    JSONObject json2;
    JSONObject jsonStatus;
    PrefManager pref;
    public static String portString;
    ArrayList<String> planetList;
    ArrayList<String> planetList1;
    private ListView mainListView;
    private ArrayAdapter<String> listAdapter;
    NiftyDialogBuilder dialogDoubt1;


    public static List<String> ipArray;

    GridView grid;
    String[] web = {
            "Browse",
            "Reset Session",
            "Create Canvas",
            "Attendence",
            "FTP Server Files",
            "Go To Web",
            "Doubts & Interchange"


    } ;
    int[] imageId = {
            R.drawable.browse,
            R.drawable.reset,
            R.drawable.create,
            R.drawable.attendence,
            R.drawable.ftp,
            R.drawable.web,
            R.drawable.interchange

    };

    @TargetApi(Build.VERSION_CODES.HONEYCOMB)
    @SuppressLint("NewApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        AlertDialogManager alert = new AlertDialogManager();
        ConnectionDetector    cd = new ConnectionDetector(getApplicationContext());

        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(MainActivity.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }
        pref = new PrefManager(getApplicationContext());
        pref.setFlag("0");
        planetList = new ArrayList<String>();
        planetList1 = new ArrayList<String>();

        //doubt = (Button) findViewById(R.id.doubt);
       /* sync = (Button) findViewById(R.id.syncIP);*/

        CustomGridIcons adapter = new CustomGridIcons(MainActivity.this, web, imageId);
        grid=(GridView)findViewById(R.id.grid2);
        grid.setAdapter(adapter);
       grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                if (web[+position].equals("Browse")) {
                    fileBrowser();
                }
                if (web[+position].equals("Create Canvas")) {
                    canvas();
                }
                if (web[+position].equals("Attendence")) {
                    Intent i = new Intent(MainActivity.this, AttendActivity.class);
                    startActivity(i);
                }
                if (web[+position].equals("FTP Server Files")) {
                    DialogBox();
                }
                if (web[+position].equals("Go To Web")) {
                    Intent search = new Intent(MainActivity.this, webview.class);
                    startActivity(search);
                }
                if (web[+position].equals("Doubts & Interchange")) {
                    new viewdoubttask().execute();
                }
                if (web[+position].equals("Reset Session")) {
                    new resetIPs().execute();
                }

            }
        });
    }

   /* @Override
    public void onDestroy() {
        //Toast.makeText(this, "Service Destroyed", Toast.LENGTH_LONG).show();
        super.onDestroy();
        new resetIPs().execute();


    }*/

    private void canvas() {
        Intent intent = new Intent(this, CanvasMain.class);
        startActivity(intent);
    }

    private void fileBrowser() {
        Intent intent = new Intent(this, FileBrowser.class);
        startActivity(intent);

    }


    public class Download extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {

            FTPClient con = null;

            try {
                con = new FTPClient();
                con.connect(downloadIp, portFTP);

                if (con.login("FTPUser", "ankit")) {
                    con.enterLocalPassiveMode();
                    con.setFileType(FTP.BINARY_FILE_TYPE);
                    String data = "/sdcard/Download/" + filename;

                    OutputStream out = new FileOutputStream(new File(data));
                    boolean result = con.retrieveFile(filename, out);
                    out.close();
                    if (result) Log.v("download result", "succeeded");
                    con.logout();
                    con.disconnect();
                }
            } catch (Exception e) {
                Log.v("download result", "failed");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(String result) {
            dialog.dismiss();

        }

        SweetAlertDialog dialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        protected void onPreExecute() {
            // this.dialog.setMessage("Uploading file...");
            //this.dialog.show();
            dialog.getProgressHelper().setBarColor(Color.parseColor("#FFF176"));
            dialog.setTitleText("Loading...");
            dialog.setCancelable(false);
            this.dialog.show();
        }


    }


    protected void DialogBox() {




        dialogRegupdate= NiftyDialogBuilder.getInstance(MainActivity.this);
        dialogRegupdate
                .withTitle("Download From FTP Server")                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def
                .withDividerColor("#11000000")
                .withDialogColor("#FFF176")
                .isCancelableOnTouchOutside(false)
                .withDuration(700)
                .withEffect(Effectstype.Flipv)
                .withButton1Text("SUBMIT")
                .withButton2Text("Cancel")
                .setCustomView(R.layout.dialog, dialogRegupdate.getContext())
                .setButton1Click(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        AlertDialogManager alert = new AlertDialogManager();
                        ConnectionDetector    cd = new ConnectionDetector(getApplicationContext());

                        // Check if Internet present
                        if (!cd.isConnectingToInternet()) {
                            // Internet Connection is not present
                            alert.showAlertDialog(MainActivity.this,
                                    "Internet Connection Error",
                                    "Please connect to working Internet connection", false);
                            // stop executing code by return
                            return;
                        }
                        downloadIp = ((EditText) dialogRegupdate.findViewById(R.id.ip)).getText().toString();
                        portIP = ((EditText) dialogRegupdate.findViewById(R.id.port)).getText().toString();
                        filename = ((EditText) dialogRegupdate.findViewById(R.id.filename)).getText().toString();
                        if(!downloadIp.equals("") && !portIP.equals("") && !filename.equals("")  ){
                            portFTP=Integer.parseInt(portIP);
                            new Download().execute();
                        }

                        else
                            Toast.makeText(MainActivity.this,"Please enter all the fileds.",Toast.LENGTH_SHORT).show();


                        dialogRegupdate.dismiss();
                    }
                })
                .setButton2Click(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {

                        dialogRegupdate.dismiss();
                    }
                })
                .show();
    }

    private class getIPs extends AsyncTask<String, Void, String> {

        SweetAlertDialog dialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
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
             params.add(new BasicNameValuePair("class", pref.getclass()));
             String log1 = "http://176.32.230.250/anshuli.com/ScreenCast/getIPs.php";


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
                ipArray = new ArrayList<>();

                JSONArray account = json1.getJSONArray("IPs");
                for (int i = 0; i < account.length(); i++) {
                    json1 = account.getJSONObject(i);

                    String IpString = json1.getString("IP");

                    portString = json1.getString("PORT");

                    if (!IpString.equals("0.0.0.0"))
                        ipArray.add(IpString);

                }

                if (ipArray != null)
                    Toast.makeText(getApplicationContext(), "IPs are synchronised.", Toast.LENGTH_LONG).show();


            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Network connection Error!!!", Toast.LENGTH_LONG).show();

            }


        }
    }


    private class resetIPs extends AsyncTask<String, Void, String> {

        SweetAlertDialog dialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        protected void onPreExecute() {
            // this.dialog.setMessage("Uploading file...");
            //this.dialog.show();
            dialog.getProgressHelper().setBarColor(Color.parseColor("#FFF176"));
            dialog.setTitleText("Reseting...");
            dialog.setCancelable(false);
            this.dialog.show();
        }


        @Override
        protected String doInBackground(String... urls) {


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            // params.add(new BasicNameValuePair("ip", downloadupdate));

            params.add(new BasicNameValuePair("class", pref.getclass()));
            //  params.add(new BasicNameValuePair("name", nameString));

            String log1 = "http://176.32.230.250/anshuli.com/ScreenCast/resetIPs.php";


            json2 = jParser7.makeHttpRequest(log1, "POST", params);


            //visible
            return null;


        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {  //gone
            // //System.out.println("RESULT : " + result);

            pref.setFlag("0");
            this.dialog.dismiss();
            try {
                // Checking for SUCCESS TAG


                //Toast.makeText(MainActivity.this, (CharSequence) json, 1).show();
                String account = json2.getString("client");
                if (account.equals("FAILED"))
                    Toast.makeText(MainActivity.this, "IPs not Resseted", Toast.LENGTH_SHORT).show();
                else if (account.equals("SUCCESS")) {
                    Toast.makeText(MainActivity.this, "Successfully Resseted.", Toast.LENGTH_SHORT).show();
                }


            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Network connection Error!!!", Toast.LENGTH_LONG).show();

            }


           /* int i =1;
            Intent intent = new Intent(MainActivity.this, MyBroadcastReceiver.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            PendingIntent pendingIntent = PendingIntent.getBroadcast(
                    getBaseContext(), 234324243, intent, PendingIntent.FLAG_UPDATE_CURRENT);
            AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
            alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (i * 1000), pendingIntent);*/

        }
    }

    private class viewdoubttask extends AsyncTask<String, Void, String> {

        SweetAlertDialog dialog = new SweetAlertDialog(MainActivity.this, SweetAlertDialog.PROGRESS_TYPE);
        protected void onPreExecute() {
            dialog.getProgressHelper().setBarColor(Color.parseColor("#FFF176"));
            dialog.setTitleText("Loading...");
            dialog.setCancelable(false);
            this.dialog.show();
        }


        @Override
        protected String doInBackground(String... urls) {


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("class", pref.getBatch()));
            //params.add(new BasicNameValuePair("doubt", doubtstring));
           /* params.add(new BasicNameValuePair("name", pref.getName()));
            params.add(new BasicNameValuePair("roll", pref.getSerialNo()));
*/
            String log = "http://176.32.230.250/anshuli.com/ScreenCast/seeDoubtListMaster.php";  // change php file name


            json = jParser5.makeHttpRequest(log, "POST", params);


            try {
                // Checking for SUCCESS TAG
                planetList.clear();
                planetList1.clear();
                JSONArray account = json.getJSONArray("client");
                for (int i = 0; i < account.length(); i++) {
                    json = account.getJSONObject(i);


                    String doubt = json.getString("DOUBT");
                    String doubt1 = json.getString("ROLL_NUMBER");


                    planetList.add(doubt);
                    planetList1.add(doubt1);
                }


            } catch (Exception e) {
                e.printStackTrace();
                Toast.makeText(getApplicationContext(), "Network connection Error!!!", Toast.LENGTH_LONG).show();
            }


            //visible
            return null;


        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {  //gone
            // //System.out.println("RESULT : " + result);


            this.dialog.dismiss();

            DoubtBox1();


        }
    }


    protected void DoubtBox1() {
        // TODO Auto-generated method stub






        dialogDoubt1= NiftyDialogBuilder.getInstance(MainActivity.this);
        dialogDoubt1
                .withTitle("View Doubt")                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def
                .withDividerColor("#11000000")
                .withDialogColor("#FFF176")
                .isCancelableOnTouchOutside(false)
                .withDuration(700)
                .withEffect(Effectstype.Fadein)
                .setCustomView(R.layout.activity_doubtlist, dialogDoubt1.getContext())
                .show();


        mainListView = (ListView) dialogDoubt1.findViewById(R.id.mainListView);

        listAdapter = new ArrayAdapter<String>(MainActivity.this, R.layout.simplerow, planetList);
        mainListView.setAdapter(listAdapter);


        mainListView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            public boolean onItemLongClick(AdapterView<?> arg0, View arg1,
                                           final int pos, long id) {
                // TODO Auto-generated method stub
                // itemValue = (String) mainListView.getItemAtPosition(pos);
                itemValue = planetList1.get(pos);
                DialogInterface.OnClickListener dialogClickListener = new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        switch (which) {
                            case DialogInterface.BUTTON_POSITIVE:
                                if (pref.getFlag() == "0") {
                                    new saveIP().execute();
                                    pref.setFlag("1");
                                } else {
                                    new recoverIP().execute();
                                }
                                break;

                            case DialogInterface.BUTTON_NEGATIVE:
                                //No button clicked
                                break;
                        }
                    }
                };

                AlertDialog.Builder builder = new AlertDialog.Builder(arg1.getContext());
                builder.setMessage("Are you sure you want to make this student serve as master ?").setPositiveButton("Yes", dialogClickListener)
                        .setNegativeButton("No", dialogClickListener).show();

                return true;

            }
        });

    }

    private class saveIP extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {


            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //  params.add(new BasicNameValuePair("roll_number", username));

            params.add(new BasicNameValuePair("roll", itemValue));

            String log = "http://176.32.230.250/anshuli.com/ScreenCast/saveIP.php";


            jsonStatus = jParserStatus.makeHttpRequest(log, "POST", params);


            //visible
            return null;


        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {  //gone
            // //System.out.println("RESULT : " + result);


            JSONArray account = null;
            try {
                account = jsonStatus.getJSONArray("IPs");

                for (int i = 0; i < account.length(); i++) {
                    jsonStatus = account.getJSONObject(i);

                    saveIP = jsonStatus.getString("IP");
                    saveRoll = jsonStatus.getString("ROLL_NUMBER");
                    pref.setIP(saveIP);
                    pref.setROLL(saveRoll);

                }
                new updatemasterIp().execute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

        private class updatemasterIp extends AsyncTask<String, Void, String> {


            @Override
            protected String doInBackground(String... urls) {


                WifiManager wm = (WifiManager) getSystemService(WIFI_SERVICE);
                String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

                List<NameValuePair> params = new ArrayList<NameValuePair>();
                //  params.add(new BasicNameValuePair("roll_number", username));

                params.add(new BasicNameValuePair("roll", itemValue));
                params.add(new BasicNameValuePair("ip", ip));
                params.add(new BasicNameValuePair("status", "1"));

                String log = "http://176.32.230.250/anshuli.com/ScreenCast/changeStatus.php";


                jsonStatus = jParserStatus.makeHttpRequest(log, "POST", params);


                //visible
                return null;


            }

            // onPostExecute displays the results of the AsyncTask.
            @Override
            protected void onPostExecute(String result) {  //gone
                // //System.out.println("RESULT : " + result);


                String account = null;
                try {
                    account = jsonStatus.getString("client");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                if (account.equals("FAILED"))
                    Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
                else if (account.equals("SUCCESS")) {
                    Intent i = new Intent(MainActivity.this, MainActivity_Reciever.class);
                    startActivity(i);
                    Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                    //Intent intent = new Intent(DoubtActivity.this, LoginActivity.class);


                }
            }
        }




    private class recoverIP extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... urls) {



            List<NameValuePair> params = new ArrayList<NameValuePair>();
            //  params.add(new BasicNameValuePair("roll_number", username));

            params.add(new BasicNameValuePair("roll", pref.getROLL()));
            params.add(new BasicNameValuePair("ip", pref.getIP()));

            String log = "http://176.32.230.250/anshuli.com/ScreenCast/recoverIP.php";


            jsonStatus = jParserStatus.makeHttpRequest(log, "POST", params);


            //visible
            return null;


        }

        // onPostExecute displays the results of the AsyncTask.
        @Override
        protected void onPostExecute(String result) {  //gone
            // //System.out.println("RESULT : " + result);


            String account = null;
            try {
                account = jsonStatus.getString("client");
            } catch (JSONException e) {
                e.printStackTrace();
            }
            if (account.equals("FAILED"))
                Toast.makeText(MainActivity.this, "Failed", Toast.LENGTH_SHORT).show();
            else if (account.equals("SUCCESS")) {

                new saveIP().execute();
               // Toast.makeText(MainActivity.this, "Success", Toast.LENGTH_SHORT).show();
                //Intent intent = new Intent(DoubtActivity.this, LoginActivity.class);


            }
        }
    }




    }

