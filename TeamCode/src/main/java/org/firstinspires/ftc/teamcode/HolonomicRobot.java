package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import java.util.Locale;

/**
 * Created by Richik SC on 10/1/2016 for MechaTech Robotics.
 */

@TeleOp(name = "Omniwheel Robot", group = "Comp")
@Disabled
public class HolonomicRobot extends OpMode {

  protected DcMotor motorFL;
  protected DcMotor motorFR;
  protected DcMotor motorBL;
  protected DcMotor motorBR;

  private float xVal;
  private float yVal;
  private float rotVal;

  private float frontLeft;
  private float frontRight;
  private float backRight;
  private float backLeft;

  @Override
  public void init() {

    motorFL = hardwareMap.dcMotor.get("motor_fl");
    motorFR = hardwareMap.dcMotor.get("motor_fr");
    motorBL = hardwareMap.dcMotor.get("motor_bl");
    motorBR = hardwareMap.dcMotor.get("motor_br");

    motorFL.setDirection(DcMotor.Direction.REVERSE);
    motorBL.setDirection(DcMotor.Direction.REVERSE);
    motorFR.setDirection(DcMotor.Direction.REVERSE);
    motorBR.setDirection(DcMotor.Direction.REVERSE);

    motorFL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    motorBL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    motorFR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
    motorBR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

    telemetry.addData("Status", "Initialized");

  }

  @Override
  public void loop() {

    // Thanks to FTC 4962 - Rockettes for this code.

    // Holonomic formulas

    frontLeft = -yVal - xVal - rotVal;
    frontRight = yVal - xVal - rotVal;
    backRight = yVal + xVal - rotVal;
    backLeft = -yVal + xVal - rotVal;

    // Clip the right/left values so that the values never exceed +/- 1
    frontRight = Range.clip(frontRight, -1, 1);
    frontLeft = Range.clip(frontLeft, -1, 1);
    backLeft = Range.clip(backLeft, -1, 1);
    backRight = Range.clip(backRight, -1, 1);

    telemetry.addData("Wheel Value Key", "(Front Left, Front Right, Back Left, Back Right)");
    telemetry.addData("Wheel Values (theoretical)",
        String.format(Locale.US, "(%d, %d, %d, %d)",
            (long)frontLeft,
            (long)frontRight,
            (long)backLeft,
            (long)backRight
        )
    );

    // Write the values to the motors

    motorFL.setPower(frontLeft);
    motorFR.setPower(frontRight);
    motorBL.setPower(backLeft);
    motorBR.setPower(backRight);

  }

  private void setPowerZero() {
    frontLeft = 0;
    frontRight = 0;
    backLeft = 0;
    backRight = 0;
  }

  protected void drive(float x, float y, float rotation) {
    xVal = x;
    yVal = y;
    rotVal = rotation;
  }
}