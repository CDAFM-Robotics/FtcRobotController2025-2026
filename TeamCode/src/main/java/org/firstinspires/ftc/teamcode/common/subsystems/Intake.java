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

            return true;
        }
    }

    public Action getSetIntakeMotorPowerAction(double power) {
        return new SetIntakeMotorPowerAction(power);
    }

    public Action getStartIntakeAction() {
        return getSetIntakeMotorPowerAction(1);
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

        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
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
