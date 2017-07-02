package anhtd.xda.edu.mylndynhcc;

import android.animation.Animator;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.ViewGroup;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.TextView;

import anhtd.xda.edu.mylndynhcc.model.Note;
import anhtd.xda.edu.mylndynhcc.model.PitchPlayer;


public class PitchFragment extends Fragment {
    public static final String TAG = PitchFragment.class.getSimpleName();
    private PitchPlayer player;
    private View viewRoot;
    private TextView text;
    private TextView text2;
    private Note note;
    //for circular reveal
    private float x;
    private float y;


    public static PitchFragment newInstance(Note note, float x, float y){
        PitchFragment fragment = new PitchFragment();
        Bundle args = new Bundle();
        args.putSerializable("note", note);
        args.putFloat("x", x);
        args.putFloat("y", y);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle args = getArguments();
        note = (Note) args.getSerializable("note");
        x = args.getFloat("x", -1);
        y = args.getFloat("y", -1);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
        player = new PitchPlayer();
        player.play(note.getFrequency());
        viewRoot = inflater.inflate(R.layout.pitch_fragment, parent, false);
        text = (TextView) viewRoot.findViewById(R.id.note);
        text2=(TextView) viewRoot.findViewById(R.id.hz);
        if (note != null) {
            text.setText(note.getNote()+note.getPosition());
            text2.setText(String.valueOf(note.getFrequency())+" Hz");
        }
        viewRoot.addOnLayoutChangeListener(new View.OnLayoutChangeListener() {
            @Override
            public void onLayoutChange(View v, int left, int top, int right, int bottom, int oldLeft, int oldTop,
                                       int oldRight, int oldBottom) {
                v.removeOnLayoutChangeListener(this);
                circularReveal(x, y);
            }
        });
        viewRoot.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(getActivity() instanceof TunerActivity){

                        ((TunerActivity) getActivity()).transitionBackToTunerFragment(unreveal(event.getX(), event.getY()));

                    player.stop();
                }
                return true;
            }
        });
        return viewRoot;
    }

    @Override
    public void onPause(){
        super.onPause();
        if(player != null){
            player.stop();
        }
    }

    public Note getNote() {
        return note;
    }

    public void circularReveal(final float x, final float y) {
        if (x != -1 && y != -1) {
            float finalRadius = (float) Math.hypot(viewRoot.getWidth(), viewRoot.getHeight());
            Animator anim = ViewAnimationUtils.createCircularReveal(viewRoot, (int) x, (int) y, 0, finalRadius);
            anim.setInterpolator(new DecelerateInterpolator(2f));
            anim.setDuration(1000);
            anim.start();
        }
    }

    public Animator unreveal(final float x, final float y){
        Animator anim = ViewAnimationUtils.createCircularReveal(getView(), (int) x, (int) y,
                (float) Math.hypot(viewRoot.getWidth(), viewRoot.getHeight()), 0);
        anim.setInterpolator(new AccelerateInterpolator(0.5f));
        anim.setDuration(500);
        return anim;
    }

    public float getX() {
        return x;
    }

    public void setX(float x) {
        this.x = x;
    }

    public float getY() {
        return y;
    }

    public void setY(float y) {
        this.y = y;
    }

}
