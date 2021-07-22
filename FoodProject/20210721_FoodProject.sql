CREATE TABLE food_category(
    cno NUMBER,
    title VARCHAR2(1000) CONSTRAINT fc_title_nn NOT NULL,
    subject VARCHAR2(1000) CONSTRAINT fc_subject_nn NOT NULL,
    poster VARCHAR2(260) CONSTRAINT fc_poster_nn NOT NULL,
    link VARCHAR2(100) CONSTRAINT fc_link_nn NOT NULL,
    CONSTRAINT fc_cno_pk PRIMARY KEY(cno)
);

CREATE TABLE food_house(  
no NUMBER,
cno NUMBER,
name VARCHAR2(100) CONSTRAINT fh_name_nn NOT NULL,
score NUMBER(2,1) CONSTRAINT fh_score_nn NOT NULL,
address VARCHAR2(200) CONSTRAINT fh_address_nn NOT NULL,
tel VARCHAR2(20) CONSTRAINT fh_tel_nn NOT NULL,
type VARCHAR2(100) CONSTRAINT fh_type_nn NOT NULL,
price VARCHAR2(100) CONSTRAINT fh_price_nn NOT NULL,
parking VARCHAR2(100) CONSTRAINT fh_parking_nn NOT NULL,
time VARCHAR2(30),
menu VARCHAR2(1000),
poster VARCHAR2(1000) CONSTRAINT fh_poster_nn NOT NULL,
good NUMBER,
soso NUMBER,
bad NUMBER,
CONSTRAINT fh_no_pk PRIMARY KEY(no),
CONSTRAINT fh_cno_fk FOREIGN KEY(cno)
REFERENCES food_category(cno)
);

DESC food_house;
SELECT * FROM food_category;
SELECT * FROM food_house;

ALTER TABLE food_house MODIFY time VARCHAR2(200);

SELECT * FROM food_house WHERE cno=3;
SELECT cno,AVG(score)
FROM food_house
GROUP BY cno
ORDER BY cno;

SELECT name FROM food_house
WHERE address LIKE '%°­³²%';

SELECT fc.cno,title,subject,name,tel FROM food_category fc,food_house fh
WHERE fc.cno=fh.cno ORDER BY fc.cno;

SELECT /*+ INDEX_ASC(food_house fh_no_pk*/ * FROM food_house;

SELECT cno,title,subject,poster,link
FROM food_category
ORDER BY cno; 

SELECT no,name,address,poster,tel FROM food_house
WHERE cno=1;

SELECT poster FROM food_house;