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
 * Super Nimble Oversized Nerf Gun on Wheels
 */

@TeleOp(name = "OmniWheel Drive TeleOp - Velocity Vortex", group = "Comp")
public class HolonomicTeleOp extends HolonomicRobot {

  private static final int TARGET_ROTATIONS_PER_SECOND = 25; // 1500 RPM / 60
  private static final int PULSES_PER_REVOLUTION = 28; // 7 ppr encoders with a 4:1 gear reduction
  private static final int TARGET_PULSES_PER_SECOND =
      TARGET_ROTATIONS_PER_SECOND * PULSES_PER_REVOLUTION;

  // Left stick controls direction
  // Right stick X controls rotation
  private float gamepad1LeftY;
  private float gamepad1LeftX;
  private float gamepad1RightX;

  // Peripheral controls
  private float gamepad2RTrigger;
  private float gamepad2LTrigger;
  private boolean gamepad2AButton;
  private boolean gamepad2BButton;


  @Override
  public void init() {
    // Instantiate all objects

    super.init();

    shooterMotors.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    shooterMotors.getMotor1().setMaxSpeed(TARGET_PULSES_PER_SECOND);
    shooterMotors.getMotor2().setMaxSpeed(TARGET_PULSES_PER_SECOND);
    telemetry.addData("speed 1",  shooterMotors.getMotor1().getMaxSpeed());
    telemetry.addData("speed 2",  shooterMotors.getMotor2().getMaxSpeed());

    telemetry.addData("tpps", TARGET_PULSES_PER_SECOND);
    telemetry.addData("Status", "Initialized");
    telemetry.update();

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

      if (gamepad2AButton) {
        sweeperMotor.setPower(0.75);
      } else if (gamepad2BButton) {
        if(gamepad2.right_bumper) {
          sweeperMotor.setPower(-1);
        } else {
          sweeperMotor.setPower(-0.75);
        }
      } else {
        sweeperMotor.setPower(0);
      }

      if(gamepad2.left_bumper) {
        conveyorMotor.setPower(-0.5);
      } else {
        conveyorMotor.setPower(Range.clip(gamepad2LTrigger, 0, 1));
      }
      shooterMotors.setPower(Range.clip(gamepad2RTrigger, 0, 1));
      drive(gamepad1LeftX, gamepad1LeftY, gamepad1RightX);

      telemetry.addData("GetMotor1 getPower", shooterMotors.getMotor1().getPower());
      telemetry.addData("GetMotor2 getPower", shooterMotors.getMotor2().getPower());
      telemetry.update();

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
    super.stop();
  }


}

