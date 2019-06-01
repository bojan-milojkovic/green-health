CREATE TABLE `herb` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `eng_name` varchar(45) NOT NULL,
  `latin_name` varchar(45) NOT NULL,
  `description` text NOT NULL,
  `grows_at` text NOT NULL,
  `when_to_pick` text NOT NULL,
  `properties` text NOT NULL,
  `warnings` text NOT NULL,
  `where_to_buy` text NULL DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `srbName_UNIQUE` (`eng_name`),
  UNIQUE KEY `latin_name_UNIQUE` (`latin_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;


ALTER TABLE `greenhealth`.`herb` 
CHANGE COLUMN `eng_name` `eng_name` VARCHAR(45) NULL ,
CHANGE COLUMN `description` `description` TEXT NULL DEFAULT NULL ,
CHANGE COLUMN `grows_at` `grows_at` TEXT NULL DEFAULT NULL ,
CHANGE COLUMN `when_to_pick` `when_to_pick` TEXT NULL DEFAULT NULL ,
CHANGE COLUMN `properties` `properties` TEXT NULL DEFAULT NULL ,
CHANGE COLUMN `warnings` `warnings` TEXT NULL DEFAULT NULL;