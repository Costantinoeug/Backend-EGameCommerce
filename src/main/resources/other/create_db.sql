DROP SCHEMA egamecommercedb;
CREATE SCHEMA egamecommercedb;
USE egamecommercedb;



CREATE TABLE user (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    username VARCHAR(70) UNIQUE
);

CREATE TABLE cart (
    id INTEGER AUTO_INCREMENT PRIMARY KEY ,
    price FLOAT,
    version LONG,
    related_user INTEGER,
    FOREIGN KEY (related_user) REFERENCES user(id)
);

CREATE TABLE game (
    name VARCHAR(50) PRIMARY KEY NOT NULL,
    publisher VARCHAR(50),
    developer VARCHAR(50),
    pegi INTEGER,
    platform VARCHAR(150),
    genre VARCHAR(150),
    description VARCHAR(500),
    price FLOAT NOT NULL,
    quantity INTEGER NOT NULL,
    version LONG,
    hidden boolean
);

CREATE TABLE purchase (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    buyer INTEGER,
    price FLOAT,
    version long,
    FOREIGN KEY (buyer) REFERENCES user (id)
);

CREATE TABLE booking (
     id INTEGER AUTO_INCREMENT PRIMARY KEY,
     related_game varchar(50),
     buyer INTEGER ,
     quantity INTEGER,
     version long,
     FOREIGN KEY (related_game) REFERENCES game (name),
     FOREIGN KEY (buyer) REFERENCES user (id)
);

CREATE TABLE purchased_game (
    id INTEGER AUTO_INCREMENT PRIMARY KEY,
    related_purchase INTEGER,
    related_game varchar(50),
    quantity INTEGER,
    price FLOAT,
    by_booking boolean,
    version long,
    FOREIGN KEY (related_purchase) REFERENCES purchase (id),
    FOREIGN KEY (related_game) REFERENCES game (name)
);



CREATE TABLE game_in_cart(
    id INTEGER AUTO_INCREMENT PRIMARY KEY ,
    related_cart INTEGER,
    related_game varchar(50),
    quantity INTEGER,
    version LONG,
    price FLOAT,
    FOREIGN KEY (related_cart) REFERENCES cart(id),
    FOREIGN KEY (related_game) REFERENCES game(name)
);
