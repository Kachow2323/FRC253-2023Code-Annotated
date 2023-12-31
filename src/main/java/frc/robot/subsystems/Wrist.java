package frc.robot.subsystems;

import frc.robot.Robot;
import frc.robot.RobotContainer;
import frc.robot.Util;
import frc.robot.Constants.IntakeConstants;
import frc.robot.Constants.VisionConstants;
import frc.robot.Constants.WristConstants;
import frc.robot.Constants.ArmConstants;

// import com.ctre.phoenix.motorcontrol.ControlMode;
// import com.ctre.phoenix.motorcontrol.can.TalonSRX;
// import com.ctre.phoenix.sensors.AbsoluteSensorRange;
import com.revrobotics.AbsoluteEncoder;
import com.revrobotics.CANSparkMax;
import com.revrobotics.RelativeEncoder;
import com.revrobotics.SparkMaxAbsoluteEncoder;
import com.revrobotics.SparkMaxPIDController;
import com.revrobotics.CANSparkMax.ControlType;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;
import com.revrobotics.SparkMaxAbsoluteEncoder.Type;

import edu.wpi.first.math.controller.ArmFeedforward;
import edu.wpi.first.math.controller.ProfiledPIDController;
import edu.wpi.first.math.trajectory.TrapezoidProfile;
import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj2.command.ProfiledPIDSubsystem;
import edu.wpi.first.wpilibj2.command.Subsystem;
import com.revrobotics.CANSparkMaxLowLevel.MotorType;

public class Wrist extends ProfiledPIDSubsystem {
    
    //private static CANSparkMax conveyorMotor;
    private static final CANSparkMax wristMotor = Util.createSparkMAX(WristConstants.wristMotor, MotorType.kBrushless); //intialzing a new motor for us to use
    private static final RelativeEncoder wristEncoder = wristMotor.getEncoder(); //using the wrist motor and creating a relencoder for it
    private static final ArmFeedforward FEEDFORWARD = new ArmFeedforward(ArmConstants.kS, ArmConstants.kCos, ArmConstants.kV, ArmConstants.kA); //FeedForward System
    private SparkMaxPIDController pidController; // creates a PID controller for the wrist
    // private RelativeEncoder relWristEncoder = wristMotor.getEncoder();
    private static final SparkMaxAbsoluteEncoder wristAbsolulteEncoder = wristMotor.getAbsoluteEncoder(Type.kDutyCycle); //initialing the absolute encoder, will be used for further reference

    private static Wrist instance; 
    public static Wrist getInstance(){
        if (instance == null) instance = new Wrist();
        return instance; //Since everything runs on the command scheduler, you still have the call it even if it is global
    }

    private Wrist(){

        super(
            new ProfiledPIDController(
                WristConstants.kP, 
                WristConstants.kI, 
                WristConstants.kD,
                new TrapezoidProfile.Constraints(WristConstants.kMaxVelocity, WristConstants.kMaxAcceleration)
            ), //self-explanatory
            0
        );
        WristConstants.initialWristAngle = wristAbsolulteEncoder.getPosition();
        // intakeMotor.setInverted(true);
        wristMotor.setInverted(false); //inverts the motor controls -1 = 1 & 1 = -1
        //wristEncoder.setPositionConversionFactor(2*Math.PI/WristConstants.gearRatio);
        wristEncoder.setPosition(0); //reset wrist encoder
        pidController = wristMotor.getPIDController(); //more assigning
        pidController.setP(WristConstants.kP);
        pidController.setI(WristConstants.kI);
        pidController.setD(WristConstants.kD);
        pidController.setIZone(0.1);
        pidController.setFF(0); // FF = FeedForward
        pidController.setOutputRange(-0.6, 0.6);
        // wristMotor.setSmartCurrentLimit(5);
        //conveyorMotor = Util.createSparkMAX(ConveyorConstants.motor, MotorType.kBrushless);
        // conveyorMotor.setInverted(true);
        // conveyorMotor.burnFlash();
        /*conveyorMotor.setInverted(false);
        conveyorMotor.burnFlash();*/
        register();
    }

    /**
     * Sets the conveyor to spin at a percent of max speed
     * @param value Percent speed
     */
    /*public void setConveyor(double value) {
        conveyorMotor.set(value);
    }*/

    /**
     * Sets the intake to spin at a given voltage
     * 
     * @param value Percent of maximum voltage to send to motor
     */
    
    // add methods to spin in opposite direction
    public void setWrist(double value) {
        wristMotor.set(value); //we use this to set its speed (1 to -1)
    }
    

    public void stopWrist() {
        wristMotor.set(0);
    }

    public void periodic() {
        SmartDashboard.putNumber("WRIST: Current angle", wristEncoder.getPosition()*360.0/WristConstants.gearRatio);
        SmartDashboard.putNumber("WRIST: RAW", wristEncoder.getPosition());
        SmartDashboard.putNumber("WRIST: absolute angle", wristAbsolulteEncoder.getPosition()*36000.0/WristConstants.gearRatio);
        SmartDashboard.putNumber("WRIST: absolute position", wristAbsolulteEncoder.getPosition());
        

        // if (RobotContainer.getOperatorLeftY() > 0.0) setWrist(0.1);
        // else if (RobotContainer.getOperatorLeftY() < 0.0) setWrist(-0.1);
        // else setWrist(0);
        //This class is called every 20ms and constanty updates the SmartDashboard values
    }

    @Override
    protected void useOutput(double output, State setpoint) {
        // TODO Auto-generated method stub
        double feedforward = FEEDFORWARD.calculate(setpoint.position, setpoint.velocity);
        wristMotor.setVoltage(output + feedforward);
        
    }

    @Override
    protected double getMeasurement() {
        // TODO Auto-generated method stub
        return wristEncoder.getPosition();
        //gets the current posisition of the encoder, idk if we use this
    }
    
    // public boolean getIntakeSensor() {
    //     if(!Robot.useV3()) {
    //         return !intakePhotoelectric.get();
    //     } else {
    //         return false; //(RobotContainer.colorSensorV3.getProximity() >= ConveyorConstants.minimumProximity);
    //     }
    // }
    
    public void setWristPositionAuto(Intake.ScorePos position) {
        double encoderVal = 0.;
        int climit = 5;
        switch (position) {
            case HIGH:
                wristMotor.setSmartCurrentLimit(climit);
                encoderVal = (Intake.cone) ? WristConstants.kConeHighScorePosition : WristConstants.kCubeHighScorePosition;
                break;
            case MID:
                wristMotor.setSmartCurrentLimit(climit);
                encoderVal = (Intake.cone) ? WristConstants.kConeMidScorePosition : WristConstants.kCubeMidScorePosition;
                break;
            case LOW:
                wristMotor.setSmartCurrentLimit(climit);
                encoderVal = (Intake.cone) ? WristConstants.kConeFloorIntakePosition : WristConstants.kCubeFloorIntakePosition;
                break;
            case STOW:
                wristMotor.setSmartCurrentLimit(20); //amp max
                encoderVal = WristConstants.kStow + 0.35;
                break;
            case SHELF:
                wristMotor.setSmartCurrentLimit(climit);
                encoderVal = WristConstants.kShelf;
                break;
            default:
                break;
        }
        pidController.setReference(encoderVal /*- WristConstants.initialWristAngle*/, ControlType.kPosition);
        SmartDashboard.putNumber("Wrist SetPoint", encoderVal);
    }

    public void setWristPosition(double position) {
        pidController.setReference(position /*- WristConstants.initialWristAngle*/, ControlType.kPosition);
        SmartDashboard.putNumber("SetWristPoint", position);
    }
}
