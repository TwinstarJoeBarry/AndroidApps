-- keep the Nest generated UPC/Brands data (presumably)
-- DROP TABLE IF EXISTS "nestUPCs"
-- DROP TABLE IF EXISTS "nestBrands"
DROP TABLE IF EXISTS "categories";
DROP TABLE IF EXISTS "cookingMethods";
DROP TABLE IF EXISTS "cookingTips";
DROP TABLE IF EXISTS "products";
DROP TABLE IF EXISTS "shelfLifeTypes";
DROP TABLE IF EXISTS "shelfLives";
DROP VIEW IF EXISTS view_upc_product_category_joined;
DROP VIEW IF EXISTS view_shelf_lives_and_type_info_joined;