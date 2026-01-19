package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.roadrunner.ParallelAction;
import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.autonomous.actions.AutonomousActionBuilder;
import org.firstinspires.ftc.teamcode.common.Robot;
import org.firstinspires.ftc.teamcode.common.util.ArtifactColor;
import org.firstinspires.ftc.teamcode.roadrunner.MecanumDrive;

@Autonomous(name = "Blue Front Straight Back Test")
public class BlueFrontStraightBackAutonomousTestOpMode extends LinearOpMode {

    AutonomousActionBuilder actionBuilder;

    @Override
    public void runOpMode() throws InterruptedException {
        MecanumDrive md = new MecanumDrive(hardwareMap, new Pose2d(-50.5, -50.5, Math.toRadians(-37)));
        Robot robot = new Robot(hardwareMap, telemetry);

        actionBuilder = new AutonomousActionBuilder(md, robot);

        waitForStart();

        Actions.runBlocking(actionBuilder.blueCloseStartToCloseLaunch);
        Actions.runBlocking(actionBuilder.getSpinLauncherClose());

        launchInMotifOrder(new ArtifactColor[]{ArtifactColor.GREEN, ArtifactColor.PURPLE, ArtifactColor.PURPLE}, 0);

        
    }
    
    public void launchInMotifOrder(ArtifactColor[] motifPattern, int greenLocation) {
        Actions.runBlocking(motifPattern[0] == ArtifactColor.GREEN ? actionBuilder.getIndexOutputAction(greenLocation) : actionBuilder.getIndexOutputAction(greenLocation == 0 ? 1 : 0));
        Actions.runBlocking(actionBuilder.getKickBall());
        Actions.runBlocking(actionBuilder.getResetKicker());
        Actions.runBlocking(motifPattern[1] == ArtifactColor.GREEN ? actionBuilder.getIndexOutputAction(greenLocation) : (motifPattern[0] == ArtifactColor.GREEN ? actionBuilder.getIndexOutputAction(greenLocation == 0 ? 1 : 0) : actionBuilder.getIndexOutputAction(greenLocation == 2 ? 1 : 2)));
        Actions.runBlocking(actionBuilder.getKickBall());
        Actions.runBlocking(actionBuilder.getResetKicker());
        Actions.runBlocking(motifPattern[2] == ArtifactColor.GREEN ? actionBuilder.getIndexOutputAction(greenLocation) : actionBuilder.getIndexOutputAction(greenLocation == 2 ? 1 : 2));
        Actions.runBlocking(actionBuilder.getKickBall());
        Actions.runBlocking(new ParallelAction(
            actionBuilder.getStopLauncher(),
            actionBuilder.getResetKicker()
        ));
    }
}
