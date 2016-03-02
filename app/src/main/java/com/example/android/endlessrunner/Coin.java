package com.example.android.endlessrunner;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Vox on 1/23/2016.
 */
public class Coin
{
    private int x, y, y2;
    private Bitmap bmp;
    private GameView gameview;
    private double xSpeed =  -GameView.globalXSpeed;//speed of how fast coin moves on screen

    private int width;
    private int height;

    private int mcurrentFrame = 0;
    private int mColumnWidth = 8; //amount of frames on sprite sheet
    private int mColumnHeight = 1; //height of frame on sprite sheet
    private Rect coinR;//rectangle for coin hit box
    private Rect playerR;//rectangle for player hit box


    public Coin(GameView gameview, Bitmap bmp, int x, int y)//Constructor for coin object
    {
        this.x = x;
        this.y2 = y;
        this.bmp = bmp;
        this.gameview = gameview;
        this.width = bmp.getWidth()/mColumnWidth;
        this.height = bmp.getHeight()/mColumnHeight;
    }

    private void update()
    {
        x += xSpeed;
        y = y2 + gameview.getHeight() - Ground.height - y2 - bmp.getHeight();
        if (mcurrentFrame >= (mColumnWidth - 1))
        {
            mcurrentFrame = 0;
        } else
            mcurrentFrame += 1;
//        if (x < 0)
//        {
//            x = gameview.getWidth() + width; deprecated code will allow for object to scroll back onto screen
//        }
    }

    public Rect getBounds()
    {
        return new Rect(this.x, this.y, (x + width), (y + height));
    }

    public int returnX(){return x;}

    public boolean checkCollision(Rect playerR, Rect coinR)
    {
        this.playerR = playerR;
        this.coinR = coinR;
        return Rect.intersects(playerR, coinR);
    }


    public void onDraw(Canvas canvas)
    {
        update();
        onDrawHelper(canvas);
    }

    public void onDrawHelper(Canvas canvas)
    {
        int srcX = mcurrentFrame * width;
        Rect src = new Rect(mcurrentFrame * width, 0, srcX + width, 80); //draws a rectangle of the bitmap
        Rect dst = new Rect(x, y, (x + width), (y + 80)); //draws rectangle relative position to player
        canvas.drawBitmap(bmp, src, dst, null);
    }
}
