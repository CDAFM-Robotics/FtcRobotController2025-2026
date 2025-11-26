package org.firstinspires.ftc.teamcode.common.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.Robot;

import java.util.List;

@Config
public class Launcher {

    HardwareMap hardwareMap;
    Telemetry telemetry;

    DcMotorEx launcherMotor1;
    DcMotorEx launcherMotor2;
    public double launchPower;
    double launcherVelocity;

    private Limelight3A limelight;

    Servo kickerServo;
    
    public final double POSITION_KICKER_SERVO_KICK_BALL = 0.88;
    public final double POSITION_KICKER_SERVO_INIT = 0.6;

    public final double LAUNCH_POWER_FAR = 0.9;
    public final double LAUNCH_POWER_NEAR= 0.8;
    public final double LAUNCH_POWER_FULL= 1.0;
    public final double LAUNCH_POWER_LOW=0.3;   // TODO find lowest valuable power and set this
    public final double LAUNCH_VELOCITY_FAR = 1400;
    public final double LAUNCH_VELOCITY_NEAR= 1300;
    public final double LAUNCH_VELOCITY_FULL= 3000;
    public final double LAUNCH_VELOCITY_LOW=690;   // TODO find lowest valuable power and set this
    public final double LIMELIGHT_OFFSET = -1.1;

    public static double aimKp = 0.02;
    public static double powerStatic = 0.0;
    public static double aimErrorTolerance = 2;

    public class SpinLauncherAction implements Action {

        private boolean initialized = false;

        private double velocity;

        public SpinLauncherAction(double velocity) {
            this.velocity = velocity;
        }

        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            if (!initialized) {
                launcherMotor1.setVelocity(velocity);
                launcherMotor2.setVelocity(velocity);
                initialized = true;
            }

            // TODO: 11/2/2025 ADD FAIL-SAFE FOR MOTOR ENCODERS BEING UNPLUGGED

            double measured_velocity_1 =  launcherMotor1.getVelocity();
            double measured_velocity_2 =  launcherMotor2.getVelocity();
            double mvel_tot = measured_velocity_1 + measured_velocity_2;
            if (measured_velocity_1 != 0.0) {
                RobotLog.d("m0: %f", measured_velocity_1);
            }
            return mvel_tot  < ((velocity * 2) - 40);
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
        launcherMotor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        launcherMotor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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
        //launchPower = LAUNCH_POWER_FAR;
        //setLauncherPower(launchPower);
        //start launcher with velocity
        launcherVelocity = LAUNCH_VELOCITY_FAR;
        setLauncherVelocity(launcherVelocity);
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
        if (launchPower < LAUNCH_POWER_FULL) {
            launchPower += 0.1;
            if (launchPower > LAUNCH_POWER_FULL)
                launchPower=LAUNCH_POWER_FULL;
        }
        else{
            launchPower = 1;
        }
        setLauncherPower(launchPower);
        launcherActive = true;
    }

    public void changeLauncherPower(double change) {
        launchPower += change;

        if (launchPower > 1.0) {
            launchPower = 1.0;
        }
        else if (launchPower < 0.0) {
            launchPower = 0.0;
        }

        setLauncherPower(launchPower);
        launcherActive = (launchPower != 0.0);
    }

    public void startLauncherPartialPower() {
        //launchPower = LAUNCH_POWER_NEAR;
        //setLauncherPower(launchPower);
        //start launcher with velocity
        launcherVelocity = LAUNCH_VELOCITY_NEAR;
        setLauncherVelocity(launcherVelocity);
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
        limelight.pipelineSwitch(Robot.LLPipelines.RED_GOAL.ordinal());    // 5 = RED_GOAL
        LLResult result = limelight.getLatestResult();
        double answer = 0;
        if(result.isValid()){
            if(Math.abs(result.getTx()) > 3){
                if(result.getTx() < 0){
                    answer = -0.17;
                }
                else if(result.getTx() > 0){
                    answer = 0.17;
                }
            }
            else{
                answer = 0;
            }
        }

        return answer;
    }

    public Boolean shouldAim(){
        limelight.pipelineSwitch(Robot.LLPipelines.RED_GOAL.ordinal());    // 5 = RED_GOAL
        LLResult result = limelight.getLatestResult();
        if(result.isValid()){
            if(Math.abs(result.getTx()) > aimErrorTolerance){
                return true;
            }
        }

        return false;
    }

    public void setLimelightPipeline(int pipeline) {
        limelight.pipelineSwitch(pipeline);
    }

    public double getBlueAimingPower(){
        limelight.pipelineSwitch(Robot.LLPipelines.BLUE_GOAL.ordinal());    // 6 = BLUE_GOAL
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

    public double setRedAimPowerPID () {
        setLimelightPipeline(Robot.LLPipelines.RED_GOAL.ordinal());
        return getAimPowerPID();
    }

    public double getBlueAimPowerPID () {
        setLimelightPipeline(Robot.LLPipelines.BLUE_GOAL.ordinal());
        return getAimPowerPID();
    }

    public double getAimPowerPID() {
        LLResult result = limelight.getLatestResult();
        double power = 0;
        if(result.isValid()){
            double currentX = result.getTx();
            if (currentX < 0) {
                power = -(powerStatic + aimKp * Math.abs(currentX));
            }
            else {
                power = powerStatic + aimKp * Math.abs(currentX);
            }
        }
        return power;
    }

    public double getRedGoalDistance(){
        limelight.pipelineSwitch(Robot.LLPipelines.RED_GOAL.ordinal());    // 5 = RED_GOAL
        LLResult llresult = limelight.getLatestResult();
        double distance = 0;
        if(llresult.isValid()){
            distance = 448/Math.tan(Math.toRadians(llresult.getTy()+LIMELIGHT_OFFSET));
        }
        else{
                distance = 0;
        }

        return distance;
    }

    public void setLauncherVelocity(double velocity) {
        launcherMotor2.setVelocity(velocity);
        launcherMotor1.setVelocity(velocity);
    }

    public void changeLauncherVelocity(double change) {
        launcherVelocity += change;

        if (launcherVelocity > LAUNCH_VELOCITY_FULL) {
            launcherVelocity = LAUNCH_VELOCITY_FULL;
        }
        else if (launcherVelocity < 0.0) {
            launcherVelocity = 0.0;
        }

        setLauncherVelocity(launcherVelocity);
        launcherActive = (launcherVelocity != 0.0);
    }


}
