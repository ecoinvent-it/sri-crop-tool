DROP TABLE IF EXISTS `generation`;
DROP TABLE IF EXISTS `license`;
DROP TABLE IF EXISTS `registrationRequest`;

CREATE TABLE `generation` (
  `id` CHAR(12) NOT NULL,
  `userId` CHAR(12) NOT NULL,
  `licenseId` CHAR(12) NOT NULL,
  `canUseForTesting` TINYINT(1) NOT NULL,
  `lastTryNumber` INT NOT NULL,
  `lastTryDate` DATETIME NOT NULL,
  `appVersion` VARCHAR(45) NOT NULL,
  `crop` VARCHAR(45) NOT NULL,
  `country` VARCHAR(45) NOT NULL,
  `filename` VARCHAR(255) NOT NULL,
  `warnings` MEDIUMTEXT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_generation_1_idx` (`userId` ASC),
  CONSTRAINT `fk_generation_1`
  FOREIGN KEY (`userId`)
  REFERENCES `user_std` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `license` (
  `id` CHAR(12) NOT NULL,
  `userId` CHAR(12) DEFAULT NULL,
  `licenseType` VARCHAR(32) NOT NULL,
  `startDate` DATE NOT NULL,
  `rentalItem` VARCHAR(32) NOT NULL,
  `price` VARCHAR(32) NOT NULL,
  `comment` VARCHAR(255) NOT NULL,
  `additionalGenerations` INT(11) NOT NULL,
  `isDepleted` TINYINT(1) NOT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_license_1_idx` (`userId` ASC),
  CONSTRAINT `fk_license_1`
  FOREIGN KEY (`userId`)
  REFERENCES `user_std` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `registrationRequest` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `name` VARCHAR(255) NOT NULL,
  `username` VARCHAR(255) NOT NULL,
  `company` VARCHAR(255) NOT NULL,
  `address` VARCHAR(512) NOT NULL,
  `mail` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`)
  ) ENGINE = InnoDB DEFAULT CHARSET=utf8;
