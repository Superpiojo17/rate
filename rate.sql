-- MySQL dump 10.13  Distrib 5.7.12, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: rate
-- ------------------------------------------------------
-- Server version	5.7.15-log

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
-- Table structure for table `account_recovery`
--

DROP TABLE IF EXISTS `account_recovery`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `account_recovery` (
  `email` varchar(45) NOT NULL,
  `temp_password` varchar(100) DEFAULT NULL,
  `datetime` datetime NOT NULL,
  PRIMARY KEY (`datetime`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `account_recovery`
--

LOCK TABLES `account_recovery` WRITE;
/*!40000 ALTER TABLE `account_recovery` DISABLE KEYS */;
INSERT INTO `account_recovery` VALUES ('b2125699@ben.edu','$2a$10$9QqJ7So0PgA/CxT4OcZZDOCOCJC1BX1hUN9cTyGEQufEKGXIdfSii','2017-04-20 12:02:27'),('b2125695@ben.edu','$2a$10$KtRQyTnNfp6x4BktXlyVDevMLAi7CKk0MqbIPtKbR5MJboB7LDdcS','2017-04-23 14:37:50'),('bob@joe.com','$2a$10$aOvPDaUqEjV0wJ7j5GvZxuBwdajpyQIlbBuTcYPfCAFBArXtZNcY6','2017-04-23 14:38:35');
/*!40000 ALTER TABLE `account_recovery` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `announcements`
--

DROP TABLE IF EXISTS `announcements`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `announcements` (
  `announcement_id` int(11) NOT NULL AUTO_INCREMENT,
  `announcement_date` varchar(45) DEFAULT NULL,
  `announcement_content` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`announcement_id`)
) ENGINE=InnoDB AUTO_INCREMENT=2 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `announcements`
--

LOCK TABLES `announcements` WRITE;
/*!40000 ALTER TABLE `announcements` DISABLE KEYS */;
INSERT INTO `announcements` VALUES (1,'03/16/17','<p><strong>announcement</strong>, yo.</p>');
/*!40000 ALTER TABLE `announcements` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `courses`
--

DROP TABLE IF EXISTS `courses`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `courses` (
  `course_id` int(11) NOT NULL AUTO_INCREMENT,
  `course_subject` varchar(45) DEFAULT NULL,
  `course_number` int(11) DEFAULT NULL,
  `course_name` varchar(45) DEFAULT NULL,
  `course_professor_id` int(11) DEFAULT '0',
  `course_semester` varchar(20) NOT NULL,
  `course_year` int(4) DEFAULT NULL,
  PRIMARY KEY (`course_id`)
) ENGINE=InnoDB AUTO_INCREMENT=19 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `courses`
--

LOCK TABLES `courses` WRITE;
/*!40000 ALTER TABLE `courses` DISABLE KEYS */;
INSERT INTO `courses` VALUES (8,'CMSC',330,'Dahtabases',12,'Spring',2017),(9,'CMSC',365,'Notworks',12,'Spring',2017),(10,'CMSC',385,'Coopstoon',11,'Spring',2017),(11,'CMSC',111,'test1',11,'Spring',2017),(12,'CMSC',112,'test2',11,'Spring',2017),(13,'CMSC',123,'abc',11,'Spring',2017),(14,'CMSC',399,'mass_1',11,'Spring',2017),(15,'CMSC',398,'mass_2',11,'Spring',2017),(16,'CMSC',397,'mass_3',11,'Spring',2017),(17,'CMSC',396,'mass_4',11,'Spring',2017),(18,'CMSC',395,'mass_5',11,'Spring',2017);
/*!40000 ALTER TABLE `courses` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `professor_review`
--

DROP TABLE IF EXISTS `professor_review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `professor_review` (
  `student_id` int(11) NOT NULL,
  `professor_first_name` varchar(45) NOT NULL,
  `professor_last_name` varchar(45) NOT NULL,
  `course` varchar(45) NOT NULL,
  `semester` varchar(10) NOT NULL,
  `year` int(11) NOT NULL,
  `comment` varchar(500) DEFAULT NULL,
  `rate_objectives` int(11) DEFAULT NULL,
  `rate_organized` int(11) DEFAULT NULL,
  `rate_challenging` int(11) DEFAULT NULL,
  `rate_outside_work` int(11) DEFAULT NULL,
  `rate_pace` int(11) DEFAULT NULL,
  `rate_assignments` int(11) DEFAULT NULL,
  `rate_grade_fairly` int(11) DEFAULT NULL,
  `rate_grade_time` int(11) DEFAULT NULL,
  `rate_accessibility` int(11) DEFAULT NULL,
  `rate_knowledge` int(11) DEFAULT NULL,
  `rate_career_development` int(11) DEFAULT NULL,
  `student_course_id` int(11) DEFAULT NULL,
  `professor_email` varchar(45) DEFAULT NULL,
  `comment_flagged` tinyint(4) NOT NULL DEFAULT '0',
  `comment_removed` tinyint(4) NOT NULL DEFAULT '0',
  `comment_approved` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`student_id`,`professor_first_name`,`professor_last_name`,`course`,`semester`,`year`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `professor_review`
--

LOCK TABLES `professor_review` WRITE;
/*!40000 ALTER TABLE `professor_review` DISABLE KEYS */;
INSERT INTO `professor_review` VALUES (8,'Grace','Mirsky','CMSC330','Spring',2017,'she\'s da best',5,5,5,5,5,5,5,5,5,5,5,13,'gmirsky@ben.edu',0,1,0),(8,'Grace','Mirsky','CMSC365','Spring',2017,'ratings other than all 5\'s',4,3,2,1,3,4,5,3,5,4,3,14,'gmirsky@ben.edu',0,0,0),(8,'Larry','Pollack','CMSC395','Spring',2017,'random scores for pollack just filling out his professor page',3,2,1,1,2,1,3,4,5,2,1,59,'lpollack@ben.edu',0,0,0),(8,'Larry','Pollack','CMSC397','Spring',2017,'3s for pollack',3,3,3,3,3,3,3,3,3,3,3,54,'lpollack@ben.edu',1,0,0),(8,'Larry','Pollack','CMSC398','Spring',2017,'4s for pollack',4,4,4,4,4,4,4,4,4,4,4,52,'lpollack@ben.edu',0,0,1),(8,'Larry','Pollack','CMSC399','Spring',2017,'all 5s for pollack',5,5,5,5,5,5,5,5,5,5,5,51,'lpollack@ben.edu',0,0,0),(9,'Larry','Pollack','CMSC111','Spring',2017,'test1',1,1,1,1,1,1,1,1,1,1,1,16,'lpollack@ben.edu',1,0,0),(9,'Larry','Pollack','CMSC112','Spring',2017,'test2',2,3,2,3,2,3,2,3,2,3,2,17,'lpollack@ben.edu',0,0,0);
/*!40000 ALTER TABLE `professor_review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `profile_pic`
--

DROP TABLE IF EXISTS `profile_pic`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `profile_pic` (
  `user_id` int(11) NOT NULL,
  `pic_location` varchar(500) DEFAULT NULL,
  PRIMARY KEY (`user_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `profile_pic`
--

LOCK TABLES `profile_pic` WRITE;
/*!40000 ALTER TABLE `profile_pic` DISABLE KEYS */;
/*!40000 ALTER TABLE `profile_pic` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `role`
--

DROP TABLE IF EXISTS `role`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `role` (
  `role_id` int(11) NOT NULL,
  `role_name` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`role_id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `role`
--

LOCK TABLES `role` WRITE;
/*!40000 ALTER TABLE `role` DISABLE KEYS */;
INSERT INTO `role` VALUES (1,'admin'),(2,'faculty'),(3,'tutor'),(4,'student');
/*!40000 ALTER TABLE `role` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `student_in_course`
--

DROP TABLE IF EXISTS `student_in_course`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `student_in_course` (
  `student_course_id` int(11) NOT NULL AUTO_INCREMENT,
  `course_id` int(11) NOT NULL DEFAULT '0',
  `student_id` int(11) NOT NULL,
  `course_reviewed` tinyint(4) NOT NULL DEFAULT '0',
  `disable_edit` tinyint(4) NOT NULL DEFAULT '0',
  `semester_past` tinyint(4) NOT NULL DEFAULT '0',
  PRIMARY KEY (`student_course_id`,`course_id`,`student_id`),
  KEY `fk_student_courses_users1_idx` (`student_id`),
  CONSTRAINT `fk_student_courses_users1` FOREIGN KEY (`student_id`) REFERENCES `users` (`user_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=62 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `student_in_course`
--

LOCK TABLES `student_in_course` WRITE;
/*!40000 ALTER TABLE `student_in_course` DISABLE KEYS */;
INSERT INTO `student_in_course` VALUES (13,8,8,1,1,0),(14,9,8,1,0,0),(15,10,8,0,0,0),(16,11,9,1,0,0),(17,12,9,1,0,0),(51,14,8,1,0,0),(52,15,8,1,0,0),(53,15,9,0,0,0),(54,16,8,1,0,0),(55,16,9,0,0,0),(56,17,8,0,0,0),(57,17,9,0,0,0),(58,17,19,0,0,0),(59,18,8,1,0,0),(60,18,9,0,0,0),(61,18,19,0,0,0);
/*!40000 ALTER TABLE `student_in_course` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tutor_appointment`
--

DROP TABLE IF EXISTS `tutor_appointment`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tutor_appointment` (
  `appointment_id` int(11) NOT NULL AUTO_INCREMENT,
  `student_id` int(11) NOT NULL,
  `tutor_id` int(11) NOT NULL,
  `date` varchar(11) NOT NULL,
  `time` varchar(45) DEFAULT NULL,
  `student_message` varchar(200) DEFAULT NULL,
  `tutor_message` varchar(200) DEFAULT NULL,
  `tutor_has_responded` tinyint(4) DEFAULT '0',
  `appointment_status` tinyint(4) NOT NULL DEFAULT '0',
  `appointment_past` tinyint(4) NOT NULL DEFAULT '0',
  `appointment_reviewed` tinyint(4) NOT NULL DEFAULT '0',
  `relationship_id` int(11) NOT NULL,
  PRIMARY KEY (`appointment_id`)
) ENGINE=InnoDB AUTO_INCREMENT=98 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tutor_appointment`
--

LOCK TABLES `tutor_appointment` WRITE;
/*!40000 ALTER TABLE `tutor_appointment` DISABLE KEYS */;
INSERT INTO `tutor_appointment` VALUES (90,8,14,'2017-05-31','14:22','school will end before this appointment - pending',NULL,0,0,0,0,6),(91,8,14,'2017-04-26','13:00','leave this pending until it is missed',NULL,0,0,1,0,5),(92,8,14,'2017-04-29','12:00','You can deny this one.','denied because reason',1,0,0,0,6),(94,8,16,'2017-04-28','10:00','here is one for bobbo-2','aww yeah',1,1,1,0,7),(95,8,14,'2017-04-24','13:00','appointment please','lets do it - edited',1,1,1,1,5),(96,8,14,'2017-05-12','15:01','approve this','as you wish',1,1,0,0,5),(97,8,16,'2017-04-26','14:01','coopstoon',NULL,0,0,1,0,7);
/*!40000 ALTER TABLE `tutor_appointment` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tutor_review`
--

DROP TABLE IF EXISTS `tutor_review`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tutor_review` (
  `review_id` int(11) NOT NULL AUTO_INCREMENT,
  `appointment_id` int(11) NOT NULL,
  `enhance_understanding` int(2) NOT NULL,
  `simple_examples` int(2) NOT NULL,
  `professional` int(2) NOT NULL,
  `prepared` int(2) NOT NULL,
  `schedule_again` int(2) NOT NULL,
  `recommend` int(2) NOT NULL,
  `comment` varchar(500) DEFAULT NULL,
  `student_id` int(11) NOT NULL,
  `tutor_id` int(11) NOT NULL,
  PRIMARY KEY (`review_id`)
) ENGINE=InnoDB AUTO_INCREMENT=10 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tutor_review`
--

LOCK TABLES `tutor_review` WRITE;
/*!40000 ALTER TABLE `tutor_review` DISABLE KEYS */;
INSERT INTO `tutor_review` VALUES (9,95,5,4,3,5,5,5,'leaving a comment on the tutor review',8,14);
/*!40000 ALTER TABLE `tutor_review` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tutors`
--

DROP TABLE IF EXISTS `tutors`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `tutors` (
  `tutor_relationship_id` int(11) NOT NULL AUTO_INCREMENT,
  `user_id_student` int(11) NOT NULL,
  `user_id_professor` int(11) NOT NULL,
  `course_id` int(11) NOT NULL,
  PRIMARY KEY (`tutor_relationship_id`)
) ENGINE=InnoDB AUTO_INCREMENT=8 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tutors`
--

LOCK TABLES `tutors` WRITE;
/*!40000 ALTER TABLE `tutors` DISABLE KEYS */;
INSERT INTO `tutors` VALUES (5,14,12,8),(6,14,12,9),(7,17,11,10);
/*!40000 ALTER TABLE `tutors` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `users` (
  `user_id` int(11) NOT NULL AUTO_INCREMENT,
  `first_name` varchar(45) NOT NULL,
  `last_name` varchar(45) NOT NULL,
  `email` varchar(45) NOT NULL,
  `encryptedPassword` varchar(255) NOT NULL,
  `role_id` int(11) NOT NULL,
  `confirmed` tinyint(4) NOT NULL DEFAULT '0',
  `active` tinyint(4) NOT NULL DEFAULT '1',
  `major` varchar(45) DEFAULT NULL,
  `school_year` int(11) DEFAULT '1',
  `nickname` varchar(45) DEFAULT NULL,
  `personal_email` varchar(45) DEFAULT NULL,
  PRIMARY KEY (`user_id`),
  UNIQUE KEY `email_UNIQUE` (`email`),
  KEY `fk_Users_Role1_idx` (`role_id`),
  CONSTRAINT `fk_Users_Role1` FOREIGN KEY (`role_id`) REFERENCES `role` (`role_id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=20 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users` DISABLE KEYS */;
INSERT INTO `users` VALUES (8,'Mike','Webb','b2125695@ben.edu','$2a$10$Hbmq2Th.iVrU2K060KW.SujuRZVPoFbv1ShsZcpHkPuK3PRz02jRS',4,1,1,'CMSC',4,NULL,NULL),(9,'Mike','Web','b2125699@ben.edu','$2a$10$/MZkzTjFlQJs1rimFUqpAe3wAnyX8fz83I/EzcpEmR4L9hM8Qq6Ze',4,1,1,'CMSC',1,NULL,NULL),(11,'Larry','Pollack','lpollack@ben.edu','$2a$10$ZkUH5aOb4fl0bNhpiSilNOe75QuYGs7zCwQRcMsFc3iHLRSyBLnlm',2,1,1,'CMSC',1,NULL,NULL),(12,'Grace','Mirsky','gmirsky@ben.edu','$2a$10$mLmyC/pnsOzrxcV/jEgGMOXCHWq9CKFVZcUhEL01siOBV2DW1y0K6',2,1,1,'CMSC',1,NULL,NULL),(13,'Michael','Webb','admin@ben.edu','$2a$10$rhV7P4xGhqz8HRSgzwHdZ.nV53tCOmD93/SAaJ6E7KybhIb85zolG',1,1,1,'CMSC',1,NULL,NULL),(14,'bob','joe','bob@joe.com','$2a$10$QUKtmuekXrb9awSscKQrueqUgtifOPibn5UbKHzDPQTf9sgrr6mMK',3,1,1,'CMSC',1,NULL,NULL),(16,'bob2','joe','bob2@joe.com','$2a$10$N8XTgW99s6eX6u7nwgrgTurxy4QK8fZ0u7zuSJv1.BDULhgh/7yUW',3,1,1,'CMSC',1,NULL,NULL),(17,'bob3','joe','bob3@joe.com','$2a$10$mCpMOi1gO8boQaUIiCNag.2TOzruSkuckEOS58MBsYgDxF3C/FDjy',3,1,1,'CMSC',1,NULL,NULL),(18,'cis','major','cis@ben.edu','$2a$10$RgwjbemkjVw2OY12P8t.huL3lIw6PrafZt90sfuNBMwZdyxa6fitG',3,1,1,'CMSC',1,NULL,NULL),(19,'student','uno','student1@ben.edu','$2a$10$zvKuv.1RI4mlFrBqhOu1DuCOlcDOzoXq/eu77841WkB8kWkqT8Ysa',4,1,1,'CMSC',1,NULL,NULL);
/*!40000 ALTER TABLE `users` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Dumping events for database 'rate'
--

--
-- Dumping routines for database 'rate'
--
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2017-05-01 13:34:04
