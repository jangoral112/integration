--this script initiates db for h2 db (used in test profile)
insert into user (id, account_status, email, first_name, last_name) values (1, 'CONFIRMED', 'john@domain.com', 'John', 'Steward')
insert into user (id, account_status, email, first_name) values (2, 'NEW', 'brian@domain.com', 'Brian')

insert into user (id, account_status, email, first_name, last_name) values (3, 'CONFIRMED', 'owner@domain.com', 'Paul', 'BlogPostOwner')
insert into user (id, account_status, email, first_name, last_name) values (4, 'CONFIRMED', 'like@domain.com', 'Anna', 'LikedBlogPost')
insert into blog_post (id, entry, user_id) values (1, 'sample entry', 3)
insert into like_post (id, post_id, user_id) values (1, 1, 4)