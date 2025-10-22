package org.firstinspires.ftc.teamcode.autonomous;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.common.Robot;

@Autonomous(name="Leave Auto", group = "00000ComnpTempThingy")
public class LeaveAutonomousOpMode extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {
        Robot robot = new Robot(hardwareMap, telemetry);

        ElapsedTime elapsedTime = new ElapsedTime(ElapsedTime.Resolution.MILLISECONDS);

        waitForStart();

        elapsedTime.reset();


        while(elapsedTime.milliseconds() < 300 && opModeIsActive()) {
            robot.getDriveBase().setMotorPowers(1,0,0,1,true);
        }

        robot.getDriveBase().setMotorPowers(0,0,0,0,true);
    }
}
