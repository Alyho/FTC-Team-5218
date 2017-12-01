package opmodes;

/**
 * Created by Elizabeth on 12/8/2016.
 */
public class MochaCalibration {

    public static final int TICKS_PER_DEGREE = 17;
    public static final int TICKS_PER_INCH = 79;
    public static final double PIVOT_MULTIPLIER = 3.71428571429;

    public static final double SHOOTER_AUTO_VORTEX = 0.195;
    public static final double SHOOTER_AUTO_CORNER = 0.4125;

    public static final double SHOOTER_Y = 0.5;
    public static final double SHOOTER_B = 0.45;
    public static final double SHOOTER_A = 0.425;
    public static final double SHOOTER_X = 0.4;

    public static final double LIGHT_MINIMUM = 1.35;
    public static final double LIGHT_MAXIMUM = 2.01;

    public static final double TURN_SPEED = 0.2;
    public static final double MOVE_SPEED = 0.75;
    public static final double LINE_SPEED = 0.2;

    public static final double LEFT_PRESSER_DIRECTION = 1;
    public static final double RIGHT_PRESSER_DIRECTION = 0;

    public static final double BRUSH_SPEED = 0.8;
    public static final double ULTRASONIC_DISTANCE_MINIMUM = 25;
    public static final double RANGE_DISTANCE_MINIMUM = 19;

    public static final int COLOR_THRESHOLD = 267;
    public static final int COLOR_READ_DELAY = 700;
}
