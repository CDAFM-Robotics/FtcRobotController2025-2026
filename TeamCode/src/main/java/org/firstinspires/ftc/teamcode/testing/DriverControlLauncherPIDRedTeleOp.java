package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.Robot;
@Disabled
@Config
@TeleOp(name = "RED Driver Control Launcher PID RedTeleop", group = "0teleop")
public class DriverControlLauncherPIDRedTeleOp extends LinearOpMode {
    public boolean isRedSide = true;

    @Override
    public void runOpMode() throws InterruptedException {

        // TODO Add Data to Dashboard Start
        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());

        //dashTelemetry.addData("HelloWorld", 0.0);
        //dashTelemetry.update();
        // TODO Add Data to Dashboard End

        Robot robot = new Robot(hardwareMap, telemetry);

        double driveSpeed = 1;
        boolean fieldCentric = true;
        double index_position = 0.5;
        boolean isAiming = false;
        boolean autoLaunch = true;

        Gamepad currentGamepad1 = new Gamepad();
        Gamepad previousGamepad1 = new Gamepad();
        Gamepad currentGamepad2 = new Gamepad();
        Gamepad previousGamepad2 = new Gamepad();

        ElapsedTime timeSinceLastIncident = new ElapsedTime();
        ElapsedTime initializedIndexerTimer  = new ElapsedTime();
        ElapsedTime aimTimer  = new ElapsedTime();

        initializedIndexerTimer.reset();
        aimTimer.reset();
        robot.resetIndexerColorStart();
        while (initializedIndexerTimer.milliseconds() < 1800) {
            robot.resetIndexer();
        }

        robot.getLauncher().setLimelightPipeline(isRedSide);


        waitForStart();

        while (opModeIsActive()){
            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);
            currentGamepad1.copy(gamepad1);
            currentGamepad2.copy(gamepad2);

            timeSinceLastIncident.reset();

            // Drive Base

            if (currentGamepad1.left_stick_button && !previousGamepad1.left_stick_button){
                driveSpeed = driveSpeed == 1 ? 0.5 : 1;
            }

            if (currentGamepad1.back && !previousGamepad1.back){
                fieldCentric = !fieldCentric;
            }

            if (currentGamepad1.start && !previousGamepad1.start){
                robot.getDriveBase().resetIMU();
            }

            if (currentGamepad1.right_bumper != previousGamepad1.right_bumper) {
                driveSpeed = driveSpeed == 1 ? 0.5 : 1;
            }

            if(currentGamepad2.y && !previousGamepad2.y){
                isAiming = true;
                aimTimer.reset();
            }
            telemetry.addData("left_bumper pushed: is aiming", isAiming);
            telemetry.addData("Limelight valid", robot.getLauncher().limelightValid());

            if (currentGamepad1.left_stick_x == 0 && currentGamepad1.left_stick_y == 0
                && currentGamepad1.right_stick_x ==0 && currentGamepad1.right_stick_y == 0 && isAiming){
                double power = robot.getLauncher().setAimPowerPID(aimTimer.milliseconds(), isRedSide);
                telemetry.addData("aiming: motor power", power);
                robot.getDriveBase().setMotorPowers(0, 0, power, driveSpeed, fieldCentric);
            }
            else {
                robot.getDriveBase().setMotorPowers(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x, driveSpeed, fieldCentric);
                isAiming = false;
            }

            telemetry.addData("limelight valid", robot.getLauncher().getLimelightResult().isValid());
            telemetry.addData("limelight x", robot.getLauncher().getLimelightResult().getTx());
            telemetry.addData("limelight y", robot.getLauncher().getLimelightResult().getTy());
            telemetry.addData("Distance to AprilTag", robot.getLauncher().getGoalDistance());
            // Active Intake
            if (currentGamepad1.right_trigger != 0.0 || currentGamepad2.left_trigger != 0.0) {
                //telemetry.addLine("gameped 1 right trigger or 2 left trigger");
                robot.getIntake().startIntake();
                if (currentGamepad1.right_trigger != 0.0)
                    robot.intakeWithIndexerTurn(gamepad1);
            }
            else if ((currentGamepad1.right_trigger == 0.0 && previousGamepad1.right_trigger != 0)
                || (currentGamepad2.left_trigger == 0.0 && previousGamepad2.left_trigger != 0)){
                robot.getIntake().stopIntake();
            }

            if (currentGamepad1.left_trigger != 0) {
                robot.getIntake().reverseIntake();
            }
            else if (currentGamepad1.left_trigger == 0 && previousGamepad1.left_trigger != 0){
                robot.getIntake().stopIntake();
            }

            if (currentGamepad1.a != previousGamepad1.a) {
                robot.getDriveBase().setKickStand();
            }

            if (currentGamepad1.b != previousGamepad1.b) {
                robot.getDriveBase().resetKickStand();
            }

            // Manual Indexer control. (deprecated)
            // removed the manual indexer control after auto indexer control is implemented
            /*if (currentGamepad2.x && !previousGamepad2.x) {
                robot.getIndexer().rotateClockwise();
            }

            if (currentGamepad2.y && !previousGamepad2.y) {
                robot.getIndexer().rotateCounterClockwise();
            }*/

            // When indexer stuck or out of alignment, recover the color of the balls
            if (currentGamepad2.left_trigger != 0 && previousGamepad2.left_trigger == 0){
                robot.resetIndexerColorStart();
            }

            if (currentGamepad2.left_trigger != 0)
                robot.resetIndexer();

            // TODO this line of code generates a call every 6-8ms
            // telemetry.addData("index position: ", robot.getIndexer().getIndexerPosition());

            //Launcher

            if (currentGamepad2.b && !previousGamepad2.b) {
                robot.getLauncher().toggleLauncherManualFar();
                autoLaunch = true;
            }

            if (currentGamepad2.a && !previousGamepad2.a) {
                robot.getLauncher().toggleLauncherManualNear();
                autoLaunch = false;
            }

//            if (currentGamepad2.x && !previousGamepad2.x) {
//                robot.getLauncher().startLauncher();
//                autoLaunch = false;
//            }

            if (currentGamepad2.dpad_up && !previousGamepad2.dpad_up) {
                robot.getLauncher().changeLauncherVelocity(10);
            }

            if (currentGamepad2.dpad_down && !previousGamepad2.dpad_down) {
                robot.getLauncher().changeLauncherVelocity(-10);
            }

            //set launcher velocity
            if ( robot.getLauncher().limelightValid()
                && robot.getLauncher().isLauncherActive()
                && autoLaunch) {
                robot.getLauncher().setLauncherVelocityDistance();
            }

            //launch a green ball
            if (currentGamepad2.left_bumper && !previousGamepad2.left_bumper){
                robot.stratLaunchAGreenBall();
            }

            if (currentGamepad2.left_bumper) {
                robot.launchAColorBall();
            }

            //launch a purple ball
            if (currentGamepad2.right_bumper && !previousGamepad2.right_bumper){
                robot.stratLaunchAPurpleBall();
            }

            if (currentGamepad2.right_bumper) {
                robot.launchAColorBall();
            }

            //launch all balls in the robot
            if (currentGamepad2.right_trigger != 0) {
                robot.shootAllBalls();
            }

            if (currentGamepad2.x && !previousGamepad2.x) {
                robot.getLauncher().setLaunchMotorPIDFCoefficients();
            }

            //telemetry.addData("launcher power:", robot.getLauncher().getLaunchPower());
            telemetry.addData("launcher target velocity:", robot.getLauncher().targetVelocity);
            telemetry.addData("launcher current velocity:", robot.getLauncher().getLauncherVelocity());
            telemetry.addData("launcher current velocity2:", robot.getLauncher().getLauncherVelocity2());
            telemetry.addData("launch motors p", robot.getLauncher().getLauncherMotorPIDFCoefficients().p);
            telemetry.addData("launch motors i", robot.getLauncher().getLauncherMotorPIDFCoefficients().i);
            telemetry.addData("launch motors d", robot.getLauncher().getLauncherMotorPIDFCoefficients().d);
            telemetry.addData("launch motors f", robot.getLauncher().getLauncherMotorPIDFCoefficients().f);
            telemetry.addData("shootKp", robot.getLauncher().shootKp);
            telemetry.addData("shootKi", robot.getLauncher().shootKi);
            telemetry.addData("shootKd", robot.getLauncher().shootKd);
            telemetry.addData("shootKf", robot.getLauncher().shootKd);
            telemetry.addData("color:", robot.getIndexer().artifactColorArray[0]);
            telemetry.addData("color:", robot.getIndexer().artifactColorArray[1]);
            telemetry.addData("color:", robot.getIndexer().artifactColorArray[2]);
            RobotLog.d("launcher velocity: %f",
                robot.getLauncher().getLauncherVelocity());

            // Refresh the indicator lights
            robot.getHud().setBalls(robot.getIndexer().artifactColorArray[0], robot.getIndexer().artifactColorArray[1],robot.getIndexer().artifactColorArray[2]);
            robot.getHud().setAimIndicator(isAiming);
            robot.getHud().UpdateBallUI();

            // TODO Add timing Log at end of loop
//            RobotLog.d("c0: %s c1: %s c2: %s",
//                    robot.getIndexer().artifactColorArray[0],
//                    robot.getIndexer().artifactColorArray[1],
//                    robot.getIndexer().artifactColorArray[2]);

            telemetry.update();
        }
    }
}

