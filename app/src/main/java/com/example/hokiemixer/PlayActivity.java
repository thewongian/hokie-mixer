package com.example.hokiemixer;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

public class PlayActivity extends AppCompatActivity implements View.OnClickListener {
    TextView currentSong;
    Button play, restart;
    ImageView image;
    EffectPlayer effectPlayer;
    MusicService musicService;
    Intent startMusicServiceIntent;
    MusicCompletionReceiver musicCompletionReceiver;
    boolean isInitialized = false;
    boolean isPlaying = false;
    boolean isBound = false;
    int currentSongIndex;
    boolean differentEffect;

    public static final String INITIALIZE_STATUS = "intialization status";
    public static final String MUSIC_PLAYING = "music playing";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_play);

        currentSong = (TextView) findViewById(R.id.currentSong);

        play = (Button) findViewById(R.id.play_pause);
        play.setOnClickListener(this);
        restart = (Button) findViewById(R.id.restart);
        restart.setOnClickListener(this);
        image = (ImageView) findViewById(R.id.image);


        Bundle b1 = getIntent().getExtras();
        currentSong.setText(b1.getString(EditSongActivity.TITLE_KEY));
        effectPlayer = new EffectPlayer();
        effectPlayer.setEffect(0, effectPlayer.getPath(b1.getInt(EditSongActivity.EFFECT1_KEY)));
        effectPlayer.setEffect(1, effectPlayer.getPath(b1.getInt(EditSongActivity.EFFECT2_KEY)));
        effectPlayer.setEffect(2, effectPlayer.getPath(b1.getInt(EditSongActivity.EFFECT3_KEY)));
        effectPlayer.setEffectOffset(0, b1.getFloat(EditSongActivity.PERCENT1_KEY));
        effectPlayer.setEffectOffset(1, b1.getFloat(EditSongActivity.PERCENT2_KEY));
        effectPlayer.setEffectOffset(2, b1.getFloat(EditSongActivity.PERCENT3_KEY));

        effectPlayer.sort();
        System.out.println(effectPlayer.toString());

        currentSongIndex = b1.getInt(EditSongActivity.INDEX_KEY);

        if (savedInstanceState != null) {
            isInitialized = savedInstanceState.getBoolean(INITIALIZE_STATUS);
            currentSong.setText(savedInstanceState.getString(MUSIC_PLAYING));
        }

        startMusicServiceIntent = new Intent(this, MusicService.class);

        if (!isInitialized) {
            startService(startMusicServiceIntent);
            isInitialized = true;
        }


        musicCompletionReceiver = new MusicCompletionReceiver(this);


    }


    /**
     *
     * @param v
     */
    @Override
    public void onClick(View v) {

        if (v.getId() == R.id.play_pause) {
            if (isBound) {
                switch(musicService.getPlayingStatus()) {
                    case 0:
                        musicService.setEffectPlayer(effectPlayer);
                        musicService.setMusicIndex(currentSongIndex);
                        musicService.startMusic();
                        play.setBackgroundResource(R.drawable.pause);
                        break;
                    case 1:
                        musicService.pauseMusic();
                        play.setBackgroundResource(R.drawable.play);
                        break;
                    case 2:
                        musicService.resumeMusic();
                        play.setBackgroundResource(R.drawable.pause);
                        break;
                }
            }
        }

        else if (v.getId() == R.id.restart) {
            musicService.restartMusic();

            play.setBackgroundResource(R.drawable.pause);
        }
    }

    public void updateName(String musicName) {
        currentSong.setText(musicName);
    }

    @Override
    protected void onResume() {
        super.onResume();

        if(isInitialized && !isBound){
            bindService(startMusicServiceIntent, musicServiceConnection, Context.BIND_AUTO_CREATE);
        }


        registerReceiver(musicCompletionReceiver, new IntentFilter(MusicService.COMPLETE_INTENT));
    }

    @Override
    protected void onPause() {
        super.onPause();

        if(isBound){
            unbindService(musicServiceConnection);
            isBound= false;
        }

        unregisterReceiver(musicCompletionReceiver);
    }

    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putBoolean(INITIALIZE_STATUS, isInitialized);
        outState.putString(MUSIC_PLAYING, currentSong.getText().toString());
        super.onSaveInstanceState(outState);
    }

    private ServiceConnection musicServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder iBinder) {
            MusicService.MyBinder binder = (MusicService.MyBinder) iBinder;
            musicService = binder.getService();
            isBound = true;


            switch (musicService.getPlayingStatus()) {
                case 0:
                    play.setBackgroundResource(R.drawable.play);
                    break;
                case 1:
                    play.setBackgroundResource(R.drawable.pause);
                    if (currentSongIndex != musicService.musicPlayer.getMusicIndex()) {
                        musicService.stopMusic();
                        play.setBackgroundResource(R.drawable.play);
                    }
                    break;
                case 2:
                    play.setBackgroundResource(R.drawable.play);
                    if (currentSongIndex != musicService.musicPlayer.getMusicIndex()) {
                        musicService.stopMusic();
                        play.setBackgroundResource(R.drawable.play);
                    }
                    break;
            }



        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            musicService = null;
            isBound = false;
        }
    };
}