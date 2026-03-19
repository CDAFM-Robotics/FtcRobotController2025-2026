package org.firstinspires.ftc.teamcode.testing.archived;

import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

@Disabled
@TeleOp(name = "ballDetector", group = "0testing")
public class ballDetectionLimelightTest extends LinearOpMode {
    private Limelight3A limelight;
    private static double MOUNTING_HEIGHT_INCHES = 18.5;
    private static double BALL_RADIUS_INCHES = 2.5;
    @Override
    public void runOpMode() throws InterruptedException {

        Gamepad currentGamepad1 = new Gamepad();
        Gamepad previousGamepad1 = new Gamepad();
        Gamepad currentGamepad2 = new Gamepad();
        Gamepad previousGamepad2 = new Gamepad();

        int currentPipelineIndex = 4;

        limelight = hardwareMap.get(Limelight3A.class, "Limelight");
        limelight.pipelineSwitch(currentPipelineIndex);
        limelight.start();

        waitForStart();

        while(opModeIsActive()){
            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);
            currentGamepad1.copy(gamepad1);
            currentGamepad2.copy(gamepad2);

            if(currentGamepad1.a && !previousGamepad1.a){
                currentPipelineIndex = currentPipelineIndex == 4 ? 5:4;
                limelight.pipelineSwitch(currentPipelineIndex);
            }
            LLResult result = limelight.getLatestResult();

            if(result.isValid()){

                double distToTargetY = (MOUNTING_HEIGHT_INCHES - BALL_RADIUS_INCHES) / Math.tan(-Math.toRadians(result.getTy()));
                double distToTargetX = distToTargetY * Math.tan(Math.toRadians(result.getTx()));
                telemetry.addData("target X", result.getTx());
                telemetry.addData("target Y", result.getTy());
                telemetry.addData("target Area", result.getTa());
                telemetry.addData("distance to target y", distToTargetY);
                telemetry.addData("distance to target x", distToTargetX);
            }
            else{
                telemetry.addLine("no valid target found");
            }

            telemetry.update();
        }
    }
}
