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
  
  UPDATE `mailTemplate` SET `contentTemplate`='Dear user,<br/><br/>Thank you for your interest in our application.<br/>Please activate your access by clicking on the link below:<br/><a href=\"{{root.url}}#activateAccount/{{activationString}}\" target=\"_blank\">{{root.url}}#activateAccount/{{activationString}}</a><br/><br/>You will then be able to login using your new password and the information below:<br/>Username: {{username}}<br/>URL: <a href=\"{{root.url}}\" target=\"_blank\">{{root.url}}</a><br/><br/>Best regards,<br/><br/>The LCI tool team' WHERE `name`='login.mail.activation.en_US';
UPDATE `mailTemplate` SET `contentTemplate`='Dear user,<br/><br/>We received a request to reset the password associated with this email address. If you are at the origin of this request, please click on the link below and follow the instructions.<br/><a href=\"{{root.url}}#resetPassword/{{validationString}}\" target=\"_blank\">{{root.url}}#resetPassword/{{validationString}}</a><br/><br/>Please note that after 15 minutes this link will expire and you will have to restart the procedure.<br/>If you did not request to reset your password, you can ignore this email. The security of your account is insured.<br/><br/>Best regards,<br/><br/>The LCI tool team' WHERE `name`='login.mail.resetpassword.en_US';

