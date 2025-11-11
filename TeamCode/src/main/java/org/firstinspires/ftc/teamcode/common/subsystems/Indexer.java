package org.firstinspires.ftc.teamcode.common.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.teamcode.common.Robot;
import org.firstinspires.ftc.teamcode.common.util.RunTimeoutAction;
import org.firstinspires.ftc.teamcode.common.util.WaitUntilAction;

public class Indexer {

    HardwareMap hardwareMap;
    Telemetry telemetry;

    Servo indexerServo = null;
    private ElapsedTime timeSinceTurnIndex = new ElapsedTime();
    private int nextEmptySlot;
    private int nextShootSlot;
    private int nextPurpleSlot;

    NormalizedColorSensor colorSensor1Left = null;
    NormalizedColorSensor colorSensor1Right = null;
    NormalizedColorSensor colorSensor2Left = null;
    NormalizedColorSensor colorSensor2Right = null;
    NormalizedColorSensor colorSensor3Left = null;
    NormalizedColorSensor colorSensor3Right = null;

    AnalogInput indexerServoVoltage = null;

    public Robot.ArtifactColor[] artifactColorArray = new Robot.ArtifactColor[] {Robot.ArtifactColor.NONE, Robot.ArtifactColor.NONE, Robot.ArtifactColor.NONE};

    //public final double POSITION_INDEXER_SERVO_SLOT_ZERO_OUTPUT = 0.10;//was 0.07 one is at wait; two is at intake
    //public final double POSITION_INDEXER_SERVO_SLOT_TWO_INTAKE = POSITION_INDEXER_SERVO_SLOT_ZERO_OUTPUT;
    //public final double POSITION_INDEXER_SERVO_SLOT_ONE_OUTPUT = 0.51;//was 0.5 zero is at intake; two is at wait
    //public final double POSITION_INDEXER_SERVO_SLOT_ZERO_INTAKE = POSITION_INDEXER_SERVO_SLOT_ONE_OUTPUT;
    //public final double POSITION_INDEXER_SERVO_SLOT_TWO_OUTPUT = 0.89;//was 0.93 zero is at wait; one is at intake
    //public final double POSITION_INDEXER_SERVO_SLOT_ONE_INTAKE = POSITION_INDEXER_SERVO_SLOT_TWO_OUTPUT;

    public final double POSITION_INDEXER_SERVO_SLOT_ONE_OUTPUT = 0.10;//was 0.07 one is at wait; two is at intake
    public final double POSITION_INDEXER_SERVO_SLOT_ZERO_INTAKE = POSITION_INDEXER_SERVO_SLOT_ONE_OUTPUT;
    public final double POSITION_INDEXER_SERVO_SLOT_TWO_OUTPUT = 0.51;//was 0.5 zero is at intake; two is at wait
    public final double POSITION_INDEXER_SERVO_SLOT_ONE_INTAKE = POSITION_INDEXER_SERVO_SLOT_TWO_OUTPUT;
    public final double POSITION_INDEXER_SERVO_SLOT_ZERO_OUTPUT = 0.89;//was 0.93 zero is at wait; one is at intake
    public final double POSITION_INDEXER_SERVO_SLOT_TWO_INTAKE = POSITION_INDEXER_SERVO_SLOT_ZERO_OUTPUT;

    public final double AXON_SERVO_VOLTAGE_OFFSET = 0.228;
    public final double AXON_SERVO_VOLTAGE_SCALER = 0.1/0.2815;

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

            return !getIndexerServoAtPosition(position, 0.02);
        }
    }

    public Action getRotateIndexerAction(double position) {
        return new RotateIndexerAction(position);
    }

    public Action getGoToFirstBallAction() {
        return getRotateIndexerAction(POSITION_INDEXER_SERVO_SLOT_ZERO_OUTPUT);
    }

    public Action getGoToSecondBallAction() {
        return getRotateIndexerAction(POSITION_INDEXER_SERVO_SLOT_ONE_OUTPUT);
    }

    public Action getGoToThirdBallAction() {
        return getRotateIndexerAction(POSITION_INDEXER_SERVO_SLOT_TWO_OUTPUT);
    }

    public Action getWaitUntilBallInIndexerAction(double timeout) {
        return new RunTimeoutAction(
            new WaitUntilAction(() -> getPredictedColor(
            colorSensor1Left.getNormalizedColors(),
            colorSensor1Right.getNormalizedColors(),
            ((DistanceSensor) colorSensor1Left).getDistance(DistanceUnit.CM),
            ((DistanceSensor) colorSensor1Right).getDistance(DistanceUnit.CM)) != Robot.ArtifactColor.NONE),
            timeout
            );
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
        //colorSensor2Left = hardwareMap.get(NormalizedColorSensor.class, "colorSensor2Left");
        //colorSensor2Right = hardwareMap.get(NormalizedColorSensor.class, "colorSensor2Right");
        //colorSensor3Left = hardwareMap.get(NormalizedColorSensor.class, "colorSensor3Left");
        //colorSensor3Right = hardwareMap.get(NormalizedColorSensor.class, "colorSensor3Right");

        colorSensor1Left.setGain(8);
        colorSensor1Right.setGain(8);
        //colorSensor2Left.setGain(8);
        //colorSensor2Right.setGain(8);
        //colorSensor3Left.setGain(8);
        //colorSensor3Right.setGain(8);

        indexerServoVoltage = hardwareMap.get(AnalogInput.class, "analog0");

        indexerServo.setPosition(POSITION_INDEXER_SERVO_SLOT_ZERO_OUTPUT);


        telemetry.addData("color:", artifactColorArray[0]);
        telemetry.addData("color:", artifactColorArray[1]);
        telemetry.addData("color:", artifactColorArray[2]);
    }

    private Robot.ArtifactColor getPredictedColor(NormalizedRGBA sensor1RGBA, NormalizedRGBA sensor2RGBA, double sensor1Distance, double sensor2Distance) {

        Robot.ArtifactColor sensor1DetectedColor;
        telemetry.addData("sensor1Distance", sensor1Distance);
        telemetry.addData("sensor2Distance", sensor2Distance);


        if (sensor1Distance > 3) {
            sensor1DetectedColor = Robot.ArtifactColor.NONE;
        }
        else if (sensor1RGBA.blue > sensor1RGBA.green) {
            sensor1DetectedColor = Robot.ArtifactColor.PURPLE;
        }
        else {
            sensor1DetectedColor = Robot.ArtifactColor.GREEN;
        }

        Robot.ArtifactColor sensor2DetectedColor;

        if (sensor2Distance > 3) {
            sensor2DetectedColor = Robot.ArtifactColor.NONE;
        }
        else if (sensor2RGBA.blue > sensor2RGBA.green) {
            sensor2DetectedColor = Robot.ArtifactColor.PURPLE;
        }
        else {
            sensor2DetectedColor = Robot.ArtifactColor.GREEN;
        }

        if (sensor1DetectedColor == sensor2DetectedColor) {
            return sensor1DetectedColor;
        }
        else if (sensor2DetectedColor == Robot.ArtifactColor.NONE) {
            return  sensor1DetectedColor;
        }
        else if (sensor1DetectedColor == Robot.ArtifactColor.NONE){
            return  sensor2DetectedColor;
        }
        else {
            return  Robot.ArtifactColor.UNKNOWN;
        }
    }

    public void updateBallColors() {
        telemetry.addData("Color", artifactColorArray[0]);
        telemetry.addData("Color", artifactColorArray[1]);
        telemetry.addData("Color", artifactColorArray[2]);
        double position = getIndexerPosition();

        int i = -1;

        if (position == POSITION_INDEXER_SERVO_SLOT_ZERO_INTAKE) {
            i = 0;
        }
        else if(position == POSITION_INDEXER_SERVO_SLOT_ONE_INTAKE) {
            i = 1;
        }
        else if (position == POSITION_INDEXER_SERVO_SLOT_TWO_INTAKE) {
            i = 2;
        }
        else {
            telemetry.addLine("ERROR: updateBallColors");
        }
        artifactColorArray[i] = getPredictedColor(
                colorSensor1Left.getNormalizedColors(),
                colorSensor1Right.getNormalizedColors(),
                ((DistanceSensor) colorSensor1Left).getDistance(DistanceUnit.CM),
                ((DistanceSensor) colorSensor1Right).getDistance(DistanceUnit.CM));
        telemetry.addData("updateBallColors index", i);
        telemetry.addData("updateBallColors color", artifactColorArray[i]);
    }



    public Robot.ArtifactColor[] getBallColors() {

        updateBallColors();
        return artifactColorArray.clone();
    }

    public double getIndexerPosition() {
        double position = indexerServo.getPosition();
        telemetry.addData("position get index position", position);
        return (double) Math.round(position * 100) / 100.00;
    }

    public void rotateToPosition(double position) {
        indexerServo.setPosition(position);
    }

    public void rotateToZeroPosition() {
        rotateToPosition(POSITION_INDEXER_SERVO_SLOT_ZERO_OUTPUT);
    }

    public void rotateToOnePosition() {
        rotateToPosition(POSITION_INDEXER_SERVO_SLOT_ONE_OUTPUT);
    }

    public void rotateToTwoPosition() {
        rotateToPosition(POSITION_INDEXER_SERVO_SLOT_TWO_OUTPUT);
    }

    public Boolean rotateToZeroIntakePosition() {
        if (getIndexerPosition() != POSITION_INDEXER_SERVO_SLOT_ZERO_INTAKE) {
            rotateToPosition(POSITION_INDEXER_SERVO_SLOT_ZERO_INTAKE);
            return true;
        }
        return false;
    }

    public Boolean rotateToOneIntakePosition() {
        if (getIndexerPosition() != POSITION_INDEXER_SERVO_SLOT_ONE_INTAKE) {
            rotateToPosition(POSITION_INDEXER_SERVO_SLOT_ONE_INTAKE);
            return true;
        }
        return false;
    }


    public Boolean rotateToTwoIntakePosition() {
        if (getIndexerPosition() != POSITION_INDEXER_SERVO_SLOT_TWO_INTAKE) {
            rotateToPosition(POSITION_INDEXER_SERVO_SLOT_TWO_INTAKE);
            return true;
        }
        return false;
    }

    public void rotateClockwise() {
        double position = indexerServo.getPosition();
        if ((Math.round(position*100.0))/100.0 == POSITION_INDEXER_SERVO_SLOT_TWO_OUTPUT) {
            indexerServo.setPosition(POSITION_INDEXER_SERVO_SLOT_ZERO_OUTPUT);
        }
        else if((Math.round(position*100.0))/100.0 == POSITION_INDEXER_SERVO_SLOT_ONE_OUTPUT) {
            indexerServo.setPosition(POSITION_INDEXER_SERVO_SLOT_TWO_OUTPUT);
        }
        else if((Math.round(position*100.0))/100.0 == POSITION_INDEXER_SERVO_SLOT_ZERO_OUTPUT) {
            indexerServo.setPosition(POSITION_INDEXER_SERVO_SLOT_ONE_OUTPUT);
        }
    }

    public void rotateCounterClockwise() {
        double position = indexerServo.getPosition();
        if ((Math.round(position*100.0))/100.0 == POSITION_INDEXER_SERVO_SLOT_TWO_OUTPUT) {
            indexerServo.setPosition(POSITION_INDEXER_SERVO_SLOT_ONE_OUTPUT);
        }
        else if ((Math.round(position*100.0))/100.0 == POSITION_INDEXER_SERVO_SLOT_ONE_OUTPUT) {
            indexerServo.setPosition(POSITION_INDEXER_SERVO_SLOT_ZERO_OUTPUT);
        }
        else if ((Math.round(position*100.0))/100.0 == POSITION_INDEXER_SERVO_SLOT_ZERO_OUTPUT) {
            indexerServo.setPosition(POSITION_INDEXER_SERVO_SLOT_TWO_OUTPUT);
        }
    }

    public int getIndexerSlotPosition() {
        return getIndexerPosition() == POSITION_INDEXER_SERVO_SLOT_TWO_OUTPUT ? 2 : (getIndexerPosition() == POSITION_INDEXER_SERVO_SLOT_ONE_OUTPUT ? 1 : 0);
    }

    public Boolean checkEmptySlot(){
        //TODO: can change the order and start with the current intake slot
        telemetry.addLine("checkEmptySlot");
        for(int i=2; i>=0; i--){
            telemetry.addData("array", i);
            telemetry.addData("color:", artifactColorArray[i]);
            if(artifactColorArray[i] == Robot.ArtifactColor.NONE){
                nextEmptySlot = i;
                telemetry.addData("slot empty", i);
                return true;
            }
        }
        telemetry.addLine("no empty slot");
        return false;
    }

    public Boolean turnEmptySlotToIntake(){
        telemetry.addLine("turnEmptySlotToIntake");
        if(nextEmptySlot == 0){
            telemetry.addLine("nextemptyslot 0");
            if(getIndexerPosition() != POSITION_INDEXER_SERVO_SLOT_ZERO_INTAKE){
                rotateToPosition(POSITION_INDEXER_SERVO_SLOT_ZERO_INTAKE);
                return true;
            }
        } else if (nextEmptySlot == 1) {
            if (getIndexerPosition() != POSITION_INDEXER_SERVO_SLOT_ONE_INTAKE){
                rotateToPosition(POSITION_INDEXER_SERVO_SLOT_ONE_INTAKE);
                return true;
            }
        } else if (nextEmptySlot == 2){
            if (getIndexerPosition() != POSITION_INDEXER_SERVO_SLOT_TWO_INTAKE){
                rotateToPosition(POSITION_INDEXER_SERVO_SLOT_TWO_INTAKE);
                return true;
            }
        }
        return false;
    }

    public Boolean haveABall(Robot.ArtifactColor ballColor) {
        telemetry.addLine("haveABall");
        telemetry.addData("color:", artifactColorArray[0]);
        telemetry.addData("color:", artifactColorArray[1]);
        telemetry.addData("color:", artifactColorArray[2]);
        telemetry.addData("color:", ballColor);
        telemetry.addData("nextShootSlot:", nextShootSlot);

        for (int i=0; i<=2; i++){
            if (artifactColorArray[i] == ballColor){
                nextShootSlot = i;
                telemetry.addData("nextShootSlot:", nextShootSlot);
                return true;
            }
        }
        return false;
    }

    public Boolean moveToOuttake() {
        telemetry.addLine("moveToOuttake");
        telemetry.addLine("haveABall");
        telemetry.addData("color:", artifactColorArray[0]);
        telemetry.addData("color:", artifactColorArray[1]);
        telemetry.addData("color:", artifactColorArray[2]);
        telemetry.addData("nextShootSlot:", nextShootSlot);

        if (nextShootSlot==0) {
            if (getIndexerPosition() != POSITION_INDEXER_SERVO_SLOT_ZERO_OUTPUT) {
                rotateToPosition(POSITION_INDEXER_SERVO_SLOT_ZERO_OUTPUT);
                return true;
            }
        }
        else if (nextShootSlot==1) {
            if (getIndexerPosition() != POSITION_INDEXER_SERVO_SLOT_ONE_OUTPUT) {
                rotateToPosition(POSITION_INDEXER_SERVO_SLOT_ONE_OUTPUT);
                return true;
            }
        }
        else if (nextShootSlot==2) {
            if (getIndexerPosition() != POSITION_INDEXER_SERVO_SLOT_TWO_OUTPUT) {
                rotateToPosition(POSITION_INDEXER_SERVO_SLOT_TWO_OUTPUT);
                return true;
            }
        }
        return false;
    }

    public void updateAfterShoot(){
        // the ball has been shot in nextShootSlot
        telemetry.addData("updateAfterShoot: next shoot slot", nextShootSlot);
        artifactColorArray[nextShootSlot] = Robot.ArtifactColor.NONE;
    }

    public Boolean findABall(){
        if (getIndexerPosition() == POSITION_INDEXER_SERVO_SLOT_ZERO_OUTPUT){
            //is there a ball at ZERO?
            if (artifactColorArray[0] != Robot.ArtifactColor.NONE) {
                nextShootSlot = 0;
                return true;
            } else if (artifactColorArray[1] != Robot.ArtifactColor.NONE){
                nextShootSlot = 1;
                return true;
            } else if (artifactColorArray[2] != Robot.ArtifactColor.NONE) {
                nextShootSlot = 2;
                return true;
            }
        } else if (getIndexerPosition() == POSITION_INDEXER_SERVO_SLOT_ONE_OUTPUT){
            //is there a ball at one?
            if (artifactColorArray[1] != Robot.ArtifactColor.NONE) {
                nextShootSlot = 1;
                return true;
            } else if (artifactColorArray[0] != Robot.ArtifactColor.NONE){
                nextShootSlot = 0;
                return true;
            } else if (artifactColorArray[2] != Robot.ArtifactColor.NONE) {
                nextShootSlot = 2;
                return true;
            }
        } else if (getIndexerPosition() == POSITION_INDEXER_SERVO_SLOT_TWO_OUTPUT) {
            //is there a ball at two?
            if (artifactColorArray[2] != Robot.ArtifactColor.NONE) {
                nextShootSlot = 2;
                return true;
            } else if (artifactColorArray[1] != Robot.ArtifactColor.NONE) {
                nextShootSlot = 1;
                return true;
            } else if (artifactColorArray[0] != Robot.ArtifactColor.NONE) {
                nextShootSlot = 0;
                return true;
            }
        }
        return false;
    }

    public double getAxonServoPosition() {
        return (indexerServoVoltage.getVoltage() - AXON_SERVO_VOLTAGE_OFFSET) * AXON_SERVO_VOLTAGE_SCALER;
    }

    public boolean getIndexerServoAtPosition(double position, double accuracy) {
        return Math.abs(getAxonServoPosition() - position) < accuracy;
    }
}
