DROP TABLE IF EXISTS `file_generation`;

CREATE TABLE `file_generation` (
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
  `warningsErrors` MEDIUMTEXT NULL,
  PRIMARY KEY (`id`),
  INDEX `fk_file_generation_1_idx` (`userId` ASC),
  CONSTRAINT `fk_file_generation_1`
  FOREIGN KEY (`userId`)
  REFERENCES `user_std` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8;
