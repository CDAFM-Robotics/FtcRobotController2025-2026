package org.firstinspires.ftc.teamcode.testing;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.TouchSensor;

@Disabled
@TeleOp(name = "magneticLimitSwitchTestOpMode", group = "0testing")
public class MagneticLimitSwitchTestOpMode extends LinearOpMode {

    TouchSensor magneticSwitch;
    @Override
    public void runOpMode() throws InterruptedException{

        magneticSwitch = hardwareMap.get(TouchSensor.class, "magneticLimitSwitch");

        boolean on = false;

        waitForStart();
        while(opModeIsActive()){
            if(magneticSwitch.isPressed()){
                on = true;
            }
            if(!magneticSwitch.isPressed()){
                on = false;
            }

            telemetry.addData("on:", on);
            telemetry.update();
        }
    }
}
