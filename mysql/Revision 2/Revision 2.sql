-- MySQL Script generated by MySQL Workbench
-- Wed 05 Oct 2016 06:47:04 PM EDT
-- Model: New Model    Version: 1.0
-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema wearablelearning
-- -----------------------------------------------------
DROP SCHEMA IF EXISTS `wearablelearning` ;

-- -----------------------------------------------------
-- Schema wearablelearning
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `wearablelearning` DEFAULT CHARACTER SET utf8 ;
USE `wearablelearning` ;

-- -----------------------------------------------------
-- Table `wearablelearning`.`class`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wearablelearning`.`class` ;

CREATE TABLE IF NOT EXISTS `wearablelearning`.`class` (
  `classId` INT NOT NULL AUTO_INCREMENT,
  `teacherId` INT NOT NULL,
  `className` VARCHAR(45) NOT NULL,
  `grade` INT NOT NULL,
  `school` VARCHAR(45) NOT NULL,
  `year` INT NOT NULL,
  PRIMARY KEY (`classId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wearablelearning`.`teacher`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wearablelearning`.`teacher` ;

CREATE TABLE IF NOT EXISTS `wearablelearning`.`teacher` (
  `teacherId` INT NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(45) NOT NULL,
  `password` VARCHAR(45) NOT NULL,
  `firstName` VARCHAR(45) NOT NULL,
  `lastName` VARCHAR(45) NOT NULL,
  `school` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`teacherId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wearablelearning`.`student`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wearablelearning`.`student` ;

CREATE TABLE IF NOT EXISTS `wearablelearning`.`student` (
  `studentId` INT NOT NULL AUTO_INCREMENT,
  `firstName` VARCHAR(45) NOT NULL,
  `lastName` VARCHAR(45) NOT NULL,
  `gender` VARCHAR(45) NULL,
  `age` INT NULL,
  `classId` INT NOT NULL,
  PRIMARY KEY (`studentId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wearablelearning`.`games`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wearablelearning`.`games` ;

CREATE TABLE IF NOT EXISTS `wearablelearning`.`games` (
  `gameId` INT NOT NULL AUTO_INCREMENT,
  `title` VARCHAR(45) NOT NULL,
  `teamCount` INT NOT NULL,
  `playersPerTeam` INT NOT NULL,
  `inProgress` TINYINT(1) NULL,
  `startTime` MEDIUMTEXT NULL,
  PRIMARY KEY (`gameId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wearablelearning`.`gameState`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wearablelearning`.`gameState` ;

CREATE TABLE IF NOT EXISTS `wearablelearning`.`gameState` (
  `gameStateId` INT NOT NULL AUTO_INCREMENT,
  `gameId` INT NULL,
  `teamId` INT NULL,
  `playerId` INT NULL,
  `hintSetId` INT NULL,
  `textContent` VARCHAR(45) NULL,
  `ledColor` VARCHAR(45) NULL,
  `ledDuration` INT NULL,
  `buzzerState` TINYINT(1) NULL,
  `buzzerDuration` INT NULL,
  `buttonInputType` INT NULL,
  `rfid` VARCHAR(45) NULL,
  PRIMARY KEY (`gameStateId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wearablelearning`.`gameStateTransitions`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wearablelearning`.`gameStateTransitions` ;

CREATE TABLE IF NOT EXISTS `wearablelearning`.`gameStateTransitions` (
  `gameStateTransitionId` INT NOT NULL AUTO_INCREMENT,
  `gameStateId` INT NULL,
  `singlePushButtonColor` INT NULL,
  `fourButtonPush0` VARCHAR(45) NULL,
  `fourButtonPush1` VARCHAR(45) NULL,
  `fourButtonPush2` VARCHAR(45) NULL,
  `fourButtonPush3` VARCHAR(45) NULL,
  `nextGameStateTransition` VARCHAR(45) NULL,
  PRIMARY KEY (`gameStateTransitionId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wearablelearning`.`gameInstance`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wearablelearning`.`gameInstance` ;

CREATE TABLE IF NOT EXISTS `wearablelearning`.`gameInstance` (
  `gameInstanceId` INT NOT NULL AUTO_INCREMENT,
  `gameId` INT NULL,
  `studentId` INT NULL,
  `playerId` INT NULL,
  `currentGameStateId` INT NULL,
  `currentHintId` INT NULL,
  PRIMARY KEY (`gameInstanceId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wearablelearning`.`hint`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wearablelearning`.`hint` ;

CREATE TABLE IF NOT EXISTS `wearablelearning`.`hint` (
  `hintId` INT NOT NULL AUTO_INCREMENT,
  `hintSetId` INT NULL,
  `gameStateId` INT NULL,
  `sequence` INT NULL,
  `textContent` VARCHAR(45) NULL,
  `gameInstanceId` INT NOT NULL,
  PRIMARY KEY (`hintId`),
  INDEX `fk_hint_gameInstance_idx` (`gameInstanceId` ASC),
  CONSTRAINT `fk_hint_gameInstance`
    FOREIGN KEY (`gameInstanceId`)
    REFERENCES `wearablelearning`.`gameInstance` (`gameInstanceId`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wearablelearning`.`team`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wearablelearning`.`team` ;

CREATE TABLE IF NOT EXISTS `wearablelearning`.`team` (
  `teamId` INT NOT NULL AUTO_INCREMENT,
  `gameId` INT NULL,
  `teamName` VARCHAR(45) NULL,
  PRIMARY KEY (`teamId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wearablelearning`.`teamStudents`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wearablelearning`.`teamStudents` ;

CREATE TABLE IF NOT EXISTS `wearablelearning`.`teamStudents` (
  `teamStudentId` INT NOT NULL AUTO_INCREMENT,
  `playerId` INT NULL,
  `teamId` INT NULL,
  PRIMARY KEY (`teamStudentId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wearablelearning`.`mathSkills`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wearablelearning`.`mathSkills` ;

CREATE TABLE IF NOT EXISTS `wearablelearning`.`mathSkills` (
  `mathSkillId` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(45) NULL,
  `grade` INT NULL,
  `subject` VARCHAR(45) NULL,
  `domainName` VARCHAR(45) NULL,
  `clusterName` VARCHAR(45) NULL,
  `standardName` VARCHAR(45) NULL,
  `standardDescription` VARCHAR(45) NULL,
  PRIMARY KEY (`mathSkillId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wearablelearning`.`studentMathSkills`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wearablelearning`.`studentMathSkills` ;

CREATE TABLE IF NOT EXISTS `wearablelearning`.`studentMathSkills` (
  `studentMathSkillId` INT NOT NULL AUTO_INCREMENT,
  `studentId` INT NULL,
  `mathSkillId` INT NULL,
  `stat` INT NULL,
  PRIMARY KEY (`studentMathSkillId`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `wearablelearning`.`devices`
-- -----------------------------------------------------
DROP TABLE IF EXISTS `wearablelearning`.`devices` ;

CREATE TABLE IF NOT EXISTS `wearablelearning`.`devices` (
  `deviceId` INT NOT NULL AUTO_INCREMENT,
  `ipAddress` VARCHAR(45) NULL,
  `macAddress` VARCHAR(45) NULL,
  `studentId` INT NULL,
  `connected` TINYINT(1) NULL,
  PRIMARY KEY (`deviceId`))
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
