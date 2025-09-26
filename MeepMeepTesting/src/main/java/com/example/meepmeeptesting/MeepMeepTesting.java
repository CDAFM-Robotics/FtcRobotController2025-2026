package com.example.meepmeeptesting;

import com.acmerobotics.roadrunner.Pose2d;
import com.acmerobotics.roadrunner.TranslationalVelConstraint;
import com.acmerobotics.roadrunner.Vector2d;
import com.noahbres.meepmeep.MeepMeep;
import com.noahbres.meepmeep.roadrunner.DefaultBotBuilder;
import com.noahbres.meepmeep.roadrunner.DriveTrainType;
import com.noahbres.meepmeep.roadrunner.entity.RoadRunnerBotEntity;

public class MeepMeepTesting {
    public static void main(String[] args) {
        MeepMeep meepMeep = new MeepMeep(600);

        RoadRunnerBotEntity myBot = new DefaultBotBuilder(meepMeep)
            // Set bot constraints: maxVel, maxAccel, maxAngVel, maxAngAccel, track width
            .setDimensions(18, 17.5)
            .setDriveTrainType(DriveTrainType.MECANUM)
            .setConstraints(60, 60, Math.toRadians(180), Math.toRadians(180), 18)
            .build();

        myBot.runAction(myBot.getDrive().actionBuilder(new Pose2d(new Vector2d(-20, 20), Math.toRadians(135)))
            .setTangent(Math.toRadians(10))
            .splineToSplineHeading(new Pose2d(new Vector2d(11.5, 30), Math.toRadians(90)), Math.toRadians(90), new TranslationalVelConstraint(50))
            .strafeToConstantHeading(new Vector2d(11.5, 46), new TranslationalVelConstraint(20))
            .setTangent(Math.toRadians(-135))
            .splineToSplineHeading(new Pose2d(new Vector2d(-20, 20), Math.toRadians(135)), Math.toRadians(-135), new TranslationalVelConstraint(50))
            .build()
        );

        meepMeep.setBackground(MeepMeep.Background.FIELD_DECODE_JUICE_DARK)
            .setDarkMode(true)
            .setBackgroundAlpha(0.95f)
            .addEntity(myBot)
            .start();
    }
}
