package com.example.hokiemixer;

import android.content.Context;
import android.media.MediaPlayer;


/**
 * Handles the music playing of the program
 * @author Ian Wong
 * @version 2021.3.19
 */
public class MusicPlayer implements MediaPlayer.OnCompletionListener {

    MediaPlayer player;
    private int currentPosition = 0;
    private int musicIndex = 0;
    private int musicStatus = 0;
    private MusicService musicService;
    public static final int[] MUSIC_PATH = {R.raw.gotechgo, R.raw.wap, R.raw.entersandman};
    public static final String[] MUSIC_NAME = {"Go Tech Go", "WAP", "Enter Sandman"};

    private EffectListener effectListener;
    private int[] effectPositions = new int[3];
    MediaPlayer[] effectPlayers = new MediaPlayer[3];



    interface EffectListener {

        void setImage(int path);
    }
    /**
     * Constructor
     * @param service
     */
    public MusicPlayer(MusicService service) {

        this.musicService = service;
    }

    public void setEffectListener(Context context) {
        effectListener = (EffectListener) context;
    }
    public void setMusicIndex(int index) {
        musicIndex = index;
    }
    public int getMusicIndex() {
        return musicIndex;
    }
    /**
     * Plays the music
     * if in a position to resume, resumes it instead
     */
    public void playMusic() {

        player = MediaPlayer.create(this.musicService, MUSIC_PATH[musicIndex]);
        player.start();
        player.setOnCompletionListener(this);
        musicService.onUpdateMusicName(getMusicName());
        musicStatus = 1;
    }

    /**
     * Pauses the music
     * remembers where it was paused
     */
    public void pauseMusic() {
        if(player!= null && player.isPlaying()) {
            player.pause();
            currentPosition = player.getCurrentPosition();
            musicStatus = 2;

        }
        pauseEffects();
    }
    public void resumeMusic() {
        if (player != null) {
            player.seekTo(currentPosition);
            player.start();
            musicStatus = 1;
        }
        resumeEffects();
    }

    public void playEffect(int path) {
        MediaPlayer effectPlayer = MediaPlayer.create(this.musicService, path);
        effectPlayer.start();
        effectPlayer.setOnCompletionListener(this);
        int imagePath = getImagePath(path);

        effectListener.setImage(imagePath);
        int i = 0;
        while (effectPlayers[i] != null) {
            i++;
        }
        effectPlayers[i] = effectPlayer;





    }

    public int getImagePath(int effectPath) {
        switch (effectPath) {
            case R.raw.cheering:
                return R.drawable.cheeringimage;
            case R.raw.clapping:
                return R.drawable.clapping;
            case R.raw.letsgohokies:
                return R.drawable.lets_go_hokes;
            default:
                throw new NumberFormatException("Not a valid path");
        }


    }

    public void pauseEffects() {
        for (int i = 0; i < effectPlayers.length; i++) {
            if (effectPlayers[i] != null && effectPlayers[i].isPlaying()) {
                effectPlayers[i].pause();
                effectPositions[i] = effectPlayers[i].getCurrentPosition();

            }
        }
    }

    public void resumeEffects() {
        for (int i = 0; i < effectPlayers.length; i++) {
            if (effectPlayers[i] != null) {
                effectPlayers[i].seekTo(effectPositions[i]);
                effectPlayers[i].start();
            }
        }
    }

    public void restartMusic() {
        player.release();
        player = null;
        restartEffects();
        playMusic();


    }

    public void restartEffects() {
        for (int i = 0; i < effectPlayers.length; i++) {
            if (effectPlayers[i] != null) {
                effectPlayers[i].release();
                effectPlayers[i] = null;
                effectPositions[i] = 0;
            }

        }
    }
    public void stopMusic() {
        player.release();
        player = null;
        musicStatus = 0;
    }

    public int getMusicStatus() {
        return musicStatus;
    }
    public String getMusicName() {
        return MUSIC_NAME[musicIndex];
    }
    @Override
    public void onCompletion(MediaPlayer mp) {
        mp.release();

        for (int i = 0; i < effectPlayers.length; i++) {
            if (mp.equals(effectPlayers[i])) {
                effectPlayers[i] = null;
                if (i < 2 && effectPlayers[i + 1] == null) {
                    effectListener.setImage(R.drawable.excessivecelebration);
                }
                return;
            }
        }



    }






}
