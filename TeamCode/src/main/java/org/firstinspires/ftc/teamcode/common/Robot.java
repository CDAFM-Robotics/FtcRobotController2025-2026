package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.subsystems.DriveBase;
import org.firstinspires.ftc.teamcode.common.subsystems.Hud;
import org.firstinspires.ftc.teamcode.common.subsystems.Indexer;
import org.firstinspires.ftc.teamcode.common.subsystems.Intake;
import org.firstinspires.ftc.teamcode.common.subsystems.Launcher;

import java.util.Arrays;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

public class Robot {

    private DriveBase driveBase;
    private Indexer indexer;
    private Launcher launcher;
    private Intake intake;
    private Hud hud;

    private ElapsedTime timeSinceIndex = new ElapsedTime();
    private ElapsedTime timeSinceKick = new ElapsedTime();
    private ElapsedTime timeSinceKickReset  = new ElapsedTime();

    private Queue<ArtifactColor> queuedLaunches = new ArrayBlockingQueue<>(3);
    private ArtifactColor ballColor = ArtifactColor.NONE;

    private HardwareMap hardwareMap;
    private Telemetry telemetry;

    public Robot(HardwareMap hardwareMap, Telemetry telemetry) {
        // Create an instance of the hardware map and telemetry in the Robot class
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        timeSinceIndex.startTime();
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
        this.hud = new Hud(this.hardwareMap, this.telemetry);
        telemetry.update();

    }

    public enum ArtifactColor {
        PURPLE,
        GREEN,
        NONE,
        UNKNOWN
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

    IndexerResetStates indexerResetState = IndexerResetStates.INIT;
    LaunchBallStates launchState = LaunchBallStates.IDLE;

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

    public void runIndexer(boolean launchGreen, boolean launchPurple, boolean launchAll) {
        ArtifactColor[] ballColors = indexer.getBallColors();
        int currentIntakePosition = indexer.getIndexerSlotPosition() + 1;
        int currentOuttakePosition = indexer.getIndexerSlotPosition();
        if (currentIntakePosition > 2) {
            currentIntakePosition = 0;
        }

        // Auto-Indexing for intake
        /*if (intake.getIntakeMotorPower() > 0.01 && timeSinceIndex.milliseconds() >= 500) {
            if (ballColors[currentIntakePosition] == ArtifactColor.GREEN || ballColors[currentIntakePosition] == ArtifactColor.PURPLE) {
                if (ballColors[0] == ArtifactColor.NONE) {
                    indexer.rotateToOnePosition();
                    timeSinceIndex.reset();
                }
                else if (ballColors[1] == ArtifactColor.NONE) {
                    indexer.rotateToTwoPosition();
                    timeSinceIndex.reset();
                }
                else if (ballColors[2] == ArtifactColor.NONE){
                    indexer.rotateToZeroPosition();
                    timeSinceIndex.reset();
                }
            }
        }*/

        if (launchAll) {
            if (ballColors[0] != null) {
                queuedLaunches.add(ballColors[0]);
            }
            if (ballColors[1] != null) {
                queuedLaunches.add(ballColors[1]);
            }
            if (ballColors[2] != null) {
                queuedLaunches.add(ballColors[2]);
            }
        }

        if (launchGreen) {
            if (Arrays.stream(ballColors).anyMatch(artifactColor -> artifactColor == ArtifactColor.GREEN)) {
                queuedLaunches.add(ArtifactColor.GREEN);
            }
        }

        if (launchPurple) {
            if (Arrays.stream(ballColors).anyMatch(artifactColor -> artifactColor == ArtifactColor.PURPLE)) {
                queuedLaunches.add(ArtifactColor.PURPLE);
            }
        }

        if (!queuedLaunches.isEmpty()) {
            if (queuedLaunches.element() == ArtifactColor.GREEN) {
                if (ballColors[currentOuttakePosition] != ArtifactColor.GREEN && timeSinceIndex.milliseconds() > 500) {
                    if (ballColors[0] == ArtifactColor.GREEN) {
                        indexer.rotateToZeroPosition();
                        timeSinceIndex.reset();
                    }
                    else if (ballColors[1] == ArtifactColor.GREEN) {
                        indexer.rotateToOnePosition();
                        timeSinceIndex.reset();
                    }
                    else if (ballColors[2] == ArtifactColor.GREEN) {
                        indexer.rotateToTwoPosition();
                        timeSinceIndex.reset();
                    }
                } else if (timeSinceIndex.milliseconds() > 500) {
                    launcher.kickBall();
                }
            }

            if (queuedLaunches.element() == ArtifactColor.PURPLE) {
                if (ballColors[currentOuttakePosition] != ArtifactColor.PURPLE && timeSinceIndex.milliseconds() > 500) {
                    if (ballColors[0] == ArtifactColor.PURPLE) {
                        indexer.rotateToZeroPosition();
                        timeSinceIndex.reset();
                    }
                    else if (ballColors[1] == ArtifactColor.PURPLE) {
                        indexer.rotateToOnePosition();
                        timeSinceIndex.reset();
                    }
                    else if (ballColors[2] == ArtifactColor.PURPLE) {
                        indexer.rotateToTwoPosition();
                        timeSinceIndex.reset();
                    }
                } else if (timeSinceIndex.milliseconds() > 500) {
                    launcher.kickBall();
                    timeSinceIndex.reset();
                }
            }


            if (timeSinceIndex.milliseconds() > 500 && launcher.getKickerPosition() == launcher.POSITION_KICKER_SERVO_KICK_BALL) {
                launcher.resetKicker();
                timeSinceIndex.reset();
            }
        }

        telemetry.addData("Ball Colors", ballColors[0].toString() + ", "  + ballColors[1].toString() + ", " + ballColors[2].toString());
        if (!queuedLaunches.isEmpty()) {
            telemetry.addData("Queue", queuedLaunches.element());
        }
        else {
            telemetry.addData("Queue", "Empty");
        }
        telemetry.addData("Intake Position", currentIntakePosition);
        telemetry.addData("outtake Position", currentOuttakePosition);
    }

     // Auto-Indexing for intake
     public void intakeWithIndexerTurn(){
         telemetry.addLine("intakeWithIndexerTurn");
        if (indexer.checkEmptySlot()){
            telemetry.addLine("Robot: found empty slot");
            if(indexer.turnEmptySlotToIntake() ) {
                timeSinceIndex.reset();
            }
            if ( timeSinceIndex.milliseconds() > 550 ) {
                telemetry.addLine("Robot:updateBallColor");
                indexer.updateBallColors();
            }
        }
    }

    public void stratLaunchAGreenBall(){
        if(launcher.isLauncherActive()) {
            telemetry.addLine("stratLaunchAGreenBall");
            ballColor = ArtifactColor.GREEN;
            launchState = LaunchBallStates.INIT;
        }
    }

    public void stratLaunchAPurpleBall(){
        if(launcher.isLauncherActive()) {
            telemetry.addLine("stratLaunchAPupleBall");
            ballColor = ArtifactColor.PURPLE;
            launchState = LaunchBallStates.INIT;
        }
    }

    public void launchAColorBall(){

            telemetry.addData("launchAColorBall", ballColor);
            telemetry.addData("color:", indexer.artifactColorArray[0]);
            telemetry.addData("color:", indexer.artifactColorArray[1]);
            telemetry.addData("color:", indexer.artifactColorArray[2]);

            switch (launchState) {
                case IDLE:
                    telemetry.addLine("launchAColorBall: IDLE");
                    break;
                case INIT:
                    telemetry.addLine("launchAColorBall: INIT");
                    if (indexer.haveABall(ballColor)) {
                        if (timeSinceKickReset.milliseconds() > 500) {
                            //If yes, turn it to launcher
                            launchState = LaunchBallStates.TURN_TO_LAUNCH;
                        } else {
                            break;
                        }
                    } else {
                        //There is no ball in the color
                        launchState = LaunchBallStates.IDLE;
                        break;
                    }
                case TURN_TO_LAUNCH:
                    telemetry.addLine("launchAColorBall: TURN_TO_LAUNCH");
                    if (indexer.moveToOuttake()) {
                        timeSinceIndex.reset();
                        launchState = LaunchBallStates.KICK_BALL;
                        break;
                    } else {
                        launchState = LaunchBallStates.KICK_BALL;
                    }
                case KICK_BALL:
                    telemetry.addLine("launchAColorBall: KICK_BALL");
                    if (timeSinceIndex.milliseconds() > 800) {
                        launcher.kickBall();
                        timeSinceKick.reset();
                        launchState = LaunchBallStates.RESET_KICKER;
                        break;
                    } else {
                        break;
                    }
                case RESET_KICKER:
                    telemetry.addLine("launchAColorBall: RESET_KICKER");
                    if (timeSinceKick.milliseconds() > 500) {
                        launcher.resetKicker();
                        timeSinceKickReset.reset();
                        launchState = LaunchBallStates.UPDATE_INDEXER;
                    } else {
                        break;
                    }
                case UPDATE_INDEXER:
                    telemetry.addLine("launchAColorBall: UPDATE_INDEXER");
                    indexer.updateAfterShoot();
                    launchState = LaunchBallStates.IDLE;
                    break;
                default:
                    throw new IllegalStateException("launchAColorBall Unexpected value: " + launchState);
            }
    }

    public void shootAllBalls() {
        if(launcher.isLauncherActive()) {
            telemetry.addLine("shootAllBalls");
            telemetry.addData("color:", indexer.artifactColorArray[0]);
            telemetry.addData("color:", indexer.artifactColorArray[1]);
            telemetry.addData("color:", indexer.artifactColorArray[2]);

            if (indexer.findABall()) {
                switch (launchState) {
                    case IDLE:
                        telemetry.addLine("shootAllBalls: IDLE");
                        launchState = LaunchBallStates.INIT;
                    case INIT:
                        telemetry.addLine("shootAllBalls: INIT");
                        if (timeSinceKickReset.milliseconds() > 500) {
                            //If yes, turn it to launcher
                            launchState = LaunchBallStates.TURN_TO_LAUNCH;
                        } else {
                            break;
                        }
                    case TURN_TO_LAUNCH:
                        telemetry.addLine("shootAllBalls: TURN_TO_LAUNCH");
                        if (indexer.moveToOuttake()) {
                            timeSinceIndex.reset();
                            launchState = LaunchBallStates.KICK_BALL;
                            break;
                        } else {
                            launchState = LaunchBallStates.KICK_BALL;
                        }
                    case KICK_BALL:
                        telemetry.addLine("shootAllBalls: KICK_BALL");
                        if (timeSinceIndex.milliseconds() > 500) {
                            launcher.kickBall();
                            timeSinceKick.reset();
                            launchState = LaunchBallStates.RESET_KICKER;
                            break;
                        } else {
                            break;
                        }
                    case RESET_KICKER:
                        telemetry.addLine("shootAllBalls: RESET_KICKER");
                        if (timeSinceKick.milliseconds() > 400) {
                            launcher.resetKicker();
                            timeSinceKickReset.reset();
                            launchState = LaunchBallStates.UPDATE_INDEXER;
                        } else {
                            break;
                        }
                    case UPDATE_INDEXER:
                        telemetry.addLine("shootAllBalls: UPDATE_INDEXER");
                        indexer.updateAfterShoot();
                        launchState = LaunchBallStates.IDLE;
                        break;
                    default:
                        throw new IllegalStateException("shootAllBalls Unexpected value: " + launchState);
                }
            }
        }
    }

    public void resetIndexerColorStart(){
        indexerResetState = IndexerResetStates.CHECK_INTAKE;
    }



    public void resetIndexer() {
        telemetry.addData("resetIndexer: state", indexerResetState);
        switch (indexerResetState){
            case INIT:
                break;
            case CHECK_INTAKE:
                if (launcher.getKickerPosition() == launcher.POSITION_KICKER_SERVO_INIT && timeSinceKickReset.milliseconds() > 500) {
                    indexer.updateBallColors();
                    double position = indexer.getIndexerPosition();
                    if (position == indexer.POSITION_INDEXER_SERVO_SLOT_ZERO_INTAKE) {
                        indexer.rotateToPosition(indexer.POSITION_INDEXER_SERVO_SLOT_ONE_INTAKE);
                        indexerResetState = IndexerResetStates.CHECK_0TO1;
                    } else if (position == indexer.POSITION_INDEXER_SERVO_SLOT_ONE_INTAKE){
                        indexer.rotateToPosition(indexer.POSITION_INDEXER_SERVO_SLOT_TWO_INTAKE);
                        indexerResetState = IndexerResetStates.CHECK_1TO2;
                    } else{
                        indexer.rotateToPosition(indexer.POSITION_INDEXER_SERVO_SLOT_ONE_INTAKE);
                        indexerResetState = IndexerResetStates.CHECK_2TO1;
                    }
                    timeSinceIndex.reset();
                }
                break;
            case CHECK_0TO1:
                if (timeSinceIndex.milliseconds() > 550) {
                    indexer.updateBallColors();
                    indexer.rotateToPosition(indexer.POSITION_INDEXER_SERVO_SLOT_TWO_INTAKE);
                    timeSinceIndex.reset();
                    indexerResetState = IndexerResetStates.CHECK_LAST;
                }
                break;
            case CHECK_1TO2:
                if (timeSinceIndex.milliseconds() > 550) {
                    indexer.updateBallColors();
                    indexer.rotateToPosition(indexer.POSITION_INDEXER_SERVO_SLOT_ZERO_INTAKE);
                    timeSinceIndex.reset();
                    indexerResetState = IndexerResetStates.CHECK_LAST;
                }
                break;
            case CHECK_2TO1:
                if (timeSinceIndex.milliseconds() > 550) {
                    indexer.updateBallColors();
                    indexer.rotateToPosition(indexer.POSITION_INDEXER_SERVO_SLOT_ZERO_INTAKE);
                    timeSinceIndex.reset();
                    indexerResetState = IndexerResetStates.CHECK_LAST;
                }
                break;
            case CHECK_LAST:
                if (timeSinceIndex.milliseconds() > 800) {
                    indexer.updateBallColors();
                    indexerResetState = IndexerResetStates.INIT;
                }
                break;
            default:
                break;
        }
    }

    public void robotStopIntake(){
        // This line cause intake color mistakes. To be investigated
        // indexer.updateBallColors();
        intake.stopIntake();
    }
}
