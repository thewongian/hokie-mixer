package com.example.hokiemixer;

import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.IBinder;

import androidx.annotation.Nullable;

import com.example.hokiemixer.MusicPlayer;

/**
 *
 */
public class MusicService extends Service {

    MusicPlayer musicPlayer;
    EffectPlayer effectPlayer;
    private final IBinder iBinder= new MyBinder();
    private EffectAsyncTask asyncTask;
    public static final String COMPLETE_INTENT = "complete intent";
    public static final String MUSICNAME = "music name";
    int effectIndex = 0;
    private boolean running;

    @Override
    public void onCreate() {
        super.onCreate();
        musicPlayer = new MusicPlayer(this);
        asyncTask = new EffectAsyncTask();

    }

    public void setEffectPlayer(EffectPlayer player) {
        effectPlayer = player;
    }
    public void startMusic(){

        musicPlayer.playMusic();
    }

    public void pauseMusic(){

        musicPlayer.pauseMusic();
    }

    public void resumeMusic(){

        musicPlayer.resumeMusic();
    }

    public int getPlayingStatus(){

        return musicPlayer.getMusicStatus();
    }
    public void setMusicIndex(int i) {

        musicPlayer.setMusicIndex(i);
    }
    public void restartMusic() {

        musicPlayer.restartMusic();
    }
    public void stopMusic() {
        musicPlayer.stopMusic();
    }
    public void onUpdateMusicName(String musicname) {
        Intent intent = new Intent(COMPLETE_INTENT);
        intent.putExtra(MUSICNAME, musicname);
        sendBroadcast(intent);
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {

        return iBinder;
    }


    public class MyBinder extends Binder {

        MusicService getService(){
            return MusicService.this;
        }
    }

    private class EffectAsyncTask extends AsyncTask<Void, Integer, Void> {

        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);

        }
        @Override
        protected Void doInBackground(Void... voids) {
            int maxDuration = musicPlayer.player.getDuration();
            int position = musicPlayer.player.getCurrentPosition();

            int effectTime = (int) (effectPlayer.getPercentage()[effectIndex] * maxDuration);
            if (position > effectTime) {
                publishProgress(effectPlayer.getPath(effectIndex));
                effectIndex++;

            }
            try {
                Thread.sleep(500);
            }
            catch (Exception e) {
                System.out.println(e);
            }
            return null;
        }
    }
}

