package org.firstinspires.ftc.teamcode.common.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
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
    public double launchPower;

    private Limelight3A limelight;

    Servo kickerServo;

    // TODO: 10/8/2025 DETERMINE CONSTANTS EMPIRICALLY
    
    public final double POSITION_KICKER_SERVO_KICK_BALL = 0.88;
    public final double POSITION_KICKER_SERVO_INIT = 0.6;

    public class SpinLauncherAction implements Action {

        private boolean initialized = false;

        private double velocity;

        private double power;

        public SpinLauncherAction(double velocity) {
            this.velocity = velocity;
            this.power = 1;
        }

        public SpinLauncherAction(double velocity, double power) {
            this.velocity = velocity;
            this.power = power;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                launcherMotor1.setPower(power);
                launcherMotor2.setPower(power);
                initialized = true;
            }

            // TODO: 11/2/2025 ADD FAIL-SAFE FOR MOTOR ENCODERS BEING UNPLUGGED 

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

    public class AprilTagAction implements Action {
        private boolean initialized = false;

        private Robot.ArtifactColor[] motifPattern;

        public AprilTagAction(int pipeline) {
            limelight.pipelineSwitch(pipeline);
        }


        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            LLResult result = limelight.getLatestResult();

            if (result.isValid()) {
                List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
                for (LLResultTypes.FiducialResult fr : fiducialResults) {
                    if (fr.getFiducialId() == 21) {
                        motifPattern = new Robot.ArtifactColor[] {Robot.ArtifactColor.GREEN, Robot.ArtifactColor.PURPLE, Robot.ArtifactColor.PURPLE};
                        return false;
                    }
                    else if (fr.getFiducialId() == 22) {
                        motifPattern = new Robot.ArtifactColor[] {Robot.ArtifactColor.PURPLE, Robot.ArtifactColor.GREEN, Robot.ArtifactColor.PURPLE};
                        return false;
                    }
                    else if (fr.getFiducialId() == 23) {
                        motifPattern = new Robot.ArtifactColor[] {Robot.ArtifactColor.PURPLE, Robot.ArtifactColor.PURPLE, Robot.ArtifactColor.GREEN};
                        return false;
                    }
                }
            }
            return true;
        }

        public Robot.ArtifactColor[] getMotifPattern() {
            return motifPattern;
        }
    }

    public Action getSpinLauncherAction(double velocity) {
        return new SpinLauncherAction(velocity);
    }

    public Action getSpinLauncherAction(double velocity, double power) {
        return new SpinLauncherAction(velocity, power);
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

    public Action getAprilTagAction () {
        return new AprilTagAction(7);
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
        limelight.pipelineSwitch(0);

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
        if (((double) Math.round(launcherMotor1.getPower()*100)/100) != 0.8) {
            startLauncherPartialPower();
        }
        else {
            stopLauncher();
        }
    }

    public void startLauncher() {
        launchPower = 1;
        setLauncherPower(launchPower);
        launcherActive = true;
    }

    public void reduceLauncherPower() {
        if (launchPower >= 0.1) {
            launchPower -= 0.1;
            launcherActive = true;
        }
        else{
            launchPower = 0;
            launcherActive = false;
        }
        setLauncherPower(launchPower);
    }

    public void increaseLauncherPower() {
        if (launchPower <= 0.9) {
            launchPower += 0.1;
        }
        else{
            launchPower = 1;
        }
        setLauncherPower(launchPower);
        launcherActive = true;
    }

    public void startLauncherPartialPower() {
        launchPower = 0.8;
        setLauncherPower(launchPower);
        launcherActive = true;
    }

    public void stopLauncher() {
        launchPower = 0;
        setLauncherPower(launchPower);
        launcherActive = false;
    }

    public void setLauncherPower(double power) {
        launcherMotor2.setPower(power);
        launcherMotor1.setPower(power);
    }

    public double getLaunchPower(){
        return launcherMotor1.getPower();
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
        limelight.pipelineSwitch(5);
        LLResult result = limelight.getLatestResult();
        double answer = 0;
        if(result.isValid()){
            if(Math.abs(result.getTx()) > 3){
                if(result.getTx() < 1){
                    answer = -0.2;
                }

                if(result.getTx() > 1){
                    answer = 0.2;
                }
            }
            else{
                answer = 0;
            }
        }

        return answer;
    }

    public void setLimelightPipeline(int pipeline) {
        limelight.pipelineSwitch(pipeline);
    }

    public double getBlueAimingPower(){
        //limelight.pipelineSwitch(5);
        limelight.pipelineSwitch(6);
        LLResult result = limelight.getLatestResult();
        double answer = 0;
        if(result.isValid()){
            if(Math.abs(result.getTx()) > 3){
                if(result.getTx() < 1){
                    answer = -0.2;
                }
                if(result.getTx() > 1){
                    answer = 0.2;
                }
            }
            else{
                answer = 0;
            }
        }

        return answer;
    }
}
