-- Notes:
-- Do not include any extraneous semicolons in this file. Use them
-- only at the end of a single statement to be processed.
-- ------------------------------------------------------------------
-- All tables are given an "_id" primary key field at the end so that
-- Android's "CursorAdapter" class will function properly. These fields
-- are defined as "INTEGER PRIMARY KEY", which makes them equivalent
-- to the SQLite internal ROWID. Further, these fields are defined
-- without the AUTOINCREMENT keyword so as to avoid the unneeded extra
-- overhead (see sqlite.org/autoinc.html).  We only use the "_id" fields
-- for the CursorAdapter requirement, so their contents are of no
-- consequence to us, even if some ROWID values from deleted rows
-- wind up being reused.
-- ------------------------------------------------------------------
-- Most of the objects created in this script are presumed to not exist
-- when this script is run (on create or on upgrade), with the exceptions
-- being the nestUPCs and nestBrands tables. These are expected to be
-- built up by Nest volunteers and, presumably, we want to keep that
-- data.  Someday, hopefully, those tables will be housed in a separate
-- database on a network server.

CREATE TABLE "categories" (
	"id"	        INTEGER NOT NULL UNIQUE,
	"name"	        TEXT NOT NULL,
	"subcategory"   TEXT,
	"description"   TEXT NOT NULL,  -- concat like "name (subcategory)" else just "name"
	"_id"	        INTEGER PRIMARY KEY
);

CREATE TABLE "cookingMethods" (
	"id"	INTEGER NOT NULL UNIQUE,
	"productId"	INTEGER NOT NULL,
	"method"	TEXT,
	"measureFrom"	TEXT,
	"measureTo"	TEXT,
	"sizeMetric"	TEXT,
	"cookingTemp"	TEXT,
	"timingFrom"	TEXT,
	"timingTo"	TEXT,
	"timingMetric"	TEXT,
	"timingPer"	TEXT,
	"_id"	INTEGER PRIMARY KEY
);

CREATE TABLE "cookingTips" (
	"id"	INTEGER NOT NULL UNIQUE,
	"productId"	INTEGER NOT NULL,
	"tips"	TEXT,
	"safeMinTemp"	INTEGER,
	"restTime"	INTEGER,
	"restTimeMetric"	TEXT,
	"_id"	INTEGER PRIMARY KEY
);

-- shelf life fields have been left out, using shelfLives table instead
CREATE TABLE "products" (
	"id"	INTEGER NOT NULL UNIQUE,
	"categoryId"	INTEGER NOT NULL,
	"name"	TEXT NOT NULL,
	"subtitle"	TEXT,
	"keywords"	TEXT,
--	"pl_min"	INTEGER,
--	"pl_max"	INTEGER,
--	"pl_metric"	TEXT,
--	"pl_tips"	TEXT,
--	"paol_min"	INTEGER,
--	"paol_max"	INTEGER,
--	"paol_metric"	TEXT,
--	"paol_tips"	TEXT,
--	"rl_min"	INTEGER,
--	"rl_max"	INTEGER,
--	"rl_metric"	TEXT,
--	"rl_tips"	TEXT,
--	"raol_min"	INTEGER,
--	"raol_max"	INTEGER,
--	"raol_metric"	TEXT,
--	"raol_tips"	TEXT,
--	"ratl_min"	INTEGER,
--	"ratl_max"	INTEGER,
--	"ratl_metric"	TEXT,
--	"ratl_tips"	TEXT,
--	"fl_min"	INTEGER,
--	"fl_max"	INTEGER,
--	"fl_metric"	TEXT,
--	"fl_tips"	TEXT,
--	"dop_pl_min"	INTEGER,
--	"dop_pl_max"	INTEGER,
--	"dop_pl_metric"	TEXT,
--	"dop_pl_tips"	TEXT,
--	"dop_rl_min"	INTEGER,
--	"dop_rl_max"	INTEGER,
--	"dop_rl_metric"	TEXT,
--	"dop_rl_tips"	TEXT,
--	"dop_fl_min"	INTEGER,
--	"dop_fl_max"	INTEGER,
--	"dop_fl_metric"	TEXT,
--	"dop_fl_tips"	TEXT,
	"_id"	INTEGER PRIMARY KEY
);

-- Idea for shelfLives is, instead of having them in the products table,
-- where most of the possible ones might not be applicable, it would be
-- better to have just the applicable ones stored here per productId.
-- It requires a shelf life type code and/or number, though, as in the
-- ShelfLives.java in the project.  This has to be translated in code
-- for creating display info (e.g. "RL" = "if refrigerated"). The shelfLifeTypes
-- table has a default description for that purpose included.
CREATE TABLE "shelfLives" (
	"productId"	INTEGER NOT NULL,
	"typeCode"	TEXT NOT NULL,		        -- up to 6 char code "RL", "DOP_FL", etc. see ShelfLife.java
	"typeIndex"	INTEGER NOT NULL,	        -- matching index/enum value of code, see ShelfLife.java
	"min"		INTEGER,
	"max"		INTEGER,
	"metric"	TEXT,
	"tips"		TEXT,
	"_id"		INTEGER PRIMARY KEY
);

CREATE TABLE "shelfLifeTypes" (
	"code"		    TEXT NOT NULL UNIQUE,		-- up to 6 char code "RL", "DOP_FL", etc. see ShelfLife.java
	"index"		    INTEGER NOT NULL UNIQUE,	-- matching index/enum value of code, see ShelfLife.java
    "json"          TEXT NOT NULL UNIQUE,       -- matching FoodKeeper API json object label in products output
	"description"   TEXT NOT NULL,
	"_id"		    INTEGER PRIMARY KEY
);

-- codes and indexes match ShelfLife.java
INSERT INTO "shelfLifeTypes" ("code", "index", "json", "description") VALUES
 ('PL'	  , 0, 'pantryLife',                    'When stored in pantry'),
 ('PAOL'  , 1, 'pantryAfterOpeningLife',        'If pantry stored after opening'),
 ('RL'	  , 2, 'refrigeratorLife',              'When stored in refrigerator'),
 ('RAOL'  , 3, 'refrigerateAfterOpeningLife',   'If refrigerated after opening'),
 ('RATL'  , 4, 'refrigerateAfterThawingLife',   'If refrigerated after thawing'),
 ('FL'	  , 5, 'freezerLife',                   'If stored frozen'),
 ('DOP_PL', 6, 'dop_pantryLife',                'When stored in pantry from date of purchase'),
 ('DOP_RL', 7, 'dop_refrigeratorLife',          'When stored in refrigerator from date of purchase'),
 ('DOP_FL', 8, 'dop_freezerLife',               'If stored frozen from date of purchase');

CREATE TABLE IF NOT EXISTS "nestUPCs" (
	"UPC"		    TEXT NOT NULL UNIQUE,		-- allows for non-UPC codes like "pb" for peanut butter in general
	"productId"	    INTEGER NOT NULL,			-- matching FoodKeeper product id (chosen by volunteer)
	"brand"		    TEXT NOT NULL,				-- e.g. “Campbell’s” OR "Any" (or make field brandId and use nestBrands table to normalize)
	"description"   TEXT NOT NULL,				-- e.g. “Chicken Noodle Soup”, OR just "Peanut Butter (jar)"
	"_id"		    INTEGER PRIMARY KEY
);

CREATE TABLE IF NOT EXISTS "nestBrands" (
	"id"		INTEGER NOT NULL UNIQUE,
	"name"		TEXT NOT NULL UNIQUE,		-- e.g. “Campbell’s” OR "Any"
	"_id"		INTEGER PRIMARY KEY
);

-- views for joined queries (avoids android gotchas re: joins)
-- view_upc_product_category_joined
-- called with a where clause on upc
CREATE VIEW view_upc_product_category_joined as
SELECT 		nestUPCs.UPC, nestUPCs.brand, nestUPCs.description,
			nestUPCs.productId, products.name, products.subtitle,
			products.categoryId, categories.description as cat_desc
FROM 		nestUPCs
LEFT JOIN	products on nestUPCs.productId = products.id
LEFT JOIN 	categories on  products.categoryId = categories.id;

-- view_shelf_lives_and_type_info_joined
-- called with a where clause on productId
CREATE VIEW view_shelf_lives_and_type_info_joined AS
SELECT		shelfLives.productId, shelfLives.typeCode, shelfLives.typeIndex,
			shelfLifeTypes.description,
			shelfLives.min, shelfLives.max, shelfLives.metric, shelfLives.tips
FROM 		shelfLives
LEFT JOIN 	shelfLifeTypes ON shelfLives.typeCode = shelfLifeTypes.code;
