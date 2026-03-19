package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.common.Robot;

@TeleOp(name="Shooter Test OpMode", group = "Testing")
public class ShooterTestOpMode extends LinearOpMode {
    public DcMotor shooterLeftMotor = null;
    public DcMotor shooterRightMotor = null;

    Gamepad currentGamepad1 = new Gamepad();
    Gamepad previousGamepad1 = new Gamepad();
    Gamepad currentGamepad2 = new Gamepad();
    Gamepad previousGamepad2 = new Gamepad();


    @Override
    public void runOpMode() {

        //Robot robot = new Robot(this);

        //robot.initializeDevices();
        shooterLeftMotor = hardwareMap.get(DcMotor.class, "shooterLeftMotor");
        shooterRightMotor = hardwareMap.get(DcMotor.class, "shooterRightMotor");


        waitForStart();

        while (opModeIsActive()) {
            if (currentGamepad2.x && !previousGamepad2.x) {
                shooterLeftMotor.setPower(0.1);
                shooterLeftMotor.setPower(-0.1);
            }

            //robot.setMotorPowers(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, 1);
            telemetry.update();
        }
    }
}
