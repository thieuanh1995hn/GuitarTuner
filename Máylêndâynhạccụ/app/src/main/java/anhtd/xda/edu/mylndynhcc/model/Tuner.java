package anhtd.xda.edu.mylndynhcc.model;

import android.media.AudioFormat;
import android.media.AudioRecord;
import android.media.MediaRecorder;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Looper;

import be.tarsos.dsp.pitch.PitchDetectionResult;
import be.tarsos.dsp.pitch.Yin;


public class Tuner {
    private static final String TAG = Tuner.class.getSimpleName();
    private TunerUpdate view;
    private int sampleRate;
    private int bufferSize;
    private  int readSize;
    private  int isRead;
    private  float[] buffer;
    private  short[] intermediaryBuffer;
    private AudioRecord audioRecord;
    private Yin yin;
    private  Note currentNote;
    private PitchDetectionResult result;
    private  boolean isRecording;
    private  Handler handler;
    private Thread thread;
    public static final int SAMPLE_RATES =  48000;
    private AsyncTask asyncTask;

    //provide the tuner view implementing the TunerUpdate to the constructor
    public Tuner(TunerUpdate view){
        this.view = view;
        init();
    }

    public void init(){
        this.sampleRate = SAMPLE_RATES;
        this.bufferSize = AudioRecord.getMinBufferSize(sampleRate, AudioFormat.CHANNEL_IN_DEFAULT, AudioFormat.ENCODING_PCM_16BIT);
        this.readSize = bufferSize / 4;
        this.buffer = new float[readSize];
        this.intermediaryBuffer = new short[readSize];
        this.isRecording = false;
        this.audioRecord = new AudioRecord(MediaRecorder.AudioSource.DEFAULT, sampleRate, AudioFormat.CHANNEL_IN_DEFAULT,
                AudioFormat.ENCODING_PCM_16BIT, bufferSize);
        this.yin = new Yin(sampleRate, readSize);
        this.currentNote = new Note(Note.DEFAULT_FREQUENCY);
        this.handler = new Handler(Looper.getMainLooper());
    }

    public void start(){
        if(audioRecord != null) {
            isRecording = true;
            audioRecord.startRecording();
//            asyncTask = new AsyncTask() {
//                @Override
//                protected Object doInBackground(Object[] params) {
//                    return null;
//                }
//            };
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    findNote();
                }
            }, "Tuner Thread");
            thread.start();

        }
    }

    private void findNote(){
        while(isRecording){
            isRead = audioRecord.read(intermediaryBuffer, 0, readSize);
            buffer = shortArrayToFloatArray(intermediaryBuffer);
            result = yin.getPitch(buffer);
            currentNote.changeTo(result.getPitch());
            handler.post(new Runnable() {
                @Override
                public void run() {
                    //Runs on the UI thread
                    view.updateNote(currentNote, result);
                }
            });
        }
    }

    private float[] shortArrayToFloatArray(short[] array){
        float[] fArray = new float[array.length];
        for(int i = 0; i < array.length; i++){
            fArray[i] = (float) array[i];
        }
        return fArray;
    }

    public void stop(){
        isRecording = false;
        if(audioRecord != null) {
            audioRecord.stop();
        }
    }

    public void release(){
        isRecording = false;
        if(audioRecord != null) {
            audioRecord.release();
        }
    }

    public boolean isInitialized(){
        if(audioRecord != null){
            return true;
        }
        return false;
    }

}
