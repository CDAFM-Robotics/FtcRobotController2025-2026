package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.sun.tools.javac.code.Scope;

import org.firstinspires.ftc.teamcode.common.Robot;

@TeleOp(name = "Axon Servo Test", group = "testing")
public class AxonPowerServoTestOpMode extends LinearOpMode {

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

        double voltageOffset = 0.228;
        double voltageScaler = 27/0.2815; // 27 degrees / 0.2815 on average change b/t every number


        Gamepad currentGamepad1 = new Gamepad();
        Gamepad previousGamepad1 = new Gamepad();
        Gamepad currentGamepad2 = new Gamepad();
        Gamepad previousGamepad2 = new Gamepad();

        waitForStart();

        while (opModeIsActive()) {

            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);
            currentGamepad1.copy(gamepad1);
            currentGamepad2.copy(gamepad2);

            if (currentGamepad1.a && !previousGamepad1.a) {
                position += 0.1;
            }
            if (currentGamepad1.b && !previousGamepad1.b) {
                position -= 0.1;
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
            telemetry.addData("set position", position * 270);
            telemetry.addData("measured position", mpos);
            telemetry.addData("Voltage: ", axon_position_V.getVoltage());
            telemetry.update();
        }
    }
}
