package uwaterloo.ca.lab4_202_24_master;

import android.util.Pair;

import java.util.LinkedList;
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
                line.add(new Pair<Integer, Integer>(0,0));
            }
            grid.add(line);
        }

        int gameBlockIndex = 0;
        for (GameBlock gameBlock : gameBlocks){
            grid.get(gameBlock.by).set(gameBlock.bx, new Pair<>(gameBlock.getBlockNum(),gameBlockIndex));
            gameBlockIndex++;
        }
    }
}
