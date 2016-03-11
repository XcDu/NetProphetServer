-- MySQL dump 10.13  Distrib 5.5.47, for debian-linux-gnu (x86_64)
--
-- Host: localhost    Database: netprophet
-- ------------------------------------------------------
-- Server version	5.5.47-0ubuntu0.14.04.1

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
-- Table structure for table `networkingdata`
--

DROP TABLE IF EXISTS `networkingdata`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `networkingdata` (
  `reqID` bigint(20) NOT NULL AUTO_INCREMENT,
  `networkType` varchar(256) DEFAULT NULL,
  `networkName` varchar(16) DEFAULT NULL,
  `WIFISignalLevel` int(11) DEFAULT NULL,
  `cellSignalLevel` int(11) DEFAULT NULL,
  `MCC` int(11) DEFAULT NULL,
  `MNC` int(11) DEFAULT NULL,
  `LAC` int(11) DEFAULT NULL,
  `firstMileLatency` int(11) DEFAULT NULL,
  `firstMilePacketLossRate` int(11) DEFAULT NULL,
  PRIMARY KEY (`reqID`)
) ENGINE=InnoDB AUTO_INCREMENT=1456539957853 DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `networkingdata`
--

LOCK TABLES `networkingdata` WRITE;
/*!40000 ALTER TABLE `networkingdata` DISABLE KEYS */;
/*!40000 ALTER TABLE `networkingdata` ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `timing`
--

DROP TABLE IF EXISTS `timing`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!40101 SET character_set_client = utf8 */;
CREATE TABLE `timing` (
  `reqID` bigint(20) NOT NULL AUTO_INCREMENT,
  `url` varchar(2083) DEFAULT NULL,
  `method` varchar(256) DEFAULT NULL,
  `userID` varchar(256) DEFAULT NULL,
  `prevReqID` bigint(20) DEFAULT NULL,
  `nextReqID` bigint(20) DEFAULT NULL,
  `startTime` bigint(20) DEFAULT NULL,
  `endTime` bigint(20) DEFAULT NULL,
  `overallDelay` bigint(20) DEFAULT NULL,
  `dnsDelay` bigint(20) DEFAULT NULL,
  `connDelay` bigint(20) DEFAULT NULL,
  `handshakeDelay` bigint(20) DEFAULT NULL,
  `tlsDelay` bigint(20) DEFAULT NULL,
  `reqWriteDelay` bigint(20) DEFAULT NULL,
  `serverDelay` bigint(20) DEFAULT NULL,
  `TTFBDelay` bigint(20) DEFAULT NULL,
  `respTransDelay` bigint(20) DEFAULT NULL,
  `useConnCache` tinyint(1) DEFAULT NULL,
  `useDNSCache` tinyint(1) DEFAULT NULL,
  `useRespCache` tinyint(1) DEFAULT NULL,
  `respSize` bigint(20) DEFAULT NULL,
  `HTTPCode` int(11) DEFAULT NULL,
  `reqSize` int(11) DEFAULT NULL,
  `isFailedRequest` tinyint(1) DEFAULT NULL,
  `errorMsg` varchar(256) DEFAULT NULL,
  `detailedErrorMsg` varchar(256) DEFAULT NULL,
  `transID` bigint(20) DEFAULT NULL,
  `transType` int(11) DEFAULT NULL,
  PRIMARY KEY (`reqID`)
) ENGINE=InnoDB DEFAULT CHARSET=latin1;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `timing`
--

LOCK TABLES `timing` WRITE;
/*!40000 ALTER TABLE `timing` DISABLE KEYS */;
/*!40000 ALTER TABLE `timing` ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE=@OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE=@OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS=@OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS=@OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS=@OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION=@OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES=@OLD_SQL_NOTES */;

-- Dump completed on 2016-03-10 20:48:47
