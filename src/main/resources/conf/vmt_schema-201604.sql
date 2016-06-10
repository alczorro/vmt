-- MySQL dump 10.13  Distrib 5.1.69, for redhat-linux-gnu (x86_64)
--
-- Host: localhost    Database: vmtdb
-- ------------------------------------------------------
-- Server version	5.1.69-log

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
-- Table structure for table `ip_filter`
--

DROP TABLE IF EXISTS `ip_filter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `ip_filter` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `ip` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=30 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `mq_message`
--

DROP TABLE IF EXISTS `mq_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `mq_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `publish_time` timestamp NULL DEFAULT NULL ON UPDATE CURRENT_TIMESTAMP,
  `type` varchar(255) DEFAULT NULL,
  `content` text,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=111425 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vmt_app`
--

DROP TABLE IF EXISTS `vmt_app`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vmt_app` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `team_dn` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `team_symbol` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `app_client_id` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `app_type` enum('oauth','local') COLLATE utf8_bin DEFAULT 'local',
  `app_name` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `app_url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `app_mobile_url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `logo_100_url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `logo_64_url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `logo_32_url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `logo_16_url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  `logo_custom_url` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=162 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vmt_app_profile`
--

DROP TABLE IF EXISTS `vmt_app_profile`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vmt_app_profile` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `fkId` varchar(255) DEFAULT NULL,
  `umtId` varchar(255) DEFAULT NULL,
  `cstnetId` varchar(255) DEFAULT NULL,
  `value` enum('hide','show') DEFAULT 'show',
  `appType` enum('local','oauth') DEFAULT 'local',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=13 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vmt_app_switch`
--

DROP TABLE IF EXISTS `vmt_app_switch`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vmt_app_switch` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `team_dn` varchar(255) COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=9868 DEFAULT CHARSET=utf8 COLLATE=utf8_bin;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vmt_email`
--

DROP TABLE IF EXISTS `vmt_email`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vmt_email` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sender_name` varchar(255) DEFAULT NULL,
  `sender_cstnet_id` varchar(255) DEFAULT NULL,
  `sender_umt_id` varchar(255) DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  `content` text,
  `send_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=210 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vmt_email_file`
--

DROP TABLE IF EXISTS `vmt_email_file`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vmt_email_file` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `clb_id` int(11) DEFAULT NULL,
  `file_name` varchar(255) DEFAULT NULL,
  `uploader_name` varchar(255) DEFAULT NULL,
  `uploader_cstnet_id` varchar(255) DEFAULT NULL,
  `uploader_umt_id` varchar(255) DEFAULT NULL,
  `email_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=110 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vmt_email_getter`
--

DROP TABLE IF EXISTS `vmt_email_getter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vmt_email_getter` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `getter_cstnet_id` varchar(255) DEFAULT NULL,
  `getter_name` varchar(255) DEFAULT NULL,
  `getter_umt_id` varchar(255) DEFAULT NULL,
  `group_id` varchar(255) DEFAULT NULL,
  `email_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=63721 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vmt_email_group`
--

DROP TABLE IF EXISTS `vmt_email_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vmt_email_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_master_umt_id` varchar(255) DEFAULT NULL,
  `group_name` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=48 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vmt_email_member`
--

DROP TABLE IF EXISTS `vmt_email_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vmt_email_member` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_dn` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `user_name` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `cstnet_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `umt_id` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  `domain` varchar(255) CHARACTER SET utf8 COLLATE utf8_bin DEFAULT NULL,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=57 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vmt_message`
--

DROP TABLE IF EXISTS `vmt_message`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vmt_message` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `msgTo` varchar(255) DEFAULT NULL,
  `teamName` varchar(255) DEFAULT NULL,
  `teamDN` varchar(255) DEFAULT NULL,
  `msgType` varchar(255) DEFAULT NULL,
  `msgStatus` varchar(255) DEFAULT NULL,
  `entryId` varchar(255) DEFAULT NULL,
  `createTime` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=7513 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vmt_message_columns`
--

DROP TABLE IF EXISTS `vmt_message_columns`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vmt_message_columns` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `msgId` int(11) DEFAULT NULL,
  `columnName` varchar(255) DEFAULT NULL,
  `columnValue` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `msg_id` (`msgId`),
  CONSTRAINT `msg_id_fk` FOREIGN KEY (`msgId`) REFERENCES `vmt_message` (`id`) ON DELETE CASCADE ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=24917 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vmt_org`
--

DROP TABLE IF EXISTS `vmt_org`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vmt_org` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_symbol` varchar(255) NOT NULL,
  `org_name` varchar(255) DEFAULT NULL,
  `is_cas` int(11) DEFAULT '0',
  `is_coremail` int(11) DEFAULT '0',
  `type` int(11) DEFAULT '0',
  PRIMARY KEY (`id`),
  UNIQUE KEY `org_domain_UNIQUE` (`org_symbol`)
) ENGINE=InnoDB AUTO_INCREMENT=297 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vmt_org_domain`
--

DROP TABLE IF EXISTS `vmt_org_domain`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vmt_org_domain` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `org_id` varchar(255) DEFAULT NULL,
  `org_domain` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `org_domain_UNIQUE` (`org_domain`)
) ENGINE=InnoDB AUTO_INCREMENT=729 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vmt_sms`
--

DROP TABLE IF EXISTS `vmt_sms`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vmt_sms` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `status` varchar(255) DEFAULT NULL,
  `sender_name` varchar(255) DEFAULT NULL,
  `sender_cstnet_id` varchar(255) DEFAULT NULL,
  `sender_umt_id` varchar(255) DEFAULT NULL,
  `content` varchar(255) DEFAULT NULL,
  `send_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `group_id` int(11) DEFAULT NULL,
  `sms_uuid` varchar(255) DEFAULT NULL,
  KEY `id` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=150 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vmt_sms_getter`
--

DROP TABLE IF EXISTS `vmt_sms_getter`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vmt_sms_getter` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `umt_id` varchar(255) DEFAULT NULL,
  `cstnet_id` varchar(255) DEFAULT NULL,
  `mobile` varchar(255) DEFAULT NULL,
  `stat` varchar(255) DEFAULT NULL,
  `sms_id` int(11) DEFAULT NULL,
  `true_name` varchar(255) DEFAULT NULL,
  `sms_uuid` varchar(255) DEFAULT NULL,
  `send_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`),
  KEY `sms_id` (`sms_id`),
  CONSTRAINT `v_s_g_sid` FOREIGN KEY (`sms_id`) REFERENCES `vmt_sms` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=244 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vmt_sms_group`
--

DROP TABLE IF EXISTS `vmt_sms_group`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vmt_sms_group` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_name` varchar(255) DEFAULT NULL,
  `account` varchar(255) DEFAULT NULL,
  `sms_count` int(11) DEFAULT NULL,
  `sms_used` int(11) DEFAULT '0',
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=14 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vmt_sms_group_member`
--

DROP TABLE IF EXISTS `vmt_sms_group_member`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vmt_sms_group_member` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `m_umt_id` varchar(255) DEFAULT NULL,
  `m_cstnet_id` varchar(255) DEFAULT NULL,
  `m_true_name` varchar(255) DEFAULT NULL,
  `m_is_admin` int(11) DEFAULT NULL,
  `sms_group_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `sms_group_id` (`sms_group_id`),
  CONSTRAINT `s_g_m` FOREIGN KEY (`sms_group_id`) REFERENCES `vmt_sms_group` (`id`) ON DELETE CASCADE ON UPDATE CASCADE
) ENGINE=InnoDB AUTO_INCREMENT=34 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vmt_sms_recharge_log`
--

DROP TABLE IF EXISTS `vmt_sms_recharge_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vmt_sms_recharge_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `group_id` int(11) DEFAULT NULL,
  `plus` int(11) DEFAULT NULL,
  `last` int(11) DEFAULT NULL,
  `who_charged` varchar(255) DEFAULT NULL,
  `charged_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=6 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vmt_sms_send_log`
--

DROP TABLE IF EXISTS `vmt_sms_send_log`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vmt_sms_send_log` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `sender_name` varchar(255) DEFAULT NULL,
  `sender_cstnet_id` varchar(255) DEFAULT NULL,
  `sender_umt_id` varchar(255) DEFAULT NULL,
  `send_success_count` varchar(255) DEFAULT NULL,
  `sms_id` int(11) DEFAULT NULL,
  `send_time` timestamp NULL DEFAULT CURRENT_TIMESTAMP,
  `group_id` int(11) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `sms_id` (`sms_id`),
  KEY `group_id` (`group_id`),
  CONSTRAINT `v_s_l_groupId` FOREIGN KEY (`group_id`) REFERENCES `vmt_sms_group` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,
  CONSTRAINT `v_s_l_smsId` FOREIGN KEY (`sms_id`) REFERENCES `vmt_sms` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=152 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `vmt_user_team_rel`
--

DROP TABLE IF EXISTS `vmt_user_team_rel`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `vmt_user_team_rel` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `team_dn` varchar(255) DEFAULT NULL,
  `umt_id` varchar(255) DEFAULT NULL,
  `type` int(11) DEFAULT NULL,
  `status` varchar(255) DEFAULT NULL,
  `symbol` varchar(255) DEFAULT NULL,
  `team_name` varchar(255) DEFAULT NULL,
  `user_name` varchar(255) DEFAULT NULL,
  `user_dn` varchar(255) DEFAULT NULL,
  `user_visible` int(11) DEFAULT '1',
  `user_cstnet_id` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `dn_index` (`team_dn`),
  KEY `umtId_index` (`umt_id`),
  KEY `symbol_index` (`symbol`),
  KEY `status_index` (`status`)
) ENGINE=InnoDB AUTO_INCREMENT=2870695 DEFAULT CHARSET=utf8;
/*!40101 SET character_set_client = @saved_cs_client */;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-04-15 19:44:25
