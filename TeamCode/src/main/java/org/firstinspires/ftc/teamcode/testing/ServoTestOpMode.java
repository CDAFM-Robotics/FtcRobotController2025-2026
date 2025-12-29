package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.common.Robot;

@TeleOp(name = "Servo Test", group = "testing")
public class ServoTestOpMode extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(hardwareMap, telemetry);

        AnalogInput axon_position_V;
        axon_position_V = hardwareMap.get(AnalogInput.class, "analog0");

        // axon_position_V interpolates between 0-3.3V based on real position in range
        // x = pos / 3.3 * 360

        telemetry.setMsTransmissionInterval(50);

        double position = 0;
        double mpos = 0;

        double led_power = 0.0;

        double voltageOffset = 0.228;
        double voltageScaler = 27/0.2815; // 27 degrees / 0.2815 on average change b/t every number


        Gamepad currentGamepad1 = new Gamepad();
        Gamepad previousGamepad1 = new Gamepad();
        Gamepad currentGamepad2 = new Gamepad();
        Gamepad previousGamepad2 = new Gamepad();

        waitForStart();

        // 2025 - 2026 (Archimedes)
        // Control HUB
        // 0 Indexer Servo (Axoon)
        // 1 Kicker
        // 2 Undef
        // 3 UNdef
        // 4 kickStandLight
        // 5 rightKickStand

        // Exp Hub
        // 0 leftKickStand
        // 1 Undef
        // 2 Undef
        // 3 Undef
        // 4 Undef
        // 5 Undef

        while (opModeIsActive()) {

            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);
            currentGamepad1.copy(gamepad1);
            currentGamepad2.copy(gamepad2);

            // A/B Test Axon (Indexer)
            if (currentGamepad1.a && !previousGamepad1.a) {
                position += 0.1;
            }
            if (currentGamepad1.b && !previousGamepad1.b) {
                position -= 0.1;
            }

            // X/Y Kick Stand   (Left = ControlHub P5 / Right = ExpHub P0
            if (currentGamepad1.x != previousGamepad1.x) {
                robot.getDriveBase().setKickStand();
            }

            if (currentGamepad1.y != previousGamepad1.y) {
                robot.getDriveBase().resetKickStand();
            }


            // U/D Kick Stand Lights
            if (currentGamepad1.dpad_up != previousGamepad1.dpad_up) {
                robot.getDriveBase().setKickStandLight();
                led_power = 1.0;
            }

            if (currentGamepad1.dpad_down != previousGamepad1.dpad_down) {
                robot.getDriveBase().resetKickStandLight();
                led_power = 0.0;
            }

            // L/R Kick Stand Lights
            if (currentGamepad1.dpad_right != previousGamepad1.dpad_right) {
                led_power += 0.1;
                if (led_power >= 1.0)
                {
                    led_power = 1.0;
                }
                robot.getDriveBase().adjustKickStandLight(led_power);
            }

            if (currentGamepad1.dpad_left != previousGamepad1.dpad_left) {
                led_power -= 0.1;
                if (led_power <= 0.0)
                {
                    led_power = 0.0;
                }
                robot.getDriveBase().adjustKickStandLight(led_power);
            }



            /*

            pos     deg     voltage

            0.0     0       0.228
            0.1     27      0.509
            0.2     54      0.794
            0.3     81      1.070
            0.4     108     1.355
            0.5     135     1.634
            0.6     162     1.918
            0.7     189     2.201
            0.8     216     2.481
            0.9     243     2.760
            1.0     270     3.043







             */



            robot.getIndexer().rotateToPosition(position);
            mpos = (axon_position_V.getVoltage() - voltageOffset) * voltageScaler;
            telemetry.addLine("A/B: Indexer,  X/Y: KickStand,  Up/Dn: kickStandLight,  RT: Kicker");
            telemetry.addData("Axon set position: ", position * 270);
            telemetry.addData("measured position: ", mpos);
            telemetry.addData("Voltage: ", axon_position_V.getVoltage());
            telemetry.addData("light power: ", led_power);
            telemetry.update();
        }
    }
}
