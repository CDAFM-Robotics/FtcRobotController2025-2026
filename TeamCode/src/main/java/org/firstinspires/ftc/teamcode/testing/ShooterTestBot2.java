package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.common.Robot;

@TeleOp(name="shooter Test", group = "Testing")
public class ShooterTestBot2 extends LinearOpMode {
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

        shooterRightMotor.setDirection(DcMotorSimple.Direction.REVERSE);


        waitForStart();

        double power = 0;



        while (opModeIsActive()) {
            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);
            currentGamepad1.copy(gamepad1);
            currentGamepad2.copy(gamepad2);

            if (currentGamepad2.x && !previousGamepad2.x) {
                power += 0.1;

                shooterLeftMotor.setPower(power);
                shooterRightMotor.setPower(power);

            }

            if (currentGamepad2.y && ! previousGamepad2.y) {
                power -= 0.1;

                shooterLeftMotor.setPower(power);
                shooterRightMotor.setPower(power);
            }
            telemetry.addData("motor left power", shooterLeftMotor.getPower());
            telemetry.addData("motor right power", shooterRightMotor.getPower());
            //robot.setMotorPowers(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, 1);
            telemetry.update();
        }
    }
}

