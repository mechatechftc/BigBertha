package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.HolonomicAutonomous;

import java.util.Locale;

/**
 * Created by Richik SC on 11/18/2016.
 */

@Autonomous(name = "SuperBasic", group = "Comp")
public class HolonomicBasicAutonomous extends LinearOpMode {

  private DcMotor motorFL;
  private DcMotor motorFR;
  private DcMotor motorBL;
  private DcMotor motorBR;

  private Servo pusherRight;

  private ColorSensor sensorR;

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

    pusherRight = hardwareMap.servo.get("pusher_r");
    pusherRight.setDirection(Servo.Direction.REVERSE);
    pusherRight.setPosition(-0.05);

    sensorR = hardwareMap.colorSensor.get("sensor_r");

    telemetry.addData("Status", "Initialized");

  }

  private void setPowerZero() {
    motorFL.setPower(0);
    motorFR.setPower(0);
    motorBL.setPower(0);
    motorBR.setPower(0);
  }

  public void drive(float xVal, float yVal, float rotVal) {
    // Holonomic formulas

    float frontLeft = -yVal - xVal - rotVal;
    float frontRight = yVal - xVal - rotVal;
    float backRight = yVal + xVal - rotVal;
    float backLeft = -yVal + xVal - rotVal;

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
    telemetry.update();

    // Write the values to the motors
    motorFL.setPower(frontLeft);
    motorFR.setPower(frontRight);
    motorBL.setPower(backLeft);
    motorBR.setPower(backRight);

  }

  @Override
  public void runOpMode() throws InterruptedException {
    custom_init();
    waitForStart();
    while (opModeIsActive()) {
      drive(1,1,0);
      if(!opModeIsActive()) {
        break;
      }
      sleep(2000);
      drive(0, 1, 0);
      sleep(1000);
      while (
          ((sensorR.red() - sensorR.green()) < 800) &&
          ((sensorR.red() - sensorR.blue()) < 800)
      ) {
        drive(0, 1, 0);
        sleep(150);
        idle();
      }
      pusherRight.setPosition(0);
      idle();
      pusherRight.setPosition(-0.05);
      break;
    }
    setPowerZero();
  }
}
