package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.Range;

import java.util.Locale;

/**
 * Created by Richik SC on 11/20/2016.
 */

@Autonomous(name = "Basic Autonomous", group = "Comp")
public class HolonomicUltraBasicAutonomousBlue extends LinearOpMode {
  private DcMotor motorFL;
  private DcMotor motorFR;
  private DcMotor motorBL;
  private DcMotor motorBR;
  private DcMotorPair shooterMotors;
  private DcMotor conveyorMotor;

  private Servo pusherRight;


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

    shooterMotors = new DcMotorPair(
        hardwareMap.dcMotor.get("shooter_l"),
        hardwareMap.dcMotor.get("shooter_r")
    );

    conveyorMotor = hardwareMap.dcMotor.get("conveyor");

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
    shooterMotors.setPower(1);
    sleep(2000);
    drive(0, -1, 0);
    sleep(700);
    setPowerZero();
    conveyorMotor.setPower(0.8);
    sleep(2000);
    setPowerZero();
    shooterMotors.setPower(0);
    conveyorMotor.setPower(0);
    drive(0, -1, 0);
    sleep(1300);
    setPowerZero();
  }
}
