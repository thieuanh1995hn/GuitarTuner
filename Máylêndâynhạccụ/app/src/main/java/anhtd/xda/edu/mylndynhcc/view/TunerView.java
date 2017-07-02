package anhtd.xda.edu.mylndynhcc.view;

import android.content.Context;
import android.graphics.Color;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.style.ForegroundColorSpan;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import anhtd.xda.edu.mylndynhcc.model.Note;
import anhtd.xda.edu.mylndynhcc.model.TunerUpdate;
import be.tarsos.dsp.pitch.PitchDetectionResult;


import java.text.DecimalFormat;


public class TunerView extends LinearLayout implements TunerUpdate{

    public static final int ORANGE = Color.parseColor("#FF9800");
    public static final int GRAY = Color.parseColor("#B0BEC5");

    private Note note;
    private PitchDetectionResult result;
    private boolean allowAddView;

    private CircleTunerView circleTunerView;
    private DirectionPointerView directionPointerView;
    private TextView fTextView;


    private SpannableStringBuilder sb;

    public TunerView(Context context){
        super(context);
        init(context, null);
    }

    public TunerView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context, attrs);
    }

    public TunerView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }



    private void init(Context context, AttributeSet attrs){
        allowAddView = true;
        setOrientation(VERTICAL);
        sb = new SpannableStringBuilder();
        circleTunerView = new CircleTunerView(context, attrs);
        directionPointerView = new DirectionPointerView(context, attrs);
        fTextView = new TextView(context, attrs);

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        params.gravity = Gravity.CENTER_HORIZONTAL;
        circleTunerView.setLayoutParams(params);
        fTextView.setLayoutParams(params);

        LayoutParams dParams = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        dParams.gravity = Gravity.CENTER_HORIZONTAL;
        dParams.setMargins(0, dpToPx(16), 0, dpToPx(16));
        directionPointerView.setLayoutParams(dParams);
        fTextView.setTextColor(GRAY);
        fTextView.setTextSize(16);
        addView(circleTunerView);
        addView(directionPointerView);
        addView(fTextView);

        allowAddView = false;
    }

    private int dpToPx(int dp){
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, getContext().getResources().getDisplayMetrics());
    }

    @Override
    public void updateNote(Note newNote, PitchDetectionResult result) {
        this.note = newNote;
        this.result = result;
        circleTunerView.updateNote(newNote, result);
        directionPointerView.updateNote(newNote, result);
        sb.clear();
        sb.clearSpans();
        String aFreq = String.valueOf(new DecimalFormat("######.##").format(note.getActualFrequency()));
        Double diffFrequency=note.getActualFrequency()-note.getFrequency();
        String diff =(diffFrequency<=0)?String.valueOf(new DecimalFormat("######.##").format(diffFrequency)):String.valueOf(new DecimalFormat("+######.##").format(diffFrequency));
        if (note.getActualFrequency()>Note.UNKNOWN_FREQUENCY) {
            sb.append(aFreq + " HZ"+"("+diff+")");
            sb.setSpan(new ForegroundColorSpan(ORANGE), 0, aFreq.length() +5+diff.length(), Spannable.SPAN_INCLUSIVE_EXCLUSIVE);
        }

        fTextView.setText(sb);
    }

    @Override
    public void addView(View child, int index, ViewGroup.LayoutParams params){
        if(allowAddView){
            super.addView(child, index, params);
        }
    }

    @Override
    public void addView(View child, ViewGroup.LayoutParams params){
        if(allowAddView){
            super.addView(child, params);
        }
    }

    @Override
    public void addView(View child, int index){
        if(allowAddView){
            super.addView(child, index);
        }
    }

    @Override
    public void addView(View child){
        if(allowAddView){
            super.addView(child);
        }
    }

    @Override
    public void addView(View child, int width, int height){
        if(allowAddView){
            super.addView(child, width, height);
        }
    }


    public void addOnNoteSelectedListener(DialView.OnNoteSelectedListener l){
        if(circleTunerView != null){
            circleTunerView.addOnNoteSelectedListener(l);
        }
    }


}
