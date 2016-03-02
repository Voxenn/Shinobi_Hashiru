package com.example.android.endlessrunner;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Vox on 1/30/2016.
 */
public class Lantern
{
    private int x, y;
    private Bitmap bmp;
    private GameView gameview;
    private Rect playerR;
    private Rect lanternr;
    private double xSpeed =  -GameView.globalXSpeed;//speed of how fast coin moves on screen


    public Lantern(GameView gameview,Bitmap bmp,int x,int y)
    {
        this.gameview = gameview;
        this.bmp = bmp;
        this.x = x;
        this.y = y;
    }

    public Rect getBounds()
    {
        return new Rect(this.x, this.y, (x + bmp.getWidth()), (y + bmp.getHeight()));
    }

    public int returnX(){
        return x;
    }
    public boolean checkCollision(Rect playerR, Rect lanternR)
    {
        this.playerR = playerR;
        this.lanternr = lanternR;
        return Rect.intersects(playerR, lanternR);
    }
    public void update()
    {
        x += xSpeed;
        y = gameview.getHeight() - Ground.height - bmp.getHeight();
    }

    public void onDraw(Canvas canvas)
    {
        update();
        onDrawHelper(canvas);
    }

    public void onDrawHelper(Canvas canvas)
    {
        int srcX = bmp.getWidth();
        Rect src = new Rect(0, 0, srcX , bmp.getHeight()); //draws a rectangle of the bitmap
        Rect dst = new Rect(x,y, (x + bmp.getWidth()), (y + bmp.getHeight())); //draws rectangle relative position to player
        canvas.drawBitmap(bmp, src, dst, null);
    }
}
