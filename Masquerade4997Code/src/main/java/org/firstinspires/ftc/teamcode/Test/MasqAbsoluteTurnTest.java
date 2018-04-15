package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;

import org.firstinspires.ftc.teamcode.Autonomus.Constants;

import Library4997.MasqUtilities.Direction;
import Library4997.MasqUtilities.MasqUtils;
import Library4997.MasqUtilities.StopCondition;
import Library4997.MasqWrappers.MasqLinearOpMode;
import SubSystems4997.SubSystems.Flipper;
import SubSystems4997.SubSystems.Gripper;

/**
 * Created by Archish on 3/8/18.
 */
@Autonomous(name = "RightTest", group = "Autonomus")
public class MasqAbsoluteTurnTest extends MasqLinearOpMode implements Constants {
    double startTicks, endTicks;
    boolean secondCollection;
    public void runLinearOpMode() throws InterruptedException {
        robot.mapHardware(hardwareMap);
        robot.lineDetector.setMargin(20);
        robot.vuforia.initVuforia(hardwareMap);
        robot.redRotator.setPosition(ROTATOR_RED_CENTER);
        robot.initializeAutonomous();
        robot.initializeServos();
        robot.intake.motor1.setStalledAction(new Runnable() {
            @Override
            public void run() {
                robot.intake.setPower(OUTAKE);
            }
        });
        robot.intake.motor1.setUnStalledAction(new Runnable() {
            @Override
            public void run() {
                robot.intake.setPower(INTAKE);
            }
        });
        while (!opModeIsActive()) {
            dash.create("Init");
            dash.update();
        }
        waitForStart();
        robot.gripper.setGripperPosition(Gripper.Grip.CLAMP);
        robot.vuforia.flash(true);
        robot.sleep(robot.getDelay());
        robot.vuforia.activateVuMark();
        String vuMark = "Right";
        runJewel();
        robot.vuforia.flash(false);
        robot.driveTrain.setClosedLoop(false);
        runVuMark(vuMark);
    }
    public String readVuMark () {
        robot.waitForVuMark();
        return robot.vuforia.getVuMark();
    }
    public void runJewel() {
        robot.jewelArmRed.setPosition(JEWEL_RED_OUT);
        robot.sleep(1000);
        if (robot.jewelColorRed.isRed()) robot.redRotator.setPosition(ROTATOR_RED_NOT_SEEN);
        else robot.redRotator.setPosition(ROTATOR_RED_SEEN);
        robot.sleep(250);
        robot.jewelArmRed.setPosition(JEWEL_RED_IN);
        robot.sleep(100);
    }
    public void runVuMark(String vuMark) {
        int heading = 0;
        robot.redRotator.setPosition(ROTATOR_RED_CENTER);
        robot.drive(20);
        if (MasqUtils.VuMark.isCenter(vuMark)) {
            heading = 140;
        }
        else if (MasqUtils.VuMark.isLeft(vuMark)) {
            heading = 160;
        }
        else if (MasqUtils.VuMark.isRight(vuMark)) {
            heading = 130;
        }
        else if (MasqUtils.VuMark.isUnKnown(vuMark)) {
            heading = 140;
        }
        robot.turnAbsolute(heading, Direction.LEFT, 4);
        robot.flipper.setFlipperPosition(Flipper.Position.OUT);
        super.runSimultaneously(new Runnable() {
            @Override
            public void run() {
                robot.drive(6, POWER_OPTIMAL, Direction.BACKWARD);
            }
        }, new Runnable() {
            @Override
            public void run() {
                robot.sleep(250);
                robot.gripper.setGripperPosition(Gripper.Grip.OUT);
            }
        });
        robot.drive(6, POWER_OPTIMAL, Direction.FORWARD);
        robot.flipper.setFlipperPosition(Flipper.Position.IN);
        robot.turnRelative(180 - heading, Direction.LEFT);
        robot.imu.reset();
        robot.go(10, 90, Direction.LEFT, 0, Direction.BACKWARD);
        robot.turnAbsolute(160, Direction.RIGHT);
        robot.intake.setPower(INTAKE);
        robot.intake.motor1.enableStallDetection();
        robot.stop(new StopCondition() {
            @Override
            public boolean stop() {
                return robot.singleBlock.stop();
            }
        }, POWER_LOW, Direction.FORWARD);
        robot.drive(10, POWER_OPTIMAL, Direction.BACKWARD);
        robot.stop(new StopCondition() {
            @Override
            public boolean stop() {
                return robot.doubleBlock.stop();
            }
        }, POWER_LOW, Direction.FORWARD);
        startTicks = Math.abs(robot.driveTrain.getCurrentPosition());
        robot.drive(6, POWER_OPTIMAL, Direction.BACKWARD);
        if (robot.doubleBlock.stop()) {
            robot.turnAbsolute(180, Direction.RIGHT);
            robot.stop(new StopCondition() {
                @Override
                public boolean stop() {
                    return robot.doubleBlock.stop();
                }
            }, POWER_LOW, Direction.FORWARD);
        }
        endTicks = Math.abs(robot.driveTrain.getCurrentPosition());
        MasqUtils.sleep(750);
        robot.turnAbsolute(170, Direction.RIGHT);
        robot.gripper.setGripperPosition(Gripper.Grip.CLAMP);
        robot.flipper.setFlipperPosition(Flipper.Position.OUT);
        robot.drive(130, POWER_OPTIMAL, Direction.BACKWARD, 4);
        robot.gripper.setGripperPosition(Gripper.Grip.OUT);
        robot.flipper.setFlipperPosition(Flipper.Position.IN);
        robot.drive(5);
    }
}