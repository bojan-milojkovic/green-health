CREATE TABLE `greenhealth`.`herb_for_illness` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `herb_id` INT(11) NOT NULL,
  `illness_id` INT(11) NOT NULL,
  `rating_ones` TINYINT(1) NOT NULL DEFAULT 0,
  `rating_twos` TINYINT(1) NOT NULL DEFAULT 0,
  `rating_threes` TINYINT(1) NOT NULL DEFAULT 0,
  `rating_fours` TINYINT(1) NOT NULL DEFAULT 0,
  `rating_fives` TINYINT(1) NOT NULL DEFAULT 0,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_herb_idx` (`herb_id` ASC),
  INDEX `fk_illness_idx` (`illness_id` ASC),
  CONSTRAINT `fk_herb`
    FOREIGN KEY (`herb_id`)
    REFERENCES `greenhealth`.`herb` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_illness`
    FOREIGN KEY (`illness_id`)
    REFERENCES `greenhealth`.`illness` (`id`)
    ON DELETE CASCADE
    ON UPDATE NO ACTION);
