package org.firstinspires.ftc.teamcode.autonomous;

import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.NullAction;
import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.SequentialAction;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.autonomous.actions.AutonomousActionBuilder;
import org.firstinspires.ftc.teamcode.common.Robot;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

import java.util.function.Supplier;

@Autonomous(name = "Red Back Autonomous", group = "0competition")
public class RedBackAutonomousOpMode extends LinearOpMode {

    Supplier<Action>[] otherActions;
    Action[] trajectories;


    @Override
    public void runOpMode() throws InterruptedException {
        MecanumDrive md = new MecanumDrive(hardwareMap, /*new Pose2d(new Vector2d(61, 11.5), Math.toRadians(180))*/ new Pose2d(new Vector2d(61, 11.75), Math.toRadians(-90)));
        Robot robot = new Robot(hardwareMap, telemetry);
        AutonomousActionBuilder autonomousTrajectoryBuilder = new AutonomousActionBuilder(md, robot);

        trajectories = autonomousTrajectoryBuilder.getRedFarTrajectories();

        otherActions = autonomousTrajectoryBuilder.getOtherActions();

        Robot.ArtifactColor[] motif = null;

        while(opModeInInit()) {
            motif = robot.getLauncher().getMotifPattern();

            if (motif == null) {
                telemetry.addData("Motif Pattern", "Not Detected");
            }
            else {
                telemetry.addData("Motif Pattern", motif[0].toString() + ", " + motif[1].toString() + ", " + motif[2].toString());
            }
            telemetry.update();
        }

        waitForStart();

        //start always with G in launcher



        if (motif == null) {
            motif = new Robot.ArtifactColor[] {Robot.ArtifactColor.GREEN, Robot.ArtifactColor.PURPLE, Robot.ArtifactColor.PURPLE};
        }

        /*

        if (motif[0] != Robot.ArtifactColor.GREEN) {
            Actions.runBlocking(otherActions[3].get());
        }

         */

        // Go to the Launch Pose

        Actions.runBlocking(new ParallelAction(
            trajectories[0],
            otherActions[0].get()
        ));

        //Actions.runBlocking(launchInMotifOrder(motif));

/*


        Actions.runBlocking(otherActions[5].get());




        Actions.runBlocking(otherActions[8].get());



        if (motif[1] == Robot.ArtifactColor.GREEN) {
            Actions.runBlocking(otherActions[2].get());
        }
        else if (motif[0] == Robot.ArtifactColor.GREEN) {
            Actions.runBlocking(otherActions[3].get());
        }
        else {
            Actions.runBlocking(otherActions[4].get());
        }

        Actions.runBlocking(otherActions[5].get());

        Actions.runBlocking(otherActions[8].get());

        if (motif[0] == Robot.ArtifactColor.GREEN) {
            Actions.runBlocking(otherActions[4].get());
        }
        else if (motif[1] == Robot.ArtifactColor.GREEN) {
            Actions.runBlocking(otherActions[4].get());
        }
        else {
            Actions.runBlocking(otherActions[2].get());
        }



        Actions.runBlocking(otherActions[5].get());

        Actions.runBlocking(new ParallelAction(
            otherActions[8].get(),
            otherActions[1].get()
        ));

 */

        /*

        // Pickup first mark

        Actions.runBlocking(new SequentialAction(
            new ParallelAction(
                trajectories[1],
                otherActions[6].get()
            ),
            new ParallelAction(
                otherActions[7].get()
                otherActions[0].get()
            )
        ));



*/


        sleep(1000);

        // LEave

        Actions.runBlocking(new ParallelAction(
            trajectories[2]
        ));



    }

    public Action launchInMotifOrder(Robot.ArtifactColor[] motifPattern) {
        /*

        if (motifPattern[0] != Robot.ArtifactColor.GREEN) {
            Actions.runBlocking(otherActions[3].get());
        }

        Actions.runBlocking(otherActions[5].get());

        Actions.runBlocking(otherActions[8].get());

        if (motifPattern[1] == Robot.ArtifactColor.GREEN) {
            Actions.runBlocking(otherActions[2].get());
        }
        else if (motifPattern[0] == Robot.ArtifactColor.GREEN) {
            Actions.runBlocking(otherActions[3].get());
        }
        else {
            Actions.runBlocking(otherActions[4].get());
        }

        Actions.runBlocking(otherActions[5].get());

        Actions.runBlocking(otherActions[8].get());

        if (motifPattern[0] == Robot.ArtifactColor.GREEN) {
            Actions.runBlocking(otherActions[4].get());
        }
        else if (motifPattern[1] == Robot.ArtifactColor.GREEN) {
            Actions.runBlocking(otherActions[4].get());
        }
        else {
            Actions.runBlocking(otherActions[2].get());
        }

        Actions.runBlocking(otherActions[5].get());

        Actions.runBlocking(new ParallelAction(
            otherActions[8].get(),
            otherActions[1].get()
        ));

         */

        return new SequentialAction(
            motifPattern[0] != Robot.ArtifactColor.GREEN ? otherActions[3].get() : new NullAction(),
            otherActions[5].get(),
            otherActions[8].get(),
            motifPattern[1] == Robot.ArtifactColor.GREEN ? otherActions[2].get() : (motifPattern[0] == Robot.ArtifactColor.GREEN ? otherActions[3].get() : otherActions[4].get()),
            otherActions[5].get(),
            otherActions[8].get(),
            motifPattern[2] == Robot.ArtifactColor.GREEN ? otherActions[2].get() : otherActions[4].get(),
            otherActions[5].get(),
            new ParallelAction(
                otherActions[1].get(),
                otherActions[8].get()
            )
        );
    }
}
