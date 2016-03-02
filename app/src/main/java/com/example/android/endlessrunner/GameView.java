package com.example.android.endlessrunner;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceHolder.Callback;
import android.view.SurfaceView;
import android.widget.ImageView;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by Vox on 1/19/2016.
 */
public class GameView extends SurfaceView
{
    GameLoopThread gameLoopThread;
    private SurfaceHolder holder;
    private Random ran = new Random();
    private int coins = 0;
    private int x;
    private int y;
    private int xx = 0;
    private int loop = ran.nextInt(40) + 10;
    private int groundx;
    private int timerItems = 0;
    private int xPosCoin = 1;
    public static int globalXSpeed = 8;
    public static int coinsCollected = 0;
    public static int lanternCollected = 0;
    public static int Score = 0;
    public static int HighScore = 0;
    private static SharedPreferences prefs;
    private String saveScore = "HighScore";
    Paint textPaint = new Paint();
    Bitmap playerbmp;
    Bitmap coinbmp;
    Bitmap bgbmp;
    Bitmap groundbmp;
    Bitmap lanternbmp;
    private List<Player> player = new ArrayList<>();
    private List<Coin> coin = new ArrayList<>();
    private List<Ground> ground = new ArrayList<>();
    private List<Lantern> lantern = new ArrayList<>();

    public GameView(Context context)
    {
        super(context);

        prefs = context.getSharedPreferences("com.example.android.endlessrunner", context.MODE_PRIVATE);//gets save location

        String sPackage = "com.example.androind.endlessrunner";

        HighScore = prefs.getInt(saveScore, 0);
        gameLoopThread = new GameLoopThread(this);

        holder = getHolder();
        holder.addCallback(new Callback()
        {
            public void surfaceDestroyed(SurfaceHolder arg0)
            {
                //TODO logic
                Score = 0;
                coinsCollected = 0;
                prefs.edit().putInt(saveScore,HighScore).commit();//saves score to file
                gameLoopThread.running = false;
            }

            public void surfaceCreated(SurfaceHolder arg0)
            {
                gameLoopThread.setRunning(true);
                gameLoopThread.start();
            }

            public void surfaceChanged(SurfaceHolder arg0, int arg1, int arg2, int arg3)
            {
                //TODO logic


            }
        });

        bgbmp = BitmapFactory.decodeResource(getResources(), R.drawable.featured);
        coinbmp = BitmapFactory.decodeResource(getResources(), R.drawable.coin_gold);
        groundbmp = BitmapFactory.decodeResource(getResources(), R.drawable.ground);
        playerbmp = BitmapFactory.decodeResource(getResources(), R.drawable.ninrun);
        lanternbmp = BitmapFactory.decodeResource(getResources(), R.drawable.lantern);

        player.add(new Player(this, playerbmp, 5, 10)); //numbers represent starting position

    }

    @Override
    public boolean onTouchEvent(MotionEvent e){
        for(Player pplayer: player) {pplayer.onTouch();}//jump event for player
        return false;
    }


    public void update()
    {
        Score += 1;
        updateTimer();
        deleteGround();
        if(Score > HighScore)
        {
            HighScore = Score;
        }
    }
//

    public void updateTimer(){
        timerItems++;

        if(timerItems >= 50)
        {
            int randomItem;
            randomItem = ran.nextInt(3) + 1;
            switch(randomItem)
            {
                case 1:
                    int currentCoin = 1;
                    while(currentCoin <= 5)
                    {
                        coin.add(new Coin(this, coinbmp, this.getWidth()+(32 * xPosCoin), 32));
                        currentCoin++;
                        xx++;
                    }
                    timerItems = 0;
                    break;
                case 2:
                    currentCoin = 1;
                    xPosCoin = 32;
                    for(int i = 0; i < 5 ; i++)
                    {
                        coin.add(new Coin(this, coinbmp, this.getWidth()+ xPosCoin, 32));
                        xPosCoin += 32;
                    }
                    timerItems = 0;
                    break;
                case 3:
                    lantern.add(new Lantern(this, lanternbmp, this.getWidth() + 32, 128));
                    timerItems = 0;
            }
        }
    }
    public void addGround(){
        addGroundHelper();
    }

    public void addGroundHelper(){
        while(xx < this.getWidth() + Ground.width)
        {
            ground.add(new Ground(this, groundbmp,xx, 0));
            xx += groundbmp.getWidth();
        }
    }

    public void deleteGround(){
        for (int i = ground.size() - 1; i >=0 ; i--)
        {
            groundx = ground.get(i).returnX();
            if(groundx <= -Ground.width)
            {
                ground.remove(i);
                ground.add(new Ground(this,groundbmp, groundx+this.getWidth()+Ground.width, 0));
            }
        }
    }
    @Override
    protected void onDraw(Canvas canvas)
    {
        update();
        //canvas.drawColor(Color.CYAN);
        canvas.drawBitmap(bgbmp, 0,0 , null);//draws the background image
        addGround();
        textPaint.setTextSize(64);
        textPaint.setColor(Color.RED);
        canvas.drawText("Score: " + String.valueOf(Score), 0, 48, textPaint);
        canvas.drawText("High Score: " + String.valueOf(HighScore), 0, 128, textPaint);

//        for(Ground gground: ground)
//        {
//            gground.onDraw(canvas);
//        }

        for(Player pplayer: player) { //draws player object
            pplayer.onDraw(canvas);
        }
        for(int i = 0; i < coin.size(); i++)
        {//draws coin object
            coin.get(i).onDraw(canvas);
            Rect playerr  = player.get(0).getBounds();
            Rect coinr = coin.get(i).getBounds();

            if(coin.get(i).returnX() < 0 - 32){
                coin.remove(i);
            }
            if(coin.get(i).checkCollision(playerr, coinr)){
                coin.remove(i);
                Score += 500;
                coinsCollected += 1;
            }
        }
        for(int i = 0; i < lantern.size(); i++)
        {//draws coin object
            lantern.get(i).onDraw(canvas);
            Rect playerr  = player.get(0).getBounds();
            Rect lanternr = lantern.get(i).getBounds();

            if(lantern.get(i).returnX() < 0 - 32){
                lantern.remove(i);
            }
            if(lantern.get(i).checkCollision(playerr, lanternr)){

                Score -= 1500;
                break;
            }
        }
    }
}



