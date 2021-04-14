CREATE TABLE `to_do` (
         `id`          BIGINT(20) unsigned NOT NULL AUTO_INCREMENT,
         `category_id` BIGINT(20) unsigned NOT NULL,
         `title`       VARCHAR(255)        NOT NULL,
         `body`        TEXT,
         `state`       TINYINT UNSIGNED    NOT NULL,
         `updated_at`  TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
         `created_at`  TIMESTAMP           NOT NULL DEFAULT CURRENT_TIMESTAMP,
         PRIMARY KEY (`id`) ,
         FOREIGN KEY (`category_id`) REFERENCES `to_do_category` (`id`)
) ENGINE=InnoDB DEFAULT CHARSET=utf8mb4;