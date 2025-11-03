package org.firstinspires.ftc.teamcode.common.util;

import androidx.annotation.NonNull;

import com.acmerobotics.dashboard.telemetry.TelemetryPacket;
import com.acmerobotics.roadrunner.Action;

import java.util.function.Supplier;

public class WaitUntilAction implements Action {
    private Supplier<Boolean> condition;

    public WaitUntilAction(Supplier<Boolean> condition) {
        this.condition = condition;
    }

    @Override
    public boolean run(@NonNull TelemetryPacket telemetryPacket) {
        return !condition.get();
    }
}
