package com.somi.kong.strikeball;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import android.app.Activity;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends Activity {

    int []arrNumbers = new int[3];
    TextView arrTv[] = new TextView[3];
    EditText arrEdit[] = new EditText[3];
    ImageView arrImageViewsStrike[] = new ImageView[3];
    ImageView arrImageViewsBall[] = new ImageView[3];
    TextView textViewHistory;
    TextView textViewCorrect;

    ArrayAdapter adapter;
    List<String> listStr;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_main);

        //위젯들 초기화
        initWidgets();

        //게임 초기화
        startGame();

        //게임시작하기
        findViewById(R.id.buttonStart).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startGame();
            }
        });

        //답보기
        findViewById(R.id.buttonViewResult).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                visibleResult();
            }
        });

        //정답확인하기
        findViewById(R.id.buttonCheckResult).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                checkResult();
            }
        });

        // Load an ad into the AdMob banner view.
        AdView adView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .setRequestAgent("android_studio:ad_template").build();
        adView.loadAd(adRequest);
    }


    /**
     * checkResult
     *
     */
    private void checkResult() {
        int arrUserValues[] = new int[3]; //사용자 입력합
        try {
            for(int i=0;i<3;i++){
                arrUserValues[i] = Integer.parseInt(arrEdit[i].getText().toString());
            }

            int cntStrikes = 0;
            int cntBalls = 0;

            for(int i=0;i<3;i++) {
                for (int j = 0; j < 3;j++) {
                    if (arrNumbers[i] == arrUserValues[j]) {
                        if(i == j) { //strike
                            cntStrikes ++;
                        }
                        else { //ball
                            cntBalls++;
                        }

                        break;
                    }
                }
            }

            //초기화
            for(int i=0;i<3;i++) {
                arrImageViewsStrike[i].setImageResource(0);
                arrImageViewsBall[i].setImageResource(0);
            }

            //strike 갯수 만큼 그리기
            for(int i=0;i<cntStrikes;i++) {
                arrImageViewsStrike[i].setImageResource(R.drawable.ball_green);
            }
            //ball 갯수만큼 그리기
            for(int i=0;i<cntBalls;i++) {
                arrImageViewsBall[i].setImageResource(R.drawable.ball_yellow);
            }

            String value = Integer.toString(arrUserValues[0]) +","
                    + Integer.toString(arrUserValues[1]) +","
                    + Integer.toString(arrUserValues[2]) +", ("
                    + Integer.toString(cntStrikes) +"S / "
                    + Integer.toString(cntBalls)  +"B ) ";
            listStr.add(value);

            adapter.notifyDataSetChanged();
            ListView listView = (ListView) findViewById(R.id.listView1);
            listView.smoothScrollToPosition(adapter.getCount() - 1);
            textViewHistory.setText("history("+Integer.toString(adapter.getCount()) +")");

            //게임성공
            if(cntStrikes == 3){
                findViewById(R.id.layoutNumbers).setVisibility(View.GONE);
               // findViewById(R.id.layoutConfirm).setVisibility(View.GONE);
                findViewById(R.id.layoutUser).setVisibility(View.GONE);
               // findViewById(R.id.listView1).setVisibility(View.GONE);
                findViewById(R.id.buttonViewResult).setVisibility(View.GONE);
                findViewById(R.id.buttonCheckResult).setVisibility(View.GONE);
                //정답보여주기
                for(int i=0;i<3;i++) {
                    arrTv[i].setText(Integer.toString(arrNumbers[i]));
                }

                final Animation anim = AnimationUtils.loadAnimation(this, R.anim.scale_anim);

                anim.setAnimationListener(new Animation.AnimationListener() {
                    @Override
                    public void onAnimationStart(Animation animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animation animation) {
                        //textViewCorrect.setVisibility(View.VISIBLE);
                        findViewById(R.id.layoutBottom).setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onAnimationRepeat(Animation animation) {

                    }
                });

                arrTv[0].startAnimation(anim);
                arrTv[1].startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_anim));
                arrTv[2].startAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_anim));
            }
        }
        catch (NumberFormatException e){
            //e.printStackTrace();
            Toast.makeText(this, "3개 숫자 모두 입력하세요", Toast.LENGTH_SHORT).show();
        }
    }

    /**
     * visibleResult
     *
     */
    private void visibleResult() {

        //  findViewById(R.id.buttonCheckResult).setEnabled(false);
        //findViewById(R.id.buttonViewResult).setEnabled(false);
        findViewById(R.id.layoutNumbers).setVisibility(View.GONE);
        findViewById(R.id.layoutConfirm).setVisibility(View.GONE);

        for(int i=0;i<3;i++) {
            arrTv[i].setText(Integer.toString(arrNumbers[i]));
            arrEdit[i].setEnabled(false);
        }
    }

    /**
     * startGame
     *
     */
    public void startGame(){

        //랜덤하게 숫자 만들기
        Random r = new Random();
        ArrayList<Integer> arrList = new ArrayList<Integer>();

        //0~9
        for(int i=0;i<=9;i++){
            arrList.add(i);
        }

        for(int i=0;i<arrNumbers.length;i++){
            int index = r.nextInt(arrList.size());
            arrNumbers[i]=arrList.get(index);
            arrList.remove(index);
            Log.d("arrNumbers", Integer.toString(arrNumbers[i]));
        }

        //
        for(int i=0;i<3;i++) {
            arrTv[i].setText("?");
            arrEdit[i].setText("");
            arrEdit[i].setEnabled(true);
            arrImageViewsStrike[i].setImageResource(0);
            arrImageViewsBall[i].setImageResource(0);
        }

        //
        listStr.clear();
        adapter.notifyDataSetChanged();

        //
        arrEdit[0].requestFocus();

        //
        findViewById(R.id.layoutNumbers).setVisibility(View.VISIBLE);
        findViewById(R.id.layoutConfirm).setVisibility(View.VISIBLE);
        findViewById(R.id.layoutUser).setVisibility(View.VISIBLE);
        findViewById(R.id.listView1).setVisibility(View.VISIBLE);
        findViewById(R.id.layoutBottom).setVisibility(View.VISIBLE);
        findViewById(R.id.buttonViewResult).setVisibility(View.VISIBLE);
        findViewById(R.id.buttonCheckResult).setVisibility(View.VISIBLE);

        findViewById(R.id.buttonCheckResult).setEnabled(true);
        findViewById(R.id.buttonViewResult).setEnabled(true);

        //
        textViewCorrect.setVisibility(View.GONE);
        //
        textViewHistory.setText("history(0)");
    }

    /**
     * clickNumber
     *
     */
    public void clickNumber(View v){
        Button button = (Button)v;

        View currView = getCurrentFocus();

        if(currView instanceof EditText) {
             if (arrEdit[0].equals((EditText) currView)){
                arrEdit[0].setText(button.getText());
                //arrEdit[1].requestFocus();
            }
            else if (arrEdit[1].equals((EditText) currView)){
                arrEdit[1].setText(button.getText());
                 //arrEdit[2].requestFocus();
            }
            else if (arrEdit[2].equals((EditText) currView)){
                arrEdit[2].setText(button.getText());
                //arrEdit[0].requestFocus();
            }
        }
    }


    /**
     * initWigets
     *
     */
    private void initWidgets() {

        arrTv[0] = (TextView)findViewById(R.id.textView1);
        arrTv[1] = (TextView)findViewById(R.id.textView2);
        arrTv[2] = (TextView)findViewById(R.id.textView3);

        arrEdit[0]= (EditText)findViewById(R.id.editText1);
        arrEdit[1]= (EditText)findViewById(R.id.editText2);
        arrEdit[2]= (EditText)findViewById(R.id.editText3);

        arrEdit[0].setKeyListener(null);
        arrEdit[1].setKeyListener(null);
        arrEdit[2].setKeyListener(null);

        arrImageViewsStrike[0] = (ImageView)findViewById(R.id.imageViewStrike1);
        arrImageViewsStrike[1] = (ImageView)findViewById(R.id.imageViewStrike2);
        arrImageViewsStrike[2] = (ImageView)findViewById(R.id.imageViewStrike3);

        arrImageViewsBall[0] = (ImageView)findViewById(R.id.imageViewBall1);
        arrImageViewsBall[1] = (ImageView)findViewById(R.id.imageViewBall2);
        arrImageViewsBall[2] = (ImageView)findViewById(R.id.imageViewBall3);


        ListView listView = (ListView) findViewById(R.id.listView1);
        listStr = new ArrayList<String>();
        adapter = new ArrayAdapter(this, android.R.layout.simple_list_item_1, listStr);
        listView.setAdapter(adapter);

        textViewHistory=(TextView)findViewById(R.id.textViewHistory);
        textViewCorrect=(TextView)findViewById(R.id.textViewCorrect);
    }
}