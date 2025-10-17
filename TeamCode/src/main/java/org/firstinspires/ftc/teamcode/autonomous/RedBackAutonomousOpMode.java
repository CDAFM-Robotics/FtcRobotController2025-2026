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
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name = "Red Back Autonomous", group = "0competition")
public class RedBackAutonomousOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        MecanumDrive md = new MecanumDrive(hardwareMap, /*new Pose2d(new Vector2d(61, 11.5), Math.toRadians(180))*/ new Pose2d(57.7, 12.5, Math.toRadians(180)));
        Robot robot = new Robot(hardwareMap, telemetry);
        AutonomousActionBuilder autonomousTrajectoryBuilder = new AutonomousActionBuilder(md, robot);

        Action[] trajectories = autonomousTrajectoryBuilder.getRedFarTrajectories();

        Action[] otherActions = autonomousTrajectoryBuilder.getOtherActions();

        Robot.ArtifactColor[] motif = null;

        while(opModeInInit()) {
            motif = robot.getLauncher().getMotifPattern();
        }

        waitForStart();

        //start always with PGP

        if (motif == null) {
            motif = new Robot.ArtifactColor[] {Robot.ArtifactColor.GREEN, Robot.ArtifactColor.PURPLE, Robot.ArtifactColor.PURPLE};
        }

        if (motif[0] != Robot.ArtifactColor.GREEN) {
            Actions.runBlocking(otherActions[2]);
        }

        // Go to the Launch Pose

        Actions.runBlocking(new ParallelAction(
            trajectories[0],
            otherActions[0]
        ));

        Actions.runBlocking(otherActions[5]);


        Actions.runBlocking(otherActions[8]);

        if (motif[0] == Robot.ArtifactColor.GREEN) {
            Actions.runBlocking(otherActions[2]);
        }
        else if (motif[1] == Robot.ArtifactColor.GREEN) {
            Actions.runBlocking(otherActions[3]);
        }
        else {
            Actions.runBlocking(otherActions[4]);
        }

        Actions.runBlocking(otherActions[5]);

        Actions.runBlocking(otherActions[8]);

        if (motif[0] == Robot.ArtifactColor.GREEN) {
            Actions.runBlocking(otherActions[4]);
        }
        else if (motif[1] == Robot.ArtifactColor.GREEN) {
            Actions.runBlocking(otherActions[4]);
        }
        else {
            Actions.runBlocking(otherActions[3]);
        }

        Actions.runBlocking(otherActions[5]);

        Actions.runBlocking(otherActions[8]);

        // Pickup first mark

        //Actions.runBlocking(trajectories[1]);

        sleep(1000);

        // LEave

        Actions.runBlocking(new ParallelAction(
            trajectories[2],
            otherActions[1]
        ));

    }
}
