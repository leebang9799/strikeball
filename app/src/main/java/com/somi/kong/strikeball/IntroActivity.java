package com.somi.kong.strikeball;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.app.Activity;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;

public class IntroActivity extends Activity {

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_intro);

        context = this;

        findViewById(R.id.layoutStrike).startAnimation(AnimationUtils.loadAnimation(this, R.anim.bounce_up_to_down_anim));

        LinearLayout layoutBalls =findViewById(R.id.layoutBalls);
        final Animation anim = AnimationUtils.loadAnimation(this, R.anim.bounce_down_to_up_anim);

        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
               Intent intent = new Intent(context, MainActivity.class);
                startActivity(intent);
                finish();

                //overridePendingTransition(android.R.anim.slide_in_left,android.R.anim.slide_out_right);
             }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        layoutBalls.startAnimation(anim);
     }

}
