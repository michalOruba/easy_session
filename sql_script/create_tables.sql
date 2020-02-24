-- CREATE DATABASE EASY SESSION

CREATE DATABASE IF NOT EXISTS  `easy_session`;
USE `easy_session`;

-- CREATE STUDENT TABLE

SET FOREIGN_KEY_CHECKS = 0;

CREATE TABLE `student` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `first_name` varchar(50) DEFAULT NULL,
    `last_name` varchar(50) DEFAULT NULL,
    `email` varchar (50) DEFAULT NULL,
    `semester` tinyint(1) DEFAULT 0,
    `session_id` int(11) DEFAULT NULL,
    `specialization_id` int(11) DEFAULT NULL,

    PRIMARY KEY (`id`),

    CONSTRAINT `FK_SESSION_STU`
    FOREIGN KEY (`session_id`)
    REFERENCES `session` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,

    CONSTRAINT `FK_SPECIALIZATION_STU`
    FOREIGN KEY (`specialization_id`)
    REFERENCES `specialization` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


-- CREATE SESSION TABLE

CREATE TABLE `session` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `status` tinyint(1) DEFAULT 0,
    `semester` tinyint(1) DEFAULT 0,
    `student_id` int(11) DEFAULT NULL,

    PRIMARY KEY (`id`),

    CONSTRAINT `FK_STUDENT_SES`
    FOREIGN KEY (`student_id`)
    REFERENCES `student` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


-- CREATE SUBJECT LIST TABLE

CREATE TABLE `subject_grade_map` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `is_passed` tinyint(1) DEFAULT 0,
    `subject_id` int(11) DEFAULT NULL,
    `session_id` int(11) DEFAULT NULL,

    PRIMARY KEY (`id`),

    CONSTRAINT `FK_SUBJECT_SGM`
    FOREIGN KEY (`subject_id`)
    REFERENCES `subject` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION,

    CONSTRAINT `FK_SESSION_SGM`
    FOREIGN KEY (`session_id`)
    REFERENCES `session` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

-- CREATE SUBJECT TABLE

CREATE TABLE `subject` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(150) DEFAULT NULL,
    `hours` smallint(3) DEFAULT NULL,
    `ECTS` smallint(2) DEFAULT NULL,
    `semester` tinyint(1) DEFAULT NULL,
    `specialization_id` int(11) DEFAULT NULL,

    PRIMARY KEY (`id`),

    CONSTRAINT `FK_SPECIALIZATION_SUB`
    FOREIGN KEY (`specialization_id`)
    REFERENCES `specialization` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

-- CREATE SPECIALIZATION TABLE

CREATE TABLE `specialization` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(150) DEFAULT NULL,
    `specialization_start_date` DATE DEFAULT NULL,
    `specialization_end_date` DATE DEFAULT NULL,
    `field_of_study_id` int(11) DEFAULT NULL,

    PRIMARY KEY (`id`),

    CONSTRAINT `FK_FIELD_OF_STUDY_SPE`
    FOREIGN KEY (`field_of_study_id`)
    REFERENCES `field_of_study` (`id`) ON DELETE NO ACTION ON UPDATE NO ACTION
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;

-- CREATE FIELD_OF_STUDY TABLE

CREATE TABLE `field_of_study` (
    `id` int(11) NOT NULL AUTO_INCREMENT,
    `name` varchar(150) DEFAULT NULL,

    PRIMARY KEY (`id`),

    UNIQUE KEY `NAME_UNIQUE` (`name`)
) ENGINE=InnoDB AUTO_INCREMENT=1 DEFAULT CHARSET=latin1;


SET FOREIGN_KEY_CHECKS = 1;