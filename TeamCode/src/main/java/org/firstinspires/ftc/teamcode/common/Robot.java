package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.subsystems.DriveBase;
import org.firstinspires.ftc.teamcode.common.subsystems.Hud;
import org.firstinspires.ftc.teamcode.common.subsystems.Indexer;
import org.firstinspires.ftc.teamcode.common.subsystems.Intake;
import org.firstinspires.ftc.teamcode.common.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.common.util.ArtifactColor;

import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class Robot {

    private DriveBase driveBase;
    private Indexer indexer;
    private Launcher launcher;
    private Intake intake;
    private Hud hud;

    //private ElapsedTime timeSinceIndex = new ElapsedTime();
    private ElapsedTime timeSinceKick = new ElapsedTime();
    private ElapsedTime timeSinceKickReset  = new ElapsedTime();
    private ElapsedTime reverseIntakeTimer  = new ElapsedTime();

    //indicators for driver
    public boolean intake3Balls = false; //Picked up all three balls
    public boolean intake1Ball = false; //Picked up one ball
    private boolean safeToStop = true; //if kicker is down

    private double shootingDistance = 0;
    private ArtifactColor ballColor = ArtifactColor.NONE;

    private HardwareMap hardwareMap;
    private Telemetry telemetry;

    public final int WAIT_TIME_KICKER = 200; // 75 didn't shoot once  // was 175 // was 275 (SNGLE RB WHEEL)

    public Robot(HardwareMap hardwareMap, Telemetry telemetry) {
        // Create an instance of the hardware map and telemetry in the Robot class
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        timeSinceKick.startTime();
        timeSinceKickReset.startTime();
        reverseIntakeTimer.startTime();

        initializeSubsystems();
    }

    public void initializeSubsystems() {
        // Create an instance of every subsystem in the Robot class
        this.driveBase = new DriveBase(this.hardwareMap, this.telemetry);
        this.indexer = new Indexer(this.hardwareMap, this.telemetry);
        this.launcher = new Launcher(this.hardwareMap, this.telemetry);
        this.intake = new Intake(this.hardwareMap, this.telemetry);
        //this.hud = new Hud(this.hardwareMap, this.telemetry);
    }

    public enum AutoIntakeStates {
        INIT,
        TURN_EMPTY_SLOT_TO_INTAKE,
        WAIT_FOR_BALL,
        POSITION_FOR_OUTTAKE,
        READY_TO_SHOOT
    }

    public enum IndexerResetStates {
        INIT,
        CHECK_INTAKE,
        CHECK_0TO1,
        CHECK_1TO2,
        CHECK_2TO1,
        CHECK_LAST
    }

    public enum LaunchBallStates {
        IDLE,
        INIT,
        TURN_TO_LAUNCH,
        KICK_BALL,
        RESET_KICKER,
        UPDATE_INDEXER
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
    public enum LLPipelines {
        PURPLE,
        YELLOW,
        BLUE,
        APRIL_TAG,
        MOTIF,
        RED_GOAL,
        BLUE_GOAL,
        OBELISK
    }

    IndexerResetStates indexerResetState = IndexerResetStates.INIT;
    LaunchBallStates launchState = LaunchBallStates.IDLE;
    AutoIntakeStates autoIntakeState = AutoIntakeStates.INIT;

    public DriveBase getDriveBase() {
        return driveBase;
    }

    public Indexer getIndexer() {
        return indexer;
    }

    public Launcher getLauncher() {
        return launcher;
    }

    public Intake getIntake() {
        return intake;
    }

    public Hud getHud() {
        return hud;
    }

     // Auto-Indexing for intake
     public void intakeWithIndexerTurn(){
         telemetry.addData("intakeWithIndexerTurn", autoIntakeState);

         switch (autoIntakeState) {
             case INIT:
                 if (indexer.checkEmptySlot()) {
                     telemetry.addLine("Robot: found empty slot");
                     RobotLog.d("RRobot: found empty slot");
                     autoIntakeState = AutoIntakeStates.TURN_EMPTY_SLOT_TO_INTAKE;
                 } else {
                     //No empty slot
                     // - update color double check
                     // This line is removed to save time.
                     indexer.updateColorAllSlots();
                     intake3Balls = true;
                     autoIntakeState = AutoIntakeStates.POSITION_FOR_OUTTAKE;
                     break;
                 }
             case TURN_EMPTY_SLOT_TO_INTAKE:
                 indexer.turnEmptySlotToIntake();
                 autoIntakeState = AutoIntakeStates.WAIT_FOR_BALL;
                 break;
             case WAIT_FOR_BALL:
                 telemetry.addLine("Robot: WAIT_FOR_BALL");
                 if (indexer.indexerFinishedTurning()) {
                     telemetry.addLine("Robot: indexerFinishedTurning");
                     if (indexer.isBallAtIntake()) {
                         telemetry.addLine("Robot: isBallAtIntake");
                         intake1Ball = true;
                         indexer.updateBallColorAtIntake(indexer.getIndexerPosition());
                         autoIntakeState = AutoIntakeStates.INIT;
                         break;
                     }
                 }
                 break;
             case POSITION_FOR_OUTTAKE:
                 indexer.positionForOuttake();
                 autoIntakeState = AutoIntakeStates.READY_TO_SHOOT;
                 break;
             case READY_TO_SHOOT:
                 break;
             default:
                 throw new IllegalStateException("intakeWithIndexerTurn Unexpected value: " + autoIntakeState);
         }
     }

    public void startLaunchAGreenBall(){
        if(launcher.isLauncherActive()) {
            //telemetry.addLine("stratLaunchAGreenBall");
            ballColor = ArtifactColor.GREEN;
            launchState = LaunchBallStates.INIT;
        }
        if (launcher.getKickerPosition() == launcher.POSITION_KICKER_SERVO_KICK_BALL) {
            launcher.resetKicker();
            timeSinceKickReset.reset();
        }
    }

    public void startLaunchAPurpleBall(){
        if(launcher.isLauncherActive()) {
            //telemetry.addLine("stratLaunchAPupleBall");
            ballColor = ArtifactColor.PURPLE;
            launchState = LaunchBallStates.INIT;
        }
        if (launcher.getKickerPosition() == launcher.POSITION_KICKER_SERVO_KICK_BALL) {
            launcher.resetKicker();
            timeSinceKickReset.reset();
        }
    }

    public void launchAColorBall(){

            //telemetry.addData("launchAColorBall", ballColor);
            //telemetry.addData("color:", indexer.artifactColorArray[0]);
            //telemetry.addData("color:", indexer.artifactColorArray[1]);
            //telemetry.addData("color:", indexer.artifactColorArray[2]);

    }

    public void shootAllBalls() {
        telemetry.addLine("shootAllBalls");
        if (indexer.atIntake()) {
            indexer.updateColorAllSlots();
        }
        telemetry.addData("color:", indexer.artifactColorArray[0]);
        telemetry.addData("color:", indexer.artifactColorArray[1]);
        telemetry.addData("color:", indexer.artifactColorArray[2]);

        // check to see if flywheel motors are running
        if(launcher.isLauncherActive()) {
            //RobotLog.d("shootAllBalls");
            //RobotLog.d("0 color: %s", indexer.artifactColorArray[0]);
            //RobotLog.d("1 color: %s", indexer.artifactColorArray[1]);
            //RobotLog.d("2 color: %s", indexer.artifactColorArray[2]);

            if (indexer.findABall()) {
                switch (launchState) {
                    case INIT:
                        telemetry.addLine("shootAllBalls: INIT");
                        //RobotLog.d("shootAllBalls: INIT");
                        launchState = LaunchBallStates.TURN_TO_LAUNCH;
                        break;
                    case TURN_TO_LAUNCH:
                        telemetry.addLine("shootAllBalls: TURN_TO_LAUNCH");
                        RobotLog.d("shootAllBalls: TURN_TO_LAUNCH");
                        indexer.moveToOuttake();
                        launchState = LaunchBallStates.KICK_BALL;
                        break;
                    case KICK_BALL:
                        telemetry.addLine("shootAllBalls: KICK_BALL");
                        RobotLog.d("shootAllBalls: KICK_BALL");
                        if (indexer.indexerFinishedTurning()) {
                            safeToStop = false;
                            launcher.kickBall();
                            timeSinceKick.reset();
                            launchState = LaunchBallStates.RESET_KICKER;
                            break;
                        } else {
                            break;
                        }
                    case RESET_KICKER:
                        telemetry.addLine("shootAllBalls: RESET_KICKER");
                        RobotLog.d("shootAllBalls: RESET_KICKER");
                        if (timeSinceKick.milliseconds() > WAIT_TIME_KICKER) {
                            launcher.resetKicker();
                            timeSinceKickReset.reset();
                            launchState = LaunchBallStates.UPDATE_INDEXER;
                            break;
                        } else {
                            break;
                        }
                    case UPDATE_INDEXER:
                        telemetry.addLine("shootAllBalls: UPDATE_INDEXER");
                        RobotLog.d("shootAllBalls: UPDATE_INDEXER");
                        indexer.updateAfterShoot();
                        if (timeSinceKickReset.milliseconds() > WAIT_TIME_KICKER) {
                            safeToStop = true;
                        }
                        launchState = LaunchBallStates.INIT;
                        break;
                    default:
                        RobotLog.d("shootAllBalls Unexpected");
                        throw new IllegalStateException("shootAllBalls Unexpected value: " + launchState);
                }
            }
            else {
                //robot think there's no more balls. verify
                // to update color need to position for intake first
                // This may create a racing condition
                indexer.positionForIntake();
            }
        }

    }

    public void resetIndexerColorStart(){
        //telemetry.addData("resetIndexerColorStart: start state", indexerResetState);
        //RobotLog.d("resetIndexerColorStart: start state: %s", indexerResetState);
        indexerResetState = IndexerResetStates.CHECK_INTAKE;
        //telemetry.addData("resetIndexerColorStart: done state", indexerResetState);
        //RobotLog.d("resetIndexerColorStart: done state %s", indexerResetState);
    }

    public void resetIndexer() {
    }

    public void robotStopIntake(){
        // This line cause intake color mistakes. To be investigated
        // indexer.updateBallColors();
        intake.stopIntake();
    }

    public Boolean isIntake3Balls () {
        return intake3Balls;
    }

    public void setIntak3BallsOff () {
        intake3Balls = false;
    }

    public Boolean isIntake1Ball () {
        return intake1Ball;
    }

    public void setIntak1BallOff () {
        intake1Ball = false;
    }

    public void updateColorAllSlots() {
        indexer.updateColorAllSlots();
    }

    //updating the turret every loop
    public void updateTurretAngle(){
        //read the current pose
        double robotX = driveBase.getPinPointPosX();
        double robotY = driveBase.getPinPointPosY();
        //read the current robot heading
        double pinPointHeading = driveBase.getPinPointHeading();
        pinPointHeading = normalizeAngle(pinPointHeading);
        double robotHeading = Math.toDegrees(pinPointHeading);

        //calculate the relative angle of the turret to the robot
        // coordinates of the blue goal
        double blueGoalX = -64;
        double blueGoalY = -64;

        // calculate vector to blue goal
        double deltaX = blueGoalX - robotX;
        double deltaY = blueGoalY - robotY;
        shootingDistance = Math.sqrt(Math.pow(deltaX, 2) + Math.pow(deltaY, 2));

        //calculates the angle in radians between the positive x-axis and a point
        double absoluteAngleRadians = Math.atan2(deltaY, deltaX);
        double absoluteAngleDegree = Math.toDegrees(absoluteAngleRadians);

        double relativeAngle = absoluteAngleDegree - robotHeading;

        relativeAngle = normalizeAngle(relativeAngle);

//        telemetry.addData("deltaX:", deltaX);
//        telemetry.addData("deltaY:", deltaY);
//        telemetry.addData("robotX:", robotX);
//        telemetry.addData("robotY:", robotY);
//        telemetry.addData("absoluteAngleRadians:", absoluteAngleRadians);
//        telemetry.addData("absoluteAngleDegree:", absoluteAngleDegree);
//        telemetry.addData("pinPointHeading:", pinPointHeading);
//        telemetry.addData("relativeAngle:", relativeAngle);
//        telemetry.addData("relativeAngle",relativeAngle);

        launcher.setTurretRelativeAngle(relativeAngle);
    }

    public double normalizeAngle (double angle) {
        while (angle > 180.0) {
            angle -= 360.0;
        }
        while (angle < -180.0) {
            angle += 360.0;
        }
        return angle;
    }

    public boolean isSafeToStop() {
        return safeToStop;
    }
}
