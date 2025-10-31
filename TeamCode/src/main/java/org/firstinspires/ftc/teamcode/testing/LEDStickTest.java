package org.firstinspires.ftc.teamcode.testing;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.common.util.QwiicLEDStick;
import androidx.annotation.ColorInt;

import java.util.Random;


@TeleOp (group = "testing", name = "LEDStickTest")

public class LEDStickTest extends LinearOpMode {
  private Blinker control_Hub;
  public ElapsedTime elapsedTime = new ElapsedTime();

  // Define the LEDStrip
  public QwiicLEDStick ledstrip;

  public static int LED_STICK_BRIGHTNESS=6; // Brightness (1-31)
  public static int LED_STICK_TOTAL_LEDS=10; // How many Total LED there are to control

  // ColorTable is an enum that define the possible color sensor states. (2023)
  public enum ColorTable { PURPLE, GREEN, YELLOW, WHITE, NONE }

  // ColorTable2 is the possible COLORS for 2025-2026 Decode
  public enum ColorTable2 {PURPLE, GREEN, RED, WHITE, NONE}

  // instance vars to hold the current (and last changed) color sensor detections (2023)
  public ColorTable drop1_sensor, last1 = ColorTable.NONE;
  public ColorTable drop2_sensor, last2 = ColorTable.NONE;

  // instance vars to hold Ball colors (2025-2026 Decode)
  public ColorTable2 ball1, blast1 = ColorTable2.NONE;
  public ColorTable2 ball2, blast2 = ColorTable2.NONE;
  public ColorTable2 ball3, blast3 = ColorTable2.NONE;


  // Pre-defined color values for valid pixel colors (Same order as Enum)
  public @ColorInt int[] Pixels = new int[]{Color.parseColor("purple"),  // purple
          Color.rgb(0, 255, 0), // green
          Color.rgb(255, 255, 0), // yellow
          Color.parseColor("silver"), // white
          Color.rgb(0, 0, 0) // off
  };

  // Pre-defined color values for valid Balls (2025-2026) (same order as Enum)
  public @ColorInt int[] Balls = new int[]{Color.parseColor("purple"), // purple
          Color.rgb(0,255,0), // green
          Color.rgb(255,0,0), // red
          Color.parseColor("silver"), // white
          Color.rgb(0,0,0) // off
  };

  @Override
  public void runOpMode() {

    // initialize the SparkFun Qwiic LED Strip Apa102C
    // remember to add the i2c port in robot config (port #1 = i2c bus 1, etc)
    ledstrip = hardwareMap.get(QwiicLEDStick.class, "ledstrip");
    ledstrip.changeLength(LED_STICK_TOTAL_LEDS); // limit addressable LED to number of LED installed
    ledstrip.setBrightness(LED_STICK_BRIGHTNESS);// 10 LEDs at brightness 31 generates 660ma current

    waitForStart();
    if (isStopRequested()) {
      return;
    }
    resetRuntime();

    while (opModeIsActive()) {
      // Lets generate some random "pixels" at 2hz refresh rate
      if (elapsedTime.milliseconds() >= 500) {
    //    drop2_sensor = randomPixel();
    //    drop1_sensor = randomPixel();
    //    UpdatePixels();
        ball1 = randomBall();
        ball2 = randomBall();
        ball3 = randomBall();
        UpdateBalls();
        elapsedTime.reset();
      }

    }
  }

  public void UpdatePixels() {

    // Sleep for 5ms between command to avoid overwhelming i2c device with
    // messages (0 causes glitches & strip freezes)

    if (drop1_sensor != last1 ) { // only send update if different

      last1 = drop1_sensor;
      // Drop1_sensor (bottom) LEDs 0-3
      ledstrip.setColor(0, Pixels[drop1_sensor.ordinal()]);
      sleep(5);
      ledstrip.setColor(1, Pixels[drop1_sensor.ordinal()]);
      sleep(5);
      ledstrip.setColor(2, Pixels[drop1_sensor.ordinal()]);
      sleep(5);
      ledstrip.setColor(3, Pixels[drop1_sensor.ordinal()]);
      sleep(5);
      // Turn off last LED in group.
      ledstrip.setColor(4, Color.rgb(0,0,0));
      sleep(5);

    }
    if (drop2_sensor != last2) {
      // Drop2_sensor (bottom) LEDs 5-8
      ledstrip.setColor(5, Pixels[drop2_sensor.ordinal()]);
      sleep(5);
      ledstrip.setColor(6, Pixels[drop2_sensor.ordinal()]);
      sleep(5);
      ledstrip.setColor(7, Pixels[drop2_sensor.ordinal()]);
      sleep(5);
      ledstrip.setColor(8, Pixels[drop2_sensor.ordinal()]);
      sleep(5);
      // Turn off last LED in group
      ledstrip.setColor(9, Color.rgb(0,0,0));
      sleep(5);
    }
  }

  public void UpdateBalls() {

    // Sleep for 5ms between command to avoid overwhelming i2c device with
    // messages (0 causes glitches & strip freezes)

    if (ball1 != blast1 ) { // only send update if different

      blast1 = ball1;
      // Ball1 (bottom) LEDs 0-2
      ledstrip.setColor(0, Balls[ball1.ordinal()]);
      sleep(5);
      ledstrip.setColor(1, Balls[ball1.ordinal()]);
      sleep(5);
      ledstrip.setColor(2, Balls[ball1.ordinal()]);
      sleep(5);
    }

    if (ball2 != blast2) {
      blast2 = ball2;
      // Ball2
      ledstrip.setColor(3, Balls[ball2.ordinal()]);
      sleep(5);
      ledstrip.setColor(4, Balls[ball2.ordinal()]);
      sleep(5);
      ledstrip.setColor(5, Balls[ball2.ordinal()]);
      sleep(5);

    }
    if (ball3 != blast3) {
      // Drop2_sensor (bottom) LEDs 5-8
      blast3 = ball3;
      ledstrip.setColor(6, Balls[ball3.ordinal()]);
      sleep(5);
      ledstrip.setColor(7, Balls[ball3.ordinal()]);
      sleep(5);
      ledstrip.setColor(8, Balls[ball3.ordinal()]);
      sleep(5);
      // Turn off last LED in group
      ledstrip.setColor(9, Color.rgb(0,0,0));
      sleep(5);
    }
  }


  // (optional) Testing Helper generates random pixel colors
  public static ColorTable randomPixel()  {
    Random random = new Random();
    return ColorTable.values()[random.nextInt(ColorTable.values().length)];
  }

  // (optional) Testing Helper generates random pixel colors
  public static ColorTable2 randomBall()  {
    Random random = new Random();
    return ColorTable2.values()[random.nextInt(2)];
  }
}
