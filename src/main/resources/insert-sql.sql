

-- Insert age ranges into age_range table
INSERT INTO age_range (age_from, age_to) VALUES
(0, 19),    -- Age range: 0 to 19
(20, 29),   -- Age range: 20 to 29
(30, 39),   -- Age range: 30 to 39
(40, 49),   -- Age range: 40 to 49
(50, 59),   -- Age range: 50 to 59
(60, 69),   -- Age range: 60 to 69
(70, 99);   

-- Insert gender values into gender table
INSERT INTO gender (gender) VALUES
('M'), -- Male
('F'); -- Female

-- Insert zip codes into zip_code table
INSERT INTO zip_code (zip_from, zip_to, gender_id) VALUES
(90000, 91000, 1),
(91000, 99000, 1),
(99000, 99999, 1),
(99999, 100000, 1);

INSERT INTO zip_code (zip_from, zip_to, gender_id) VALUES
(90000, 91000, 2),
(91000, 99000, 2),
(99000, 99999, 2),
(99999, 100000, 2);

-- Insert data into the insurance_rate table
INSERT INTO insurance_rate (zip_code_id, age_range_id, gender_id, rate) VALUES
(1, 1, 1, 100), -- For zip_code_id = 1 (male)
(1, 2, 1, 120),
(1, 3, 1, 120),
(1, 4, 1, 110),
(1, 5, 1, 100),
(1, 6, 1, 90),
(1, 7, 1, 20),

(2, 1, 1, 110), -- For zip_code_id = 2 (male)
(2, 2, 1, 132),
(2, 3, 1, 132),
(2, 4, 1, 121),
(2, 5, 1, 110),
(2, 6, 1, 99),
(2, 7, 1, 22),

(3, 1, 1, 95), -- For zip_code_id = 3 (male)
(3, 2, 1, 114),
(3, 3, 1, 114),
(3, 4, 1, 104),
(3, 5, 1, 95),
(3, 6, 1, 85),
(3, 7, 1, 19),

(4, 1, 1, 100), -- For zip_code_id = 4 (male)
(4, 2, 1, 120),
(4, 3, 1, 120),
(4, 4, 1, 110),
(4, 5, 1, 100),
(4, 6, 1, 90),
(4, 7, 1, 20);

-- For female (gender_id = 2)
INSERT INTO insurance_rate (zip_code_id, age_range_id, gender_id, rate) VALUES
(5, 1, 2, 90),  -- For zip_code_id = 5 (female)
(5, 2, 2, 125),
(5, 3, 2, 125),
(5, 4, 2, 115),
(5, 5, 2, 100),
(5, 6, 2, 90),
(5, 7, 2, 20),

(6, 1, 2, 99),  -- For zip_code_id = 6 (female)
(6, 2, 2, 137),
(6, 3, 2, 137),
(6, 4, 2, 126),
(6, 5, 2, 110),
(6, 6, 2, 99),
(6, 7, 2, 22),

(7, 1, 2, 85),  -- For zip_code_id = 7 (female)
(7, 2, 2, 118),
(7, 3, 2, 118),
(7, 4, 2, 109),
(7, 5, 2, 95),
(7, 6, 2, 85),
(7, 7, 2, 19),

(8, 1, 2, 90),  -- For zip_code_id = 8 (female)
(8, 2, 2, 125),
(8, 3, 2, 125),
(8, 4, 2, 115),
(8, 5, 2, 100),
(8, 6, 2, 90), 
(8, 7, 2, 20);


