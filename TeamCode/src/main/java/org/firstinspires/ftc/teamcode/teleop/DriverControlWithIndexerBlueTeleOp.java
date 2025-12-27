package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.teamcode.common.Robot;

@TeleOp(name = "BLUE Driver Control With Indexer Teleop", group = "0teleop")
public class DriverControlWithIndexerBlueTeleOp extends LinearOpMode {
    public boolean isRedSide = false;
    public boolean isBlueSide = true;

    @Override
    public void runOpMode() throws InterruptedException {

        // TODO Add Data to Dashboard Start
        FtcDashboard dashboard = FtcDashboard.getInstance();
        telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());


        Robot robot = new Robot(hardwareMap, telemetry);

        double driveSpeed = 1;
        boolean fieldCentric = true;
        double index_position = 0.5;
        boolean isAiming = false;
        boolean autoLaunch = true;
        boolean aprilTagInView = false;
        double targetLauncherVelocity = 0.0;

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

        robot.getLauncher().setLimelightPipeline(isRedSide, isBlueSide);

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
                gamepad1.rumble(300);
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
                    double power = robot.getLauncher().setAimPowerPID(aimTimer.milliseconds(), isRedSide, isBlueSide);
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
                autoLaunch = false;
            }

            if (currentGamepad2.a && !previousGamepad2.a) {
                robot.getLauncher().toggleLauncherManualNear();
                autoLaunch = false;
            }

            if (currentGamepad2.x && !previousGamepad2.x) {
                robot.getLauncher().toggleLauncher();
                autoLaunch = true;
            }

            if (currentGamepad2.dpad_up && !previousGamepad2.dpad_up) {
                robot.getLauncher().changeLauncherVelocity(50);
            }

            if (currentGamepad2.dpad_down && !previousGamepad2.dpad_down) {
                robot.getLauncher().changeLauncherVelocity(-50);
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

            //rumble gamepad 2 when apriltag is in view
            /*if(robot.getLauncher().getLimelightResult().isValid() && !aprilTagInView && robot.getLauncher().getLauncherTargetVelocity() == 0.0){
                gamepad2.rumble(50);
                gamepad2.setLedColor(255, 255, 255, 50);
                aprilTagInView = true;
            }
            if(!robot.getLauncher().getLimelightResult().isValid() && aprilTagInView){
                gamepad2.rumble(0.5, .5, 60);
                aprilTagInView = false;
            }*/

            //change gamepad 2 light barwhen sped up all the way
            if(robot.getLauncher().getLauncherVelocity() == robot.getLauncher().getLauncherTargetVelocity() && robot.getLauncher().getLauncherTargetVelocity() != 0.0){
                gamepad2.setLedColor(255, 255, 0, 20);
            }

            //rumble gamepad 2 when empty
            if(robot.getIndexer().artifactColorArray == new Robot.ArtifactColor[] {Robot.ArtifactColor.NONE, Robot.ArtifactColor.NONE, Robot.ArtifactColor.NONE} && robot.getLauncher().getLauncherTargetVelocity() != 0.0){
                gamepad2.rumble(0.25, 0, 10);
                gamepad2.rumble(0, 0.25, 10);
            }


            //telemetry.addData("launcher power:", robot.getLauncher().getLaunchPower());
            telemetry.addData("launcher velocity:", robot.getLauncher().getLauncherVelocity());
            telemetry.addData("color:", robot.getIndexer().artifactColorArray[0]);
            telemetry.addData("color:", robot.getIndexer().artifactColorArray[1]);
            telemetry.addData("color:", robot.getIndexer().artifactColorArray[2]);
            RobotLog.d("launcher velocity: %f",
                    robot.getLauncher().getLauncherVelocity());

            // Refresh the indicator lights
            robot.getHud().setBalls(robot.getIndexer().artifactColorArray[0], robot.getIndexer().artifactColorArray[1],robot.getIndexer().artifactColorArray[2]);
            robot.getHud().setAimIndicator(isAiming);
            robot.getHud().UpdateBallUI2();

            // TODO Add timing Log at end of loop
//            RobotLog.d("c0: %s c1: %s c2: %s",
//                    robot.getIndexer().artifactColorArray[0],
//                    robot.getIndexer().artifactColorArray[1],
//                    robot.getIndexer().artifactColorArray[2]);

            telemetry.update();
        }
    }
}
