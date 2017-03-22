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
    //Merge vectors together
    public static Vector<GameBlock> merge(Vector<GameBlock> sector, GestureCallback.Direction direction){
        //Ex. 2222 merges to 44XX and if left and XX44 if right, similar for up/down
        Vector<GameBlock> fillers = new Vector<>(4);    //buffer for to-be-deleted items
        int delta = 0;
        //Shift everything as far as possible without merging to begin
        //Ex. 24X4 becomes 244X (LEFT) or X244(RIGHT)
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
        //Merge blocks finally
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
        //Input blocks into vector grid
        Vector<Vector<GameBlock>> grid = new Vector<>(4);
        for (int i = 0; i < 4; ++i){
            grid.add(new Vector<GameBlock>(4));
            for (int j = 0; j < 4; ++j){
                grid.get(i).add(j, null);
            }
        }

        //Load values from linked list
        for (GameBlock gameBlock : gameBlocks){
            grid.get(gameBlock.by).set(gameBlock.bx, gameBlock);
        }

        //Loop through and merge rows/columns depending on direction
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

        //Add blocks to linked list and return it
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
    public static boolean NoMergable(LinkedList<GameBlock> gameBlocks){
        //Checks blocks around to see if there are any blocks still mergable (for lose condition)
        int[][] grid = new int[4][4];
        for (GameBlock gb : gameBlocks){
            grid[gb.bx][gb.by] = gb.getBlockNum();
        }
        for (int x = 0; x < 4; ++x){
            for (int y = 0; y < 4; ++y){
                for (int dy = -1; dy <= 1; ++dy){
                    int dx = 0;
                    if (dx == 0 && dy == 0){
                        continue;
                    }
                    int tx = x + dx;
                    int ty = y + dy;
                    if (tx >= 0 && tx <= 3 && ty >=0 && ty <= 3){
                        if (grid[y][x] == grid[ty][tx]){
                            Log.d("VAL", String.format("%d, %d", grid[y][x], grid[ty][tx]));
                            return false;
                        }
                    }
                }
                for (int dx = -1; dx <= 1; ++dx){
                    int dy = 0;
                    if (dx == 0 && dy == 0){
                        continue;
                    }
                    int tx = x + dx;
                    int ty = y + dy;
                    if (tx >= 0 && tx <= 3 && ty >=0 && ty <= 3){
                        if (grid[y][x] == grid[ty][tx]){
                            Log.d("VAL", String.format("%d, %d", grid[y][x], grid[ty][tx]));
                            return false;
                        }
                    }
                }
            }
        }
        return true;
    }
    public static void GenerateBlock(LinkedList<GameBlock> gameBlocks, GameLoopTask task, TextView GameOver){
        //Finds an empty slot to put new gameblock in
        boolean[][] grid = new boolean[4][4];
        Random rand = new Random();
        int freeBlocks = 16;
        for (GameBlock gb : gameBlocks){{
            grid[gb.by][gb.bx] = true;
            freeBlocks--;
        }}
        if (freeBlocks == 0){
            if (CollisionHandler.NoMergable(gameBlocks)) {
                //INSERT LOSE SCREEN HERE
                GameOver.setVisibility(View.VISIBLE);
                GameOver.bringToFront();
                task.enable = false;
            }

            return;
        }
        int newBlockIndex = rand.nextInt(freeBlocks);

        int newX = 0, newY = 0;
        boolean found = false;
        for (int i = 0; i < 4 && !found; ++i) {
            for (int j = 0; j < 4 && !found; ++j) {
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
