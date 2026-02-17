package org.firstinspires.ftc.teamcode.autonomous;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.seattlesolvers.solverslib.command.CommandOpMode;
import com.seattlesolvers.solverslib.command.ParallelDeadlineGroup;
import com.seattlesolvers.solverslib.command.SequentialCommandGroup;
import com.seattlesolvers.solverslib.command.WaitCommand;
import com.seattlesolvers.solverslib.pedroCommand.FollowPathCommand;

import org.firstinspires.ftc.teamcode.common.Robot;
import org.firstinspires.ftc.teamcode.pedropathing.Constants;
import org.firstinspires.ftc.teamcode.pedropathing.commands.Paths;

@Autonomous(name = "Pedro's Cool Auto Program", group = "Testing")
public class BlueBackPedroPathingAuto extends CommandOpMode {

    Follower follower;

    @Override
    public void initialize() {
        super.reset();
        follower = Constants.createFollower(hardwareMap);
        follower.setStartingPose(new Pose(56, 8.5, Math.PI));
        Paths paths = new Paths(follower);

        schedule(
            new SequentialCommandGroup(
                new FollowPathCommand(follower, paths.getBlueFarPickupFirstMark()),
                new WaitCommand(1000),
                new FollowPathCommand(follower, paths.getBlueFarPickupSecondMark()),
                new WaitCommand(1000),
                new FollowPathCommand(follower, paths.getBlueFarPickupThirdMark()),
                new WaitCommand(1000)
            )
        );
    }

    @Override
    public void run() {
        super.run();
        follower.update();
    }
}
