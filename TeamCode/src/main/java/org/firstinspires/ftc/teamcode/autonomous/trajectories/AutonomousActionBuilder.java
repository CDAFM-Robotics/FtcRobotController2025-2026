package org.firstinspires.ftc.teamcode.autonomous.trajectories;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;

import org.firstinspires.ftc.teamcode.common.Robot;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

public class AutonomousActionBuilder {

    public Action redFarStartToFarLaunch;
    public Action redFarLaunchPickupThirdMark;
    public Action redFarLaunchToLeaveLaunchZone;

    public Action redCloseStartToCloseLaunch;
    public Action redCloseLaunchPickupFirstMark;
    public Action redCloseLaunchPickupSecondMark;
    public Action redCloseLaunchToLeaveLaunchZone;

    public Action blueFarStartToFarLaunch;
    public Action blueFarLaunchPickupThirdMark;
    public Action blueFarLaunchToLeaveLaunchZone;

    public Action blueCloseStartToCloseLaunch;
    public Action blueCloseLaunchPickupFirstMark;
    public Action blueCloseLaunchPickupSecondMark;
    public Action blueCloseLaunchToLeaveLaunchZone;

    public Action spinUpAction;
    public Action stopSpinUpAction;
    public Action goToFirstBallAction;
    public Action goToSecondBallAction;
    public Action goToThirdBallAction;
    public Action launchBallAction;
    public Action startIntakeAction;
    public Action stopIntakeAction;
    public Action resetKickerAction;

    public Pose2d redFarLaunchPose = new Pose2d(57.7, 12.5, Math.toRadians(165));
    public Pose2d redCloseLaunchPose = new Pose2d(new Vector2d(-20, 20), Math.toRadians(135));

    public Pose2d blueFarLaunchPose = new Pose2d(57.7, -12.5, Math.toRadians(165));
    public Pose2d blueCloseLaunchPose = new Pose2d(new Vector2d(-20, -20), Math.toRadians(-135));

    public VelConstraint normalTranslationalVelConstraint = new TranslationalVelConstraint(20);
    public VelConstraint slowTranslationalVelConstraint = new TranslationalVelConstraint(10);

    public AutonomousActionBuilder(MecanumDrive md, Robot robot) {

        redFarStartToFarLaunch = md.actionBuilder(new Pose2d(new Vector2d(61, 11.5), Math.toRadians(180)))
            .strafeToLinearHeading(redFarLaunchPose.position, redFarLaunchPose.heading, normalTranslationalVelConstraint)
            .build();

        redFarLaunchPickupThirdMark = md.actionBuilder(redFarLaunchPose)
          .setTangent(Math.toRadians(165))
          .splineToSplineHeading(new Pose2d(36, 23.5, Math.toRadians(90)), Math.toRadians(90), normalTranslationalVelConstraint)
          .strafeToConstantHeading(new Vector2d(36, 53), slowTranslationalVelConstraint)
          .setTangent(Math.toRadians(-90))
          .splineToSplineHeading(redFarLaunchPose, Math.toRadians(-15), normalTranslationalVelConstraint)
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

        blueFarStartToFarLaunch = md.actionBuilder(new Pose2d(new Vector2d(61, -11.5), Math.toRadians(180)))
            .strafeToLinearHeading(blueFarLaunchPose.position, blueFarLaunchPose.heading, normalTranslationalVelConstraint)
            .build();

        blueFarLaunchPickupThirdMark = md.actionBuilder(blueFarLaunchPose)
            .setTangent(Math.toRadians(-165))
            .splineToSplineHeading(new Pose2d(36, -23.5, Math.toRadians(-90)), Math.toRadians(-90), normalTranslationalVelConstraint)
            .strafeToConstantHeading(new Vector2d(36, -53), slowTranslationalVelConstraint)
            .setTangent(Math.toRadians(90))
            .splineToSplineHeading(blueFarLaunchPose, Math.toRadians(15), normalTranslationalVelConstraint)
            .build();

        blueFarLaunchToLeaveLaunchZone = md.actionBuilder(blueFarLaunchPose)
            .lineToX(43, normalTranslationalVelConstraint)
            .build();

        blueCloseStartToCloseLaunch = md.actionBuilder(new Pose2d(-50.5, -50.5, Math.toRadians(53)))
            .strafeToSplineHeading(blueCloseLaunchPose.position, blueCloseLaunchPose.heading, normalTranslationalVelConstraint)
            .build();

        blueCloseLaunchPickupFirstMark = md.actionBuilder(blueCloseLaunchPose)
            .setTangent(Math.toRadians(0))
            .splineToSplineHeading(new Pose2d(new Vector2d(-11.5, 26), Math.toRadians(90)), Math.toRadians(90), normalTranslationalVelConstraint)
            .strafeToConstantHeading(new Vector2d(-11.5, 46), slowTranslationalVelConstraint)
            .setTangent(Math.toRadians(-90))
            .splineToSplineHeading(blueCloseLaunchPose, Math.toRadians(-135), normalTranslationalVelConstraint)
            .build();

        blueCloseLaunchPickupSecondMark = md.actionBuilder(blueCloseLaunchPose)
            .setTangent(Math.toRadians(10))
            .splineToSplineHeading(new Pose2d(new Vector2d(11.5, 30), Math.toRadians(90)), Math.toRadians(90), normalTranslationalVelConstraint)
            .strafeToConstantHeading(new Vector2d(11.5, 46), slowTranslationalVelConstraint)
            .setTangent(Math.toRadians(-135))
            .splineToSplineHeading(blueCloseLaunchPose, Math.toRadians(-135), normalTranslationalVelConstraint)
            .build();

        blueCloseLaunchToLeaveLaunchZone = md.actionBuilder(blueCloseLaunchPose)
            .strafeToConstantHeading(new Vector2d( -10, 30))
            .build();


        // Non-driving actions

        spinUpAction = robot.getLauncher().getSpinLauncherAction(1000);
        stopSpinUpAction = robot.getLauncher().getStopLauncherAction();

        goToFirstBallAction = robot.getIndexer().getGoToFirstBallAction();
        goToSecondBallAction = robot.getIndexer().getGoToSecondBallAction();
        goToThirdBallAction = robot.getIndexer().getGoToThirdBallAction();

        launchBallAction = robot.getLauncher().getKickBallAction();
        resetKickerAction = robot.getLauncher().getResetKickerAction();

        startIntakeAction = robot.getIntake().getStartIntakeAction();
        stopIntakeAction = robot.getIntake().getStopIntakeAction();
    }

    public Action[] getRedFarTrajectories() {
        return new Action[] {
            redFarStartToFarLaunch,
            redFarLaunchPickupThirdMark,
            redFarLaunchToLeaveLaunchZone
        };
    }

    public Action[] getRedCloseTrajectories() {
        return new Action[] {
            redCloseStartToCloseLaunch,
            redCloseLaunchPickupFirstMark,
            redCloseLaunchPickupSecondMark,
            redCloseLaunchToLeaveLaunchZone
        };
    }

    public Action[] getBlueFarTrajectories() {
        return new Action[] {
            blueFarStartToFarLaunch,
            blueFarLaunchPickupThirdMark,
            blueFarLaunchToLeaveLaunchZone
        };
    }

    public Action[] getBlueCloseTrajectories() {
        return new Action[] {
            blueCloseStartToCloseLaunch,
            blueCloseLaunchPickupFirstMark,
            blueCloseLaunchPickupSecondMark,
            blueCloseLaunchToLeaveLaunchZone
        };
    }

    public Action[] getOtherActions() {
        return new Action[] {
            spinUpAction,
            stopSpinUpAction,
            goToFirstBallAction,
            goToSecondBallAction,
            goToThirdBallAction,
            launchBallAction,
            startIntakeAction,
            stopIntakeAction,
            resetKickerAction,
        };
    }
}
