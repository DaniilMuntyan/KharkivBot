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
                               map text, -- Точка на карте
                               contact VARCHAR(50), -- Контакт, с кем связаться
                               telegraph text, -- Ссылка на фотографии
                               info TEXT, -- Краткая дополнительная информация
                               primary key (choice_id)
);

DROP TABLE IF EXISTS "user_choice";
create table "user_choice"( -- Таблица для предпочтений пользователя
                              choice_id SERIAL,
                              menu_message_id INTEGER,
                              is_rent_flat BOOLEAN,
                              rooms varchar(10),
                              districts varchar(200),
                              budget varchar(200),
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
                       admin_state smallint,
                       user_state smallint,
                       phone varchar(20),
                       admin_choice INTEGER REFERENCES "admin_choice"(choice_id),
                       user_choice INTEGER REFERENCES "user_choice"(choice_id),
                       wants_updates boolean,
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
                         map TEXT, -- Точка на карте
                         contact VARCHAR(50), -- Контакт, с кем связаться
                         telegraph TEXT, -- Ссылка на фотографии
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
                      map TEXT, -- Точка на карте
                      contact VARCHAR(50), -- Контакт, с кем связаться
                      telegraph TEXT, -- Ссылка на фотографии
                      info TEXT, -- Краткая дополнительная информация
                      PRIMARY KEY(buy_id)
);
create index on "buy"(square);
SELECT setval('buy_buy_id_seq', 12345);

DROP TABLE IF EXISTS "user_choice_rental"; -- Многие ко многим
CREATE TABLE "user_choice_rental"(
                                     user_choice_id INTEGER REFERENCES "user_choice"(choice_id),
                                     flat_id INTEGER REFERENCES "rental"(rental_id),
                                     CONSTRAINT user_choice_rental_pkey PRIMARY KEY (user_choice_id, flat_id)
);

DROP TABLE IF EXISTS "user_choice_buy"; -- Многие ко многим
CREATE TABLE "user_choice_buy"(
                                  user_choice_id INTEGER REFERENCES "user_choice"(choice_id),
                                  flat_id INTEGER REFERENCES "buy"(buy_id),
                                  CONSTRAINT user_choice_buy_pkey PRIMARY KEY (user_choice_id, flat_id)
);











