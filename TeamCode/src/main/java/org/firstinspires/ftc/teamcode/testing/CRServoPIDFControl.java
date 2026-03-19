package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

/**
 * Advanced CRServo PIDF Control with Axon Servo Feedback
 *
 * Features:
 * - Position control using analog feedback
 * - Velocity calculation and limiting
 * - Motion profiling for smooth movements
 * - Gravity compensation
 * - Advanced anti-windup
 *
 * Hardware Setup:
 * - CRServo on servo port
 * - Axon servo analog feedback on analog input port
 *
 * Axon Analog Output: 0-3.3V represents 0-333 degrees
 */
@Config
@TeleOp(name="CRServo Advanced Axon Control", group="Advanced")
public class CRServoPIDFControl extends LinearOpMode {

    // Hardware
    private CRServo crServo;
    private AnalogInput analogInput;

    // PIDF Coefficients
    public static double kP = 0.0038;  // Position proportional
    public static double kI = 0.01;  // Position integral
    public static double kD = 0.001;  // Position derivative
    public static double kF = 0.07;    // Feedforward (gravity compensation)
    public static double  power = 0.0;
    public static double error = 0.0;

    // Velocity PIDF (optional - for velocity mode)
    private double kV_P = 0.01;
    private double kV_I = 0.0;
    private double kV_D = 0.0;

    // Position tracking
    public static double targetPosition = 0.0;
    public static double currentPosition = 0.0;
    public static double lastPosition = 0.0;

    // Velocity tracking
    private double currentVelocity = 0.0;      // degrees/sec
    private double maxVelocity = 180.0;        // degrees/sec limit

    // Control state
    private double integralSum = 0.0;
    private double lastError = 0.0;

    // Timing
    private ElapsedTime timer = new ElapsedTime();
    private double lastTime = 0.0;

    // Configuration
    private static final double VOLTAGE_TO_DEGREES = 360.0 / 3.3;
    private static final double INTEGRAL_MAX = 1.0;
    public static double POSITION_TOLERANCE = 0;
    private static final double VELOCITY_FILTER_ALPHA = 0.7;  // Low-pass filter

    // Position limits
    private static final double MIN_POSITION = 0.0;
    private static final double MAX_POSITION = 270.0;

    // BALL POSITIONS - emperical 13Feb26
    private double BALL3_OUTPUT = 168.8;
    private double BALL2_OUTPUT = 63.7;
    private double BALL1_OUTPUT = 276.0;

    // Motion profiling
    private boolean useMotionProfile = false;
    private double profiledTarget = 0.0;
    private double maxAcceleration = 360.0;  // degrees/sec^2


    // Control modes
    private enum ControlMode {
        POSITION,
        VELOCITY
    }
    private ControlMode controlMode = ControlMode.POSITION;

    @Override
    public void runOpMode() {

        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());


        // Initialize hardware
        crServo = hardwareMap.get(CRServo.class, "turretServo");
        analogInput = hardwareMap.get(AnalogInput.class, "turretAnalog");

        // TODO erase this line if you want
        int debug=0;

        // Initialize position
        currentPosition = readPosition();
        lastPosition = currentPosition;
        targetPosition = currentPosition;
        profiledTarget = currentPosition;

        telemetry.addData("Status", "Initialized");
        telemetry.addData("Mode", "Advanced PIDF with Motion Profiling");
        telemetry.addData("Initial Position", "%.1f°", currentPosition);
        telemetry.update();

        waitForStart();
        timer.reset();
        lastTime = timer.seconds();

        while (opModeIsActive()) {
            // Read current position and calculate velocity
            updatePositionAndVelocity();

            // Update target from gamepad
            updateTarget();

            // Apply motion profiling
            if (useMotionProfile) {
                profiledTarget = applyMotionProfile(profiledTarget, targetPosition);
            } else {
                profiledTarget = targetPosition;
            }

            // Tune PIDF gains
            //tunePIDFGains();

            // Calculate control output
            power = calculateControl();


            // Apply power
            crServo.setPower(power);

            // Telemetry
            updateTelemetry(power);
        }

        crServo.setPower(0.0);
    }

    /**
     * Read position from Axon and calculate velocity
     */
    private void updatePositionAndVelocity() {
        double currentTime = timer.seconds();
        double dt = currentTime - lastTime;
        if (dt == 0) dt = 0.02;

        // Read position
        currentPosition = readPosition();

        // Calculate velocity (degrees/sec)
        //double rawVelocity = (currentPosition - lastPosition) / dt;

        // Apply low-pass filter to smooth velocity
        //currentVelocity = VELOCITY_FILTER_ALPHA * currentVelocity +
            //(1.0 - VELOCITY_FILTER_ALPHA) * rawVelocity;

        // Update tracking variables
        lastPosition = currentPosition;
        lastTime = currentTime;
    }

    /**
     * Read position from Axon analog feedback
     */
    private double readPosition() {
        double voltage = analogInput.getVoltage();
        telemetry.addData("readPosition Votage:", "%.3f", voltage);
        telemetry.addData("readPosition Degrees:", "%.2f°", voltage * VOLTAGE_TO_DEGREES);
        return voltage * VOLTAGE_TO_DEGREES;
    }

    /**
     * Update target position from gamepad
     */
    private void updateTarget() {
        // Position control mode
//        if (gamepad1.left_stick_y != 0) {
//            double stickValue = -gamepad1.left_stick_y;
//            targetPosition = MIN_POSITION + (stickValue + 1.0) / 2.0 *
//                (MAX_POSITION - MIN_POSITION);
//            controlMode = ControlMode.POSITION;
//        }

        // Fine adjustment
//        if (Math.abs(gamepad1.right_stick_y) > 0.1) {
//            targetPosition -= gamepad1.right_stick_y * 0.3;
//        }

        if (gamepad2.a) targetPosition -= 0.01;
        if (gamepad2.b) targetPosition += 0.01;

        // Preset positions
        if (gamepad1.dpad_left) {
            targetPosition = BALL1_OUTPUT;
            sleep(200);
        } else if (gamepad1.dpad_right) {
            targetPosition = BALL3_OUTPUT;
            sleep(200);
        } else if (gamepad1.dpad_up) {
            targetPosition = BALL2_OUTPUT;
            sleep(200);
        }

//        // Toggle motion profiling
//        if (gamepad1.back) {
//            useMotionProfile = !useMotionProfile;
//            sleep(300);
//        }
//
//        // Reset to current
//        if (gamepad2.start) {
//            targetPosition = currentPosition;
//            profiledTarget = currentPosition;
//            integralSum = 0.0;
//            sleep(200);
//        }

        // Clamp target
        //targetPosition = clamp(targetPosition, MIN_POSITION, MAX_POSITION);
    }

    /**
     * Apply trapezoidal motion profile for smooth acceleration
     */
    private double applyMotionProfile(double current, double target) {
        double currentTime = timer.seconds();
        double dt = currentTime - lastTime;
        if (dt == 0) dt = 0.02;

        error = target - current;

        // Calculate maximum position change based on velocity and acceleration limits
        double maxVelocityChange = maxAcceleration * dt;
        double desiredVelocity = error / dt;

        // Limit velocity
        desiredVelocity = clamp(desiredVelocity, -maxVelocity, maxVelocity);

        // Apply acceleration limit
        double velocityChange = desiredVelocity - (profiledTarget - lastPosition) / dt;
        velocityChange = clamp(velocityChange, -maxVelocityChange, maxVelocityChange);

        double newVelocity = (profiledTarget - lastPosition) / dt + velocityChange;
        double newPosition = current + newVelocity * dt;

        return newPosition;
    }

    /**
     * Calculate PIDF control output
     */
    private double calculateControl() {
        double currentTime = timer.seconds();
        double dt = currentTime - lastTime;
        if (dt == 0) dt = 0.02;

        // Position error
        error = profiledTarget - currentPosition;

        double sign = Math.signum(error);

        // Handle wraparound
        if (error > 180.0) error -= 360.0;
        else if (error < -180.0) error += 360.0;

        // P term
        double pTerm = kP * error;

        // I term with conditional integration
        if (Math.abs(error) > POSITION_TOLERANCE) {
            integralSum += error * dt;
            // Anti-windup with clamping
            integralSum = clamp(integralSum, -INTEGRAL_MAX, INTEGRAL_MAX);

            // Anti-windup: reset if error changes sign
            if (error * lastError < 0) {
                integralSum *= 0.5;
            }
        }
        else {
            // Decay integral when at target
            integralSum *= 0.95;
        }
        double iTerm = kI * integralSum;

        // D term (using position derivative, not error derivative)
        // This prevents derivative kick on setpoint changes
        double dTerm = -kD * (error - lastError);

        // F term for gravity compensation
        // Adjust based on position (e.g., more assistance at horizontal)
        double fTerm = kF * Math.signum(error);

        lastError = error;

        // Combine terms
        double output = pTerm + iTerm + dTerm + fTerm;

        // Deadband to prevent jitter
        if (Math.abs(error) < POSITION_TOLERANCE) {
            output = 0.0;
        }

        /*if (Math.abs(error) < POSITION_TOLERANCE && Math.abs(output) < 0.02) {
            output = 0.0;
        }*/
        RobotLog.d("velocity: %.2f", output);
        return clamp(output, -1.0, 1.0);
    }

    /**
     * Tune PIDF gains in real-time
     */
    private void tunePIDFGains() {
        double increment = gamepad1.right_trigger > 0.5 ? 0.0001 : 0.001;

        if (gamepad1.x) {
            kP += increment;
            sleep(100);
        } else if (gamepad1.y) {
            kP = Math.max(0, kP - increment);
            sleep(100);
        }

        if (gamepad1.a) {
            kI += increment * 0.1;
            sleep(100);
        } else if (gamepad1.b) {
            kI = Math.max(0, kI - increment * 0.1);
            sleep(100);
        }

        if (gamepad1.right_bumper && !gamepad1.y) {
            kD += increment;
            sleep(100);
        } else if (gamepad1.left_bumper && !gamepad1.y) {
            kD = Math.max(0, kD - increment);
            sleep(100);
        }

        // Adjust max velocity
        if (gamepad1.start) {
            maxVelocity += 10.0;
            sleep(100);
        }
    }

    /**
     * Display telemetry
     */
    private void updateTelemetry(double power) {
        boolean atTarget = Math.abs(error) < POSITION_TOLERANCE;

        telemetry.addData("=== Position ===", "");
        telemetry.addData("Target", "%.1f°", targetPosition);
        telemetry.addData("Profiled", "%.1f°", profiledTarget);
        telemetry.addData("Current", "%.1f°", currentPosition);
        telemetry.addData("Error", "%.1f°", error);
        telemetry.addData("At Target", atTarget);

        telemetry.addData("", "");
        telemetry.addData("=== Velocity ===", "");
        telemetry.addData("Current", "%.1f°/s", currentVelocity);
        telemetry.addData("Max Limit", "%.1f°/s", maxVelocity);

        telemetry.addData("", "");
        telemetry.addData("=== Control ===", "");
        telemetry.addData("Mode", controlMode);
        telemetry.addData("Motion Profile", useMotionProfile ? "ON" : "OFF");
        telemetry.addData("Power", "%.3f", power);
        telemetry.addData("Voltage", "%.3fV", analogInput.getVoltage());

        telemetry.addData("", "");
        telemetry.addData("=== PIDF ===", "");
        telemetry.addData("kP", "%.5f", kP);
        telemetry.addData("kI", "%.5f", kI);
        telemetry.addData("kD", "%.5f", kD);
        telemetry.addData("kF", "%.5f", kF);
        telemetry.addData("Integral", "%.3f", integralSum);

        telemetry.update();
    }

    /**
     * Clamp value
     */
    private double clamp(double value, double min, double max) {
        return Math.max(min, Math.min(max, value));
    }
}