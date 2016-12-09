package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.util.DcMotorPair;

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

  protected DcMotorPair shooterMotors;
  protected DcMotor conveyorMotor;
  protected DcMotor sweeperMotor;

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

    motorFL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    motorBL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    motorFR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    motorBR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

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
