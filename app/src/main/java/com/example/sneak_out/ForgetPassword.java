package com.example.sneak_out;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.hbb20.CountryCodePicker;

public class ForgetPassword extends AppCompatActivity {

    Button buttnext, buttback;
    TextView txtforget, txtemail;
    ImageView lok;
    CountryCodePicker ccpbutt;
    TextInputLayout txtedphone, texteduser;
    String val, phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);

        //Hooks
        buttback = findViewById(R.id.backbutt);
        lok = findViewById(R.id.lockimg);
        txtforget = findViewById(R.id.forgettxtvw);
        txtemail = findViewById(R.id.provideemltxtvw);
        txtedphone = findViewById(R.id.entrphone);
        ccpbutt = findViewById(R.id.buttccp);
        buttnext = findViewById(R.id.nextbutt);
        texteduser = findViewById(R.id.fusername);

        String user_username = getIntent().getStringExtra("username");
        texteduser.getEditText().setText(user_username);
    }

    public void callLogin(View view) {
        Intent intent = new Intent(ForgetPassword.this,Login.class);
        Pair[] pairs = new Pair[6];
        pairs[0]= new Pair<View, String>(buttback,"splashlogoimage");
        pairs[1]= new Pair<View, String>(lok,"splashtextimage");
        pairs[2]= new Pair<View, String>(txtforget,"signinup");
        pairs[3]= new Pair<View, String>(buttback,"buttgo");
        pairs[4]= new Pair<View, String>(txtemail,"userna");
        pairs[5]= new Pair<View, String>(txtedphone,"passw");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ForgetPassword.this,pairs);
        startActivity(intent,options.toBundle());
        finish();
    }

    private Boolean validateUsername(){
        String val = texteduser.getEditText().getText().toString();
        String noWhiteSpace = "\\A\\w{4,20}\\z";

        if (val.isEmpty()){
            texteduser.setError("Field cannot be empty");
            return false;
        } else if (val.length() >= 20){
            texteduser.setError("Username too long, length should be less than 20 characters.");
            return false;
        } else if (!val.matches(noWhiteSpace)){
            texteduser.setError("Username too small or no special characters allowed");
            return false;
        }else{
            texteduser.setError(null);
            texteduser.setErrorEnabled(false);
            return true;
        }
    }
//    private Boolean validatePassword(){
//        String val = regPassword.getEditText().getText().toString();
//        String passwordVal = "^" +
//                "(?=.*[0-9])" +         //at least 1 digit
//                "(?=.*[a-z])" +         //at least 1 lower case letter
//                "(?=.*[A-Z])" +         //at least 1 upper case letter
//                "(?=.*[a-zA-Z])"+       //any letter
//                "(?=.*[@#$%^&+=])" +    //at least 1 special character
//                "(?=\\S+$)" +           //no white spaces
//                ".{4,}" +               //minimum 4 characters
//                "$";
//
//        if (val.isEmpty()){
//            regPassword.setError("Field cannot be empty");
//            return false;
//        }else if (!val.matches(passwordVal)){
//            regPassword.setError("Password must contain: 1 digit, 1 lowercase letter, 1 uppercase letter & 1 special character");
//            return false;
//        }else {
//            regPassword.setHelperText("Your Password strength is strong.");
//            regPassword.setError(null);
//            regPassword.setErrorEnabled(false);
//            return true;
//        }
//    }
//
//    private Boolean validateName() {
//        String val = regName.getEditText().getText().toString();
//        if (val.isEmpty()){
//            regName.setError("Field cannot be empty");
//            return false;
//        }else{
//            regName.setError(null);
//            regName.setErrorEnabled(false);
//            return true;
//        }
//
//    }
    private boolean validatePhoneNo() {
        val = txtedphone.getEditText().getText().toString().trim();
        if (val.isEmpty()){
            txtedphone.setError("Field cannot be empty");
            return false;
        }else{
            txtedphone.setError(null);
            txtedphone.setErrorEnabled(false);
            return true;
        }
    }

    public void callOTP(View view) {

        if (!validatePhoneNo() | !validateUsername()){
            return;
        }
        else{
            verify_otp();
        }

    }

    private void verify_otp() {

        CheckUser();
    }

    private void CheckUser() {
        final String userInputU = texteduser.getEditText().getText().toString().trim();
        final String userInputPh = "+"+ccpbutt.getFullNumber()+txtedphone.getEditText().getText().toString().trim();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference("Users");

        Query checkUser = reference.orderByChild("username").equalTo(userInputU);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    texteduser.setError(null);
                    txtedphone.setErrorEnabled(false);
                    String phonDB = dataSnapshot.child(userInputU).child("phoneNo").getValue(String.class);

                    if (phonDB.equals(userInputPh)) {

                        txtedphone.setError(null);
                        txtedphone.setErrorEnabled(false);

                        String usernameDB = dataSnapshot.child(userInputU).child("username").getValue(String.class);
                        phone = "+"+ccpbutt.getFullNumber()+val;

                        Intent intent = new Intent(ForgetPassword.this,VerifyOTP.class);
                        Pair[] pairs = new Pair[6];
                        pairs[0]= new Pair<View, String>(buttback,"splashlogoimage");
                        pairs[1]= new Pair<View, String>(lok,"splashtextimage");
                        pairs[2]= new Pair<View, String>(txtforget,"signinup");
                        pairs[3]= new Pair<View, String>(buttback,"buttgo");
                        pairs[4]= new Pair<View, String>(txtemail,"userna");
                        pairs[5]= new Pair<View, String>(txtedphone,"passw");


                        intent.putExtra("username", usernameDB);
                        intent.putExtra("PhoneNo",phone);

                        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(ForgetPassword.this,pairs);
                        startActivity(intent,options.toBundle());
                        finish();

                    } else {
                        txtedphone.setError("Invalid Phone Number");
                        txtedphone.requestFocus();
                    }
                } else {
                    texteduser.setError("Please enter a valid Username");
                    texteduser.requestFocus();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(ForgetPassword.this, databaseError.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
}