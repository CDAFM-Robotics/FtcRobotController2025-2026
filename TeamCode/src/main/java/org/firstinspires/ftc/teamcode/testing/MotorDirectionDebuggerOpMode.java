package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.Robot;

@TeleOp(name = "motor debugger", group = "testing")
public class MotorDirectionDebuggerOpMode extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(hardwareMap, telemetry);

        double frontLeftPower = 0;
        double frontRightPower = 0;
        double backLeftPower = 0;
        double backRightPower = 0;

        waitForStart();

        while (opModeIsActive()) {
            frontLeftPower = gamepad1.x ? 1 : 0;
            frontRightPower = gamepad1.y ? 1 : 0;
            backRightPower = gamepad1.b ? 1 : 0;
            backLeftPower = gamepad1.a ? 1 : 0;

            robot.getDriveBase().setIndividualMotorPowers(frontLeftPower, frontRightPower, backRightPower, backLeftPower);

        }
    }
}
