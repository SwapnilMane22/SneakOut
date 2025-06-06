package com.example.sneak_out;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.Objects;

public class Login extends AppCompatActivity {

    Button callSignUp, goo, buttforget;
    ImageView logo;
    CheckBox buttcheck;
    TextInputLayout uss, pass;
    TextView text, sign;

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
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
//        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_login);

        //Hooks
        goo = findViewById(R.id.buttgooo);
        logo = findViewById(R.id.logoimageV);
        uss = findViewById(R.id.loginlayout);
        pass = findViewById(R.id.ploginlayout);
        text = findViewById(R.id.loginheading);
        sign = findViewById(R.id.signinslogan);
        callSignUp = findViewById(R.id.signup_screen);
        buttforget = findViewById(R.id.forgetbutt);
        buttcheck = findViewById(R.id.checkbutt);

    }

    private Boolean validateUsername() {
        EditText val = uss.getEditText();

        if (val == null) {
            uss.setError("Field cannot be empty");
            return false;
        } else {
            uss.setError(null);
            uss.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword() {
        EditText val = pass.getEditText();

        if (val == null) {
            pass.setError("Field cannot be empty");
            return false;
        } else {
            pass.setError(null);
            pass.setErrorEnabled(false);
            return true;
        }
    }

    public void loginUser(View view) {

        if (!isConnected(Login.this)) {
            Toast.makeText(getApplicationContext(), "Please make sure the device has an active Internet", Toast.LENGTH_LONG).show();
            showCustom();
            return;
        }

        if (!validateUsername() | !validatePassword()) {
            return;
        } else {
            isUser();
        }
    }

    private boolean isConnected(Login login) {

        ConnectivityManager connectivityManager = (ConnectivityManager) login.getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager != null) {
            Network conne = connectivityManager.getActiveNetwork();
            if (conne == null) {
                Toast.makeText(getApplicationContext(), "Device has no active Internet", Toast.LENGTH_LONG).show();
                return true;
            } else {
                NetworkCapabilities nc = connectivityManager.getNetworkCapabilities(conne);
                return (nc.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) || nc.hasTransport(NetworkCapabilities.TRANSPORT_WIFI));
            }
        } else{
            return true;
        }
//        NetworkInfo wificon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI);
//        NetworkInfo mobilecon = connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE);

//        if ((wificon != null && wificon.isConnected()) || (mobilecon != null && mobilecon.isConnected())){
//
//            return true;
//
//        }else return false;
    }

    private void showCustom() {

        AlertDialog.Builder builder = new AlertDialog.Builder(Login.this);
        builder.setTitle(R.string.app_name);
        builder.setIcon(R.mipmap.ic_launcher);
        builder.setMessage("Please connect to the internet to proceed further")
                .setCancelable(false)
                .setPositiveButton("Connect", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        startActivity(new Intent(Settings.ACTION_WIFI_SETTINGS));

                    }
                })
                .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {


                    }
                });
        AlertDialog alert = builder.create();
        alert.show();
    }

    private void isUser() {
        final String userInputU = uss.getEditText().getText().toString().trim();
        final String userInputP = pass.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        Query checkUser = reference.orderByChild("username").equalTo(userInputU);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    uss.setError(null);
                    uss.setErrorEnabled(false);
                    String passDB = dataSnapshot.child(userInputU).child("password").getValue(String.class);

                    if (passDB.equals(userInputP)) {

                        pass.setError(null);
                        pass.setErrorEnabled(false);

                        String nameDB = dataSnapshot.child(userInputU).child("name").getValue(String.class);
                        String usernameDB = dataSnapshot.child(userInputU).child("username").getValue(String.class);
                        String emailDB = dataSnapshot.child(userInputU).child("email").getValue(String.class);
                        String phoneNoDB = dataSnapshot.child(userInputU).child("phoneNo").getValue(String.class);

                        Intent intent = new Intent(Login.this, Profile.class);
                        Pair[] pairs = new Pair[8];
                        pairs[0] = new Pair<View, String>(logo, "splashlogoimage");
                        pairs[1] = new Pair<View, String>(text, "splashtextimage");
                        pairs[2] = new Pair<View, String>(sign, "signinup");
                        pairs[3] = new Pair<View, String>(goo, "buttgo");
                        pairs[4] = new Pair<View, String>(uss, "userna");
                        pairs[5] = new Pair<View, String>(pass, "passw");
                        pairs[6] = new Pair<View, String>(callSignUp, "buttsignup");
                        pairs[7] = new Pair<View, String>(buttforget, "forgtpass");

                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
                        intent.putExtra("name", nameDB);
                        intent.putExtra("username", usernameDB);
                        intent.putExtra("email", emailDB);
                        intent.putExtra("phoneNo", phoneNoDB);

                        startActivity(intent, options.toBundle());
                        finish();

                    } else {
                        pass.setError("Incorrect Password");
                        pass.requestFocus();
                    }
                } else {
                    uss.setError("Please enter a valid Username");
                    uss.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(Login.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }

    public void callSignUp(View view) {
        Intent intent = new Intent(Login.this, SignUp.class);
        Pair[] pairs = new Pair[7];
        pairs[0] = new Pair<View, String>(logo, "splashlogoimage");
        pairs[1] = new Pair<View, String>(text, "splashtextimage");
        pairs[2] = new Pair<View, String>(sign, "signinup");
        pairs[3] = new Pair<View, String>(goo, "buttgo");
        pairs[4] = new Pair<View, String>(uss, "userna");
        pairs[5] = new Pair<View, String>(pass, "passw");
        pairs[6] = new Pair<View, String>(callSignUp, "buttsignup");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
        startActivity(intent, options.toBundle());
    }

    public void callForgetPass(View view) {

        final String InputUser = uss.getEditText().getText().toString().trim();

        Intent intent = new Intent(Login.this, ForgetPassword.class);
        Pair[] pairs = new Pair[6];
        pairs[0] = new Pair<View, String>(logo, "splashlogoimage");
        pairs[1] = new Pair<View, String>(text, "splashtextimage");
        pairs[2] = new Pair<View, String>(sign, "signinup");
        pairs[3] = new Pair<View, String>(goo, "buttgo");
        pairs[4] = new Pair<View, String>(uss, "userna");
        pairs[5] = new Pair<View, String>(pass, "passw");
//        pairs[6]= new Pair<View, String>(callSignUp,"buttsignup");

        intent.putExtra("username", InputUser);

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(Login.this, pairs);
        startActivity(intent, options.toBundle());
    }
}