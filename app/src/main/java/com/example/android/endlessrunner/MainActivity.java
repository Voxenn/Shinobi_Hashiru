package com.example.android.endlessrunner;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.media.MediaPlayer;
import android.net.rtp.AudioStream;
import android.os.IBinder;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Display;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.RelativeLayout;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;


public class MainActivity extends Activity
{
    private GameView gameView;
    MediaPlayer bgSong;

    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        gameView = new GameView(this);
        setContentView(gameView);
        bgSong = MediaPlayer.create(MainActivity.this, R.raw.ffxv);
        bgSong.start();
    }



    /*@Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }*/

   /* @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings)
        {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }*/
   @Override
   public void onPause()
   {
       super.onPause();
       gameView.gameLoopThread.running = false;
       bgSong.release();
       finish();
   }

}
