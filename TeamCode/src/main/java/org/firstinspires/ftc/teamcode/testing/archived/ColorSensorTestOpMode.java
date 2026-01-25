package org.firstinspires.ftc.teamcode.testing.archived;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

@TeleOp(name = "Color Sensor Test", group = "Testing")
// @Disabled
public class ColorSensorTestOpMode extends LinearOpMode {

    NormalizedColorSensor colorSensorIntakeLeft;
    NormalizedColorSensor colorSensorIntakeRight;

    NormalizedColorSensor colorSensorOutputFront;
    NormalizedColorSensor colorSensorOutputB;

    NormalizedColorSensor colorSensorAltFront;
    NormalizedColorSensor colorSensorAltB;


    NormalizedRGBA colorSensorANormalizedColors;
    NormalizedRGBA colorSensorBNormalizedColors;

    enum ArtifactColor {
        PURPLE,
        GREEN,
        NONE,
        UNKNOWN
    }

    ArtifactColor colorSensorADetectedColor = null;
    ArtifactColor colorSensorBDetectedColor = null;

    ArtifactColor predictedColor = null;


    double sensorADistance = 0.0;
    double sensorBDistance = 0.0;
    float gain = 8;


    @Override
    public void runOpMode() {


        colorSensorIntakeLeft = hardwareMap.get(NormalizedColorSensor.class, "colorSensor1Left");
        colorSensorIntakeRight = hardwareMap.get(NormalizedColorSensor.class, "colorSensor1Right");
        colorSensorOutputFront = hardwareMap.get(NormalizedColorSensor.class, "colorSensorOutputFront");
        colorSensorOutputB = hardwareMap.get(NormalizedColorSensor.class, "colorSensorOutputB");
        colorSensorAltFront = hardwareMap.get(NormalizedColorSensor.class, "colorSensorAltFront");
        colorSensorAltB = hardwareMap.get(NormalizedColorSensor.class, "colorSensorAltB");


        waitForStart();

        while (opModeIsActive()) {


            telemetry.addLine("Color Sensor Intake");
            getColorSensor(colorSensorIntakeLeft, colorSensorIntakeRight);
            telemetry.addLine();
            telemetry.addLine("Color Sensor Output");
            getColorSensor(colorSensorOutputFront, colorSensorOutputB);

            telemetry.addLine();
            telemetry.addLine("Color Sensor Alt");
            getColorSensor(colorSensorAltFront, colorSensorAltB);

            telemetry.update();


        }
    }

    public void getColorSensor(NormalizedColorSensor colorSensorA, NormalizedColorSensor colorSensorB)
    {
        colorSensorA.setGain(gain);
        colorSensorB.setGain(gain);

        colorSensorANormalizedColors = colorSensorA.getNormalizedColors();
        sensorADistance = ((DistanceSensor) colorSensorA).getDistance(DistanceUnit.CM);

        // TODO Changed "Detect Distance to 6.5 for both (Output and Alternate Sensor Distance in real program)
        // TODO: ~15 = Black Divider wall
        // TODO: ~15 = No Ball
        // TODO: BUT having detect distance too high may cause false-trigger on intake slot causing ball-stuck (sugg: ~3-4.5)

        if (sensorADistance > 6.5) {
            colorSensorADetectedColor = ArtifactColor.NONE;
        }
        else if (colorSensorANormalizedColors.blue > colorSensorANormalizedColors.green) {
            colorSensorADetectedColor = ArtifactColor.PURPLE;
        }
        else { // telemetry.addData("Predicted Color", ArtifactColor.UNKNOWN);
            colorSensorADetectedColor = ArtifactColor.GREEN;
        }

        colorSensorBNormalizedColors = colorSensorB.getNormalizedColors();
        sensorBDistance = ((DistanceSensor) colorSensorB).getDistance(DistanceUnit.CM);

        if (sensorBDistance > 6.5) {
            colorSensorBDetectedColor = ArtifactColor.NONE;
        }
        else if (colorSensorBNormalizedColors.blue > colorSensorBNormalizedColors.green) {
            colorSensorBDetectedColor = ArtifactColor.PURPLE;
        }
        else {
            colorSensorBDetectedColor = ArtifactColor.GREEN;
        }

        // Predicted Color
        if (colorSensorADetectedColor == colorSensorBDetectedColor) {
            predictedColor =  colorSensorADetectedColor;
        }
        else if (colorSensorBDetectedColor == ArtifactColor.NONE) {
            predictedColor = colorSensorADetectedColor;
        }
        else if (colorSensorADetectedColor == ArtifactColor.NONE){
            predictedColor = colorSensorBDetectedColor;
        }
        else {
            predictedColor = ArtifactColor.UNKNOWN;
        }

        telemetry.addLine()
                .addData("RGBA", "%.2f, %.2f, %.2f, %.2f / %.2f, %.2f, %.2f, %.2f",
                        colorSensorANormalizedColors.red, colorSensorANormalizedColors.green, colorSensorANormalizedColors.blue, colorSensorANormalizedColors.alpha,
                        colorSensorBNormalizedColors.red, colorSensorBNormalizedColors.green, colorSensorBNormalizedColors.blue, colorSensorBNormalizedColors.alpha);
        telemetry.addData("Distance (cm)", "%.2f / %.2f", sensorADistance, sensorBDistance);
        telemetry.addData("Detected Color",  "%s / %s", colorSensorADetectedColor, colorSensorBDetectedColor);
        telemetry.addData("Predicted Color", predictedColor);

    }
}
