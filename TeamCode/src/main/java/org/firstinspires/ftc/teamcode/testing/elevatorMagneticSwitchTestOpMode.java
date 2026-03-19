package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.TouchSensor;

@TeleOp(name = "elevatorMagneticSwitchTEST", group = "0testing")
public class elevatorMagneticSwitchTestOpMode extends LinearOpMode {
    TouchSensor elevatorMagneticSwitch;

    @Override
    public void runOpMode() throws InterruptedException {
        elevatorMagneticSwitch = hardwareMap.get(TouchSensor.class, "magneticLimitSwitch");

        waitForStart();
        while(opModeIsActive()){

        }
    }
}
