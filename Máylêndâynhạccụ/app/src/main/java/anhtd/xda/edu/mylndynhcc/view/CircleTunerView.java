package anhtd.xda.edu.mylndynhcc.view;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import anhtd.xda.edu.mylndynhcc.model.Note;
import anhtd.xda.edu.mylndynhcc.model.TunerUpdate;
import be.tarsos.dsp.pitch.PitchDetectionResult;


public class CircleTunerView extends RelativeLayout implements TunerUpdate {
    private static final String TAG = CircleTunerView.class.getSimpleName();
    public static final int DEFAULT_DONUT_COLOR = Color.parseColor("#FF9800");
    public static final int DEFAULT_TEXT_COLOR = Color.parseColor("#FFFFFF");
    public static final int DEFAULT_CIRCLE_COLOR = Color.parseColor("#B0BEC5");


    private Note note;

    private DialView dial;
    private CircleView circle;
    private boolean allowAddView = false;


    public CircleTunerView(Context context){
        super(context);
        init(context, null);
    }

    public CircleTunerView(Context context, AttributeSet attrs){
        super(context, attrs);
        init(context, attrs);
    }

    public CircleTunerView(Context context, AttributeSet attrs, int defStyleAttr){
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }



    private void init(Context context, AttributeSet attrs){
        allowAddView = true;
        note = new Note(Note.DEFAULT_FREQUENCY);
        dial = new DialView(context, attrs);
        circle = new CircleView(context, attrs);

        LayoutParams params = new LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        dial.setLayoutParams(params);
        addView(dial);
        circle.setLayoutParams(params);
        addView(circle);
        allowAddView = false;

    }

    @Override
    public void updateNote(Note newNote, PitchDetectionResult result) {
        note = newNote;
        if (note.getPosition() > Note.UNKNOWN_POSITION){
            circle.setText(note.getNote() + note.getPosition());

        }
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



    //delegating methods
    public void addOnNoteSelectedListener(DialView.OnNoteSelectedListener l){
        if(dial != null){
            dial.addOnNoteSelectedListener(l);
        }
    }

    public boolean removeOnNoteSelectedListener(DialView.OnNoteSelectedListener l){
        if(dial != null){
            return dial.removeOnNoteSelectedListener(l);
        }
        return false;
    }


    /*
     * This class is used to store text and it's corresponding positions that need to be drawn to the canvas.
     */
    public static class NotePosition {
        private String note;
        private float x;
        private float y;

        public NotePosition(float x, float y, String note){
            this.x = x;
            this.y = y;
            this.note = note;
        }

        public String getNote() {
            return note;
        }

        public void setNote(String note) {
            this.note = note;
        }

        public float getX() {
            return x;
        }


        public float getY() {
            return y;
        }


    }

}
