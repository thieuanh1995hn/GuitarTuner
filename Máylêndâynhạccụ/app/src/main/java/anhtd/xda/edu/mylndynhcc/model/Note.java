package anhtd.xda.edu.mylndynhcc.model;


import java.io.Serializable;
import java.util.UUID;


public class Note implements Serializable{
    public static final double FREQUENCY_A_0 = 27.5000;
    public static final double FREQUENCY_PIANO_KEY_1 = FREQUENCY_A_0;
    public static final double FREQUENCY_C_8 = 4186.01;
    public static final double FREQUENCY_PIANO_KEY_88 = FREQUENCY_C_8;
    public static final double FREQUENCY_C_4 = 261.626;
    public static final double FREQUENCY_MIDDLE_C = FREQUENCY_C_4;
    public static final double FREQUENCY_C_SHARP_4 = 277.183;
    public static final double FREQUENCY_D_FLAT_4 = 277.183;
    public static final double FREQUENCY_D_4 = 293.665;
    public static final double FREQUENCY_D_SHARP_4 = 311.127;
    public static final double FREQUENCY_E_FLAT_4 = 311.127;
    public static final double FREQUENCY_E_4 = 329.628;
    public static final double FREQUENCY_F_4 = 349.228;
    public static final double FREQUENCY_F_SHARP_4 = 369.994;
    public static final double FREQUENCY_G_FLAT_4 = 369.994;
    public static final double FREQUENCY_G_4 = 391.995;
    public static final double FREQUENCY_G_SHARP_4 = 415.305;
    public static final double FREQUENCY_A_FLAT_4 = 415.305;
    public static final double FREQUENCY_A_4 = 440.000;
    public static final double FREQUENCY_A_SHARP_4 = 466.164;
    public static final double FREQUENCY_B_FLAT_4 = 466.164;
    public static final double FREQUENCY_B_4 = 493.883;
    public static final double DEFAULT_FREQUENCY = FREQUENCY_A_4;
    public static final double UNKNOWN_FREQUENCY = -1;



    public static final double[] PIANO_NOTE_FREQUENCIES = new double[]{
            27.5000, 29.1352, 30.8677, 32.7032, 34.6478, 36.7081, 38.8909, 41.2034, 43.6535, 46.2493, 48.9994, 51.9131,
            55.0000, 58.2705, 61.7354, 65.4064, 69.2957, 73.4162, 77.7817, 82.4069, 87.3071, 92.4986, 97.9989, 103.826,
            110.000, 116.541, 123.471, 130.813, 138.591, 146.832, 155.563, 164.814, 174.614, 184.997, 195.998, 207.652,
            220.000, 233.082, 246.942, 261.626, 277.183, 293.665, 311.127, 329.628, 349.228, 369.994, 391.995, 415.305,
            440.000, 466.164, 493.883, 523.251, 554.365, 587.330, 622.254, 659.255, 698.456, 739.989, 783.991, 830.609,
            880.000, 932.328, 987.767, 1046.50, 1108.73, 1174.66, 1244.51, 1318.51, 1396.91, 1479.98, 1567.98, 1661.22,
            1760.00, 1864.66, 1975.53, 2093.00, 2217.46, 2349.32, 2489.02, 2637.02, 2793.83, 2959.96, 3135.96, 3322.44,
            3520.00, 3729.31, 3951.07, 4186.01};

    public static final int UNKNOWN_POSITION = -1;

    public static final String FLAT = "\u266D";
    public static final String SHARP = "\u266F";
    public static final String C = "C";
    public static final String C_SHARP = C + SHARP;
    public static final String D = "D";
    public static final String D_FLAT = D + FLAT;
    public static final String D_SHARP = D + SHARP;
    public static final String E = "E";
    public static final String E_FLAT = E + FLAT;
    public static final String F = "F";
    public static final String F_SHARP = F + SHARP;
    public static final String G = "G";
    public static final String G_FLAT = G + FLAT;
    public static final String G_SHARP = G + SHARP;
    public static final String A = "A";
    public static final String A_FLAT = A + FLAT;
    public static final String A_SHARP = A + SHARP;
    public static final String B = "B";
    public static final String B_FLAT = B + FLAT;
    public static final String UNKNOWN_NOTE = "";
    public static final String E2="E2";
    public static final String A2="A2";
    public static final String D3="D3";
    public static final String G3="G3";
    public static final String B3="B3";
    public static final String E4="E4";




    public static final String[] C_TO_B_NOTES = new String[]{C, C_SHARP, D, D_SHARP, E, F, F_SHARP,
            G, G_SHARP, A, A_SHARP, B};
    public static final String[] OPEN_STRING_NOTES= new String[]{E2,A2,D3,G3,B3,E4};
    public static final double[] OPEN_STRING_VALUES = new double[]{82.4069,110.000,146.832,195.998,246.942,329.628};

    public static final String[] NOTES = new String[]{A, A_SHARP, B, C, C_SHARP, D, D_SHARP, E, F, F_SHARP, G, G_SHARP};


    private String note;

    private double frequency;

    private double actualFrequency;
    //like  4 in A4
    private int position;

    private double difference;
    private int index;

    public Note(double frequency){

        this.actualFrequency = frequency;
        init(frequency);
    }


    public Note(Note note){;
        this.note = note.getNote();
        this.frequency = note.getFrequency();
        this.actualFrequency = note.getActualFrequency();
        this.position = note.getPosition();
        this.difference = note.getDifference();

        this.index = note.getIndex();
    }


    public void changeTo(double frequency){
        this.actualFrequency = frequency;
        init(frequency);
    }

    private void init(double frequency){
        if(frequency < 27.5000 || frequency > 4186.01){
            this.frequency = UNKNOWN_FREQUENCY;
            this.position = UNKNOWN_POSITION;
            this.note = UNKNOWN_NOTE;
            this.index = -1;
        }else{
            this.index = searchForNote(this.actualFrequency, PIANO_NOTE_FREQUENCIES,
                    0, PIANO_NOTE_FREQUENCIES.length-1);
            this.frequency = PIANO_NOTE_FREQUENCIES[index];
            this.note = getNoteFromIndex(index);
            this.position = getPositionFromIndex(index);
        }
        this.difference = this.actualFrequency - this.frequency;
    }

    private int searchForNote(double frequency, double[] array, int minIndex, int maxIndex){
        //that we found our value and we can return it
        if(minIndex == maxIndex){
            return minIndex;
        }
        if(minIndex+1==maxIndex){
            double averageFrequency= (array[minIndex]+array[maxIndex])/2;
            if (frequency < averageFrequency) {
                   return minIndex;
                } else {
                    return maxIndex;
                }
        }

        int pivot = (minIndex + (maxIndex)) / 2;
        double midValue = array[pivot];
        if(frequency == midValue){
            return pivot;
        }else if(frequency < midValue){
            return searchForNote(frequency, array, minIndex, pivot);
        }else{
            //frequency is greater than or equal to value so search the right side of the array
            return searchForNote(frequency, array, pivot, maxIndex);
        }

//        int index=0;
//        for (int i=minIndex;i<maxIndex;i++) {
//            if (frequency == array[i]) {
//                index = i;
//            } else if (frequency < array[i + 1] && frequency > array[i]) {
//                double averageFrequency = (array[i] + array[i + 1]) / 2;
//                if (frequency < averageFrequency) {
//                    index=i;
//                } else {
//                    index=i+1;
//                }
//
//            }
//        }
//        return index;

    }


    private int getPositionFromIndex(int index){
        if(index < 3){
            return 0;
        }else if(index < 15){
            return 1;
        }else if(index < 27){
            return 2;
        }else if(index < 39){
            return 3;
        }else if(index < 51){
            return 4;
        }else if(index < 63){
            return 5;
        }else if(index < 75){
            return 6;
        }else if(index < 87){
            return 7;
        }else{
            return 8;
        }
    }


    private String getNoteFromIndex(int index){

        if(index < 12) {
            return NOTES[index];
        }else{

            return NOTES[index % 12];
        }
    }


    public String getNote() {
        return note;
    }

    public double getFrequency() {
        return frequency;
    }

    public double getActualFrequency() {
        return actualFrequency;
    }

    public int getPosition() {
        return position;
    }

    public double getDifference() {
        return difference;
    }


    public int getIndex() {
        return index;
    }


    public int getCToBNotesIndex(){
        for(int i = 0; i < C_TO_B_NOTES.length; i++){
            if(C_TO_B_NOTES[i].equals(note)){
                return i;
            }
        }
        return -1;
    }


}
