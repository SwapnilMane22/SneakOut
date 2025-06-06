package com.example.sneak_out;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
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

public class SetNewPassword extends AppCompatActivity {

    Button butt2back, buttok;
    ImageView imgpass;
    TextView txtcred, txtset;
    FirebaseDatabase rootNode;
    DatabaseReference reference;
    TextInputLayout passnew,passconfrm;
    String txtnewpass, txtconfrmpass, userName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_new_password);

        //Hooks
        butt2back = findViewById(R.id.backbutt2);
        buttok = findViewById(R.id.okbutt);
        imgpass = findViewById(R.id.passwordimg);
        txtcred = findViewById(R.id.credtxt);
        txtset = findViewById(R.id.settxt);
        passnew = findViewById(R.id.newpass);
        passconfrm = findViewById(R.id.confrmpass);

        userName = getIntent().getStringExtra("username");
    }

    public void callOTP(View view) {
        Intent intent = new Intent(SetNewPassword.this,ForgetPassword.class);
        Pair[] pairs = new Pair[6];
        pairs[0]= new Pair<View, String>(butt2back,"splashlogoimage");
        pairs[1]= new Pair<View, String>(imgpass,"splashtextimage");
        pairs[2]= new Pair<View, String>(txtcred,"signinup");
        pairs[3]= new Pair<View, String>(buttok,"buttgo");
        pairs[4]= new Pair<View, String>(txtset,"userna");
        pairs[5]= new Pair<View, String>(passnew,"passw");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SetNewPassword.this,pairs);
        startActivity(intent,options.toBundle());
        finish();
    }

    private Boolean validatePassword(){
        txtnewpass = passnew.getEditText().getText().toString().trim();
        String passwordVal = "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])"+       //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //minimum 4 characters
                "$";

        if (txtnewpass.isEmpty()){
            passnew.setError("Field cannot be empty");
            return false;
        }else if (!txtnewpass.matches(passwordVal)){
            passnew.setError("Password must contain: 1 digit, 1 lowercase letter, 1 uppercase letter & 1 special character");
            return false;
        }else {
            passnew.setError(null);
            passnew.setErrorEnabled(false);
            return true;
        }
    }

    private Boolean validatecnfrmPassword(){
        txtconfrmpass = passconfrm.getEditText().getText().toString().trim();
        String passwordVal = "^" +
                "(?=.*[0-9])" +         //at least 1 digit
                "(?=.*[a-z])" +         //at least 1 lower case letter
                "(?=.*[A-Z])" +         //at least 1 upper case letter
                "(?=.*[a-zA-Z])"+       //any letter
                "(?=.*[@#$%^&+=])" +    //at least 1 special character
                "(?=\\S+$)" +           //no white spaces
                ".{4,}" +               //minimum 4 characters
                "$";

        if (txtconfrmpass.isEmpty()){
            passconfrm.setError("Field cannot be empty");
            return false;
        }else if (!txtconfrmpass.matches(passwordVal)){
            passconfrm.setError("Password must contain: 1 digit, 1 lowercase letter, 1 uppercase letter & 1 special character");
            return false;
        }else if (!txtconfrmpass.matches(txtnewpass)){
            passconfrm.setError("Confirmed Password doesn't match with the new password");
            return false;
        }else {
            passnew.setError(null);
            passnew.setErrorEnabled(false);
            passconfrm.setHelperText("Your Password strength is strong.");
            passconfrm.setError(null);
            passconfrm.setErrorEnabled(false);
            return true;
        }
    }

    public void callforgetSucess(View view) {
        if (!validatePassword() | !validatecnfrmPassword()){
            return;
        }
        else{
            forgetSuccess();
        }
    }

    private void forgetSuccess() {

        rootNode = FirebaseDatabase.getInstance();
        reference = rootNode.getReference("Users");
        Query checkUser = reference.orderByChild("username").equalTo(userName);
        checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()){

                    reference.child(userName).child("password").setValue(txtconfrmpass);

                    Intent intent = new Intent(SetNewPassword.this,ForgetPasswordSucess.class);
                    Pair[] pairs = new Pair[4];
                    pairs[0]= new Pair<View, String>(butt2back,"splashlogoimage");
                    pairs[1]= new Pair<View, String>(txtcred,"signinup");
                    pairs[2]= new Pair<View, String>(passnew,"passw");
                    pairs[3]= new Pair<View, String>(buttok,"buttgo");

                    ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(SetNewPassword.this,pairs);
                    startActivity(intent,options.toBundle());
                    finish();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(SetNewPassword.this, error.getMessage(), Toast.LENGTH_LONG).show();

            }
        });
    }
}
