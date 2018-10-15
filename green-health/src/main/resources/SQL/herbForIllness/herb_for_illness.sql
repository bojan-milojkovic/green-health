CREATE TABLE `herb_for_illness` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `herb_id` int(11) NOT NULL,
  `illness_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `idnew_table_UNIQUE` (`id`),
  KEY `fk_illness_id_idx` (`illness_id`),
  KEY `fk_herb_id_idx` (`herb_id`),
  CONSTRAINT `fk_herb_id` FOREIGN KEY (`herb_id`) REFERENCES `herb` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_illness_id` FOREIGN KEY (`illness_id`) REFERENCES `illness` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);