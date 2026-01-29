package org.firstinspires.ftc.teamcode.autonomous.actions;

import com.acmerobotics.roadrunner.AccelConstraint;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.AngularVelConstraint;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ProfileAccelConstraint;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.TurnConstraints;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;

import org.firstinspires.ftc.teamcode.common.Robot;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

public class AutonomousActionBuilder {

    public Action redFarStartToFarLaunch;
    public Action redFarLaunchPickupThirdMark;
    public Action redFarLaunchPickupSecondMark;
    public Action redFarLaunchPickupFirstMark;
    public Action redFarLaunchToLeaveLaunchZone;
    public Action redFarPickupLoadingZone;

    public Action redCloseStartToAprilTagRead;
    public Action redCloseStartToCloseLaunch;
    public Action redAprilTagReadToCloseLaunch;
    public Action redCloseLaunchPickupFirstMark;
    public Action redCloseLaunchPickupSecondMark;
    public Action redCloseLaunchToLeaveLaunchZone;
    public Action redCloseLaunchPickupSecondMarkHitGate;
    public Action redCloseLaunchTurnToAprilTag;
    public Action redCloseLaunchPickupThirdMark;

    public Action blueFarStartToFarLaunch;
    public Action blueFarLaunchPickupThirdMark;
    public Action blueFarLaunchPickupSecondMark;
    public Action blueFarLaunchPickupFirstMark;
    public Action blueFarLaunchToLeaveLaunchZone;
    public Action blueFarPickupLoadingZone;

    public Action blueCloseStartToAprilTagRead;
    public Action blueCloseStartToCloseLaunch;
    public Action blueAprilTagReadToCloseLaunch;
    public Action blueCloseLaunchPickupFirstMark;
    public Action blueCloseLaunchPickupSecondMark;
    public Action blueCloseLaunchPickupSecondMarkHitGate;
    public Action blueCloseLaunchPickupThirdMark;
    public Action blueCloseLaunchToLeaveLaunchZone;

    public static Pose2d redFarLaunchPose = new Pose2d(49, 12.5, Math.toRadians(-110.5)); // TODO 14nov25 -113
    public static Pose2d redCloseLaunchPose = new Pose2d(new Vector2d(-20, 20), Math.toRadians(-129));
    public static Pose2d redFirstMarkStart = new Pose2d(new Vector2d(-11.5, 30), Math.toRadians(90));
    public static Pose2d redFirstMarkEnd = new Pose2d(new Vector2d(-11.5, 54), Math.toRadians(90));
    public static Pose2d redSecondMarkStart = new Pose2d(new Vector2d(13.5, 30), Math.toRadians(90));
    public static Pose2d redSecondMarkEnd = new Pose2d(new Vector2d(13.5, 58), Math.toRadians(90));
    public static Pose2d redSecondMarkStartClose = new Pose2d(new Vector2d(12.5, 30), Math.toRadians(90));
    public static Pose2d redSecondMarkEndClose = new Pose2d(new Vector2d(13.5, 58), Math.toRadians(90));
    public static Pose2d redThirdMarkStart = new Pose2d(36, 28, Math.toRadians(90));
    public static Pose2d redThirdMarkEnd = new Pose2d(new Vector2d(36, 57), Math.toRadians(90));
    public static Pose2d redThirdMarkEndClose = new Pose2d(new Vector2d(36, 60), Math.toRadians(90));
    public static Pose2d redLoadingZoneStart = new Pose2d(58, 40, Math.toRadians(120));
    public static Pose2d redLoadingZoneEnd = new Pose2d(58, 62, Math.toRadians(80));

    public static Pose2d blueFarLaunchPose = new Pose2d(49, -12.5, Math.toRadians(-64));
    public static Pose2d blueCloseLaunchPose = new Pose2d(-20, -20, Math.toRadians(-40));
    public static Pose2d blueFirstMarkStart = new Pose2d(new Vector2d(-11.5, -30), Math.toRadians(-90));
    public static Pose2d blueFirstMarkEnd = new Pose2d(new Vector2d(-11.5, -56), Math.toRadians(-90)); // -54 25jan // 53 -> 50 // goes 3 inches less far
    public static Pose2d blueSecondMarkStartClose = new Pose2d(new Vector2d(12.5, -30), Math.toRadians(-90));
    public static Pose2d blueSecondMarkEndClose = new Pose2d(new Vector2d(12.5, -62), Math.toRadians(-90)); // -59 -> -53 // goes 3 inches less far
    public static Pose2d blueSecondMarkStartFar = new Pose2d(new Vector2d(12, -30), Math.toRadians(-90));
    public static Pose2d blueSecondMarkEndFar = new Pose2d(new Vector2d(12, -62), Math.toRadians(-90));
    public static Pose2d blueThirdMarkStart = new Pose2d(34, -34, Math.toRadians(-90)); // x:36->34 TODO y: was -30 bluenear 12ball
    public static Pose2d blueThirdMarkEnd = new Pose2d(new Vector2d(36, -63), Math.toRadians(-90)); // 62->63 // 59->54  // goes 3 inches less far
    public static Pose2d blueLoadingZoneStart = new Pose2d(58, -40, Math.toRadians(-90));
    public static Pose2d blueLoadingZoneEnd = new Pose2d(58, -62, Math.toRadians(-90));

    public static VelConstraint normalTranslationalVelConstraint = new TranslationalVelConstraint(62);
    public static VelConstraint slowTranslationalVelConstraint = new TranslationalVelConstraint(35); // was 25 (27Dec25)

    public static AccelConstraint slightLowAccelConstraint = new ProfileAccelConstraint(-40,36);
    public static AccelConstraint lowAccelConstraint = new ProfileAccelConstraint(-20, 30);

    public static VelConstraint angularConstraint = new AngularVelConstraint(5);

    public static TurnConstraints turnConstraints = new TurnConstraints(Math.PI, -Math.PI, Math.PI);

    Robot robot;

    MecanumDrive md;

    public AutonomousActionBuilder(MecanumDrive md, Robot robot) {

        this.robot = robot;
        this.md = md;

        redFarStartToFarLaunch = md.actionBuilder(new Pose2d(new Vector2d(61, 11.75), Math.toRadians(-90)))
            .strafeToLinearHeading(redFarLaunchPose.position, redFarLaunchPose.heading, normalTranslationalVelConstraint)
            .build();
        redFarLaunchPickupThirdMark = md.actionBuilder(redFarLaunchPose)
          .setTangent(Math.toRadians(165))
          .splineToSplineHeading(redThirdMarkStart, Math.toRadians(90), normalTranslationalVelConstraint, lowAccelConstraint)
          .strafeToConstantHeading(redThirdMarkEnd.position, normalTranslationalVelConstraint, lowAccelConstraint)
          .setTangent(Math.toRadians(-90))
          .strafeToLinearHeading(redFarLaunchPose.position, redFarLaunchPose.heading, normalTranslationalVelConstraint)
          .build();
        redFarLaunchPickupSecondMark = md.actionBuilder(redFarLaunchPose)
            .setTangent(Math.toRadians(180))
            .splineToSplineHeading(redSecondMarkStart, Math.toRadians(90), normalTranslationalVelConstraint, lowAccelConstraint)
            .strafeToConstantHeading(redSecondMarkEnd.position, normalTranslationalVelConstraint, lowAccelConstraint)
            .setTangent(Math.toRadians(-90))
            .strafeToLinearHeading(redFarLaunchPose.position, redFarLaunchPose.heading, normalTranslationalVelConstraint)
            .build();
        redFarLaunchPickupFirstMark = md.actionBuilder(redFarLaunchPose)
            .setTangent(Math.toRadians(180))
            .splineToSplineHeading(redFirstMarkStart, Math.toRadians(90), normalTranslationalVelConstraint, lowAccelConstraint)
            .strafeToConstantHeading(redFirstMarkEnd.position, normalTranslationalVelConstraint, lowAccelConstraint)
            .setTangent(Math.toRadians(-90))
            .strafeToLinearHeading(redFarLaunchPose.position, redFarLaunchPose.heading, normalTranslationalVelConstraint)
            .build();
        redFarLaunchToLeaveLaunchZone = md.actionBuilder(redFarLaunchPose)
            .strafeTo(new Vector2d(47.5, 23.5), normalTranslationalVelConstraint)
            .build();
        redFarPickupLoadingZone = md.actionBuilder(redFarLaunchPose).build();

        redCloseStartToAprilTagRead = md.actionBuilder(new Pose2d(-50.5, 50.5, Math.toRadians(37)))
            .strafeToLinearHeading(new Vector2d(-35, 35), Math.toRadians(-45), normalTranslationalVelConstraint)
            .build();
        redAprilTagReadToCloseLaunch = md.actionBuilder(new Pose2d(new Vector2d(-35, 35), Math.toRadians(-45)))
            .strafeToLinearHeading(redCloseLaunchPose.position, redCloseLaunchPose.heading, normalTranslationalVelConstraint)
            .build();
        redCloseLaunchPickupFirstMark = md.actionBuilder(redCloseLaunchPose)
            .turnTo(Math.toRadians(90))
            .setTangent(Math.toRadians(0))
            .splineToConstantHeading(redFirstMarkStart.position, Math.toRadians(90), normalTranslationalVelConstraint, lowAccelConstraint)
            .strafeToConstantHeading(redFirstMarkEnd.position, normalTranslationalVelConstraint)
            .setTangent(Math.toRadians(-90))
            .splineToSplineHeading(redCloseLaunchPose, Math.toRadians(-135), normalTranslationalVelConstraint)
            .build();
        redCloseLaunchPickupSecondMark = md.actionBuilder(redCloseLaunchPose)
            .turnTo(Math.toRadians(90))
            .setTangent(Math.toRadians(0))
            .splineToConstantHeading(redSecondMarkStartClose.position, Math.toRadians(90), normalTranslationalVelConstraint, lowAccelConstraint)
            .strafeToConstantHeading(redSecondMarkEndClose.position, normalTranslationalVelConstraint)
            .setTangent(Math.toRadians(-90))
            .splineToSplineHeading(redCloseLaunchPose, Math.toRadians(180), normalTranslationalVelConstraint)
            .build();
        redCloseLaunchToLeaveLaunchZone = md.actionBuilder(redCloseLaunchPose)
            .strafeToConstantHeading(new Vector2d( -10, 30))
            .build();
        redCloseLaunchPickupSecondMarkHitGate = md.actionBuilder(redCloseLaunchPose)
            .turnTo(Math.toRadians(90))
            .setTangent(Math.toRadians(0))
            .splineToConstantHeading(redSecondMarkStart.position, Math.toRadians(90), normalTranslationalVelConstraint, lowAccelConstraint)
            .strafeToConstantHeading(redSecondMarkEnd.position, normalTranslationalVelConstraint)
            .setTangent(Math.toRadians(-90))
            .splineToSplineHeading(new Pose2d(12.5, 43, Math.toRadians(180)), Math.toRadians(-90), normalTranslationalVelConstraint)
            .splineToSplineHeading(new Pose2d(4, 53.5, Math.toRadians(180)), Math.toRadians(90), normalTranslationalVelConstraint)
            .waitSeconds(0.75)
            .setTangent(Math.toRadians(-65))
            .splineToLinearHeading(redCloseLaunchPose, Math.toRadians(180), normalTranslationalVelConstraint)
            .build();
        redCloseStartToCloseLaunch = md.actionBuilder(new Pose2d(-50.5, 50.5, Math.toRadians(-143)))
            .strafeToLinearHeading(redCloseLaunchPose.position, redCloseLaunchPose.heading, normalTranslationalVelConstraint)
            .build();
        redCloseLaunchTurnToAprilTag = md.actionBuilder(redCloseLaunchPose)
            .turnTo(Math.toRadians(-90))
            .build();
        redCloseLaunchPickupThirdMark = md.actionBuilder(redCloseLaunchPose)
            .strafeToLinearHeading(redThirdMarkStart.position, redThirdMarkStart.heading, normalTranslationalVelConstraint)
            .strafeToConstantHeading(redThirdMarkEndClose.position, normalTranslationalVelConstraint)
            .strafeToLinearHeading(redCloseLaunchPose.position, redCloseLaunchPose.heading, normalTranslationalVelConstraint)
            .build();



        blueFarStartToFarLaunch = md.actionBuilder(new Pose2d(new Vector2d(61, -11.75), Math.toRadians(-90)))
            .strafeToLinearHeading(blueFarLaunchPose.position, blueFarLaunchPose.heading, normalTranslationalVelConstraint)
            .build();
        blueFarLaunchPickupThirdMark = md.actionBuilder(blueFarLaunchPose)
            .setTangent(Math.toRadians(-165))
            .splineToSplineHeading(blueThirdMarkStart, Math.toRadians(-90), normalTranslationalVelConstraint)
            .strafeToConstantHeading(blueThirdMarkEnd.position, normalTranslationalVelConstraint)
            .setTangent(Math.toRadians(90))
            .splineToSplineHeading(blueFarLaunchPose, Math.toRadians(15), normalTranslationalVelConstraint)
            .build();
        blueFarLaunchPickupSecondMark = md.actionBuilder(blueFarLaunchPose)
            .setTangent(Math.toRadians(-165))
            .splineToSplineHeading(blueSecondMarkStartFar, Math.toRadians(-90), normalTranslationalVelConstraint, lowAccelConstraint)
            .strafeToConstantHeading(blueSecondMarkEndFar.position, normalTranslationalVelConstraint)
            .setTangent(Math.toRadians(90))
            .splineToSplineHeading(blueFarLaunchPose, Math.toRadians(15), normalTranslationalVelConstraint)
            .build();
        blueFarLaunchPickupFirstMark = md.actionBuilder(blueFarLaunchPose)
            .setTangent(Math.toRadians(-165))
            .splineToSplineHeading(blueFirstMarkStart, Math.toRadians(-90), normalTranslationalVelConstraint)
            .strafeToConstantHeading(blueFirstMarkEnd.position, normalTranslationalVelConstraint)
            .setTangent(Math.toRadians(90))
            .splineToSplineHeading(blueFarLaunchPose, Math.toRadians(15), normalTranslationalVelConstraint)
            .build();
        blueFarLaunchToLeaveLaunchZone = md.actionBuilder(blueFarLaunchPose)
            .strafeTo(new Vector2d(47.5, -23.5), normalTranslationalVelConstraint)
            .build();
        blueFarPickupLoadingZone = md.actionBuilder(blueFarLaunchPose)
            .strafeToLinearHeading(blueLoadingZoneEnd.position, blueLoadingZoneEnd.heading)
            .turn(Math.toRadians(-40))
            .turn(Math.toRadians(20))
            .strafeToLinearHeading(blueFarLaunchPose.position, blueFarLaunchPose.heading)
            .build();

        blueCloseStartToAprilTagRead = md.actionBuilder(new Pose2d(-50.5, -50.5, Math.toRadians(143)))
            .strafeToLinearHeading(new Vector2d(-35, -35), Math.toRadians(-135), normalTranslationalVelConstraint)  // TODO april tag view angle may need tweak
            .build();
        blueAprilTagReadToCloseLaunch = md.actionBuilder(new Pose2d(new Vector2d(-35, -35), Math.toRadians(-135)))
            .strafeToLinearHeading(blueCloseLaunchPose.position, blueCloseLaunchPose.heading, normalTranslationalVelConstraint)
            .build();
        blueCloseStartToCloseLaunch = md.actionBuilder(new Pose2d(-50.5, -50.5, Math.toRadians(-37)))
            .strafeToLinearHeading(blueCloseLaunchPose.position, blueCloseLaunchPose.heading, normalTranslationalVelConstraint)
            .build();
        blueCloseLaunchPickupFirstMark = md.actionBuilder(blueCloseLaunchPose)
            .setTangent(Math.toRadians(0))
            .splineToSplineHeading(blueFirstMarkStart, Math.toRadians(-90), normalTranslationalVelConstraint, slightLowAccelConstraint)
            .strafeToConstantHeading(blueFirstMarkEnd.position, normalTranslationalVelConstraint)
            .setTangent(Math.toRadians(90))
            .splineToLinearHeading(blueCloseLaunchPose, Math.toRadians(135), normalTranslationalVelConstraint)
            .build();
        blueCloseLaunchPickupSecondMark = md.actionBuilder(blueCloseLaunchPose)
            .setTangent(Math.toRadians(0)) // was -10 prev tod o change to 0?
            .splineToSplineHeading(blueSecondMarkStartClose, Math.toRadians(-90), normalTranslationalVelConstraint, slightLowAccelConstraint)
            .strafeToConstantHeading(blueSecondMarkEndClose.position, normalTranslationalVelConstraint)
            .setTangent(Math.toRadians(90))
            .splineToLinearHeading(blueCloseLaunchPose, Math.toRadians(180), normalTranslationalVelConstraint)
            .build();
        blueCloseLaunchPickupSecondMarkHitGate = md.actionBuilder(blueCloseLaunchPose)
            .setTangent(Math.toRadians(-10))
            .splineToSplineHeading(blueSecondMarkStartClose, Math.toRadians(-90), normalTranslationalVelConstraint, slightLowAccelConstraint)
            .strafeToConstantHeading(blueSecondMarkEndClose.position, normalTranslationalVelConstraint)
            .setTangent(Math.toRadians(90)) //TODO HITTING Gate way too hard -56 -> -52
            .splineToSplineHeading(new Pose2d(12.5, -43, Math.toRadians(180)), Math.toRadians(90), normalTranslationalVelConstraint)
            .splineToSplineHeading(new Pose2d(2, -53.5, Math.toRadians(180)), Math.toRadians(-90), normalTranslationalVelConstraint)
            .waitSeconds(0.75)
            .setTangent(Math.toRadians(90)) // TODO is this causing the misfire? of second set? very strange rotation after gate wait
            .splineToLinearHeading(blueCloseLaunchPose, Math.toRadians(180), normalTranslationalVelConstraint)
            // .waitSeconds(0.5) // TODO not getting off mark at end
            .build();
        blueCloseLaunchPickupThirdMark = md.actionBuilder(blueCloseLaunchPose)
            .setTangent(Math.toRadians(0))
            .strafeToLinearHeading(blueThirdMarkStart.position, blueThirdMarkStart.heading, normalTranslationalVelConstraint) // TODO 25jan low -> slightlow
            .strafeToConstantHeading(blueThirdMarkEnd.position, normalTranslationalVelConstraint) // TODO 25Jan normalTranslation
            .setTangent(Math.toRadians(90))
            .splineToLinearHeading(blueCloseLaunchPose, Math.toRadians(180), normalTranslationalVelConstraint) // strafe?
            .build();
        blueCloseLaunchToLeaveLaunchZone = md.actionBuilder(blueCloseLaunchPose)
            .strafeToConstantHeading(new Vector2d(-10, -30))
            .build();
    }

    public Action[] getRedFarTrajectories() {
        return new Action[] {
            redFarStartToFarLaunch,
            redFarLaunchPickupThirdMark,
            redFarLaunchPickupSecondMark,
            redFarLaunchPickupFirstMark,
            redFarLaunchToLeaveLaunchZone,
            redFarPickupLoadingZone
        };
    }

    public Action getRedFarPickupLoadingZone() {
        return md.actionBuilder(redFarLaunchPose)
            .strafeToLinearHeading(redLoadingZoneStart.position, redLoadingZoneStart.heading, normalTranslationalVelConstraint)
            .strafeToLinearHeading(redLoadingZoneEnd.position, redLoadingZoneEnd.heading, normalTranslationalVelConstraint)
            .turn(Math.toRadians(-40))
            .turn(Math.toRadians(20))
            .strafeToLinearHeading(redFarLaunchPose.position, redFarLaunchPose.heading, normalTranslationalVelConstraint)
            .build();
    }

    public Action getRedFarPickupLoadingZoneLeave() {
        return md.actionBuilder(redFarLaunchPose)
            .strafeToLinearHeading(redLoadingZoneEnd.position, redLoadingZoneEnd.heading, normalTranslationalVelConstraint)
            .turn(Math.toRadians(40))
            .turn(Math.toRadians(-20))
            .strafeToConstantHeading(new Vector2d(30.5, -30.5), normalTranslationalVelConstraint)
            .build();
    }

    public Action[] getRedCloseTrajectories() {
        return new Action[] {
            redCloseStartToAprilTagRead,
            redAprilTagReadToCloseLaunch,
            redCloseLaunchPickupFirstMark,
            redCloseLaunchPickupSecondMark,
            redCloseLaunchToLeaveLaunchZone,
            redCloseLaunchPickupSecondMarkHitGate,
            redCloseStartToCloseLaunch,
            redCloseLaunchTurnToAprilTag,
            redCloseLaunchPickupThirdMark
        };
    }

    public Action[] getBlueFarTrajectories() {
        return new Action[] {
            blueFarStartToFarLaunch,
            blueFarLaunchPickupThirdMark,
            blueFarLaunchPickupSecondMark,
            blueFarLaunchPickupFirstMark,
            blueFarLaunchToLeaveLaunchZone,
            blueFarPickupLoadingZone
        };
    }

    public Action getBlueFarPickupLoadingZone() {
        return md.actionBuilder(blueFarLaunchPose)
            .strafeToLinearHeading(blueLoadingZoneEnd.position, blueLoadingZoneEnd.heading, normalTranslationalVelConstraint)
            .turn(Math.toRadians(-40))
            .turn(Math.toRadians(20))
            .strafeToLinearHeading(blueFarLaunchPose.position, blueFarLaunchPose.heading, normalTranslationalVelConstraint)
            .build();
    }

    public Action getBlueFarPickupLoadingZoneLeave() {
        return md.actionBuilder(blueFarLaunchPose)
            .strafeToLinearHeading(blueLoadingZoneEnd.position, blueLoadingZoneEnd.heading, normalTranslationalVelConstraint)
            .turn(Math.toRadians(-40))
            .turn(Math.toRadians(20))
            .strafeToConstantHeading(new Vector2d(30.5, -30.5), normalTranslationalVelConstraint)
            .build();
    }

    public Action[] getBlueCloseTrajectories() {
        return new Action[] {
            blueCloseStartToAprilTagRead,
            blueAprilTagReadToCloseLaunch,
            blueCloseLaunchPickupFirstMark,
            blueCloseLaunchPickupSecondMark,
            blueCloseLaunchToLeaveLaunchZone,
            blueCloseLaunchPickupSecondMarkHitGate,
            blueCloseLaunchPickupThirdMark,
            blueCloseStartToCloseLaunch
        };
    }

    public Action getSpinLauncherFar() {
        return robot.getLauncher().getSpinLauncherAction(1420);  // was 1360 (27Dec25)
    }

    public Action getSpinLauncherClose() {
        return robot.getLauncher().getSpinLauncherAction(1220);
    }

    public Action getStopLauncher() {
        return robot.getLauncher().getStopLauncherAction();
    }

    // Output
    public Action getIndexOutputAction(int indexPos) {
        return indexPos == 0 ? robot.getIndexer().getGoToZeroBallOutputAction() : (indexPos == 1 ? robot.getIndexer().getGoToOneBallOutputAction() : robot.getIndexer().getGoToTwoBallOutputAction());
    }

    // Intake
    public Action getIndexIntakeAction(int indexPos) {
        return indexPos == 0 ? robot.getIndexer().getGotoZeroBallIntakeAction() : (indexPos == 1 ? robot.getIndexer().getGoToOneBallIntakeAction() : robot.getIndexer().getGoToTwoBallIntakeAction());
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

    public Action getAprilTagAction() {
        return robot.getLauncher().getAprilTagAction();
    }
}
