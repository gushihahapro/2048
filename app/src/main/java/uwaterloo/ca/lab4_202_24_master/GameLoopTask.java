package uwaterloo.ca.lab4_202_24_master;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.LinkedList;
import java.util.Queue;
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
    boolean genBlockOnReady = false;
    public TextView GAMEOVER;
    RelativeLayout parentLayout;


    public GameLoopTask(Activity myActivity1, RelativeLayout myRL1, Context myContext1, GestureCallback mainCallBack ){       //Constructor for gameloopTask
        myActivity = myActivity1;
        myContext = myContext1;
        myRL = myRL1;
        this.mainCallBack = mainCallBack;
        createBlock(0,0);          //instantiate block
        animators.add(currentBlock.animator);

        GAMEOVER = new TextView(myContext);
        parentLayout =  myRL;
        myRL.addView(GAMEOVER);
        GAMEOVER.setTextSize(50);
        GAMEOVER.setVisibility(View.INVISIBLE);


    }
    @Override
    public void onGestureDetect(Direction direction) {      //Stores current direction returned from FSM in Acceleration handler to Current Direction local variable
        GAMEOVER.setVisibility(View.INVISIBLE);
        mainCallBack.onGestureDetect(direction);        //send direction back to main to be outputted onto screen in textview
        CurrentDirection = direction;
        Log.d(TAG, "setDirection: " + CurrentDirection);    //logd onto console for testing

        //state machine to determine target coordinates for appropriate gestures
        // send target coordinates to animator method for each specific movement
        blockList = CollisionHandler.ShiftBlocks(direction, blockList);
        genBlockOnReady = true;
    }


    @Override
    public void run() {
        final GameLoopTask parent = this;
        this.myActivity.runOnUiThread(
                new Runnable(){
                    public void run(){
                        boolean ready = true;
                        Vector<GameBlock> del = new Vector<GameBlock>();
                        for (GameBlock b : blockList){
                            if (!b.animator.tick()){
                                ready = false;
                            }
                        }

                        if (ready) {
                            if (genBlockOnReady) {
                                CollisionHandler.GenerateBlock(blockList, parent, GAMEOVER);
                                genBlockOnReady = false;
                            }
                            for (GameBlock b : blockList) {
                                if (!b.ready()){
                                    del.add(b);
                                }
                            }
                        }
                        for (GameBlock b : del){
                            blockList.remove(b);
                        }
                    }
                }
        );


    }

    public void createBlock(int x, int y){
        GameBlock newBlock = new GameBlock(myContext,x, y, myRL, GAMEOVER);//Instantiates new block at coordinates randomly generated from 0 to 3, image scaling and pixel calculations offset in GameBlock.
        currentBlock = newBlock;
        blockList.add(newBlock);

    }


}
