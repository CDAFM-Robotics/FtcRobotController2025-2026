package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.autonomous.trajectories.AutonomousTrajectoryBuilder;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name = "Red Back Autonomous")
public class RedBackAutonomousOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        MecanumDrive md = new MecanumDrive(hardwareMap, new Pose2d(new Vector2d(61, 11.5), Math.toRadians(180)));
        AutonomousTrajectoryBuilder autonomousTrajectoryBuilder = new AutonomousTrajectoryBuilder(md);

        Action[] trajectories = autonomousTrajectoryBuilder.getRedFarTrajectories();

        waitForStart();

        Actions.runBlocking(trajectories[0]);
        sleep(2000);
        Actions.runBlocking(trajectories[1]);
        sleep(2000);
        Actions.runBlocking(trajectories[2]);
        sleep(2000);
        Actions.runBlocking(trajectories[3]);

    }
}
