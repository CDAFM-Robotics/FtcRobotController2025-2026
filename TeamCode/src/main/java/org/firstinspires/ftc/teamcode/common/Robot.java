package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.subsystems.DriveBase;
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

    private ElapsedTime timeSinceIndex = new ElapsedTime();

    private Queue<ArtifactColor> queuedLaunches = new ArrayBlockingQueue<>(3);

    private HardwareMap hardwareMap;
    private Telemetry telemetry;

    public Robot(HardwareMap hardwareMap, Telemetry telemetry) {
        // Create an instance of the hardware map and telemetry in the Robot class
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;
        timeSinceIndex.startTime();

        initializeSubsystems();
    }

    public void initializeSubsystems() {

        // Create an instance of every subsystem in the Robot class
        this.driveBase = new DriveBase(this.hardwareMap, this.telemetry);
        this.indexer = new Indexer(this.hardwareMap, this.telemetry);
        this.launcher = new Launcher(this.hardwareMap, this.telemetry);
        this.intake = new Intake(this.hardwareMap, this.telemetry);
        telemetry.update();

    }

    public enum ArtifactColor {
        PURPLE,
        GREEN,
        NONE,
        UNKNOWN
    }

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
            if ( timeSinceIndex.milliseconds() > 800 ) {
                telemetry.addLine("Robot:updateBallColor");
                indexer.updateBallColors();
            }
        }
    }
}
