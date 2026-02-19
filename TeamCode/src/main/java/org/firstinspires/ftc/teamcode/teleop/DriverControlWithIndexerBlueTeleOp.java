package org.firstinspires.ftc.teamcode.teleop;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.internal.camera.delegating.DelegatingCaptureSequence;
import org.firstinspires.ftc.teamcode.common.Robot;
import org.firstinspires.ftc.teamcode.common.subsystems.Launcher;

@TeleOp(name = "BLUE Bot2", group = "0teleop")
public class DriverControlWithIndexerBlueTeleOp extends LinearOpMode {
    public boolean isRedSide = false;

    @Override
    public void runOpMode() throws InterruptedException {

        // TODO Add Data to Dashboard Start
        // FtcDashboard dashboard = FtcDashboard.getInstance();
        // telemetry = new MultipleTelemetry(telemetry, dashboard.getTelemetry());

        Robot robot = new Robot(hardwareMap, telemetry);

        double driveSpeed = 1;
        boolean fieldCentric = true;
        boolean waitForReverseTimer = false;
        int REVERSE_INTAKE_TIME = 500;

        Gamepad currentGamepad1 = new Gamepad();
        Gamepad previousGamepad1 = new Gamepad();
        Gamepad currentGamepad2 = new Gamepad();
        Gamepad previousGamepad2 = new Gamepad();

        ElapsedTime rumbleLauncherTimer  = new ElapsedTime();
        rumbleLauncherTimer.reset();
        ElapsedTime reverseIntakeTimer  = new ElapsedTime();
        reverseIntakeTimer.reset();
        //robot.getLauncher().setLimelightPipeline(isRedSide);
        telemetry.update();

        waitForStart();

        while (opModeIsActive()){
            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);
            currentGamepad1.copy(gamepad1);
            currentGamepad2.copy(gamepad2);

            // Driving controls for the robot
            if (currentGamepad1.left_stick_button && !previousGamepad1.left_stick_button){
                driveSpeed = driveSpeed == 1 ? 0.5 : 1;
            }

            if (currentGamepad1.back && !previousGamepad1.back){
                fieldCentric = !fieldCentric;
            }

            // Disabled the driver's ability to reset robot heading
            // since we are keeping the heading from autonomous
//            if (currentGamepad1.start && !previousGamepad1.start){
//                robot.getDriveBase().resetIMU();
//                gamepad1.rumble(300);
//            }

            if (currentGamepad1.right_bumper != previousGamepad1.right_bumper) {
                driveSpeed = driveSpeed == 1 ? 0.5 : 1;
            }

            robot.getDriveBase().setMotorPowers(gamepad1.left_stick_x, -gamepad1.left_stick_y, gamepad1.right_stick_x, driveSpeed, fieldCentric);

            // Kickstand control
//            if (currentGamepad1.a != previousGamepad1.a) {
//                robot.getDriveBase().setKickStand();
//                robot.getDriveBase().setKickStandLight();
//            }
//
//            if (currentGamepad1.b != previousGamepad1.b) {
//                robot.getDriveBase().resetKickStand();
//                robot.getDriveBase().resetKickStandLight();
//            }

            // Intake Balls
            if (currentGamepad1.right_trigger != 0.0) {
                //telemetry.addLine("gameped 1 right trigger or 2 left trigger");
                //start the intake rolling
                robot.getIntake().startIntake();
                //turn the indexer for intake
                robot.intakeWithIndexerTurn();
            }
            else if (currentGamepad1.right_trigger == 0.0 && previousGamepad1.right_trigger != 0){
                //robot update artifact colors
                robot.getIntake().stopIntake();
            }

            if (currentGamepad1.left_trigger != 0) {
                robot.getIntake().reverseIntake();
            }
            else if (currentGamepad1.left_trigger == 0.0 && previousGamepad1.left_trigger != 0){
                //robot update artifact colors
                robot.getIntake().stopIntake();
            }



            // reverse the intake for half a second to prevent the robot from intake the fourth ball
            if (waitForReverseTimer
                    && reverseIntakeTimer.milliseconds() >= REVERSE_INTAKE_TIME
                    && robot.getIntake().getIntakeState() == -1) {
                waitForReverseTimer = false;
                robot.getIntake().stopIntake();
            }

            // When indexer stuck or out of alignment, recover the color of the balls
            if (currentGamepad2.left_trigger != 0 && previousGamepad2.left_trigger == 0){
                robot.updateColorAllSlots();
            }

            // Launcher
            if (currentGamepad2.x && !previousGamepad2.x) {
                robot.getLauncher().toggleLauncher();
                if (robot.getLauncher().isLauncherActive()){
                    gamepad2.rumble(0.0,1.0,500);
                }
                else{
                    gamepad2.rumble(1.0,0.0,250);
                }
            }

            if (robot.getLauncher().isLauncherActive() && rumbleLauncherTimer.milliseconds() > 1000){
                gamepad2.rumble(0.0,1.0,500);
                rumbleLauncherTimer.reset();
            }

            if (currentGamepad2.dpad_up && !previousGamepad2.dpad_up) {
                robot.getLauncher().changeLauncherVelocity(20);
            }

            if (currentGamepad2.dpad_down && !previousGamepad2.dpad_down) {
                robot.getLauncher().changeLauncherVelocity(-20);
            }

            if (currentGamepad2.dpad_left && !previousGamepad2.dpad_left) {
                robot.getLauncher().changeHood(-0.05);
            }

            if (currentGamepad2.dpad_right && !previousGamepad2.dpad_right) {
                robot.getLauncher().changeHood(0.05);
            }

            telemetry.addData("hoodServo postion", robot.getLauncher().getHoodServoPosition());
            telemetry.addData("kickerServo postion", robot.getLauncher().getKickerServoPosition());
            telemetry.addData("isLauncher active", robot.getLauncher().isLauncherActive());

            //set launcher velocity
//            if ( robot.getLauncher().limelightValid()
//                    && robot.getLauncher().isLauncherActive()
//                    && autoLaunch) {
//                robot.getLauncher().setLauncherVelocityDistance();
//            }

            //launch a green ball
//            if (currentGamepad2.left_bumper && !previousGamepad2.left_bumper){
//                robot.startLaunchAGreenBall();
//            }
//            if (currentGamepad2.left_bumper) {
//                    robot.launchAColorBall();
//            }
//
//            //launch a purple ball
//            if (currentGamepad2.right_bumper && !previousGamepad2.right_bumper){
//                robot.startLaunchAPurpleBall();
//            }
//
//            if (currentGamepad2.right_bumper) {
//                    robot.launchAColorBall();
//            }

            //Launch all balls in the robot.
            if (currentGamepad2.right_trigger != 0) {
                robot.shootAllBalls();
            }

            if (currentGamepad2.right_trigger == 0 && !robot.isSafeToStop()) {
                robot.shootAllBalls();
            }

            //TODO: driver 1 would like the gamepad 1 to rumble when the robot pick up a ball
/*            if (robot.isIntake1Ball()) {
                gamepad1.rumble(250);
                robot.setIntak1BallOff();
            }

            if (robot.isIntake3Balls()) {
                gamepad1.rumble(500);
                robot.setIntak3BallsOff();
            }*/

            //change gamepad 2 light bar when sped up all the way
            /*if(robot.getLauncher().getLauncherVelocity() == robot.getLauncher().getLauncherTargetVelocity() && robot.getLauncher().getLauncherTargetVelocity() != 0.0){
                gamepad2.setLedColor(255, 255, 0, 20);
            }*/

            //rumble gamepad 2 when empty
            /*if(robot.getIndexer().artifactColorArray == new ArtifactColor[] {ArtifactColor.NONE, ArtifactColor.NONE, ArtifactColor.NONE} && robot.getLauncher().getLauncherTargetVelocity() != 0.0){
                gamepad2.rumble(0.25, 0, 10);
                gamepad2.rumble(0, 0.25, 10);
            }*/

            //telemetry.addData("launcher power:", robot.getLauncher().getLaunchPower());
            telemetry.addData("launcher velocity:", robot.getLauncher().getLauncherVelocity());
            telemetry.addData("launcher velocity2:", robot.getLauncher().getLauncherVelocity2());
            telemetry.addData("color:", robot.getIndexer().artifactColorArray[0]);
            telemetry.addData("color:", robot.getIndexer().artifactColorArray[1]);
            telemetry.addData("color:", robot.getIndexer().artifactColorArray[2]);
            //RobotLog.d("launcher velocity: %f",
                    //robot.getLauncher().getLauncherVelocity());

            // Refresh the indicator lights
//            robot.getHud().setBalls(robot.getIndexer().artifactColorArray[0], robot.getIndexer().artifactColorArray[1],robot.getIndexer().artifactColorArray[2]);
//            if (llLastIsValid == true)
//            {
//                // RobotLog.d("Aim PID X: %f", xAngle);
//                if (xAngle < Launcher.aimErrorTolerance)
//                {
//                    robot.getHud().setAimIndicator(true);
//                }
//            }
//            else {
//                robot.getHud().setAimIndicator(false);
//            }
//            robot.getHud().UpdateBallUI();

            // TODO Add timing Log at end of loop
//            RobotLog.d("c0: %s c1: %s c2: %s",
//                    robot.getIndexer().artifactColorArray[0],
//                    robot.getIndexer().artifactColorArray[1],
//                    robot.getIndexer().artifactColorArray[2]);

            // Update turret angle so that it always point to the goal
            robot.updateTurretAngle();

            telemetry.update();
        }
    }
}
