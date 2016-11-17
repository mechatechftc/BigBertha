package org.firstinspires.ftc.teamcode;

import com.qualcomm.ftccommon.SoundPlayer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

import java.util.Locale;

/**
 * Created by Richik SC on 10/29/2016.
 */

@Autonomous(name = "OmniWheel Autonomous - Velocity Vortex", group = "Comp")
public class HolonomicAutonomous extends LinearOpMode {

  private static final double WHEEL_DIAMETER = 3.96;
  private static final double WHEEL_CIRCUMFERENCE = WHEEL_DIAMETER * Math.PI;
  private static final int PULSES_PER_REVOLUTION = 140;
  private static final double PPI = PULSES_PER_REVOLUTION / WHEEL_CIRCUMFERENCE; // Pulses per inch

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

  int desiredInches;
  int desiredTicks;

  private boolean loopBroken = false;

  public static enum States {
    ROTATE_45, DRIVE_FORWARD_1, STRAFE_RIGHT_1, DRIVE_FORWARD_2, DONE
  }

  private States state;

  private void resetEncoders() {
    motorFL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    motorFR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    motorBL.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    motorBR.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
    if(motorBR.getCurrentPosition() != 0) {
      telemetry.addLine("Encoders did not reset fully");
      requestOpModeStop();
    }
    motorFL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    motorFR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    motorBL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    motorBR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

    desiredInches = 0;
    desiredTicks = 0;
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

  public void custom_init() {

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

    resetEncoders();
    state = States.DRIVE_FORWARD_1;

    telemetry.addData("Status", "Initialized");
  }

  public void custom_loop() {
    switch (state) {
      case ROTATE_45:
        telemetry.addLine("Not supported yet - no gyro");
        break;

      case DRIVE_FORWARD_1:
        if (desiredTicks == 0) {
          desiredInches = 36;
          desiredTicks = (int) Math.round(desiredInches * PPI);
        }
        if(motorFL.getCurrentPosition() > desiredTicks) {
          drive(0, 1, 0);
        } else {
          resetEncoders();
          desiredInches = 0;
          desiredTicks = 0;
        }
        break;

      case DONE:
        telemetry.addLine("DONE!");
        loopBroken = true;

      default:
        telemetry.addLine("You called " + state.toString());
        telemetry.addLine("That method is not supported yet.");
        loopBroken = true;
        break;
    }

    applyDrive();

  }

  private void applyDrive() {

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

  @Override
  public void runOpMode() {
    custom_init();
    waitForStart();
    while(opModeIsActive()) {
      custom_loop();
      if (loopBroken) {
        break;
      }
    }
  }

}
