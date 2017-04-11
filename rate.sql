-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='TRADITIONAL,ALLOW_INVALID_DATES';

-- -----------------------------------------------------
-- Schema mydb
-- -----------------------------------------------------
-- -----------------------------------------------------
-- Schema rate
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema rate
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `rate` DEFAULT CHARACTER SET utf8 ;
USE `rate` ;

-- -----------------------------------------------------
-- Table `rate`.`account_recovery`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rate`.`account_recovery` (
  `email` VARCHAR(45) NOT NULL,
  `temp_password` VARCHAR(100) NULL DEFAULT NULL,
  `datetime` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`datetime`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `rate`.`announcements`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rate`.`announcements` (
  `announcement_id` INT(11) NOT NULL AUTO_INCREMENT,
  `announcement_date` VARCHAR(45) NULL DEFAULT NULL,
  `announcement_content` VARCHAR(250) NULL DEFAULT NULL,
  PRIMARY KEY (`announcement_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 44
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `rate`.`courses`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rate`.`courses` (
  `course_id` INT(11) NOT NULL AUTO_INCREMENT,
  `course_subject` VARCHAR(45) NOT NULL,
  `course_number` INT(11) NOT NULL,
  `course_name` VARCHAR(45) NOT NULL,
  `course_professor_id` INT(11) NULL DEFAULT '0',
  `course_term` VARCHAR(45) NOT NULL DEFAULT 'Spring 2017',
  PRIMARY KEY (`course_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 33
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `rate`.`professor_review`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rate`.`professor_review` (
  `student_id` INT(11) NOT NULL,
  `professor_first_name` VARCHAR(45) NOT NULL,
  `professor_last_name` VARCHAR(45) NOT NULL,
  `course` VARCHAR(45) NOT NULL,
  `semester` VARCHAR(10) NOT NULL,
  `year` INT(11) NOT NULL,
  `comment` VARCHAR(500) NULL DEFAULT NULL,
  `rate_objectives` INT(11) NULL DEFAULT NULL,
  `rate_organized` INT(11) NULL DEFAULT NULL,
  `rate_challenging` INT(11) NULL DEFAULT NULL,
  `rate_outside_work` INT(11) NULL DEFAULT NULL,
  `rate_pace` INT(11) NULL DEFAULT NULL,
  `rate_assignments` INT(11) NULL DEFAULT NULL,
  `rate_grade_fairly` INT(11) NULL DEFAULT NULL,
  `rate_grade_time` INT(11) NULL DEFAULT NULL,
  `rate_accessibility` INT(11) NULL DEFAULT NULL,
  `rate_knowledge` INT(11) NULL DEFAULT NULL,
  `rate_career_development` INT(11) NULL DEFAULT NULL,
  `course_id` INT(11) NULL DEFAULT NULL,
  `professor_email` VARCHAR(45) NULL DEFAULT NULL,
  `comment_flagged` TINYINT(4) NOT NULL DEFAULT '0',
  `comment_removed` TINYINT(4) NOT NULL DEFAULT '0',
  `comment_approved` TINYINT(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`student_id`, `professor_first_name`, `professor_last_name`, `course`, `semester`, `year`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `rate`.`profile_pic`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rate`.`profile_pic` (
  `user_id` INT(11) NOT NULL,
  `pic_location` VARCHAR(500) NULL DEFAULT NULL,
  PRIMARY KEY (`user_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `rate`.`role`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rate`.`role` (
  `role_id` INT(11) NOT NULL,
  `role_name` VARCHAR(45) NULL DEFAULT NULL,
  PRIMARY KEY (`role_id`))
ENGINE = InnoDB
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `rate`.`users`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rate`.`users` (
  `user_id` INT(11) NOT NULL AUTO_INCREMENT,
  `first_name` VARCHAR(45) NOT NULL,
  `last_name` VARCHAR(45) NOT NULL,
  `email` VARCHAR(45) NOT NULL,
  `encryptedPassword` VARCHAR(255) NOT NULL,
  `role_id` INT(11) NOT NULL,
  `confirmed` TINYINT(4) NOT NULL DEFAULT '0',
  `active` TINYINT(4) NOT NULL DEFAULT '1',
  `major` VARCHAR(45) NULL DEFAULT NULL,
  `school_year` INT(11) NULL DEFAULT '1',
  PRIMARY KEY (`user_id`),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC),
  INDEX `fk_Users_Role1_idx` (`role_id` ASC),
  CONSTRAINT `fk_Users_Role1`
    FOREIGN KEY (`role_id`)
    REFERENCES `rate`.`role` (`role_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 105
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `rate`.`student_courses`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rate`.`student_courses` (
  `course_id` INT(11) NOT NULL AUTO_INCREMENT,
  `course_name` VARCHAR(30) NOT NULL,
  `semester` VARCHAR(10) NOT NULL,
  `year` INT(4) NOT NULL,
  `users_user_id` INT(11) NOT NULL,
  `professor_first_name` VARCHAR(45) NOT NULL,
  `professor_last_name` VARCHAR(45) NOT NULL,
  `professor_email` VARCHAR(45) NULL DEFAULT NULL,
  `course_reviewed` TINYINT(4) NOT NULL DEFAULT '0',
  `disable_edit` TINYINT(4) NOT NULL DEFAULT '0',
  `semester_past` TINYINT(4) NULL DEFAULT '0',
  PRIMARY KEY (`course_id`, `course_name`, `semester`, `year`, `users_user_id`, `professor_first_name`, `professor_last_name`),
  INDEX `fk_student_courses_users1_idx` (`users_user_id` ASC),
  CONSTRAINT `fk_student_courses_users1`
    FOREIGN KEY (`users_user_id`)
    REFERENCES `rate`.`users` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB
AUTO_INCREMENT = 34
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `rate`.`tutor_appointment`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rate`.`tutor_appointment` (
  `appointment_id` INT(11) NOT NULL AUTO_INCREMENT,
  `student_id` INT(11) NOT NULL,
  `tutor_id` INT(11) NOT NULL,
  `date` VARCHAR(11) NOT NULL,
  `time` VARCHAR(45) NULL DEFAULT 'null',
  `student_message` VARCHAR(200) NULL DEFAULT 'null',
  `tutor_message` VARCHAR(200) NULL DEFAULT 'null',
  `tutor_has_responded` TINYINT(4) NULL DEFAULT '0',
  `appointment_status` TINYINT(4) NOT NULL DEFAULT '0',
  `student_firstname` VARCHAR(45) NOT NULL DEFAULT 'null',
  `student_lastname` VARCHAR(45) NOT NULL DEFAULT 'null',
  `tutor_firstname` VARCHAR(45) NOT NULL DEFAULT 'null',
  `tutor_lastname` VARCHAR(45) NOT NULL DEFAULT 'null',
  `appointment_past` TINYINT(4) NULL DEFAULT '0',
  PRIMARY KEY (`appointment_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 11
DEFAULT CHARACTER SET = utf8;


-- -----------------------------------------------------
-- Table `rate`.`tutors`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `rate`.`tutors` (
  `tutor_relationship_id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id_student` INT(11) NOT NULL,
  `user_id_professor` INT(11) NOT NULL,
  `course_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`tutor_relationship_id`))
ENGINE = InnoDB
AUTO_INCREMENT = 57
DEFAULT CHARACTER SET = utf8;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
