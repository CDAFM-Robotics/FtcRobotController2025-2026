package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.common.Robot;
import org.firstinspires.ftc.teamcode.common.subsystems.Launcher;

@TeleOp(name = "Custom Action Test", group = "testing")
public class CustomActionTestOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(hardwareMap, telemetry);
        waitForStart();
        Actions.runBlocking(robot.getLauncher().getSpinLauncherAction(10000));
    }
}
