package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.teamcode.common.Robot;
import org.firstinspires.ftc.teamcode.common.subsystems.Launcher;

@TeleOp(name = "Custom Action Test", group = "testing")
public class CustomActionTestOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(hardwareMap, telemetry);
        waitForStart();

        /*

        robot.getLauncher().startLauncher();
        while (opModeIsActive()) {
            robot.getLauncher().startLauncher();
            telemetry.addData("Current Velocity", robot.getLauncher().getLauncherVelocity());
            telemetry.update();
        }

         */

        Actions.runBlocking(robot.getLauncher().getSpinLauncherAction(1600));
        telemetry.addData("Status", "Done with Action");
        telemetry.update();

        robot.getLauncher().setLauncherPower(0);
        sleep (1000);

        Actions.runBlocking(robot.getLauncher().getSpinLauncherAction(1600));
        telemetry.addData("Status", "Done with Action");
        telemetry.update();
        sleep(2000);
    }
}
