package uwaterloo.ca.lab4_202_24_master;

import android.content.Context;
import android.graphics.Color;
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


    public GameBlock(Context myContext, int bx, int by, RelativeLayout relLayout) {

        super(myContext);
        this.setImageResource(R.drawable.gameblock);

        this.setScaleX(IMAGE_SCALE);
        this.setScaleY(IMAGE_SCALE);
        this.bx=bx;
        this.by=by;

        this.myCoordX = blockLayoutIncrement*bx;
        this.myCoordY = blockLayoutIncrement*by;

        blockTV = new TextView(myContext);
        ranNum = new Random();
        blockNum = 2^(ranNum.nextInt(3));
        blockTV.setText(Integer.toString(blockNum));
        blockTV.bringToFront();
        blockTV.setTextColor(Color.BLACK);
        blockTV.setTextSize(26);

        setPixelX(myCoordX);
        setPixelY(myCoordY);

        animator = new Animator(this);


        relLayout.addView(this);
        relLayout.addView(blockTV);
    }


    public int getBlockNum(){
        return this.blockNum;

    }

    public void setBlockNum(int num){
        this.blockNum = num;

    }
    public void moveTo(int x, int y){
        this.animator.setTarget(x * blockLayoutIncrement, y * blockLayoutIncrement);
        this.bx = x;
        this.by = y;
    }

    @Override
    public void setPixelX(int x) {
        this.setX(x-69);                //Offset to match image coordinate to background(0,0) is actually (-69, -69)
        this.blockTV.setX(x+105);
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
