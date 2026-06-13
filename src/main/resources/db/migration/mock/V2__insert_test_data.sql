CREATE EXTENSION IF NOT EXISTS pgcrypto;

INSERT INTO users (id, login, phone_number, email, password, role) VALUES
	('04070c17-74da-4074-892b-d9c4dedab9cf', 'login0', '81111111111', 'email1@e', crypt('password0', gen_salt('bf')), 'ADMIN'),
	('3e5f1ff2-7c6f-47ec-9aac-62d0f328b4bd', 'login1', '82222222222', 'email2@e', crypt('password1', gen_salt('bf')), 'USER'),
	('e4a507da-7b8b-4f4b-9957-9d592d474621', 'login2', '83333333333', 'email3@e', crypt('password2', gen_salt('bf')), 'USER'),
	('09db59da-56eb-480d-8886-88642450fc98', 'login3', '84444444444', 'email4@e', crypt('password3', gen_salt('bf')), 'USER'),
	('bc146acb-084c-472b-b3a3-f58ba2677c05', 'login4', '85555555555', null, crypt('password4', gen_salt('bf')), 'USER'),
	('edec74be-5539-4d72-9537-03acf9e1c866', 'login5', '86666666666', null, crypt('password5', gen_salt('bf')), 'USER'),
	('499af41b-fddb-4970-868b-201c2b5776bb', 'login6', null, 'email5@e', crypt('password6', gen_salt('bf')), 'USER'),
	('a92b31ca-8087-48b9-bd9d-a4c6e730f529', 'login7', null, 'email6@e', crypt('password7', gen_salt('bf')), 'USER'),
	('9400ca07-e888-488c-878b-c56743dc646e', 'login8', null, null, crypt('password8', gen_salt('bf')), 'USER'),
	('af402408-fbdd-460d-8819-866cccda6a22', 'login9', null, null, crypt('password9', gen_salt('bf')), 'USER');
