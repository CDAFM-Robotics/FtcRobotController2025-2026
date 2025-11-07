package org.firstinspires.ftc.teamcode.common.util;

import android.os.Bundle;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;
import com.acmerobotics.roadrunner.SleepAction;

public class RunTimeoutAction implements Action {

    boolean initialized = false;
    double timeout;

    Action action;
    SleepAction sleepAction;


    public RunTimeoutAction(Action action, double timeout) {
        this.action = action;
        this.timeout = timeout;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        if (!initialized) {
            initialized = true;
            sleepAction = new SleepAction(timeout);
        }
        return action.run(telemetryPacket) && sleepAction.run(telemetryPacket);
    }
}
