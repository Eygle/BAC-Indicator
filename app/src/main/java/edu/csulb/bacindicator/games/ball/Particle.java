package edu.csulb.bacindicator.games.ball;

/**
 * Created by Jimmy
 */
public class Particle {

    private static final float COR = 0.7f;

    public float mPosX;
    public float mPosY;
    private float mVelX;
    private float mVelY;

    public Particle(float posX, float posY) {
        mPosX = posX;
        mPosY = posY;
    }

    public void updatePosition(float sx, float sy, float sz, long timestamp) {
        float dt = (System.nanoTime() - timestamp) / 50000000.0f;

        mVelX += -sx * dt;
        mVelY += -sy * dt;

        mPosX += mVelX * dt;
        mPosY += mVelY * dt;
    }

    /*
    **  Bound Screen from 0:0 to horitonzalBound:verticalBound
     */
    public void resolveCollisionWithBounds(float horizontalBound, float verticalBound, int modelSize) {
        if (mPosX > horizontalBound - modelSize) {
            mPosX = horizontalBound - modelSize;
            mVelX = -mVelX * COR;
        } else if (mPosX < 0) {
            mPosX = 0;
            mVelX = -mVelX * COR;
        }

        if (mPosY > verticalBound - modelSize) {
            mPosY = verticalBound - modelSize;
            mVelY = -mVelY * COR;
        } else if (mPosY < 0) {
            mPosY = 0;
            mVelY = -mVelY * COR;
        }
    }

    public boolean isInTheCircle(float horizontalBound, float verticalBound, int radius) {
        return (Math.pow(mPosX - horizontalBound / 2, 2) + Math.pow(mPosY - verticalBound / 2, 2) < Math.pow(radius, 2));
    }
}
