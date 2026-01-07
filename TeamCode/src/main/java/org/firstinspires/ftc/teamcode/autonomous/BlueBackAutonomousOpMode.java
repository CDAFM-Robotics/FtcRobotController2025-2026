package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.SleepAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.autonomous.actions.AutonomousActionBuilder;
import org.firstinspires.ftc.teamcode.common.Robot;
import org.firstinspires.ftc.teamcode.common.util.ArtifactColor;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

import java.util.ArrayList;
import java.util.function.Supplier;

@Autonomous(name = "Blue Back Autonomous", group = "0competition")
public class BlueBackAutonomousOpMode extends LinearOpMode {

    Supplier<Action>[] otherActions;
    Action[] trajectories;

    AutonomousActionBuilder autonomousActionBuilder;


    @Override
    public void runOpMode() throws InterruptedException {
        MecanumDrive md = new MecanumDrive(hardwareMap, /*new Pose2d(new Vector2d(61, 11.5), Math.toRadians(180))*/ new Pose2d(new Vector2d(61, -11.75), Math.toRadians(-90)));
        Robot robot = new Robot(hardwareMap, telemetry);
        robot.getLauncher().setLimelightPipeline(Robot.LLPipelines.OBELISK.ordinal());
        autonomousActionBuilder = new AutonomousActionBuilder(md, robot);

        trajectories = autonomousActionBuilder.getBlueFarTrajectories();



        ArtifactColor[] motif = null;

        telemetry.setMsTransmissionInterval(50);

        int selectedRow = 0;
        double delay = 0;
        int play = 2;

        ArrayList<String> names = new ArrayList<>();
        names.add(0, "Third And Second Mark");
        names.add(1, "Third Mark");
        names.add(2, "Third Mark And Pickup Loading Zone");
        names.add(3, "Pickup Loading Zone");

        boolean thirdMark = true;
        boolean secondMark = false;
        boolean loadingZone = true;

        Gamepad currentGamepad1 = new Gamepad();
        Gamepad previousGamepad1 = new Gamepad();
        Gamepad currentGamepad2 = new Gamepad();
        Gamepad previousGamepad2 = new Gamepad();

        while(opModeInInit()) {

            motif = robot.getLauncher().getMotifPattern();

            if (motif == null) {
                telemetry.addData("  Motif Pattern", "Not Detected");
            }
            else {
                telemetry.addData("  Motif Pattern", motif[0].toString() + ", " + motif[1].toString() + ", " + motif[2].toString());
            }

            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);
            currentGamepad1.copy(gamepad1);
            currentGamepad2.copy(gamepad2);

            if ((currentGamepad1.dpad_down && !previousGamepad1.dpad_down) || (currentGamepad2.dpad_down && !previousGamepad2.dpad_down)) {
                selectedRow++;
                if (selectedRow > 4) {
                    selectedRow = 0;
                }
            }
            if ((currentGamepad1.dpad_up && !previousGamepad1.dpad_up) || (currentGamepad2.dpad_up && !previousGamepad2.dpad_up)) {
                selectedRow--;
                if (selectedRow < 0) {
                    selectedRow = 4;
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
                        thirdMark = true;
                        secondMark = true;
                        loadingZone = false;
                    }
                    if (play == 1) {
                        thirdMark = true;
                        secondMark = false;
                        loadingZone = false;
                    }
                    if (play == 2) {
                        thirdMark = true;
                        secondMark = false;
                        loadingZone = true;
                    }
                    if (play == 3) {
                        thirdMark = false;
                        secondMark = false;
                        loadingZone = true;
                    }
                }

                else if (selectedRow == 4) {
                    secondMark = !secondMark;
                }
                else if (selectedRow == 3) {
                    thirdMark = !thirdMark;
                }
                else if (selectedRow == 2) {
                    loadingZone = !loadingZone;
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
                        thirdMark = true;
                        secondMark = true;
                        loadingZone = false;
                    }
                    if (play == 1) {
                        thirdMark = true;
                        secondMark = false;
                        loadingZone = false;
                    }
                    if (play == 2) {
                        thirdMark = true;
                        secondMark = false;
                        loadingZone = true;
                    }
                    if (play == 3) {
                        thirdMark = false;
                        secondMark = false;
                        loadingZone = true;
                    }
                }

                else if (selectedRow == 4) {
                    secondMark = !secondMark;
                    if (secondMark && loadingZone) {
                        loadingZone = false;
                    }
                }
                else if (selectedRow == 3) {
                    thirdMark = !thirdMark;
                }
                else if (selectedRow == 2) {
                    loadingZone = !loadingZone;
                    if (secondMark && loadingZone) {
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
                telemetry.addData("> Pickup in loading zone after", loadingZone);
            }
            else {
                telemetry.addData("    Pickup in loading zone after", loadingZone);
            }

            if (selectedRow == 3) {
                telemetry.addData("> Pickup Third Mark", thirdMark);
            }
            else {
                telemetry.addData("    Pickup Third Mark", thirdMark);
            }

            if (selectedRow == 4) {
                telemetry.addData("> Pickup Second Mark", secondMark);
            }
            else {
                telemetry.addData("    Pickup Second Mark", secondMark);
            }



            telemetry.update();


        }

        waitForStart();

        sleep((long) (delay * 1000));

        //start always with G in launcher

        if (motif == null) {
            motif = new ArtifactColor[] {ArtifactColor.GREEN, ArtifactColor.PURPLE, ArtifactColor.PURPLE};
        }


        // Go to the Launch Pose

        Actions.runBlocking(new ParallelAction(
            trajectories[0],
            new SequentialAction(
                new SleepAction(0.5),
                autonomousActionBuilder.getSpinLauncherFar()
            )
        ));


        // Launch first 3
        launchInMotifOrder(motif, 0);

        // Pickup third mark

        if (thirdMark) {

            Actions.runBlocking(new SequentialAction(
                new ParallelAction(
                    trajectories[1],
                    autonomousActionBuilder.getIndexIntakeAction(0),
                    new SequentialAction(
                        autonomousActionBuilder.getStartIntake(),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(4),
                        autonomousActionBuilder.getIndexIntakeAction(1),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(1.5),
                        autonomousActionBuilder.getIndexIntakeAction(2),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(1.5),
                        autonomousActionBuilder.getSpinLauncherFar(),
                        autonomousActionBuilder.getStopIntake()
                    )
                )
            ));



            launchInMotifOrder(motif, 0);
        }

        if (secondMark) {

            Actions.runBlocking(new SequentialAction(
                new ParallelAction(
                    trajectories[2],
                    autonomousActionBuilder.getIndexIntakeAction(0),
                    new SequentialAction(
                        new SleepAction(0.5),
                        autonomousActionBuilder.getStartIntake(),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(4),
                        autonomousActionBuilder.getIndexIntakeAction(1),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(1.5),
                        autonomousActionBuilder.getIndexIntakeAction(2),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(1.5),
                        autonomousActionBuilder.getSpinLauncherFar(),
                        autonomousActionBuilder.getStopIntake()
                    )
                )
            ));


            launchInMotifOrder(motif, 1);

        }

        if (loadingZone) {
            Actions.runBlocking(new SequentialAction(
                new ParallelAction(
                    trajectories[5],
                    autonomousActionBuilder.getIndexIntakeAction(0),
                    new SequentialAction(
                        new SleepAction(1.0),
                        autonomousActionBuilder.getStartIntake(),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(4),
                        autonomousActionBuilder.getIndexIntakeAction(1),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(1.5),
                        autonomousActionBuilder.getIndexIntakeAction(2),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(1.5),
                        autonomousActionBuilder.getSpinLauncherFar(),
                        autonomousActionBuilder.getStopIntake()
                    )
                )
            ));
            launchInMotifOrder(motif, 2);

            if (!thirdMark && !secondMark) {
                Actions.runBlocking(new SequentialAction(
                    new ParallelAction(
                        autonomousActionBuilder.getBlueFarPickupLoadingZone(),
                        autonomousActionBuilder.getIndexIntakeAction(0),
                        new SequentialAction(
                            new SleepAction(1.0),
                            autonomousActionBuilder.getStartIntake(),
                            autonomousActionBuilder.getWaitUntilBallInIndexer(4),
                            autonomousActionBuilder.getIndexIntakeAction(1),
                            autonomousActionBuilder.getWaitUntilBallInIndexer(1.5),
                            autonomousActionBuilder.getIndexIntakeAction(2),
                            autonomousActionBuilder.getWaitUntilBallInIndexer(1.5),
                            autonomousActionBuilder.getSpinLauncherFar(),
                            autonomousActionBuilder.getStopIntake()
                        )
                    )
                ));
                launchInMotifOrder(motif, 2);
            }

            // Run Pickup again so drivers may have balls

            Actions.runBlocking(new SequentialAction(
                new ParallelAction(
                    autonomousActionBuilder.getBlueFarPickupLoadingZoneLeave(),
                    autonomousActionBuilder.getIndexIntakeAction(0),
                    new SequentialAction(
                        new SleepAction(1.0),
                        autonomousActionBuilder.getStartIntake(),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(4),
                        autonomousActionBuilder.getIndexIntakeAction(1),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(1.5),
                        autonomousActionBuilder.getIndexIntakeAction(2),
                        autonomousActionBuilder.getWaitUntilBallInIndexer(1.5),
                        autonomousActionBuilder.getSpinLauncherFar(),
                        autonomousActionBuilder.getStopIntake()
                    )
                )
            ));
        }

        // LEave

        Actions.runBlocking(new ParallelAction(
            trajectories[4]
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
