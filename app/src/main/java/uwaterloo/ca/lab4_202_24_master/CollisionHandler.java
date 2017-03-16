package uwaterloo.ca.lab4_202_24_master;

import android.util.Log;
import android.util.Pair;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

/**
 * Created by patri on 2017-03-15.
 */

public class CollisionHandler {
    public static void ShiftBlocks(GestureCallback.Direction direction, LinkedList<GameBlock> gameBlocks){
        Vector<Vector<Pair<Integer,Integer>>> grid = new Vector<>();
        //val, gameblockindex
        for (int i = 0; i < 4; ++i){
            Vector<Pair<Integer, Integer>> line = new Vector<>();
            for (int j = 0; j < 4; ++j){
                line.add(new Pair<Integer, Integer>(-1,0));
            }
            grid.add(line);
        }

        int gameBlockIndex = 0;
        for (GameBlock gameBlock : gameBlocks) {
            grid.get(gameBlock.by).set(gameBlock.bx, new Pair<>(gameBlock.getBlockNum(), gameBlockIndex));
            gameBlockIndex++;
        }

        if (direction == GestureCallback.Direction.RIGHT){
            for (int i = 0; i < 4; ++i){
                int delta = 0;
                for (int j = 3; j >= 0; --j){
                    if (grid.get(i).get(j).first == -1){
                        delta++;
                    }else{
                        int gbIndex = grid.get(i).get(j).second;
                        Log.d("XY:", String.format("X: %d, Y: %d",j + delta, i));

                        gameBlocks.get(gbIndex).moveTo(j + delta, i);
                        grid.get(i).set(j + delta, grid.get(i).get(j));
                    }
                }
            }
        }else if (direction == GestureCallback.Direction.LEFT){

        }
    }
    public static void GenerateBlock(LinkedList<GameBlock> gameBlocks, GameLoopTask task){
        boolean[][] grid = new boolean[4][4];
        Random rand = new Random();
        int freeBlocks = 16;
        for (GameBlock gb : gameBlocks){{
            grid[gb.by][gb.bx] = true;
            freeBlocks--;
        }}
        int newBlockIndex = rand.nextInt(freeBlocks);

        int newX = 0, newY = 0;
        for (int i = 0; i < 4; ++i){
            Log.d("grid",Arrays.toString(grid[i]));
            for (int j = 0; j < 4; ++j){
                if (grid[i][j] == false) {
                    newBlockIndex--;
                    if (newBlockIndex == 0) {
                        newY = i;
                        newX = j;
                    }
                }
            }
        }
        task.createBlock(newX, newY);
    }
}
