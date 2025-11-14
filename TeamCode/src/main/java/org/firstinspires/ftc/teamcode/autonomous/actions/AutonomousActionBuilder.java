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

    public static Pose2d redFarLaunchPose = new Pose2d(47, 11.5, Math.toRadians(-114)); // TODO 14nov25 -113
    public static Pose2d redCloseLaunchPose = new Pose2d(new Vector2d(-20, 20), Math.toRadians(-135));
    public static Pose2d redFirstMarkStart = new Pose2d(new Vector2d(-11.5, 20), Math.toRadians(90));
    public static Pose2d redFirstMarkEnd = new Pose2d(new Vector2d(-11.5, 46), Math.toRadians(90));
    public static Pose2d redSecondMarkStart = new Pose2d(new Vector2d(11.5, 20), Math.toRadians(90));
    public static Pose2d redSecondMarkEnd = new Pose2d(new Vector2d(11.5, 46), Math.toRadians(90));
    public static Pose2d redThirdMarkStart = new Pose2d(36, 20, Math.toRadians(90));
    public static Pose2d redThirdMarkEnd = new Pose2d(new Vector2d(36, 62), Math.toRadians(90));

    public static Pose2d blueFarLaunchPose = new Pose2d(47, -11.5, Math.toRadians(-64));
    public static Pose2d blueCloseLaunchPose = new Pose2d(-20, -20, Math.toRadians(-45));
    public static Pose2d blueFirstMarkStart = new Pose2d(new Vector2d(-11.5, -20), Math.toRadians(-90)); // TODO added minus to y (to prevent travel to red side)
    public static Pose2d blueFirstMarkEnd = new Pose2d(new Vector2d(-11.5, -57), Math.toRadians(-90)); // TODO added minus to y
    public static Pose2d blueSecondMarkStart = new Pose2d(new Vector2d(11.5, -20), Math.toRadians(-90));  // TODO made (-y) change UNTESTED
    public static Pose2d blueSecondMarkEnd = new Pose2d(new Vector2d(11.5, -57), Math.toRadians(-90)); // TODO made (-y) change UNTESTED
    public static Pose2d blueThirdMarkStart = new Pose2d(new Vector2d(36, -20), Math.toRadians(-90)); // TODO made (-y) change UNTESTED
    public static Pose2d blueThirdMarkEnd = new Pose2d(new Vector2d(36, -62), Math.toRadians(-90)); // TODO made (-y) change UNTESTED

    public VelConstraint normalTranslationalVelConstraint = new TranslationalVelConstraint(30);
    public VelConstraint slowTranslationalVelConstraint = new TranslationalVelConstraint(10);

    Robot robot;

    public AutonomousActionBuilder(MecanumDrive md, Robot robot) {

        this.robot = robot;

        redFarStartToFarLaunch = md.actionBuilder(new Pose2d(new Vector2d(61, 11.75), Math.toRadians(-90)))
            .strafeToLinearHeading(redFarLaunchPose.position, redFarLaunchPose.heading, normalTranslationalVelConstraint)
            .build();

        redFarLaunchPickupThirdMark = md.actionBuilder(redFarLaunchPose)
          .setTangent(Math.toRadians(165))
          .splineToSplineHeading(redThirdMarkStart, Math.toRadians(90), normalTranslationalVelConstraint)
          .strafeToConstantHeading(redThirdMarkEnd.position, slowTranslationalVelConstraint)
          .setTangent(Math.toRadians(-90))
          .splineToSplineHeading(redFarLaunchPose, Math.toRadians(-15), normalTranslationalVelConstraint)
          .build();

        redFarLaunchToLeaveLaunchZone = md.actionBuilder(redFarLaunchPose)
            .strafeTo(new Vector2d(47.5, 23.5), normalTranslationalVelConstraint)
            .build();



        redCloseStartToAprilTagRead = md.actionBuilder(new Pose2d(-50.5, 50.5, Math.toRadians(37)))
            .strafeToLinearHeading(new Vector2d(-35, 35), Math.toRadians(-45), normalTranslationalVelConstraint)
            .build();

        redAprilTagReadToCloseLaunch = md.actionBuilder(new Pose2d(new Vector2d(-35, 35), Math.toRadians(-45)))
            .strafeToLinearHeading(redCloseLaunchPose.position, redCloseLaunchPose.heading, normalTranslationalVelConstraint)
            .build();

        redCloseLaunchPickupFirstMark = md.actionBuilder(redCloseLaunchPose)
            .setTangent(Math.toRadians(0))
            .splineToLinearHeading(redFirstMarkStart, Math.toRadians(90), normalTranslationalVelConstraint)
            .strafeToConstantHeading(redFirstMarkEnd.position, slowTranslationalVelConstraint)
            .setTangent(Math.toRadians(-90))
            .splineToSplineHeading(redCloseLaunchPose, Math.toRadians(-135), normalTranslationalVelConstraint)
            .build();

        redCloseLaunchPickupSecondMark = md.actionBuilder(redCloseLaunchPose)
            .setTangent(Math.toRadians(10))
            .splineToSplineHeading(redSecondMarkStart, Math.toRadians(90), normalTranslationalVelConstraint)
            .strafeToConstantHeading(redSecondMarkEnd.position, slowTranslationalVelConstraint)
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
            .splineToSplineHeading(blueThirdMarkStart, Math.toRadians(-90), normalTranslationalVelConstraint)
            .strafeToConstantHeading(blueThirdMarkEnd.position, slowTranslationalVelConstraint)
            .setTangent(Math.toRadians(90))
            .splineToSplineHeading(blueFarLaunchPose, Math.toRadians(15), normalTranslationalVelConstraint)
            .build();

        blueFarLaunchToLeaveLaunchZone = md.actionBuilder(blueFarLaunchPose)
            .strafeTo(new Vector2d(47.5, -23.5), normalTranslationalVelConstraint)
            .build();


        blueCloseStartToAprilTagRead = md.actionBuilder(new Pose2d(-50.5, -50.5, Math.toRadians(143)))
            .strafeToSplineHeading(new Vector2d(-35, -35), Math.toRadians(-135), normalTranslationalVelConstraint)  // TODO april tag view angle may need tweak
            .build();

        blueAprilTagReadToCloseLaunch = md.actionBuilder(new Pose2d(new Vector2d(-35, -35), Math.toRadians(-135)))
            .strafeToSplineHeading(blueCloseLaunchPose.position, blueCloseLaunchPose.heading, normalTranslationalVelConstraint)
            .build();

        blueCloseLaunchPickupFirstMark = md.actionBuilder(blueCloseLaunchPose)
            .setTangent(Math.toRadians(0))
            .splineToSplineHeading(blueFirstMarkStart, Math.toRadians(-90), normalTranslationalVelConstraint)
            .strafeToConstantHeading(blueFirstMarkEnd.position, slowTranslationalVelConstraint)
            .setTangent(Math.toRadians(90))
            .splineToSplineHeading(blueCloseLaunchPose, Math.toRadians(135), normalTranslationalVelConstraint)
            .build();

        blueCloseLaunchPickupSecondMark = md.actionBuilder(blueCloseLaunchPose)
            .setTangent(Math.toRadians(-10))
            .splineToSplineHeading(blueSecondMarkStart, Math.toRadians(-90), normalTranslationalVelConstraint)
            .strafeToConstantHeading(blueSecondMarkEnd.position, slowTranslationalVelConstraint)
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
            () -> robot.getLauncher().getSpinLauncherAction(1600, 0.82),    // 0
            () -> robot.getLauncher().getStopLauncherAction(),                            // 1
            () -> robot.getIndexer().getGoToFirstBallAction(),                            // 2
            () -> robot.getIndexer().getGoToSecondBallAction(),                           // 3
            () -> robot.getIndexer().getGoToThirdBallAction(),                          // 4
            () -> robot.getLauncher().getKickBallAction(),                              // 5
            () -> robot.getIntake().getStartIntakeAction(),                             // 6
            () -> robot.getIntake().getStopIntakeAction(),                              // 7
            () -> robot.getLauncher().getResetKickerAction(),                           // 8
            () -> robot.getLauncher().getSpinLauncherAction(1600, 0.70),                // 9
            () -> robot.getIndexer().getWaitUntilBallInIndexerAction(4),                // 10
            () -> robot.getIndexer().getWaitUntilBallInIndexerAction(1.5)               // 11
        };
    }
}
