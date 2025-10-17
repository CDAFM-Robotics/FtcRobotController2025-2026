package org.firstinspires.ftc.teamcode.testing.archived;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;


/*
 * This file contains an minimal example of a Linear "OpMode". An OpMode is a 'program' that runs in either
 * the autonomous or the teleop period of an FTC match. The names of OpModes appear on the menu
 * of the FTC Driver Station. When a selection is made from the menu, the corresponding OpMode
 * class is instantiated on the Robot Controller and executed.
 *
 * This particular OpMode just executes a basic Tank Drive Teleop for a two wheeled robot
 * It includes all the skeletal structure that all linear OpModes contain.
 *
 * Use Android Studio to Copy this Class, and Paste it into your team's code folder with a new name.
 * Remove or comment out the @Disabled line to add this OpMode to the Driver Station OpMode list
 */

@TeleOp(name="Inverse Kinematics Test", group="testing")
@Disabled
public class InverseKinematicsTestOpMode extends LinearOpMode {

    // Declare OpMode members.
    private ElapsedTime runtime = new ElapsedTime();
    private DcMotor leftDrive = null;
    private DcMotor rightDrive = null;



    public Gamepad currentGamepad1 = new Gamepad();
    public Gamepad previousGamepad1 = new Gamepad();
    public Gamepad currentGamepad2 = new Gamepad();
    public Gamepad previousGamepad2 = new Gamepad();
    @Override
    public void runOpMode() {
        telemetry.addData("Status", "Initialized");
        telemetry.update();

        // Initialize the hardware variables. Note that the strings used here as parameters
        // to 'get' must correspond to the names assigned during the robot configuration
        // step (using the FTC Robot Controller app on the phone).
        leftDrive  = hardwareMap.get(DcMotor.class, "left_drive");
        rightDrive = hardwareMap.get(DcMotor.class, "right_drive");

        // To drive forward, most robots need the motor on one side to be reversed, because the axles point in opposite directions.
        // Pushing the left stick forward MUST make robot go forward. So adjust these two lines based on your first test drive.
        // Note: The settings here assume direct drive on left and right wheels.  Gear Reduction or 90 Deg drives may require direction flips
        leftDrive.setDirection(DcMotor.Direction.REVERSE);
        rightDrive.setDirection(DcMotor.Direction.FORWARD);

        // Wait for the game to start (driver presses START)
        waitForStart();
        runtime.reset();
        int ARM_ONE_LENGTH_MM = 400;
        int ARM_TWO_LENGTH_MM = 300;

        double x;
        double y;
        double z;

        // run until the end of the match (driver presses STOP)
        while (opModeIsActive()) {
//            telemetry.addData("x", x);
//            telemetry.addData("y", y);
//            telemetry.addData("z", z);

//            double baseAngle = (double) Math.atan(x/y);
//            double AngleA = Math.acos(((-1*Math.pow(ARM_TWO_LENGTH_MM, 2)) + Math.pow(ARM_ONE_LENGTH_MM, 2) + Math.pow(x, 2) + Math.pow(z, 2))/(2*ARM_ONE_LENGTH_MM*Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2)));
//            double Servo1 = AngleA + Math.atan(z/x);
//            double Servo2 = Math.asin((Math.sqrt(Math.pow(x, 2) + Math.pow(z, 2)) * Math.sin(AngleA)) / ARM_TWO_LENGTH_MM)
            telemetry.update();
        }
    }
}
