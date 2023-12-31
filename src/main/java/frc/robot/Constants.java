package frc.robot;

import edu.wpi.first.math.trajectory.TrapezoidProfile.State;
import edu.wpi.first.wpilibj.I2C.Port;
import frc.robot.Units;

/*  Robot Specs:
    4 TalonFX motors
    4-6 NEO motors
    2 TalonSRX motors
*/
//Hello
public class Constants {
    public static final double dt = 0.02; // no clue
    public static final double kMaxVoltage = 12.0; //max voltage if we draw current for use

    public static class InputPorts {
        public static final int driverController = 0 /*Port 0 is driver */, operatorController = 1; /*Port 1 is Operator */
    }

    public static class AutoConstants {
        
        public static final double[] DXMConstraints = {1, 0.5}, TXDConstraints = {480, 360}; //Pathweaver Error Limits
        public static final double hubXOffset = 5, distToCargo = 5; // 2022 code stuff
    }

    public static class DriverConstants {
        /* Common drive mode settings */
        public static final double kJoystickDeadband = 0.07; // How much of joystick is "dead" zone [0,1]
        public static final double kDriveSens = 1.0; // Overall speed setting (turn down for demos) [0,1] //changed to 1.0
        public static final double kTurnInPlaceSens = 0.175; //0.175; // Maximum turn-in-place rate (in percent of max) to allow
                                                            // robot to turn to [0,1] //original is .3
        public static final double kTurnSens = .65; //.65; // Maximum normal turning rate (in percent of max) to allow robot to
                                                  // turn to [0,1]
    }
    
    public static class ArmConstants {
        public static final int actuateMotorL = 4; //change to actuatemotor
        public static final int actuateMotorR = 0; //unused i think
        /* PID Constants */
        public static double kP = 0.1; // Porpotianl Gain for Feedback Control Loops
        public static double kI = 0; // Intergral Gain for Feedback Control Loops
        public static double kD = 0.25; // Derivative Gain for Feedback Control Loops
        // Manually Eye-balled at comp :insert crying emoji:

        // Controller to apply corrections once an error is detected. P = Gain x Error, I = Gain x Duration & Magnitude. D = Gain x rate of Change
        // Simple Terms: P = Get there, I = If you are getting there slowly, get there faster, D = If going too fast, slow down 
        // Flaws: P: Steady-State error: runs close to setpoint ("asymptote"), I: Overshoots, D: None, perfect child
        // Tuning = Getting the right gain values

        /* Feedforward Constants */
        public static double kS = 0.402; //gains in units of volts, used for overcoming friction
        public static double kCos = 0.771; 
        public static double kV = 0.758; //gains in units of volts * seconds / distance
        public static double kA = 0.00717; //gains in units of volts * seconds^2 / distance (Can be ommited)
        // FeeedForward Control Loops: Let's say for example we need to use the motors, and we know using motors require power.
        // If we know in advance that we will be using power = error, Feedback Control can only react when there is an error present
        // ex: motor rpm from 100 to 200 rpm. It will draw power, about 3 volts.
        // Graphically, we increase our rpm "exponentially" and level off at our setpoint. If we pull power rn, the voltage will drop.
        // Instead of the feedback controller taking into effect and slowly brining the rpm back up, we apply Feed Forward into system to "fill the pothole"
        //FeedForawrd can also be used to apply bandwitdh limits to remove noise in sensors/sys or to decrease our disturbances.
        //It is to not that lowering the bandwitdh range, will lower the response time, which may dispaly increases in delay time until action is applied.

        /* Intake constants */
        public static double kMaxVelocity = 0.25; // Maximum velocity to turn arm at, radians per second
        public static double kMaxAcceleration = 2; // Maximum acceleration to turn arm at, radians per second per second
        public static double kArmOffset = Math.toRadians(27); 

        public static edu.wpi.first.math.trajectory.TrapezoidProfile.State kStartRads;

        public static double autoDisplacementRads;
        public static int gearRatio = 25; //physical gear ration on the SparkMAX
        public static double kShelf = -32.428291; //-32.094963;

        //arm encoder values
        // arm mid cone = -28.714066
        // wrist mid cone = 42.428131

        // wrist high cone = 39.761509
        // arm high cone = -32.071156
        public static final double kStow = -6.857;
        public static final double kCubeFloorIntakePosition = -5.285 + kStow;
        public static final double kCubeMidScorePosition = -27.499;
        public static final double kCubeHighScorePosition = -33.499;
        public static final double kConeFloorUprightIntakePosition = -2.071 + kStow;
        public static final double kConeFloorSidewaysIntakePosition = -1.786; // not used
        public static final double kConeMidScorePosition = -28.714066;
        public static final double kConeMidSidewaysScorePosition = -17.286; // not used
        public static final double kConeHighScorePosition = -35;
        //self-explanatory
    }

    public static class WristConstants {
        public static final int intakeMotor = 4; 
        public static final int wristMotor = 10;
        public static final double gearRatio = 100.0;
        public static double autoDisplacementRads;
        public static double initialWristAngle;

        public static final double kMaxVelocity = 2*Math.PI;
        public static final double kMaxAcceleration = 2*Math.PI;

        //wrist encoder values for intake
        public static final double kCubeFloorIntakePosition = 39.166; //44.832851; 
        public static final double kCubeMidScorePosition = 29.381;
        public static final double kCubeHighScorePosition = 37.833;
        public static final double kConeFloorIntakePosition = 23.524;
        public static final double kConeFloorSidewaysIntakePosition = 35.428; // not used
        public static final double kConeMidScorePosition = 42.428131;
        public static final double kConeMidSidewaysScorePosition = 20.167; // not used
        public static final double kConeHighScorePosition = 38.761509;
        public static final double kStow = 0.0;
        //self explanatory

        /* PID Constants */
        public static double kP = 0.2; // Porpotianl Gain for Feedback Control Loops
        public static double kI = 0.0; // Intergal Gain for Feedback Control Loops
        public static double kD = 0.1; // Derivative Gain for Feedback Control Loops

        // Controller to apply corrections once an error is detected. P = Gain x Error, I = Gain x Duration & Magnitude. D = Gain x rate of Change
        // Simple Terms: P = Get there, I = If you are getting there slowly, get there faster, D = If going to fast, slow down 
        // Flaws: P: Steady-State error: runs close to setpoint ("asymptote"), I: Overshoots, D: None, perfect child
        // Tuning = Getting the right gain values

        /* Feedforward Constants */
        public static double kS = 0.402;
        public static double kCos = 0.771;
        public static double kV = 0.758;
        public static double kA = 0.00717;

        // Refer to lines 52 for FeedFowardControl Schemes

        public static double kShelf = 40.118641; // 39.928169;

    }

    public static class DrivetrainConstants {
        public static final int
        /* Drivetrain motor IDs */ 
            leftMaster = 3, // TalonFX right Masters & Slaves currently reversed
            leftSlave = 1, // TalonFX ID
            rightMaster = 4, // TalonFX ID
            rightSlave = 2; // TalonFX ID
        
        /* feedforward constants */
        public static final double kV = 1.;// 2.4372; // voltage over velocity (V/(meters/second))
        public static final double kS = 1.;  // 0.66589; // voltage required to overcome friction (V)
        public static final double kA = 1.;  // 0.28968; // voltage over acceleration (V(meters/second/second))

        /* PID constants */
        public static final double kP = 3.2181;
        public static final double kI = 0;
        public static final double kD = 0;

        /* Wheels Constants */
        public static final double kTicksPerRotation = 2048 * 10.71; // Falcon 500 integrated encoder (2048 CPR)
                                                                     // multiplied by gear ratio (10.42:1)
        public static final double kWheelDiameter = Units.InchesToMeters(6);

        public static final double kMaxSpeedMPS = 3.726; // max speed in meters per second
        public static final double kMaxAcceleration = 0; //max acceleration in meters per second per second
        public static final double kTrackWidth = 0.7051868402911773; // distance between wheels
        public static final double kMaxTurnRate = -5.283706; //Max turn rate in radians per second
        public static final double kMaxCurvature = kMaxTurnRate / kMaxSpeedMPS; // Maximum turn rate in radians per meter TODO: update

        public static final double sdx = 0.2; // unused

        public static final double kPV = 0; //not 100% sure what this does, no comment
    }

    public static class IntakeConstants {
        /* Motors */
        public static final int rollerMotor = 3; // CAN ID
    }


    public static class VisionConstants { //idk if we use this
       
        /* Turn PID Constants */
        public static double kPTurn = 0.1;
        public static double kITurn = 0;
        public static double kDTurn = 0.0035;
        public static double kTurnTolerance = 1.07;

        /* Distance PID Constants */
        public static double kPDist = 0.1;
        public static double kIDist = 0;
        public static double kDDist = 0;
        public static double kDistTolerance = 0;
        public static double kYDesired = 0.0; //For proper shooting distance
        /* For calculating distance from goal */
        // public static double mountAngle = 48; //TODO: verify distance constants
        public static double goalHeightInches = 104;
        public static double limelightHeightInches = 25.5;
        // ignore
    }
}