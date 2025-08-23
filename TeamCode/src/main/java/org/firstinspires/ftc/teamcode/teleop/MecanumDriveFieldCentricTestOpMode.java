package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.Robot;
@TeleOp(name="Mecanum Drive Field Centric Test", group = "Testing")
public class MecanumDriveFieldCentricTestOpMode extends LinearOpMode {

    @Override
    public void runOpMode() {
        Robot robot = new Robot(this);

        robot.initializeDevices();

        waitForStart();

        while (opModeIsActive()) {
            robot.setMotorPowers(gamepad1.left_stick_x, gamepad1.left_stick_y, gamepad1.right_stick_x, 1);
            telemetry.update();
        }
    }
}
