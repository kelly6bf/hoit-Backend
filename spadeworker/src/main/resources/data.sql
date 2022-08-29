-- 사용자 권한 테스트 데이터
insert into role (authority, created_at, modified_at) values ('USER', now(), now()) ON DUPLICATE KEY UPDATE `authority`=VALUES(`authority`);
insert into role (authority, created_at, modified_at) values ('ADMIN', now(), now()) ON DUPLICATE KEY UPDATE `authority`=VALUES(`authority`);
insert into role (authority, created_at, modified_at) values ('BOARD_HOST', now(), now()) ON DUPLICATE KEY UPDATE `authority`=VALUES(`authority`);

-- 게시판 카테고리 테스트 데이터
insert into board_category (title, created_at, modified_at) values ('프로그래밍', now(), now()) ON DUPLICATE KEY UPDATE `title`=VALUES(`title`);
insert into board_category (title, created_at, modified_at) values ('취미', now(), now()) ON DUPLICATE KEY UPDATE `title`=VALUES(`title`);
insert into board_category (title, created_at, modified_at) values ('일상', now(), now()) ON DUPLICATE KEY UPDATE `title`=VALUES(`title`);
insert into board_category (title, created_at, modified_at) values ('예능', now(), now()) ON DUPLICATE KEY UPDATE `title`=VALUES(`title`);

-- 사용자 테스트 데이터
insert into user(login_id, password, name, profile_img_url, email, description, provider_type, status, created_at, modified_at)
    values ('testerId', 'testerPw1234', 'tester', 'tester photo', 'tester@email.com', 'This is test account', 'LOCAL', 'ACTIVE', now(), now())
    ON DUPLICATE KEY UPDATE `login_id`=VALUES(`login_id`);

-- 게시판 테스트 데이터
insert into board(title, description, image_url, user_id, board_category_id, created_at, created_by, modified_at, modified_by)
    values ('스프링 게시판', '스!프!링! 시작해볼까요?', 'img_url aaaa', 1, 1, now(), 'tester', now(), 'tester')
    ON DUPLICATE KEY UPDATE `title`=VALUES(`title`);

-- 게시글 카테고리 테스트 데이터
insert into article_category (title, created_at, modified_at) values ('잡담', now(), now()) ON DUPLICATE KEY UPDATE `title`=VALUES(`title`);
insert into article_category (title, created_at, modified_at) values ('질문', now(), now()) ON DUPLICATE KEY UPDATE `title`=VALUES(`title`);
insert into article_category (title, created_at, modified_at) values ('홍보/모집', now(), now()) ON DUPLICATE KEY UPDATE `title`=VALUES(`title`);
insert into article_category (title, created_at, modified_at) values ('공지 사항', now(), now()) ON DUPLICATE KEY UPDATE `title`=VALUES(`title`);
insert into article_category (title, created_at, modified_at) values ('이벤트', now(), now()) ON DUPLICATE KEY UPDATE `title`=VALUES(`title`);

