package org.firstinspires.ftc.teamcode.common.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.Robot;

import java.util.List;

public class Launcher {

    HardwareMap hardwareMap;
    Telemetry telemetry;

    DcMotorEx launcherMotor1;
    DcMotorEx launcherMotor2;

    private Limelight3A limelight;

    Servo kickerServo;

    // TODO: 10/8/2025 DETERMINE CONSTANTS EMPIRICALLY
    
    public final double POSITION_KICKER_SERVO_KICK_BALL = 0.88;
    public final double POSITION_KICKER_SERVO_INIT = 0.6;

    public class SpinLauncherAction implements Action {

        private boolean initialized = false;

        private double velocity;


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

            return launcherMotor1.getVelocity() + launcherMotor2.getVelocity() < velocity * 2;
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
            return false;
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

            return false;
        }
    }

    public Action getSpinLauncherAction(double velocity) {
        return new SpinLauncherAction(velocity);
    }

    public Action getRotateKickerAction(double position) {
        return new SequentialAction(
            new SetKickerPositionAction(position),
            new SleepAction(0.5)
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

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(5);

        limelight.start();
    }
    /*
        LIMELIGHT PIPELINES:        TYPE:               STATUS:
            0: PURPLE               COLOR               USED
            1: YELLOW               COLOR               OPEN FOR CONFIGURATION
            2: BLUE                 COLOR               OPEN FOR CONFIGURATION
            3: APRIL_TAG            AprilTag            OPEN FOR CONFIGURATION
            4: MOTIF                AprilTag            USED
            5: RED_GOAL             AprilTag            USED
            6: BLUE_GOAL            AprilTag            USED
            7: OBELISK              AprilTag            USED

     */

    public Robot.ArtifactColor[] getMotifPattern() {
        LLResult result = limelight.getLatestResult();
        if (result.isValid()) {
            List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
            if (fiducialResults != null) {
                for (LLResultTypes.FiducialResult fr : fiducialResults) {
                    if (fr.getFiducialId() == 21) {
                        return new Robot.ArtifactColor[]{Robot.ArtifactColor.GREEN, Robot.ArtifactColor.PURPLE, Robot.ArtifactColor.PURPLE};
                    } else if (fr.getFiducialId() == 22) {
                        return new Robot.ArtifactColor[]{Robot.ArtifactColor.PURPLE, Robot.ArtifactColor.GREEN, Robot.ArtifactColor.PURPLE};
                    } else if (fr.getFiducialId() == 23) {
                        return new Robot.ArtifactColor[]{Robot.ArtifactColor.PURPLE, Robot.ArtifactColor.PURPLE, Robot.ArtifactColor.GREEN};
                    }
                }
            }
        }
        return null;
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

    public double getKickerPosition() {
        return (double) Math.round(kickerServo.getPosition()*100)/100;
    }

    public void toggleLauncher() {
        if (launcherMotor1.getPower() == 0) {
            startLauncher();
        }
        else {
            stopLauncher();
        }
    }

    public void toggleLauncherPartialPower() {
        if (launcherMotor1.getPower() != 0.5) {
            startLauncherPartialPower();
        }
        else {
            stopLauncher();
        }
    }

    public void startLauncher() {
        setLauncherPower(1);
        launcherActive = true;
    }

    public void startLauncherPartialPower() {
        setLauncherPower(0.8);
        launcherActive = true;
    }

    public void stopLauncher() {
        setLauncherPower(0);
        launcherActive = false;
    }

    public void setLauncherPower(double power) {
        launcherMotor2.setPower(power);
        launcherMotor1.setPower(power);
    }

    public double getLauncherVelocity() {
        return launcherMotor1.getVelocity();
    }

    public Boolean isLauncherActive(){
        return launcherActive;
    }

    public LLResult getLimelightResult(){
        return limelight.getLatestResult();
    }

    public double getRedAimingPower(){
        //limelight.pipelineSwitch(5);
        LLResult result = limelight.getLatestResult();
        double answer = 0;
        if(result.isValid()){
            if(Math.abs(result.getTx()) > 1.5){
                if(result.getTx() < 0){
                    answer = -0.15;
                }
                if(result.getTx() > 0){
                    answer = 0.15;
                }
            }
            else{
                answer = 0;
            }
        }

        return answer;
    }
}
