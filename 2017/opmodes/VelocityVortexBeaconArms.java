package opmodes;

/*
 * Created by izzielau on 1/2/2017.
 */

import com.qualcomm.robotcore.hardware.DeviceInterfaceModule;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.RobotLog;

import team25core.AutonomousEvent;
import team25core.ColorSensorTask;
import team25core.DeadReckonPath;
import team25core.DeadReckonTask;
import team25core.Drivetrain;
import team25core.LimitSwitchTask;
import team25core.Robot;
import team25core.RobotEvent;
import team25core.SingleShotTimerTask;

public class VelocityVortexBeaconArms {

    protected Robot robot;
    protected Servo servo;
    protected DeviceInterfaceModule module;
    protected DeadReckonPath moveToNextButton;
    protected MochaParticleBeaconAutonomous.NumberOfBeacons numberOfBeacons;
    protected boolean isBlueAlliance;
    protected Drivetrain drivetrain;

    protected double RIGHT_DIRECTION = MochaCalibration.RIGHT_PRESSER_DIRECTION;
    protected double LEFT_DIRECTION = MochaCalibration.LEFT_PRESSER_DIRECTION;

    public VelocityVortexBeaconArms(Robot robot, DeviceInterfaceModule interfaceModule, DeadReckonPath moveToNextButton, Drivetrain drivetrain, Servo servo, boolean isBlueAlliance, MochaParticleBeaconAutonomous.NumberOfBeacons numberOfBeacons)
    {
        this.robot = robot;
        this.servo = servo;
        this.module = interfaceModule;
        this.isBlueAlliance = isBlueAlliance;
        this.moveToNextButton = moveToNextButton;
        this.numberOfBeacons = numberOfBeacons;
        this.drivetrain = drivetrain;
    }

    public VelocityVortexBeaconArms(Robot robot, DeviceInterfaceModule interfaceModule, DeadReckonPath moveToNextButton, Drivetrain drivetrain, Servo servo, boolean isBlueAlliance)
    {
        this.robot = robot;
        this.servo = servo;
        this.module = interfaceModule;
        this.isBlueAlliance = isBlueAlliance;
        this.moveToNextButton = moveToNextButton;
        this.drivetrain = drivetrain;
    }

    public void deploy(boolean sensedMyAlliance, boolean firstButton)
    {
        if (sensedMyAlliance && isBlueAlliance) {
            RobotLog.i("163 Blue alliance, aligned correctly");
            deployServo();
        } else if (sensedMyAlliance && !isBlueAlliance){
            RobotLog.i("163 Red alliance, aligned correctly");
            deployServo();
        } else if (!sensedMyAlliance && firstButton) {
            RobotLog.i("163 Wrong color, moving on to the next button");
            handleWrongColor();
        } else {
            RobotLog.e("163 Something went wrong with the color sensing");
        }
    }

    public void handleWrongColor()
    {
        robot.addTask(new DeadReckonTask(robot, moveToNextButton, drivetrain){
            @Override
            public void handleEvent(RobotEvent e)
            {
                DeadReckonEvent event = (DeadReckonEvent) e;
                if (event.kind == EventKind.PATH_DONE) {
                    RobotLog.i("163 Robot finished moving to the next button, redoing color");
                    deploy(true, false);
                }
            }
        });
    }

    public void deployServo()
    {
        RobotLog.i("163 Timer start, moving the servo");
        runServoWithTimer();
    }

    public void runServoWithTimer()
    {
        servo.setPosition(RIGHT_DIRECTION);
        robot.addTask(new SingleShotTimerTask(robot, 2000) {
            @Override
            public void handleEvent(RobotEvent e) {
                SingleShotTimerEvent event = (SingleShotTimerEvent) e;

                if (event.kind == EventKind.EXPIRED) {
                    stowServo();
                }
            }
        });
    }

    public void stowServo()
    {
        RobotLog.i("163 Timer start, stowing the servo");
        if (isBlueAlliance) {
            servo.setPosition(LEFT_DIRECTION);
        } else {
            servo.setPosition(RIGHT_DIRECTION);
        }

        robot.addTask(new SingleShotTimerTask(robot, 1000) {
            @Override
            public void handleEvent(RobotEvent e) {
                SingleShotTimerEvent event = (SingleShotTimerEvent) e;

                this.stop();
                if (event.kind == EventKind.EXPIRED) {
                    stowServoForASecond();

                    if (numberOfBeacons == MochaParticleBeaconAutonomous.NumberOfBeacons.TWO) {
                        firstBeaconWorkDone();
                    }
                }
            }
        });
    }

    public void stowServoForASecond()
    {
        servo.setPosition(LEFT_DIRECTION);
        robot.addTask(new SingleShotTimerTask(robot, 1000) {
            @Override
            public void handleEvent(RobotEvent e)
            {
                SingleShotTimerEvent event = (SingleShotTimerEvent) e;
                if (event.kind == EventKind.EXPIRED) {
                    RobotLog.i("163 Timer done, finish stowing the servo for a second");
                    servo.setPosition(0.5);
                }
            }
        });
    }

    public void firstBeaconWorkDone()
    {
        RobotLog.i("163 Queuing BeaconDone AutonomousEvent");
        AutonomousEvent beaconDone = new AutonomousEvent(robot, AutonomousEvent.EventKind.BEACON_DONE);
        robot.queueEvent(beaconDone);
    }
}