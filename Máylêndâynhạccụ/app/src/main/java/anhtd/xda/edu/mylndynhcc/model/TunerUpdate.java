package anhtd.xda.edu.mylndynhcc.model;



import be.tarsos.dsp.pitch.PitchDetectionResult;


public interface TunerUpdate {

    void updateNote(Note newNote, PitchDetectionResult result);

}
