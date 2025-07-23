INSERT INTO users (name, username, password, street, city, zipcode, country) VALUES
('Usuario 1', 'user1', '$2a$10$9EeDiFgL1P6kpggirkfgSe64plsFH.MVbtNaQfdIy8Lh0Bbjh2uXW', 'Calle 1', 'Ciudad 1', '10001', 'País 1'),
('Usuario 2', 'user2', '$2a$10$9EeDiFgL1P6kpggirkfgSe64plsFH.MVbtNaQfdIy8Lh0Bbjh2uXW', 'Calle 2', 'Ciudad 2', '10002', 'País 2'),
('Usuario 3', 'user3', '$2a$10$9EeDiFgL1P6kpggirkfgSe64plsFH.MVbtNaQfdIy8Lh0Bbjh2uXW', 'Calle 3', 'Ciudad 3', '10003', 'País 3'),
('Usuario 4', 'user4', '$2a$10$9EeDiFgL1P6kpggirkfgSe64plsFH.MVbtNaQfdIy8Lh0Bbjh2uXW', 'Calle 4', 'Ciudad 4', '10004', 'País 4'),
('Usuario 5', 'user5', '$2a$10$9EeDiFgL1P6kpggirkfgSe64plsFH.MVbtNaQfdIy8Lh0Bbjh2uXW', 'Calle 5', 'Ciudad 5', '10005', 'País 5');


INSERT INTO todos (title, completed, user_id) VALUES
('Tarea 1', false, 1),
('Tarea 2',  false, 2),
('Tarea 3', false, 3),
('Tarea 4', false, 4),
('Tarea 5',  false, 5),
('Tarea 6',  false, 1),
('Tarea 7',false, 2),
('Tarea 8',false, 3),
('Tarea 9',false, 4),
('Tarea 10',false, 5),
('Tarea 11',true, 1),
('Tarea 12',true, 2),
('Tarea 13',true, 3),
('Tarea 14',false, 4),
('Tarea 15',true, 5);
