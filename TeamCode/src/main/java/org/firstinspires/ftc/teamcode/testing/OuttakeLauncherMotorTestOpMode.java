package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.common.Robot;

@TeleOp(name = "Outtake and Intake Test", group = "testing")
public class OuttakeLauncherMotorTestOpMode extends LinearOpMode {

    DcMotor outtakeMotorLeft = null;
    DcMotor outtakeMotorRight = null;
    DcMotor intakeMotor = null;

    @Override
    public void runOpMode() throws InterruptedException {

        telemetry.addData("Status", "Initializing");
        telemetry.update();

        //Robot robot = new Robot(this);

        outtakeMotorLeft = hardwareMap.get(DcMotor.class, "outtakeMotorLeft");
        outtakeMotorRight = hardwareMap.get(DcMotor.class, "outtakeMotorRight");
        intakeMotor = hardwareMap.get(DcMotor.class, "intakeMotor");

        outtakeMotorLeft.setDirection(DcMotorSimple.Direction.FORWARD);
        outtakeMotorRight.setDirection(DcMotorSimple.Direction.REVERSE);

        double launchSpeed = 0;

        Gamepad currentGamepad1 = new Gamepad();
        Gamepad previousGamepad1 = new Gamepad();
        Gamepad currentGamepad2 = new Gamepad();
        Gamepad previousGamepad2 = new Gamepad();

        telemetry.addData("Status", "Initialized");
        telemetry.update();

        waitForStart();

        while (opModeIsActive()) {

            telemetry.addData("Status", "Running");

            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);
            currentGamepad1.copy(gamepad1);
            currentGamepad2.copy(gamepad2);

            //robot.setMotorPowers(currentGamepad1.left_stick_x, -currentGamepad1.left_stick_y, currentGamepad1.right_stick_x, 1, true);

            if (currentGamepad2.a && !previousGamepad2.a) {
                launchSpeed += 0.1;
            }
            if (currentGamepad2.b && !previousGamepad2.b) {
                launchSpeed -= 0.1;
            }
            if (currentGamepad2.right_bumper && !previousGamepad2.right_bumper) {
                launchSpeed += 0.01;
            }
            if (currentGamepad2.left_bumper && !previousGamepad2.left_bumper) {
                launchSpeed += 0.01;
            }

            outtakeMotorLeft.setPower(launchSpeed);
            outtakeMotorRight.setPower(launchSpeed);

            intakeMotor.setPower(-currentGamepad1.left_stick_y);

            telemetry.addData("Launch Speed", launchSpeed);
            telemetry.update();
        }
    }
}
