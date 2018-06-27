CREATE DATABASE  IF NOT EXISTS `greenhealth` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `greenhealth`;

--
-- Table structure for table `user`
--
DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `username` varchar(50) NOT NULL,
  `password` varchar(150) NOT NULL,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `reg_date` datetime NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `id_UNIQUE` (`user_id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
--
-- Dumping data for table `user`
--
LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (3,'Lazaruss','$2a$10$eOd3C0cQk1S1IBGu7g.sIuyuDA9vsvDk6zvzni26o0dnkHasedMz6','Bojan','Milojkovic','lord_lazaruss@yahoo.com','2018-06-01 00:00:00'),(4,'Kale01','$2a$10$H6m2RiAcL22bO8rm73DFT.C4AZakGrbtnUkVykWjOOtp1CYfLprJS','Davor','Milojkovic','milojkovic.davor@yahoo.com','2018-06-01 00:00:00');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_security`
--
DROP TABLE IF EXISTS `user_security`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_security` (
  `user_id` int(11) NOT NULL,
  `username` varchar(50) NOT NULL,
  `password` varchar(150) NOT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `not_locked` tinyint(1) NOT NULL DEFAULT '1',
  `last_login` datetime NOT NULL,
  `last_password_change` datetime NOT NULL,
  `last_update` datetime NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  CONSTRAINT `fk_us_u` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
--
-- Dumping data for table `user_security`
--
LOCK TABLES `user_security` WRITE;
/*!40000 ALTER TABLE `user_security` DISABLE KEYS */;
INSERT INTO `user_security` VALUES (3,'Lazaruss','$2a$10$eOd3C0cQk1S1IBGu7g.sIuyuDA9vsvDk6zvzni26o0dnkHasedMz6',1,1,'2018-06-01 22:24:43','2018-06-01 19:18:58','2018-06-01 19:18:58'),(4,'Kale01','$2a$10$H6m2RiAcL22bO8rm73DFT.C4AZakGrbtnUkVykWjOOtp1CYfLprJS',1,1,'2018-06-01 22:23:18','2018-06-01 22:22:05','2018-06-01 22:22:05');
/*!40000 ALTER TABLE `user_security` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--
DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `roles` (
  `role_id` int(4) NOT NULL,
  `role_name` varchar(45) NOT NULL,
  PRIMARY KEY (`role_id`),
  UNIQUE KEY `role_id_UNIQUE` (`role_id`),
  UNIQUE KEY `role_name_UNIQUE` (`role_name`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
--
-- Dumping data for table `roles`
--
LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles` DISABLE KEYS */;
INSERT INTO `roles` VALUES (3,'ROLE_ADMIN'),(2,'ROLE_HERBALIST'),(4,'ROLE_SUPERADMIN'),(1,'ROLE_USER');
/*!40000 ALTER TABLE `roles` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user_has_roles`
--
DROP TABLE IF EXISTS `user_has_roles`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user_has_roles` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `role_id` int(4) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_us_r_idx` (`user_id`),
  KEY `fk_r_idx` (`role_id`),
  CONSTRAINT `fk_r` FOREIGN KEY (`role_id`) REFERENCES `roles` (`role_id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_us` FOREIGN KEY (`user_id`) REFERENCES `user_security` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;
--
-- Dumping data for table `user_has_roles`
--
LOCK TABLES `user_has_roles` WRITE;
/*!40000 ALTER TABLE `user_has_roles` DISABLE KEYS */;
INSERT INTO `user_has_roles` VALUES (1,3,1),(2,3,2),(3,3,3),(4,3,4),(5,4,1);
/*!40000 ALTER TABLE `user_has_roles` ENABLE KEYS */;
UNLOCK TABLES;
