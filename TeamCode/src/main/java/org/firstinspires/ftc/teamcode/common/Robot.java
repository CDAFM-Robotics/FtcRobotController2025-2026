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

import java.util.Arrays;
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

    //indicators for driver
    public Boolean intake3Balls = false; //Picked up all three balls
    public Boolean intake1Ball = false; //Picked up one ball

    private Queue<ArtifactColor> queuedLaunches = new ArrayBlockingQueue<>(3);
    private ArtifactColor ballColor = ArtifactColor.NONE;

    private HardwareMap hardwareMap;
    private Telemetry telemetry;

    public final int WAIT_TIME_KICKER = 100; // 75 didn't shoot once  // was 175 // was 275 (SNGLE RB WHEEL)

    public Robot(HardwareMap hardwareMap, Telemetry telemetry) {
        // Create an instance of the hardware map and telemetry in the Robot class
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        //timeSinceIndex.startTime();
        timeSinceKick.startTime();
        timeSinceKickReset.startTime();

        initializeSubsystems();
    }

    public void initializeSubsystems() {

        // Create an instance of every subsystem in the Robot class
        this.driveBase = new DriveBase(this.hardwareMap, this.telemetry);
        this.indexer = new Indexer(this.hardwareMap, this.telemetry);
        this.launcher = new Launcher(this.hardwareMap, this.telemetry);
        this.intake = new Intake(this.hardwareMap, this.telemetry);
        //this.hud = new Hud(this.hardwareMap, this.telemetry);
        telemetry.update();

    }

    public enum AutoIntakeStates {
        INIT,
        RESET_KICKER,
        WAIT_KICKER,
        TURN_EMPTY_SLOT_TO_INTAKE,
        WAIT_FOR_BALL
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
        //Check if the front two slot are empty if yes, no turning


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

        //read the current robot heading

        //calculate the relative angle of the turret to the robot

    }

}
