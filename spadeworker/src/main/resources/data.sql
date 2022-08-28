insert into role (authority, created_at, modified_at) values ('USER', now(), now()) ON DUPLICATE KEY UPDATE `authority`=VALUES(`authority`);
insert into role (authority, created_at, modified_at) values ('ADMIN', now(), now()) ON DUPLICATE KEY UPDATE `authority`=VALUES(`authority`);
insert into role (authority, created_at, modified_at) values ('BOARD_HOST', now(), now()) ON DUPLICATE KEY UPDATE `authority`=VALUES(`authority`);

insert into board_category (name, created_at, modified_at) values ('프로그래밍', now(), now()) ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);
insert into board_category (name, created_at, modified_at) values ('취미', now(), now()) ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);
insert into board_category (name, created_at, modified_at) values ('일상', now(), now()) ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);
insert into board_category (name, created_at, modified_at) values ('예능', now(), now()) ON DUPLICATE KEY UPDATE `name`=VALUES(`name`);

insert into user(login_id, password, name, profile_img_url, email, description, provider_type, status, created_at, modified_at)
    values ('testerId', 'testerPw1234', 'tester', 'tester photo', 'tester@email.com', 'This is test account', 'LOCAL', 'ACTIVE', now(), now())
    ON DUPLICATE KEY UPDATE `login_id`=VALUES(`login_id`);

insert into board(title, description, image_url, user_id, board_category_id, created_at, created_by, modified_at, modified_by)
    values ('스프링 게시판', '스!프!링! 시작해볼까요?', 'img_url aaaa', 1, 1, now(), 'tester', now(), 'tester')
    ON DUPLICATE KEY UPDATE `title`=VALUES(`title`);
