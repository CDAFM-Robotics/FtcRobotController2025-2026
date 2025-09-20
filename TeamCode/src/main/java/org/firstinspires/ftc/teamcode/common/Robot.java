package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

public class Robot {

    private ElapsedTime elapsedTime = new ElapsedTime();

    private DcMotor frontLeftMotor = null;
    private DcMotor frontRightMotor = null;
    private DcMotor backLeftMotor = null;
    private DcMotor backRightMotor = null;

    private IMU imu;

    private HardwareMap hwMap;
    private Telemetry telemetry;

    /***************************************************************************************************
     ******************************************** CONSTANTS ********************************************
     ***************************************************************************************************/


    public double MAX_ROTATIONAL_VELOCITY = Math.toRadians(291.02 / 2.0);





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

    private double prevTime = 0;
    private double currentTime = 0;
    private double rotX;
    private double rotY;
    private double denominator;
    private double frontLeftPower;
    private double frontRightPower;
    private double backLeftPower;
    private double backRightPower;
    private double heading;
    private double targetHeading;
    private double error;
    private double kp = -1;

    public void setMotorPowers(double x, double y, double rx, double speed, boolean fieldCentric) {

/*
        currentTime = elapsedTime.time();

        targetHeading = (currentTime - prevTime) * MAX_ROTATIONAL_VELOCITY * rx;
        targetHeading -= targetHeading > Math.PI ? Math.PI * 2 : (targetHeading < Math.PI ? -Math.PI*2 : 0);
*/

        if (fieldCentric) {
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

        // error = targetHeading - heading;

        frontLeftPower = ((rotY + rotX) * speed + rx) / denominator;
        backLeftPower = ((rotY - rotX) * speed + rx) / denominator;
        frontRightPower = ((rotY - rotX) * speed - rx) / denominator;
        backRightPower = ((rotY + rotX) * speed - rx) / denominator;



        frontLeftMotor.setPower(frontLeftPower);
        backLeftMotor.setPower(backLeftPower);
        frontRightMotor.setPower(frontRightPower);
        backRightMotor.setPower(backRightPower);
        telemetry.addData("Heading", heading);
        telemetry.addData("powers", "front left: %.2f, front right: %.2f, back left: %.2f, back right: %.2f",frontLeftPower*speed*100, frontRightPower*speed*100, backLeftPower*speed*100, backRightPower*speed*100);

        // prevTime = currentTime;

    }
}
