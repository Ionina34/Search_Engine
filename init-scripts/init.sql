-- Создание таблицы sites
CREATE TABLE sites (
    id INT AUTO_INCREMENT PRIMARY KEY,
    status ENUM('INDEXING', 'INDEXED', 'FAILED') NOT NULL,
    status_time DATETIME NOT NULL,
    last_error TEXT,
    url VARCHAR(255) NOT NULL,
    name VARCHAR(255) NOT NULL
);

-- Создание таблицы pages
CREATE TABLE pages (
    id INT AUTO_INCREMENT PRIMARY KEY,
    site_id INT NOT NULL,
    path TEXT NOT NULL,
    code INT NOT NULL,
    content MEDIUMTEXT NOT NULL,
    FOREIGN KEY (site_id) REFERENCES sites(id)
);

-- Создание таблицы lemmas
CREATE TABLE lemmas (
    id INT AUTO_INCREMENT PRIMARY KEY,
    site_id INT NOT NULL,
    lemma VARCHAR(255) NOT NULL,
    frequency INT NOT NULL,
    FOREIGN KEY (site_id) REFERENCES sites(id)
);

-- Создание таблицы indexes
CREATE TABLE indexes (
    id INT AUTO_INCREMENT PRIMARY KEY,
    page_id INT NOT NULL,
    lemma_id INT NOT NULL,
    `rank` FLOAT NOT NULL,
    FOREIGN KEY (page_id) REFERENCES pages(id),
    FOREIGN KEY (lemma_id) REFERENCES lemmas(id)
);

GRANT ALL PRIVILEGES ON test_db.* TO 'test_user'@'%';
FLUSH PRIVILEGES;

