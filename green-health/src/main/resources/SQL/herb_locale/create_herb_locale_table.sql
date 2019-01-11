CREATE TABLE `greenhealth`.`herb_locale` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `herb_id` INT(11) NOT NULL,
  `locale` VARCHAR(5) NOT NULL,
  `local_name` VARCHAR(45) NOT NULL,
  `description` TEXT NOT NULL,
  `grows_at` TEXT NOT NULL,
  `when_to_pick` TEXT NOT NULL,
  `properties` TEXT NOT NULL,
  `warnings` TEXT NOT NULL,
  `where_to_buy` TEXT NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_herb_id_idx` (`herb_id` ASC),
  CONSTRAINT `fk_herb_id`
    FOREIGN KEY (`herb_id`)
    REFERENCES `greenhealth`.`herb` (`id`)
    ON DELETE CASCADE
    ON UPDATE CASCADE);
    
create index idx1 on greenhealth.herb_locale (locale, local_name);

create index idx2 on greenhealth.herb_locale (herb_id, locale);