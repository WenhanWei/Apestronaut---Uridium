CREATE DATABASE IF NOT EXISTS`game` DEFAULT CHARACTER SET utf8;

USE `game`;

/*Table structure for table `players` and `workshop` */

DROP TABLE IF EXISTS `players`;
DROP TABLE IF EXISTS `workshop`;

CREATE TABLE `players` (
  `id` INT NOT NULL AUTO_INCREMENT,
  `username` VARCHAR(20) NOT NULL,
  `password` VARCHAR(40) NOT NULL,
  `authenticate` DATE DEFAULT '1970-01-01' NOT NULL,
  `singlePlayerScore` INT DEFAULT 0 NOT NULL,
  `multiPlayerScore` INT DEFAULT 0 NOT NULL,
  `highestLevel` INT DEFAULT 0 NOT NULL,
  #`avatar` BLOB,
  PRIMARY KEY (id),
  UNIQUE (username)
  
) ENGINE=INNODB DEFAULT CHARSET=utf8;

CREATE TABLE `workshop` (

  `author` VARCHAR(20) NOT NULL,
  `filename` VARCHAR(40) DEFAULT 'myCreation' NOT NULL,
  `levelFile` MEDIUMBLOB,
   UNIQUE (filename)

) ENGINE=INNODB DEFAULT CHARSET=utf8;