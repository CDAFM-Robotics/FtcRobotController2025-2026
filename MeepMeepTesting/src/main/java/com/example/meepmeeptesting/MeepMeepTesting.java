package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.acmerobotics.roadrunner.VelConstraint;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.DriveTrainType;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting{

    public static Pose2d redFarLaunchPose = new Pose2d(47, 11.5, Math.toRadians(-113));
    public static Pose2d redCloseLaunchPose = new Pose2d(new Vector2d(-20, 20), Math.toRadians(-135));
    public static Pose2d redFirstMarkStart = new Pose2d(new Vector2d(-11.5, 26), Math.toRadians(90));
    public static Pose2d redFirstMarkEnd = new Pose2d(new Vector2d(-11.5, 46), Math.toRadians(90));
    public static Pose2d redSecondMarkStart = new Pose2d(new Vector2d(11.5, 30), Math.toRadians(90));
    public static Pose2d redSecondMarkEnd = new Pose2d(new Vector2d(11.5, 46), Math.toRadians(90));
    public static Pose2d redThirdMarkStart = new Pose2d(36, 23.5, Math.toRadians(90));
    public static Pose2d redThirdMarkEnd = new Pose2d(new Vector2d(36, 53), Math.toRadians(90));

    public static Pose2d blueFarLaunchPose = new Pose2d(47, -11.5, Math.toRadians(-64));
    public static Pose2d blueCloseLaunchPose = new Pose2d(-20, -20, Math.toRadians(-45));
    public static Pose2d blueFirstMarkStart = new Pose2d(new Vector2d(-11.5, -26), Math.toRadians(-90));
    public static Pose2d blueFirstMarkEnd = new Pose2d(new Vector2d(-11.5, -46), Math.toRadians(-90));
    public static Pose2d blueSecondMarkStart = new Pose2d(new Vector2d(11.5, -30), Math.toRadians(-90));
    public static Pose2d blueSecondMarkEnd = new Pose2d(new Vector2d(11.5, -46), Math.toRadians(-90));
    public static Pose2d blueThirdMarkStart = new Pose2d(36, -23.5, Math.toRadians(-90));
    public static Pose2d blueThirdMarkEnd = new Pose2d(new Vector2d(36, -53), Math.toRadians(-90));


    public static VelConstraint normalTranslationalVelConstraint = new TranslationalVelConstraint(50);
    public static VelConstraint slowTranslationalVelConstraint = new TranslationalVelConstraint(20);

    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
            // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
            .setDimensions(18, 17.5)
            .setDriveTrainType(DriveTrainType.MECANUM)
            .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 18)
            .build();

        myBot.runAction(myBot.getDrive()
            .actionBuilder(new Pose2d(-50.5, 50.5, Math.toRadians(37)))
            .strafeToLinearHeading(new Vector2d(-35, 35), Math.toRadians(-45), normalTranslationalVelConstraint)
            .build()
        );

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
            .setDarkMode(true)
            .setBackgroundAlpha(0.95f)
            .addEntity(myBot)
            .start();
    }
}
