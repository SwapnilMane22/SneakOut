package com.example.sneak_out;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.textfield.TextInputLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

public class Profile extends AppCompatActivity {

    TextInputLayout fullName, email, phonNum;
    TextView fullnamelabel, usernamelabel;
    ImageView logoprof;
    Button buttlogout, buttupd;

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Profile.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Do you want to exit?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        dialog.cancel();
                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile2);

//        14:45 https://www.youtube.com/watch?v=9QOg8R8ol1w&feature=emb_logo
//        14:38 https://www.youtube.com/watch?v=eGWu0-0TWFI
        //Hooks
        fullName = findViewById(R.id.fullna);
        logoprof = findViewById(R.id.profilelogo);
        email = findViewById(R.id.emal);
        buttlogout = findViewById(R.id.logoutbutt);
        buttupd = findViewById(R.id.updprof);
        phonNum = findViewById(R.id.phonNo);
        fullnamelabel = findViewById(R.id.full_nam);
        usernamelabel = findViewById(R.id.usernam);
        //ShowAllData

        showAllUserData();


    }

    private void showAllUserData() {

        Intent intent = getIntent();
        String user_username = intent.getStringExtra("username");
        String user_name = intent.getStringExtra("name");
        String user_email = intent.getStringExtra("email");
        String user_phone = intent.getStringExtra("phoneNo");

        fullnamelabel.setText(user_name);
        usernamelabel.setText(user_username);
        fullName.getEditText().setText(user_name);
        email.getEditText().setText(user_email);
        phonNum.getEditText().setText(user_phone);

    }

    public void goBacktoLogin(View view) {

        Intent intent = new Intent(Profile.this,Login.class);
        Pair[] pairs = new Pair[8];
        pairs[0]= new Pair<View, String>(logoprof,"splashlogoimage");
        pairs[1]= new Pair<View, String>(fullnamelabel,"splashtextimage");
        pairs[2]= new Pair<View, String>(usernamelabel,"signinup");
        pairs[3]= new Pair<View, String>(buttupd,"buttgo");
        pairs[4]= new Pair<View, String>(fullName,"userna");
        pairs[5]= new Pair<View, String>(email,"passw");
        pairs[6]= new Pair<View, String>(buttlogout,"buttsignup");
        pairs[7]= new Pair<View, String>(phonNum,"forgtpass");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Profile.this,pairs);
        startActivity(intent,options.toBundle());
        finish();
    }
}