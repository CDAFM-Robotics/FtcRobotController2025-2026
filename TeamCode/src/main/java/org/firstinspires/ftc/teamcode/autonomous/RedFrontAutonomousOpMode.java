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
import org.firstinspires.ftc.teamcode.common.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.common.util.ArtifactColor;
import org.firstinspires.ftc.teamcode.common.util.RunTimeoutAction;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

import java.util.ArrayList;
import java.util.Arrays;

@Autonomous(name = "Red Front Autonomous", group = "Competition")
public class RedFrontAutonomousOpMode extends LinearOpMode {

    Action[] trajectories;



    AutonomousActionBuilder autonomousActionBuilder;


    @Override
    public void runOpMode() throws InterruptedException {

        Robot robot = new Robot(hardwareMap, telemetry);
        robot.getLauncher().setLimelightPipeline(Robot.LLPipelines.OBELISK.ordinal());
        MecanumDrive md = new MecanumDrive(hardwareMap, new Pose2d(-50.5, 50.5, Math.toRadians(-143)));
        autonomousActionBuilder = new AutonomousActionBuilder(md, robot);

        trajectories = autonomousActionBuilder.getRedCloseTrajectories();




        ArtifactColor[] motif = null;

        telemetry.setMsTransmissionInterval(50);

        int selectedRow = 0;
        double delay = 0;
        int play = 1;

        ArrayList<String> names = new ArrayList<>();
        names.add(0, "First Mark");
        names.add(1, "First And Second Mark");
        names.add(2, "Second Mark And Gate And First Mark");
        names.add(3, "12 ball, other corner pocket");

        boolean secondMarkGate = false;
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
                if (selectedRow > 5) {
                    selectedRow = 0;
                }
            }
            if ((currentGamepad1.dpad_up && !previousGamepad1.dpad_up) || (currentGamepad2.dpad_up && !previousGamepad2.dpad_up)) {
                selectedRow--;
                if (selectedRow < 0) {
                    selectedRow = 5;
                }
            }

            if ((currentGamepad1.dpad_right && !previousGamepad1.dpad_right) || (currentGamepad2.dpad_right && !previousGamepad2.dpad_right)) {
                if (selectedRow == 0) {
                    delay += 0.5;
                    if (delay > 30) {
                        delay = 30;
                    }
                }

                if (selectedRow == 1) {
                    play++;
                    if (play > 3) {
                        play = 0;
                    }
                    if (play == 0) {
                        firstMark = true;
                        secondMark = false;
                        secondMarkGate = false;
                        thirdMark = false;
                    }
                    if (play == 1) {
                        firstMark = true;
                        secondMark = true;
                        secondMarkGate = false;
                        thirdMark = false;
                    }
                    if (play == 2) {
                        firstMark = true;
                        secondMark = false;
                        secondMarkGate = true;
                        thirdMark = false;
                    }
                    if (play == 3) {
                        firstMark = true;
                        secondMark = true;
                        secondMarkGate = false;
                        thirdMark = true;
                    }
                }

                else if (selectedRow == 4) {
                    secondMark = !secondMark;
                    if (secondMark && secondMarkGate) {
                        secondMarkGate = false;
                    }
                }
                else if (selectedRow == 3) {
                    firstMark = !firstMark;
                }
                else if (selectedRow == 2) {
                    secondMarkGate = !secondMarkGate;
                    if (secondMark && secondMarkGate) {
                        secondMark = false;
                    }
                }
            }

            if ((currentGamepad1.dpad_left && !previousGamepad1.dpad_left) || (currentGamepad2.dpad_left && !previousGamepad2.dpad_left)) {
                if (selectedRow == 0) {
                    delay -= 0.5;
                    if (delay < 0) {
                        delay = 0;
                    }
                }

                if (selectedRow == 1) {
                    play--;
                    if (play < 0) {
                        play = 3;
                    }
                    if (play == 0) {
                        firstMark = true;
                        secondMark = false;
                        secondMarkGate = false;
                        thirdMark = false;
                    }
                    if (play == 1) {
                        firstMark = true;
                        secondMark = true;
                        secondMarkGate = false;
                        thirdMark = false;
                    }
                    if (play == 2) {
                        firstMark = true;
                        secondMark = false;
                        secondMarkGate = true;
                        thirdMark = false;
                    }
                    if (play == 3) {
                        firstMark = true;
                        secondMark = true;
                        secondMarkGate = false;
                        thirdMark = true;
                    }
                }

                else if (selectedRow == 4) {
                    secondMark = !secondMark;
                    if (secondMark && secondMarkGate) {
                        secondMarkGate = false;
                    }
                }
                else if (selectedRow == 3) {
                    firstMark = !firstMark;
                }
                else if (selectedRow == 2) {
                    secondMarkGate = !secondMarkGate;
                    if (secondMark && secondMarkGate) {
                        secondMark = false;
                    }
                }
            }

            if (selectedRow == 0) {
                telemetry.addData("> Delay", delay);
            }
            else {
                telemetry.addData("    Delay", delay);
            }

            if (selectedRow == 1) {
                telemetry.addData("> Play", play);
            }
            else {
                telemetry.addData("    Play", play);
            }

            telemetry.addData("    Play Name", names.get(play));

            if (selectedRow == 2) {
                telemetry.addData("> Pickup Second Mark And Hit Gate", secondMarkGate);
            }
            else {
                telemetry.addData("    Pickup Second Mark And Hit Gate", secondMarkGate);
            }

            if (selectedRow == 3) {
                telemetry.addData("> Pickup First Mark", firstMark);
            }
            else {
                telemetry.addData("    Pickup First Mark", firstMark);
            }

            if (selectedRow == 4) {
                telemetry.addData("> Pickup Second Mark", secondMark);
            }
            else {
                telemetry.addData("    Pickup Second Mark", secondMark);
            }

            if (selectedRow == 5) {
                telemetry.addData("> Pickup Third Mark", thirdMark);
            }
            else {
                telemetry.addData("    Pickup Third Mark", thirdMark);
            }



            telemetry.update();


        }
        if (!thirdMark) {

            md = new MecanumDrive(hardwareMap, !thirdMark ? new Pose2d(-50.5, 50.5, Math.toRadians(37)) : new Pose2d(-50.5, 50.5, Math.toRadians(-143)));
            autonomousActionBuilder = new AutonomousActionBuilder(md, robot);

            trajectories = autonomousActionBuilder.getRedCloseTrajectories();
        }

        waitForStart();

        sleep((long) (delay * 1000));

        //start always with PGP

        // Go to the Launch Pose and april tag

        if (!thirdMark) {

            Actions.runBlocking(new ParallelAction(
                trajectories[0]
            ));

            for (int i = 0; i < 50; i++) {
                motif = robot.getLauncher().getMotifPattern();

                if (motif == null) {
                    telemetry.addData("Motif Pattern", "Not Detected");
                } else {
                    telemetry.addData("Motif Pattern", motif[0].toString() + ", " + motif[1].toString() + ", " + motif[2].toString());
                }
                telemetry.update();
                sleep(1);
            }

            if (motif == null) {
                motif = new ArtifactColor[]{ArtifactColor.PURPLE, ArtifactColor.PURPLE, ArtifactColor.GREEN};
            }

            if (motif[0] != ArtifactColor.GREEN) {
                Actions.runBlocking(autonomousActionBuilder.getIndexOutputAction(1));
            }

            Actions.runBlocking(new ParallelAction(
                trajectories[1],
                autonomousActionBuilder.getSpinLauncherClose()
            ));

            launchInMotifOrder(motif, 0);
        }
        else {
            Actions.runBlocking(new ParallelAction(
                trajectories[6],
                autonomousActionBuilder.getSpinLauncherClose()
            ));
            launchInMotifOrder(new ArtifactColor[]{ArtifactColor.GREEN, ArtifactColor.PURPLE, ArtifactColor.PURPLE}, 0);

            RunTimeoutAction aprilTagAction = (RunTimeoutAction) autonomousActionBuilder.getAprilTagAction();

            Actions.runBlocking(new ParallelAction(
                trajectories[7],
                aprilTagAction
            ));

            motif = ((Launcher.AprilTagAction) aprilTagAction.getAction()).getPattern();

            if (motif != null) {

                telemetry.addData("motif", Arrays.toString(motif));
                telemetry.update();
            }
            else {
                telemetry.addData("motif", "not detected");
                telemetry.update();
            }
        }

        if (motif==null) {
            motif = new ArtifactColor[] {ArtifactColor.GREEN, ArtifactColor.PURPLE, ArtifactColor.PURPLE};
        }

        if (secondMarkGate) {
            Actions.runBlocking(
                new ParallelAction(
                    trajectories[5],
                    autonomousActionBuilder.getIndexIntakeAction(0),
                    new SequentialAction(
                        new SleepAction(0.5),
                        autonomousActionBuilder.getStartIntake(),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(4),
                        autonomousActionBuilder.getIndexIntakeAction(1),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(1.2),
                        autonomousActionBuilder.getIndexIntakeAction(2),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(1.2),
                        autonomousActionBuilder.getSpinLauncherClose(),
                        autonomousActionBuilder.getStopIntake()
                    )
            ));

            launchInMotifOrder(motif, 1);
        }

        if (firstMark) {

            // Pickup first mark

            Actions.runBlocking(new SequentialAction(
                new ParallelAction(
                    trajectories[2],
                    autonomousActionBuilder.getIndexIntakeAction(0),
                    new SequentialAction(
                        new SleepAction(0.5),
                        autonomousActionBuilder.getStartIntake(),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(4),
                        autonomousActionBuilder.getIndexIntakeAction(1),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(1.2),
                        autonomousActionBuilder.getIndexIntakeAction(2),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(1.2),
                        autonomousActionBuilder.getSpinLauncherClose(),
                        autonomousActionBuilder.getStopIntake()
                    )
                )
            ));

            //Actions.runBlocking(trajectories[1]);

            launchInMotifOrder(motif, 2);
        }

        if (secondMark) {

            // Pickup second mark

            Actions.runBlocking(new SequentialAction(
                new ParallelAction(
                    trajectories[3],
                    autonomousActionBuilder.getIndexIntakeAction(0),
                    new SequentialAction(
                        new SleepAction(0.5),
                        autonomousActionBuilder.getStartIntake(),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(4),
                        autonomousActionBuilder.getIndexIntakeAction(1),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(1.2),
                        autonomousActionBuilder.getIndexIntakeAction(2),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(1.2),
                        autonomousActionBuilder.getSpinLauncherClose(),
                        autonomousActionBuilder.getStopIntake()
                    )
                )
            ));

            //Actions.runBlocking(trajectories[1]);

            launchInMotifOrder(motif, 1);
        }

        if (thirdMark) {

            // Pickup third mark

            Actions.runBlocking(new SequentialAction(
                new ParallelAction(
                    trajectories[8],
                    autonomousActionBuilder.getIndexIntakeAction(0),
                    new SequentialAction(
                        new SleepAction(0.5),
                        autonomousActionBuilder.getStartIntake(),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(4),
                        autonomousActionBuilder.getIndexIntakeAction(1),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(1.2),
                        autonomousActionBuilder.getIndexIntakeAction(2),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(1.2),
                        autonomousActionBuilder.getSpinLauncherClose(),
                        autonomousActionBuilder.getStopIntake()
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

    public void launchInMotifOrder(ArtifactColor[] motifPattern, int greenLocation) {
        Actions.runBlocking(motifPattern[0] == ArtifactColor.GREEN ? autonomousActionBuilder.getIndexOutputAction(greenLocation) : autonomousActionBuilder.getIndexOutputAction(greenLocation == 0 ? 1 : 0));
        Actions.runBlocking(autonomousActionBuilder.getKickBall());
        Actions.runBlocking(autonomousActionBuilder.getResetKicker());
        Actions.runBlocking(motifPattern[1] == ArtifactColor.GREEN ? autonomousActionBuilder.getIndexOutputAction(greenLocation) : (motifPattern[0] == ArtifactColor.GREEN ? autonomousActionBuilder.getIndexOutputAction(greenLocation == 0 ? 1 : 0) : autonomousActionBuilder.getIndexOutputAction(greenLocation == 2 ? 1 : 2)));
        Actions.runBlocking(autonomousActionBuilder.getKickBall());
        Actions.runBlocking(autonomousActionBuilder.getResetKicker());
        Actions.runBlocking(motifPattern[2] == ArtifactColor.GREEN ? autonomousActionBuilder.getIndexOutputAction(greenLocation) : autonomousActionBuilder.getIndexOutputAction(greenLocation == 2 ? 1 : 2));
        Actions.runBlocking(autonomousActionBuilder.getKickBall());
        Actions.runBlocking(new ParallelAction(
            autonomousActionBuilder.getStopLauncher(),
            autonomousActionBuilder.getResetKicker()
        ));
    }
}
