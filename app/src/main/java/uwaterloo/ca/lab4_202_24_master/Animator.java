package uwaterloo.ca.lab4_202_24_master;

/**
 * Created by patri on 2017-03-05.
 */

interface Movement
/*Movement Interface for getting and setting locations of an object*/
{
    public void setPixelX(int x);
    public void setPixelY(int y);
    public int getPixelX();
    public int getPixelY();
}

public class Animator {
    Movement contextObject;             //Object to move
    private int targetX, targetY;       //Destination

    private float Vx, Vy;                       //Current velocity

    Animator(Movement object){
        this.contextObject = object;
        targetX = contextObject.getPixelX();
        targetY = contextObject.getPixelY();
    }

    public void setTarget(int x, int y){
        if (Vx == 0 && Vy == 0)
        //Ensure that there is no velocity at the moment.
        {
            targetX = x;
            targetY = y;
            Vx = 0;
            Vy = 0;
        }
    }
    public void move(GestureCallback.Direction direction){

    }

    public boolean tick()
    /**
     * Tick handles the per-cycle animation
     **/
    {
        int cx = contextObject.getPixelX();
        int cy = contextObject.getPixelY();

        //Calculate delta locations
        int dx = targetX - cx;
        int dy = targetY - cy;

        final float ACCELERATION = (float) 2;       //Acceleration constant

        //If the delta is non-zero, add acceleration to the velocity
        if (dx != 0) {
            Vx += ACCELERATION * (dx < 0 ? -1 : 1);
        }else{
            Vx = 0;
        }
        if (dy != 0) {
            Vy += ACCELERATION * (dy < 0 ? -1 : 1);
        }else{
            Vy = 0;
        }

        //If the delta is close enough to the final destination jump there.
        if (Math.abs(dx) < Math.abs((int) (2 * Vx))){
            contextObject.setPixelX(targetX);
            Vx = 0;
        }else{
            contextObject.setPixelX((int)(cx + Vx));
        }
        if (Math.abs(dy) < Math.abs((int) (2 * Vy))){
            contextObject.setPixelY(targetY);
            Vy = 0;
        }else{
            contextObject.setPixelY((int)(cy + Vy));
        }

        if (Vx == 0 && Vy == 0){
            return true;
        }else{
            return false;
        }
    }
}
