package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "New Intake Test", group = "Testing")
public class NewIntakeMotorTestOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotorEx launcherMotor1 = hardwareMap.get(DcMotorEx.class, "launcherMotor1");
        DcMotorEx launcherMotor2 = hardwareMap.get(DcMotorEx.class, "launcherMotor2");

        launcherMotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        launcherMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        launcherMotor2.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        double power = 0;

        while (opModeIsActive()) {

            power = -gamepad2.right_stick_y;

            launcherMotor1.setPower(power);
            launcherMotor2.setPower(power);

            telemetry.addData("Power", -gamepad2.right_stick_y);
            telemetry.update();
        }

    }
}
