package org.firstinspires.ftc.teamcode.teleop;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Gamepad;

import org.firstinspires.ftc.teamcode.common.Robot;

@TeleOp(name = "Driver Control Teleop", group = "0teleop")
public class DriverControlTeleOp extends LinearOpMode {

    @Override
    public void runOpMode() throws InterruptedException {

        Robot robot = new Robot(hardwareMap, telemetry);

        double driveSpeed = 1;
        boolean fieldCentric = true;
        double index_position = 0.5;

        Gamepad currentGamepad1 = new Gamepad();
        Gamepad previousGamepad1 = new Gamepad();
        Gamepad currentGamepad2 = new Gamepad();
        Gamepad previousGamepad2 = new Gamepad();

        waitForStart();

        while(opModeIsActive()){
            previousGamepad1.copy(currentGamepad1);
            previousGamepad2.copy(currentGamepad2);
            currentGamepad1.copy(gamepad1);
            currentGamepad2.copy(gamepad2);

            // Drive Base

            if (currentGamepad1.left_stick_button && !previousGamepad1.left_stick_button){
                driveSpeed = driveSpeed == 1 ? 0.5 : 1;
            }

            if (currentGamepad1.back && !previousGamepad1.back){
                fieldCentric = !fieldCentric;
            }

            if (currentGamepad1.right_bumper != previousGamepad1.right_bumper) {
                driveSpeed = driveSpeed == 1 ? 0.5 : 1;
            }

            robot.getDriveBase().setMotorPowers(-gamepad1.left_stick_x, gamepad1.left_stick_y, -gamepad1.right_stick_x, driveSpeed, fieldCentric);

            // TODO We need 2 states: pickup and shoot

            // Intake

            if (currentGamepad1.a && !previousGamepad1.a) {
                robot.getIntake().toggleIntake();
            }

            // Indexer
            //index_position += (currentGamepad2.x && !previousGamepad2.x) ? 0.4 : ((currentGamepad2.y && !previousGamepad2.y) ? -0.4 : 0);
            //index_position += (currentGamepad2.x && !previousGamepad2.x) ? 0.01 : ((currentGamepad2.y && !previousGamepad2.y) ? -0.01 : 0);

            /*if (index_position > 1) {
                index_position -= 0.4;
            }

            if (index_position < 0) {
                index_position += 0.4;
            }**/

            /*if (index_position > 1) {
                index_position =1;
            }

            if (index_position < 0) {
                index_position =0;
            }*/

            //robot.getIndexer().rotateToPosition(index_position);
            telemetry.addData("index position: ", robot.getIndexer().getIndexerPosition());


           if (currentGamepad2.x && !previousGamepad2.x) {
                robot.getIndexer().rotateClock();
            }

           if (currentGamepad2.y && !previousGamepad2.y) {
                robot.getIndexer().rotateCounterClock();
           }
           telemetry.addData("index position2: ", robot.getIndexer().getIndexerPosition());


            // TODO We need to make it so when we are picking up, there is an empty slot but when shooting, there is a ball in the indexer ready to shoot.


            //Launcher

            if (currentGamepad2.b && !previousGamepad2.b) {
                robot.getLauncher().toggleLauncher();
            }

            if (currentGamepad2.right_bumper) {
                robot.getLauncher().kickBall();
            }
            else {
                robot.getLauncher().resetKicker();
            }



            telemetry.update();

        }
    }
}
