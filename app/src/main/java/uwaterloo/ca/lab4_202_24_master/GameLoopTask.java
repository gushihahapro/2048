package uwaterloo.ca.lab4_202_24_master;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.RelativeLayout;

import java.util.LinkedList;
import java.util.Random;
import java.util.TimerTask;
import java.util.Vector;

import static android.content.ContentValues.TAG;

/**
 * Created by Alex on 2017-02-21.
 */

public class GameLoopTask extends TimerTask implements GestureCallback {

    Activity myActivity;
    Context myContext;
    RelativeLayout myRL;
    int blockLayoutIncrement = 243;     // coordinate pixel constant for moving one block up or down
    Direction CurrentDirection;
    Vector<Animator> animators = new Vector<>();
    GestureCallback mainCallBack;
    GameBlock currentBlock;
    LinkedList<GameBlock> blockList = new LinkedList<>();
    Random myRandomNum = new Random();


    public GameLoopTask(Activity myActivity1, RelativeLayout myRL1, Context myContext1, GestureCallback mainCallBack ){       //Constructor for gameloopTask
        myActivity = myActivity1;
        myContext = myContext1;
        myRL = myRL1;
        this.mainCallBack = mainCallBack;
        createBlock();          //instantiate block
        animators.add(currentBlock.animator);

    }
    @Override
    public void onGestureDetect(Direction direction) {      //Stores current direction returned from FSM in Acceleration handler to Current Direction local variable
        mainCallBack.onGestureDetect(direction);        //send direction back to main to be outputted onto screen in textview
        CurrentDirection = direction;
        Log.d(TAG, "setDirection: " + CurrentDirection);    //logd onto console for testing

        //state machine to determine target coordinates for appropriate gestures
        // send target coordinates to animator method for each specific movement

        switch (CurrentDirection){
            case UP:
                for (GameBlock b : blockList){
                    b.moveTo(b.bx, 0);
                }
                break;

            case DOWN:
                for (GameBlock b : blockList){
                    b.moveTo(b.bx, 3);
                }
                break;
            case LEFT:
                for (GameBlock b : blockList){
                    b.moveTo(0, b.by);
                }
                break;
            case RIGHT:
                for (GameBlock b : blockList){
                    b.moveTo(3, b.by);
                }
                break;
        }

        createBlock();
    }






    @Override
    public void run() {
        this.myActivity.runOnUiThread(
                new Runnable(){
                    public void run(){
                    for (GameBlock b : blockList){
                        b.animator.tick();
                    }
                    }
                }
        );


    }

    private void createBlock(){
        GameBlock newBlock = new GameBlock(myContext,(myRandomNum.nextInt(4)), (myRandomNum.nextInt(4)), myRL);//Instantiates new block at coordinates randomly genorated from 0 to 3, image scaling and pixel calculations offset in GameBlock.
        currentBlock = newBlock;
        blockList.add(newBlock);




    }


}
