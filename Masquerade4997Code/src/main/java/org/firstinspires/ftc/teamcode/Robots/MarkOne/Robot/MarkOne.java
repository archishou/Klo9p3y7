package org.firstinspires.ftc.teamcode.Robots.MarkOne.Robot;

import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.teamcode.Robots.MarkOne.Robot.SubSystems.MarkOneFoundationHook;
import org.firstinspires.ftc.teamcode.Robots.MarkOne.Robot.SubSystems.MarkOneSideGrabber;

import Library4997.MasqCV.MasqCV;
import Library4997.MasqCV.detectors.skystone.SkystoneDetector;
import Library4997.MasqControlSystems.MasqPID.MasqPIDController;
import Library4997.MasqDriveTrains.MasqMechanumDriveTrain;
import Library4997.MasqMotors.MasqMotor;
import Library4997.MasqMotors.MasqMotorModel;
import Library4997.MasqMotors.MasqMotorSystem;
import Library4997.MasqPositionTracker;
import Library4997.MasqResources.MasqUtils;
import Library4997.MasqRobot;
import Library4997.MasqServos.MasqServo;
import Library4997.MasqWrappers.DashBoard;

import static Library4997.MasqResources.MasqUtils.tolerance;


/**
 * Created by Archishmaan Peyyety on 2019-08-06.
 * Project: MasqLib
 */
public class MarkOne extends MasqRobot {

    public MasqServo blockGrabber, blockRotater, blockPusher, capper;
    public MarkOneFoundationHook foundationHook;
    public MarkOneSideGrabber sideGrabber;
    public MasqMotor lift,X;
    public MasqMotorSystem intake;
    public MasqCV cv;

    @Override
    public void mapHardware(HardwareMap hardwareMap) {
        driveTrain = new MasqMechanumDriveTrain(hardwareMap);
        blockGrabber = new MasqServo("blockGrabber", hardwareMap);
        lift = new MasqMotor("lift", MasqMotorModel.NEVEREST60, hardwareMap);
        blockRotater = new MasqServo("blockRotater", hardwareMap);
        intake = new MasqMotorSystem("intakeRight", DcMotorSimple.Direction.FORWARD, "intakeLeft", DcMotorSimple.Direction.REVERSE, MasqMotorModel.REVTHROUGHBORE, hardwareMap);
        blockPusher = new MasqServo("blockPusher", hardwareMap);
        capper = new MasqServo("capper", hardwareMap);
        sideGrabber = new MarkOneSideGrabber(hardwareMap);
        X = new MasqMotor("X", MasqMotorModel.USDIGITAL_E4T, DcMotorSimple.Direction.REVERSE,hardwareMap);
        tracker = new MasqPositionTracker(X,intake.motor1, intake.motor2, hardwareMap);
        foundationHook = new MarkOneFoundationHook(hardwareMap);
        dash = DashBoard.getDash();
    }

    public void init(HardwareMap hardwareMap) {
        mapHardware(hardwareMap);
        tracker.setPosition(MasqPositionTracker.DeadWheelPosition.THREE);
        driveTrain.setTracker(tracker);
        tracker.setXRadius(5.68);
        tracker.setTrackWidth(14.625);
        MasqUtils.driveController = new MasqPIDController(0.005);
        MasqUtils.angleController = new MasqPIDController(0.005);
        MasqUtils.turnController = new MasqPIDController(0.015);
        MasqUtils.velocityTeleController = new MasqPIDController(0.001);
        MasqUtils.velocityAutoController = new MasqPIDController(0.004);
        MasqUtils.xySpeedController = new MasqPIDController(0.04, 0, 0);
        MasqUtils.xyAngleController = new MasqPIDController(0.05, 0, 0);
        lift.encoder.setWheelDiameter(1);
        X.setWheelDiameter(2);
        intake.setWheelDiameter(2);
        driveTrain.setClosedLoop(true);
        driveTrain.resetEncoders();
        lift.setClosedLoop(true);
        lift.setKp(0.001);
        scaleServos();
        resetServos();
        SkystoneDetector detector = new SkystoneDetector();
        detector.setClippingMargins(100,80,110,70);
        cv = new MasqCV(detector, MasqCV.Cam.WEBCAM, hardwareMap);
    }

 /*   public Library4997.MasqSensors.MasqPositionTracker.MasqPositionTracker createTracker() {
        Library4997.MasqSensors.MasqPositionTracker.MasqPositionTracker tracker = new Library4997.MasqSensors.MasqPositionTracker.MasqPositionTracker(hardwareMap);
        MasqDeadwheel masqDeadwheel = new MasqDeadwheel(intake.motor1, WheelPosition.BOTTOM, Measurement.X,5068) {
            @Override
            public double getInches() {
                return 0;
            }
        };
        tracker.addWheel(masqDeadwheel);
        return tracker;
    }
*/
    private void scaleServos() {
        blockPusher.scaleRange(0, 0.5);
        blockGrabber.scaleRange(0, 0.5);
        blockRotater.scaleRange(0.02, 0.7);
        capper.scaleRange(0.5,1);
        sideGrabber.scaleServos();
    }

    private void resetServos() {
        blockPusher.setPosition(0);
        blockRotater.setPosition(0);
        blockGrabber.setPosition(1);
        foundationHook.raise();
        sideGrabber.reset();
        capper.setPosition(0);
    }
    public void stopDriving(double tolerance) {
        boolean isMoving;
        do {
            isMoving = false;
            driveTrain.setVelocity(0);
            for (MasqMotor motor : driveTrain.getMotors()) {
                if (!tolerance(motor.getVelocity(),0,tolerance)) isMoving = true;
            }
        } while (isMoving);
        driveTrain.setPower(0);
    }
}