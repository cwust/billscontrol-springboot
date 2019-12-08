CREATE TABLE IF NOT EXISTS `user` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `email` VARCHAR(100) NOT NULL,
  `name` VARCHAR(100) NOT NULL,
  `role` VARCHAR(45) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `email_UNIQUE` (`email` ASC));


CREATE TABLE IF NOT EXISTS `category` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT UNSIGNED NULL,
  `name` VARCHAR(45) NOT NULL,
  `color` VARCHAR(7) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_Category_User_idx` (`user_id` ASC),
  CONSTRAINT `fk_Category_User`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);


CREATE TABLE IF NOT EXISTS `bill_definition` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `user_id` BIGINT UNSIGNED NOT NULL,
  `name` VARCHAR(200) NOT NULL,
  `default_value` DECIMAL(8,2) UNSIGNED NULL,
  `start_date` DATE NOT NULL,
  `end_date` DATE NULL,
  `recurrence_type` VARCHAR(10) NULL,
  `category_id` BIGINT UNSIGNED NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_Bill_Definition_Category1_idx` (`category_id` ASC),
  INDEX `fk_Bill_Definition_User1_idx` (`user_id` ASC),
  CONSTRAINT `fk_Bill_Definition_Category1`
    FOREIGN KEY (`category_id`)
    REFERENCES `category` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_Bill_Definition_User1`
    FOREIGN KEY (`user_id`)
    REFERENCES `user` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);

CREATE TABLE IF NOT EXISTS `bill_instance` (
  `id` BIGINT UNSIGNED NOT NULL AUTO_INCREMENT,
  `bill_definition_id` BIGINT UNSIGNED NOT NULL,
  `recurrence_period` INT UNSIGNED NOT NULL,
  `additionalInfo` VARCHAR(400) NULL,
  `due_date` DATE NOT NULL,
  `paid_date` DATE NULL,
  `value` DECIMAL(8,2) NULL,
  `paid_value` DECIMAL(8,2) NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_bill_instance_bill_definition1_idx` (`bill_definition_id` ASC),
  CONSTRAINT `fk_bill_instance_bill_definition1`
    FOREIGN KEY (`bill_definition_id`)
    REFERENCES `bill_definition` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);
