CREATE DATABASE  IF NOT EXISTS `greenhealth` /*!40100 DEFAULT CHARACTER SET latin1 */;
USE `greenhealth`;
-- MySQL dump 10.13  Distrib 5.7.17, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: greenhealth
-- ------------------------------------------------------
-- Server version	5.5.5-10.1.16-MariaDB

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS=@@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION=@@COLLATION_CONNECTION */;
/*!40101 SET NAMES utf8 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS=@@UNIQUE_CHECKS, UNIQUE_CHECKS=0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;

--
-- Table structure for table `herb`
--

DROP TABLE IF EXISTS `herb`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
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
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `herb`
--

LOCK TABLES `herb` WRITE;
/*!40000 ALTER TABLE `herb` DISABLE KEYS */;
INSERT INTO `herb` VALUES (1,'mint','mentha piperita','Mints are aromatic, almost exclusively perennial herbs. They have wide-spreading underground and overground stolons and erect, square, branched stems. The leaves are arranged in opposite pairs, from oblong to lanceolate, often downy, and with a serrated margin. Leaf colors range from dark green and gray-green to purple, blue, and sometimes pale yellow.[6] The flowers are white to purple and produced in false whorls called verticillasters. The corolla is two-lipped with four subequal lobes, the upper lobe usually the largest. The fruit is a nutlet, containing one to four seeds.','world wide','Harvesting of mint leaves can be done at any time. Fresh leaves should be used immediately or stored up to a few days in plastic bags in a refrigerator. Optionally, leaves can be frozen in ice cube trays. Dried mint leaves should be stored in an airtight container placed in a cool, dark, dry area.','Mint was originally used as a medicinal herb to treat stomach ache and chest pains. It soothes headaches and relaxes the body. There are several uses in traditional medicine and preliminary research for possible use in treating irritable bowel syndrome. Menthol and mint essential oil are also used in aromatherapy which may have clinical use to alleviate post-surgery nausea. Mint oil is also used as an environmentally friendly insecticide for its ability to kill some common pests such as wasps, hornets, ants, and cockroaches. ','Completely safe, except in oil-form. It may cause allergy in some people',NULL);
/*!40000 ALTER TABLE `herb` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `herb_for_illness`
--

DROP TABLE IF EXISTS `herb_for_illness`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `herb_for_illness` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `herb_id` int(11) NOT NULL,
  `illness_id` int(11) NOT NULL,
  `rating_ones` tinyint(1) NOT NULL DEFAULT '0',
  `rating_twos` tinyint(1) NOT NULL DEFAULT '0',
  `rating_threes` tinyint(1) NOT NULL DEFAULT '0',
  `rating_fours` tinyint(1) NOT NULL DEFAULT '0',
  `rating_fives` tinyint(1) NOT NULL DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_herb_idx` (`herb_id`),
  KEY `fk_illness_idx` (`illness_id`),
  CONSTRAINT `fk_herb` FOREIGN KEY (`herb_id`) REFERENCES `herb` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION,
  CONSTRAINT `fk_illness` FOREIGN KEY (`illness_id`) REFERENCES `illness` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `herb_for_illness`
--

LOCK TABLES `herb_for_illness` WRITE;
/*!40000 ALTER TABLE `herb_for_illness` DISABLE KEYS */;
INSERT INTO `herb_for_illness` VALUES (1,1,2,0,0,2,0,0);
/*!40000 ALTER TABLE `herb_for_illness` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `herb_locale`
--

DROP TABLE IF EXISTS `herb_locale`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `herb_locale` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `herb_id` int(11) NOT NULL,
  `locale` varchar(20) CHARACTER SET latin1 NOT NULL,
  `local_name` varchar(45) CHARACTER SET latin1 NOT NULL,
  `description` text CHARACTER SET latin1 NOT NULL,
  `grows_at` text CHARACTER SET latin1 NOT NULL,
  `when_to_pick` text CHARACTER SET latin1 NOT NULL,
  `properties` text CHARACTER SET latin1 NOT NULL,
  `warnings` text CHARACTER SET latin1 NOT NULL,
  `where_to_buy` text CHARACTER SET latin1,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_herb_id_idx` (`herb_id`),
  KEY `idx1` (`locale`,`local_name`),
  KEY `idx2` (`herb_id`,`locale`),
  CONSTRAINT `fk_herb_id` FOREIGN KEY (`herb_id`) REFERENCES `herb` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf16;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `herb_locale`
--

LOCK TABLES `herb_locale` WRITE;
/*!40000 ALTER TABLE `herb_locale` DISABLE KEYS */;
INSERT INTO `herb_locale` VALUES (2,1,'sr_RS','nana','Stabljika je visoka oko 50 cm, razgranata i cetrovouglasta. Stabljika, lisne drske i nervi su mo dri ili su Ijubi?asto-crvenkasti, osobito u prolece kad nana nice iz zemlje. Listovi su dugacki 3-9 cm, jajasto-kopljasti, tanki, s gornje strane tamnozeleni, a s donje bledji, pri dnu se suzavaju u drske dugacke do 1 cm.','gaji se sirom sveta','brati po najlepsem vremenu','Nana se upotrebljava kao prijatan, blag i neškodljiv lek za umirivanje, protiv gasova, nadimanja i gr?eva, protiv teškog varenja, kao stomahik, nana ulazi u sastav ?ajeva za le?enje žu?i','Nana je potpuno ne skodljiva',NULL);
/*!40000 ALTER TABLE `herb_locale` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `illness`
--

DROP TABLE IF EXISTS `illness`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `illness` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `latin_name` varchar(75) NOT NULL,
  `eng_name` varchar(75) NOT NULL,
  `description` text NOT NULL,
  `symptoms` text NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `latin_name_UNIQUE` (`latin_name`),
  UNIQUE KEY `srb_name_UNIQUE` (`eng_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `illness`
--

LOCK TABLES `illness` WRITE;
/*!40000 ALTER TABLE `illness` DISABLE KEYS */;
INSERT INTO `illness` VALUES (1,'Astma','Asthma','Asthma is a common long-term inflammatory disease of the airways of the lungs. It is characterized by variable and recurring symptoms, reversible airflow obstruction, and bronchospasm.','Symptoms include episodes of wheezing, coughing, chest tightness, and shortness of breath. These episodes may occur a few times a day or a few times per week. Depending on the person, they may become worse at night or with exercise.'),(2,'Cephalea','Head ache','Headache is a persistent pain anywhere in the region of the head or neck.','It occurs in migraines (sharp, or throbbing pains), tension-type headaches, and cluster headaches. Frequent headaches can affect relationships and employment. There is also an increased risk of depression in those with severe headaches.');
/*!40000 ALTER TABLE `illness` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `illness_locale`
--

DROP TABLE IF EXISTS `illness_locale`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `illness_locale` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `illness_id` int(11) NOT NULL,
  `locale` varchar(20) CHARACTER SET latin1 NOT NULL,
  `local_name` varchar(45) CHARACTER SET latin1 NOT NULL,
  `description` text CHARACTER SET latin1 NOT NULL,
  `symptoms` text CHARACTER SET latin1 NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_illness_idx` (`illness_id`),
  KEY `index1` (`illness_id`,`locale`),
  KEY `index2` (`locale`,`local_name`),
  CONSTRAINT `fk_illness_parent` FOREIGN KEY (`illness_id`) REFERENCES `illness` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `illness_locale`
--

LOCK TABLES `illness_locale` WRITE;
/*!40000 ALTER TABLE `illness_locale` DISABLE KEYS */;
INSERT INTO `illness_locale` VALUES (1,2,'sr_RS','Glavobolja','Glavobolja je bilo koji uporan bol u predelu vrata i glave','Pojavljuje se kod migrene (oštri ili pulsirajuc?i bolovi), glavobolja tenzionog tipa i klaster glavobolje. ?este glavobolje mogu uticati na odnose i zaposlenje. Tako?e postoji povec?an rizik od depresije kod onih sa teškim glavoboljama.');
/*!40000 ALTER TABLE `illness_locale` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `ratings`
--

DROP TABLE IF EXISTS `ratings`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ratings` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id` int(11) NOT NULL,
  `link_id` int(11) NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_user_idx` (`user_id`),
  KEY `fk_link_idx` (`link_id`),
  CONSTRAINT `fk_link` FOREIGN KEY (`link_id`) REFERENCES `herb_for_illness` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `fk_user` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ratings`
--

LOCK TABLES `ratings` WRITE;
/*!40000 ALTER TABLE `ratings` DISABLE KEYS */;
INSERT INTO `ratings` VALUES (2,5,1);
/*!40000 ALTER TABLE `ratings` ENABLE KEYS */;
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
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) NOT NULL,
  `last_name` varchar(50) NOT NULL,
  `email` varchar(100) NOT NULL,
  `reg_date` datetime NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `id_UNIQUE` (`user_id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=7 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` VALUES (5,'Bojan','Milojkovic','lord_lazaruss@yahoo.com','2018-07-23 19:49:20'),(6,'Davor','Milojkovic','milojkovicdavor@yahoo.com','2018-07-23 20:12:50');
/*!40000 ALTER TABLE `user` ENABLE KEYS */;
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
) ENGINE=InnoDB AUTO_INCREMENT=11 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_has_roles`
--

LOCK TABLES `user_has_roles` WRITE;
/*!40000 ALTER TABLE `user_has_roles` DISABLE KEYS */;
INSERT INTO `user_has_roles` VALUES (6,5,1),(7,5,2),(8,5,3),(9,5,4),(10,6,1);
/*!40000 ALTER TABLE `user_has_roles` ENABLE KEYS */;
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
INSERT INTO `user_security` VALUES (5,'Lazaruss','$2a$10$Dp64n95ROylB21j8kbxpAuJdFvF.bgddIv0D3SGIyRw4YCnsLvJZi',1,1,'2019-01-20 13:37:50','2018-07-23 19:49:20','2018-07-23 19:49:20'),(6,'Kale01','$2a$10$ZHtFYXIvxYYGjUqsC45TQOYQpxm3JWRiMJF8uJEmgsXrN1RJpZS9q',1,1,'2018-08-18 17:03:01','2018-07-23 20:12:50','2018-07-23 20:12:50');
/*!40000 ALTER TABLE `user_security` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2019-01-20 13:53:13
