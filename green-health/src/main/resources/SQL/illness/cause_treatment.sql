ALTER TABLE `greenhealth`.`illness_locale` 
ADD COLUMN `cause` TEXT NOT NULL AFTER `symptoms`,
ADD COLUMN `treatment` TEXT NOT NULL AFTER `cause`;

ALTER TABLE `greenhealth`.`illness` 
ADD COLUMN `cause` TEXT NOT NULL AFTER `symptoms`,
ADD COLUMN `treatment` TEXT NOT NULL AFTER `cause`;