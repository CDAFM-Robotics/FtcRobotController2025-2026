package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.common.Robot;

@TeleOp(name = "Driver Control Teleop", group = "0teleop")
public class TeleOP extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Robot robot = new Robot(this);

        robot.initializeDevices();

        double drivespeed = 1;
        boolean fieldcentricboolean = true;

        waitForStart();

        Gamepad currentGamepad1 = new Gamepad();
        Gamepad previousGamepad1 = new Gamepad();
        Gamepad currentGamepad2 = new Gamepad();
        Gamepad previousGamepad2 = new Gamepad();

        while(opModeIsActive()){
            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);
            currentGamepad1.copy(gamepad1);
            currentGamepad2.copy(gamepad2);
            robot.setMotorPowers(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, drivespeed, true);

            if(currentGamepad1.left_stick_button && !previousGamepad1.left_stick_button){
                drivespeed = drivespeed == 1 ? 0.5 : 1;
            }

            if(currentGamepad1.back && !previousGamepad1.back){
                fieldcentricboolean = fieldcentricboolean == true ? false : true;
            }
        }
    }
}
