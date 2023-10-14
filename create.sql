-- INSERT USERS
INSERT INTO the_day_market.user (id, created_at, updated_at, address_line1, city, post_code, last_logged_in_at, password, token, email, first_name, last_name, profile_image_name, role) VALUES (1, now(), now(), '3 Macoma Road', 'London', 'SE18 2QW', now(), '$2a$10$hMino9WwLwyy0TIATOp9Ne.XItmfZHmnyLBMEB1U6eBnAW/L4flQu', 'eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJzdXJhbmphbnNlbGxlckB0ZXN0LmNvbSIsImlzcyI6InRoZWRheW1hcmtldCIsImlhdCI6MTY5NjYwNjc1NCwiZXhwIjoxNjk2NjEwMzU0fQ.9u9s9o9uwVhKXe8J9nA8bIrut3tSlVohyoCRLSdVWOHcrK-dMOtFEy0ffe5tOD5fVgfLkvh5i2Muodq0J_UIYQ', 'suranjanseller@test.com', 'Suranjan', 'Poudel', '67f983a1_27a7_4fcd_9c28_7161a64395c0_2023_09_30_13_19_18_.jpeg', 'SELLER');
INSERT INTO the_day_market.user (id, created_at, updated_at, address_line1, city, post_code, last_logged_in_at, password, token, email, first_name, last_name, profile_image_name, role) VALUES (2, now(), now(), '46 saldine road', 'London', 'SE2 PK3', now(), '$2a$10$/WbZRnGRVia0Oj0v.fATd.EH3oaE4/u6.xxEA/OypOJXIuAo1LAUS', 'eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJtYXhzZWxsd2VsbEB0ZXN0LmNvbSIsImlzcyI6InRoZWRheW1hcmtldCIsImlhdCI6MTY5NjA3OTUwNSwiZXhwIjoxNjk2MDgzMTA1fQ.EP2BTcv9SUlvFckQAPV9UMc0ZUvi0sDZ3IpraWsx19QVLi1RhPBm6ZGF3DThLb7C2izDPQEqlVO9B3EkjqMx-Q', 'maxsellwell@test.com', 'Max', 'Tiger', '8d158d0a_5586_431e_8bc3_64275190fc6f_2023_09_30_13_51_50_.jpeg', 'SELLER');
INSERT INTO the_day_market.user (id, created_at, updated_at, address_line1, city, post_code, last_logged_in_at, password, token, email, first_name, last_name, profile_image_name, role) VALUES (3, now(), now(), '89 Garland Road', 'Manchester', 'ET6 PE3', now(), '$2a$10$8UislqjGFTEwqdkyhg2g0ORvneKXttFQT1VJur0lAAnGS1SaruFfm', 'eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJqYWNrZXRzaW5AdGVzdC5jb20iLCJpc3MiOiJ0aGVkYXltYXJrZXQiLCJpYXQiOjE2OTYwODAzODIsImV4cCI6MTY5NjA4Mzk4Mn0.pVe-EZz9yzbhSaqoXNQ4zTt_2amBIBOmgTv5FazjCn6Ta6vpJ1rLhGAhL_8LIeuDi9owJVcnJIhLybMoVYnj2A', 'jacketsin@test.com', 'Jack', 'Etsin', 'fffec380_51bd_4350_a65f_8fd4f5f3ce67_2023_09_30_13_58_37_.jpeg', 'SELLER');
INSERT INTO the_day_market.user (id, created_at, updated_at, address_line1, city, post_code, last_logged_in_at, password, token, email, first_name, last_name, profile_image_name, role) VALUES (4, now(), now(), '3 maso cora', 'London', 'SE12 2LO', now(), '$2a$10$Gvsa524F4qSTHvnCKRbFb.6bgCugEanUWnOyy6j7XWEbQZWp8euoO', 'eyJhbGciOiJIUzUxMiIsInR5cCI6IkpXVCJ9.eyJzdWIiOiJoYnV5ZXJAdGVzdC5jb20iLCJpc3MiOiJ0aGVkYXltYXJrZXQiLCJpYXQiOjE2OTcyNzgzNDcsImV4cCI6MTY5NzI4MTk0N30.ktwKbOskwlT-p36aEmXM9b5bAlsIh9pljxvBRCmH5knGe96cJgd2OBx7Gwprf0ky0NrKiYoyzUlpulxXPxHeEg', 'hbuyer@test.com', 'Heavy', 'Buyer', 'ffaed2e1_de68_4e41_9411_46dc74797b5b_2023_10_14_10_24_26_.jpg', 'BUYER');

-- INSERT REPUTATIONS
INSERT INTO the_day_market.reputation (id, created_at, updated_at, reputation_points, user_id) VALUES (1, now(), now(), 20, 1);
INSERT INTO the_day_market.reputation (id, created_at, updated_at, reputation_points, user_id) VALUES (2, now(), now(), 40, 2);
INSERT INTO the_day_market.reputation (id, created_at, updated_at, reputation_points, user_id) VALUES (3, now(), now(), 90, 3);

-- INSERT POINTS
INSERT INTO the_day_market.user_points (id, created_at, updated_at, count, belongs_to_id) VALUES (1, now(), now(), 1000.00, 1);
INSERT INTO the_day_market.user_points (id, created_at, updated_at, count, belongs_to_id) VALUES (2, now(), now(), 1000.00, 2);
INSERT INTO the_day_market.user_points (id, created_at, updated_at, count, belongs_to_id) VALUES (3, now(), now(), 1000.00, 3);

-- INSERT CATEGORIES
INSERT INTO the_day_market.category (id, created_at, updated_at, tag, parent_id) VALUES (1, now(), now(), 'shoes', null);
INSERT INTO the_day_market.category (id, created_at, updated_at, tag, parent_id) VALUES (2, now(), now(), 'jacket', null);
INSERT INTO the_day_market.category (id, created_at, updated_at, tag, parent_id) VALUES (3, now(), now(), 'watches', null);
INSERT INTO the_day_market.category (id, created_at, updated_at, tag, parent_id) VALUES (4, now(), now(), 'bags', null);

-- INSERT AUCTIONS
INSERT INTO the_day_market.auction (id, created_at, updated_at, decrement_factor, decrement_seconds, description, image_name, item_count, min_ask_price, scheduled_date, status, title, type, assigned_market_id, category_id, seller_id, decrement_limit) VALUES (2, now(), now(), null, null, 'Sleek \'Better and Gray\' shoes: a blend of style & sophistication. Perfect for any occasion with a timeless hue & premium comfort. Elevate your look with modern elegance.', '6a7f8850_dbee_4b0e_9c8b_548a814a54a3_2023_09_30_13_29_09_.jpg', 1, 100.00, DATE(now()), 'PUBLISHED', 'Better and Gray', 'English', null, 1, 1, null);
INSERT INTO the_day_market.auction (id, created_at, updated_at, decrement_factor, decrement_seconds, description, image_name, item_count, min_ask_price, scheduled_date, status, title, type, assigned_market_id, category_id, seller_id, decrement_limit) VALUES (3, now(), now(), null, null, 'Introducing \'Leather Weather\' shoes: unparalleled elegance meets durability. Crafted with premium leather, they promise both style & resilience. Step into any season with confidence.', '6384830f_ce63_47d2_814b_211461fdeedb_2023_09_30_13_34_26_.jpg', 1, 145.00, DATE(now()), 'DRAFT', 'Leather weather', 'Sealed', null, 1, 1, null);
INSERT INTO the_day_market.auction (id, created_at, updated_at, decrement_factor, decrement_seconds, description, image_name, item_count, min_ask_price, scheduled_date, status, title, type, assigned_market_id, category_id, seller_id, decrement_limit) VALUES (4, now(), now(), 2, 5, 'Discover \'Adi Dash\' shoes: a fusion of dynamic design and everyday comfort. Crafted for those on the move, these sneakers offer style without compromising performance. Stand out in every stride.', 'b930ca83_96ae_4275_be85_46a7d75aebe9_2023_09_30_13_36_30_.jpg', 1, 300.00, DATE(now()), 'PUBLISHED', 'Adi Dash', 'Dutch', null, 1, 1, 150.00);
INSERT INTO the_day_market.auction (id, created_at, updated_at, decrement_factor, decrement_seconds, description, image_name, item_count, min_ask_price, scheduled_date, status, title, type, assigned_market_id, category_id, seller_id, decrement_limit) VALUES (5, now(), now(), null, null, 'Step into the spotlight with \'Tight and White\' footwear: where sleek design meets pristine elegance. Perfectly contoured for a snug fit, these shoes radiate modern sophistication in every detail.', 'c201ce46_64a9_4047_bcd5_dfba870a1381_2023_09_30_13_50_20_.jpg', 1, 45.00, DATE(now()), 'PUBLISHED', 'Tight and White', 'English', null, 1, 1, null);
INSERT INTO the_day_market.auction (id, created_at, updated_at, decrement_factor, decrement_seconds, description, image_name, item_count, min_ask_price, scheduled_date, status, title, type, assigned_market_id, category_id, seller_id, decrement_limit) VALUES (6, now(), now(), null, null, 'Introducing \'Better Blue\' shoes: Dive into deep elegance and comfort. With a hue that captures attention and craftsmanship that ensures durability, stride confidently into any occasion.', '0b5162b3_e890_4b0e_adc3_735e356bfb30_2023_09_30_13_52_42_.jpg', 1, 135.00, DATE(now()), 'PUBLISHED', 'Better Blue', 'English', null, 1, 2, null);
INSERT INTO the_day_market.auction (id, created_at, updated_at, decrement_factor, decrement_seconds, description, image_name, item_count, min_ask_price, scheduled_date, status, title, type, assigned_market_id, category_id, seller_id, decrement_limit) VALUES (7, now(), now(), 5, 3600, 'Dive into timeless style with Lacoste footwear: where iconic design meets modern comfort. Renowned for its crocodile emblem, these shoes offer a blend of casual sophistication and premium quality.', 'c9884956_e587_4093_9f58_42df3a633dc5_2023_09_30_13_53_32_.jpg', 1, 500.00, DATE(now()), 'PUBLISHED', 'Lacotse', 'Dutch', null, 1, 2, 250.00);
INSERT INTO the_day_market.auction (id, created_at, updated_at, decrement_factor, decrement_seconds, description, image_name, item_count, min_ask_price, scheduled_date, status, title, type, assigned_market_id, category_id, seller_id, decrement_limit) VALUES (8, now(), now(), null, null, 'Ladie\'s Way heels: Elevate your elegance. Femininely crafted for discerning tastes, these heels promise poise & grace with every step. A true style statement.', 'f216692e_37d1_409c_a656_36092a550e07_2023_09_30_13_54_55_.jpg', 1, 250.00, DATE(now()), 'PUBLISHED', 'Ladie\'s Way', 'English', null, 1, 2, null);
INSERT INTO the_day_market.auction (id, created_at, updated_at, decrement_factor, decrement_seconds, description, image_name, item_count, min_ask_price, scheduled_date, status, title, type, assigned_market_id, category_id, seller_id, decrement_limit) VALUES (9, now(), now(), null, null, 'Black Belly shoes: A blend of classic allure and contemporary style. These versatile flats exude sophistication, making them a must-have for any wardrobe. Perfect for day-to-night elegance.', '24bd7c06_c66a_4fb5_96ed_05ee002b4087_2023_09_30_13_56_23_.jpg', 1, 246.80, DATE(now()), 'PUBLISHED', 'Black belly', 'Sealed', null, 1, 2, null);
INSERT INTO the_day_market.auction (id, created_at, updated_at, decrement_factor, decrement_seconds, description, image_name, item_count, min_ask_price, scheduled_date, status, title, type, assigned_market_id, category_id, seller_id, decrement_limit) VALUES (10, now(), now(), 10, 3600, 'Block Weather Jacket: Where functionality meets style. Crafted for protection against the elements while ensuring a sharp look. Your go-to for unpredictable weather, wrapped in sleek design.', '0f48ff07_c804_4318_a384_296bbeb94eb7_2023_09_30_14_00_16_.jpg', 1, 300.00, DATE(now()), 'PUBLISHED', 'Block Weather ', 'Dutch', null, 2, 3, 150.00);
INSERT INTO the_day_market.auction (id, created_at, updated_at, decrement_factor, decrement_seconds, description, image_name, item_count, min_ask_price, scheduled_date, status, title, type, assigned_market_id, category_id, seller_id, decrement_limit) VALUES (11, now(), now(), null, null, 'Red Mountaineer Jacket: Adventure with elegance. Crafted for rugged terrains, offering warmth and style. Stand out as you scale peaks.', 'e3e9d4a2_4528_437d_a9f0_2a406b65eb64_2023_09_30_14_02_25_.jpg', 1, 250.00, DATE(now()), 'PUBLISHED', 'Red Mountaineer', 'English', null, 2, 3, null);
INSERT INTO the_day_market.auction (id, created_at, updated_at, decrement_factor, decrement_seconds, description, image_name, item_count, min_ask_price, scheduled_date, status, title, type, assigned_market_id, category_id, seller_id, decrement_limit) VALUES (12, now(), now(), null, null, 'Hill Figure Jacket: Classic meets contemporary. Embodying warmth and style, this jacket is tailored for both urban adventures and countryside escapes. Make a statement, come rain or shine.', 'f420a728_de78_4af5_899d_8c9bb810d647_2023_09_30_14_03_37_.jpg', 1, 268.00, DATE(now()), 'PUBLISHED', 'Hill Figure', 'English', null, 2, 3, null);
INSERT INTO the_day_market.auction (id, created_at, updated_at, decrement_factor, decrement_seconds, description, image_name, item_count, min_ask_price, scheduled_date, status, title, type, assigned_market_id, category_id, seller_id, decrement_limit) VALUES (13, now(), now(), null, null, 'Rain Blocker: Defend against downpours. Expertly designed to keep you dry and comfortable, this essential offers unmatched protection without sacrificing style. Brave the storm with confidence.', '758cc184_7e4e_43e3_bc25_e1b812f99ecd_2023_09_30_14_06_36_.jpg', 1, 312.65, DATE(now()), 'PUBLISHED', 'Rain blocker', 'English', null, 2, 3, null);
INSERT INTO the_day_market.auction (id, created_at, updated_at, decrement_factor, decrement_seconds, description, image_name, item_count, min_ask_price, scheduled_date, status, title, type, assigned_market_id, category_id, seller_id, decrement_limit) VALUES (14, now(), now(), null, null, 'Browneather Quartz Watch: A union of luxury and precision. With a rich leather strap and accurate quartz movement, it\'s elegance for the modern era.', '89b2125c_24a4_4847_be88_410d14127a41_2023_09_30_14_09_26_.jpg', 1, 237.00, DATE(now()), 'PUBLISHED', 'Browneather Quartz', 'English', null, 3, 1, null);
INSERT INTO the_day_market.auction (id, created_at, updated_at, decrement_factor, decrement_seconds, description, image_name, item_count, min_ask_price, scheduled_date, status, title, type, assigned_market_id, category_id, seller_id, decrement_limit) VALUES (15, now(), now(), null, null, 'Digital Divine Watch: Unveil modern elegance. This timepiece merges technology and style flawlessly. Experience the future on your wrist with timeless sophistication.', '32d3c374_92c2_4a5a_b77f_37699b09ff01_2023_09_30_14_10_49_.jpg', 1, 485.00, DATE(now()), 'PUBLISHED', 'Digital Divine', 'English', null, 3, 1, null);
INSERT INTO the_day_market.auction (id, created_at, updated_at, decrement_factor, decrement_seconds, description, image_name, item_count, min_ask_price, scheduled_date, status, title, type, assigned_market_id, category_id, seller_id, decrement_limit) VALUES (16, now(), now(), null, null, 'Silver Blue Precision Watch: Where metallic sheen meets azure elegance. A testament to fine craftsmanship and accuracy. Elevate every moment with its radiant charm.', '91b2f97f_4e9d_42df_9439_b9434595197c_2023_09_30_14_12_50_.jpg', 1, 760.00, DATE(now()), 'PUBLISHED', 'Silver Blue Precision', 'English', null, 3, 2, null);
INSERT INTO the_day_market.auction (id, created_at, updated_at, decrement_factor, decrement_seconds, description, image_name, item_count, min_ask_price, scheduled_date, status, title, type, assigned_market_id, category_id, seller_id, decrement_limit) VALUES (17, now(), now(), null, null, 'Fossil Quartz: Timeless design meets modern precision. A symbol of elegance and reliability, crafted for the discerning wrist.', 'ed751ba0_c99c_41e2_9459_dc512dd21446_2023_09_30_14_14_12_.jpg', 1, 1023.00, DATE(now()), 'PUBLISHED', 'Fossil Quarz', 'English', null, 3, 2, null);
INSERT INTO the_day_market.auction (id, created_at, updated_at, decrement_factor, decrement_seconds, description, image_name, item_count, min_ask_price, scheduled_date, status, title, type, assigned_market_id, category_id, seller_id, decrement_limit) VALUES (18, now(), now(), null, null, 'Smart Today Smartwatch: Embrace the future today. Combining cutting-edge tech with sleek design, it\'s not just a watch—it\'s a lifestyle companion. Stay connected, stay ahead.', '9bcf49b9_90fc_418b_8509_4b3ce65320c9_2023_09_30_14_15_30_.jpg', 1, 360.00, DATE(now()), 'PUBLISHED', 'Smart Today', 'English', null, 3, 2, null);
INSERT INTO the_day_market.auction (id, created_at, updated_at, decrement_factor, decrement_seconds, description, image_name, item_count, min_ask_price, scheduled_date, status, title, type, assigned_market_id, category_id, seller_id, decrement_limit) VALUES (19, now(), now(), null, null, 'Magical Carry Bag: Beyond just storage. Merging style with utility, this bag transforms any outfit and occasion. A fusion of fashion and function.', '579adacc_d6de_4d28_adfa_3e3f07534bbc_2023_09_30_14_17_17_.jpg', 1, 320.00, DATE(now()), 'PUBLISHED', 'Magical Carry', 'English', null, 4, 3, null);
INSERT INTO the_day_market.auction (id, created_at, updated_at, decrement_factor, decrement_seconds, description, image_name, item_count, min_ask_price, scheduled_date, status, title, type, assigned_market_id, category_id, seller_id, decrement_limit) VALUES (20, now(), now(), null, null, 'So School Bag: Learn in style. Crafted for the modern student, this bag balances functionality with flair. Make every school day a fashion statement.', '3ce62ae6_f26e_4aed_87f0_3715ae205528_2023_09_30_14_18_13_.jpg', 1, 150.00, DATE(now()), 'PUBLISHED', 'So school', 'English', null, 4, 3, null);
INSERT INTO the_day_market.auction (id, created_at, updated_at, decrement_factor, decrement_seconds, description, image_name, item_count, min_ask_price, scheduled_date, status, title, type, assigned_market_id, category_id, seller_id, decrement_limit) VALUES (21, now(), now(), null, null, 'Luxury Lather: Indulgence in every touch. Crafted from premium materials, this product epitomizes sophistication. Experience opulence in its purest form.', '2d8e4f57_74ad_407f_9b18_c428f04ced02_2023_09_30_14_18_58_.jpg', 1, 570.00, DATE(now()), 'PUBLISHED', 'Luxury Lather', 'Sealed', null, 4, 3, null);
INSERT INTO the_day_market.auction (id, created_at, updated_at, decrement_factor, decrement_seconds, description, image_name, item_count, min_ask_price, scheduled_date, status, title, type, assigned_market_id, category_id, seller_id, decrement_limit) VALUES (22, now(), now(), 15, 3600, 'Lady\'s Delight Bag: Elegance redefined. Designed for the modern woman, it\'s the perfect blend of style and function. Turn heads with this timeless accessory.', '2e80d1ce_294a_4f25_87de_7e4597ea7d0a_2023_09_30_14_20_08_.jpg', 1, 1500.00, DATE(now()), 'PUBLISHED', 'Lady\'s Delight', 'Dutch', null, 4, 3, 800.00);
INSERT INTO the_day_market.auction (id, created_at, updated_at, decrement_factor, decrement_seconds, description, image_name, item_count, min_ask_price, scheduled_date, status, title, type, assigned_market_id, category_id, seller_id, decrement_limit) VALUES (23, now(), now(), null, null, 'Redish Red Bag: A bold statement of vibrancy. Dive into the allure of its deep hue, perfectly blending fashion and function. Stand out with every carry.', '7c8ac3d7_a1b8_403a_a3cc_9389eead858a_2023_09_30_14_21_07_.jpg', 1, 600.00, DATE(now()), 'PUBLISHED', 'Redish Red', 'English', null, 4, 3, null);
