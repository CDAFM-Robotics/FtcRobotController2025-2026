package org.firstinspires.ftc.teamcode.common;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.subsystems.DriveBase;
import org.firstinspires.ftc.teamcode.common.subsystems.Indexer;
import org.firstinspires.ftc.teamcode.common.subsystems.Intake;
import org.firstinspires.ftc.teamcode.common.subsystems.Launcher;

public class Robot {

    private DriveBase driveBase;
    private Indexer indexer;
    private Launcher launcher;
    private Intake intake;

    private ElapsedTime elapsedTime = new ElapsedTime();

    private HardwareMap hardwareMap;
    private Telemetry telemetry;

    public Robot(HardwareMap hardwareMap, Telemetry telemetry) {
        // Create an instance of the hardware map and telemetry in the Robot class
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;

        initializeSubsystems();
    }

    public void initializeSubsystems() {

        // Create an instance of every subsystem in the Robot class
        this.driveBase = new DriveBase(this.hardwareMap, this.telemetry);
        this.indexer = new Indexer(this.hardwareMap, this.telemetry);
        this.launcher = new Launcher(this.hardwareMap, this.telemetry);
        this.intake = new Intake(this.hardwareMap, this.telemetry);

    }

    public DriveBase getDriveBase() {
        return driveBase;
    }

    public Indexer getIndexer() {
        return indexer;
    }

    public Launcher getLauncher() {
        return launcher;
    }

    public Intake getIntake() {
        return intake;
    }
}
