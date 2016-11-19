package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.Disabled;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.Range;

/**
 * Created by Richik SC on 10/29/2016.
 */

@Autonomous(name = "OmniWheel Autonomous - Velocity Vortex", group = "Comp")
@Disabled
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
    idle();
    if(motorBR.getCurrentPosition() != 0) {
      telemetry.addLine("Encoders did not reset fully");
      requestOpModeStop();
    }
    motorFL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    motorFR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    motorBL.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
    motorBR.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

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

    // Holonomic formulas

    frontLeft = -yVal - xVal - rotVal;
    frontRight = yVal - xVal - rotVal;
    backRight = yVal + xVal - rotVal;
    backLeft = -yVal + xVal - rotVal;
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

    motorFL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    motorBL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    motorFR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
    motorBR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

    resetEncoders();
    state = States.DRIVE_FORWARD_1;

    telemetry.addData("Status", "Initialized");

  }

  private void calcDesiredTicks(int desiredInches) {
    desiredTicks = (int) Math.round(desiredInches * PPI);
  }

  public void custom_loop() {
    switch (state) {
      case ROTATE_45:
        telemetry.addLine("Not supported yet - no gyro");
        break;

      case DRIVE_FORWARD_1:
        telemetry.addData("State", "DRIVE_FORWARD_1");
        if (desiredTicks == 0) {
          calcDesiredTicks(36);
        }

        int motorFLCurrPos = motorFL.getCurrentPosition();
//        telemetry.addData("Desired Ticks", desiredTicks);
//        telemetry.addData("Motor FL - Current Position", motorFLCurrPos);
//        telemetry.addData("Difference", (desiredTicks - motorFLCurrPos));
        telemetry.addData("moterFL ", motorFL.getCurrentPosition());
        telemetry.addData("moterFR ", motorFR.getCurrentPosition());
        telemetry.addData("motorBL ", motorBL.getCurrentPosition());
        telemetry.addData("motorBR ", motorBR.getCurrentPosition());
        telemetry.update();
        //motorFL.get
        if(motorFLCurrPos < desiredTicks) {
          drive(1, 1, 0);
        } else {
          resetEncoders();
          state = States.DONE;
        }
        applyDrive();
        break;

      case DONE:
        telemetry.addData("State", "DONE");
        loopBroken = true;
        setPowerZero();
        break;

      default:
        telemetry.addData("Message1", "You called " + state.toString());
        telemetry.addData("Message2", "That method is not supported yet.");
        loopBroken = true;
        break;
    }

    //applyDrive();

  }

  private void applyDrive() {

    // Thanks to FTC 4962 - Rockettes for this code.

    // Clip the right/left values so that the values never exceed +/- 1
    frontRight = Range.clip(frontRight, -1, 1);
    frontLeft = Range.clip(frontLeft, -1, 1);
    backLeft = Range.clip(backLeft, -1, 1);
    backRight = Range.clip(backRight, -1, 1);
/*
    telemetry.addData("Wheel Value Key", "(Front Left, Front Right, Back Left, Back Right)");
    telemetry.addData("Wheel Values (theoretical)",
        String.format(Locale.US, "(%d, %d, %d, %d)",
            (long)frontLeft,
            (long)frontRight,
            (long)backLeft,
            (long)backRight
        )
    );*/
    telemetry.update();
    // Write the values to the motors

    motorFL.setTargetPosition(motorFL.getCurrentPosition() + 3000);
    motorFL.setMode(DcMotor.RunMode.RUN_TO_POSITION);
    motorFL.setPower(frontLeft);
    motorFR.setPower(frontRight);
    motorBL.setPower(backLeft);
    motorBR.setPower(backRight);

    idle();

  }

  @Override
  public void runOpMode() {
    telemetry.setAutoClear(false);
  //  telemetry.setMsTransmissionInterval(1000);
    telemetry.addData("TelemetryTest", "Works!");
    telemetry.update();
    custom_init();
    waitForStart();
    while(opModeIsActive()) {
      custom_loop();
      telemetry.update();
      if (loopBroken) {
        break;
      }
      idle();
    }
  }

}
