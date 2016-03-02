package com.example.android.endlessrunner;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Rect;

/**
 * Created by Vox on 1/19/2016.
 */
public class Player
{
    static int x; // x axis
    static int y; //y axis
    static int gravity = 1;
    static int vspeed = 1;
    static int playerheight;
    static int playerwidth;
    static int jumppower = -25; //height of jump, lower = higher jump
    static int maxJump = 2; //max number of times player can jump
    private int xSpeed;
    private int width, height;//coordinates of hit box
    private int mcurrentFrame = 0;
    private int mColumnWidth = 1;//initial hit box width
    private int mColumnHeight = 1;//initial hit box height
    private Rect coinR;//rectangle for coin hit box
    private Rect playerR;//rectangle for player hit box



    Bitmap bmp;
    GameView gameview;
    public Player(GameView gameview, Bitmap bmp, int x, int y) //Constructor for player object
    {
        this.x = x;
        this.y = y;
        this.gameview = gameview;
        this.bmp = bmp;
        this.width = bmp.getWidth()/mColumnWidth;
        this.height = bmp.getHeight()/mColumnHeight;

        playerheight = bmp.getHeight(); //initializes player with height of ground
    }

    public void update(){
        checkGround();
        x += xSpeed;
        if (mcurrentFrame >= (mColumnWidth - 1))
        {
            mcurrentFrame = 0;
        } else
            mcurrentFrame += 1;
        if (x < 0)
        {
            x = gameview.getWidth() + width;
        }

    }

    public void checkGround(){
        if(y < gameview.getHeight()- Ground.height - playerheight){
            vspeed += gravity;
            if (y > gameview.getHeight()- Ground.height - playerheight - vspeed)//makes sure he doesn't fall through floor
            {
                vspeed = gameview.getHeight()- Ground.height - playerheight;
            }
        }
        else if (vspeed > 0)
        {
            vspeed = 0;
            y = gameview.getHeight()- Ground.height - playerheight;
        }
        y += vspeed;
    }

    public void onTouch(){

        if((y >= gameview.getHeight()- Ground.height - playerheight) && (maxJump > 0))
        {
            vspeed = jumppower;//height player jumps

        }
        if((y >= gameview.getHeight() - Ground.height - playerheight) && (maxJump == 0))
        {
            maxJump = 2;
        }

    }

    public boolean checkCollision(Rect playerR, Rect coinR)
    {
        this.playerR = playerR;
        this.coinR = coinR;
        return Rect.intersects(playerR, coinR);
    }


    public Rect getBounds()
    {
        return new Rect(this.x, this.y, (x + width), (y + height));
    }

    public void onDraw(Canvas canvas)
    {
        update();
        onDrawHelper(canvas);
    }

    public void onDrawHelper(Canvas canvas) //Helper method to help allocation resources in onDraw method
    {
        int srcX = mcurrentFrame * width; //updates current frame
        Rect src = new Rect(mcurrentFrame * width, 0, srcX + width, 128); //draws a rectangle around the player
        Rect dst = new Rect(x, y, (x + width), (y + 128)); //draws a rectangle relative to the coin object
        canvas.drawBitmap(bmp, src, dst, null);
    }

}
