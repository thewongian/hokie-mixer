package com.example.hokiemixer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.DragEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.Spinner;

public class EditSongActivity extends AppCompatActivity implements View.OnClickListener {

    Spinner song, effect1, effect2, effect3;
    SeekBar seek1, seek2, seek3;
    Button play;
    private int[] progress = {0, 0, 0};
    private int[] selection = {0, 0, 0, 0};

    //keys
    public final static String TITLE_KEY = "title";
    public final static String INDEX_KEY = "song_index";
    public final static String EFFECT1_KEY = "effect1";
    public final static String EFFECT2_KEY = "effect2";
    public final static String EFFECT3_KEY = "effect3";
    public final static String PERCENT1_KEY = "percent1";
    public final static String PERCENT2_KEY = "percent2";
    public final static String PERCENT3_KEY = "percent3";
    /**
     * run when created
     * @param savedInstanceState
     * state it was saved in
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        song = (Spinner) findViewById(R.id.song);
        effect1 = (Spinner) findViewById(R.id.effect1);
        effect2 = (Spinner) findViewById(R.id.effect2);
        effect3 = (Spinner) findViewById(R.id.effect3);

        ArrayAdapter<String> songAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, MusicPlayer.MUSIC_NAME);
        songAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        song.setAdapter(songAdapter);

        ArrayAdapter<String> effectAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, EffectPlayer.EFFECT_NAME);
        songAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        effect1.setAdapter(effectAdapter);
        effect2.setAdapter(effectAdapter);
        effect3.setAdapter(effectAdapter);

        song.setSelection(selection[0]);
        effect1.setSelection(selection[1]);
        effect2.setSelection(selection[2]);
        effect3.setSelection(selection[3]);




        seek1 = (SeekBar) findViewById(R.id.effect1seek);
        seek2 = (SeekBar) findViewById(R.id.effect2seek);
        seek3 = (SeekBar) findViewById(R.id.effect3seek);

        seek1.setProgress(progress[0]);
        seek2.setProgress(progress[1]);
        seek3.setProgress(progress[2]);

        play = (Button) findViewById(R.id.play);


    }
    @Override
    protected void onPause() {
        super.onPause();
        rememberChoices();
    }

    private void rememberChoices() {
        selection[0] = song.getSelectedItemPosition();
        selection[1] = effect1.getSelectedItemPosition();
        selection[2] = effect2.getSelectedItemPosition();
        selection[3] = effect3.getSelectedItemPosition();
    }
    /**
     * on click method for play button and sliders
     * @param v
     * view clicked
     */
    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.play) {
            rememberChoices();

            Intent intent = new Intent(this, PlayActivity.class);
            intent.putExtra(this.TITLE_KEY, song.getSelectedItem().toString());
            intent.putExtra(this.INDEX_KEY, selection[0]);
            intent.putExtra(this.EFFECT1_KEY, selection[1]);
            intent.putExtra(this.EFFECT2_KEY, selection[2]);
            intent.putExtra(this.EFFECT3_KEY, selection[3]);
            intent.putExtra(this.PERCENT1_KEY, getProgressPercentage(seek1));
            intent.putExtra(this.PERCENT2_KEY, getProgressPercentage(seek2));
            intent.putExtra(this.PERCENT3_KEY, getProgressPercentage(seek3));
            startActivity(intent);
        }
        else if (v.getId() == R.id.effect1seek) {
            progress[0] = seek1.getProgress();
        }
        else if (v.getId() == R.id.effect2seek) {
            progress[1] = seek2.getProgress();
        }
        else if (v.getId() == R.id.effect3seek) {
            progress[2] = seek3.getProgress();
        }
    }

    /**
     * Calculates progress of bar in percentage
     * @param bar
     * bar that we're trying to find percentage of
     * @return percentage of progress in int
     */
    public float getProgressPercentage(SeekBar bar) {

        int max = bar.getMax();
        int progress = bar.getProgress();

        float percent = (float) progress / (float) max;

        return percent;

    }



}