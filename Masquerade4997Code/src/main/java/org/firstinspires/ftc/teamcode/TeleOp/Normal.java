package org.firstinspires.ftc.teamcode.TeleOp;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import Library4997.MasqWrappers.MasqLinearOpMode;

/**
 * Created by Archish on 9/8/17.
 */
@TeleOp(name = "NFS", group = "Template")
public class Normal extends MasqLinearOpMode implements Constants{
    @Override
    public void run() throws InterruptedException {
        robot.mapHardware(hardwareMap);
        while (!opModeIsActive()){
            dash.create(robot.imu.getHeading());
            dash.create("USE RIGHT TRIGGER, and LEFT TRIGGER for lift");
            dash.create(controller1.a());
            dash.update();
        }
        waitForStart();
        while (opModeIsActive()){
            robot.NFS(controller1);
            if (controller1.rightTriggerPressed()) robot.lift.setPower(controller1.rightTrigger());
            else if (controller1.leftTriggerPressed()) robot.lift.setPower(-1);
            else robot.lift.setPower(0);

            dash.create(controller1.a());
            dash.create("LEFT",robot.driveTrain.leftDrive.getRate());
            dash.create("RIGHT", robot.driveTrain.rightDrive.getRate());
            dash.create("LIFT POSITION", robot.lift.getCurrentPosition());
            dash.update();
        }
    }
}