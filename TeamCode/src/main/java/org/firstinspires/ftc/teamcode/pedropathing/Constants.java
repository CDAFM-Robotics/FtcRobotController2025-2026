package org.firstinspires.ftc.teamcode.pedropathing;

import com.pedropathing.control.FilteredPIDFCoefficients;
import com.pedropathing.control.PIDFCoefficients;
import com.pedropathing.follower.Follower;
import com.pedropathing.follower.FollowerConstants;
import com.pedropathing.ftc.FollowerBuilder;
import com.pedropathing.ftc.drivetrains.MecanumConstants;
import com.pedropathing.ftc.localization.constants.PinpointConstants;
import com.pedropathing.paths.PathConstraints;
import com.qualcomm.hardware.gobilda.GoBildaPinpointDriver;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;

public class Constants {
    public static FollowerConstants followerConstants = new FollowerConstants()
        .mass(11.33981)
        .forwardZeroPowerAcceleration(-38.1641)
        .lateralZeroPowerAcceleration(-81.5006)
        .translationalPIDFCoefficients(new PIDFCoefficients(0.08, 0.0, 0.01, 0.02))
        .headingPIDFCoefficients(new PIDFCoefficients(2.0, 0.0, 0.1, 0.022))
        .drivePIDFCoefficients(new FilteredPIDFCoefficients(0.03, 0.0, 0.001, 0.6, 0.0))
        .centripetalScaling(0.0005);

    public static MecanumConstants mecanumConstants = new MecanumConstants()
        .maxPower(1)
        .rightFrontMotorName("frontRightMotor")
        .rightRearMotorName("backRightMotor")
        .leftFrontMotorName("frontLeftMotor")
        .leftRearMotorName("backLeftMotor")
        .rightFrontMotorDirection(DcMotorSimple.Direction.REVERSE)
        .rightRearMotorDirection(DcMotorSimple.Direction.REVERSE)
        .leftFrontMotorDirection(DcMotorSimple.Direction.FORWARD)
        .leftRearMotorDirection(DcMotorSimple.Direction.FORWARD)
        .xVelocity(66.4477)
        .yVelocity(42.1582);

    public static PinpointConstants pinpointConstants = new PinpointConstants()
        .forwardPodY(8)
        .strafePodX(-3.25)
        .distanceUnit(DistanceUnit.INCH)
        .hardwareMapName("pinpoint")
        .encoderResolution(GoBildaPinpointDriver.GoBildaOdometryPods.goBILDA_4_BAR_POD)
        .forwardEncoderDirection(GoBildaPinpointDriver.EncoderDirection.FORWARD)
        .strafeEncoderDirection(GoBildaPinpointDriver.EncoderDirection.REVERSED);

    public static PathConstraints pathConstraints = new PathConstraints(0.99, 100, 1, 1);

    public static Follower createFollower(HardwareMap hardwareMap) {
        return new FollowerBuilder(followerConstants, hardwareMap)
            .pinpointLocalizer(pinpointConstants)
            .pathConstraints(pathConstraints)
            .mecanumDrivetrain(mecanumConstants)
            .build();
    }
}