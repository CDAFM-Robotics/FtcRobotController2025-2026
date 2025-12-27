package org.firstinspires.ftc.teamcode.autonomous.actions;

import com.acmerobotics.roadrunner.AccelConstraint;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.TurnConstraints;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;

import org.firstinspires.ftc.teamcode.common.Robot;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

import java.util.function.Supplier;

public class AutonomousActionBuilder {

    public Action redFarStartToFarLaunch;
    public Action redFarLaunchPickupThirdMark;
    public Action redFarLaunchPickupSecondMark;
    public Action redFarLaunchPickupFirstMark;
    public Action redFarLaunchToLeaveLaunchZone;

    public Action redCloseStartToAprilTagRead;
    public Action redAprilTagReadToCloseLaunch;
    public Action redCloseLaunchPickupFirstMark;
    public Action redCloseLaunchPickupSecondMark;
    public Action redCloseLaunchToLeaveLaunchZone;

    public Action blueFarStartToFarLaunch;
    public Action blueFarLaunchPickupThirdMark;
    public Action blueFarLaunchPickupSecondMark;
    public Action blueFarLaunchPickupFirstMark;
    public Action blueFarLaunchToLeaveLaunchZone;

    public Action blueCloseStartToAprilTagRead;
    public Action blueAprilTagReadToCloseLaunch;
    public Action blueCloseLaunchPickupFirstMark;
    public Action blueCloseLaunchPickupSecondMark;
    public Action blueCloseLaunchPickupSecondMarkHitGate;
    public Action blueCloseLaunchToLeaveLaunchZone;

    public static Pose2d redFarLaunchPose = new Pose2d(49, 12.5, Math.toRadians(-111.5)); // TODO 14nov25 -113
    public static Pose2d redCloseLaunchPose = new Pose2d(new Vector2d(-20, 20), Math.toRadians(-129));
    public static Pose2d redFirstMarkStart = new Pose2d(new Vector2d(-11.5, 30), Math.toRadians(90));
    public static Pose2d redFirstMarkEnd = new Pose2d(new Vector2d(-11.5, 57), Math.toRadians(90));
    public static Pose2d redSecondMarkStart = new Pose2d(new Vector2d(14, 30), Math.toRadians(90));
    public static Pose2d redSecondMarkEnd = new Pose2d(new Vector2d(14, 62), Math.toRadians(90));
    public static Pose2d redThirdMarkStart = new Pose2d(36, 30, Math.toRadians(90));
    public static Pose2d redThirdMarkEnd = new Pose2d(new Vector2d(36, 62), Math.toRadians(90));

    public static Pose2d blueFarLaunchPose = new Pose2d(49, -12.5, Math.toRadians(-64));
    public static Pose2d blueCloseLaunchPose = new Pose2d(-20, -20, Math.toRadians(-40));
    public static Pose2d blueFirstMarkStart = new Pose2d(new Vector2d(-11.5, -30), Math.toRadians(-90));
    public static Pose2d blueFirstMarkEnd = new Pose2d(new Vector2d(-11.5, -56), Math.toRadians(-90));
    public static Pose2d blueSecondMarkStart = new Pose2d(new Vector2d(14, -30), Math.toRadians(-90));
    public static Pose2d blueSecondMarkEnd = new Pose2d(new Vector2d(14, -62), Math.toRadians(-90));
    public static Pose2d blueThirdMarkStart = new Pose2d(36, -30, Math.toRadians(-90));
    public static Pose2d blueThirdMarkEnd = new Pose2d(new Vector2d(36, -62), Math.toRadians(-90));

    public static VelConstraint normalTranslationalVelConstraint = new TranslationalVelConstraint(40);
    public static VelConstraint slowTranslationalVelConstraint = new TranslationalVelConstraint(25);

    public static AccelConstraint lowAccelConstraint = new ProfileAccelConstraint(-10, 15);

    public static TurnConstraints turnConstraints = new TurnConstraints(Math.PI, -Math.PI, Math.PI);

    Robot robot;

    public AutonomousActionBuilder(MecanumDrive md, Robot robot) {

        this.robot = robot;

        redFarStartToFarLaunch = md.actionBuilder(new Pose2d(new Vector2d(61, 11.75), Math.toRadians(-90)))
            .strafeToLinearHeading(redFarLaunchPose.position, redFarLaunchPose.heading, normalTranslationalVelConstraint)
            .build();

        redFarLaunchPickupThirdMark = md.actionBuilder(redFarLaunchPose)
          .setTangent(Math.toRadians(165))
          .splineToSplineHeading(redThirdMarkStart, Math.toRadians(90), normalTranslationalVelConstraint, lowAccelConstraint)
          .strafeToConstantHeading(redThirdMarkEnd.position, slowTranslationalVelConstraint)
          .setTangent(Math.toRadians(-90))
          .splineToSplineHeading(redFarLaunchPose, Math.toRadians(-15), normalTranslationalVelConstraint)
          .build();

        redFarLaunchPickupSecondMark = md.actionBuilder(redFarLaunchPose)
            .setTangent(Math.toRadians(180))
            .splineToSplineHeading(redSecondMarkStart, Math.toRadians(90), normalTranslationalVelConstraint, lowAccelConstraint)
            .strafeToConstantHeading(redSecondMarkEnd.position, slowTranslationalVelConstraint)
            .setTangent(Math.toRadians(-90))
            .splineToSplineHeading(redFarLaunchPose, Math.toRadians(-15), normalTranslationalVelConstraint)
            .build();

        redFarLaunchPickupFirstMark = md.actionBuilder(redFarLaunchPose)
            .setTangent(Math.toRadians(180))
            .splineToSplineHeading(redFirstMarkStart, Math.toRadians(90), normalTranslationalVelConstraint, lowAccelConstraint)
            .strafeToConstantHeading(redFirstMarkEnd.position, slowTranslationalVelConstraint)
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
            .turn(Math.toRadians(-141), turnConstraints)
            .setTangent(Math.toRadians(0))
            .splineToConstantHeading(redFirstMarkStart.position, Math.toRadians(90), normalTranslationalVelConstraint, lowAccelConstraint)
            .strafeToConstantHeading(redFirstMarkEnd.position, slowTranslationalVelConstraint)
            .setTangent(Math.toRadians(-90))
            .splineToSplineHeading(redCloseLaunchPose, Math.toRadians(-135), normalTranslationalVelConstraint)
            .build();

        redCloseLaunchPickupSecondMark = md.actionBuilder(redCloseLaunchPose)
            .turn(Math.toRadians(-141), turnConstraints)
            .setTangent(Math.toRadians(0))
            .splineToConstantHeading(redSecondMarkStart.position, Math.toRadians(90), normalTranslationalVelConstraint, lowAccelConstraint)
            .strafeToConstantHeading(redSecondMarkEnd.position, slowTranslationalVelConstraint)
            .setTangent(Math.toRadians(-90))
            .splineToSplineHeading(redCloseLaunchPose, Math.toRadians(180), normalTranslationalVelConstraint)
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
            .setTangent(Math.toRadians(-90))
            .splineToSplineHeading(blueFarLaunchPose, Math.toRadians(15), normalTranslationalVelConstraint)
            .build();

        blueFarLaunchPickupSecondMark = md.actionBuilder(blueFarLaunchPose)
            .setTangent(Math.toRadians(-165))
            .splineToSplineHeading(blueSecondMarkStart, Math.toRadians(-90), normalTranslationalVelConstraint, lowAccelConstraint)
            .strafeToConstantHeading(blueSecondMarkEnd.position, slowTranslationalVelConstraint)
            .setTangent(Math.toRadians(90))
            .splineToSplineHeading(blueFarLaunchPose, Math.toRadians(15), normalTranslationalVelConstraint)
            .build();

        blueFarLaunchPickupFirstMark = md.actionBuilder(blueFarLaunchPose)
            .setTangent(Math.toRadians(-165))
            .splineToSplineHeading(blueFirstMarkStart, Math.toRadians(-90), normalTranslationalVelConstraint)
            .strafeToConstantHeading(blueFirstMarkEnd.position, slowTranslationalVelConstraint)
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
            .splineToSplineHeading(blueFirstMarkStart, Math.toRadians(-90), normalTranslationalVelConstraint, lowAccelConstraint)
            .strafeToConstantHeading(blueFirstMarkEnd.position, slowTranslationalVelConstraint)
            .setTangent(Math.toRadians(90))
            .splineToSplineHeading(blueCloseLaunchPose, Math.toRadians(135), normalTranslationalVelConstraint)
            .build();

        blueCloseLaunchPickupSecondMark = md.actionBuilder(blueCloseLaunchPose)
            .setTangent(Math.toRadians(-10))
            .splineToSplineHeading(blueSecondMarkStart, Math.toRadians(-90), normalTranslationalVelConstraint, lowAccelConstraint)
            .strafeToConstantHeading(blueSecondMarkEnd.position, slowTranslationalVelConstraint)
            .setTangent(Math.toRadians(90))
            .splineToSplineHeading(new Pose2d(11.5, -46, Math.toRadians(180)), Math.toRadians(90), normalTranslationalVelConstraint)
            .splineToSplineHeading(new Pose2d(2, -56, Math.toRadians(180)), Math.toRadians(-90), normalTranslationalVelConstraint)
            .setTangent(Math.toRadians(90))
            .splineToSplineHeading(blueCloseLaunchPose, Math.toRadians(180), normalTranslationalVelConstraint)
            .build();

//        blueCloseLaunchPickupSecondMark = md.actionBuilder(blueCloseLaunchPose)
//            .setTangent(Math.toRadians(-10))
//            .splineToSplineHeading(blueSecondMarkStart, Math.toRadians(-90), normalTranslationalVelConstraint, lowAccelConstraint)
//            .strafeToConstantHeading(blueSecondMarkEnd.position, slowTranslationalVelConstraint)
//            .setTangent(Math.toRadians(90))
//            .splineToSplineHeading(blueCloseLaunchPose, Math.toRadians(180), normalTranslationalVelConstraint)
//            .build();

        blueCloseLaunchPickupSecondMarkHitGate = md.actionBuilder(blueCloseLaunchPose)
            .setTangent(Math.toRadians(-10))
            .splineToSplineHeading(blueSecondMarkStart, Math.toRadians(-90), normalTranslationalVelConstraint, lowAccelConstraint)
            .strafeToConstantHeading(blueSecondMarkEnd.position, slowTranslationalVelConstraint)
            .setTangent(Math.toRadians(90))
            .splineToSplineHeading(new Pose2d(11.5, -46, Math.toRadians(180)), Math.toRadians(90), normalTranslationalVelConstraint)
            .splineToSplineHeading(new Pose2d(2, -56, Math.toRadians(180)), Math.toRadians(-90), normalTranslationalVelConstraint)
            .setTangent(Math.toRadians(90))
            .splineToSplineHeading(blueCloseLaunchPose, Math.toRadians(180), normalTranslationalVelConstraint)
            .build();

        blueCloseLaunchToLeaveLaunchZone = md.actionBuilder(blueCloseLaunchPose)
            .strafeToConstantHeading(new Vector2d( -10, -30))
            .build();
    }

    public Action[] getRedFarTrajectories() {
        return new Action[] {
            redFarStartToFarLaunch,
            redFarLaunchPickupThirdMark,
            redFarLaunchPickupSecondMark,
            redFarLaunchPickupFirstMark,
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
            blueFarLaunchPickupSecondMark,
            blueFarLaunchPickupFirstMark,
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

    public Action getSpinLauncherFar() {
        return robot.getLauncher().getSpinLauncherAction(1360);
    }

    public Action getSpinLauncherClose() {
        return robot.getLauncher().getSpinLauncherAction(1200);
    }

    public Action getLauncherIsReadyFar() {
        return robot.getLauncher().getWaitUntilVelocityAction(1340, 1);
    }

    public Action getLauncherIsReadyClose() {
        return robot.getLauncher().getWaitUntilVelocityAction(1160, 1);
    }

    public Action getStopLauncher() {
        return robot.getLauncher().getStopLauncherAction();
    }

    public Action getIndexAction(int indexPos) {
        return indexPos == 0 ? robot.getIndexer().getGoToZeroBallAction() : (indexPos == 1 ? robot.getIndexer().getGoToOneBallAction() : robot.getIndexer().getGoToTwoBallAction());
    }

    public Action getKickBall() {
        return robot.getLauncher().getKickBallAction();
    }

    public Action getResetKicker() {
        return robot.getLauncher().getResetKickerAction();
    }

    public Action getStartIntake() {
        return robot.getIntake().getStartIntakeAction();
    }

    public Action getStopIntake() {
        return robot.getIntake().getStopIntakeAction();
    }

    public Action getWaitUntilBallInIndexer(double timeout) {
        return robot.getIndexer().getWaitUntilBallInIndexerAction(timeout);
    }
}
