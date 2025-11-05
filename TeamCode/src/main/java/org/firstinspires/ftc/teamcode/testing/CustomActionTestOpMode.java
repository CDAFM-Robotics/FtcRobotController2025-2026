package org.firstinspires.ftc.teamcode.testing;

import com.acmerobotics.roadrunner.ftc.Actions;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.common.Robot;
import org.firstinspires.ftc.teamcode.common.subsystems.Launcher;

@TeleOp(name = "Custom Action Test", group = "testing")
public class CustomActionTestOpMode extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(hardwareMap, telemetry);
        waitForStart();

        double power = 0;

        Gamepad currentGamepad1 = new Gamepad();
        Gamepad previousGamepad1 = new Gamepad();
        Gamepad currentGamepad2 = new Gamepad();
        Gamepad previousGamepad2 = new Gamepad();



        while (opModeIsActive()) {

            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);
            currentGamepad1.copy(gamepad1);
            currentGamepad2.copy(gamepad2);

            power += currentGamepad2.dpad_up && !previousGamepad2.dpad_up ? 0.1 : (currentGamepad2.dpad_down && !previousGamepad2.dpad_down ? -0.1 : 0);

            robot.getLauncher().setLauncherPower(power);
            telemetry.addData("Current Velocity", robot.getLauncher().getLauncherVelocity());
            telemetry.update();
        }

         /*

        Actions.runBlocking(robot.getLauncher().getSpinLauncherAction(1600));
        telemetry.addData("Status", "Done with Action");
        telemetry.update();

        robot.getLauncher().setLauncherPower(0);
        sleep (1000);

        Actions.runBlocking(robot.getLauncher().getSpinLauncherAction(1600));
        telemetry.addData("Status", "Done with Action");
        telemetry.update();
        sleep(2000);

          */
    }
}
