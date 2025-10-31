package org.firstinspires.ftc.teamcode.common.subsystems;


import android.graphics.Color;

import androidx.annotation.ColorInt;

import com.qualcomm.robotcore.hardware.HardwareMap;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.Robot;
import org.firstinspires.ftc.teamcode.common.util.QwiicLEDStick;
import org.firstinspires.ftc.teamcode.testing.LEDStickTest;

public class Hud {

    HardwareMap hardwareMap;
    Telemetry telemetry;

    public QwiicLEDStick ledstrip;

    public static int LED_STICK_BRIGHTNESS=6; // Brightness (1-31)
    public static int LED_STICK_TOTAL_LEDS=10; // How many Total LED there are to control
    public static int ms_delay=6;

    // ColorTable2 is the possible COLORS for 2025-2026 Decode
    public enum ColorTable {PURPLE, GREEN, NONE, RED, WHITE}

    // instance vars to hold Ball colors (2025-2026 Decode)
    public LEDStickTest.ColorTable ball1, last1 = LEDStickTest.ColorTable.NONE;
    public LEDStickTest.ColorTable ball2, last2 = LEDStickTest.ColorTable.NONE;
    public LEDStickTest.ColorTable ball3, last3 = LEDStickTest.ColorTable.NONE;


    // Pre-defined color values for valid Balls (2025-2026) (same order as Enum)
    public @ColorInt int[] Balls = new int[]{
            Color.parseColor("purple"), // purple
            Color.rgb(0,255,0), // green
            Color.rgb(0,0,0), // off
            Color.rgb(255,0,0), // red
            Color.parseColor("silver") // white
    };

    public Hud(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;

        initializeHUD();
    }

    public void initializeHUD() {
        ledstrip = hardwareMap.get(QwiicLEDStick.class, "ledstrip");
        ledstrip.changeLength(LED_STICK_TOTAL_LEDS); // limit addressable LED to number of LED installed
        ledstrip.setBrightness(LED_STICK_BRIGHTNESS);// 10 LEDs at brightness 31 generates 660ma current
    }

    public void UpdateBallUI() {

        // Sleep for 5ms between command to avoid overwhelming i2c device with
        // messages (0 causes glitches & strip freezes)

        if (ball1 != last1 ) { // only send update if different

            last1 = ball1;
            // Ball1 (bottom) LEDs 0-2
            ledstrip.setColor(0, Balls[ball1.ordinal()]);
            sleep(ms_delay);
            ledstrip.setColor(1, Balls[ball1.ordinal()]);
            sleep(ms_delay);
            ledstrip.setColor(2, Balls[ball1.ordinal()]);
            sleep(ms_delay);
        }

        if (ball2 != last2) {
            last2 = ball2;
            // Ball2
            ledstrip.setColor(3, Balls[ball2.ordinal()]);
            sleep(ms_delay);
            ledstrip.setColor(4, Balls[ball2.ordinal()]);
            sleep(ms_delay);
            ledstrip.setColor(5, Balls[ball2.ordinal()]);
            sleep(ms_delay);

        }
        if (ball3 != last3) {
            // Drop2_sensor (bottom) LEDs 5-8
            last3 = ball3;
            ledstrip.setColor(6, Balls[ball3.ordinal()]);
            sleep(ms_delay);
            ledstrip.setColor(7, Balls[ball3.ordinal()]);
            sleep(ms_delay);
            ledstrip.setColor(8, Balls[ball3.ordinal()]);
            sleep(ms_delay);
            // Turn off last LED in group
            ledstrip.setColor(9, Color.rgb(0,0,0));
            sleep(ms_delay);
        }
    }

    public void setBalls(Robot.ArtifactColor b1, Robot.ArtifactColor b2, Robot.ArtifactColor b3)
    {
        ball1 = LEDStickTest.ColorTable.values()[b1.ordinal()];
        ball2 = LEDStickTest.ColorTable.values()[b2.ordinal()];
        ball3 = LEDStickTest.ColorTable.values()[b3.ordinal()];
    }

    public void AllOff()
    {
        ball1= LEDStickTest.ColorTable.NONE;
        ball2= LEDStickTest.ColorTable.NONE;
        ball3= LEDStickTest.ColorTable.NONE;
    }

    public void sleep(int x)
    {
        try{
            Thread.sleep(x);
        }
        catch (InterruptedException e)
        {
            throw new RuntimeException(e);
        }
    }

}
