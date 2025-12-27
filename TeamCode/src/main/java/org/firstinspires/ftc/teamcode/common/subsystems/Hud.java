package org.firstinspires.ftc.teamcode.common.subsystems;

import android.graphics.Color;

import androidx.annotation.ColorInt;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.util.RobotLog;

import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.teamcode.common.Robot;
import org.firstinspires.ftc.teamcode.common.util.QwiicLEDStick;

public class Hud {

    HardwareMap hardwareMap;
    Telemetry telemetry;

    public QwiicLEDStick ledstripRear;
    public QwiicLEDStick ledstripFront;

    public static int LED_STICK_BRIGHTNESS=8; // Brightness (1-31)
    public static int LED_STICK_TOTAL_LEDS=10; // How many Total LED there are to control
    public static int ms_delay=8;

    // ColorTable2 is the possible COLORS for 2025-2026 Decode
    public enum ColorTable {PURPLE, GREEN, NONE, RED, WHITE}

    // instance vars to hold Ball colors (2025-2026 Decode)
    public ColorTable ball1, last1 = ColorTable.NONE;
    public ColorTable ball2, last2 = ColorTable.NONE;
    public ColorTable ball3, last3 = ColorTable.NONE;
    public ColorTable aimLED, lastAimLED = ColorTable.NONE;

    // Pre-defined color values for valid Balls (2025-2026) (same order as Enum)
    public @ColorInt int[] Balls = new int[]{
            Color.parseColor("purple"), // purple
            Color.rgb(0,255,0), // green
            Color.rgb(0,0,0), // off
            Color.parseColor("red"), // red
            Color.parseColor("silver") // white
    };

    private @ColorInt int[] colors_all = new int[]{
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
            Color.rgb(0, 0, 0),
    };

    public Hud(HardwareMap hardwareMap, Telemetry telemetry) {
        this.hardwareMap = hardwareMap;
        this.telemetry = telemetry;

        initializeHUD();
    }

    public void initializeHUD() {
        ledstripRear = hardwareMap.get(QwiicLEDStick.class, "ledstrip");
        ledstripRear.changeLength(LED_STICK_TOTAL_LEDS); // limit addressable LED to number of LED installed
        ledstripRear.setBrightness(LED_STICK_BRIGHTNESS);// 10 LEDs at brightness 31 generates 660ma current
        ledstripFront = hardwareMap.get(QwiicLEDStick.class, "ledstripFront");
        ledstripFront.changeLength(LED_STICK_TOTAL_LEDS);
        ledstripFront.setBrightness(LED_STICK_BRIGHTNESS);
    }

    public void UpdateBallUI2() {


        if (ball1 != last1 || ball2 != last2 || ball3 != last3 || aimLED != lastAimLED)
        {
            RobotLog.d("HUD: %s %s %s %s", ball1,ball2,ball3,aimLED);
            if (ball1 != last1)
            {
                last1 = ball1;
                colors_all[0] = Balls[ball1.ordinal()];
                colors_all[1] = Balls[ball1.ordinal()];
                colors_all[2] = Balls[ball1.ordinal()];
            }
            if (ball2 != last2)
            {
                last2 = ball2;
                colors_all[3] = Balls[ball2.ordinal()];
                colors_all[4] = Balls[ball2.ordinal()];
                colors_all[5] = Balls[ball2.ordinal()];
            }
            if (ball3 != last3)
            {
                last3 = ball3;
                colors_all[6] = Balls[ball3.ordinal()];
                colors_all[7] = Balls[ball3.ordinal()];
                colors_all[8] = Balls[ball3.ordinal()];
            }
            if (aimLED != lastAimLED)
            {
                lastAimLED = aimLED;
                colors_all[9] = Balls[aimLED.ordinal()];
            }

//            RobotLog.d("HUD: %d %d %d | %d %d %d | %d %d %d | %d",
//                    colors_all[0], colors_all[1], colors_all[2],
//                    colors_all[3], colors_all[4], colors_all[5],
//                    colors_all[6], colors_all[7], colors_all[8],
//                    colors_all[9]
//            );
            ledstripRear.setColors(colors_all);
            sleep(ms_delay);
            ledstripFront.setColors(colors_all);
            sleep(ms_delay);
        }

    }


    // This is the older UpdateUI routine (no longer used). kept here to show how to address individual
    // LED colors and the associated delay between I2C messages to prevent confusing the LED stick
    public void UpdateBallUI() {


        // Sleep for 5ms between command to avoid overwhelming i2c device with
        // messages (0 causes glitches & strip freezes)

        if (ball1 != last1 ) { // only send update if different

            last1 = ball1;
            // Ball1 (bottom) LEDs 0-2
            ledstripRear.setColor(0, Balls[ball1.ordinal()]);
            ledstripFront.setColor(0, Balls[ball1.ordinal()]);

            sleep(ms_delay);
            ledstripRear.setColor(1, Balls[ball1.ordinal()]);
            ledstripFront.setColor(1, Balls[ball1.ordinal()]);
            sleep(ms_delay);
            ledstripRear.setColor(2, Balls[ball1.ordinal()]);
            ledstripFront.setColor(2, Balls[ball1.ordinal()]);
            sleep(ms_delay);
        }

        if (ball2 != last2) {
            last2 = ball2;
            // Ball2
            ledstripRear.setColor(3, Balls[ball2.ordinal()]);
            ledstripFront.setColor(3, Balls[ball2.ordinal()]);

            sleep(ms_delay);
            ledstripRear.setColor(4, Balls[ball2.ordinal()]);
            ledstripFront.setColor(4, Balls[ball2.ordinal()]);

            sleep(ms_delay);
            ledstripRear.setColor(5, Balls[ball2.ordinal()]);
            ledstripFront.setColor(5, Balls[ball2.ordinal()]);

            sleep(ms_delay);

        }
        if (ball3 != last3) {
            // Drop2_sensor (bottom) LEDs 5-8
            last3 = ball3;
            ledstripRear.setColor(6, Balls[ball3.ordinal()]);
            ledstripFront.setColor(6, Balls[ball3.ordinal()]);

            sleep(ms_delay);
            ledstripRear.setColor(7, Balls[ball3.ordinal()]);
            ledstripFront.setColor(7, Balls[ball3.ordinal()]);

            sleep(ms_delay);
            ledstripRear.setColor(8, Balls[ball3.ordinal()]);
            ledstripFront.setColor(8, Balls[ball3.ordinal()]);
            sleep(ms_delay);
        }
        // set aiming led
        if (aimLED != lastAimLED) {
            lastAimLED = aimLED;
            ledstripRear.setColor(9, Balls[aimLED.ordinal()]);
            ledstripFront.setColor(9, Balls[aimLED.ordinal()]);
            sleep(ms_delay);
        }

    }

    public void setBalls(Robot.ArtifactColor b1, Robot.ArtifactColor b2, Robot.ArtifactColor b3)
    {
        ball1 = Hud.ColorTable.values()[b1.ordinal()];
        ball2 = Hud.ColorTable.values()[b2.ordinal()];
        ball3 = Hud.ColorTable.values()[b3.ordinal()];
    }

    public void setAimIndicator(Boolean aimOn)
    {
        if (aimOn) {
           aimLED = Hud.ColorTable.values()[Hud.ColorTable.RED.ordinal()];
        }
        else {
            aimLED = Hud.ColorTable.values()[Hud.ColorTable.NONE.ordinal()];
        }
    }

    public void AllOff()
    {
        ball1= Hud.ColorTable.NONE;
        ball2= Hud.ColorTable.NONE;
        ball3= Hud.ColorTable.NONE;
        aimLED= Hud.ColorTable.NONE;
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
