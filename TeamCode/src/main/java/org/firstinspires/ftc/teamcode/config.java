package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotor.RunMode;
import com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior;
import com.qualcomm.robotcore.hardware.DcMotorSimple.Direction;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.ClassFactory;
import org.firstinspires.ftc.robotcore.external.Telemetry;
import org.firstinspires.ftc.robotcore.external.navigation.DistanceUnit;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaLocalizer;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackable;
import org.firstinspires.ftc.robotcore.external.navigation.VuforiaTrackables;
import org.firstinspires.ftc.robotcore.external.tfod.TFObjectDetector;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * This is the config file for the robot. It also has (or will have) many useful functions in it.
 * <p>
 * For example rather than typing: <br>
 * <br>
 * <code>left_front = hardware.dcMotor.get("left_drive");<br>
 * right_front = hardware.dcMotor.get("right_drive");<br>
 * ...</code>
 * <br>
 * <br>
 * you can just type: <br>
 * <br>
 * <code>private config robot = new config();<br>
 * robot.ConfigureRobotHardware(this.hardwareMap);</code>
 * <br>
 * <br>
 * That's literally the configuration process done!
 * <br>
 * <br>
 * <br>
 * <p>
 * <p>
 * Created by Stephen Ogden on 9/13/18.
 * Modified on 9/20/18.
 * <p>
 * FTC 6128 | 7935
 * FRC 1595
 */
class config {

    // Field measurements
    private final float mmPerInch = 25.4f;
    private final float mmFTCFieldWidth = (12 * 6) * mmPerInch; // The width of the FTC field (from the center point to the outer panels)
    private final float mmTargetHeight = (6) * mmPerInch; // The height of the center of the target image above the floor

    // DcMotors and servos used on the robot
    DcMotor left_front, right_front, left_back, right_back;
    DcMotor IO_Motor, armMotorL, armMotorR, armMotorExtend;
    Servo IO_Servo_Left, IO_Servo_Right;
    String target = "None";
    TFObjectDetector objectDetector;
    // Version 2 color sensor
    ColorSensor sensorColorLeft;
    ColorSensor sensorColorRight;
    DistanceSensor sensorDistanceLeft;
    DistanceSensor sensorDistanceRight;
    List<VuforiaTrackable> VisionTargets = new ArrayList<>();
    // Stuff for vision
    private VuforiaTrackables pictures;
    private VuforiaLocalizer vuforia;
    public VuforiaTrackable BlueRover, RedFootprint, FrontCraters, BackSpace;
    private boolean VisionIsActive = false;
    private int cameraViewID;
    // Telemetry stuff
    private Telemetry telemetry;

    config(Telemetry t) {
        this.telemetry = t;
    }

    /**
     * Goes through the configureation of the robot, even updating the telemetry :)
     * <br>
     * <br>
     *
     * @param hardware - The HardwareMap of the robot. Just type <code>this.hardwareMap</code> for this parameter.
     */
    void ConfigureRobtHardware(HardwareMap hardware) {

        // Declare and setup left_front
        status("Configuring left front motor");
        left_front = hardware.dcMotor.get("left front");
        left_front.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
        left_front.setMode(RunMode.RUN_WITHOUT_ENCODER);
        left_front.setDirection(Direction.FORWARD);

        // Declare and setup right_front
        status("Configuring right front motor");
        right_front = hardware.dcMotor.get("right front");
        right_front.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
        right_front.setMode(RunMode.RUN_WITHOUT_ENCODER);
        right_front.setDirection(Direction.REVERSE);

        // Declare and setup left_back
        status("Configuring left back motor");
        left_back = hardware.dcMotor.get("left back");
        left_back.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
        left_back.setMode(RunMode.RUN_WITHOUT_ENCODER);
        left_back.setDirection(Direction.FORWARD);

        // Declare and setup right_back
        status("Configuring right back motor");
        right_back = hardware.dcMotor.get("right back");
        right_back.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
        right_back.setMode(RunMode.RUN_WITHOUT_ENCODER);
        right_back.setDirection(Direction.REVERSE);

        //Declare and setup armMotorL and armMotorR
        armMotorL = hardware.dcMotor.get("arm motor left");
        armMotorL.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
        armMotorL.setMode(RunMode.RUN_WITHOUT_ENCODER);
        armMotorL.setDirection(Direction.REVERSE);

        armMotorR = hardware.dcMotor.get("arm motor right");
        armMotorR.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
        armMotorR.setMode(RunMode.RUN_WITHOUT_ENCODER);
        armMotorR.setDirection(Direction.FORWARD);

        armMotorExtend = hardware.dcMotor.get("arm motor extend");
        armMotorExtend.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
        armMotorExtend.setMode(RunMode.RUN_WITHOUT_ENCODER);
        armMotorExtend.setDirection(Direction.FORWARD);

        // Declare the left servo for the intake
        status("Setting up left servo");
        IO_Servo_Left = hardware.servo.get("IO Servo Left");

        // Declare the right servo for the intake
        status("Setting up right servo");
        IO_Servo_Right = hardware.servo.get("IO Servo Right");

        //Declare and setup Intake Motor
        status("Configuring Intake Motor");
        IO_Motor = hardware.dcMotor.get("IO motor");
        IO_Motor.setZeroPowerBehavior(ZeroPowerBehavior.BRAKE);
        IO_Motor.setMode(RunMode.RUN_WITHOUT_ENCODER);
        IO_Motor.setDirection(Direction.REVERSE);

        status("Setting up color sensor");
        sensorColorLeft = hardware.colorSensor.get("color sensor left");
        sensorDistanceLeft = hardware.get(DistanceSensor.class, "color sensor left");
        sensorColorRight = hardware.colorSensor.get("color sensor right");
        sensorDistanceRight = hardware.get(DistanceSensor.class, "color sensor right");

        // Update telemetry to signal done!
        status("Done!");
    }

    /**
     * Updates the telemetry automatically the appropriate values (basically items that are not null)
     */
    void updateTelemetry() {

        if (left_front != null) {
            telemetry.addData("Left front power", String.format(Locale.US, "%.2f", left_front.getPower()));
        }
        if (right_front != null) {
            telemetry.addData("Right front power", String.format(Locale.US, "%.2f", right_front.getPower()));
        }
        if (left_back != null) {
            telemetry.addData("Left back power", String.format(Locale.US, "%.2f", this.left_back.getPower()));
        }
        if (right_back != null) {
            telemetry.addData("Right back power", String.format(Locale.US, "%.2f", this.right_back.getPower()));
        }

        telemetry.addData("", ""); // Add a space between the drive power and the encoder values

        if (left_front != null) {
            if (left_front.getMode() == RunMode.RUN_USING_ENCODER) {
                telemetry.addData("Left front target, current location (displacement)", String.format("%s, %s (%s)", this.left_front.getTargetPosition(), this.left_front.getCurrentPosition(), Math.abs(this.left_front.getCurrentPosition() - this.left_front.getTargetPosition())));
            }
        }
        if (right_front != null) {
            if (right_front.getMode() == RunMode.RUN_USING_ENCODER) {
                telemetry.addData("Right front target, current location (displacement)", String.format("%s, %s (%s)", this.right_front.getTargetPosition(), this.right_front.getCurrentPosition(), Math.abs(this.right_front.getCurrentPosition() - this.right_front.getTargetPosition())));
            }
        }
        if (left_back != null) {
            if (left_back.getMode() == RunMode.RUN_USING_ENCODER) {
                telemetry.addData("Left back target, current location (displacement)", String.format("%s, %s (%s)", this.left_back.getTargetPosition(), this.left_back.getCurrentPosition(), Math.abs(this.left_back.getCurrentPosition() - this.right_front.getTargetPosition())));
            }
        }
        if (right_back != null) {
            if (right_back.getMode() == RunMode.RUN_USING_ENCODER) {
                telemetry.addData("Right back target, current location (displacement)", String.format("%s, %s (%s)", this.right_back.getTargetPosition(), this.right_back.getCurrentPosition(), Math.abs(this.right_back.getCurrentPosition() - this.right_back.getTargetPosition())));
            }
        }

        telemetry.addData("", ""); // Add a space between encoder values and servo values

        if (IO_Servo_Left != null) {
            telemetry.addData("IO Servo Left target position", IO_Servo_Left.getPosition());
        }
        if (IO_Servo_Right != null) {
            telemetry.addData("IO Servo Right target position", IO_Servo_Right.getPosition());
        }

        telemetry.addData("", ""); // Add a space between servo values and color sensor stuff

        

        telemetry.addData("", ""); // Add a space between the color sensor stuff and the vision stuff

        if (VisionIsActive) {
            telemetry.addData("Current visible target", target);
        }

        // Update the telemetry
        telemetry.update();

    }

    /**
     * Checks all the motors have reached their target positions, withing the discrepancy
     *
     * @param discrepancy -- The number of ticks the current position is allowed to be within in order to qualify it as at target
     * @return -- Whether all motors have reached their targets
     */
    boolean isAtTarget(int discrepancy) {
        return ((Math.abs(this.left_front.getCurrentPosition() - this.left_front.getTargetPosition()) <= discrepancy &&
                (Math.abs(this.right_front.getCurrentPosition() - this.right_front.getTargetPosition()) <= discrepancy) &&
                (Math.abs(this.left_back.getCurrentPosition() - this.left_back.getTargetPosition()) <= discrepancy) &&
                (Math.abs(this.right_back.getCurrentPosition() - this.right_back.getTargetPosition()) <= discrepancy)));
    }


    /**
     * This function sets up everything you need to run vision. Be sure to call this before calling <code>StartTrackingVisionTargets<code/>.
     *
     * @param hardware -- The HardwareMap of the robot. Just type <code>this.hardwareMap</code> for this parameter.
     */
    void InitializeVision(HardwareMap hardware, boolean useTF) {

        status("Setting up vision system");

        // Get the camera monitor id for the app
        int cameraMonitorViewId = hardware.appContext.getResources().getIdentifier("cameraMonitorViewId", "id", hardware.appContext.getPackageName());

        // Create a variable for passing parameters, such as the key for vuforia, and what camera we want to use (Back vs Selfie camera)
        status("Setting up parameters");
        VuforiaLocalizer.Parameters parameters = new VuforiaLocalizer.Parameters(cameraMonitorViewId);
        parameters.vuforiaLicenseKey = "AUgZTU3/////AAAAGaQ5yTo6EkZqvsH9Iel0EktQjXWAZUz3q3FPq22sUTrmsYCccs/mjYiflQBH2u7lofbTxe4BxTca9o2EOnNwA8dLGa/yL3cUgDGjeRfXuwZUCpIG6OEKhiPU5ntOpT2Nr5uVkT3vs2uRr7J6G7YoaGHLw2i1wGncRaw37rZyO03QRh0ZatdKIiK1ItuvJkP3qfUJwQwcpROwa+ZdDNQDbpU6WTL+kPZpnkgR8oLcu+Na1lWrbJ2ZTYG8eUjoIGowbVVGJgORHJazy6/7MbYH268h9ZC4vZ12ItyDK/GlPRTeQWdcZRlWfzAAFwNrjmdjWv9hMuOMoWxo2Y2Rw1Fwii4ohLyRmcQa/wAWY+AOEL14";
        parameters.cameraDirection = VuforiaLocalizer.CameraDirection.BACK;

        // Create the pictures engine and pass in the provided parameters
        status("Applying parameters");
        vuforia = ClassFactory.getInstance().createVuforia(parameters);

        // Load the pictures targets from the engine, that way we can track them
        status("Loading assets");
        pictures = vuforia.loadTrackablesFromAsset("RoverRuckus");

        // Setup the images one by one
        // This *could* be done all at once, but one by one keeps things simple
        BlueRover = pictures.get(0);
        BlueRover.setName("Blue rover");

        RedFootprint = pictures.get(1);
        RedFootprint.setName("Red footprint");

        FrontCraters = pictures.get(2);
        FrontCraters.setName("Front craters");

        BackSpace = pictures.get(3);
        BackSpace.setName("Back space");

        // Add all the trackables to a list
        VisionTargets.addAll(pictures);

        status("Getting camera view id");
        cameraViewID = hardware.appContext.getResources().getIdentifier("tfodMonitorViewId", "id", hardware.appContext.getPackageName());


        // TODO: We could also add location data to get the position of the images on the field as well as the robot

        if (useTF) {
            InitTensorFlow();
        }

        status("Done!");
    }

    /**
     * Starts tracking the vision targets. This is quite taxing on the phone, so be sure to end it as soon as you can
     */
    void StartTrackingVisionTargets() {
        pictures.activate();
        if (objectDetector != null) {
            objectDetector.activate();
        }
        VisionIsActive = true;
    }

    /**
     * Stops tracking the vision targets
     */
    void StopTrackingVisionTargets() {
        pictures.deactivate();
        if (objectDetector != null) {
            objectDetector.deactivate();
            objectDetector.shutdown();
        }
        VisionIsActive = false;
    }

    private void InitTensorFlow() {
        status("Setting parameters");
        TFObjectDetector.Parameters parameters = new TFObjectDetector.Parameters(cameraViewID);
        status("Creating object detector");
        objectDetector = ClassFactory.getInstance().createTFObjectDetector(parameters, vuforia);
        status("Loading object assets");
        objectDetector.loadModelFromAsset("RoverRuckus.tflite", "Gold Mineral");
    }

    private void status(String string) {
        telemetry.addData("Status", string);
        telemetry.update();
    }

}