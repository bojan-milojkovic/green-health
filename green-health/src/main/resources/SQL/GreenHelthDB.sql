CREATE DATABASE  IF NOT EXISTS `greenhealth` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci */;
USE `greenhealth`;
-- MySQL dump 10.16  Distrib 10.1.37-MariaDB, for Win32 (AMD64)
--
-- Host: 127.0.0.1    Database: greenhealth
-- ------------------------------------------------------
-- Server version	10.1.37-MariaDB

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
  `eng_name` varchar(45) CHARACTER SET latin1 DEFAULT NULL,
  `latin_name` varchar(45) CHARACTER SET latin1 NOT NULL,
  `description` text CHARACTER SET latin1,
  `grows_at` text CHARACTER SET latin1,
  `when_to_pick` text CHARACTER SET latin1,
  `properties` text CHARACTER SET latin1,
  `warnings` text CHARACTER SET latin1,
  `where_to_buy` text CHARACTER SET latin1,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `latin_name_UNIQUE` (`latin_name`),
  UNIQUE KEY `srbName_UNIQUE` (`eng_name`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `herb`
--

LOCK TABLES `herb` WRITE;
/*!40000 ALTER TABLE `herb` DISABLE KEYS */;
INSERT INTO `herb` (`id`, `eng_name`, `latin_name`, `description`, `grows_at`, `when_to_pick`, `properties`, `warnings`, `where_to_buy`) VALUES (1,'mint','mentha piperita','Mints are aromatic, almost exclusively perennial herbs. They have wide-spreading underground and overground stolons and erect, square, branched stems. The leaves are arranged in opposite pairs, from oblong to lanceolate, often downy, and with a serrated margin. Leaf colors range from dark green and gray-green to purple, blue, and sometimes pale yellow.[6] The flowers are white to purple and produced in false whorls called verticillasters. The corolla is two-lipped with four subequal lobes, the upper lobe usually the largest. The fruit is a nutlet, containing one to four seeds.','world wide','Harvesting of mint leaves can be done at any time. Fresh leaves should be used immediately or stored up to a few days in plastic bags in a refrigerator. Optionally, leaves can be frozen in ice cube trays. Dried mint leaves should be stored in an airtight container placed in a cool, dark, dry area.','Mint was originally used as a medicinal herb to treat stomach ache and chest pains. It soothes headaches and relaxes the body. There are several uses in traditional medicine and preliminary research for possible use in treating irritable bowel syndrome. Menthol and mint essential oil are also used in aromatherapy which may have clinical use to alleviate post-surgery nausea. Mint oil is also used as an environmentally friendly insecticide for its ability to kill some common pests such as wasps, hornets, ants, and cockroaches. ','Completely safe, except in oil-form. It may cause allergy in some people',NULL),(3,NULL,'lavandula',NULL,NULL,NULL,NULL,NULL,NULL);
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
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `herb_for_illness`
--

LOCK TABLES `herb_for_illness` WRITE;
/*!40000 ALTER TABLE `herb_for_illness` DISABLE KEYS */;
INSERT INTO `herb_for_illness` (`id`, `herb_id`, `illness_id`, `rating_ones`, `rating_twos`, `rating_threes`, `rating_fours`, `rating_fives`) VALUES (1,1,2,0,0,2,0,0),(2,3,2,0,0,0,0,1);
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
) ENGINE=InnoDB AUTO_INCREMENT=5 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `herb_locale`
--

LOCK TABLES `herb_locale` WRITE;
/*!40000 ALTER TABLE `herb_locale` DISABLE KEYS */;
INSERT INTO `herb_locale` (`id`, `herb_id`, `locale`, `local_name`, `description`, `grows_at`, `when_to_pick`, `properties`, `warnings`, `where_to_buy`) VALUES (2,1,'sr_RS','nana','Stabljika je visoka oko 50 cm, razgranata i cetrovouglasta. Stabljika, lisne drske i nervi su mo dri ili su Ijubi?asto-crvenkasti, osobito u prolece kad nana nice iz zemlje. Listovi su dugacki 3-9 cm, jajasto-kopljasti, tanki, s gornje strane tamnozeleni, a s donje bledji, pri dnu se suzavaju u drske dugacke do 1 cm.','gaji se sirom sveta','brati po najlepsem vremenu','Nana se upotrebljava kao prijatan, blag i neškodljiv lek za umirivanje, protiv gasova, nadimanja i gr?eva, protiv teškog varenja, kao stomahik, nana ulazi u sastav ?ajeva za le?enje žu?i','Nana je potpuno bezbedna, osim kad je upitanju ulje. Moze izazvati alergiju kod nekih ljudi',NULL),(4,3,'sr_RS','lavanda','Lavandu karakterisu ljubicasti cvetovi i aromatican miris. Raste do izmedju 30 i 70 cm','Gaji se sirom sveta i na suvim i na vlaznim podrucjima','Brati kada su cvetovi naj bujniji','Najcesce se koristi za lecenje organa za varenje, srcanih bolesti, hipertenzije, bronhalne asme reumatizma, anksioznosti, nesanice, glavobolje, i vrtoglavice. Podstice mokrenje i menstrualno krvarenje. Suzbija infekciju koze i sluzokoze. Umiruje kasalj i otklanja zadah. Korisna je za lecenje akni, opekotina psorijaze i koznih oboljenja. Takodje rasteruje insekta, prvenstveno moljce i komarce.','Dijabeticari i trudnice dojilje treba da pripaze pri koriscenju lavande. Takodje moze izazvati alergijsku reakciju kod nekihljudi.',NULL);
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
  `latin_name` varchar(75) CHARACTER SET latin1 NOT NULL,
  `eng_name` varchar(75) CHARACTER SET latin1 DEFAULT NULL,
  `description` text CHARACTER SET latin1,
  `symptoms` text CHARACTER SET latin1,
  `cause` text CHARACTER SET latin1,
  `treatment` text CHARACTER SET latin1,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  UNIQUE KEY `latin_name_UNIQUE` (`latin_name`),
  UNIQUE KEY `srb_name_UNIQUE` (`eng_name`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `illness`
--

LOCK TABLES `illness` WRITE;
/*!40000 ALTER TABLE `illness` DISABLE KEYS */;
INSERT INTO `illness` (`id`, `latin_name`, `eng_name`, `description`, `symptoms`, `cause`, `treatment`) VALUES (1,'Astma','Asthma','Asthma is a common long-term inflammatory disease of the airways of the lungs. It is characterized by variable and recurring symptoms, reversible airflow obstruction, and bronchospasm.','Symptoms include episodes of wheezing, coughing, chest tightness, and shortness of breath. These episodes may occur a few times a day or a few times per week. Depending on the person, they may become worse at night or with exercise.','Opstrukcija sinusa, Stres, Promaja, Depresija, Mamurluk, Dehidratacija, Istegnuce','Opustanje, Lekovi za bolove'),(2,'Cephalea','Head ache','Headache is a persistent pain anywhere in the region of the head or neck.','It occurs in migraines (sharp, or throbbing pains), tension-type headaches, and cluster headaches. Frequent headaches can affect relationships and employment. There is also an increased risk of depression in those with severe headaches.','Sinus obstruction, Stress, Draft, Depression, Hangover','Relaxation, Painkillers');
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
  `cause` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  `treatment` text CHARACTER SET utf8mb4 COLLATE utf8mb4_bin NOT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `id_UNIQUE` (`id`),
  KEY `fk_illness_idx` (`illness_id`),
  KEY `index1` (`illness_id`,`locale`),
  KEY `index2` (`locale`,`local_name`),
  CONSTRAINT `fk_illness_parent` FOREIGN KEY (`illness_id`) REFERENCES `illness` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `illness_locale`
--

LOCK TABLES `illness_locale` WRITE;
/*!40000 ALTER TABLE `illness_locale` DISABLE KEYS */;
INSERT INTO `illness_locale` (`id`, `illness_id`, `locale`, `local_name`, `description`, `symptoms`, `cause`, `treatment`) VALUES (1,2,'sr_RS','Glavobolja','Glavobolja je bilo koji uporan bol u predelu vrata i glave','Pojavljuje se kod migrene (oštri ili pulsirajuc?i bolovi), glavobolja tenzionog tipa i klaster glavobolje. ?este glavobolje mogu uticati na odnose i zaposlenje. Tako?e postoji povec?an rizik od depresije kod onih sa teškim glavoboljama.','Opstrukcija sinusa, Stres, Promaja, Depresija, Mamurluk, Dehidratacija, Istegnuce','Opustanje, Lekovi za bolove'),(2,1,'sr_RS','Asma','Asma je dugorocna bolest pluca i disajnih puteva okarakterisana zacepljenjem disajnih kanala usled alergijske reakcije.','Skripanje u plucima, nedostatak vazduha, nemogucnost da se udahne','Ekstremna alergijska reakcija, stress','Pri napadu asme, odmah uzmite prepisanu terapiju. Redovno uzimajte prepisanu terapiju');
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
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `ratings`
--

LOCK TABLES `ratings` WRITE;
/*!40000 ALTER TABLE `ratings` DISABLE KEYS */;
INSERT INTO `ratings` (`id`, `user_id`, `link_id`) VALUES (2,5,1),(3,5,2);
/*!40000 ALTER TABLE `ratings` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `user`
--

DROP TABLE IF EXISTS `user`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `user` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(50) CHARACTER SET latin1 NOT NULL,
  `last_name` varchar(50) CHARACTER SET latin1 NOT NULL,
  `email` varchar(100) CHARACTER SET latin1 NOT NULL,
  `reg_date` datetime NOT NULL,
  `city` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `country` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `postal_code` varchar(10) COLLATE utf8mb4_unicode_ci NOT NULL,
  `address_1` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `address_2` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `phone_1` varchar(45) COLLATE utf8mb4_unicode_ci NOT NULL,
  `phone_2` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `id_UNIQUE` (`user_id`),
  UNIQUE KEY `email_UNIQUE` (`email`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user`
--

LOCK TABLES `user` WRITE;
/*!40000 ALTER TABLE `user` DISABLE KEYS */;
INSERT INTO `user` (`user_id`, `first_name`, `last_name`, `email`, `reg_date`, `city`, `country`, `postal_code`, `address_1`, `address_2`, `phone_1`, `phone_2`) VALUES (5,'Bojan','Milojkovic','lord_lazaruss@yahoo.com','2018-07-23 19:49:20','Beograd','Srbija','11000','Ustanicka 10','3ci ulaz, drugi sprat stan broj 34','+381605178733','+381112443049'),(6,'Davor','Milojkovic','milojkovicdavor@yahoo.com','2018-07-23 20:12:50','','','11000','',NULL,'',NULL),(7,'Dusan','Kanlic','kanlic@eunet.rs','2019-07-20 11:21:37','Novi Beograd','Srbija','11000','asdfsd',NULL,'+381605643812',NULL);
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
  `username` varchar(50) CHARACTER SET latin1 NOT NULL,
  `password` varchar(150) CHARACTER SET latin1 NOT NULL,
  `hash_key` varchar(45) COLLATE utf8mb4_unicode_ci DEFAULT NULL,
  `active` tinyint(1) NOT NULL DEFAULT '1',
  `not_locked` tinyint(1) NOT NULL DEFAULT '1',
  `last_login` datetime NOT NULL,
  `last_password_change` datetime NOT NULL,
  `last_update` datetime NOT NULL,
  `user_has_roles` varchar(52) COLLATE utf8mb4_unicode_ci NOT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `user_id_UNIQUE` (`user_id`),
  UNIQUE KEY `username_UNIQUE` (`username`),
  UNIQUE KEY `hash_key` (`hash_key`),
  CONSTRAINT `fk_us_u` FOREIGN KEY (`user_id`) REFERENCES `user` (`user_id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_unicode_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `user_security`
--

LOCK TABLES `user_security` WRITE;
/*!40000 ALTER TABLE `user_security` DISABLE KEYS */;
INSERT INTO `user_security` (`user_id`, `username`, `password`, `hash_key`, `active`, `not_locked`, `last_login`, `last_password_change`, `last_update`, `user_has_roles`) VALUES (5,'Lazaruss','$2a$10$nLmLTuMr1gRHsaaNfH8BFehEp5KpC3pyI91jLyAzKRz9MiUwTNgJ2',NULL,1,1,'2019-07-20 13:23:25','2018-07-23 19:49:20','2018-07-23 19:49:20','ROLE_USER#ROLE_HERBALIST#ROLE_ADMIN#ROLE_SUPERADMIN'),(6,'Kale01','$2a$10$ZHtFYXIvxYYGjUqsC45TQOYQpxm3JWRiMJF8uJEmgsXrN1RJpZS9q',NULL,1,1,'2018-08-18 17:03:01','2018-07-23 20:12:50','2018-07-23 20:12:50','ROLE_USER'),(7,'KanlicD','$2a$10$tpfIZUSEHDF8yShgQGXljO.HrD7YwUMJEEgnq/Z5pmasH85sRPL22',NULL,1,1,'2019-07-20 11:21:37','2019-07-20 11:21:37','2019-07-20 11:21:39','ROLE_USER');
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

-- Dump completed on 2019-07-20 13:26:01