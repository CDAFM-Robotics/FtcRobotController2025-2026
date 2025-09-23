package org.firstinspires.ftc.teamcode.autonomous.trajectories;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;

import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

public class AutonomousTrajectories {
    public Action redFarSidePickupThirdMark = null;
    public Action redFarSidePickupHumanPlayerZone = null;
    public Action redFarSideLeaveLaunchZone = null;
    public AutonomousTrajectories(MecanumDrive md) {
        redFarSidePickupThirdMark = md.actionBuilder(new Pose2d(57.7, -12.5, Math.toRadians(-165)))
          .setTangent(Math.toRadians(-165))
          .splineToSplineHeading(new Pose2d(36, -23.5, Math.toRadians(-90)), Math.toRadians(-90), new TranslationalVelConstraint(50))
          .strafeToConstantHeading(new Vector2d(36, -53), new TranslationalVelConstraint(20))
          .setTangent(Math.toRadians(90))
          .splineToSplineHeading(new Pose2d(57.7, -12.5, Math.toRadians(-165)), Math.toRadians(15), new TranslationalVelConstraint(50))
          .build();

        redFarSidePickupHumanPlayerZone = md.actionBuilder(new Pose2d(57.7, -12.5, Math.toRadians(-165)))
          .setTangent(Math.toRadians(-90))
          .splineToSplineHeading(new Pose2d(60, -47.5, Math.toRadians(-90)), Math.toRadians(-90), new TranslationalVelConstraint(50))
          .strafeToConstantHeading(new Vector2d(60, -61), new TranslationalVelConstraint(20))
          .setTangent(Math.toRadians(90))
          .splineToSplineHeading(new Pose2d(57.7, -12.5, Math.toRadians(-165)), Math.toRadians(90), new TranslationalVelConstraint(50))
          .build();

        redFarSideLeaveLaunchZone = md.actionBuilder(new Pose2d(57.7, -12.5, Math.toRadians(-165)))
          .lineToX(43)
          .build();
    }
}
