package com.erginus.klips;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;

import com.erginus.klips.Commons.ConnectionDetector;
import com.erginus.klips.Fragments.LoginFragment;
import com.facebook.FacebookSdk;



public class LoginActivity extends FragmentActivity {

    private ConnectionDetector cd;
    LoginFragment loginFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(this.getApplicationContext());
        cd = new ConnectionDetector(getApplicationContext());
        if (!cd.isConnectingToInternet()) {
            AlertDialog alertDialog=new AlertDialog.Builder(LoginActivity.this, R.style.cust_dialog).create();
            alertDialog.setTitle("Alert!");
            alertDialog.setMessage("Internet Connection Error, Please connect to working Internet connection");
            alertDialog.setButton("OK", new DialogInterface.OnClickListener() {

                public void onClick(DialogInterface dialog,int which)
                {
                    Toast.makeText(LoginActivity.this, "You clicked on OK", Toast.LENGTH_SHORT).show();
                }
            });

            // Showing Alert Message
            alertDialog.show();

        }
        if(cd.isConnectingToInternet()) {


            if (savedInstanceState == null) {

                loginFragment = new LoginFragment();
                getSupportFragmentManager()
                        .beginTransaction()
                        .add(android.R.id.content, loginFragment)
                        .commit();
            } else {

                loginFragment = (LoginFragment) getSupportFragmentManager()
                        .findFragmentById(android.R.id.content);
            }
        }
    }
    @Override
    public void onBackPressed() {

        new AlertDialog.Builder(this, R.style.cust_dialog)
                .setTitle("Alert !")
                .setMessage("Are you sure you want to exit?")
                .setNegativeButton(android.R.string.no, null)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

//                        Intent a = new Intent(Intent.ACTION_MAIN);
//                        a.addCategory(Intent.CATEGORY_HOME);
//                        a.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                        startActivity(a);


                        ExitActivity.exitApplication(LoginActivity.this);

                        //finish();
                        //LoginActivity.super.onBackPressed();
                    }
                }).create().show();
    }
    @Override
    public void onResume() {
        super.onResume();


    }
    @Override
    public void onPause() {
        super.onPause();


    }

}
