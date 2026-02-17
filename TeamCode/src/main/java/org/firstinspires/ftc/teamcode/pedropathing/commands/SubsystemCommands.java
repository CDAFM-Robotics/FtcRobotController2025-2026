package org.firstinspires.ftc.teamcode.pedropathing.commands;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.seattlesolvers.solverslib.command.InstantCommand;

import org.firstinspires.ftc.teamcode.common.Robot;


public class SubsystemCommands {
    Robot robot;
    public SubsystemCommands(Robot robot) {
        this.robot = robot;
    }

    public InstantCommand setPowerIntake(double power) {
        return new InstantCommand(() -> robot.getIntake().setIntakeMotorPower(power));
    }

    public InstantCommand startIntake() {
        return setPowerIntake(1);
    }

    public InstantCommand stopIntake() {
        return setPowerIntake(0);
    }

    public InstantCommand rotateIndexer(double position){return null;}
}
