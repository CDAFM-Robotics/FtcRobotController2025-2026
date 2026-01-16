package org.firstinspires.ftc.teamcode.testing.archived;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name = "Color Sensor Test", group = "Testing")
@Disabled
public class ColorSensorTestOpMode extends LinearOpMode {

    NormalizedColorSensor colorSensor1;
    NormalizedColorSensor colorSensor2;

    NormalizedRGBA colorSensor1NormalizedColors;
    NormalizedRGBA colorSensor2NormalizedColors;

    enum ArtifactColor {
        PURPLE,
        GREEN,
        NONE,
        UNKNOWN
    }

    ArtifactColor colorSensor1DetectedColor = null;
    ArtifactColor colorSensor2DetectedColor = null;

    double sensor2Distance;
    double sensor1Distance;

    @Override
    public void runOpMode() {

        float gain = 8;

        colorSensor1 = hardwareMap.get(NormalizedColorSensor.class, "colorSensor1Left");
        colorSensor2 = hardwareMap.get(NormalizedColorSensor.class, "colorSensor1Right");

        waitForStart();

        while (opModeIsActive()) {

            colorSensor1.setGain(gain);
            colorSensor2.setGain(gain);

            telemetry.addLine("Color Sensor 1");

            colorSensor1NormalizedColors = colorSensor1.getNormalizedColors();

            telemetry.addLine()
              .addData("Red", "%.3f", colorSensor1NormalizedColors.red)
              .addData("Green", "%.3f", colorSensor1NormalizedColors.green)
              .addData("Blue", "%.3f", colorSensor1NormalizedColors.blue);
            telemetry.addData("Alpha", "%.3f", colorSensor1NormalizedColors.alpha);

            sensor1Distance = ((DistanceSensor) colorSensor1).getDistance(DistanceUnit.CM);
            telemetry.addData("Distance (cm)", "%.3f", sensor1Distance);

            if (sensor1Distance > 3) {
                colorSensor1DetectedColor = ArtifactColor.NONE;
            }
            else if (colorSensor1NormalizedColors.blue > colorSensor1NormalizedColors.green) {
                colorSensor1DetectedColor = ArtifactColor.PURPLE;
            }
            else {
                colorSensor1DetectedColor = ArtifactColor.GREEN;
            }

            telemetry.addData("Detected Color", colorSensor1DetectedColor);

            telemetry.addLine();

            telemetry.addLine("Color Sensor 2");

            colorSensor2NormalizedColors = colorSensor2.getNormalizedColors();

            telemetry.addLine()
              .addData("Red", "%.3f", colorSensor2NormalizedColors.red)
              .addData("Green", "%.3f", colorSensor2NormalizedColors.green)
              .addData("Blue", "%.3f", colorSensor2NormalizedColors.blue);
            telemetry.addData("Alpha", "%.3f", colorSensor2NormalizedColors.alpha);

            sensor2Distance = ((DistanceSensor) colorSensor2).getDistance(DistanceUnit.CM);
            telemetry.addData("Distance (cm)", "%.3f", sensor2Distance);

            if (sensor2Distance > 3) {
                colorSensor2DetectedColor = ArtifactColor.NONE;
            }
            else if (colorSensor2NormalizedColors.blue > colorSensor2NormalizedColors.green) {
                colorSensor2DetectedColor = ArtifactColor.PURPLE;
            }
            else {
                colorSensor2DetectedColor = ArtifactColor.GREEN;
            }

            telemetry.addData("Detected Color", colorSensor2DetectedColor);

            telemetry.addLine();

            if (colorSensor1DetectedColor == colorSensor2DetectedColor) {
                telemetry.addData("Predicted Color", colorSensor1DetectedColor);
            }
            else if (colorSensor2DetectedColor == ArtifactColor.NONE) {
                telemetry.addData("Predicted Color", colorSensor1DetectedColor);
            }
            else if (colorSensor1DetectedColor == ArtifactColor.NONE){
                telemetry.addData("Predicted Color", colorSensor2DetectedColor);
            }
            else {
                telemetry.addData("Predicted Color", ArtifactColor.UNKNOWN);
            }

            telemetry.update();
        }
    }
}
