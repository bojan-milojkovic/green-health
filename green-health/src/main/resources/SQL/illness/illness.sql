CREATE TABLE `greenhealth`.`illness` (
  `id` INT(11) NOT NULL,
  `latin_name` VARCHAR(75) NOT NULL,
  `srb_name` VARCHAR(75) NOT NULL,
  `description` TEXT NOT NULL,
  `symptoms` TEXT NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  UNIQUE INDEX `latin_name_UNIQUE` (`latin_name` ASC),
  UNIQUE INDEX `srb_name_UNIQUE` (`srb_name` ASC));