--CURD 게시판
SELECT * FROM USER_TABLES;
CREATE TABLE board(
    NO NUMBER,
    NAME VARCHAR2(34) CONSTRAINT board_name_nn NOT NULL,
    SUBJECT VARCHAR2(1000) CONSTRAINT board_subject_nn NOT NULL,
    content clob CONSTRAINT board_content_nn NOT NULL,
    pwd VARCHAR2(10) CONSTRAINT board_pwd_nn NOT NULL,
    regdate date default sysdate,
    hit NUMBER DEFAULT 0,
    CONSTRAINT board_no_pk PRIMARY KEY(no)
);
INSERT INTO board(no,name,subject,content,pwd) VALUES(1,'홍길동','웹 게시판 제작..',
'웹 게시판 제작..','1234');
INSERT INTO board(no,name,subject,content,pwd) VALUES(2,'홍길동','웹 게시판 제작..',
'웹 게시판 제작..','1234');
INSERT INTO board(no,name,subject,content,pwd) VALUES(3,'홍길동','웹 게시판 제작..',
'웹 게시판 제작..','1234');
INSERT INTO board(no,name,subject,content,pwd) VALUES(4,'홍길동','웹 게시판 제작..',
'웹 게시판 제작..','1234');
INSERT INTO board(no,name,subject,content,pwd) VALUES(5,'홍길동','웹 게시판 제작..',
'웹 게시판 제작..','1234');
INSERT INTO board(no,name,subject,content,pwd) VALUES(6,'홍길동','웹 게시판 제작..',
'웹 게시판 제작..','1234');
INSERT INTO board(no,name,subject,content,pwd) VALUES(7,'홍길동','웹 게시판 제작..',
'웹 게시판 제작..','1234');
INSERT INTO board(no,name,subject,content,pwd) VALUES(8,'홍길동','웹 게시판 제작..',
'웹 게시판 제작..','1234');
INSERT INTO board(no,name,subject,content,pwd) VALUES(9,'홍길동','웹 게시판 제작..',
'웹 게시판 제작..','1234');
INSERT INTO board(no,name,subject,content,pwd) VALUES(10,'홍길동','웹 게시판 제작..',
'웹 게시판 제작..','1234');
INSERT INTO board(no,name,subject,content,pwd) VALUES(11,'홍길동','웹 게시판 제작..',
'웹 게시판 제작..','1234');
COMMIT;
SELECT * FROM BOARD;

SELECT CEIL(COUNT(*)/10.0) FROM board;
SELECT no,subject,name,regdate,hit FROM board ORDER BY no DESC;
