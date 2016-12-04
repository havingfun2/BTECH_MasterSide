package com.example.gemswin.screancasttest;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import java.util.ArrayList;

public class checkAttendActivity extends AppCompatActivity {
    Integer [] roll;
    int x,ct=0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_check_attend);
        getSupportActionBar().hide();
        AlertDialogManager alert = new AlertDialogManager();
        ConnectionDetector    cd = new ConnectionDetector(getApplicationContext());

        // Check if Internet present
        if (!cd.isConnectingToInternet()) {
            // Internet Connection is not present
            alert.showAlertDialog(checkAttendActivity.this,
                    "Internet Connection Error",
                    "Please connect to working Internet connection", false);
            // stop executing code by return
            return;
        }
        roll=new Integer[1000];
        for(int i=0;i<AttendActivity.absent.size();i++) {
            x=Integer.parseInt(AttendActivity.absent.get(i));
            roll[x]=0;
            ct++;
        }
        for(int i=0;i<AttendActivity.present.size();i++) {
            x=Integer.parseInt(AttendActivity.present.get(i));
            roll[x]=1;
            ct++;
        }
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
        for (int i = 1; i < ct; i++) {
            TableRow tbrow = new TableRow(this);
            TextView t1v = new TextView(this);
            int p = i;
            t1v.setText("" + p );
            t1v.setTextColor(Color.BLACK);
            t1v.setGravity(Gravity.CENTER);
            t1v.setTextSize(15);
            tbrow.addView(t1v);
            TextView t2v = new TextView(this);
            t2v.setText(AttendActivity.putClass + "-" + i);
            t2v.setTextColor(Color.BLACK);
            t2v.setGravity(Gravity.CENTER);
            t2v.setTextSize(15);
            tbrow.addView(t2v);
            TextView t3v = new TextView(this);
            t3v.setText(String.valueOf(roll[i]));
            t3v.setTextColor(Color.BLACK);
            t3v.setGravity(Gravity.CENTER);
            t3v.setTextSize(15);
            tbrow.addView(t3v);
           /* TextView t4v = new TextView(this);
            t4v.setText("" + i * 15 / 32 * 10);
            t4v.setTextColor(Color.WHITE);
            t4v.setGravity(Gravity.CENTER);
            tbrow.addView(t4v);*/
            stk.addView(tbrow);
        }

    }
}
