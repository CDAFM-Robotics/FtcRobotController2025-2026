package org.firstinspires.ftc.teamcode.testing.archived;

import android.graphics.Color;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Blinker;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.common.util.QwiicLEDStick;
import androidx.annotation.ColorInt;

import java.util.Random;


@TeleOp (group = "testing", name = "LEDStickTest")
@Disabled
public class LEDStickTest extends LinearOpMode {
  private Blinker control_Hub;
  public ElapsedTime elapsedTime = new ElapsedTime();

  // Define the LEDStrip
  public QwiicLEDStick ledstrip;

  public static int LED_STICK_BRIGHTNESS=6; // Brightness (1-31)
  public static int LED_STICK_TOTAL_LEDS=10; // How many Total LED there are to control

  // ColorTable2 is the possible COLORS for 2025-2026 Decode
  public enum ColorTable {PURPLE, GREEN, RED, WHITE, NONE}

  // instance vars to hold Ball colors (2025-2026 Decode)
  public ColorTable ball1, last1 = ColorTable.NONE;
  public ColorTable ball2, last2 = ColorTable.NONE;
  public ColorTable ball3, last3 = ColorTable.NONE;


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
      // Lets generate some random "balls" at 2hz refresh rate
      if (elapsedTime.milliseconds() >= 500) {
        ball1 = randomBall();
        ball2 = randomBall();
        ball3 = randomBall();
        UpdateBalls();
        elapsedTime.reset();
      }

    }
  }


  public void UpdateBalls() {

    // Sleep for 5ms between command to avoid overwhelming i2c device with
    // messages (0 causes glitches & strip freezes)

    if (ball1 != last1 ) { // only send update if different

      last1 = ball1;
      // Ball1 (bottom) LEDs 0-2
      ledstrip.setColor(0, Balls[ball1.ordinal()]);
      sleep(5);
      ledstrip.setColor(1, Balls[ball1.ordinal()]);
      sleep(5);
      ledstrip.setColor(2, Balls[ball1.ordinal()]);
      sleep(5);
    }

    if (ball2 != last2) {
      last2 = ball2;
      // Ball2
      ledstrip.setColor(3, Balls[ball2.ordinal()]);
      sleep(5);
      ledstrip.setColor(4, Balls[ball2.ordinal()]);
      sleep(5);
      ledstrip.setColor(5, Balls[ball2.ordinal()]);
      sleep(5);

    }
    if (ball3 != last3) {
      // Drop2_sensor (bottom) LEDs 5-8
      last3 = ball3;
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
  public static ColorTable randomBall()  {
    Random random = new Random();
    return ColorTable.values()[random.nextInt(2)];
  }
}
