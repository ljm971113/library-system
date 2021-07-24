/*
 Navicat Premium Data Transfer

 Source Server         : localhost
 Source Server Type    : MySQL
 Source Server Version : 50728
 Source Host           : localhost:3306
 Source Schema         : libsys

 Target Server Type    : MySQL
 Target Server Version : 50728
 File Encoding         : 65001

 Date: 19/07/2021 23:55:55
*/

CREATE DATABASE libsys SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ----------------------------
-- Table structure for tbl_book
-- ----------------------------
DROP TABLE IF EXISTS `tbl_book`;
CREATE TABLE `tbl_book` (
  `bid` int(11) NOT NULL AUTO_INCREMENT COMMENT '图书编号',
  `bname` varchar(50) NOT NULL COMMENT '图书名称',
  `typeid` int(11) DEFAULT NULL COMMENT '图书类别ID',
  `author` varchar(50) DEFAULT NULL COMMENT '图书作者',
  `companyname` varchar(50) DEFAULT NULL COMMENT '图书出版社名称',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '图书状态标志',
  `content` longtext COMMENT '图书简介',
  PRIMARY KEY (`bid`) USING BTREE,
  KEY `typeid_tbl` (`typeid`) USING BTREE,
  CONSTRAINT `typeid_tbl` FOREIGN KEY (`typeid`) REFERENCES `tbl_book_type` (`tid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbl_book
-- ----------------------------
BEGIN;
INSERT INTO `tbl_book` VALUES (1, '英语书', 2, '王狗蛋', '王狗蛋和张二狗的出版社', 1, '测试');
INSERT INTO `tbl_book` VALUES (2, '怎么看英语书', 2, '王二狗', '测试出版社', 1, '测试简介');
COMMIT;

-- ----------------------------
-- Table structure for tbl_book_reserves
-- ----------------------------
DROP TABLE IF EXISTS `tbl_book_reserves`;
CREATE TABLE `tbl_book_reserves` (
  `id` int(11) NOT NULL AUTO_INCREMENT,
  `bid` int(11) NOT NULL COMMENT '图书ID',
  `total` int(11) NOT NULL COMMENT '图书总量',
  `borrowed` int(11) NOT NULL COMMENT '借出的数量',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '记录状态',
  PRIMARY KEY (`id`) USING BTREE,
  KEY `bid_book` (`bid`) USING BTREE,
  CONSTRAINT `bid_book` FOREIGN KEY (`bid`) REFERENCES `tbl_book` (`bid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbl_book_reserves
-- ----------------------------
BEGIN;
INSERT INTO `tbl_book_reserves` VALUES (1, 1, 12, 1, 1);
INSERT INTO `tbl_book_reserves` VALUES (2, 2, 12, 0, 1);
COMMIT;

-- ----------------------------
-- Table structure for tbl_book_type
-- ----------------------------
DROP TABLE IF EXISTS `tbl_book_type`;
CREATE TABLE `tbl_book_type` (
  `tid` int(11) NOT NULL AUTO_INCREMENT COMMENT '图书类别ID',
  `typename` varchar(50) NOT NULL COMMENT '图书类别名字',
  PRIMARY KEY (`tid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbl_book_type
-- ----------------------------
BEGIN;
INSERT INTO `tbl_book_type` VALUES (1, '测试');
INSERT INTO `tbl_book_type` VALUES (2, '英语');
COMMIT;

-- ----------------------------
-- Table structure for tbl_borrowinfo
-- ----------------------------
DROP TABLE IF EXISTS `tbl_borrowinfo`;
CREATE TABLE `tbl_borrowinfo` (
  `browid` int(11) NOT NULL AUTO_INCREMENT COMMENT '借阅信息ID',
  `userid` int(11) NOT NULL COMMENT '用户ID',
  `bookid` int(11) NOT NULL COMMENT '书籍ID',
  `brrodate` date NOT NULL COMMENT '借书日期',
  `returndate` date NOT NULL COMMENT '应还书日期',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '记录状态',
  `totalbrrow` int(11) NOT NULL COMMENT '借书总数',
  PRIMARY KEY (`browid`) USING BTREE,
  KEY `uid_borro` (`userid`) USING BTREE,
  KEY `bid_borro` (`bookid`) USING BTREE,
  CONSTRAINT `bid_borro` FOREIGN KEY (`bookid`) REFERENCES `tbl_book` (`bid`),
  CONSTRAINT `uid_borro` FOREIGN KEY (`userid`) REFERENCES `tbl_user` (`uid`)
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbl_borrowinfo
-- ----------------------------
BEGIN;
INSERT INTO `tbl_borrowinfo` VALUES (1, 2, 1, '2021-07-18', '2021-08-18', 0, 1);
INSERT INTO `tbl_borrowinfo` VALUES (2, 2, 1, '2021-07-18', '2021-08-18', 1, 1);
COMMIT;

-- ----------------------------
-- Table structure for tbl_user
-- ----------------------------
DROP TABLE IF EXISTS `tbl_user`;
CREATE TABLE `tbl_user` (
  `uid` int(11) NOT NULL AUTO_INCREMENT COMMENT '用户ID',
  `username` varchar(50) NOT NULL COMMENT '用户名',
  `gender` int(1) NOT NULL COMMENT '用户性别',
  `email` varchar(50) NOT NULL COMMENT '用户邮箱',
  `phone` varchar(50) NOT NULL COMMENT '用户电话',
  `password` varchar(255) NOT NULL COMMENT '用户登录密码',
  `authority` varchar(50) NOT NULL COMMENT '用户权限',
  `since` date NOT NULL COMMENT '用户注册日期',
  `status` int(1) NOT NULL DEFAULT '1' COMMENT '用户状态',
  PRIMARY KEY (`uid`) USING BTREE
) ENGINE=InnoDB AUTO_INCREMENT=3 DEFAULT CHARSET=utf8mb4 ROW_FORMAT=DYNAMIC;

-- ----------------------------
-- Records of tbl_user
-- ----------------------------
BEGIN;
INSERT INTO `tbl_user` VALUES (1, 'admin', 0, 'admin@qq.com', '', 'admin', 'ROLE_admin', '2021-07-18', 1);
INSERT INTO `tbl_user` VALUES (2, 'user1', 1, 'user1@qq.com', '', 'user1', 'ROLE_common', '2021-07-18', 1);
COMMIT;

SET FOREIGN_KEY_CHECKS = 1;
