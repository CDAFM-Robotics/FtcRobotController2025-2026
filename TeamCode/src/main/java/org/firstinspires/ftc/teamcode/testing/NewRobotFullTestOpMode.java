package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

@TeleOp(name = "New Robot Test", group = "Testing")
public class NewRobotFullTestOpMode extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        DcMotorEx intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");

        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        DcMotorEx launcherMotor1 = hardwareMap.get(DcMotorEx.class, "launcherMotor1");
        DcMotorEx launcherMotor2 = hardwareMap.get(DcMotorEx.class, "launcherMotor2");

        DcMotorEx rollerMotor = hardwareMap.get(DcMotorEx.class, "rollerMotor");

        launcherMotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        launcherMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        rollerMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        launcherMotor2.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        double intakePower = 0;

        double outtakePower = 0;

        while (opModeIsActive()) {
            intakePower = -gamepad2.left_stick_y;
            intakeMotor.setPower(intakePower);

            telemetry.addData("Intake Power", intakePower);

            outtakePower = -gamepad2.right_stick_y;

            launcherMotor1.setPower(outtakePower);
            launcherMotor2.setPower(outtakePower);

            if (gamepad1.right_bumper) {
                rollerMotor.setPower(1);
            }
            else {
                rollerMotor.setPower(0);
            }

            telemetry.addData("Outtake Power", outtakePower);
            telemetry.addData("Rolling", gamepad1.a);


            telemetry.update();
        }
    }
}
