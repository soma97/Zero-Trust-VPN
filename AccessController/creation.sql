-- MySQL Workbench Forward Engineering

SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0;
SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0;
SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='ONLY_FULL_GROUP_BY,STRICT_TRANS_TABLES,NO_ZERO_IN_DATE,NO_ZERO_DATE,ERROR_FOR_DIVISION_BY_ZERO,NO_ENGINE_SUBSTITUTION';

-- -----------------------------------------------------
-- Schema access_db
-- -----------------------------------------------------

-- -----------------------------------------------------
-- Schema access_db
-- -----------------------------------------------------
CREATE SCHEMA IF NOT EXISTS `access_db` DEFAULT CHARACTER SET utf8 ;
USE `access_db` ;

-- -----------------------------------------------------
-- Table `access_db`.`user`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `access_db`.`user` (
  `email` VARCHAR(100) NOT NULL,
  `password` VARCHAR(100) NULL,
  `ip` VARCHAR(16) NULL,
  PRIMARY KEY (`email`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `access_db`.`permission`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `access_db`.`permission` (
  `name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`name`))
ENGINE = InnoDB;


-- -----------------------------------------------------
-- Table `access_db`.`user_has_permission`
-- -----------------------------------------------------
CREATE TABLE IF NOT EXISTS `access_db`.`user_has_permission` (
  `email` VARCHAR(45) NOT NULL,
  `permission_name` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`email`, `permission_name`),
  INDEX `fk_user_has_permission_permission1_idx` (`permission_name` ASC) VISIBLE,
  INDEX `fk_user_has_permission_user_idx` (`email` ASC) VISIBLE,
  CONSTRAINT `fk_user_has_permission_user`
    FOREIGN KEY (`email`)
    REFERENCES `access_db`.`user` (`email`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_user_has_permission_permission1`
    FOREIGN KEY (`permission_name`)
    REFERENCES `access_db`.`permission` (`name`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION)
ENGINE = InnoDB;


SET SQL_MODE=@OLD_SQL_MODE;
SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS;
SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS;
