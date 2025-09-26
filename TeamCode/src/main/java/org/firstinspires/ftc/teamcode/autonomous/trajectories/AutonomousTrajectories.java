package org.firstinspires.ftc.teamcode.autonomous.trajectories;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

public class AutonomousTrajectories {

    public Action redFarLaunchPickupThirdMark;
    public Action redFarLaunchPickupHumanPlayerZone;
    public Action redFarLaunchToLeaveLaunchZone;

    public Action redCloseStartToCloseLaunch;
    public Action redCloseLaunchPickupFirstMark;
    public Action redCloseLaunchPickupSecondMark;
    public Action redCloseLaunchToLeaveLaunchZone;

    public Pose2d redFarLaunchPose = new Pose2d(57.7, -12.5, Math.toRadians(-165));

    public Pose2d redCloseLaunchPose = new Pose2d(new Vector2d(-20, 20), Math.toRadians(135));

    public VelConstraint normalTranslationalVelConstraint = new TranslationalVelConstraint(50);
    public VelConstraint slowTranslationalVelConstraint = new TranslationalVelConstraint(20);

    public AutonomousTrajectories(MecanumDrive md) {

        redFarLaunchPickupThirdMark = md.actionBuilder(redFarLaunchPose)
          .setTangent(Math.toRadians(165))
          .splineToSplineHeading(new Pose2d(36, 23.5, Math.toRadians(90)), Math.toRadians(90), normalTranslationalVelConstraint)
          .strafeToConstantHeading(new Vector2d(36, 53), slowTranslationalVelConstraint)
          .setTangent(Math.toRadians(-90))
          .splineToSplineHeading(redFarLaunchPose, Math.toRadians(-15), normalTranslationalVelConstraint)
          .build();

        redFarLaunchPickupHumanPlayerZone = md.actionBuilder(redFarLaunchPose)
          .setTangent(Math.toRadians(-90))
          .splineToSplineHeading(new Pose2d(60, -47.5, Math.toRadians(-90)), Math.toRadians(-90), normalTranslationalVelConstraint)
          .strafeToConstantHeading(new Vector2d(60, -61), slowTranslationalVelConstraint)
          .setTangent(Math.toRadians(90))
          .splineToSplineHeading(redFarLaunchPose, Math.toRadians(90), normalTranslationalVelConstraint)
          .build();

        redFarLaunchToLeaveLaunchZone = md.actionBuilder(redFarLaunchPose)
          .lineToX(43, normalTranslationalVelConstraint)
          .build();

        redCloseStartToCloseLaunch = md.actionBuilder(new Pose2d(-50.5, 50.5, Math.toRadians(-53)))
          .strafeToSplineHeading(redCloseLaunchPose.position, redCloseLaunchPose.heading, normalTranslationalVelConstraint)
          .build();

        redCloseLaunchPickupFirstMark = md.actionBuilder(redCloseLaunchPose)
            .setTangent(Math.toRadians(0))
            .splineToSplineHeading(new Pose2d(new Vector2d(-11.5, 26), Math.toRadians(90)), Math.toRadians(90), normalTranslationalVelConstraint)
            .strafeToConstantHeading(new Vector2d(-11.5, 46), slowTranslationalVelConstraint)
            .setTangent(Math.toRadians(-90))
            .splineToSplineHeading(redCloseLaunchPose, Math.toRadians(-135), normalTranslationalVelConstraint)
            .build();

        redCloseLaunchPickupSecondMark = md.actionBuilder(redCloseLaunchPose)
            .setTangent(Math.toRadians(10))
            .splineToSplineHeading(new Pose2d(new Vector2d(11.5, 30), Math.toRadians(90)), Math.toRadians(90), normalTranslationalVelConstraint)
            .strafeToConstantHeading(new Vector2d(11.5, 46), slowTranslationalVelConstraint)
            .setTangent(Math.toRadians(-135))
            .splineToSplineHeading(redCloseLaunchPose, Math.toRadians(-135), normalTranslationalVelConstraint)
            .build();

        redCloseLaunchToLeaveLaunchZone = md.actionBuilder(redCloseLaunchPose)
            .strafeToConstantHeading(new Vector2d( -10, 30))
            .build();
    }
}
