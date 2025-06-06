package com.example.sneak_out;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.chaos.view.PinView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskExecutors;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;

import java.util.concurrent.TimeUnit;

public class VerifyOTP extends AppCompatActivity {

    Button buttclose, buttverifyotp;
    TextView txtcode, txtverify, txtotp;
    PinView boxotp;
    String noPhone,nameuser;
    String codeSys;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify_o_t_p);

        //Hooks
        buttclose = findViewById(R.id.closebutt);
        buttverifyotp = findViewById(R.id.verifyotpbutt);
        txtcode = findViewById(R.id.codetxt);
        txtverify = findViewById(R.id.veritxt);
        txtotp = findViewById(R.id.otptxt);
        boxotp = findViewById(R.id.otpbox);

        noPhone = getIntent().getStringExtra("PhoneNo");
        nameuser = getIntent().getStringExtra("username");
        sendOTP(noPhone);
        //Fix glitch for verify code button

    }

    private void sendOTP(String noPhone) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(
                noPhone,        // Phone number to verify
                60,                 // Timeout duration
                TimeUnit.SECONDS,   // Unit of timeout
                TaskExecutors.MAIN_THREAD,               // Activity (for callback binding)
                mCallbacks);        // OnVerificationStateChangedCallbacks
    }

    private PhoneAuthProvider.OnVerificationStateChangedCallbacks mCallbacks =
            new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {
                @Override
                public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                    super.onCodeSent(s, forceResendingToken);
                    codeSys = s;
                }

                @Override
                public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {
                    String cod = phoneAuthCredential.getSmsCode();
                    if (cod!=null){
                        boxotp.setText(cod);
                        verifyCode(cod);
                    }
                }

                @Override
                public void onVerificationFailed(@NonNull FirebaseException e) {
                    Toast.makeText(VerifyOTP.this,e.getMessage(),Toast.LENGTH_LONG).show();
                }
            };

    private void verifyCode(String cod) {
        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(codeSys,cod);
        signInWithPhoneAuthCredential(credential);
    }

    private void signInWithPhoneAuthCredential(PhoneAuthCredential credential) {

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(VerifyOTP.this,"Verification Completed",Toast.LENGTH_LONG).show();
                            setnewpass();
                            // Sign in success, update UI with the signed-in user's information
//                            Log.d(TAG, "signInWithCredential:success");
//
//                            FirebaseUser user = task.getResult().getUser();
//                            // ...
                        } else {
                            // Sign in failed, display a message and update the UI
//                            Toast.makeText(VerifyOTP.this,"Unsuccessful OTP verification",Toast.LENGTH_LONG).show();
                            if (task.getException() instanceof FirebaseAuthInvalidCredentialsException) {
                                // The verification code entered was invalid
                                Toast.makeText(VerifyOTP.this,"Invalid OTP",Toast.LENGTH_LONG).show();

                            }
                        }
                    }
                });
    }

    public void callLogin(View view) {
        Intent intent = new Intent(VerifyOTP.this,Login.class);
        Pair[] pairs = new Pair[6];
        pairs[0]= new Pair<View, String>(buttclose,"splashlogoimage");
        pairs[1]= new Pair<View, String>(txtcode,"splashtextimage");
        pairs[2]= new Pair<View, String>(txtverify,"signinup");
        pairs[3]= new Pair<View, String>(buttverifyotp,"buttgo");
        pairs[4]= new Pair<View, String>(txtotp,"userna");
        pairs[5]= new Pair<View, String>(boxotp,"passw");

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(VerifyOTP.this,pairs);
        startActivity(intent,options.toBundle());
        finish();
    }

    public void callSETNEW(View view) {

        String code = boxotp.getText().toString();
        if (!code.isEmpty()){
            verifyCode(code);
        }
    }

    private void setnewpass() {
        Intent intent = new Intent(VerifyOTP.this,SetNewPassword.class);
        Pair[] pairs = new Pair[6];
        pairs[0]= new Pair<View, String>(buttclose,"splashlogoimage");
        pairs[1]= new Pair<View, String>(txtcode,"splashtextimage");
        pairs[2]= new Pair<View, String>(txtverify,"signinup");
        pairs[3]= new Pair<View, String>(buttverifyotp,"buttgo");
        pairs[4]= new Pair<View, String>(txtotp,"userna");
        pairs[5]= new Pair<View, String>(boxotp,"passw");

        intent.putExtra("username", nameuser);

        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(VerifyOTP.this,pairs);
        startActivity(intent,options.toBundle());
        finish();
    }
}