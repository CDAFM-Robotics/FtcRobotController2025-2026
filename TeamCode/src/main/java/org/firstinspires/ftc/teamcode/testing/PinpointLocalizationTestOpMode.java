package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.roadrunner.Pose2d;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@TeleOp(name = "Pinpoint localizer", group = "Testing")
public class PinpointLocalizationTestOpMode extends LinearOpMode {

    private static final Logger log = LoggerFactory.getLogger(PinpointLocalizationTestOpMode.class);
    private double parYTicks = -2343.70;
    private double perpXTicks = -3138.34;
    private double inPerTick = 0.001957;

    private Pose2d txWorldPinpoint;
    private Pose2d txPinpointRobot = new Pose2d(0, 0, 0);

    GoBildaPinpointDriver pinpoint;

    @Override
    public void runOpMode() {

        pinpoint = hardwareMap.get(GoBildaPinpointDriver.class, "pinpoint");

        double mmPerTick = inPerTick * 25.4;
        pinpoint.setEncoderResolution(1 / mmPerTick, DistanceUnit.MM);

        // pinpoint.setOffsets(mmPerTick * parYTicks, mmPerTick * perpXTicks, DistanceUnit.MM);
        //TODO COPIED FROM drivebase.java
        pinpoint.setOffsets(8, -3.25, DistanceUnit.INCH); //Tuned for 2026 Bot2 17Feb26

        pinpoint.setEncoderDirections(GoBildaPinpointDriver.EncoderDirection.FORWARD,
                GoBildaPinpointDriver.EncoderDirection.REVERSED);

        pinpoint.resetPosAndIMU();

        txWorldPinpoint = new Pose2d(0, 0, 0);

        waitForStart();

        while (opModeIsActive()) {
            pinpoint.update();

            telemetry.addData("x", pinpoint.getPosX(DistanceUnit.INCH));
            telemetry.addData("y", pinpoint.getPosY(DistanceUnit.INCH));
            telemetry.addData("heading", pinpoint.getHeading(AngleUnit.RADIANS));

            telemetry.update();
        }
    }
}
