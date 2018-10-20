CREATE TABLE `greenhealth`.`ratings` (
  `id` INT(11) NOT NULL AUTO_INCREMENT,
  `user_id` INT(11) NOT NULL,
  `link_id` INT(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE INDEX `id_UNIQUE` (`id` ASC),
  INDEX `fk_user_idx` (`user_id` ASC),
  INDEX `fk_link_idx` (`link_id` ASC),
  CONSTRAINT `fk_user`
    FOREIGN KEY (`user_id`)
    REFERENCES `greenhealth`.`user` (`user_id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION,
  CONSTRAINT `fk_link`
    FOREIGN KEY (`link_id`)
    REFERENCES `greenhealth`.`herb_for_illness` (`id`)
    ON DELETE NO ACTION
    ON UPDATE NO ACTION);