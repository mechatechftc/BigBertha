package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

/**
 * Created by Richik SC on 10/29/2016.
 */

@TeleOp(name = "OmniWheel Drive TeleOp - Velocity Vortex", group = "Comp")
public class HolonomicTeleOp extends HolonomicRobot {
  // Left stick controls direction
  // Right stick X controls rotation
  float gamepad1LeftY;
  float gamepad1LeftX;
  float gamepad1RightX;

  @Override
  public void loop() {
    gamepad1LeftY = -gamepad1.left_stick_y;
    gamepad1LeftX = gamepad1.left_stick_x;
    gamepad1RightX = gamepad1.right_stick_x;

    drive(gamepad1LeftX, gamepad1LeftY, gamepad1RightX);
    super.loop();

  }
}
