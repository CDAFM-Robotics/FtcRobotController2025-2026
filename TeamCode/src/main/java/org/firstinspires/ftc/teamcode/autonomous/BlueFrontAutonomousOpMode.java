package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.autonomous.actions.AutonomousActionBuilder;
import org.firstinspires.ftc.teamcode.common.Robot;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name = "Blue Front Autonomous", group = "0competition")
public class BlueFrontAutonomousOpMode extends LinearOpMode {

    Action[] trajectories;

    AutonomousActionBuilder autonomousActionBuilder;


    @Override
    public void runOpMode() throws InterruptedException {
        MecanumDrive md = new MecanumDrive(hardwareMap, new Pose2d(-50.5, -50.5, Math.toRadians(143)));
        Robot robot = new Robot(hardwareMap, telemetry);
        robot.getLauncher().setLimelightPipeline(Robot.LLPipelines.OBELISK.ordinal());
        autonomousActionBuilder = new AutonomousActionBuilder(md, robot);

        trajectories = autonomousActionBuilder.getBlueCloseTrajectories();

        Robot.ArtifactColor[] motif = null;

        telemetry.setMsTransmissionInterval(50);

        int selectedRow = 0;
        double delay = 0;

        boolean firstMark = true;
        boolean secondMark = true;
        boolean thirdMark = false;

        Gamepad currentGamepad1 = new Gamepad();
        Gamepad previousGamepad1 = new Gamepad();
        Gamepad currentGamepad2 = new Gamepad();
        Gamepad previousGamepad2 = new Gamepad();

        while(opModeInInit()) {
            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);
            currentGamepad1.copy(gamepad1);
            currentGamepad2.copy(gamepad2);

            if ((currentGamepad1.dpad_down && !previousGamepad1.dpad_down) || (currentGamepad2.dpad_down && !previousGamepad2.dpad_down)) {
                selectedRow++;
                if (selectedRow > 3) {
                    selectedRow = 0;
                }
            }
            if ((currentGamepad1.dpad_up && !previousGamepad1.dpad_up) || (currentGamepad2.dpad_up && !previousGamepad2.dpad_up)) {
                selectedRow--;
                if (selectedRow < 0) {
                    selectedRow = 3;
                }
            }

            if ((currentGamepad1.dpad_right && !previousGamepad1.dpad_right) || (currentGamepad2.dpad_right && !previousGamepad2.dpad_right)) {
                if (selectedRow == 0) {
                    delay += 0.5;
                    if (delay > 30) {
                        delay = 30;
                    }
                }
                else if (selectedRow == 1) {
                    thirdMark = !thirdMark;
                }
                else if (selectedRow == 2) {
                    secondMark = !secondMark;
                }
                else if (selectedRow == 3) {
                    firstMark = !firstMark;
                }
            }

            if ((currentGamepad1.dpad_left && !previousGamepad1.dpad_left) || (currentGamepad2.dpad_left && !previousGamepad2.dpad_left)) {
                if (selectedRow == 0) {
                    delay -= 0.5;
                    if (delay < 0) {
                        delay = 0;
                    }
                }
                else if (selectedRow == 1) {
                    thirdMark = !thirdMark;
                }
                else if (selectedRow == 2) {
                    secondMark = !secondMark;
                }
                else if (selectedRow == 3) {
                    firstMark = !firstMark;
                }
            }

            if (selectedRow == 0) {
                telemetry.addData("> Delay", delay);
            }
            else {
                telemetry.addData("  Delay", delay);
            }

            if (selectedRow == 1) {
                telemetry.addData("> Pickup First Mark", firstMark);
            }
            else {
                telemetry.addData("  Pickup First Mark", firstMark);
            }

            if (selectedRow == 2) {
                telemetry.addData("> Pickup Second Mark", secondMark);
            }
            else {
                telemetry.addData("  Pickup Second Mark", secondMark);
            }

            if (selectedRow == 3) {
                telemetry.addData("> Pickup Third Mark", thirdMark);
            }
            else {
                telemetry.addData("  Pickup Third Mark", thirdMark);
            }


            telemetry.update();


        }

        waitForStart();

        sleep((long) (delay * 1000));

        //start always with PGP

        // Go to the Launch Pose and april tag

        Actions.runBlocking(new ParallelAction(
            trajectories[0]
        ));

        for(int i = 0; i < 1000; i++) {
            motif = robot.getLauncher().getMotifPattern();

            if (motif == null) {
                telemetry.addData("Motif Pattern", "Not Detected");
            }
            else {
                telemetry.addData("Motif Pattern", motif[0].toString() + ", " + motif[1].toString() + ", " + motif[2].toString());
            }
            telemetry.update();
        }

        if (motif == null) {
            motif = new Robot.ArtifactColor[] {Robot.ArtifactColor.PURPLE, Robot.ArtifactColor.PURPLE, Robot.ArtifactColor.GREEN};
        }

        if (motif[0] != Robot.ArtifactColor.GREEN) {
            Actions.runBlocking(autonomousActionBuilder.getIndexAction(1));
        }

        Actions.runBlocking(new ParallelAction(
            trajectories[1],
            autonomousActionBuilder.getSpinLauncherClose()
        ));

        launchInMotifOrder(motif, 0);

        if (firstMark) {

            // Pickup first mark

            Actions.runBlocking(new SequentialAction(
                new ParallelAction(
                    trajectories[2],
                    autonomousActionBuilder.getIndexAction(0),
                    new SequentialAction(
                        new SleepAction(0),
                        autonomousActionBuilder.getStartIntake(),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(4),
                        autonomousActionBuilder.getIndexAction(1),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(1.5),
                        autonomousActionBuilder.getIndexAction(2),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(1.5),
                        autonomousActionBuilder.getStopIntake(),
                        autonomousActionBuilder.getSpinLauncherClose()
                    )
                )
            ));

            //Actions.runBlocking(trajectories[1]);

            launchInMotifOrder(motif, 1);
        }

        if (secondMark) {

            // Pickup second mark

            Actions.runBlocking(new SequentialAction(
                new ParallelAction(
                    trajectories[3],
                    autonomousActionBuilder.getIndexAction(0),
                    new SequentialAction(
                        new SleepAction(0),
                        autonomousActionBuilder.getStartIntake(),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(4),
                        autonomousActionBuilder.getIndexAction(1),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(1.5),
                        autonomousActionBuilder.getIndexAction(2),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(1.5),
                        autonomousActionBuilder.getStopIntake(),
                        autonomousActionBuilder.getSpinLauncherClose()
                    )
                )
            ));

            //Actions.runBlocking(trajectories[1]);

            launchInMotifOrder(motif, 0);
        }

        // LEave

        Actions.runBlocking(new ParallelAction(
            trajectories[4],
            autonomousActionBuilder.getStopLauncher()
        ));
    }

    public void launchInMotifOrder(Robot.ArtifactColor[] motifPattern, int greenLocation) {
        Actions.runBlocking(motifPattern[0] == Robot.ArtifactColor.GREEN ? autonomousActionBuilder.getIndexAction(greenLocation) : autonomousActionBuilder.getIndexAction(greenLocation == 0 ? 1 : 0));
        Actions.runBlocking(autonomousActionBuilder.getKickBall());
        Actions.runBlocking(autonomousActionBuilder.getResetKicker());
        Actions.runBlocking(motifPattern[1] == Robot.ArtifactColor.GREEN ? autonomousActionBuilder.getIndexAction(greenLocation) : (motifPattern[0] == Robot.ArtifactColor.GREEN ? autonomousActionBuilder.getIndexAction(greenLocation == 0 ? 1 : 0) : autonomousActionBuilder.getIndexAction(greenLocation == 2 ? 1 : 2)));
        Actions.runBlocking(autonomousActionBuilder.getKickBall());
        Actions.runBlocking(autonomousActionBuilder.getResetKicker());
        Actions.runBlocking(motifPattern[2] == Robot.ArtifactColor.GREEN ? autonomousActionBuilder.getIndexAction(greenLocation) : autonomousActionBuilder.getIndexAction(greenLocation == 2 ? 1 : 2));
        Actions.runBlocking(autonomousActionBuilder.getKickBall());
        Actions.runBlocking(new ParallelAction(
            autonomousActionBuilder.getStopLauncher(),
            autonomousActionBuilder.getResetKicker()
        ));
    }
}
