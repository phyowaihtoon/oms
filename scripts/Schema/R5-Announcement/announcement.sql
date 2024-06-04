use oms;


DROP TABLE IF EXISTS `announcement`;
/*!40101 SET @saved_cs_client     = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `announcement` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(1000) NOT NULL,
  `active_flag` char(1) NOT NULL,
  `del_flag` char(1) NOT NULL,
   PRIMARY KEY (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=16 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;


INSERT INTO `menu_item` VALUES (28,'announcement','Announcement','global.menu.entities.announcement','','announcement',5,6);
