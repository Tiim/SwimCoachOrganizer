INSERT INTO set_focus (name, abbr)
VALUES
('Sprint', 'Spr'),
('Endurance', 'End'),
('Recovery', 'Rec');

INSERT INTO set_stroke (name, abbr)
VALUES
('Butterfly', 'Fly'),
('Back Stroke', 'Ba'),
('Breast Stroke', 'Br'),
('Freestyle', 'Fr'),
('Individual Medley', 'IM');

INSERT INTO sets (name, content, distance_f1, distance_f2, distance_f3, intensity, focus_id, stroke_id, notes, interval, is_pause)
VALUES
('Crawl Pull/Kick', 'First pull, second kick', 1, 2, 325, 20, 2, 4, NULL, 0, 1);

INSERT INTO swimmer (first_name, last_name, birthday, address, phone_private, phone_work, phone_mobile, email, license, is_female, notes)
VALUES
('John', 'Smith', '1970-12-10', 'On the moon', '0012345678', NULL, NULL,'email@example.com', NULL, FALSE, 'Has asthma');
