package org.firstinspires.ftc.teamcode.Robots.Falcon;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.DogeForia;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.teamcode.Robots.Falcon.FalconSubSystems.MasqElevator;
import org.firstinspires.ftc.teamcode.Robots.Falcon.FalconSubSystems.MasqRotator;

import Library4997.MasqControlSystems.MasqPurePursuit.MasqPositionTracker;
import Library4997.MasqDriveTrains.MasqDriveTrain;
import Library4997.MasqMotors.MasqMotorSystem;
import Library4997.MasqResources.MasqHelpers.Direction;
import Library4997.MasqResources.MasqHelpers.MasqMotorModel;
import Library4997.MasqResources.MasqUtils;
import Library4997.MasqRobot;
import Library4997.MasqSensors.MasqAdafruitIMU;
import Library4997.MasqSensors.MasqClock;
import Library4997.MasqServos.MasqCRServo;
import Library4997.MasqServos.MasqServo;
import Library4997.MasqWrappers.DashBoard;

/**
 * Created by Archishmaan Peyyety on 6/2/18.
 * Project: MasqLib
 */

public class Falcon extends MasqRobot {
    public MasqAdafruitIMU imu;
    public MasqRotator rotator;
    public MasqElevator lift;
    public MasqServo dumper;
    public MasqCRServo collector;
    public MasqServo adjuster;
    public MasqClock clock;
    public MasqMotorSystem hangSystem;
    public GoldAlignDetector goldAlignDetector;
    public DogeForia dogeForia;
    public void mapHardware(HardwareMap hardwareMap) {
        dash = DashBoard.getDash();
        imu = new MasqAdafruitIMU("imu", hardwareMap);
        driveTrain = new MasqDriveTrain(hardwareMap);
        tracker = new MasqPositionTracker(driveTrain.leftDrive, driveTrain.rightDrive, imu);
        rotator = new MasqRotator(hardwareMap);
        lift = new MasqElevator(hardwareMap);
        dumper = new MasqServo("dumper", hardwareMap);
        collector = new MasqCRServo("collector", hardwareMap);
        adjuster = new MasqServo("adjuster", hardwareMap);
        hangSystem = new MasqMotorSystem("hangOne", "hangTwo", "hang", hardwareMap, MasqMotorModel.NEVEREST40);
        goldAlignDetector = new GoldAlignDetector();
        startOpenCV(hardwareMap);
    }
    private void startOpenCV (HardwareMap hardwareMap) {
        goldAlignDetector.init(hardwareMap.appContext, CameraViewDisplay.getInstance(), 0, true);
        goldAlignDetector.useDefaults();
        goldAlignDetector.alignSize = 200;
        goldAlignDetector.alignPosOffset = 0;
        goldAlignDetector.downscale = 0.4;
        goldAlignDetector.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA;
        goldAlignDetector.maxAreaScorer.weight = 0.005;
        goldAlignDetector.ratioScorer.weight = 5;
        goldAlignDetector.ratioScorer.perfectRatio = 1.0;
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters();
        parameters.vuforiaLicenseKey = MasqUtils.VUFORIA_KEY;
        parameters.fillCameraMonitorViewParent = true;
        parameters.cameraName = getWebCameName(hardwareMap, "Webcam 1");
        dogeForia = new DogeForia(parameters);
        dogeForia.enableConvertFrameToBitmap();
        dogeForia.setDogeCVDetector(goldAlignDetector);
        dogeForia.enableDogeCV();
        dogeForia.showDebug();
        dogeForia.start();
    }
    public void turnTillGold (double speed, Direction direction) {
        clock = new MasqClock();
        while (opModeIsActive() && !goldAlignDetector.getAligned() && imu.getRelativeYaw() <= 165) {
            driveTrain.setVelocity(-speed * direction.value, speed * direction.value);
            dash.create(imu.getRelativeYaw());
            dash.update();
        }
        driveTrain.setVelocity(0, 0);
    }
    public void update () {
        dash.update();
        tracker.updateSystemV2();
    }
}
