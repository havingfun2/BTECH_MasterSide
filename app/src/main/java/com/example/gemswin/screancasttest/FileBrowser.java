package com.example.gemswin.screancasttest;

import android.Manifest;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.ListActivity;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.ListView;
import android.widget.Toast;

import com.gitonway.lee.niftymodaldialogeffects.lib.Effectstype;
import com.gitonway.lee.niftymodaldialogeffects.lib.NiftyDialogBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;



import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;

import cn.pedant.SweetAlert.SweetAlertDialog;


public class FileBrowser extends Activity {

    private static final String ARG_COLOR = "color";

    String downloadIp,filename,portIP;
    NiftyDialogBuilder dialogReg;
    EditText ipAddress;
    int portFTP;
    private String path;
    public List<File> files = new ArrayList();
    public List<File> dirs = new ArrayList();
    /*********  work only for Dedicated IP ***********/
    static final String FTP_HOST= "192.168.15.50:211";

    /*********  FTP USERNAME ***********/
    static final String FTP_USER = "FTPUser";

    /*********  FTP PASSWORD ***********/
    static final String FTP_PASS  ="ankit";
    File file;
    // TODO: Rename and change types of parameters
    private int color;

    CardView card;

    GridView grid;

    CustomGrid adapter;
    File path1;
    private static final int REQUEST_WRITE_STORAGE = 112;
    static ArrayList<String> pdf_paths=new ArrayList<String>();
    static ArrayList<String> pdf_names=new ArrayList<String>();
    static ArrayList<Integer> pdf_images=new ArrayList<Integer>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_file_browser);

        AlertDialogManager alert = new AlertDialogManager();
        ConnectionDetector    cd = new ConnectionDetector(getApplicationContext());

        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(FileBrowser.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }

        /*Intent intent1 = new Intent(this, MyBroadcastReceiver.class);
        intent1.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(
                getApplicationContext(), 234324243, intent1, PendingIntent.FLAG_UPDATE_CURRENT);
        AlarmManager alarmManager = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + (5 * 1000), pendingIntent);
*/
        boolean hasPermission = (ContextCompat.checkSelfPermission(FileBrowser.this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        boolean hasPermission1 = (ContextCompat.checkSelfPermission(FileBrowser.this,
                Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED);
        if (!hasPermission) {
            ActivityCompat.requestPermissions(FileBrowser.this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }
        if (!hasPermission1) {
            ActivityCompat.requestPermissions(FileBrowser.this,
                    new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    REQUEST_WRITE_STORAGE);
        }
        pdf_paths.clear();
        pdf_names.clear();
        pdf_images.clear();

        path1 = new File(Environment.getExternalStorageDirectory() + "");
        searchFolderRecursive1(path1);

        for(int i = 0;i<  pdf_names.size();i++)
        {
            pdf_images.add(R.drawable.pdf);
        }

        adapter = new CustomGrid(FileBrowser.this, pdf_names, pdf_images);

        grid=(GridView)findViewById(R.id.gridfile);
        grid.setAdapter(adapter);
        grid.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {

                path = pdf_paths.get(+position);
                if (path.endsWith(".pdf")) {

                    DialogBox();
                    /*Intent intent = new Intent(FileBrowser.this, PdfFileRenderer.class);
                    intent.putExtra("pdfpath", path);
                    startActivity(intent)*/;

                } else {
                    Toast.makeText(FileBrowser.this, " File is not suported", Toast.LENGTH_LONG).show();
                }



            }
        });


    }

    private static void searchFolderRecursive1(File folder)
    {
        if (folder != null)
        {
            if (folder.listFiles() != null)
            {
                for (File file : folder.listFiles())
                {
                    if (file.isFile())
                    {
                        //.pdf files
                        if(file.getName().contains(".pdf"))
                        {
                            Log.e("ooooooooooooo", "path__=" + file.getName());
                            file.getPath();
                            pdf_names.add(file.getName());
                            pdf_paths.add(file.getPath());
                            Log.e("pdf_paths", ""+pdf_names);
                        }
                    }
                    else
                    {
                        searchFolderRecursive1(file);
                    }
                }
            }
        }
    }
    public class uploadFile extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            FTPClient con = null;

            try
            {
                con = new FTPClient();
                con.connect(downloadIp,portFTP);

                if (con.login("FTPUser", "ankit"))
                {
                    con.enterLocalPassiveMode(); // important!
                    con.setFileType(FTP.BINARY_FILE_TYPE);
                    // String data = "/sdcard/Download/a.pdf";
                    //String fileSave = file.getPath();
                    String splitter[] = path.split("/");
                    int counter = path.split("/").length - 1;
                    String name = splitter[counter];
                    FileInputStream in = new FileInputStream(new File(path));
                    boolean result = con.storeFile("/"+name, in);
                    in.close();
                    if (result) Log.v("upload result", "succeeded");
                    con.logout();
                    con.disconnect();
                }
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String result) {

            this.dialog.dismiss();
            Intent intent = new Intent(FileBrowser.this, PdfFileRenderer.class);
            intent.putExtra("pdfpath", path);
            startActivity(intent);
        }


      //  private ProgressDialog dialog = new ProgressDialog(FileBrowser.this);

        SweetAlertDialog dialog = new SweetAlertDialog(FileBrowser.this, SweetAlertDialog.PROGRESS_TYPE);
        protected void onPreExecute() {
           // this.dialog.setMessage("Uploading file...");
            //this.dialog.show();
            dialog.getProgressHelper().setBarColor(Color.parseColor("#FFF176"));
            dialog.setTitleText("Uploading file...");
            dialog.setCancelable(false);
            this.dialog.show();
        }




    }

    protected void DialogBox () {
        dialogReg= NiftyDialogBuilder.getInstance(FileBrowser.this);
       // dialogReg = new Dialog(FileBrowser.this);

        dialogReg
                .withTitle("Upload To FTP Server")                                  //.withTitle(null)  no title
                .withTitleColor("#FFFFFF")                                  //def
                .withDividerColor("#11000000")
                .withDialogColor("#FFF176")
                .isCancelableOnTouchOutside(false)
                .withDuration(700)
                .withEffect(Effectstype.Slit)
                .withButton1Text("SUBMIT")
                .withButton2Text("Cancel")
                .setCustomView(R.layout.upload_dialog, dialogReg.getContext())
                .setButton1Click(new View.OnClickListener() {

                    @Override
                    public void onClick(View v) {
                        AlertDialogManager alert = new AlertDialogManager();
                        ConnectionDetector    cd = new ConnectionDetector(getApplicationContext());

                        // Check if Internet present
                        if (!cd.isConnectingToInternet()) {
                            // Internet Connection is not present
                            alert.showAlertDialog(FileBrowser.this,
                                    "Internet Connection Error",
                                    "Please connect to working Internet connection", false);
                            // stop executing code by return
                            return;
                        }
                        downloadIp = ((EditText) dialogReg.findViewById(R.id.ip_up)).getText().toString();
                        portIP = ((EditText) dialogReg.findViewById(R.id.port_up)).getText().toString();
                        if(!downloadIp.equals("") && !portIP.equals("")  )
                        {
                            portFTP=Integer.parseInt(portIP);
                            new uploadFile().execute();
                        }
                        else
                            Toast.makeText(FileBrowser.this,"Please enter all the fileds.",Toast.LENGTH_SHORT).show();



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


}