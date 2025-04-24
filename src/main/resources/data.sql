--INSERT INTO clients (email, phone, first_name, last_name, middle_name, status) VALUES
--('test1@ya.ru', '89241234567', 'Иван', 'Иванов', 'Иванович', 'CREATED'),
--('test2@ya.ru', '89240000000', 'Петр', 'Иванов', 'Петрович', 'CREATED');
--
--INSERT INTO laboratories (name, description, status) VALUES
--('climate', 'climate tests', 'CREATED'),
--('vibration', 'vibration testing', 'CREATED');
--
--INSERT INTO equipments (name, type, status) VALUES
--('spectrum analyzer', 'RADIOENG', 'CREATED'),
--('pulse generator', 'RADIOENG', 'CREATED'),
--('digital ECG system', 'MEDICAL', 'CREATED'),
--('defibrillator', 'MEDICAL', 'CREATED');
--
--INSERT INTO exams (name, laboratory_id, status) VALUES
--('calibration', 1, 'CREATED'),
--('percussion', 1, 'CREATED'),
--('tightness', 2, 'CREATED');

--INSERT INTO employees (email, first_name, last_name, middle_name, post, laboratory_id, status) VALUES
--('exam-center-e1@ya.ru', 'Ирина', 'Олеговна', 'Катина', 'LAB_TECH', 1, 'CREATED'),
--('exam-center-e2@ya.ru', 'Светлана', 'Игоревна', 'Мишина', 'LAB_ASSISTANT', 2, 'CREATED');

--INSERT INTO orders (client_id, status) VALUES
--(1, 'CREATED');
--
--INSERT INTO equip_exams2 (equipment_id, exam_id, availability) VALUES
--(1, 1, 'AVAILABLE'),
--(1, 2, 'UNAVAILABLE_TEMPORARILY'),
--(4, 3, 'UNAVAILABLE_TEMPORARILY'),
--(5, 19, 'UNAVAILABLE_TEMPORARILY');

--INSERT INTO order_items (order_id, equipment_id, exam_id, quantity) VALUES
--(1, 5, 2, 11),
--(2, 4, 3, 10);

--INSERT INTO tasks (order_item_id, employee_id, status) VALUES
--(5, 5, 'CREATED'),
--(6, 5, 'CREATED');