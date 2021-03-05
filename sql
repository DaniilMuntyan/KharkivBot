DROP SCHEMA public CASCADE;
CREATE SCHEMA public;


DROP TABLE IF EXISTS "admin_choice";
create table "admin_choice"( -- Таблица для публикации новой квартиры
                               choice_id SERIAL,
                               menu_message_id INTEGER,
                               is_rent_flat BOOLEAN,
                               district VARCHAR(50), -- Район (enum)
                               metro VARCHAR(50), -- Станция метро
                               address TEXT, -- Адрес дома
                               rooms SMALLINT, -- Количество комнат (enum). Если 0 - гостинка
                               square REAL, -- Площадь (м2)
                               floor SMALLINT, -- Этаж квартиры
                               all_floors SMALLINT, -- Всего этажей в доме
                               money VARCHAR(50), -- Стоимость. Цена + доп расходы
                               money_range VARCHAR(30), -- Бюджет, в который входит стоимость квартиры в UAH (enum)
                               telegraph VARCHAR(150), -- Ссылка на фотографии
                               info TEXT, -- Краткая дополнительная информация
                               primary key (choice_id)
);

DROP TABLE IF EXISTS "user";
create table "user"(
                       user_id SERIAL,
                       chat_id integer unique,
                       firstname varchar(100),
                       lastname varchar(100),
                       username varchar(100),
                       admin_mode boolean,
                       state smallint,
                       admin_choice INTEGER REFERENCES "admin_choice"(choice_id),
                       created_at timestamp with time zone not null default now(),
                       last_action timestamp with time zone,
                       primary key(user_id)
);
create index on "user"(chat_id);

DROP TABLE IF EXISTS "rental";
create table "rental"(
                         rental_id SERIAL,
                         district VARCHAR(50), -- Район (enum)
                         metro VARCHAR(50), -- Станция метро
                         address TEXT, -- Адрес дома
                         rooms SMALLINT, -- Количество комнат (enum). Если 0 - гостинка
                         square REAL, -- Площадь (м2)
                         floor SMALLINT, -- Этаж квартиры
                         all_floors SMALLINT, -- Всего этажей в доме
                         money VARCHAR(50), -- Стоимость. Цена + доп расходы
                         money_range VARCHAR(30), -- Бюджет, в который входит стоимость квартиры в UAH (enum)
                         telegraph VARCHAR(150), -- Ссылка на фотографии
                         info TEXT, -- Краткая дополнительная информация
                         PRIMARY KEY(rental_id)
);
create index on "rental"(square);
SELECT setval('rental_rental_id_seq', 12345);

DROP TABLE IF EXISTS "buy";
create table "buy"(
                      buy_id SERIAL,
                      district VARCHAR(50), -- Район (enum)
                      metro VARCHAR(50), -- Станция метро
                      address TEXT, -- Адрес дома
                      rooms SMALLINT, -- Количество комнат (enum). Если 0 - гостинка
                      square REAL, -- Площадь (м2)
                      floor SMALLINT, -- Этаж квартиры
                      all_floors SMALLINT, -- Всего этажей в доме
                      money VARCHAR(50), -- Стоимость. Цена + доп расходы
                      money_range VARCHAR(30), -- Бюджет, в который входит стоимость квартиры в USD (enum)
                      telegraph VARCHAR(150), -- Ссылка на фотографии
                      info TEXT, -- Краткая дополнительная информация
                      PRIMARY KEY(buy_id)
);
create index on "buy"(square);

