package frc.robot.Autonomous;

import frc.robot.subsystems.*;
import frc.robot.*;
import edu.wpi.first.wpilibj2.command.Command;
import edu.wpi.first.wpilibj2.command.Commands;
import edu.wpi.first.wpilibj2.command.InstantCommand;
import edu.wpi.first.wpilibj2.command.ParallelCommandGroup;
import edu.wpi.first.wpilibj2.command.RunCommand;
import frc.robot.Constants.AutoConstants;
import frc.robot.Constants.VisionConstants;
import frc.robot.commands.DriveXMeters;
import frc.robot.commands.GridTrack;
import frc.robot.commands.TurnXDegrees;
import frc.robot.subsystems.Drivetrain;
import edu.wpi.first.wpilibj2.command.SequentialCommandGroup;
import edu.wpi.first.wpilibj2.command.WaitCommand;

/*
    Class to store autonomous sequences, including sequences such as intake
*/

public class Auto {
    public enum Selection {
        MOVEARM(1), MOVEWRIST(2), OTHER(3);
        public int val;
        private Selection(int val) {
            this.val = val;
        }
    } //auto selector by choice

    public static Command getShootCommand() { //Drive up and shoot
        return new SequentialCommandGroup(
            new GridTrack()//,
            //new DriveXMeters(AutoConstants.hubXOffset, AutoConstants.DXMConstraints[0], AutoConstants.DXMConstraints[1]), 
            // new SmartShoot(RobotContainer.getDistance()).withTimeout(4) //TODO: adjust shooter velocity based on distance
        );
    } //not used, 2022 codes

    public static Command getBackupCommand() { //Back up and find new ball
        return new SequentialCommandGroup(
            // new DriveXMeters(-AutoConstants.backupDistance, AutoConstants.DXMConstraints[0], AutoConstants.DXMConstraints[1]),
            new TurnXDegrees(180, AutoConstants.TXDConstraints[0], AutoConstants.TXDConstraints[1]) //pathweaver, unused
        );
    }

    public static Command lowConeBackup() {
        return new SequentialCommandGroup(
            new InstantCommand(() -> RobotContainer.wrist.setWristPositionAuto(Intake.ScorePos.STOW), RobotContainer.wrist),
            new InstantCommand(() -> RobotContainer.arm.setArmPositionAuto(Intake.ScorePos.STOW), RobotContainer.arm),
            new RunCommand(() -> RobotContainer.intake.set(-0.9), RobotContainer.intake).withTimeout(1),
            new WaitCommand(.5),
            new RunCommand(() -> Drivetrain.setOpenLoop(-0.25, -0.25), RobotContainer.drivetrain).withTimeout(2.0),
            new RunCommand(() -> RobotContainer.intake.set(0)).withTimeout(1)
        ); //Sequence of Events: Set arm and wrist pos to low pos. Spit out cone and then wait, drive back for a set time length and stop running intake
    }

    public static Command highConeBackup() {
        return new SequentialCommandGroup(
            new RunCommand(() -> Drivetrain.setOpenLoop(-0.25, -0.25), RobotContainer.drivetrain).withTimeout(1.), //timing
            new InstantCommand(() -> RobotContainer.wrist.setWristPositionAuto(Intake.ScorePos.HIGH), RobotContainer.wrist),
            new InstantCommand(() -> RobotContainer.arm.setArmPositionAuto(Intake.ScorePos.HIGH), RobotContainer.arm),
            new InstantCommand(() -> Drivetrain.setOpenLoop(0, 0), RobotContainer.drivetrain),
            new WaitCommand(2.),
            new RunCommand(() -> Drivetrain.setOpenLoop(0.10, 0.10), RobotContainer.drivetrain).withTimeout(1.9),
            new RunCommand(() -> RobotContainer.intake.set(1.25), RobotContainer.intake).withTimeout(1),
            new RunCommand(() -> Drivetrain.setOpenLoop(-0.10, -0.10), RobotContainer.drivetrain).withTimeout(.25),
            new WaitCommand(.25),
            new InstantCommand(() -> RobotContainer.wrist.setWristPositionAuto(Intake.ScorePos.STOW), RobotContainer.wrist),
            new InstantCommand(() -> RobotContainer.arm.setArmPositionAuto(Intake.ScorePos.STOW), RobotContainer.arm),
            new WaitCommand(.5),
            new RunCommand(() -> Drivetrain.setOpenLoop(-0.25, -0.25), RobotContainer.drivetrain).withTimeout(1.75),
            new RunCommand(() -> RobotContainer.intake.set(0)).withTimeout(2)
// "HIGH" = Mid Cone (idk why) - Timing shoud work and has been tested on practice field
// Sequence of Events: Set pos, back up slightly, set new scoring pos, drop off cone, set stow pos, drive back. pray it works.
        );
    }
}
