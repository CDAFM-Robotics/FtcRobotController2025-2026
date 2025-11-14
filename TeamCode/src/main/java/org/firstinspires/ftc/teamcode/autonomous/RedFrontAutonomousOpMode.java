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
import org.firstinspires.ftc.teamcode.common.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

import java.util.function.Supplier;

@Autonomous(name = "Red Front Autonomous", group = "0competition")
public class RedFrontAutonomousOpMode extends LinearOpMode {

    Action[] trajectories;

    Supplier<Action>[] otherActions;


    @Override
    public void runOpMode() throws InterruptedException {
        MecanumDrive md = new MecanumDrive(hardwareMap, new Pose2d(-50.5, 50.5, Math.toRadians(37)));
        Robot robot = new Robot(hardwareMap, telemetry);
        robot.getLauncher().setLimelightPipeline(7);
        AutonomousActionBuilder autonomousTrajectoryBuilder = new AutonomousActionBuilder(md, robot);

        trajectories = autonomousTrajectoryBuilder.getRedCloseTrajectories();

        otherActions = autonomousTrajectoryBuilder.getOtherActions();

        Robot.ArtifactColor[] motif = null;

        waitForStart();

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

        sleep(1000);


        if (motif == null) {
            motif = new Robot.ArtifactColor[] {Robot.ArtifactColor.PURPLE, Robot.ArtifactColor.PURPLE, Robot.ArtifactColor.GREEN};
        }

        if (motif[0] != Robot.ArtifactColor.GREEN) {
            Actions.runBlocking(otherActions[3].get());
        }

        Actions.runBlocking(new ParallelAction(
            trajectories[1],
            otherActions[9].get()
        ));

        sleep(500);

        launchInMotifOrder(motif, 1);

        // Pickup first mark

        Actions.runBlocking(new SequentialAction(
            new ParallelAction(
                trajectories[2],
                otherActions[2].get(),
                new SequentialAction(
                    new SleepAction(0.6),
                    otherActions[6].get(),
                    otherActions[10].get(),
                    otherActions[3].get(),
                    otherActions[10].get(),
                    otherActions[4].get(),
                    otherActions[10].get(),
                    otherActions[9].get(),
                    otherActions[7].get()
                )
            )
        ));

        //Actions.runBlocking(trajectories[1]);

        sleep(500);

        launchInMotifOrder(motif, 0);

        sleep(500);

        // LEave

        Actions.runBlocking(new ParallelAction(
            trajectories[4],
            otherActions[1].get()
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
