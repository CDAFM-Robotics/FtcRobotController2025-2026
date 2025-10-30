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

import java.util.function.Supplier;

@Autonomous(name = "Red Front Autonomous", group = "0competition")
public class RedFrontAutonomousOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        MecanumDrive md = new MecanumDrive(hardwareMap, new Pose2d(new Vector2d(61, 11.5), Math.toRadians(180)));
        Robot robot = new Robot(hardwareMap, telemetry);
        AutonomousActionBuilder autonomousTrajectoryBuilder = new AutonomousActionBuilder(md, robot);

        Action[] trajectories = autonomousTrajectoryBuilder.getRedCloseTrajectories();

        Supplier<Action>[] otherActions = autonomousTrajectoryBuilder.getOtherActions();

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
            Actions.runBlocking(otherActions[2].get());
        }

        // Go to the Launch Pose

        Actions.runBlocking(new ParallelAction(
            trajectories[0],
            otherActions[0].get()
        ));

        Actions.runBlocking(otherActions[5].get());

        // Pickup first mark

        //Actions.runBlocking(trajectories[1]);

        sleep(2000);

        // LEave

        Actions.runBlocking(new ParallelAction(
            trajectories[3],
            otherActions[1].get()
        ));

    }
}
