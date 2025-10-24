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

        initializeSubsystems();
    }

    public void initializeSubsystems() {

        // Create an instance of every subsystem in the Robot class
        this.driveBase = new DriveBase(this.hardwareMap, this.telemetry);
        this.indexer = new Indexer(this.hardwareMap, this.telemetry);
        this.launcher = new Launcher(this.hardwareMap, this.telemetry);
        this.intake = new Intake(this.hardwareMap, this.telemetry);

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

    public void runIndexer(boolean launchGreen, boolean launchPurple) {
        ArtifactColor[] ballColors = indexer.getBallColors();
        int currentIntakePosition = indexer.getIndexerSlotPosition() == 3 ? 0 : indexer.getIndexerSlotPosition();
        int currentOuttakePosition = indexer.getIndexerSlotPosition() - 1;
        if (intake.getIntakeMotorPower() > 0.01 && timeSinceIndex.milliseconds() >= 500) {
            if (ballColors[currentIntakePosition] == ArtifactColor.GREEN || ballColors[currentIntakePosition] == ArtifactColor.PURPLE) {
                if (ballColors[0] == ArtifactColor.NONE) {
                    indexer.rotateToFirstPosition();
                    timeSinceIndex.reset();
                }
                else if (ballColors[1] == ArtifactColor.NONE) {
                    indexer.rotateToSecondPosition();
                    timeSinceIndex.reset();
                }
                else if (ballColors[2] == ArtifactColor.NONE){
                    indexer.rotateToThirdPosition();
                    timeSinceIndex.reset();
                }
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

        if (queuedLaunches.element() == ArtifactColor.GREEN) {
            if (ballColors[currentOuttakePosition] != ArtifactColor.GREEN && timeSinceIndex.milliseconds() > 500) {
                if (ballColors[0] == ArtifactColor.GREEN) {
                    indexer.rotateToFirstPosition();
                    timeSinceIndex.reset();
                } else if (ballColors[1] == ArtifactColor.GREEN) {
                    indexer.rotateToSecondPosition();
                    timeSinceIndex.reset();
                } else if (ballColors[2] == ArtifactColor.GREEN) {
                    indexer.rotateToThirdPosition();
                    timeSinceIndex.reset();
                }
            }
            else if (timeSinceIndex.milliseconds() > 500) {
                launcher.kickBall();
            }
        }

        if (queuedLaunches.element() == ArtifactColor.PURPLE) {
            if (ballColors[currentOuttakePosition] != ArtifactColor.PURPLE && timeSinceIndex.milliseconds() > 500) {
                if (ballColors[0] == ArtifactColor.PURPLE) {
                    indexer.rotateToFirstPosition();
                    timeSinceIndex.reset();
                } else if (ballColors[1] == ArtifactColor.PURPLE) {
                    indexer.rotateToSecondPosition();
                    timeSinceIndex.reset();
                } else if (ballColors[2] == ArtifactColor.PURPLE) {
                    indexer.rotateToThirdPosition();
                    timeSinceIndex.reset();
                }
            }
            else if (timeSinceIndex.milliseconds() > 500) {
                launcher.kickBall();
                timeSinceIndex.reset();
            }
        }

        if (queuedLaunches.element() != null) {
            if (timeSinceIndex.milliseconds() > 500 && launcher.getKickerPosition() == launcher.POSITION_KICKER_SERVO_KICK_BALL) {
                launcher.resetKicker();
                timeSinceIndex.reset();
            }
        }
    }
}
