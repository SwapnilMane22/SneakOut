package com.example.sneak_out;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Pair;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.ActionCodeSettings;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.hbb20.CountryCodePicker;

public class SignUp extends AppCompatActivity {

    TextInputLayout regName, regUsername, regEmail, regPhoneNo, regPassword;
    Button regBtn, regToLogin;
    ImageView imglogo;
    TextView txtwel, txtsign;
    CountryCodePicker ccpbutt2;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    String phoneN, valphN;
//    FirebaseAuth authrzn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        //Hooks
        imglogo = findViewById(R.id.logoimageV2);
        txtwel = findViewById(R.id.signupheading);
        txtsign = findViewById(R.id.signinslogan);
        regName = findViewById(R.id.fullname);
        regUsername = findViewById(R.id.usignuplayout);
        regEmail = findViewById(R.id.email);
        regPhoneNo = findViewById(R.id.phoneno);
        regPassword = findViewById(R.id.password);
        regBtn = findViewById(R.id.buttongooo);
        regToLogin = findViewById(R.id.alreadysign);
        ccpbutt2 = findViewById(R.id.buttccp2);

        //for radiogroup see 15:00 https://www.youtube.com/watch?reload=9&v=qcDlcITNqnE

    }

    private Boolean validateUsername(){
        String val = regUsername.getEditText().getText().toString().trim();
        String noWhiteSpace = "\\A\\w{1,20}\\z";

        if (val.isEmpty()){
            regUsername.setError("Field cannot be empty");
            return false;
        } else if (val.length() >= 20){
            regUsername.setError("Username too long, length should be less than 20 characters.");
            return false;
        } else if (!val.matches(noWhiteSpace)){
            regUsername.setError("White spaces are not allowed");
            return false;
        }else{
            regUsername.setError(null);
            regUsername.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatePassword(){
        String val = regPassword.getEditText().getText().toString().trim();
        String passwordVal = "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])"+       //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //minimum 4 characters
                "$";

        if (val.isEmpty()){
            regPassword.setError("Field cannot be empty");
            return false;
        }else if (!val.matches(passwordVal)){
            regPassword.setError("Password must contain: 1 digit, 1 lowercase letter, 1 uppercase letter & 1 special character");
            return false;
        }else {
            regPassword.setHelperText("Your Password strength is strong.");
            regPassword.setError(null);
            regPassword.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validateName() {
        String val = regName.getEditText().getText().toString().trim();
        if (val.isEmpty()){
            regName.setError("Field cannot be empty");
            return false;
        }else{
            regName.setError(null);
            regName.setErrorEnabled(false);
            return true;
        }

    }

    private Boolean validateEmail() {
        String val = regEmail.getEditText().getText().toString().trim();
        String noemailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

        if (val.isEmpty()){
            regEmail.setError("Field cannot be empty");
            return false;
        }else if (!val.matches(noemailPattern)){
            regEmail.setError("Invalid E-mail Address");
            return false;
        }else{
            regEmail.setError(null);
            regEmail.setErrorEnabled(false);
            return true;
        }

    }

    private boolean validatePhoneNo() {
        valphN = regPhoneNo.getEditText().getText().toString().trim();
        if (valphN.isEmpty()){
            regPhoneNo.setError("Field cannot be empty");
            return false;
        }else{
            regPhoneNo.setError(null);
            regPhoneNo.setErrorEnabled(false);
            return true;
        }
    }

    public void SignUpUser(View view){
        if (!validateUsername() | !validatePassword() | !validateName() | !validateEmail() | !validatePhoneNo()){
            return;
        }
        else{
            if (valphN.charAt(0) == '0'){
                valphN = valphN.substring(1);
            }
            phoneN = "+"+ccpbutt2.getFullNumber()+valphN;
            registerUser();
        }
    }

    private void registerUser() {
        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Users");

        String name = regName.getEditText().getText().toString().trim();
        String username = regUsername.getEditText().getText().toString().trim();
        String email = regEmail.getEditText().getText().toString().trim();
        String phoneNo = phoneN;
        String password = regPassword.getEditText().getText().toString().trim();


        UserHelper helper = new UserHelper(name,username,email,phoneNo,password);
        reference.child(username).setValue(helper);

//        authrzn = FirebaseAuth.getInstance();
//        sendOTPemail();
//        Check 5:16 from https://www.youtube.com/watch?v=06YKlMdWyMM

        Toast toast = Toast.makeText(getApplicationContext(),
                "User registered Successfully!\nPlease go to Login Screen to continue.",
                Toast.LENGTH_LONG);
        toast.show();
    }

    public void callLoginScreen(View view) {
        Intent intent = new Intent(SignUp.this,Login.class);
        Pair[] pairs = new Pair[7];
        pairs[0]= new Pair<View, String>(imglogo,"splashlogoimage");
        pairs[1]= new Pair<View, String>(txtwel,"splashtextimage");
        pairs[2]= new Pair<View, String>(txtsign,"signinup");
        pairs[3]= new Pair<View, String>(regBtn,"buttgo");
        pairs[4]= new Pair<View, String>(regUsername,"userna");
        pairs[5]= new Pair<View, String>(regPassword,"passw");
        pairs[6]= new Pair<View, String>(regToLogin,"buttsignup");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SignUp.this,pairs);
        startActivity(intent,options.toBundle());
        finish();
    }

//    private void sendOTPemail() {
//
//        ActionCodeSettings actionCodeSettings =
//                ActionCodeSettings.newBuilder()
//                        // URL you want to redirect back to. The domain (www.example.com) for this
//                        // URL must be whitelisted in the Firebase Console.
//                        .setUrl("https://www.example.com/finishSignUp?cartId=1234")
//                        // This must be true
//                        .setHandleCodeInApp(true)
//                        .setAndroidPackageName(
//                                "com.example.sneak_out",
//                                true, /* installIfNotAvailable */
//                                "12"    /* minimumVersion */)
//                        .build();
//
//        authrzn.sendSignInLinkToEmail(regEmail.getEditText().getText().toString(),actionCodeSettings).addOnCompleteListener(new OnCompleteListener<Void>() {
//            @Override
//            public void onComplete(@NonNull Task<Void> task) {
//                if (task.isSuccessful()){
//                    Toast.makeText(SignUp.this,"Please click on the verification link, sent to your e-mail id",Toast.LENGTH_LONG).show();
//                }else {
//                    Toast.makeText(SignUp.this,task.getException().getMessage(),Toast.LENGTH_LONG).show();
//                }
//            }
//        });
//
//    }
}