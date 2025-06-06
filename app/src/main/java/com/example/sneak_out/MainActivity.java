package com.example.sneak_out;


import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.app.ActivityOptions;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    Animation topAnim, bottomAnim;
    ImageView image, logo;
    long aniduration = 2000, alduration = 5000;

//    adding following lines in AndroidManifest.xml if required
//    android:hardwareAccelerated="false"
//    android:largeHeap="true"

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main);

        //Animation
        topAnim = AnimationUtils.loadAnimation(this,R.anim.top_animation);
        bottomAnim = AnimationUtils.loadAnimation(this,R.anim.bottom_animation);

        //Hooks
        image = findViewById(R.id.logowotext);
        logo = findViewById(R.id.logotext);

        image.setAnimation(topAnim);
        logo.setAnimation(bottomAnim);

        int splash_Screen = 2000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                ObjectAnimator roAnim = ObjectAnimator.ofFloat(image, "rotation",0f,360f);
                roAnim.setDuration(aniduration);

                ObjectAnimator alAnim = ObjectAnimator.ofFloat(image, View.ALPHA,1.0f,0.3f);
                alAnim.setDuration(alduration);

                ObjectAnimator altexAnim = ObjectAnimator.ofFloat(logo, View.ALPHA,1.0f,0.3f);
                altexAnim.setDuration(alduration);

                AnimatorSet animatorSet = new AnimatorSet();
                animatorSet.playTogether(roAnim,alAnim,altexAnim);
                animatorSet.start();
            }
        }, splash_Screen);

        int next_Screen = 5000;
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(MainActivity.this,Login.class);
                Pair[] pairs = new Pair[2];
                pairs[0]= new Pair<View, String>(image,"splashlogoimage");
                pairs[1]= new Pair<View, String>(logo,"splashtextimage");

                ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(MainActivity.this,pairs);
                startActivity(intent,options.toBundle());
                finish();
            }
        }, next_Screen);

    }
}