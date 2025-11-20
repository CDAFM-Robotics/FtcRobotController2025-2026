package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.common.Robot;

@TeleOp(name = "RED Driver Control With Indexer Teleop", group = "0teleop")
public class DriverControlWithIndexerRedTeleOp extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Robot robot = new Robot(hardwareMap, telemetry);

        double driveSpeed = 1;
        boolean fieldCentric = true;
        double index_position = 0.5;
        boolean isTurning = false;

        Gamepad currentGamepad1 = new Gamepad();
        Gamepad previousGamepad1 = new Gamepad();
        Gamepad currentGamepad2 = new Gamepad();
        Gamepad previousGamepad2 = new Gamepad();

        ElapsedTime timeSinceLastIncident = new ElapsedTime();

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

            if (currentGamepad1.share && !previousGamepad1.share){
                fieldCentric = !fieldCentric;
            }

            if (currentGamepad1.options && !previousGamepad1.options){
                robot.getDriveBase().resetIMU();
            }

            if (currentGamepad1.right_bumper != previousGamepad1.right_bumper) {
                driveSpeed = driveSpeed == 1 ? 0.5 : 1;
            }

            if (currentGamepad2.y) {
                double power = robot.getLauncher().getRedAimingPower();
                telemetry.addData("aiming: motor power", power);
                robot.getDriveBase().setMotorPowers(0, 0, power, driveSpeed, fieldCentric);
            }
            else {
                robot.getDriveBase().setMotorPowers(currentGamepad1.left_stick_x, -currentGamepad1.left_stick_y, currentGamepad1.right_stick_x, driveSpeed, fieldCentric);
            }

            telemetry.addData("limelight x", robot.getLauncher().getLimelightResult().getTx());

            // Active Intake
            if (currentGamepad1.right_trigger != 0.0 || currentGamepad2.left_trigger != 0.0) {
                //telemetry.addLine("gameped 1 right trigger or 2 left trigger");
                robot.getIntake().startIntake();
                if (currentGamepad1.right_trigger != 0.0)
                    robot.intakeWithIndexerTurn();
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

            // Manual Indexer control.
            // TODO: Add checking the kicker position so the indexer will not hit the kicker
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
                robot.getLauncher().toggleLauncher();
            }

            if (currentGamepad2.a && !previousGamepad2.a) {
              robot.getLauncher().toggleLauncherPartialPower();
            }

            if (currentGamepad2.dpad_up && !previousGamepad2.dpad_up) {
                robot.getLauncher().changeLauncherPower(0.05);
            }

            if (currentGamepad2.dpad_down && !previousGamepad2.dpad_down) {
                robot.getLauncher().changeLauncherPower(-0.05);
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

            telemetry.addData("launcher power:", robot.getLauncher().getLaunchPower());
            telemetry.addData("color:", robot.getIndexer().artifactColorArray[0]);
            telemetry.addData("color:", robot.getIndexer().artifactColorArray[1]);
            telemetry.addData("color:", robot.getIndexer().artifactColorArray[2]);

            // Refresh the indicator lights
            robot.getHud().setBalls(robot.getIndexer().artifactColorArray[0], robot.getIndexer().artifactColorArray[1],robot.getIndexer().artifactColorArray[2]);
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
