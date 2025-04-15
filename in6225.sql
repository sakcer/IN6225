-- --------------------------------------------------------
-- 主机:                           127.0.0.1
-- 服务器版本:                        8.0.39 - MySQL Community Server - GPL
-- 服务器操作系统:                      Win64
-- HeidiSQL 版本:                  12.10.0.7000
-- --------------------------------------------------------

/*!40101 SET @OLD_CHARACTER_SET_CLIENT=@@CHARACTER_SET_CLIENT */;
/*!40101 SET NAMES utf8 */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE=@@TIME_ZONE */;
/*!40103 SET TIME_ZONE='+00:00' */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS=@@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS=0 */;
/*!40101 SET @OLD_SQL_MODE=@@SQL_MODE, SQL_MODE='NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES=@@SQL_NOTES, SQL_NOTES=0 */;


-- 导出 IN6225 的数据库结构
CREATE DATABASE IF NOT EXISTS `IN6225` /*!40100 DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_0900_ai_ci */ /*!80016 DEFAULT ENCRYPTION='N' */;
USE `IN6225`;

-- 导出  表 IN6225.project 结构
CREATE TABLE IF NOT EXISTS `project` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `description` varchar(255) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `progress` int NOT NULL,
  `start_date` date DEFAULT NULL,
  `status` enum('ACTIVE','COMPLETED') DEFAULT NULL,
  `leader_id` bigint DEFAULT NULL,
  PRIMARY KEY (`id`),
  KEY `FKiw5kyt4ful6dvrcq3rmktwi98` (`leader_id`),
  CONSTRAINT `FKiw5kyt4ful6dvrcq3rmktwi98` FOREIGN KEY (`leader_id`) REFERENCES `user` (`id`)
) ENGINE=InnoDB AUTO_INCREMENT=4 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  IN6225.project 的数据：~3 rows (大约)
INSERT IGNORE INTO `project` (`id`, `description`, `name`, `progress`, `start_date`, `status`, `leader_id`) VALUES
	(1, 'this is project1', 'Project1', 20, '2025-04-07', 'ACTIVE', 22),
	(2, 'this is project2', 'Project2', 100, '2025-04-07', 'COMPLETED', 23),
	(3, 'this is project3', 'Project3', 10, '2025-04-07', 'ACTIVE', 24);

-- 导出  表 IN6225.project_user 结构
CREATE TABLE IF NOT EXISTS `project_user` (
  `project_id` bigint NOT NULL,
  `user_id` bigint NOT NULL,
  KEY `FK4jl2o131jivd80xsuw6pivnbx` (`user_id`),
  KEY `FK4ug72llnm0n7yafwntgdswl3y` (`project_id`),
  CONSTRAINT `FK4jl2o131jivd80xsuw6pivnbx` FOREIGN KEY (`user_id`) REFERENCES `user` (`id`),
  CONSTRAINT `FK4ug72llnm0n7yafwntgdswl3y` FOREIGN KEY (`project_id`) REFERENCES `project` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  IN6225.project_user 的数据：~11 rows (大约)
INSERT IGNORE INTO `project_user` (`project_id`, `user_id`) VALUES
	(1, 25),
	(1, 2),
	(1, 3),
	(1, 4),
	(2, 5),
	(2, 4),
	(2, 2),
	(2, 7),
	(2, 25),
	(3, 25),
	(3, 5);

-- 导出  表 IN6225.user 结构
CREATE TABLE IF NOT EXISTS `user` (
  `id` bigint NOT NULL AUTO_INCREMENT,
  `avatar` varchar(255) DEFAULT NULL,
  `department` enum('CS','FIN','HR','LGL','MKT','OPS','SLS','TCH') DEFAULT NULL,
  `email` varchar(255) DEFAULT NULL,
  `employee_id` varchar(255) DEFAULT NULL,
  `join_date` datetime(6) DEFAULT NULL,
  `name` varchar(255) DEFAULT NULL,
  `password` varchar(255) DEFAULT NULL,
  `role` enum('ADMIN','LEADER','USER') DEFAULT NULL,
  `status` enum('ACTIVE','INACTIVE') DEFAULT NULL,
  `title` varchar(255) DEFAULT NULL,
  PRIMARY KEY (`id`),
  UNIQUE KEY `UKr1usl9qoplqsbrhha5e0niqng` (`employee_id`)
) ENGINE=InnoDB AUTO_INCREMENT=26 DEFAULT CHARSET=utf8mb4 COLLATE=utf8mb4_0900_ai_ci;

-- 正在导出表  IN6225.user 的数据：~25 rows (大约)
INSERT IGNORE INTO `user` (`id`, `avatar`, `department`, `email`, `employee_id`, `join_date`, `name`, `password`, `role`, `status`, `title`) VALUES
	(1, NULL, 'HR', 'Admin@example.com', 'EID_99999', '2025-04-01 21:56:05.000000', 'Admin', '$2a$10$4KUBqhlopGmTMx3YRzRj9OOr/iblmyAzilIG/LGOpx9Z9Obkrx4cG', 'ADMIN', 'ACTIVE', 'Administrator'),
	(2, NULL, 'HR', 'User10000@example.com', 'EID_10000', '2025-04-16 00:00:00.000000', 'User10000', '$2a$10$4KUBqhlopGmTMx3YRzRj9OOr/iblmyAzilIG/LGOpx9Z9Obkrx4cG', 'USER', 'ACTIVE', 'Engineer'),
	(3, NULL, 'TCH', 'User10001@example.com', 'EID_10001', '2025-04-20 00:00:00.000000', 'User10001', '$2a$10$4KUBqhlopGmTMx3YRzRj9OOr/iblmyAzilIG/LGOpx9Z9Obkrx4cG', 'USER', 'ACTIVE', 'Intern'),
	(4, NULL, 'HR', 'User10002@example.com', 'EID_10002', '2025-04-04 00:00:00.000000', 'User10002', '$2a$10$4KUBqhlopGmTMx3YRzRj9OOr/iblmyAzilIG/LGOpx9Z9Obkrx4cG', 'USER', 'ACTIVE', 'Intern'),
	(5, NULL, 'HR', 'User10003@example.com', 'EID_10003', '2025-04-08 00:00:00.000000', 'User10003', '$2a$10$4KUBqhlopGmTMx3YRzRj9OOr/iblmyAzilIG/LGOpx9Z9Obkrx4cG', 'USER', 'INACTIVE', 'Analyst'),
	(6, NULL, 'LGL', 'User10004@example.com', 'EID_10004', '2025-04-24 00:00:00.000000', 'User10004', '$2a$10$4KUBqhlopGmTMx3YRzRj9OOr/iblmyAzilIG/LGOpx9Z9Obkrx4cG', 'USER', 'ACTIVE', 'Manager'),
	(7, NULL, 'HR', 'User10005@example.com', 'EID_10005', '2025-04-15 00:00:00.000000', 'User10005', '$2a$10$4KUBqhlopGmTMx3YRzRj9OOr/iblmyAzilIG/LGOpx9Z9Obkrx4cG', 'USER', 'INACTIVE', 'Analyst'),
	(8, NULL, 'OPS', 'User10006@example.com', 'EID_10006', '2025-04-22 00:00:00.000000', 'User10006', '$2a$10$4KUBqhlopGmTMx3YRzRj9OOr/iblmyAzilIG/LGOpx9Z9Obkrx4cG', 'USER', 'INACTIVE', 'Analyst'),
	(9, NULL, 'TCH', 'User10007@example.com', 'EID_10007', '2025-04-25 00:00:00.000000', 'User10007', '$2a$10$4KUBqhlopGmTMx3YRzRj9OOr/iblmyAzilIG/LGOpx9Z9Obkrx4cG', 'USER', 'ACTIVE', 'Director'),
	(10, NULL, 'TCH', 'User10008@example.com', 'EID_10008', '2025-04-02 00:00:00.000000', 'User10008', '$2a$10$4KUBqhlopGmTMx3YRzRj9OOr/iblmyAzilIG/LGOpx9Z9Obkrx4cG', 'USER', 'ACTIVE', 'Manager'),
	(11, NULL, 'HR', 'User10009@example.com', 'EID_10009', '2025-04-20 00:00:00.000000', 'User10009', '$2a$10$4KUBqhlopGmTMx3YRzRj9OOr/iblmyAzilIG/LGOpx9Z9Obkrx4cG', 'USER', 'ACTIVE', 'Intern'),
	(12, NULL, 'LGL', 'User10010@example.com', 'EID_10010', '2025-04-21 00:00:00.000000', 'User10010', '$2a$10$4KUBqhlopGmTMx3YRzRj9OOr/iblmyAzilIG/LGOpx9Z9Obkrx4cG', 'USER', 'ACTIVE', 'Director'),
	(13, NULL, 'TCH', 'User10011@example.com', 'EID_10011', '2025-04-03 00:00:00.000000', 'User10011', '$2a$10$4KUBqhlopGmTMx3YRzRj9OOr/iblmyAzilIG/LGOpx9Z9Obkrx4cG', 'USER', 'INACTIVE', 'Director'),
	(14, NULL, 'LGL', 'User10012@example.com', 'EID_10012', '2025-04-29 00:00:00.000000', 'User10012', '$2a$10$4KUBqhlopGmTMx3YRzRj9OOr/iblmyAzilIG/LGOpx9Z9Obkrx4cG', 'USER', 'INACTIVE', 'Analyst'),
	(15, NULL, 'LGL', 'User10013@example.com', 'EID_10013', '2025-04-21 00:00:00.000000', 'User10013', '$2a$10$4KUBqhlopGmTMx3YRzRj9OOr/iblmyAzilIG/LGOpx9Z9Obkrx4cG', 'USER', 'INACTIVE', 'Manager'),
	(16, NULL, 'HR', 'User10014@example.com', 'EID_10014', '2025-04-09 00:00:00.000000', 'User10014', '$2a$10$4KUBqhlopGmTMx3YRzRj9OOr/iblmyAzilIG/LGOpx9Z9Obkrx4cG', 'USER', 'ACTIVE', 'Manager'),
	(17, NULL, 'CS', 'User10015@example.com', 'EID_10015', '2025-04-20 00:00:00.000000', 'User10015', '$2a$10$4KUBqhlopGmTMx3YRzRj9OOr/iblmyAzilIG/LGOpx9Z9Obkrx4cG', 'USER', 'INACTIVE', 'Director'),
	(18, NULL, 'OPS', 'User10016@example.com', 'EID_10016', '2025-04-07 00:00:00.000000', 'User10016', '$2a$10$4KUBqhlopGmTMx3YRzRj9OOr/iblmyAzilIG/LGOpx9Z9Obkrx4cG', 'USER', 'INACTIVE', 'Manager'),
	(19, NULL, 'LGL', 'User10017@example.com', 'EID_10017', '2025-04-24 00:00:00.000000', 'User10017', '$2a$10$4KUBqhlopGmTMx3YRzRj9OOr/iblmyAzilIG/LGOpx9Z9Obkrx4cG', 'USER', 'ACTIVE', 'Analyst'),
	(20, NULL, 'TCH', 'User10018@example.com', 'EID_10018', '2025-04-08 00:00:00.000000', 'User10018', '$2a$10$4KUBqhlopGmTMx3YRzRj9OOr/iblmyAzilIG/LGOpx9Z9Obkrx4cG', 'USER', 'INACTIVE', 'Engineer'),
	(21, NULL, 'MKT', 'User10019@example.com', 'EID_10019', '2025-04-30 00:00:00.000000', 'User10019', '$2a$10$4KUBqhlopGmTMx3YRzRj9OOr/iblmyAzilIG/LGOpx9Z9Obkrx4cG', 'USER', 'ACTIVE', 'Manager'),
	(22, NULL, 'OPS', 'Leader1@example.com', 'EID_00001', '2025-04-07 15:56:05.000000', 'Leader1', '$2a$10$4KUBqhlopGmTMx3YRzRj9OOr/iblmyAzilIG/LGOpx9Z9Obkrx4cG', 'LEADER', 'ACTIVE', 'Leader1'),
	(23, NULL, 'TCH', 'Leader2@example.com', 'EID_00002', '2025-04-15 15:56:05.000000', 'Leader2', '$2a$10$4KUBqhlopGmTMx3YRzRj9OOr/iblmyAzilIG/LGOpx9Z9Obkrx4cG', 'LEADER', 'ACTIVE', 'Leader2'),
	(24, NULL, 'MKT', 'Leader3@example.com', 'EID_00003', '2025-04-10 15:56:05.000000', 'Leader3', '$2a$10$4KUBqhlopGmTMx3YRzRj9OOr/iblmyAzilIG/LGOpx9Z9Obkrx4cG', 'LEADER', 'ACTIVE', 'Leader3'),
	(25, NULL, 'MKT', 'User@example.com', 'EID_00000', '2025-04-02 15:56:05.000000', 'User', '$2a$10$4KUBqhlopGmTMx3YRzRj9OOr/iblmyAzilIG/LGOpx9Z9Obkrx4cG', 'USER', 'ACTIVE', 'User');

/*!40103 SET TIME_ZONE=IFNULL(@OLD_TIME_ZONE, 'system') */;
/*!40101 SET SQL_MODE=IFNULL(@OLD_SQL_MODE, '') */;
/*!40014 SET FOREIGN_KEY_CHECKS=IFNULL(@OLD_FOREIGN_KEY_CHECKS, 1) */;
/*!40101 SET CHARACTER_SET_CLIENT=@OLD_CHARACTER_SET_CLIENT */;
/*!40111 SET SQL_NOTES=IFNULL(@OLD_SQL_NOTES, 1) */;
