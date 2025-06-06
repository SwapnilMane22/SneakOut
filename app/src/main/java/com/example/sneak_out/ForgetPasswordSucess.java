package com.example.sneak_out;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class ForgetPasswordSucess extends AppCompatActivity {

    TextView txtupd,txtupdsuccess;
    ImageView imgcheck;
    Button buttlogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password_sucess);

        //Hooks
        txtupd = findViewById(R.id.updtxt);
        txtupdsuccess = findViewById(R.id.updsuccesstxt);
        imgcheck = findViewById(R.id.checkimg);
        buttlogin = findViewById(R.id.loginbutt);
    }

    public void callLogin(View view) {
        Intent intent = new Intent(ForgetPasswordSucess.this,Login.class);
        Pair[] pairs = new Pair[4];
        pairs[0]= new Pair<View, String>(txtupd,"splashlogoimage");
        pairs[1]= new Pair<View, String>(imgcheck,"signinup");
        pairs[2]= new Pair<View, String>(txtupdsuccess,"passw");
        pairs[3]= new Pair<View, String>(buttlogin,"buttgo");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ForgetPasswordSucess.this,pairs);
        startActivity(intent,options.toBundle());
        finish();
    }
}