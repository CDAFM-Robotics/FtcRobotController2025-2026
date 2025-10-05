package org.firstinspires.ftc.teamcode.common.subsystems;

import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {

    HardwareMap hardwareMap;
    Telemetry telemetry;

    DcMotorEx intakeMotor;

    public Intake(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;

        initializeIntakeDevices();
    }

    public void initializeIntakeDevices() {
        intakeMotor = hardwareMap.get(DcMotorEx.class, "intakeMotor");
    }

    public void toggleIntake() {
        if (intakeMotor.getPower() != 0) {
            stopIntake();
        }
        else {
            startIntake();
        }
    }

    public void startIntake() {
        setIntakeMotorPower(1);
    }
    public void stopIntake() {
        setIntakeMotorPower(0);
    }
    public void setIntakeMotorPower(double speed) {
        intakeMotor.setPower(speed);
    }


}
