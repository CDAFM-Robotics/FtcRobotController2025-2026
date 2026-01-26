package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.autonomous.actions.AutonomousActionBuilder;
import org.firstinspires.ftc.teamcode.common.Robot;
import org.firstinspires.ftc.teamcode.common.subsystems.Launcher;
import org.firstinspires.ftc.teamcode.common.util.RunTimeoutAction;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

import java.util.Arrays;

@TeleOp (name = "April Tag Action Test", group = "Testing")
public class AutonomousAprilTagActionTestOpMode extends LinearOpMode {
    Action[] trajectories;

    AutonomousActionBuilder autonomousActionBuilder;


    @Override
    public void runOpMode() {
        MecanumDrive md = new MecanumDrive(hardwareMap, new Pose2d(-50.5, -50.5, Math.toRadians(143)));
        Robot robot = new Robot(hardwareMap, telemetry);
        robot.getLauncher().setLimelightPipeline(Robot.LLPipelines.OBELISK.ordinal());
        autonomousActionBuilder = new AutonomousActionBuilder(md, robot);

        Action aprilTagAction = autonomousActionBuilder.getAprilTagAction();

        waitForStart();

        Actions.runBlocking(aprilTagAction);

        telemetry.addData("Motif Pattern", Arrays.toString(((Launcher.AprilTagAction) ((RunTimeoutAction) aprilTagAction).getAction()).getPattern()));

        telemetry.update();

        sleep(10000);
    }
}
