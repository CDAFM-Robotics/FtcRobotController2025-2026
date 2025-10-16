package org.firstinspires.ftc.teamcode.common.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;

import java.util.Set;

public class Launcher {

    HardwareMap hardwareMap;
    Telemetry telemetry;

    DcMotorEx launcherMotor1;
    DcMotorEx launcherMotor2;

    Servo kickerServo;

    // TODO: 10/8/2025 DETERMINE CONSTANTS EMPIRICALLY
    
    public final double POSITION_KICKER_SERVO_KICK_BALL = 0.88;
    public final double POSITION_KICKER_SERVO_INIT = 0.6;

    public class SpinLauncherAction implements Action {

        private boolean initialized = false;

        public double velocity;

        public SpinLauncherAction(double velocity) {
            this.velocity = velocity;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                launcherMotor1.setPower(1);
                launcherMotor2.setPower(1);
                initialized = true;
            }

            double vel1 = launcherMotor1.getVelocity();
            double vel2 = launcherMotor2.getVelocity();

            telemetryPacket.put("Shooter Velocity", System.out.format("1: %.4f, 2: %.4f", vel1, vel2));

            return vel2 + vel1 > velocity * 2;
        }
    }

    public class SetLauncherPowerAction implements Action {

        private boolean initialized = false;

        public double power;

        public SetLauncherPowerAction(double power) {
            this.power = power;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                launcherMotor1.setPower(power);
                launcherMotor2.setPower(power);
                initialized = true;
            }
            return true;
        }
    }

    public class SetKickerPositionAction implements Action {

        private boolean initialized = false;

        public double position;

        public SetKickerPositionAction(double position) {
            this.position = position;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                kickerServo.setPosition(position);
                initialized = true;
            }

            return true;
        }
    }

    public Action getSpinLauncherAction(double velocity) {
        return new SpinLauncherAction(velocity);
    }

    public Action getRotateKickerAction(double position) {
        return new SequentialAction(
            new SetKickerPositionAction(position),
            new SleepAction(1000)
        );
    }

    public Action getKickBallAction() {
        return getRotateKickerAction(POSITION_KICKER_SERVO_KICK_BALL);
    }

    public Action getResetKickerAction() {
        return getRotateKickerAction(POSITION_KICKER_SERVO_INIT);
    }

    public Action getSetLauncherPowerAction(double power) {
        return new SetLauncherPowerAction(power);
    }
    public Action getStopLauncherAction() {
        return getSetLauncherPowerAction(0);
    }

    public Launcher(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;

        initializeLauncherDevices();
    }

    public void initializeLauncherDevices () {
        launcherMotor1 = hardwareMap.get(DcMotorEx.class, "launcherMotor1");
        launcherMotor2 = hardwareMap.get(DcMotorEx.class, "launcherMotor2");

        launcherMotor1.setDirection(DcMotorSimple.Direction.REVERSE);
        launcherMotor2.setDirection(DcMotorSimple.Direction.REVERSE);

        kickerServo = hardwareMap.get(Servo.class, "kickerServo");

        kickerServo.setPosition(POSITION_KICKER_SERVO_INIT);
    }

    private boolean launcherActive = false;

    public void toggleKicker() {
        if (kickerServo.getPosition() == POSITION_KICKER_SERVO_INIT && launcherActive) {
            kickBall();
        }
        else {
            resetKicker();
        }
    }
    
    public void kickBall() {
        if (launcherActive) {
            kickerServo.setPosition(POSITION_KICKER_SERVO_KICK_BALL);
        }
    }

    public void resetKicker() {
        kickerServo.setPosition(POSITION_KICKER_SERVO_INIT);
    }

    public void toggleLauncher() {
        if (launcherMotor1.getPower() == 0) {
            startLauncher();
        }
        else {
            stopLauncher();
        }
    }

    public void startLauncher() {
        setLauncherPower(1);
        launcherActive = true;
    }

    public void stopLauncher() {
        setLauncherPower(0);
        launcherActive = false;
    }

    public void setLauncherPower(double power) {
        launcherMotor2.setPower(power);
        launcherMotor1.setPower(power);
        launcherActive = power != 0;
    }
}
