-- USERS
INSERT INTO users (id, first_name, last_name, username, password, is_active)
VALUES (1, 'Ali', 'Veli', 'ali.veli', '1234', true),
       (2, 'Ayşe', 'Yılmaz', 'ayse.yilmaz', 'abcd', true),
       (3, 'Mehmet', 'Demir', 'mehmet.demir', 'pass', true);

-- TRAINING_TYPE
INSERT INTO training_types (id, training_type_name)
VALUES (1, 'Cardio'),
       (2, 'Strength'),
       (3, 'Yoga');

-- TRAINEE
INSERT INTO trainees (id, user_id, date_of_birth, address)
VALUES (1, 1, '2000-01-01', 'Istanbul'),
       (2, 3, '1995-05-10', 'Ankara');

-- TRAINER
INSERT INTO trainers (id, user_id, specialization)
VALUES (1, 2, 'Fitness');

-- TRAINING
INSERT INTO trainings (id, trainee_id, trainer_id, training_name, training_type_id, training_date, training_duration)
VALUES (1, 1, 1, 'Morning Cardio', 1, '2025-07-22 09:00:00', 60),
       (2, 2, 1, 'Strength Basics', 2, '2025-07-22 10:00:00', 45);

-- TRAINEE_TRAINER (Many-to-Many JOIN TABLE)
INSERT INTO trainee_trainer (trainee_id, trainer_id)
VALUES (1, 1),
       (2, 1);