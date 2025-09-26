package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;

@TeleOp(name = "Outtake test thingy", group = "testing")
public class OuttakeRevStarterBotTestOpMode extends LinearOpMode {

    public DcMotorEx launchMotor = null;
    @Override
    public void runOpMode() throws InterruptedException {

        launchMotor = hardwareMap.get(DcMotorEx.class, "launchMotor");

        waitForStart();

        while (opModeIsActive()) {
            launchMotor.setPower(gamepad1.a ? -1 : 0);
        }
    }
}
