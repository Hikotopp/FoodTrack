ALTER TABLE menu_items
    ADD COLUMN stock_quantity INT NOT NULL DEFAULT 100 AFTER price;

UPDATE menu_items
SET stock_quantity = 100
WHERE stock_quantity IS NULL OR stock_quantity = 0;
