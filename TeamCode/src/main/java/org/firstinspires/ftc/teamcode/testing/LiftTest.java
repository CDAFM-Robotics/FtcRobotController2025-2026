package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.hardware.ColorSensor;

@TeleOp (name = "LiftTest", group = "Test")
public class LiftTest extends LinearOpMode {
    @Override
    public void runOpMode(){

        DcMotor lift1 = null;
        DcMotor lift2 = null;

        lift1 = hardwareMap.get(DcMotor.class, "lift1");
        lift2 = hardwareMap.get(DcMotor.class, "lift2");

        lift1.setDirection(DcMotor.Direction.FORWARD);
        lift2.setDirection(DcMotorSimple.Direction.REVERSE);



        waitForStart();


        while(opModeIsActive()) {

            //Gamepad gamepad1 = new Gamepad();

           Gamepad currentGamepad1 = new Gamepad();
           Gamepad previousGamepad1 = new Gamepad();

           previousGamepad1.copy(currentGamepad1);
           currentGamepad1.copy(gamepad1);

            if (currentGamepad1.right_trigger != 0 && previousGamepad1.right_trigger == 0) {
                lift1.setPower(1);
                lift2.setPower(1);
            }

            if (currentGamepad1.right_trigger == 0 && previousGamepad1.right_trigger != 0) {
                lift1.setPower(0);
                lift2.setPower(0);
            }

            if (currentGamepad1.left_trigger != 0 && previousGamepad1.left_trigger == 0) {
                lift1.setPower(-0.25);
                lift2.setPower(-0.25);
            }

            if (currentGamepad1.left_trigger == 0 && previousGamepad1.left_trigger != 0) {
                lift1.setPower(0);
                lift2.setPower(0);
            }

            telemetry.addLine()
                    .addData("Current Position Lift1:", lift1.getCurrentPosition())
                    .addData("Current Position Lift2:", lift2.getCurrentPosition());
            telemetry.update();
        }
    }
}
