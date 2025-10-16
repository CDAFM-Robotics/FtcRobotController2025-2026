package org.firstinspires.ftc.teamcode.common.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Indexer {

    HardwareMap hardwareMap;
    Telemetry telemetry;

    Servo indexerServo = null;

    NormalizedColorSensor colorSensor1Left = null;
    NormalizedColorSensor colorSensor1Right = null;
    NormalizedColorSensor colorSensor2Left = null;
    NormalizedColorSensor colorSensor2Right = null;
    NormalizedColorSensor colorSensor3Left = null;
    NormalizedColorSensor colorSensor3Right = null;

    public double POSITION_INDEXER_SERVO_FIRST_BALL_INPUT = 0.944444444;
    public double POSITION_INDEXER_SERVO_SECOND_BALL_INPUT = 0.055555556;
    public double POSITION_INDEXER_SERVO_THIRD_BALL_INPUT = 0.5;
    public double POSITION_INDEXER_SERVO_THIRD_BALL_OUTPUT = 0.10;//was 0.07
    public double POSITION_INDEXER_SERVO_SECOND_BALL_OUTPUT = 0.51;//was 0.5
    public double POSITION_INDEXER_SERVO_FIRST_BALL_OUTPUT = 0.89;//was 0.93

    public class RotateIndexerAction implements Action {

        private boolean initialized = false;

        public double position;

        public RotateIndexerAction(double position) {
            this.position = position;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                indexerServo.setPosition(position);
                initialized = true;
            }
            return true;
        }
    }

    public Action getRotateIndexerAction(double position) {
        return new SequentialAction(
            new RotateIndexerAction(position),
            new SleepAction(1000)
        );
    }

    public Action getGoToFirstBallAction() {
        return getRotateIndexerAction(POSITION_INDEXER_SERVO_SECOND_BALL_OUTPUT);
    }

    public Action getGoToSecondBallAction() {
        return getRotateIndexerAction(POSITION_INDEXER_SERVO_SECOND_BALL_OUTPUT);
    }

    public Action getGoToThirdBallAction() {
        return getRotateIndexerAction(POSITION_INDEXER_SERVO_THIRD_BALL_OUTPUT);
    }

    public enum ArtifactColor {
        PURPLE,
        GREEN,
        NONE,
        UNKNOWN
    }

    public Indexer(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        initializeIndexerDevices();
    }

    public void initializeIndexerDevices() {
        indexerServo = hardwareMap.get(Servo.class, "indexerServo");

        colorSensor1Left = hardwareMap.get(NormalizedColorSensor.class, "colorSensor1Left");
        colorSensor1Right = hardwareMap.get(NormalizedColorSensor.class, "colorSensor1Right");
        colorSensor2Left = hardwareMap.get(NormalizedColorSensor.class, "colorSensor2Left");
        colorSensor2Right = hardwareMap.get(NormalizedColorSensor.class, "colorSensor2Right");
        colorSensor3Left = hardwareMap.get(NormalizedColorSensor.class, "colorSensor3Left");
        colorSensor3Right = hardwareMap.get(NormalizedColorSensor.class, "colorSensor3Right");

        colorSensor1Left.setGain(8);
        colorSensor1Right.setGain(8);
        colorSensor2Left.setGain(8);
        colorSensor2Right.setGain(8);
        colorSensor3Left.setGain(8);
        colorSensor3Right.setGain(8);

        indexerServo.setPosition(POSITION_INDEXER_SERVO_SECOND_BALL_OUTPUT);
    }

    private ArtifactColor getPredictedColor(NormalizedRGBA sensor1RGBA, NormalizedRGBA sensor2RGBA, double sensor1Distance, double sensor2Distance) {

        ArtifactColor sensor1DetectedColor;

        if (sensor1Distance > 3) {
            sensor1DetectedColor = ArtifactColor.NONE;
        }
        else if (sensor1RGBA.blue > sensor1RGBA.green) {
            sensor1DetectedColor = ArtifactColor.PURPLE;
        }
        else {
            sensor1DetectedColor = ArtifactColor.GREEN;
        }

        ArtifactColor sensor2DetectedColor;

        if (sensor2Distance > 3) {
            sensor2DetectedColor = ArtifactColor.NONE;
        }
        else if (sensor2RGBA.blue > sensor2RGBA.green) {
            sensor2DetectedColor = ArtifactColor.PURPLE;
        }
        else {
            sensor2DetectedColor = ArtifactColor.GREEN;
        }

        if (sensor1DetectedColor == sensor2DetectedColor) {
            return sensor1DetectedColor;
        }
        else if (sensor2DetectedColor == ArtifactColor.NONE) {
            return  sensor1DetectedColor;
        }
        else if (sensor1DetectedColor == ArtifactColor.NONE){
            return  sensor2DetectedColor;
        }
        else {
            return  ArtifactColor.UNKNOWN;
        }
    }

    public ArtifactColor[] getBallColors() {

        ArtifactColor[] colors = new ArtifactColor[3];

        colors[0] = getPredictedColor(colorSensor1Left.getNormalizedColors(), colorSensor1Right.getNormalizedColors(), ((DistanceSensor) colorSensor1Left).getDistance(DistanceUnit.CM), ((DistanceSensor) colorSensor1Right).getDistance(DistanceUnit.CM));
        colors[1] = getPredictedColor(colorSensor2Left.getNormalizedColors(), colorSensor2Right.getNormalizedColors(), ((DistanceSensor) colorSensor2Left).getDistance(DistanceUnit.CM), ((DistanceSensor) colorSensor2Right).getDistance(DistanceUnit.CM));
        colors[2] = getPredictedColor(colorSensor3Left.getNormalizedColors(), colorSensor3Right.getNormalizedColors(), ((DistanceSensor) colorSensor3Left).getDistance(DistanceUnit.CM), ((DistanceSensor) colorSensor3Right).getDistance(DistanceUnit.CM));

        return colors;
    }

    public double getIndexerPosition() {
        return indexerServo.getPosition();
    }

    public void rotateToPosition(double position) {
        indexerServo.setPosition(position);
    }

    public void rotateClock() {
        double position = indexerServo.getPosition();
        if ((Math.round(position*100.0))/100.0 == POSITION_INDEXER_SERVO_THIRD_BALL_OUTPUT) {
            indexerServo.setPosition(POSITION_INDEXER_SERVO_SECOND_BALL_OUTPUT);
        }
        else if((Math.round(position*100.0))/100.0 == POSITION_INDEXER_SERVO_SECOND_BALL_OUTPUT) {
            indexerServo.setPosition(POSITION_INDEXER_SERVO_FIRST_BALL_OUTPUT);
        }
    }

    public void rotateCounterClock() {
        double position = indexerServo.getPosition();
        if ((Math.round(position*100.0))/100.0 == POSITION_INDEXER_SERVO_FIRST_BALL_OUTPUT) {
            indexerServo.setPosition(POSITION_INDEXER_SERVO_SECOND_BALL_OUTPUT);
        }
        else if ((Math.round(position*100.0))/100.0 == POSITION_INDEXER_SERVO_SECOND_BALL_OUTPUT) {
            indexerServo.setPosition(POSITION_INDEXER_SERVO_THIRD_BALL_OUTPUT);
        }
    }

}
