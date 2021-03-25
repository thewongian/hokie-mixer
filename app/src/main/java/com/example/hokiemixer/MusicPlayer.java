package com.example.hokiemixer;

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

    /**
     * Constructor
     * @param service
     */
    public MusicPlayer(MusicService service) {

        this.musicService = service;
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
    }
    public void resumeMusic() {
        if (player != null) {
            player.seekTo(currentPosition);
            player.start();
            musicStatus = 1;
        }
    }


    public void restartMusic() {
        player.release();
        player = null;
        playMusic();

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
        player.release();
        player = null;
    }





}
