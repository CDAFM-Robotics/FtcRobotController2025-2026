package org.firstinspires.ftc.teamcode.common.subsystems;

import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

public class DriveBase {

    HardwareMap hardwareMap;
    Telemetry telemetry;

    private DcMotor frontLeftMotor = null;
    private DcMotor frontRightMotor = null;
    private DcMotor backLeftMotor = null;
    private DcMotor backRightMotor = null;

    // private IMU imu;

    GoBildaPinpointDriver pinpoint;
    private Pose2D pos;


    public DriveBase(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;

        initializeDriveBaseDevices();
    }

    public void initializeDriveBaseDevices() {
        frontLeftMotor = hardwareMap.get(DcMotor.class, "frontLeftMotor");
        frontRightMotor = hardwareMap.get(DcMotor.class, "frontRightMotor");
        backLeftMotor = hardwareMap.get(DcMotor.class, "backLeftMotor");
        backRightMotor = hardwareMap.get(DcMotor.class, "backRightMotor");

        frontLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        frontRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backLeftMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backRightMotor.setDirection(DcMotorSimple.Direction.FORWARD);

        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        // imu = hardwareMap.get(IMU.class, "imu");

        // Get a reference to the sensor
        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");



        // Configure the sensor
        configurePinpoint();

        // Set the location of the robot - this should be the place you are starting the robot from
        pinpoint.setPosition(new Pose2D(DistanceUnit.MM, 0, 0, AngleUnit.RADIANS, 0));


//        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
//            RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
//            RevHubOrientationOnRobot.UsbFacingDirection.UP
//        )
//        );

       // imu.initialize(parameters);
       //  imu.resetYaw();
    }

    public void resetIMU(){
//        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
//                RevHubOrientationOnRobot.LogoFacingDirection.RIGHT,
//                RevHubOrientationOnRobot.UsbFacingDirection.UP
//        )
//        );
//
//        imu.initialize(parameters);
//        imu.resetYaw();

        resetPinpointIMU();

    }

    public void resetPinpointIMU()
    {
        pinpoint.resetPosAndIMU();
    }

    public void configurePinpoint(){

        pinpoint.setOffsets(-116.0, -156.0, DistanceUnit.MM); //Tuned for Archimedes 1.5 31Oct25

        pinpoint.setEncoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD);

        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.FORWARD);

        pinpoint.resetPosAndIMU();
    }

    public void setMotorPowers(double x, double y, double rx, double speed, boolean fieldCentric) {
        pinpoint.update();
        pos = pinpoint.getPosition();


        double heading;
        if (fieldCentric) {
            // heading = -imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
            heading = -pos.getHeading(AngleUnit.RADIANS);
        }
        else {
            heading = 0;
        }

        double rotX = x * Math.cos(heading) - y * Math.sin(heading);
        double rotY = x * Math.sin(heading) + y * Math.cos(heading);

        // put strafing factors here
        rotX = rotX * 1;
        rotY = rotY * 1;

        double denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);

        double frontLeftPower = ((rotY + rotX) * speed + rx) / denominator;
        double backLeftPower = ((rotY - rotX) * speed + rx) / denominator;
        double frontRightPower = ((rotY - rotX) * speed - rx) / denominator;
        double backRightPower = ((rotY + rotX) * speed - rx) / denominator;

        frontLeftMotor.setPower(frontLeftPower);
        backLeftMotor.setPower(backLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backRightMotor.setPower(backRightPower);
        telemetry.addData("Pinpoint", "Heading %.2f, Pos %.2f", heading, pos.getX(DistanceUnit.MM));
        telemetry.addData("fieldCentric",fieldCentric);
        telemetry.addData("powers", "front left: %.2f, front right: %.2f, back left: %.2f, back right: %.2f", frontLeftPower *speed*100, frontRightPower *speed*100, backLeftPower *speed*100, backRightPower *speed*100);
    }

    public void setIndividualMotorPowers(double frontLeftPower, double frontRightPower, double backRightPower, double backLeftPower) {
        frontLeftMotor.setPower(frontLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backRightMotor.setPower(backRightPower);
        backLeftMotor.setPower(backLeftPower);
    }

}
