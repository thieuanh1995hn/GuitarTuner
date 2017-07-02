package anhtd.xda.edu.mylndynhcc.view;


import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.util.AttributeSet;

import anhtd.xda.edu.mylndynhcc.model.Note;
import anhtd.xda.edu.mylndynhcc.R;
import anhtd.xda.edu.mylndynhcc.model.TunerUpdate;

import be.tarsos.dsp.pitch.PitchDetectionResult;


public class DirectionPointerView extends CircleImageView implements TunerUpdate {
    //Should keep this in sync with TunerView.DEFAULT_WIGGLE_ROOM
    public static final int DEFAULT_WIGGLE_ROOM = 1;
    private Drawable check;
    private Drawable up;
    private Drawable down;

    public DirectionPointerView(Context context){
        super(context);
        init(context, null);
    }

    public DirectionPointerView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context, attrs);
    }

    public DirectionPointerView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    @SuppressWarnings("deprecation")
    private void init(Context context, AttributeSet attrs){
        //set default fill color

          //  setFillColor(context.getResources().getColor(R.color.white));

        if(up == null){

                up = context.getResources().getDrawable(R.drawable.up);

        }
        if(down == null){

                down = context.getResources().getDrawable(R.drawable.down);

        }
        if(check == null){

                check = context.getResources().getDrawable(R.drawable.check);

        }
    }





    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);
        int wMode = MeasureSpec.getMode(widthMeasureSpec);
        int hMode = MeasureSpec.getMode(heightMeasureSpec);

        if(wMode == MeasureSpec.UNSPECIFIED || hMode == MeasureSpec.UNSPECIFIED
                || wMode == MeasureSpec.AT_MOST || hMode == MeasureSpec.AT_MOST){
            if(width >= height){
                width = height / 3;
                height = height / 3;
            }else{
                width = width / 3;
                height = width / 3;
            }
            setMeasuredDimension(width, height);
        }else{

            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

    @Override
    public void updateNote(Note newNote, PitchDetectionResult result) {

        boolean acceptable = false;
        if(newNote.getActualFrequency() < newNote.getFrequency()){
            if(newNote.getActualFrequency() + DEFAULT_WIGGLE_ROOM >= newNote.getFrequency()){
                setImageDrawable(check);
                acceptable = true;
            }
        }else{
            if(newNote.getActualFrequency() - DEFAULT_WIGGLE_ROOM <= newNote.getFrequency()){
                setImageDrawable(check);
                acceptable= true;
            }
        }
        if(!acceptable){
            if(newNote.getActualFrequency() < newNote.getFrequency()){
                setImageDrawable(up);
            }else{
                setImageDrawable(down);
            }
        }
    }

}
