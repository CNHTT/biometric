package com.extra.widget.progress;


import com.extra.widget.progress.sprite.Sprite;
import com.extra.widget.progress.style.ChasingDots;
import com.extra.widget.progress.style.Circle;
import com.extra.widget.progress.style.CubeGrid;
import com.extra.widget.progress.style.DoubleBounce;
import com.extra.widget.progress.style.FadingCircle;
import com.extra.widget.progress.style.FoldingCube;
import com.extra.widget.progress.style.MultiplePulse;
import com.extra.widget.progress.style.MultiplePulseRing;
import com.extra.widget.progress.style.Pulse;
import com.extra.widget.progress.style.PulseRing;
import com.extra.widget.progress.style.RotatingCircle;
import com.extra.widget.progress.style.RotatingPlane;
import com.extra.widget.progress.style.ThreeBounce;
import com.extra.widget.progress.style.WanderingCubes;
import com.extra.widget.progress.style.Wave;

/**
 * Created by ybq.
 */
public class SpriteFactory {

    public static Sprite create(Style style) {
        Sprite sprite = null;
        switch (style) {
            case ROTATING_PLANE:
                sprite = new RotatingPlane();
                break;
            case DOUBLE_BOUNCE:
                sprite = new DoubleBounce();
                break;
            case WAVE:
                sprite = new Wave();
                break;
            case WANDERING_CUBES:
                sprite = new WanderingCubes();
                break;
            case PULSE:
                sprite = new Pulse();
                break;
            case CHASING_DOTS:
                sprite = new ChasingDots();
                break;
            case THREE_BOUNCE:
                sprite = new ThreeBounce();
                break;
            case CIRCLE:
                sprite = new Circle();
                break;
            case CUBE_GRID:
                sprite = new CubeGrid();
                break;
            case FADING_CIRCLE:
                sprite = new FadingCircle();
                break;
            case FOLDING_CUBE:
                sprite = new FoldingCube();
                break;
            case ROTATING_CIRCLE:
                sprite = new RotatingCircle();
                break;
            case MULTIPLE_PULSE:
                sprite = new MultiplePulse();
                break;
            case PULSE_RING:
                sprite = new PulseRing();
                break;
            case MULTIPLE_PULSE_RING:
                sprite = new MultiplePulseRing();
                break;
            default:
                break;
        }
        return sprite;
    }
}
