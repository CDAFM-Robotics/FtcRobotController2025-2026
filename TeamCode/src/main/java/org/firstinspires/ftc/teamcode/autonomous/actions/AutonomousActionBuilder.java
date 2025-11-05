package org.firstinspires.ftc.teamcode.autonomous.actions;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;

import org.firstinspires.ftc.teamcode.common.Robot;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

import java.util.function.Supplier;

public class AutonomousActionBuilder {

    public Action redFarStartToFarLaunch;
    public Action redFarLaunchPickupThirdMark;
    public Action redFarLaunchToLeaveLaunchZone;

    public Action redCloseStartToAprilTagRead;
    public Action redAprilTagReadToCloseLaunch;
    public Action redCloseLaunchPickupFirstMark;
    public Action redCloseLaunchPickupSecondMark;
    public Action redCloseLaunchToLeaveLaunchZone;

    public Action blueFarStartToFarLaunch;
    public Action blueFarLaunchPickupThirdMark;
    public Action blueFarLaunchToLeaveLaunchZone;

    public Action blueCloseStartToAprilTagRead;
    public Action blueAprilTagReadToCloseLaunch;
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
    public Action aprilTagAction;

    public static Pose2d redFarLaunchPose = new Pose2d(47, 11.5, Math.toRadians(-113));
    public static Pose2d redCloseLaunchPose = new Pose2d(new Vector2d(-20, 20), Math.toRadians(-135));

    public Pose2d blueFarLaunchPose = new Pose2d(47, -11.5, Math.toRadians(-64));
    public Pose2d blueCloseLaunchPose = new Pose2d(-20, -20, Math.toRadians(-45));

    public VelConstraint normalTranslationalVelConstraint = new TranslationalVelConstraint(15);
    public VelConstraint slowTranslationalVelConstraint = new TranslationalVelConstraint(10);

    Robot robot;

    public AutonomousActionBuilder(MecanumDrive md, Robot robot) {

        this.robot = robot;

        redFarStartToFarLaunch = md.actionBuilder(new Pose2d(new Vector2d(61, 11.75), Math.toRadians(-90)))
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
            .strafeTo(new Vector2d(47.5, 23.5), normalTranslationalVelConstraint)
            .build();



        redCloseStartToAprilTagRead = md.actionBuilder(new Pose2d(-50.5, 50.5, Math.toRadians(37)))
            .strafeToSplineHeading(new Vector2d(-35, 35), Math.toRadians(-45), normalTranslationalVelConstraint)
            .build();

        redAprilTagReadToCloseLaunch = md.actionBuilder(new Pose2d(new Vector2d(-35, 35), Math.toRadians(-45)))
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


        blueFarStartToFarLaunch = md.actionBuilder(new Pose2d(new Vector2d(61, -11.75), Math.toRadians(-90)))
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
            .strafeTo(new Vector2d(47.5, -23.5), normalTranslationalVelConstraint)
            .build();


        blueCloseStartToAprilTagRead = md.actionBuilder(new Pose2d(-50.5, -50.5, Math.toRadians(143)))
            .strafeToSplineHeading(new Vector2d(-35, -35), Math.toRadians(-135), normalTranslationalVelConstraint)
            .build();

        blueAprilTagReadToCloseLaunch = md.actionBuilder(new Pose2d(new Vector2d(-35, -35), Math.toRadians(-135)))
            .strafeToSplineHeading(blueCloseLaunchPose.position, blueCloseLaunchPose.heading, normalTranslationalVelConstraint)
            .build();

        blueCloseLaunchPickupFirstMark = md.actionBuilder(blueCloseLaunchPose)
            .setTangent(Math.toRadians(0))
            .splineToSplineHeading(new Pose2d(new Vector2d(-11.5, -26), Math.toRadians(-90)), Math.toRadians(-90), normalTranslationalVelConstraint)
            .strafeToConstantHeading(new Vector2d(-11.5, -46), slowTranslationalVelConstraint)
            .setTangent(Math.toRadians(90))
            .splineToSplineHeading(blueCloseLaunchPose, Math.toRadians(135), normalTranslationalVelConstraint)
            .build();

        blueCloseLaunchPickupSecondMark = md.actionBuilder(blueCloseLaunchPose)
            .setTangent(Math.toRadians(-10))
            .splineToSplineHeading(new Pose2d(new Vector2d(11.5, -30), Math.toRadians(-90)), Math.toRadians(-90), normalTranslationalVelConstraint)
            .strafeToConstantHeading(new Vector2d(11.5, -46), slowTranslationalVelConstraint)
            .setTangent(Math.toRadians(135))
            .splineToSplineHeading(blueCloseLaunchPose, Math.toRadians(135), normalTranslationalVelConstraint)
            .build();

        blueCloseLaunchToLeaveLaunchZone = md.actionBuilder(blueCloseLaunchPose)
            .strafeToConstantHeading(new Vector2d( -10, -30))
            .build();


        // Non-driving actions

        spinUpAction = robot.getLauncher().getSpinLauncherAction(1600);
        stopSpinUpAction = robot.getLauncher().getStopLauncherAction();

        goToFirstBallAction = robot.getIndexer().getGoToFirstBallAction();
        goToSecondBallAction = robot.getIndexer().getGoToSecondBallAction();
        goToThirdBallAction = robot.getIndexer().getGoToThirdBallAction();

        launchBallAction = robot.getLauncher().getKickBallAction();
        resetKickerAction = robot.getLauncher().getResetKickerAction();

        startIntakeAction = robot.getIntake().getStartIntakeAction();
        stopIntakeAction = robot.getIntake().getStopIntakeAction();


        aprilTagAction = robot.getLauncher().getAprilTagAction();
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
            redCloseStartToAprilTagRead,
            redAprilTagReadToCloseLaunch,
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
            blueCloseStartToAprilTagRead,
            blueAprilTagReadToCloseLaunch,
            blueCloseLaunchPickupFirstMark,
            blueCloseLaunchPickupSecondMark,
            blueCloseLaunchToLeaveLaunchZone
        };
    }

    public Supplier[] getOtherActions() {
        return new Supplier[] {
            () -> robot.getLauncher().getSpinLauncherAction(1600, 1),
            () -> robot.getLauncher().getStopLauncherAction(),
            () -> robot.getIndexer().getGoToFirstBallAction(),
            () -> robot.getIndexer().getGoToSecondBallAction(),
            () -> robot.getIndexer().getGoToThirdBallAction(),
            () -> robot.getLauncher().getKickBallAction(),
            () -> robot.getIntake().getStartIntakeAction(),
            () -> robot.getIntake().getStopIntakeAction(),
            () -> robot.getLauncher().getResetKickerAction(),
            () -> robot.getLauncher().getAprilTagAction(),
            () -> robot.getLauncher().getSpinLauncherAction(1500, 0.85)
        };
    }
}
