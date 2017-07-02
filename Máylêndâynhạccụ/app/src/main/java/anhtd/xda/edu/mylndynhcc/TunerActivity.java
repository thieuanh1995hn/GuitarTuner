package anhtd.xda.edu.mylndynhcc;

import android.Manifest;
import android.animation.Animator;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;

import java.util.Calendar;

import anhtd.xda.edu.mylndynhcc.model.Note;
import anhtd.xda.edu.mylndynhcc.util.PermissionUtils;


public class TunerActivity extends AppCompatActivity {
    private Toolbar toolbar;
    private TunerFragment tunerFragment;
    private PitchFragment pitchFragment;
    private boolean showCancel;
    private AdView mAdView;
    public static final int AUDIO_PERMISSION_REQUEST_CODE = 4;

    @Override
    public void onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        showCancel = false;
        setContentView(R.layout.tuner_activity);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        tunerFragment = new TunerFragment();
        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, tunerFragment, TunerFragment.TAG).commit();
                if(!PermissionUtils.hasPermission(this, Manifest.permission.RECORD_AUDIO)){
           PermissionUtils.requestPermissions(this, new String[]{Manifest.permission.RECORD_AUDIO}, AUDIO_PERMISSION_REQUEST_CODE);
        }
        mAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder()
                .addTestDevice(AdRequest.DEVICE_ID_EMULATOR)
                .build();
        mAdView.loadAd(adRequest);
    }

    @Override
    public void onResume(){
        super.onResume();
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);
    }

    @Override
    protected void onStop() {
        setTimedNotification();
        super.onStop();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        ActionBar actionBar = getSupportActionBar();
        if(showCancel) {
            actionBar.setDisplayHomeAsUpEnabled(true);
        }else{
            actionBar.setDisplayHomeAsUpEnabled(false);
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                if(showCancel){
                    transitionBackToTunerFragment(pitchFragment.unreveal(pitchFragment.getX(), pitchFragment.getY()));
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        if(showCancel){
            transitionBackToTunerFragment(pitchFragment.unreveal(pitchFragment.getX(), pitchFragment.getY()));
        }else{
            super.onBackPressed();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults){
        switch(requestCode){
            case TunerFragment.AUDIO_PERMISSION_REQUEST_CODE:
                if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    if(tunerFragment != null) {
                        tunerFragment.init();
                    }
                }else if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_DENIED){
                    Toast.makeText(TunerActivity.this, "Máy lên dây cần được kết nối với microphone", Toast.LENGTH_LONG).show();
                    TunerActivity.this.finish();
                }
                break;
        }
    }

    public void transitionToPitchFragment(Note note, float x, float y){
        if(!showCancel) {
            pitchFragment = PitchFragment.newInstance(note, x, y);
            getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, pitchFragment, PitchFragment.TAG).commit();
            tunerFragment.stop();
            showCancel = true;
            invalidateOptionsMenu();
        }
    }

    public void transitionBackToTunerFragment(Animator anim){
        if(anim != null){
            anim.addListener(new Animator.AnimatorListener() {
                @Override
                public void onAnimationStart(Animator animation) {
                }

                @Override
                public void onAnimationEnd(Animator animation) {
                    getSupportFragmentManager().beginTransaction().remove(pitchFragment).commit();
                    getSupportFragmentManager().executePendingTransactions();
                    tunerFragment.start();
                    showCancel = false;
                    invalidateOptionsMenu();
                }

                @Override
                public void onAnimationCancel(Animator animation) {
                }

                @Override
                public void onAnimationRepeat(Animator animation) {
                }
            });
            anim.start();
        }else{
            getSupportFragmentManager().beginTransaction().remove(pitchFragment).commit();
            tunerFragment.start();
            showCancel = false;
            invalidateOptionsMenu();
        }
    }

    private void setTimedNotification(){
        //Send a notification to the user reminding them to tune their guitar if they haven't opened the app in awhile
        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, NotificationPublishReceiver.class);
        PendingIntent alarmIntent = PendingIntent.getBroadcast(this, NotificationPublishReceiver.REQUEST_CODE, intent, 0);
        //Cancel any previously set alarms
        //alarmManager.cancel(alarmIntent);
        //One week - 7 days, 24 hours, 60 minutes, 60 seconds, 1000 milliseconds
        long time = Calendar.getInstance().getTimeInMillis() + 1000 /*(7 * 24 * 60 * 60 * 1000)*/;
        alarmManager.set(AlarmManager.RTC, time, alarmIntent);
    }

}
