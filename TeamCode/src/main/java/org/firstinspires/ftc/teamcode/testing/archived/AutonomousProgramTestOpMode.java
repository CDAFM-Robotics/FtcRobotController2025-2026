package org.firstinspires.ftc.teamcode.testing.archived;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name = "Autonomous test thingy yay finally this took so long :)", group = "Testing")
@Disabled
public class AutonomousProgramTestOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        MecanumDrive drive = new MecanumDrive(hardwareMap, new Pose2d(23.5, -47.5, Math.PI/2));

        Action trajectory = drive.actionBuilder(new Pose2d(23.5, -47.5, Math.PI/2))
            .setTangent(0)
            .strafeTo(new Vector2d(23.5, 47.5))
            .strafeTo(new Vector2d(-23.5, 47.5))
            .strafeTo(new Vector2d(-23.5, -47.5))
            .strafeTo(new Vector2d(23.5, -47.5))
            .build();

        waitForStart();

        Actions.runBlocking(trajectory);

    }
}
