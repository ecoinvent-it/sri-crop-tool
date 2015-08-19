DROP TABLE IF EXISTS `user_pwd`;
DROP TABLE IF EXISTS `user_std`;

CREATE TABLE user_std (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(255) NOT NULL,
  `email` VARCHAR(255) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `username_unique` (`username` ASC),
  UNIQUE INDEX `email_unique` (`email` ASC)
) ENGINE = InnoDB DEFAULT CHARSET=utf8;

CREATE TABLE `user_pwd` (
  `userId` INT(11) NOT NULL,
  `base64salt` VARCHAR(255) NOT NULL,
  `password` VARCHAR(255) NOT NULL,
  `forceChangePassword` TINYINT(1) NOT NULL,
  `failedAttemps` INT(11) NOT NULL DEFAULT 0,
  `lockedSince` DATETIME NULL DEFAULT NULL,
  `registrationCode` VARCHAR(255) DEFAULT NULL,
  `validationCode` VARCHAR(255) DEFAULT NULL,
  `codeGeneration` DATETIME NULL DEFAULT NULL,
  PRIMARY KEY (`userId`),
  INDEX `user_pwd_userId_user_id` (`userId` ASC),
  CONSTRAINT `user_pwd_userId_user_id`
    FOREIGN KEY (`userId`)
    REFERENCES `user_std` (`id`)
) ENGINE = InnoDB DEFAULT CHARSET=utf8;

