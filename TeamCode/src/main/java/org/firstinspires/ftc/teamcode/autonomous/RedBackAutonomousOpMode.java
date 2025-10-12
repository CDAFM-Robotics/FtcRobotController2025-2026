package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.autonomous.trajectories.AutonomousActionBuilder;
import org.firstinspires.ftc.teamcode.common.Robot;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name = "Red Back Autonomous")
public class RedBackAutonomousOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        MecanumDrive md = new MecanumDrive(hardwareMap, new Pose2d(new Vector2d(61, 11.5), Math.toRadians(180)));
        Robot robot = new Robot(hardwareMap, telemetry);
        AutonomousActionBuilder autonomousTrajectoryBuilder = new AutonomousActionBuilder(md, robot);

        Action[] trajectories = autonomousTrajectoryBuilder.getRedFarTrajectories();

        Action[] otherActions = autonomousTrajectoryBuilder.getOtherActions();

        waitForStart();

        // Go to the Launch Pose

        Actions.runBlocking(new ParallelAction(
            trajectories[0],
            otherActions[0]
        ));

        // Pickup first mark

        Actions.runBlocking(trajectories[1]);

        sleep(2000);

        // Pickup second mark

        Actions.runBlocking(trajectories[2]);

    }
}
