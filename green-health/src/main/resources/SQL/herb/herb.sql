CREATE TABLE `herb` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `eng_name` varchar(45) NOT NULL,
  `latin_name` varchar(45) NOT NULL,
  `description` text NOT NULL,
  `grows_at` text NOT NULL,
  `when_to_pick` text NOT NULL,
  `properties` text NOT NULL,
  `warnings` text NOT NULL,
  `where_to_buy` text,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `srbName_UNIQUE` (`eng_name`),
  UNIQUE KEY `latin_name_UNIQUE` (`latin_name`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1