package org.firstinspires.ftc.teamcode.testing;

import android.graphics.Canvas;
import android.graphics.Color;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.robotcore.external.hardware.camera.WebcamName;
import org.firstinspires.ftc.robotcore.internal.camera.calibration.CameraCalibration;
import org.firstinspires.ftc.vision.VisionPortal;
import org.firstinspires.ftc.vision.VisionPortalImpl;
import org.firstinspires.ftc.vision.VisionProcessor;
import org.firstinspires.ftc.vision.opencv.Circle;
import org.firstinspires.ftc.vision.opencv.ColorBlobLocatorProcessor;
import org.firstinspires.ftc.vision.opencv.ColorRange;
import org.firstinspires.ftc.vision.opencv.ImageRegion;
import org.opencv.core.Mat;

import android.util.Size;


import java.util.Collections;
import java.util.List;

@Config
@TeleOp(name = "cameraTest", group = "0testing")
public class cameraDetectTestOpMode extends LinearOpMode {
    private VisionPortal visionPortal;
    private ColorBlobLocatorProcessor colorBlobLocatorProcessor;

    public static int dialateSize = 15;
    public static int erosionSize = 15;
    Circle biggestBlob = new Circle(0, 0, 0);
    VisionProcessor processor;


    @Override
    public  void runOpMode() throws InterruptedException{
        //telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());
        initVisionPortal();
        waitForStart();

        while(opModeIsActive() || opModeInInit()){
            telemetry.addData("preview on/off", "... Camera Stream\n");

            telemetry.addLine("****************************************************");
            telemetry.addData("BIGGEST BLOB X", biggestBlob.getX());
            telemetry.addData("BIGGEST BLOB Y", biggestBlob.getY());
            telemetry.addData("BIGGEST BLOB RADIUS", biggestBlob.getRadius());
            telemetry.addLine("****************************************************");

            List<ColorBlobLocatorProcessor.Blob> detectedBlobs = colorBlobLocatorProcessor.getBlobs();
            for(ColorBlobLocatorProcessor.Blob blob : detectedBlobs){
                Circle circleFit = blob.getCircle();
                telemetry.addData("Blob x", circleFit.getX());
                telemetry.addData("Blob y", circleFit.getY());
                telemetry.addData("Blob radius", circleFit.getRadius());
                telemetry.addData("Blob Size", (Math.PI*Math.pow(circleFit.getRadius(), 2)));
                telemetry.addLine("----------------------------------------------------------");
                if(circleFit.getRadius() >= biggestBlob.getRadius()){
                    biggestBlob = circleFit;
                    //ppoopo test thing delete ts line
                }
            }

            telemetry.update();
        }
    }
    void initVisionPortal(){
        colorBlobLocatorProcessor = new ColorBlobLocatorProcessor.Builder()
                .setTargetColorRange(ColorRange.ARTIFACT_GREEN)
                .setRoi(ImageRegion.entireFrame())
                .setContourMode(ColorBlobLocatorProcessor.ContourMode.EXTERNAL_ONLY)
                .setDrawContours(true)
                .setCircleFitColor(Color.GREEN)
                .setDilateSize(dialateSize)
                .setErodeSize(erosionSize)
                .setMorphOperationType(ColorBlobLocatorProcessor.MorphOperationType.CLOSING)
                .build();
        visionPortal = new VisionPortal.Builder()
                .addProcessor(colorBlobLocatorProcessor)
                .setCameraResolution(new Size(640, 480))
                .setCamera(hardwareMap.get(WebcamName.class, "Webcam"))
                .enableLiveView(true)
                .build();
    }
}

