package org.firstinspires.ftc.teamcode.pedropathing.commands;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.PathChain;

public class Paths {
    private PathChain blueFarPickupFirstMark;
    private PathChain blueFarPickupSecondMark;
    private PathChain blueFarPickupThirdMark;

    Follower follower;

    public Paths(Follower follower) {
        this.follower = follower;
    }

    public Follower getFollower() {
        return follower;
    }

    public PathChain getBlueFarPickupFirstMark() {
        blueFarPickupFirstMark = follower.pathBuilder().addPath(
                new BezierCurve(
                    new Pose(56.000, 8.500),
                    new Pose(43.000, 52.000),
                    new Pose(4.000, 43.000),
                    new Pose(0.000, 18.000),
                    new Pose(56.000, 8.500)
                )
            ).setConstantHeadingInterpolation(Math.toRadians(180))
            .build();

        return blueFarPickupFirstMark;
    }

    public PathChain getBlueFarPickupSecondMark() {
        blueFarPickupSecondMark = follower.pathBuilder().addPath(
                new BezierCurve(
                    new Pose(56.000, 8.500),
                    new Pose(43.000, 99.000),
                    new Pose(2.000, 59.000),
                    new Pose(2.000, 38.000),
                    new Pose(56.000, 8.500)
                )
            ).setConstantHeadingInterpolation(Math.toRadians(180))
            .build();

        return blueFarPickupSecondMark;
    }

    public PathChain getBlueFarPickupThirdMark() {
        blueFarPickupThirdMark = follower.pathBuilder().addPath(
                new BezierCurve(
                    new Pose(56.000, 8.500),
                    new Pose(63.000, 109.000),
                    new Pose(28.000, 74.000),
                    new Pose(1.000, 96.000),
                    new Pose(15.000, 89.000),
                    new Pose(56.000, 8.500)
                )
            ).setConstantHeadingInterpolation(Math.toRadians(180))
            .build();

        return blueFarPickupThirdMark;
    }
}

