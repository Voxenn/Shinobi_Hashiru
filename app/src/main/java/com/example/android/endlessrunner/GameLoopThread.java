package com.example.android.endlessrunner;

import android.graphics.Canvas;
/**
 * Created by Vox on 1/19/2016.
 */
public class GameLoopThread extends Thread
{
    private GameView view;
    private static final int MAX_FPS = 60; //Desired FPS
    private static final int MAX_FRAME_SKIPS = 5;//max number of frames allowed to be skipped
    private final static int FRAME_PERIOD = 1000 / MAX_FPS;

    boolean running;

    public GameLoopThread(GameView view){
        this.view = view;
    }

    public void setRunning(boolean run){
        running = true;
    }

    @Override
    public void run()
    {
        Canvas canvas;
        long beginTime;         //the time when the cycle begun
        long timeDiff;          //the time it took for the cycle to execute
        int sleepTime = 0;          //ms to sleep(<0 if we're behind)
        int framesSkipped;      //number of frames being skipped


//        long ticksPS = 1000/FPS;
//        long startTime = 0;
//        long sleepTime;
        while(running)
        {
            canvas = null;
            //try to lock the canvas for exclusive pixel editing in surface
            try
            {
                canvas = view.getHolder().lockCanvas();
                synchronized (view.getHolder())
                {
                    beginTime = System.currentTimeMillis();
                    framesSkipped = 0; //resetting the frames skipped
                    this.view.onDraw(canvas); // draws the canvas on the panel
                    timeDiff = System.currentTimeMillis() - beginTime; //calc how long the cycle took
                    sleepTime = (int) (FRAME_PERIOD - timeDiff);

                    if (sleepTime > 0)
                    {
                        // if sleepTime > 0, we're OK
                        try
                        {
                            //send the thread to sleep for a short period(good for battery life)
                            Thread.sleep(sleepTime);
                        } catch (InterruptedException e)
                        {
                        }
                    }

                    while (sleepTime < 0 && framesSkipped < MAX_FRAME_SKIPS)
                    {
                        //we need to catch up in frames, so update without rendering
                        this.view.update();
                        sleepTime += FRAME_PERIOD; //add frame period to check if in next frame
                        framesSkipped++;
                    }
                }

            } finally
            {
                //in case of an exception the surface is not left in an inconsistent state
                if (canvas != null)
                {
                    view.getHolder().unlockCanvasAndPost(canvas);
                }
            }
        }
    }
}
