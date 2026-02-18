package org.firstinspires.ftc.teamcode.common.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;

public class Intake {

    HardwareMap hardwareMap;
    Telemetry telemetry;

    DcMotorEx intakeMotor;

    private int activeIntake = 0;
    public final double INTAKE_POWER = 1.0;


    public class SetIntakeMotorPowerAction implements Action {

        private boolean initialized = false;

        public double power;

        public SetIntakeMotorPowerAction(double power) {
            this.power = power;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                intakeMotor.setPower(power);
                initialized = true;
            }

            return false;
        }
    }

    public Action getSetIntakeMotorPowerAction(double power) {
        return new SetIntakeMotorPowerAction(power);
    }

    public Action getStartIntakeAction() {
        return getSetIntakeMotorPowerAction(1.0);
    }

    public Action getStopIntakeAction() {
        return getSetIntakeMotorPowerAction(0);
    }

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

    public void reverseToggleIntake() {
        if (intakeMotor.getPower() != 0) {
            stopIntake();
        }
        else {
            reverseIntake();
        }
    }

    public void startIntake() {
        setIntakeMotorPower(INTAKE_POWER);
        activeIntake = 1;
    }
    public void stopIntake() {

        setIntakeMotorPower(0);
        activeIntake = 0;
    }
    public void reverseIntake() {

        setIntakeMotorPower(-1);
        activeIntake = -1;
    }

    public void setIntakeMotorPower(double speed) {
        intakeMotor.setPower(speed);
    }
    public double getIntakeMotorPower() {
        return intakeMotor.getPower();
    }

    public int getIntakeState(){
        return activeIntake;
    }
}
