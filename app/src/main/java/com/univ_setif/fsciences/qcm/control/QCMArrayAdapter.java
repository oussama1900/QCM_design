package com.univ_setif.fsciences.qcm.control;

import android.content.Context;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.univ_setif.fsciences.qcm.R;
import com.univ_setif.fsciences.qcm.entity.Answer;
import com.univ_setif.fsciences.qcm.entity.QCM;

import java.util.List;

/**
 * Created by hzerrad on 24-Mar-18.
 */

public class QCMArrayAdapter extends ArrayAdapter<QCM> {
    private Context mContext;
    private List<QCM> mQCM;

    public QCMArrayAdapter(@NonNull Context context, int resource, @NonNull List<QCM> objects) {
        super(context, resource, objects);
        mContext = context;
        mQCM = objects;
    }

    @Override
    public int getCount() {
        if(mQCM != null)
            return mQCM.size();
        else
            return -1;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View listItem = inflater.inflate(R.layout.listview_line, parent, false);

        TextView questionText = (TextView) listItem.findViewById(R.id.qText);
        TextView answerText1  = (TextView) listItem.findViewById(R.id.aText1);
        TextView answerText2  = (TextView) listItem.findViewById(R.id.aText2);
        TextView answerText3  = (TextView) listItem.findViewById(R.id.aText3);
        TextView answerText4  = (TextView) listItem.findViewById(R.id.answerText4);

    if(mQCM.get(position) != null) {
        questionText.setText(mQCM.get(position).getQuestion().getText());

        answerText1.setText(mQCM.get(position).getAns1().getText());
        if(mQCM.get(position).getQuestion().getAnswers().contains(mQCM.get(position).getAns1())) {
            answerText1.setTextColor(listItem.getResources().getColor(R.color.colorPrimaryDark));
            answerText1.setTypeface(null, Typeface.BOLD);
        }

        answerText2.setText(mQCM.get(position).getAns2().getText());
        if(mQCM.get(position).getQuestion().getAnswers().contains(mQCM.get(position).getAns2())) {
            answerText2.setTextColor(listItem.getResources().getColor(R.color.colorPrimaryDark));
            answerText2.setTypeface(null, Typeface.BOLD);
        }

        answerText3.setText(mQCM.get(position).getAns3().getText());
        if(mQCM.get(position).getQuestion().getAnswers().contains(mQCM.get(position).getAns3())) {
            answerText3.setTextColor(listItem.getResources().getColor(R.color.colorPrimaryDark));
            answerText3.setTypeface(null, Typeface.BOLD);
        }

        answerText4.setText(mQCM.get(position).getAns4().getText());
        if(mQCM.get(position).getQuestion().getAnswers().contains(mQCM.get(position).getAns4())) {
            answerText4.setTextColor(listItem.getResources().getColor(R.color.colorPrimaryDark));
            answerText4.setTypeface(null, Typeface.BOLD);
        }
    }

        return listItem;
    }
}
