DROP TABLE IF EXISTS User;

CREATE TABLE User (
  id INT AUTO_INCREMENT  PRIMARY KEY,
  username VARCHAR(250) NOT NULL,
  firstName VARCHAR(250) NOT NULL,
  lastName VARCHAR(250) NOT NULL,
 password VARCHAR(250) NOT NULL,
 email VARCHAR(250) NOT NULL
);

INSERT INTO User (username, firstName, lastName, password, email) VALUES
  ('admin', 'test', 'test', 'password', 'email');