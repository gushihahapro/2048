package uwaterloo.ca.lab4_202_24_master;

/**
 * Created by patri on 2017-02-04.
 */

public interface GestureCallback {
    enum Direction{LEFT, RIGHT, UP, DOWN};
    void onGestureDetect(Direction direction);
}
