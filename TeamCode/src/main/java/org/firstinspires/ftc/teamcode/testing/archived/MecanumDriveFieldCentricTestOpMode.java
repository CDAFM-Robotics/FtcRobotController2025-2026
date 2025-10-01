package org.firstinspires.ftc.teamcode.testing.archived;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.common.Robot;
@TeleOp(name="Mecanum Drive Field Centric Test", group = "Testing")
@Disabled
public class MecanumDriveFieldCentricTestOpMode extends LinearOpMode {

    @Override
    public void runOpMode() {
        Robot robot = new Robot(hardwareMap, telemetry);

        Gamepad currentGamepad1 = new Gamepad();
        Gamepad previousGamepad1 = new Gamepad();
        Gamepad currentGamepad2 = new Gamepad();
        Gamepad previousGamepad2 = new Gamepad();

        boolean fieldcentric = true;

        waitForStart();

        while (opModeIsActive()) {
            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);
            currentGamepad1.copy(gamepad1);
            currentGamepad2.copy(gamepad2);

            if (currentGamepad1.start && !previousGamepad1.start) {
                fieldcentric = !fieldcentric;
            }

            fieldcentric = (currentGamepad1.start && !previousGamepad1.start) != fieldcentric;

            robot.getDriveBase().setMotorPowers(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, 1, fieldcentric);
            telemetry.update();
        }
    }
}