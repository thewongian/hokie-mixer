package com.example.hokiemixer;

import android.media.MediaPlayer;
import android.os.Parcel;
import android.os.Parcelable;

import java.util.Arrays;

/**
 * Handles playing effects for the audio mixer
 * @author Ian Wong (thewongian)
 * @version 2021.3.19
 */
public class EffectPlayer implements MediaPlayer.OnCompletionListener{


    private MediaPlayer player;

    private int[] effectPath = new int[3];

    private float[] effectOffsetPercentage = new float[3];

    public static final String[] EFFECT_NAME = {"Cheering", "Clapping", "Let's Go Hokies"};

    public static final int[] EFFECT_PATHS = {R.raw.cheering, R.raw.clapping, R.raw.letsgohokies};


    /**
     *
     * @param index
     * @param id
     */
    public void setEffect(int index, int id) {
        effectPath[index] = id;
    }

    public void setEffectOffset(int index, float offset) {

        effectOffsetPercentage[index] = offset;
    }

    public int getPath(int index) {
        return EFFECT_PATHS[index];

    }

    public float[] getPercentage() {
        return effectOffsetPercentage;
    }
    //unrolled bubble sort LUL
    public void sort() {
        if (effectOffsetPercentage[0] > effectOffsetPercentage[1]) {
            float temp = effectOffsetPercentage[1];
            int tempInt = effectPath[1];

            effectOffsetPercentage[1] = effectOffsetPercentage[0];
            effectOffsetPercentage[0] = temp;

            effectPath[1] = effectPath[0];
            effectPath[0] = 1;
        }
        if (effectOffsetPercentage[1] > effectOffsetPercentage[2]) {
            float temp = effectOffsetPercentage[2];
            int tempInt = effectPath[2];

            effectOffsetPercentage[2] = effectOffsetPercentage[1];
            effectOffsetPercentage[1] = temp;

            effectPath[2] = effectPath[1];
            effectPath[1] = tempInt;
        }

        if (effectOffsetPercentage[0] > effectOffsetPercentage[1]) {
            float temp = effectOffsetPercentage[1];
            int tempInt = effectPath[1];

            effectOffsetPercentage[1] = effectOffsetPercentage[0];
            effectOffsetPercentage[0] = temp;

            effectPath[1] = effectPath[0];
            effectPath[0] = 1;
        }

    }

    public void playEffect(int path) {

    }
    @Override
    public void onCompletion(MediaPlayer mp) {
        player.release();
        player = null;
    }

    @Override
    public String toString() {
        StringBuffer buffer = new StringBuffer();
        buffer.append("Cheering: ");
        buffer.append(R.raw.cheering);
        buffer.append(" | Clapping: ");
        buffer.append(R.raw.clapping);
        buffer.append(" | Lets Go Hokies: ");
        buffer.append(R.raw.letsgohokies);
        buffer.append("\n");
        buffer.append("Effect Player\n");
        for (int i = 0; i < 3; i++) {
            buffer.append(effectPath[i]);
            buffer.append(" ");
            buffer.append(effectOffsetPercentage[i]);
            buffer.append("\n");
        }
        return buffer.toString();
    }

}
