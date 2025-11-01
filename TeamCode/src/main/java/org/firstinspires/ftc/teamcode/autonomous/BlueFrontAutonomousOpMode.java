package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.autonomous.actions.AutonomousActionBuilder;
import org.firstinspires.ftc.teamcode.common.Robot;
import org.firstinspires.ftc.teamcode.common.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

import java.util.function.Supplier;

@Autonomous(name = "Blue Front Autonomous", group = "0competition")
public class BlueFrontAutonomousOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        MecanumDrive md = new MecanumDrive(hardwareMap, new Pose2d(-50.5, -50.5, Math.toRadians(143)));
        Robot robot = new Robot(hardwareMap, telemetry);
        robot.getLauncher().setLimelightPipeline(7);
        AutonomousActionBuilder autonomousTrajectoryBuilder = new AutonomousActionBuilder(md, robot);

        Action[] trajectories = autonomousTrajectoryBuilder.getBlueCloseTrajectories();

        Supplier<Action>[] otherActions = autonomousTrajectoryBuilder.getOtherActions();

        Robot.ArtifactColor[] motif = null;

        waitForStart();

        //start always with PGP

        // Go to the Launch Pose

        Launcher.AprilTagAction aprilTagAction = (Launcher.AprilTagAction) otherActions[9].get();


        Actions.runBlocking(new ParallelAction(
            trajectories[0]
        ));

        for(int i = 0; i < 100; i++) {
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
            Actions.runBlocking(otherActions[3].get());
        }

        Actions.runBlocking(new ParallelAction(
            trajectories[1],
            otherActions[10].get()
        ));

        sleep(500);

        Actions.runBlocking(otherActions[5].get());

        Actions.runBlocking(otherActions[8].get());

        if (motif[1] == Robot.ArtifactColor.GREEN) {
            Actions.runBlocking(otherActions[2].get());
        }
        else if (motif[0] == Robot.ArtifactColor.GREEN) {
            Actions.runBlocking(otherActions[3].get());
        }
        else {
            Actions.runBlocking(otherActions[4].get());
        }

        Actions.runBlocking(otherActions[5].get());

        Actions.runBlocking(otherActions[8].get());

        if (motif[0] == Robot.ArtifactColor.GREEN) {
            Actions.runBlocking(otherActions[4].get());
        }
        else if (motif[1] == Robot.ArtifactColor.GREEN) {
            Actions.runBlocking(otherActions[4].get());
        }
        else {
            Actions.runBlocking(otherActions[2].get());
        }



        Actions.runBlocking(otherActions[5].get());

        Actions.runBlocking(new ParallelAction(
            otherActions[8].get(),
            otherActions[1].get()
        ));

        // Pickup first mark

        //Actions.runBlocking(trajectories[1]);

        sleep(2000);

        // LEave

        Actions.runBlocking(new ParallelAction(
            trajectories[4],
            otherActions[1].get()
        ));





    }
}
