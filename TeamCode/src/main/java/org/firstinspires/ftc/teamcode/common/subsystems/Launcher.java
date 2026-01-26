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
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.Robot;
import org.firstinspires.ftc.teamcode.common.util.ArtifactColor;
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

    private Limelight3A limelight;

    Servo kickerServo;
    
    public final double POSITION_KICKER_SERVO_KICK_BALL = 0.87; // 0.88
    public final double POSITION_KICKER_SERVO_INIT = 0.6;

    public final double LAUNCH_POWER_FAR = 0.9;
    public final double LAUNCH_POWER_NEAR= 0.8;
    public final double LAUNCH_POWER_FULL= 1.0;
    public final double LAUNCH_POWER_LOW=0.3;   // TODO find lowest valuable power and set this
    public final double LAUNCH_VELOCITY_FAR = 1380;
    public final double LAUNCH_VELOCITY_NEAR= 1300;
    public final double LAUNCH_VELOCITY_FULL= 1500;
    public final double LAUNCH_VELOCITY_LOW= 1060;   // TODO find lowest valuable power and set this
    public final double LIMELIGHT_OFFSET = 17.4;

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

    // A TreeMap is better than HashMap for interpolation because it keeps
    // keys sorted, allowing easy finding of surrounding points.
    private final TreeMap<Double, Double> distanceToVelocityMap = new TreeMap<>();

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
            limelight.pipelineSwitch(pipeline);
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
       // launcherMotor1.setDirection(DcMotorSimple.Direction.REVERSE);
       // launcherMotor2.setDirection(DcMotorSimple.Direction.REVERSE);
        launcherMotor1.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        launcherMotor2.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        PIDFCoefficients pidfNew = new PIDFCoefficients(shootKp, shootKi, shootKd, shootKf);
        launcherMotor1.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfNew);
        launcherMotor2.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, pidfNew);

        kickerServo = hardwareMap.get(Servo.class, "kickerServo");

        kickerServo.setPosition(POSITION_KICKER_SERVO_INIT);

        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        limelight.pipelineSwitch(0);

        limelight.start();

        // Initialize the map with calibration points.
        // Distances in cm, velocities as motor power (0.0 to 1.0)
        // Example values:
        distanceToVelocityMap.put(650.0, 1160.0);
        distanceToVelocityMap.put(684.0, 1160.0);
        distanceToVelocityMap.put(768.0, 1180.0);
        distanceToVelocityMap.put(890.0, 1180.0);
        distanceToVelocityMap.put(976.0, 1180.0);
        distanceToVelocityMap.put(1051.0, 1200.0);
        distanceToVelocityMap.put(1126.0, 1200.0);
        distanceToVelocityMap.put(1244.0, 1200.0);
        distanceToVelocityMap.put(1326.0, 1220.0);
        distanceToVelocityMap.put(1436.0, 1220.0);
        distanceToVelocityMap.put(1524.0, 1220.0);
        distanceToVelocityMap.put(1648.0, 1230.0);
        distanceToVelocityMap.put(1748.0, 1230.0);
        distanceToVelocityMap.put(1825.0, 1240.0);
        distanceToVelocityMap.put(1925.0, 1260.0);
        distanceToVelocityMap.put(2046.0, 1280.0);
        distanceToVelocityMap.put(2126.0, 1300.0);
        distanceToVelocityMap.put(2169.0, 1310.0);
        distanceToVelocityMap.put(2400.0, 1360.0);
        distanceToVelocityMap.put(2528.0, 1360.0);
        distanceToVelocityMap.put(2681.0, 1380.0);
        distanceToVelocityMap.put(2751.0, 1400.0);
        distanceToVelocityMap.put(2853.0, 1420.0);
        distanceToVelocityMap.put(2952.0, 1440.0);
        distanceToVelocityMap.put(3014.0, 1440.0);
        distanceToVelocityMap.put(3100.0, 1440.0);
        distanceToVelocityMap.put(3550.0, 1460.0);  // Far, max speed
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
        LLResult result = limelight.getLatestResult();
        if (result.isValid()) {
            List<LLResultTypes.FiducialResult> fiducialResults = result.getFiducialResults();
            if (fiducialResults != null) {
                for (LLResultTypes.FiducialResult fr : fiducialResults) {
                    if (fr.getFiducialId() == 21) {
                        return new ArtifactColor[]{ArtifactColor.GREEN, ArtifactColor.PURPLE, ArtifactColor.PURPLE};
                    }
                    else if (fr.getFiducialId() == 22) {
                        return new ArtifactColor[]{ArtifactColor.PURPLE, ArtifactColor.GREEN, ArtifactColor.PURPLE};
                    }
                    else if (fr.getFiducialId() == 23) {
                        return new ArtifactColor[]{ArtifactColor.PURPLE, ArtifactColor.PURPLE, ArtifactColor.GREEN};
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

    public void toggleLauncherManualFar() {
        if (launcherMotor1.getPower() == 0) {
            startLauncherManualFar();
        }
        else {
            stopLauncher();
        }
    }

    public void toggleLauncherManualNear(){
        if (launcherMotor1.getPower() == 0) {
            startLauncherManualNear();
        }
        else {
            stopLauncher();
        }
    }
    public void startLauncher() {
        //Auto shooting velocity
        if ( limelightValid() ) {
            launcherVelocity = getVelocityDistance(getGoalDistance());
        }
        else {
            launcherVelocity = LAUNCH_VELOCITY_FAR;
        }
        setLauncherVelocity(launcherVelocity);
        launcherActive = true;
    }

    public void startLauncherManualNear(){
        launcherVelocity = LAUNCH_VELOCITY_NEAR;
        setLauncherVelocity(launcherVelocity);
        launcherActive = true;
    }

    public void startLauncherManualFar(){
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
        if ( limelightValid() ) {
            launcherVelocity = getVelocityDistance(getGoalDistance());
        }
        else {
            launcherVelocity = LAUNCH_VELOCITY_NEAR;
        }
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

    public LLResult getLimelightResult(){
        return limelight.getLatestResult();
    }

/*    public double getRedAimingPower(){
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
*/
    public Boolean limelightValid() {
        return limelight.getLatestResult().isValid();
    }

    public void setLimelightPipeline(int pipeline) {
        limelight.pipelineSwitch(pipeline);
    }

    public void setLimelightPipeline(boolean isRed) {
        if (isRed) {
            limelight.pipelineSwitch(Robot.LLPipelines.RED_GOAL.ordinal());    // 5 = RED_GOAL
        } else {
            limelight.pipelineSwitch(Robot.LLPipelines.BLUE_GOAL.ordinal());    // 6 = BLUE_GOAL
        }
    }

    public double getAimingPower(){
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

    public double setAimPowerPID (double time, boolean isRed) {
        double currentTime = time;
        double deltaTime = currentTime - lastTime;
        lastTime = currentTime;

        LLResult result = limelight.getLatestResult();
        double power = 0;
        if(result.isValid()) {
            double currentX = result.getTx();
            if (!isRed) {
                currentX -= 1.25;
            }

            // Proportional term
            double proportional = 0.0;
            proportional = aimKp * currentX;

            // Integral term
            integralSum += currentX * deltaTime;
            // Clamp integral sum to prevent windup
            if (integralSum > integralSumMax) {
                integralSum = integralSumMax;
            } else if (integralSum < integralSumMin) {
                integralSum = integralSumMin;
            }
            double integral = aimKi * integralSum;

            // Derivative term
            double derivative = aimKd * ((currentX - lastError) / deltaTime);
            lastError = currentX;

            // Feedforward term (can be used to counteract gravity or apply a base power)
            double feedforward = 0.0;
            if (currentX < 0) {
                feedforward = 0 - powerStatic; // Or kF * signum(target - currentPosition) for simple direction
            }
            else{
                feedforward = powerStatic;
            }

            // Combine all terms
            power = proportional + integral + derivative + feedforward;
        }
        return power;
    }

/*    public double getREDGoalDistance() {
        limelight.pipelineSwitch(Robot.LLPipelines.RED_GOAL.ordinal());    // 5 = RED_GOAL
        return getGoalDistance();
    }

    public double getBlueGoalDistance(){
        limelight.pipelineSwitch(Robot.LLPipelines.BLUE_GOAL.ordinal());    // 6 = Blue_GOAL
        return getGoalDistance();
    }
*/

    public double getGoalDistance () {
        LLResult llresult = limelight.getLatestResult();
        double distance = 0;
        if (llresult.isValid()) {
            distance = 448 / Math.tan(Math.toRadians(llresult.getTy() + LIMELIGHT_OFFSET));
        }
        return distance;
    }

    public void setLauncherVelocity(double velocity) {
        launcherMotor2.setVelocity(launcherVelocity);
        launcherMotor1.setVelocity(launcherVelocity);
    }

    public void setLauncherVelocityDistance() {
        launcherVelocity = getVelocityDistance(getGoalDistance());
        setLauncherVelocity(launcherVelocity);
        targetVelocity = launcherVelocity;
    }

/*    public void setLauncherVelocityBlueDistance() {
        launcherVelocity = getVelocityDistance(getGoalDistance());
        setLauncherVelocity(launcherVelocity);
    }
*/

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

    public double getVelocityDistance(double currentDistance) {
        // Handle edge cases: distance beyond min/max points
        if (currentDistance <= distanceToVelocityMap.firstKey()) {
            return distanceToVelocityMap.firstEntry().getValue();
        }
        if (currentDistance >= distanceToVelocityMap.lastKey()) {
            return distanceToVelocityMap.lastEntry().getValue();
        }

        // Find the bounding points for linear interpolation
        Map.Entry<Double, Double> lower = distanceToVelocityMap.floorEntry(currentDistance);
        Map.Entry<Double, Double> upper = distanceToVelocityMap.ceilingEntry(currentDistance);

        if (lower == null || upper == null) {
            return 0.0; // Should not happen with the edge case checks, but for safety
        }

        // Perform linear interpolation (LERP)
        double dist1 = lower.getKey();
        double vel1 = lower.getValue();
        double dist2 = upper.getKey();
        double vel2 = upper.getValue();

        // Formula: v = v1 + (v2 - v1) * ((d - d1) / (d2 - d1))
        double velocity = vel1 + (vel2 - vel1) * ((currentDistance - dist1) / (dist2 - dist1));

        return velocity;
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

}