/*DROP TABLE IF EXISTS Categories;
CREATE TABLE Categories (
	cno 			BIGINT PRIMARY KEY AUTO_INCREMENT,
	name 		    VARCHAR(255),
	daily_visitors 	INTEGER DEFAULT 0,
	is_subclass 	BOOLEAN DEFAULT FALSE
);*/
INSERT INTO Categories (cno, name) VALUES
(0, '쓰레기통');
INSERT INTO Categories (name) VALUES
('유머게시판'),
('고민게시판'),
('썰게시판'),
('취업게시판'),
('방송게시판'),
('스포츠게시판'),
('게임게시판'),
('대외활동게시판'),
('이슈게시판'),
('IT게시판'),
('음악게시판'),
('패션게시판'),
('경제게시판'),
('독서게시판'),
('유학게시판'),
('운동게시판'),
('여행게시판'),
('서브컬쳐게시판'),
('예술게시판');

select * from CATEGORIES;