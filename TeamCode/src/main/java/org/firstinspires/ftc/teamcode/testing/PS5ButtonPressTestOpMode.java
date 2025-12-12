package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp(name = "PS5 Button Test", group = "testing")

public class PS5ButtonPressTestOpMode extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        waitForStart();

        telemetry.setMsTransmissionInterval(16);
        while (opModeIsActive()) {

            telemetry.addLine("Gamepad 1");

            telemetry.addLine();

            addData("leftStickX", gamepad1.left_stick_x);
            addData("leftStickY", gamepad1.left_stick_y);
            addData("rightStickX", gamepad1.right_stick_x);
            addData("rightStickY", gamepad1.right_stick_y);

            telemetry.addLine();

            addData("leftStickX", gamepad1.touchpad_finger_1_x);
            addData("leftStickY", gamepad1.touchpad_finger_1_y);
            addData("rightStickX", gamepad1.touchpad_finger_2_x);
            addData("rightStickY", gamepad1.touchpad_finger_2_y);

            

            telemetry.addLine();

            addData("leftTrigger", gamepad1.left_trigger);
            addData("rightTrigger", gamepad1.right_trigger);

            telemetry.addLine();

            addData("cross", gamepad1.cross);
            addData("circle", gamepad1.circle);
            addData("square", gamepad1.square);
            addData("triangle", gamepad1.triangle);

            telemetry.addLine();

            addData("dpadUp", gamepad1.dpad_up);
            addData("dpadRight", gamepad1.dpad_right);
            addData("dpadDown", gamepad1.dpad_down);
            addData("dpadLeft", gamepad1.dpad_left);

            telemetry.addLine();

            addData("leftBumper", gamepad1.left_bumper);
            addData("rightBumper", gamepad1.right_bumper);

            telemetry.addLine();

            addData("share", gamepad1.share);
            addData("options", gamepad1.options);

            telemetry.addLine();

            telemetry.addLine("Gamepad 2");

            telemetry.addLine();

            addData("leftStickX", gamepad2.left_stick_x);
            addData("leftStickY", gamepad2.left_stick_y);
            addData("rightStickX", gamepad2.right_stick_x);
            addData("rightStickY", gamepad2.right_stick_y);

            telemetry.addLine();

            addData("leftTrigger", gamepad2.left_trigger);
            addData("rightTrigger", gamepad2.right_trigger);

            telemetry.addLine();

            addData("cross", gamepad2.cross);
            addData("circle", gamepad2.circle);
            addData("square", gamepad2.square);
            addData("triangle", gamepad2.triangle);

            telemetry.addLine();

            addData("dpadUp", gamepad2.dpad_up);
            addData("dpadRight", gamepad2.dpad_right);
            addData("dpadDown", gamepad2.dpad_down);
            addData("dpadLeft", gamepad2.dpad_left);

            telemetry.addLine();

            addData("leftBumper", gamepad2.left_bumper);
            addData("rightBumper", gamepad2.right_bumper);

            telemetry.addLine();

            addData("share", gamepad2.share);
            addData("options", gamepad2.options);

            if(gamepad1.triangle){
                gamepad1.rumble(250);
            }

            telemetry.update();
        }
    }

    public void addData(String message, boolean value) {
        telemetry.addData(message, value);
    }
    public void addData(String message, double value) {
        telemetry.addData(message, "%.4f", value);
    }
}
