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

import org.firstinspires.ftc.teamcode.autonomous.actions.AutonomousActionBuilder;
import org.firstinspires.ftc.teamcode.common.Robot;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

import java.util.function.Supplier;

@Autonomous(name = "Red Back Autonomous", group = "0competition")
public class RedBackAutonomousOpMode extends LinearOpMode {

    Supplier<Action>[] otherActions;
    Action[] trajectories;


    @Override
    public void runOpMode() throws InterruptedException {
        MecanumDrive md = new MecanumDrive(hardwareMap, /*new Pose2d(new Vector2d(61, 11.5), Math.toRadians(180))*/ new Pose2d(new Vector2d(61, 11.75), Math.toRadians(-90)));
        Robot robot = new Robot(hardwareMap, telemetry);
        robot.getLauncher().setLimelightPipeline(7);
        AutonomousActionBuilder autonomousTrajectoryBuilder = new AutonomousActionBuilder(md, robot);

        trajectories = autonomousTrajectoryBuilder.getBlueFarTrajectories();

        otherActions = autonomousTrajectoryBuilder.getOtherActions();

        Robot.ArtifactColor[] motif = null;

        while(opModeInInit()) {
            motif = robot.getLauncher().getMotifPattern();

            if (motif == null) {
                telemetry.addData("Motif Pattern", "Not Detected");
            }
            else {
                telemetry.addData("Motif Pattern", motif[0].toString() + ", " + motif[1].toString() + ", " + motif[2].toString());
            }
            telemetry.update();
        }

        waitForStart();



        //start always with G in launcher



        if (motif == null) {
            motif = new Robot.ArtifactColor[] {Robot.ArtifactColor.GREEN, Robot.ArtifactColor.PURPLE, Robot.ArtifactColor.PURPLE};
        }







        // Go to the Launch Pose

        Actions.runBlocking(new ParallelAction(
            new SequentialAction(
                new SleepAction(0.5),
                trajectories[0]
            ),
            otherActions[0].get()
        ));



        sleep(500);

        //Actions.runBlocking(launchInMotifOrder(motif));

        launchInMotifOrder(motif, 0);



//        if (motif[0] != Robot.ArtifactColor.GREEN) {
//            Actions.runBlocking(otherActions[3].get());
//        }
//
//        Actions.runBlocking(otherActions[5].get());
//
//        Actions.runBlocking(otherActions[8].get());
//
//        if (motif[1] == Robot.ArtifactColor.GREEN) {
//            Actions.runBlocking(otherActions[2].get());
//        }
//        else if (motif[0] == Robot.ArtifactColor.GREEN) {
//            Actions.runBlocking(otherActions[3].get());
//        }
//        else {
//            Actions.runBlocking(otherActions[4].get());
//        }
//
//        Actions.runBlocking(otherActions[5].get());
//
//        Actions.runBlocking(otherActions[8].get());
//
//        if (motif[0] == Robot.ArtifactColor.GREEN) {
//            Actions.runBlocking(otherActions[4].get());
//        }
//        else if (motif[1] == Robot.ArtifactColor.GREEN) {
//            Actions.runBlocking(otherActions[4].get());
//        }
//        else {
//            Actions.runBlocking(otherActions[2].get());
//        }
//
//        Actions.runBlocking(otherActions[5].get());
//
//        Actions.runBlocking(new ParallelAction(
//            otherActions[8].get(),
//            otherActions[1].get()
//        ));







        // Pickup first mark

        Actions.runBlocking(new SequentialAction(
            new ParallelAction(
                trajectories[1],
                otherActions[2].get(),
                new SequentialAction(
                    new SleepAction(0.4),
                    otherActions[6].get(),
                    otherActions[10].get(),
                    otherActions[3].get(),
                    otherActions[11].get(),
                    otherActions[4].get(),
                    otherActions[11].get(),
                    otherActions[0].get(),
                    otherActions[7].get()
                )
            )
        ));



        sleep(1000);

        launchInMotifOrder(motif, 2);

        sleep(500);

        // LEave

        Actions.runBlocking(new ParallelAction(
            trajectories[2]
        ));



    }

    public Action launchInMotifOrder(Robot.ArtifactColor[] motifPattern, int greenLocation) {

        Actions.runBlocking(motifPattern[0] == Robot.ArtifactColor.GREEN ? otherActions[greenLocation + 2].get() : otherActions[greenLocation == 0 ? 3 : 2].get());
        Actions.runBlocking(otherActions[5].get());
        Actions.runBlocking(otherActions[8].get());
        Actions.runBlocking(motifPattern[1] == Robot.ArtifactColor.GREEN ? otherActions[greenLocation + 2].get() : (motifPattern[0] == Robot.ArtifactColor.GREEN ? otherActions[greenLocation == 0 ? 3 : 2].get() : otherActions[greenLocation == 2 ? 3 : 4].get()));
        Actions.runBlocking(otherActions[5].get());
        Actions.runBlocking(otherActions[8].get());
        Actions.runBlocking(motifPattern[2] == Robot.ArtifactColor.GREEN ? otherActions[greenLocation + 2].get() : otherActions[greenLocation == 2 ? 3 : 4].get());
        Actions.runBlocking(otherActions[5].get());
        Actions.runBlocking(new ParallelAction(
            otherActions[1].get(),
            otherActions[8].get()
        ));

        return new SequentialAction(
            motifPattern[0] == Robot.ArtifactColor.GREEN ? otherActions[greenLocation + 2].get() : otherActions[greenLocation == 0 ? 3 : 2].get(),
            otherActions[5].get(),
            otherActions[8].get(),
            motifPattern[1] == Robot.ArtifactColor.GREEN ? otherActions[greenLocation + 2].get() : (motifPattern[0] == Robot.ArtifactColor.GREEN ? otherActions[greenLocation == 0 ? 3 : 2].get() : otherActions[greenLocation == 2 ? 3 : 4].get()),
            otherActions[5].get(),
            otherActions[8].get(),
            motifPattern[2] == Robot.ArtifactColor.GREEN ? otherActions[greenLocation + 2].get() : otherActions[greenLocation == 2 ? 3 : 4].get(),
            otherActions[5].get(),
            new ParallelAction(
                otherActions[1].get(),
                otherActions[8].get()
            )
        );


    }
}
