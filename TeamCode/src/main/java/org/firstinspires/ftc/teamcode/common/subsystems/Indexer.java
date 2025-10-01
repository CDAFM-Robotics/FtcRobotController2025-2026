package org.firstinspires.ftc.teamcode.common.subsystems;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

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

    enum ArtifactColor {
        PURPLE,
        GREEN,
        NONE,
        UNKNOWN
    }

    public Indexer(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
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
    }
}
