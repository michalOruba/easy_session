-- CREATE USER easysession

CREATE USER 'easysession'@'localhost' IDENTIFIED BY 'easysession';
GRANT ALL PRIVILEGES ON `easy_session`.* TO 'easysession'@'localhost';

ALTER USER 'easysession'@'localhost' IDENTIFIED WITH mysql_native_password BY 'easysession';