
CREATE TABLE product (
    id INT NOT NULL AUTO_INCREMENT,
    name VARCHAR(255) NOT NULL,
    price INT,
    creation_date TIMESTAMP,
    status VARCHAR(25),
    deleted INT default 0,
    PRIMARY KEY (id)
   
);

CREATE TABLE approval_queue (
    id INT NOT NULL AUTO_INCREMENT,
    creation_date TIMESTAMP,
    status VARCHAR(25),
    deleted INT default 0,
    product_id INT NOT NULL,
    PRIMARY KEY (id)
);


