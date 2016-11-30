package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.hardware.DcMotor;

/**
 * Created by Richik SC on 11/13/2016.
 */

public class DcMotorPair {
  private DcMotor dcMotor1;
  private DcMotor dcMotor2;

  public DcMotorPair(DcMotor a, DcMotor b) {
    this.dcMotor1 = a;
    this.dcMotor2 = b;
  }

  public void setPower(double power) {
    dcMotor1.setPower(power);
    dcMotor2.setPower(power);
  }

  public void setReverse(boolean isReverse1, boolean isReverse2) {
    dcMotor1.setDirection(isReverse1 ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
    dcMotor2.setDirection(isReverse2 ? DcMotor.Direction.REVERSE : DcMotor.Direction.FORWARD);
  }

  public void setZeroPowerBehavior(DcMotor.ZeroPowerBehavior zeroPowerBehavior) {
    dcMotor1.setZeroPowerBehavior(zeroPowerBehavior);
    dcMotor2.setZeroPowerBehavior(zeroPowerBehavior);
  }

  public boolean isBusy() {
    return dcMotor1.isBusy() || dcMotor2.isBusy();
  }

  public void setMode(DcMotor.RunMode runMode) {
    dcMotor1.setMode(runMode);
    dcMotor2.setMode(runMode);
  }

  public DcMotor.RunMode[] getMode() {
    DcMotor.RunMode[] runModes = {dcMotor1.getMode(), dcMotor2.getMode()};
    return runModes;
  }

  public void setMaxSpeed(int encoderTicksPerSecond1, int encoderTicksPerSecond2) {
    dcMotor1.setMaxSpeed(encoderTicksPerSecond1);
    dcMotor2.setMaxSpeed(encoderTicksPerSecond2);
  }

  public void setMaxSpeed(int encoderTicksPerSecond) {
    dcMotor1.setMaxSpeed(encoderTicksPerSecond);
    dcMotor2.setMaxSpeed(encoderTicksPerSecond);
  }

  public DcMotor getMotor1() {
    return dcMotor1;
  }

  public DcMotor getMotor2() {
    return dcMotor2;
  }

}
