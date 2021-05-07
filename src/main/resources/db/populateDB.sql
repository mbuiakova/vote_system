DELETE FROM menu_history;
DELETE FROM votes;
DELETE FROM users;
DELETE FROM restaurants;
ALTER SEQUENCE global_seq RESTART WITH 1;

INSERT INTO users (name, email, password, is_admin)
VALUES ('User Userovich', 'user@domain.com', '123456', false),
       ('Admin Adminovich', 'admin@domain.com', '654321', true);

INSERT INTO restaurants (NAME)
VALUES ('Dominos'), ('TrackFood'), ('PapaPizza'),('Dutch Food');

INSERT INTO menu_history (restaurant_id, date, menu)
VALUES (3, '2021-04-20', 'item, item2, item3, drink, dessert'),
       (4, '2021-04-20', 'pizza, pizza, pizza, cola, sugar'),
       (4, '2021-04-21', 'bread, salad, cheese, cola'),
       (6, '2021-04-20', 'Stamppot, Hutspot, Hachee, Huzarensalade');

INSERT INTO votes (RESTAURANT_ID, DATE, USER_ID)
VALUES (3, '2021-04-20', 1),
       (4, '2021-04-20', 2);