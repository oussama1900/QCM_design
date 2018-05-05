package com.univ_setif.fsciences.qcm;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.CountDownTimer;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.bcgdv.asia.lib.ticktock.TickTockView;
import com.univ_setif.fsciences.qcm.control.AnswerCTRL;
import com.univ_setif.fsciences.qcm.control.SwipeAdapter;
import com.univ_setif.fsciences.qcm.entity.Answer;
import com.univ_setif.fsciences.qcm.entity.QCM;
import com.univ_setif.fsciences.qcm.fragments.DisplayQcm;

import java.util.ArrayList;
import java.util.Calendar;

public class SessionClock extends FragmentActivity implements DisplayQcm.SwipeListener {

    private SwipeAdapter swipeAdapter;
    private ViewPager viewPager;

    private ArrayList[] answers;
    private ArrayList<QCM> qcmList;

    TickTockView mCountDown;
    public TextView timerView;

    private int nbrQCM;
    private String date;

    private boolean finalized = false;

    //new attributes
    private long seconds, minutes;
    private int int_seconde,int_minute,hours,days;

    public static boolean isStop;



    boolean stop=false;

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_session);

        viewPager = findViewById(R.id.viewPager);
        timerView = findViewById(R.id.timer);

        if(getIntent().getStringExtra("Log") != null){
            Button evaluate = findViewById(R.id.submit);
            evaluate.setVisibility(View.GONE);

            qcmList = (ArrayList<QCM>) getIntent().getSerializableExtra("qcmList");
            answers = (ArrayList[]) getIntent().getSerializableExtra("answers");
            String elapsedTime = getIntent().getStringExtra("elapsed");
            nbrQCM = qcmList.size();

            swipeAdapter = new SwipeAdapter(getSupportFragmentManager(), Session.this, qcmList, answers);
            viewPager.setAdapter(swipeAdapter);
            viewPager.setPageMargin(40);
            viewPager.setOffscreenPageLimit(nbrQCM);

            timerView.setText(elapsedTime);
            timerView.setTypeface(null, Typeface.BOLD);
            timerView.setTextColor(getResources().getColor(android.R.color.holo_green_dark));

            finalized = true;
        }else {
            SharedPreferences sp = getSharedPreferences("adminSettings", MODE_PRIVATE);
            minutes = sp.getLong("minutes", 10);
            seconds = sp.getLong("secondes", 0);
            nbrQCM = sp.getInt("nbrQCM", 20);


            int_seconde = (int) minutes;
            int_minute = (int) seconds;


            answers = new ArrayList[nbrQCM];

            for (int i = 0; i < nbrQCM; i++)
                answers[i] = new ArrayList<Answer>();

            mCountDown = (TickTockView) findViewById(R.id.countdown);

            swipeAdapter = new SwipeAdapter(getSupportFragmentManager(), Session.this, nbrQCM);
            qcmList = swipeAdapter.getQcmList();
            viewPager.setAdapter(swipeAdapter);
            viewPager.setPageMargin(40);
            viewPager.setOffscreenPageLimit(nbrQCM);


            if (mCountDown != null) {

                mCountDown.setOnTickListener(new TickTockView.OnTickListener() {
                        @Override
                        public String getText(long timeRemaining) {

                            return toTime(timeRemaining);

                            /*
                            seconds = (timeRemaining / 1000) % 60;
                            minutes = ((timeRemaining / (1000 * 60)) % 60);
                            hours = (int) ((timeRemaining / (1000 * 60 * 60)) % 24);
                            days = (int) (timeRemaining / (1000 * 60 * 60 * 24));

                            int_seconde = (int) seconds;
                            int_minute = (int) minutes;


                            boolean hasDays = days > 0;
                            return String.format("%1$02d%4$s %2$02d%5$s %3$02d%6$s",
                                    hasDays ? days : hours,
                                    hasDays ? hours : minutes,
                                    hasDays ? minutes : seconds,
                                    hasDays ? "d" : "h",
                                    hasDays ? "h" : "m",
                                    hasDays ? "m" : "s");
                           */
                        }
                    });

                    Calendar end = Calendar.getInstance();
                    end.add(Calendar.MINUTE, int_minute);
                    end.add(Calendar.SECOND, int_seconde);

                    Calendar start = Calendar.getInstance();
                    start.add(Calendar.MINUTE, -1);

                    if (mCountDown != null) {
                        mCountDown.start(start, end);


                    }
                }


                //    timer = new SessionTimer(minutes*60*1000 + secondes*1000, 1000);
                //  timer.start();
            }
        }


    @Override
    protected void onStop() {
        super.onStop();
        if(timerView.isShown()){

            mCountDown.stop();

        }else {
            stop=true;
        }

        mCountDown.stop();


    }

    public String toTime(){
        String timeLeft;

        if(minutes < 10) timeLeft = "0";

        timeLeft = minutes + ":";

        if(seconds < 10) timeLeft += "0";

        timeLeft += seconds;

        return timeLeft;
    }

    public String toTime(long time){
        String timeString = "";
        int hours;
        int minutes;
        int seconds;

        if(time >= 3600) {
            hours = (int)time / 3600;
            timeString += hours + ":";
            minutes = (int)time % 3600;
        } else minutes = (int)time;

        seconds = (int) minutes % 60;
        minutes /= 60;

        if(minutes < 10) timeString += "0";

        timeString += minutes + ":";

        if(seconds < 10) timeString += "0";

        timeString += seconds;

        return timeString;

    }

    private void finalizeSession(){

        Button submit = findViewById(R.id.submit);


        timerView.setVisibility(View.VISIBLE);
        timerView.setText("00h "+int_minute+"m "+int_seconde+"s");
        mCountDown.stop();

        submit.setVisibility(View.GONE);


        for (int i=0; i<nbrQCM; i++) {
            DisplayQcm fragment = (DisplayQcm) swipeAdapter.getFragment(i);

            ArrayList<Answer> correctAnswer = qcmList.get(i).getQuestion().getAnswers();

            fragment.setCorrectAnswers(correctAnswer);

            fragment.updateView();
        }

        viewPager.setCurrentItem(0);
        finalized = true;
    }

    @Override
    public void onSwipeIn(int position) {
        TextView questionNumber = findViewById(R.id.showQuestionNumber);
        String display = "Question "+ position;
        questionNumber.setText(display);
    }

    @Override
    public void onAnswer(int position, ArrayList<Answer> answer){
        answers[position-1] = answer;
    }

    @Override
    public void onBackPressed() {
        if(!finalized) {
            mCountDown.stop();
            AlertDialog.Builder builder = new AlertDialog.Builder(Session.this);
            builder.setMessage("Voulez-vous vraiment retourner au menu principal? \n" +
                    "AVERTISSEMENT: Cette session sera perdue!")
                    .setCancelable(false)
                    .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    })
                    .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            resume();

                            dialogInterface.cancel();
                        }
                    });

            AlertDialog retour = builder.create();
            retour.setTitle("Retour au Menu Principale");
            retour.setCanceledOnTouchOutside(true);
            retour.show();
        }
        else
            super.onBackPressed();
    }

    private boolean hasEmpty(ArrayList[] answers){
        for (ArrayList ans:
                answers) {
            if(ans.size() == 0)
                return true;
        }

        return false;
    }

    /*Submit Button*/
    public void onSubmitClickListener(View view) {

        mCountDown.stop();

        AlertDialog.Builder confirm = new AlertDialog.Builder(Session.this);
        confirm.setCancelable(false)
                .setPositiveButton("Oui", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        dispatchForCorrection();
                        finalizeSession();
                    }
                })
                .setNegativeButton("Non", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        resume();

                        dialogInterface.cancel();
                    }
                });
        if(hasEmpty(answers))
            confirm.setMessage("Vous avez pas répondu au tout les questions proposées. Puisque le système " +
                    "d'évaluation n'est pas négative, nous vous conseillons de répondre comme mème.\n" +
                    "Etes-vous sur d'envoyer vos réponses maintenant?");
        else
            confirm.setMessage("Voulez-vous vraiment envoyer vos réponses? Vous pouvez plus modifier vos réponses dès" +
                    " ce point.");


        AlertDialog exit = confirm.create();
        exit.setCanceledOnTouchOutside(true);
        exit.setTitle("Submit");
        exit.show();

    }

    private void dispatchForCorrection(){
        AnswerCTRL answerCTRL = new AnswerCTRL(qcmList);

        @SuppressWarnings("unchecked")
        final double myNote = answerCTRL.checkAnswers(answers);

        Intent t = new Intent(Session.this, ShowCorrection.class);
        t.putExtra("minutes",minutes);
        t.putExtra("seconds",seconds);
        t.putExtra("note", myNote);
        startActivity(t);
    }

    private void resume(){
        Calendar end = Calendar.getInstance();
        end.add(Calendar.MINUTE, int_minute);
        end.add(Calendar.SECOND, int_seconde);
        mCountDown.start(end);
    }

  /*  private class SessionTimer extends CountDownTimer{

        private long initialTime;
        private long timeLeft;

        private long getInitialTime() {
            return initialTime;
        }

        private void setInitialTime(long l){
            initialTime = l;
        }

        SessionTimer(long millisInFuture, long countDownInterval) {
            super(millisInFuture, countDownInterval);
            initialTime = millisInFuture;
        }

        SessionTimer resume(){
            SessionTimer newTimer = new SessionTimer(timeLeft, 1000);
            newTimer.setInitialTime(initialTime);
            newTimer.start();
            return newTimer;
        }

        long getElapsed() {
            return initialTime - timeLeft;
        }

     /*   @Override
        public void onTick(long l) {
            timeLeft = l;
//            timerView.setText(toTime(l));
        }*/

    /*    @Override
        public void onFinish() {
            timerView.setText("Terminé!");
            timerView.setTypeface(null, Typeface.BOLD);
            timerView.setTextColor(getResources().getColor(android.R.color.holo_red_dark));

            AnswerCTRL answerCTRL = new AnswerCTRL(qcmList);

            @SuppressWarnings("unchecked")
            final double myNote = answerCTRL.checkAnswers(answers);

            Intent t = new Intent(Session.this, ShowCorrection.class);
            t.putExtra("note", myNote);
            startActivity(t);
            finalizeSession();
        }
    }*/
}