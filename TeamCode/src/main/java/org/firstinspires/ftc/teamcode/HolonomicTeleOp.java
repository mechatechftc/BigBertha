package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.util.DcMotorPair;

import java.io.PrintWriter;
import java.io.StringWriter;

/**
 * Created by Richik SC on 10/29/2016.
 */

@TeleOp(name = "OmniWheel Drive TeleOp - Velocity Vortex", group = "Comp")
public class HolonomicTeleOp extends HolonomicRobot {
  // Left stick controls direction
  // Right stick X controls rotation
  private float gamepad1LeftY;
  private float gamepad1LeftX;
  private float gamepad1RightX;
  private float gamepad2RTrigger;
  private float gamepad2LTrigger;
  private boolean gamepad2AButton;
  private boolean gamepad2BButton;
  private ColorSensor sensorRGB;
  private float hsvValues[] = {0F, 0F, 0F};
  private float values[] = hsvValues;
  private View relativeLayout;

  private DcMotorPair shooterMotors;
  private DcMotor conveyorMotor;
  private DcMotor sweeperMotor;
  private Servo pusherRight;
  private Servo pusherLeft;

  @Override
  public void init() {
    // Instantiate all objects

    relativeLayout = ((Activity) hardwareMap.appContext)
        .findViewById(com.qualcomm.ftcrobotcontroller.R.id.RelativeLayout);

    // Use custom DcMotorPair class here to prevent any mistakes
    shooterMotors = new DcMotorPair(
        hardwareMap.dcMotor.get("shooter_l"),
        hardwareMap.dcMotor.get("shooter_r")
    );
    /* Shooter motors are reversed physically by reversing the polarity of the wires
    No need to reverse in code, otherwise use:
    shooterMotors.setReverse(true, false); */

    conveyorMotor = hardwareMap.dcMotor.get("conveyor");
    sweeperMotor = hardwareMap.dcMotor.get("sweeper");

    pusherRight = hardwareMap.servo.get("pusher_r");
    pusherLeft = hardwareMap.servo.get("pusher_l");
    pusherRight.setDirection(Servo.Direction.REVERSE);

    pusherRight.setPosition(-0.025);

    sensorRGB = hardwareMap.colorSensor.get("sensor_r");

    super.init();
  }

  @Override
  public void loop() {

    // Retrieve gamepad values
    gamepad1LeftY = -gamepad1.left_stick_y;
    gamepad1LeftX = gamepad1.left_stick_x;
    gamepad1RightX = 0.9F * gamepad1.right_stick_x;
    gamepad2RTrigger = gamepad2.right_trigger;
    gamepad2LTrigger = gamepad2.left_trigger;
    gamepad2AButton = gamepad2.a;
    gamepad2BButton = gamepad2.b;
    boolean gp2x = gamepad2.x;


    // Proper exception handling, FTC does not show full stack trace
    try {

      Color.RGBToHSV(
          (sensorRGB.red() * 255) / 800,
          (sensorRGB.green() * 255) / 800,
          (sensorRGB.blue() * 255) / 800,
          hsvValues
      );

      telemetry.addData("Clear", sensorRGB.alpha());
      telemetry.addData("Red  ", sensorRGB.red());
      telemetry.addData("Green", sensorRGB.green());
      telemetry.addData("Blue ", sensorRGB.blue());

      if (gamepad2AButton) {
        sweeperMotor.setPower(1);
      } else if (gamepad2BButton) {
        sweeperMotor.setPower(-1);
      } else {
        sweeperMotor.setPower(0);
      }

      if (gp2x) {
        pusherRight.setPosition(0);
      } else {
        pusherRight.setPosition(-0.05);
      }

      if (gamepad2.y) {
        pusherLeft.setPosition(0);
      } else {
        pusherLeft.setPosition(-0.05);
      }

      conveyorMotor.setPower(Range.clip(gamepad2LTrigger, 0, 1));
      shooterMotors.setPower(Range.clip(gamepad2RTrigger, 0, 1));
      drive(gamepad1LeftX, gamepad1LeftY, gamepad1RightX);

      relativeLayout.post(new Runnable() {
        public void run() {
          relativeLayout.setBackgroundColor(Color.HSVToColor(0xff, values));
        }
      });

      super.loop();
    } catch (Exception ex) {

      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      ex.printStackTrace(pw);
      telemetry.addData("Error", ex.getClass().toString() + "\n" + sw.toString());
      requestOpModeStop();

    }
  }

  @Override
  public void stop() {
    relativeLayout.post(new Runnable() {
      public void run() {
        relativeLayout.setBackgroundColor(Color.WHITE);
      }
    });
    super.stop();
  }


}

