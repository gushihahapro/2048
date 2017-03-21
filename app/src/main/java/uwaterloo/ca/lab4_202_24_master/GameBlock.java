package uwaterloo.ca.lab4_202_24_master;

import android.content.Context;
import android.graphics.Color;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Random;

/**
 * Created by Alex on 2017-03-05.
 */

public class GameBlock extends ImageView implements Movement {



    private float IMAGE_SCALE = 0.5f;       //custom scaling to fit image into background grid **note scaling image does not change coordinate borders
    private int myCoordX;
    private int myCoordY;
    public int bx, by;
    private int blockLayoutIncrement = 243;     // coordinate pixel constant for moving one block up or down
    public Animator animator;
    private int blockNum;
    private Random ranNum;
    public TextView blockTV;
    RelativeLayout parentLayout;
    private boolean destroyOnReady = false;
    private boolean doubleOnReady = false;
    private TextView GAMEOVER;

    public GameBlock(Context myContext, int bx, int by, RelativeLayout relLayout, TextView GameOver) {

        super(myContext);
        this.setImageResource(R.drawable.gameblock);        // instantiate block image from drawable folder

        this.setScaleX(IMAGE_SCALE);                        //scale block image to fit background
        this.setScaleY(IMAGE_SCALE);
        this.bx=bx;                                         // store block grid coordinate into bx an by
        this.by=by;

        this.myCoordX = blockLayoutIncrement*bx;            //convert bx and by to pixel coordinates
        this.myCoordY = blockLayoutIncrement*by;

        blockTV = new TextView(myContext);                  //instantiate, generate and set up TextView for numbers on box
        ranNum = new Random();
        blockNum = (int) Math.pow(2, ranNum.nextInt(2) + 1);
        blockTV.setText(Integer.toString(blockNum));
        blockTV.bringToFront();
        blockTV.setTextColor(Color.BLACK);
        blockTV.setTextSize(26);

        setPixelX(myCoordX);                        //send coordinates to be centered
        setPixelY(myCoordY);

        animator = new Animator(this);

        parentLayout =  relLayout;          //add Views to Relative Layout
        relLayout.addView(this);
        relLayout.addView(blockTV);

        this.GAMEOVER = GameOver;       //pass gameover text view

    }


    public int getBlockNum(){
        return this.blockNum;

    }

    public boolean ready(){
        if (destroyOnReady){
            parentLayout.removeViewInLayout(this);
            parentLayout.removeViewInLayout(blockTV);
            return false;
        }
        if (doubleOnReady){
            setBlockNum(this.blockNum * 2);
            doubleOnReady = false;
        }
        return true;
    }

    public void destroy(){
       destroyOnReady = true;
    }
    public void doubleValue(){
        doubleOnReady = true;
    }

    public void setBlockNum(int num){
        this.blockNum = num;
        this.blockTV.setText(Integer.toString(num));
        if(num == 256){                                     // Check if game has been won
            GAMEOVER.setText("WIN");
            GAMEOVER.setVisibility(View.VISIBLE);


        }
    }
    public void moveTo(int x, int y){
        this.animator.setTarget(x * blockLayoutIncrement, y * blockLayoutIncrement);
        this.bx = x;
        this.by = y;
    }

    @Override
    public void setPixelX(int x) {
        this.setX(x-69);                //Offset to match image coordinate to background(0,0) is actually (-69, -69)
        //center for 2 digit numbers
        if(this.blockNum > 9 && this.blockNum < 100){
            this.blockTV.setX(myCoordX+82);
        }
        //center for 3 digit numbers
        else if(this.blockNum >= 100 && this.blockNum < 1000){
            this.blockTV.setX(myCoordX+63);
        }
        //center for 4 digit numbers
        else if(this.blockNum >= 1000 && this.blockNum < 10000){
            this.blockTV.setX((myCoordX+105)/2);
        }
        else this.blockTV.setX(x+105);
        myCoordX = x;                   //sets new image coordinates from animator
    }

    @Override
    public void setPixelY(int y) {
        this.setY(y-69);                //Offset to match image coordinate to background(0,0) is actually (-69, -69)
        this.blockTV.setY(y+75);
        myCoordY = y;                   //sets new image coordinates from animator
    }

    @Override
    public int getPixelX() {
        return myCoordX;                //send current coordinates to movement method to be used in animator
    }

    @Override
    public int getPixelY() {
        return myCoordY;
    }
}
