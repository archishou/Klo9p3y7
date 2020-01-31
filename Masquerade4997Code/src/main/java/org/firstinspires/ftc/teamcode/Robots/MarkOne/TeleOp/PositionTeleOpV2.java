package org.firstinspires.ftc.teamcode.Robots.MarkOne.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.Robots.MarkOne.Robot.MarkOne;

import Library4997.MasqWrappers.MasqLinearOpMode;

/**
 * Created by Keval Kataria on 9/14/2019
 */
@TeleOp(name = "PositionTeleOpV2", group = "MarkOne")
@Disabled
public class PositionTeleOpV2 extends MasqLinearOpMode {
    private MarkOne robot = new MarkOne();

    @Override
    public void runLinearOpMode() throws InterruptedException {
        robot.init(hardwareMap);
        robot.initializeTeleop();


        while(!opModeIsActive()) {
            dash.create("Heading: ", robot.tracker.getHeading());
            dash.update();
        }

        waitForStart();

        Thread thread = new Thread(robot.trackerV2);
        thread.start();
        while(opModeIsActive()) {

            dash.create("X: ",robot.tracker.getGlobalX());
            dash.create("Y: ",robot.tracker.getGlobalY());
            dash.create("Raw X: ",robot.tapeMeasure.getCurrentPosition());
            dash.create("Raw YL: ",robot.intake.motor2.getCurrentPosition());
            dash.create("Raw YR: ", robot.intake.motor1.getCurrentPosition());
            dash.create("XR stick: ", controller1.rightStickX());
            robot.tracker.updateSystem();
            dash.update();

            controller1.update();
            controller2.update();
        }
        robot.trackerV2.setRunning(false);
    }
}