package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.autonomous.actions.AutonomousActionBuilder;
import org.firstinspires.ftc.teamcode.common.Robot;
import org.firstinspires.ftc.teamcode.roadrunner.Drawing;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name = "Auto_RR_MotorSetupTest", group = "Testing")
public class Auto_RR_MotorSetupTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {

        // Start at Center of Field (Red Alliance ref) bot facing forwards (along positive Y axis = 90 degrees(PI/2) on unit circle)

        // TODO : SET ROBOT UP FIRST SINCE IT ESTABLISHES SOME VARIABLES
        // Robot robot = new Robot(hardwareMap, telemetry);

        MecanumDrive md = new MecanumDrive(hardwareMap, new Pose2d(0, 0, Math.toRadians(0)));


//        AutonomousActionBuilder autonomousTrajectoryBuilder = new AutonomousActionBuilder(md, robot);
//
//        Action[] trajectories = autonomousTrajectoryBuilder.getRedFarTrajectories();
//
//        Action[] otherActions = autonomousTrajectoryBuilder.getOtherActions();
//
        Robot.ArtifactColor[] motif = null;

        while(opModeInInit()) {
//            motif = robot.getLauncher().getMotifPattern();
        }

        waitForStart();

//        //start always with PGP
//
//        if (motif == null) {
//            motif = new Robot.ArtifactColor[] {Robot.ArtifactColor.GREEN, Robot.ArtifactColor.PURPLE, Robot.ArtifactColor.PURPLE};
//        }
//
//        if (motif[0] != Robot.ArtifactColor.GREEN) {
//            Actions.runBlocking(otherActions[2]);
//        }

        // Go to the Launch Pose

        Actions.runBlocking(new ParallelAction(
            // trajectories[0]
            // otherActions[0]  // TODO SPINUPASCTION
                // TODO basic start and move forward.
                md.actionBuilder(new Pose2d(new Vector2d(0,0), Math.toRadians(0)))
                        .strafeTo(new Vector2d(0, 1))  // Move forward one square
                        .turnTo(Math.PI/2)
                        .strafeTo(new Vector2d(0,0))
                .build()
        ));

        // TODO Jer Added for
        md.updatePoseEstimate();

        Pose2d pose = md.localizer.getPose();
        telemetry.addData("x", pose.position.x);
        telemetry.addData("y", pose.position.y);
        telemetry.addData("heading (deg)", Math.toDegrees(pose.heading.toDouble()));
        telemetry.update();

        TelemetryPacket packet = new TelemetryPacket();
        packet.fieldOverlay().setStroke("#3F51B5");
        Drawing.drawRobot(packet.fieldOverlay(), pose);
        FtcDashboard.getInstance().sendTelemetryPacket(packet);

/*
        Actions.runBlocking(otherActions[5]);


        Actions.runBlocking(otherActions[8]);

        if (motif[0] == Robot.ArtifactColor.GREEN) {
            Actions.runBlocking(otherActions[2]);
        }
        else if (motif[1] == Robot.ArtifactColor.GREEN) {
            Actions.runBlocking(otherActions[3]);
        }
        else {
            Actions.runBlocking(otherActions[4]);
        }

        Actions.runBlocking(otherActions[5]);

        Actions.runBlocking(otherActions[8]);

        if (motif[0] == Robot.ArtifactColor.GREEN) {
            Actions.runBlocking(otherActions[4]);
        }
        else if (motif[1] == Robot.ArtifactColor.GREEN) {
            Actions.runBlocking(otherActions[4]);
        }
        else {
            Actions.runBlocking(otherActions[3]);
        }

        Actions.runBlocking(otherActions[5]);

        Actions.runBlocking(otherActions[8]);

        // Pickup first mark

        //Actions.runBlocking(trajectories[1]);

        sleep(1000);

        // LEave

        Actions.runBlocking(new ParallelAction(
            trajectories[2],
            otherActions[1]
        ));
*/
    }
}
