package com.univ_setif.fsciences.qcm.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.univ_setif.fsciences.qcm.R;
import com.univ_setif.fsciences.qcm.entity.Answer;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;


public class DisplayQcm extends Fragment {

    private SwipeListener swipe;
    private int questionNumber;

    TextView qstText;

    Button [] ans;
    boolean [] pressed;
    CardView card;

    boolean isLog = false;

    ArrayList<Answer> userAnswer;
    ArrayList<Answer> correctAnswers;

    public DisplayQcm() {
        // Required empty public constructor

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("UseSparseArrays")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        questionNumber = getArguments().getInt("number");
        if(!isLog) userAnswer = new ArrayList<>();
        if(!isLog) correctAnswers = new ArrayList<>();

        View view = inflater.inflate(R.layout.fragment_display_qcm, container, false);

        card = view.findViewById(R.id.card);

        qstText     = view.findViewById(R.id.questionContent);
        ans         = new Button[4];
        pressed     = new boolean[4];

        for (boolean b: pressed) b = false;

        ans[0]      = view.findViewById(R.id.answerContent1);
        ans[1]      = view.findViewById(R.id.answerContent2);
        ans[2]      = view.findViewById(R.id.answerContent3);
        ans[3]      = view.findViewById(R.id.answerContent4);


        ans[0].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!pressed[0]) {
                    pressed[0] = true;
                    ans[0].setBackgroundResource(R.color.selectedButton);
                    userAnswer.add(new Answer(ans[0].getText().toString()));
                    swipe.onAnswer(questionNumber, userAnswer);
                }else{
                    pressed[0] = false;
                    ans[0].setBackgroundResource(android.R.drawable.btn_default);
                    swipe.onAnswer(questionNumber, userAnswer);
                    userAnswer.remove(new Answer(ans[0].getText().toString()));

                }
            }
        });

        ans[1].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!pressed[1]) {
                    pressed[1] = true;
                    ans[1].setBackgroundResource(R.color.selectedButton);
                    userAnswer.add(new Answer(ans[1].getText().toString()));
                    swipe.onAnswer(questionNumber, userAnswer);
                }else{
                    pressed[1] = false;
                    ans[1].setBackgroundResource(android.R.drawable.btn_default);
                    swipe.onAnswer(questionNumber, userAnswer);
                    userAnswer.remove(new Answer(ans[1].getText().toString()));

                }
            }
        });

        ans[2].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!pressed[2]) {
                    pressed[2] = true;
                    ans[2].setBackgroundResource(R.color.selectedButton);
                    userAnswer.add(new Answer(ans[2].getText().toString()));
                    swipe.onAnswer(questionNumber, userAnswer);
                }else{
                    pressed[2] = false;
                    ans[2].setBackgroundResource(android.R.drawable.btn_default);
                    swipe.onAnswer(questionNumber, userAnswer);
                    userAnswer.remove(new Answer(ans[2].getText().toString()));

                }
            }
        });

        ans[3].setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!pressed[3]) {
                    pressed[3] = true;
                    ans[3].setBackgroundResource(R.color.selectedButton);
                    userAnswer.add(new Answer(ans[3].getText().toString()));
                    swipe.onAnswer(questionNumber, userAnswer);

                }else{
                    pressed[3] = false;
                    ans[3].setBackgroundResource(android.R.drawable.btn_default);
                    swipe.onAnswer(questionNumber, userAnswer);
                    userAnswer.remove(new Answer(ans[3].getText().toString()));

                }
            }
        });

        qstText.setText(getArguments().getString("questionText"));
        ans[0].setText(getArguments().getString("answer1"));
        ans[1].setText(getArguments().getString("answer2"));
        ans[2].setText(getArguments().getString("answer3"));
        ans[3].setText(getArguments().getString("answer4"));

        if(isLog) {
            for (Answer a: userAnswer)
                pressed[getAnswerButtonIndex(a.getText())] = true;

            updateView();
        }

        return view;
    }

    private int getAnswerButtonIndex(String text){
        if(ans[0].getText().toString().equals(text))
            return 0;
        if(ans[1].getText().toString().equals(text))
            return 1;
        if(ans[2].getText().toString().equals(text))
            return 2;
        if(ans[3].getText().toString().equals(text))
            return 3;

        return -1;
    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if(isVisibleToUser && getView() != null)
            swipe.onSwipeIn(questionNumber);
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        Activity activity = (Activity) context;
        swipe = (SwipeListener) activity;
    }

    public void setCorrectAnswers(ArrayList<Answer> correctAnswers) {
        this.correctAnswers = correctAnswers;
    }

    public void setUserAnswer(ArrayList<Answer> userAnswer){
        this.userAnswer = userAnswer;
    }


    public void updateView() {

        boolean skipped = true;

        ans[0].setClickable(false);
        ans[1].setClickable(false);
        ans[2].setClickable(false);
        ans[3].setClickable(false);

        ans[0].setBackgroundResource(android.R.drawable.btn_default);
        ans[1].setBackgroundResource(android.R.drawable.btn_default);
        ans[2].setBackgroundResource(android.R.drawable.btn_default);
        ans[3].setBackgroundResource(android.R.drawable.btn_default);

        if(pressed[0]){
            skipped = false;
            if(!correctAnswers.contains(new Answer(ans[0].getText().toString())))
                ans[0].setBackgroundResource(R.color.wrongAnswer);
        }

        if(pressed[1]){
            skipped = false;
            if(!correctAnswers.contains(new Answer(ans[1].getText().toString())))
                ans[1].setBackgroundResource(R.color.wrongAnswer);
        }

        if(pressed[2]) {
            skipped = false;
            if (!correctAnswers.contains(new Answer(ans[2].getText().toString())))
                ans[2].setBackgroundResource(R.color.wrongAnswer);
        }

        if(pressed[3]){
            skipped = false;
            if(!correctAnswers.contains(new Answer(ans[3].getText().toString())))
                ans[3].setBackgroundResource(R.color.wrongAnswer);
        }

        if(skipped)
            card.setBackgroundResource(R.color.wrongAnswer);

        for (Answer a: correctAnswers) {
            if(a.getText().equals(ans[0].getText().toString()))
                ans[0].setBackgroundResource(R.color.correctAnswer);
            else if(a.getText().equals(ans[1].getText().toString()))
                ans[1].setBackgroundResource(R.color.correctAnswer);
            else if(a.getText().equals(ans[2].getText().toString()))
                ans[2].setBackgroundResource(R.color.correctAnswer);
            else
                ans[3].setBackgroundResource(R.color.correctAnswer);
        }
    }

    public void fromLog(){
        isLog = true;
    }

    public interface SwipeListener {
        void onSwipeIn(int position);
        void onAnswer(int position, ArrayList<Answer> answer);
    }

}
