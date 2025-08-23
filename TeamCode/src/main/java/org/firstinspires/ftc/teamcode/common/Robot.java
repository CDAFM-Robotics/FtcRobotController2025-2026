package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Robot {

    public DcMotor frontLeftMotor = null;
    public DcMotor frontRightMotor = null;
    public DcMotor backLeftMotor = null;
    public DcMotor backRightMotor = null;

    public IMU imu;

    HardwareMap hwMap;
    Telemetry telemetry;

    public Robot(OpMode opMode) {
        hwMap = opMode.hardwareMap;
        telemetry = opMode.telemetry;
    }



    public void initializeDevices() {
        initializeDriveDevices();
        initializeArmDevices();
        initializeSensorDevices();
    }

    public void initializeDriveDevices() {
        frontLeftMotor = hwMap.get(DcMotor.class, "frontLeftMotor");
        frontRightMotor = hwMap.get(DcMotor.class, "frontRightMotor");
        backLeftMotor = hwMap.get(DcMotor.class, "backLeftMotor");
        backRightMotor = hwMap.get(DcMotor.class, "backRightMotor");

        frontLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        frontRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        backLeftMotor.setDirection(DcMotorSimple.Direction.FORWARD);
        backRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);

        frontLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        frontRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backLeftMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        backRightMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    }

    public void initializeArmDevices() {}

    public void initializeSensorDevices() {
        imu = hwMap.get(IMU.class, "imu");

        IMU.Parameters parameters = new IMU.Parameters(new RevHubOrientationOnRobot(
                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                RevHubOrientationOnRobot.UsbFacingDirection.LEFT
            )
        );

        imu.initialize(parameters);
        imu.resetYaw();
    }

    double rotX;
    double rotY;
    double denominator;
    double frontLeftPower;
    double frontRightPower;
    double backLeftPower;
    double backRightPower;
    double heading;

    public void setMotorPowers(double x, double y, double rx, double speed, boolean fieldCentric) {
        if(fieldCentric) {
            heading = -imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.RADIANS);
        }
        else {
            heading = 0;
        }

        rotX = x * Math.cos(-heading) - y * Math.sin(-heading);
        rotY = x * Math.sin(-heading) + y * Math.cos(-heading);

        // put strafing factors here
        rotX = rotX * 1;
        rotY = rotY * 1;

        denominator = Math.max(Math.abs(rotY) + Math.abs(rotX) + Math.abs(rx), 1);

        frontLeftPower = (rotY - rotX - rx) / denominator;
        backLeftPower = (rotY + rotX - rx) / denominator;
        frontRightPower = (rotY + rotX + rx) / denominator;
        backRightPower = (rotY - rotX + rx) / denominator;

        frontLeftMotor.setPower(frontLeftPower * speed);
        backLeftMotor.setPower(backLeftPower * speed);
        frontRightMotor.setPower(frontRightPower * speed);
        backRightMotor.setPower(backRightPower * speed);
        telemetry.addData("Heading", heading);
        telemetry.addData("powers", "front left: %.2f, front right: %.2f, back left: %.2f, back right: %.2f",frontLeftPower*speed*100, frontRightPower*speed*100, backLeftPower*speed*100, backRightPower*speed*100);

    }
}
