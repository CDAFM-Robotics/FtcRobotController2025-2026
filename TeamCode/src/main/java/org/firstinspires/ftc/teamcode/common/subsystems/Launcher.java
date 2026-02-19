package org.firstinspires.ftc.teamcode.common.subsystems;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.Range;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.Robot;
import org.firstinspires.ftc.teamcode.common.RobotStaticValuesClass;
import org.firstinspires.ftc.teamcode.common.util.ArtifactColor;
import org.firstinspires.ftc.teamcode.common.util.InterpolationTable;
import org.firstinspires.ftc.teamcode.common.util.RunTimeoutAction;
import org.firstinspires.ftc.teamcode.common.util.WaitUntilAction;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

@Config
public class Launcher {

    HardwareMap hardwareMap;
    Telemetry telemetry;

    DcMotorEx launcherMotor1;
    DcMotorEx launcherMotor2;
    public double launchPower;
    double launcherVelocity;
    double hoodPosition;
    double kickerPosition;

    private Limelight3A limelight;

    public Servo kickerServo;
    CRServo turretServo;
    Servo hoodServo;
    AnalogInput RSFeedback;

    // turret servo
    CRServo launcherServo;
    AnalogInput launcherAnalogInput;

    public enum QuadrantRotatorServo{
        POSITIVE, NEGATIVE, ZERO
    }

    QuadrantRotatorServo currentQuadrant;

    double lastServoPosition;

    public final double POSITION_KICKER_SERVO_KICK_BALL = 0.16; // 0.26
    public final double POSITION_KICKER_SERVO_INIT = 0.51;
    public final double POSITION_TUREET_SERVO_INIT = 0.5;

    // Hood Servo
    public final double POSITION_HOOD_SERVO_INIT = 0.1;
    public final double POSITION_HOOD_SERVO_HIGH = 0.1;
    public final double POSITION_HOOD_SERVO_LOW = 1.0;

    public final double LAUNCH_POWER_FAR = 0.9;
    public final double LAUNCH_POWER_NEAR= 0.8;
    public final double LAUNCH_POWER_FULL= 1.0;
    public final double LAUNCH_POWER_LOW=0.3;   // TODO find lowest valuable power and set this
    public final double LAUNCH_VELOCITY_FAR = 2200; // was: 6000 (wrong, 2200tps/28ppr*60 rpm 4714 rpm )
    public final double LAUNCH_VELOCITY_NEAR= 1300;
    public final double LAUNCH_VELOCITY_FULL= 2200;
    public final double LAUNCH_VELOCITY_LOW= 1060;   // TODO find lowest valuable power and set this
    public final double LIMELIGHT_OFFSET = 17.4;
    public final double LIMELIGHT_HEIGHT_OFFSET = 436;

    //rotate autoaim PID Constants
    private double rotateIntegralSum = 0.0;
    public static double rotateKp = 0.1;
    public static double rotateKi = 0;
    public static double rotateKd = 0;
    public static double rotateKf = 0;

    // Teleop AutoAIM PID Constants
    public static double aimKp = 0.016; // 0.02
    public static double aimKi = 0.006; // 0.01
    public static double aimKd = 1.0; // 1.1 // 0.0055
    public static int aimTimeout = 650; // 800
    public static double powerStatic = 0.064; // 0.05
    public static double aimErrorTolerance = 0; // 3
    private double lastTime;
    private double integralSum = 0.0;
    // Limits for integral sum to prevent windup
    private double integralSumMax = 1.0;
    private double integralSumMin = -1.0;
    private double lastError = 0.0;

    public static double shootKp = 250; //25;
    public static double shootKi = 1.5;
    public static double shootKd = 10; //4;
    public static double shootKf = 1.1;
    public static double targetVelocity;
    public static double currentVelocity;
    private double shootingDistance;

    // Use a table for interpolation
    private InterpolationTable shootingTable;

    // Turret control variables
    public static double turretkF = 0.10;
    public static double turretkP = 0.005;
    public static double turretkI = 0;
    public static double turretkD = 0.00005;

    private double currentVoltage;
    private double currentAngle;
    private double currentAngleOffset = 0;

    private double lastVoltage = 0;
    private double lastAngle = 0;
    private double turretLastError = 0;

    private double actualAngle;
    private double greatestDiff = -0x80000000;

    private boolean firstLoop = true;

    private double turretIntegralSum = 0;
    private double turretTime = 0;

    // Autonomous Actions
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


            // Add some Debugging Helpers
            double measuredVelocity1 =  launcherMotor1.getVelocity();
            double measuredVelocity2 =  launcherMotor2.getVelocity();
            double measuredVelocityTotal = measuredVelocity1 + measuredVelocity2;
            // Logging
            if (measuredVelocity1 != 0.0 || measuredVelocity2 != 0.0) {
                //RobotLog.d("m1: %f m2: %f", measuredVelocity1, measuredVelocity2);
            }

            if (measuredVelocity1 == 0 && measuredVelocity2 > velocity/2 || measuredVelocity2 == 0 && measuredVelocity1 > velocity/2) {
                measuredVelocityTotal = (measuredVelocity1 + measuredVelocity2) * 2;
            }
            return measuredVelocityTotal < velocity * 2;
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

        private ArtifactColor[] motifPattern;

        public AprilTagAction(int pipeline) {
            //limelight.pipelineSwitch(pipeline);
        }


        @Override
        public boolean run(@NonNull TelemetryPacket telemetryPacket) {
            ArtifactColor[] result = getMotifPattern();

            if (result != null) {
                motifPattern = result;
                return false;
            }
            return true;
        }

        public ArtifactColor[] getPattern() {
            return motifPattern;
        }
    }

    public Action getSpinLauncherAction(double velocity) {
        return new SequentialAction(
            new SpinLauncherAction(velocity));
    }

    public Action getWaitUntilVelocityAction(double velocity, double timeout) {
        return new RunTimeoutAction(
            new WaitUntilAction(() -> launcherMotor1.getVelocity() + launcherMotor2.getVelocity() == velocity),
            timeout
        );
    }

    public Action getRotateKickerAction(double position) {
        return new SequentialAction(
            new SetKickerPositionAction(position),
            new SleepAction(0.100) // TODO 125 / 300
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
        return new RunTimeoutAction(new AprilTagAction(7), 8);
    }

    public Launcher(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;

        initializeLauncherDevices();
    }

    public void initializeLauncherDevices () {
        launcherMotor1 = hardwareMap.get(DcMotorEx.class, "launcherMotor1");
        launcherMotor2 = hardwareMap.get(DcMotorEx.class, "launcherMotor2");

        launcherMotor1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        launcherMotor2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        launcherMotor1.setDirection(DcMotorSimple.Direction.REVERSE);
        launcherMotor2.setDirection(DcMotorSimple.Direction.FORWARD);
        launcherMotor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        launcherMotor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        PIDFCoefficients pidfNew = new PIDFCoefficients(shootKp, shootKi, shootKd, shootKf);
        launcherMotor1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfNew);
        launcherMotor2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfNew);

        kickerServo = hardwareMap.get(Servo.class, "kickerServo");
        launcherServo = hardwareMap.get(CRServo.class, "turretServo");
        launcherAnalogInput = hardwareMap.get(AnalogInput.class, "turretAnalog");
        hoodServo = hardwareMap.get(Servo.class, "hoodServo");

        kickerPosition = POSITION_KICKER_SERVO_INIT;
        kickerServo.setPosition(POSITION_KICKER_SERVO_INIT);

        hoodPosition = POSITION_HOOD_SERVO_HIGH;
        hoodServo.setPosition(hoodPosition);

        // initialized turret variables
        currentVoltage = launcherAnalogInput.getVoltage();
        currentAngle = currentVoltage / 3.3 * 360;
        // set the offset to the value from Autonomous
        if (RobotStaticValuesClass.autoCompleted) {
            currentAngleOffset = RobotStaticValuesClass.turretAngleOffset;
        }
        //348.55 is max with no power
        // 378.33 with power
        //369 with -power

        // min is 0
        // 38.84 with power
        // 25 with - power

        actualAngle = currentAngle + currentAngleOffset;

        telemetry.addData("Servo Voltage", "%.2f", currentVoltage);
        telemetry.addData("Servo Angle Raw", "%.2f", currentAngle);
        telemetry.addData("Last Servo Voltage", "%.2f", lastVoltage);
        telemetry.addData("Last Servo Angle Raw", "%.2f", lastAngle);
        telemetry.addData("Actual Servo Angle", "%.2f", actualAngle);

        lastAngle = currentAngle;
        lastVoltage = currentVoltage;

        shootingTable = new InterpolationTable(InterpolationTable.ExtrapolationMode.LINEAR);

        shootingTable.add(30.20,1080,1.0);
        shootingTable.add(60.02,1200,0.30);
        shootingTable.add(90,1320,0.15);
        shootingTable.add(90,1560,0.0);

        //limelight = hardwareMap.get(Limelight3A.class, "limelight");
        //limelight.pipelineSwitch(0);

        //limelight.start();

        // Initialize the map with calibration points.
        // Distances in cm, velocities as motor power (0.0 to 1.0)
        // Example values:
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

    public ArtifactColor[] getMotifPattern() {
//        LLResult result = limelight.getLatestResult();
//        if (result.isValid()) {
//            List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
//            if (fiducialResults != null) {
//                for (LLResultTypes.FiducialResult fr : fiducialResults) {
//                    if (fr.getFiducialId() == 21) {
//                        return new ArtifactColor[]{ArtifactColor.GREEN, ArtifactColor.PURPLE, ArtifactColor.PURPLE};
//                    }
//                    else if (fr.getFiducialId() == 22) {
//                        return new ArtifactColor[]{ArtifactColor.PURPLE, ArtifactColor.GREEN, ArtifactColor.PURPLE};
//                    }
//                    else if (fr.getFiducialId() == 23) {
//                        return new ArtifactColor[]{ArtifactColor.PURPLE, ArtifactColor.PURPLE, ArtifactColor.GREEN};
//                    }
//                }
//            }
//        }
        return null;
    }

    private boolean launcherActive = false;

    public void kickBall() {
        RobotLog.d("kickball");
        if (isLauncherActive()) {
            kickerServo.setPosition(POSITION_KICKER_SERVO_KICK_BALL);
        }
    }

    public void resetKicker() {
        RobotLog.d("reset kicker");
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

//    public void toggleLauncherManualFar() {
//        if (launcherMotor1.getPower() == 0) {
//            startLauncherManualFar();
//        }
//        else {
//            stopLauncher();
//        }
//    }

    /***********************************
     ***********ROTATOR SERVO***********
     ***********************************/
    public double getRawRotatorServoPower(){
        double output = turretServo.getPower();
        return output;
    }

    public double getRawRotatorServoPosition(){return  RSFeedback.getVoltage() * (180/3.3);}

    public double getRotatorServoPosition(){
        if(currentQuadrant == QuadrantRotatorServo.NEGATIVE){
            return getRawRotatorServoPosition();
        }
        if(currentQuadrant == QuadrantRotatorServo.POSITIVE){
            return getRawRotatorServoPosition() + 180;
        }
        if(currentQuadrant == QuadrantRotatorServo.ZERO){
            return 180;
        }
        else{
            return 0;
        }
    }
    public void startLauncher() {
        //launcherVelocity = shootingTable.getData1(shootingDistance);
        setLauncherVelocity(launcherVelocity);
        launcherActive = true;
    }

    public double convertFromDegreesToVoltage(double degrees){
        return degrees * (3.3/180);
    }


    public double getRotatorServoVoltage(){
        return RSFeedback.getVoltage();
    }

    public void setRotatorServoPower(double power){
        turretServo.setPower(power);

        if(power > 0 && getRawRotatorServoPosition() >= 180){
            currentQuadrant = QuadrantRotatorServo.POSITIVE;
        }
        else if(power < 0 && (lastServoPosition - getRawRotatorServoPosition()) < -3.5){
            currentQuadrant = QuadrantRotatorServo.NEGATIVE;
        }
        else if (getRawRotatorServoPosition() == 0){
            currentQuadrant = QuadrantRotatorServo.ZERO;
        }

        lastServoPosition = getRawRotatorServoPosition();
    }

    public double error() {return  lastServoPosition - getRawRotatorServoPosition();}

    public QuadrantRotatorServo getCurrentQuadrantOfRotatorServo(){return currentQuadrant;}

    public double targetRotatorPositionPIDControl(double targetDegrees, ElapsedTime timer){
        double error = (targetDegrees) - (getRotatorServoPosition());
        rotateIntegralSum += error*timer.seconds();
        double derivative = (error - lastError) / timer.seconds();
        double output = (rotateKp * error) + (rotateKi * rotateIntegralSum) + (rotateKd * derivative);
        lastError = error;
        timer.reset();

        return Range.clip(output, -1, 1);
    }

    /******************************
     **********HOOD SERVO**********
     ******************************/
    public void setHoodServoPosition(double position){
        hoodServo.setPosition(position);
    }

    public double getHoodServoPosition(){return hoodServo.getPosition();}

    public void setHoodServoDirection(double direction){
        if(direction > 0.05)
            hoodServo.setPosition(hoodServo.getPosition() - 0.05);
        else if (direction < -0.05) {
            hoodServo.setPosition(hoodServo.getPosition() + 0.05);
        }
    }

    public void changeHood(double change) {
        hoodPosition += change;

        if (hoodPosition > 1.0) {
            hoodPosition = 1.0;
        }
        else if (hoodPosition < 0.0) {
            hoodPosition = 0.0;
        }

        hoodServo.setPosition(hoodPosition);
    }

    /*****************************
     *******LAUNCHER MOTOR********
     *****************************/

    public void increaseLauncherMotorPower(){
        launcherMotor1.setPower(launcherMotor1.getPower() + 0.25);
        launcherMotor2.setPower(launcherMotor2.getPower() + 0.25);
    }
    public void decreaseLauncherMotorPower(){
        launcherMotor1.setPower(launcherMotor1.getPower() - 0.25);
        launcherMotor2.setPower(launcherMotor2.getPower() - 0.25);
    }

    public double getLauncherMotorPower(){return launcherMotor1.getPower();}

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
        currentVelocity = launcherMotor1.getVelocity();
        return currentVelocity;
    }

    public double getLauncherTargetVelocity(){
        return launcherVelocity;
    }

    public double getLauncherVelocity2() {
        double currentVelocity2 = launcherMotor2.getVelocity();
        return currentVelocity2;
    }

    public Boolean isLauncherActive(){
        return launcherActive;
    }

    public void setLauncherVelocity(double velocity) {
        launcherMotor2.setVelocity(launcherVelocity);
        launcherMotor1.setVelocity(launcherVelocity);
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

    public void changeKicker(double change) {
        kickerPosition += change;

        if (kickerPosition > 1.0) {
            kickerPosition = 1.0;
        }
        else if (kickerPosition < 0.0) {
            kickerPosition = 0.0;
        }

        //kickerServo.setPosition(kickerPosition);
    }

    public double getKickerServoPosition() {
        return kickerServo.getPosition();
    }


    //For launch motor coefficients testing only
    public void setLaunchMotorPIDFCoefficients() {
        // Change coefficients using methods included with DcMotorEx class.
        PIDFCoefficients pidfNew = new PIDFCoefficients(shootKp, shootKi, shootKd, shootKf);
        launcherMotor1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfNew);
        launcherMotor2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfNew);
    }

    public PIDFCoefficients getLauncherMotorPIDFCoefficients() {
        return launcherMotor1.getPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER);
    }

    // Turret Methods
    public void setTurretRelativeAngle(double relativeTargetAngle){
        double turretTarget = relativeTargetAngle * 2;
        // Find the voltage returned and the angle of the servo

        currentVoltage = launcherAnalogInput.getVoltage();
        currentAngle = currentVoltage / 3.3 * 360;

        // Find out whether the angle looped around

        double diff = Math.abs(currentAngle - lastAngle);

        if (!firstLoop) {
            if (currentAngle > 180 && lastAngle < 180 && diff > 30) {
                currentAngleOffset -= 360;
            }

            if (currentAngle < 180 && lastAngle > 180 && diff > 30) {
                currentAngleOffset += 360;
            }
        }

        actualAngle = currentAngle + currentAngleOffset;

        greatestDiff = Math.max(greatestDiff, diff);

        // Set Servo power
        double turretPower = updateTurretPID(turretTarget, actualAngle);
        launcherServo.setPower(turretPower);

        // Set hood Servo position
        hoodPosition = shootingTable.getData2(shootingDistance);
        hoodServo.setPosition(hoodPosition);

        // Set launcher power
        launcherVelocity = shootingTable.getData1(shootingDistance);
        if (isLauncherActive())
            setLauncherVelocity(launcherVelocity);

        // Add telemetry data for debugging

        telemetry.addData("Power", turretPower);
        telemetry.addLine();
        telemetry.addData("Servo Voltage", "%.2f", currentVoltage);
        telemetry.addData("Servo Angle Raw", "%.2f", currentAngle);
        telemetry.addLine();
        telemetry.addData("Last Servo Voltage", "%.2f", lastVoltage);
        telemetry.addData("Last Servo Angle Raw", "%.2f", lastAngle);
        telemetry.addData("Difference", "%.2f", diff);
        telemetry.addData("Angle Offset", "%.2f", currentAngleOffset);
        telemetry.addData("Actual Servo Angle", "%.2f", actualAngle);

        // TODO don't do this here. one time per loooop
//        telemetry.update();

        // Logging

        RobotLog.d("Power: %.2f, Servo Angle: %.2f, Last Servo Angle: %.2f, Difference: %.2f, Angle Offset: %.2f, Actual Servo Angle: %.2f, target angle: %.2f", turretPower, currentAngle, lastAngle, diff, currentAngleOffset, actualAngle, turretTarget);

        // Set last variables for next loop

        lastAngle = currentAngle;
        lastVoltage = currentVoltage;

        firstLoop = false;
    }

    public double updateTurretPID(double target, double current) {
        double turretLastTime = turretTime;
        turretTime = System.nanoTime() / 1000000000.0;
        double dt = turretTime - turretLastTime;

        double error = target - current;

        if (Math.abs(error) < 2.5) {
            return 0;
        }

        turretIntegralSum += error * dt;

        double derivative = (error - turretLastError) / dt;
        turretLastError = error;

        double turretPower = Math.max(Math.min((error * turretkP) + (turretIntegralSum * turretkI) + (derivative * turretkD) + (turretkF * Math.signum(error)), 1), -1);

        telemetry.addData("turret target angle", "%.2f", target);
        telemetry.addData("turret current angle", "%.2f", current);
        telemetry.addData("turret Error", "%.2f", error);
        telemetry.addData("turret Integral", "%.2f", turretIntegralSum);
        telemetry.addData("turret Derivative", "%.2f", derivative);
        telemetry.addData("turret Power", "%.2f", turretPower);

        return turretPower;
    }

    public void setShootingDistance(double distanceToGoal) {
        shootingDistance = distanceToGoal;
    }
}