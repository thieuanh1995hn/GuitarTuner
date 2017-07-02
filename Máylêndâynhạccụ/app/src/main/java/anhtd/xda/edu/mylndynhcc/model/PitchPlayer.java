package anhtd.xda.edu.mylndynhcc.model;

import android.media.AudioFormat;
import android.media.AudioManager;
import android.media.AudioTrack;

import java.util.ArrayList;
import java.util.List;
//Lấy trên mạng
public class PitchPlayer  {
    private static final double DEFAULT_SMOOTHING_FACTOR = 0.4;
    private volatile AudioTrack track;
    private final int sampleRate = 48000;
    private volatile int bufferSize;
    private volatile int sampleCount;
    private volatile List<Double> sound = new ArrayList<>();
    private volatile boolean stopped = true;
    private volatile boolean completed = false;
    private Thread thread;

    public PitchPlayer(){

    }


    public void play(final double frequency) {
            thread = new Thread(new Runnable() {
                @Override
                public void run() {
                    playTone(frequency);
                }
            });
            thread.start();
    }

    public void stop() {
        stopped = true;
        completed = true;
        if(track != null){
            track.pause();
            track.flush();
            track.stop();
        }
    }

    private void playTone(double frequency){
        bufferSize = (int) Math.round(sampleRate / frequency);
        sampleCount = (int) ((bufferSize * frequency / sampleRate) * (sampleRate / frequency));
        sound = getTone(frequency, sampleRate);
        byte[] buffer = new byte[2 * sound.size()];
        int i = 0;
        for(double d : sound){
            short val = (short) (d * 32767);
            buffer[i++] = (byte) (val & 0x00ff);
            buffer[i++] = (byte) ((val & 0xff00) >>> 8);
        }
        track = new AudioTrack(AudioManager.STREAM_MUSIC, sampleRate, AudioFormat.CHANNEL_OUT_MONO,
                AudioFormat.ENCODING_PCM_16BIT, 2 * sound.size(), AudioTrack.MODE_STATIC);
        track.write(buffer, 0, sampleCount);
        track.reloadStaticData();
        track.setLoopPoints(0, sampleCount / 2, -1);
        track.play();
    }

    private List<Double> getTone(double frequency, double sampleRate){
        List<Double> tone = new ArrayList<>();
        for (int i = 0; i < sampleCount; i++) {
            tone.add(Math.sin(2 * Math.PI * i / (sampleRate / frequency)));
        }
        return tone;
    }

    private List<Double> karplusStrong(List<Double> noise){
        //TODO get this to work
        int sampleCount = noise.size();
        double noiseSample;
        double currentInput = 0.0;
        double lastOutput = 0.0;
        double currentOutput;
        for(int i = 0; i < sampleCount; i++){
            if(i < sampleCount){
                noiseSample = noise.get(i);
                noiseSample = (noiseSample * 0.5) + 0.5 * (2 * Math.random() - 1);
                currentInput = lowPassFilter(currentInput, noiseSample, DEFAULT_SMOOTHING_FACTOR);
            }else{
                currentInput = noise.get(i - sampleCount);
            }
            currentOutput = lowPassFilter(lastOutput, currentInput, DEFAULT_SMOOTHING_FACTOR);
            noise.add(currentOutput);
            lastOutput = currentOutput;
        }
        return noise;
    }

    private List<Double> generateNoise(int samples){
        List<Double> buffer = new ArrayList<>();
        int i;
        for(i = 0; i < samples; i++){
            buffer.add(2 * Math.random() - 1);
        }
        return buffer;
    }

    private double lowPassFilter(double lastOutput, double currentInput, double smoothingFactor){
        return smoothingFactor * currentInput + (1.0 - smoothingFactor) * lastOutput;
    }

}
