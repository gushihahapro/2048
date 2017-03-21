package uwaterloo.ca.lab4_202_24_master;

import android.content.Intent;
import android.util.Log;
import android.util.Pair;
import android.view.View;
import android.widget.TextView;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Random;
import java.util.Vector;

import static uwaterloo.ca.lab4_202_24_master.GestureCallback.Direction.DOWN;
import static uwaterloo.ca.lab4_202_24_master.GestureCallback.Direction.LEFT;
import static uwaterloo.ca.lab4_202_24_master.GestureCallback.Direction.RIGHT;
import static uwaterloo.ca.lab4_202_24_master.GestureCallback.Direction.UP;

/**
 * Created by patri on 2017-03-15.
 */

public class CollisionHandler {
    public static Vector<GameBlock> merge(Vector<GameBlock> sector, GestureCallback.Direction direction){
        Vector<GameBlock> fillers = new Vector<>(4);
        int delta = 0;
        if (direction == LEFT || direction == UP) {
            for (int i = 0; i < sector.size(); ++i) {
                if (sector.get(i) == null) {
                    delta++;
                } else if (delta != 0) {
                    sector.set(i - delta, sector.get(i));
                    if (direction == UP) {
                        sector.get(i - delta).moveTo(sector.get(i - delta).bx, i - delta);
                    } else if (direction == LEFT) {
                        sector.get(i - delta).moveTo(i - delta, sector.get(i - delta).by);
                    }
                    sector.set(i, null);
                }
            }
        }else if (direction == RIGHT || direction == DOWN){
            for (int i = sector.size() - 1; i >= 0; --i){
                if (sector.get(i) == null){
                    delta++;
                }else if (delta != 0){
                    sector.set(i + delta, sector.get(i));
                    if (direction == DOWN) {
                        sector.get(i + delta).moveTo(sector.get(i + delta).bx, i + delta);
                    } else if (direction == RIGHT) {
                        sector.get(i + delta).moveTo(i + delta, sector.get(i + delta).by);
                    }
                    sector.set(i, null);
                }
            }
        }
        delta = 0;
        if (direction == LEFT || direction == UP) {
            for (int i = 0; i < sector.size(); ++i) {
                if (sector.get(i) != null) {
                    if (i + 1 != sector.size() && sector.get(i + 1) != null) {
                        if (sector.get(i).getBlockNum() == sector.get(i + 1).getBlockNum()) {
                            Log.d("BLKNOS:", String.format("%d, %d", sector.get(i).getBlockNum(), sector.get(i + 1).getBlockNum()));
                            sector.get(i).doubleValue();
                            sector.get(i + 1).destroy();
                            if (direction == UP) {
                                sector.get(i + 1).moveTo(sector.get(i + 1).bx, i - delta);
                            } else if (direction == LEFT) {
                                sector.get(i + 1).moveTo(i - delta, sector.get(i + 1).by);
                            }
                            fillers.add(sector.get(i + 1));
                            sector.set(i + 1, null);
                        }
                    }
                    if (delta != 0) {
                        sector.set(i - delta, sector.get(i));
                        if (direction == UP) {
                            sector.get(i - delta).moveTo(sector.get(i - delta).bx, i - delta);
                        } else if (direction == LEFT) {
                            sector.get(i - delta).moveTo(i - delta, sector.get(i - delta).by);
                        }
                        sector.set(i, null);
                    }
                } else {
                    delta++;
                }
            }
            for (int i = 0; i < fillers.size(); ++i){
                sector.set(sector.size() - i - 1, fillers.get(i));
            }
        }else if (direction == RIGHT || direction == DOWN){
            for (int i = sector.size() - 1; i >= 0; --i){
                if (sector.get(i) != null){
                    if (i - 1 != -1 && sector.get(i - 1) != null) {
                        if (sector.get(i).getBlockNum() == sector.get(i - 1).getBlockNum()) {
                            Log.d("BLKNOS:", String.format("%d, %d", sector.get(i).getBlockNum(), sector.get(i - 1).getBlockNum()));
                            sector.get(i).doubleValue();
                            sector.get(i - 1).destroy();
                            if (direction == DOWN) {
                                sector.get(i - 1).moveTo(sector.get(i - 1).bx, i + delta);
                            } else if (direction == RIGHT) {
                                sector.get(i - 1).moveTo(i + delta, sector.get(i - 1).by);
                            }
                            fillers.add(sector.get(i - 1));
                            sector.set(i - 1, null);
                        }
                    }
                    if (delta != 0) {
                        sector.set(i + delta, sector.get(i));
                        if (direction == DOWN) {
                            sector.get(i + delta).moveTo(sector.get(i + delta).bx, i + delta);
                        } else if (direction == RIGHT) {
                            sector.get(i + delta).moveTo(i + delta, sector.get(i + delta).by);
                        }
                        sector.set(i, null);
                    }
                } else {
                    delta++;
                }
            }
            for (int i = 0; i < fillers.size(); ++i){
                sector.set(i, fillers.get(i));
            }
        }


        return sector;
    }
    public static LinkedList<GameBlock> ShiftBlocks(GestureCallback.Direction direction, LinkedList<GameBlock> gameBlocks){
        Vector<Vector<GameBlock>> grid = new Vector<>(4);
        for (int i = 0; i < 4; ++i){
            grid.add(new Vector<GameBlock>(4));
            for (int j = 0; j < 4; ++j){
                grid.get(i).add(j, null);
            }
        }

        for (GameBlock gameBlock : gameBlocks){
            grid.get(gameBlock.by).set(gameBlock.bx, gameBlock);
        }

        if (direction == LEFT){
            for (int i = 0; i < 4; ++i){
                grid.set(i, CollisionHandler.merge(grid.get(i), LEFT));
            }
        } else if (direction == GestureCallback.Direction.UP){
            for (int i = 0; i < 4; ++i){
                Vector<GameBlock> sector = new Vector<>(4);
                for (int j = 0; j < 4; ++j){
                    sector.add(grid.get(j).get(i));
                }
                sector = CollisionHandler.merge(sector, GestureCallback.Direction.UP);
                for (int j = 0; j < 4; ++j){
                    grid.get(j).set(i, sector.get(j));
                }
            }
        }else if (direction == RIGHT){
            for (int i = 0; i < 4; ++i){
                grid.set(i, CollisionHandler.merge(grid.get(i), RIGHT));
            }
        }else if (direction == DOWN){
            for (int i = 0; i < 4; ++i){
                Vector<GameBlock> sector = new Vector<>(4);
                for (int j = 0; j < 4; ++j){
                    sector.add(grid.get(j).get(i));
                }
                sector = CollisionHandler.merge(sector, GestureCallback.Direction.DOWN);
                for (int j = 0; j < 4; ++j){
                    grid.get(j).set(i, sector.get(j));
                }
            }
        }

        LinkedList<GameBlock> ret = new LinkedList<>();
        for (int i = 0; i < 4; ++i){
            for (int j = 0; j < 4; ++j){
                if (grid.get(i).get(j) != null){
                    ret.add(grid.get(i).get(j));
                }
            }
        }
        return ret;
    }
    public static void GenerateBlock(LinkedList<GameBlock> gameBlocks, GameLoopTask task, TextView GameOver){
        boolean[][] grid = new boolean[4][4];
        Random rand = new Random();
        int freeBlocks = 16;
        for (GameBlock gb : gameBlocks){{
            grid[gb.by][gb.bx] = true;
            freeBlocks--;
        }}
        if (freeBlocks == 0){
            //INSERT LOSE SCREEN HERE
            GameOver.setText("LOSE");
            GameOver.setVisibility(View.VISIBLE);


            return;
        }

        int newBlockIndex = rand.nextInt(freeBlocks);

        int newX = 0, newY = 0;
        boolean found = false;
        for (int i = 0; i < 4 && !found; ++i){
            for (int j = 0; j < 4 && !found; ++j){
                if (!grid[i][j]) {
                    if (newBlockIndex == 0) {
                        newY = i;
                        newX = j;
                        found = true;
                    }
                    newBlockIndex--;
                }
            }
        }
        task.createBlock(newX, newY);
    }
}
