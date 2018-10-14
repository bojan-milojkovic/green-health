CREATE TABLE `herb_for_illness` (
  `herb_id` int(11) NOT NULL,
  `illness_id` int(11) NOT NULL,
  `ratings` int(11) NOT NULL DEFAULT '3',
  PRIMARY KEY (`herb_id`,`illness_id`),
  KEY `fk_illness_id_idx` (`illness_id`),
  CONSTRAINT `fk_herb_id` FOREIGN KEY (`herb_id`) REFERENCES `herb` (`id`) ON DELETE CASCADE ON UPDATE CASCADE,
  CONSTRAINT `fk_illness_id` FOREIGN KEY (`illness_id`) REFERENCES `illness` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
);