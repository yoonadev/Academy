CREATE TABLE daum_movie(
    mno NUMBER, --PRIMARY KEY
    cno NUMBER, --ī�װ���ȣ(����)
    title VARCHAR2(300) CONSTRAINT dm_title_nn NOT NULL,
    regdate VARCHAR2(100),
    genre VARCHAR2(200) CONSTRAINT dm_genrenn NOT NULL,
    nation VARCHAR2(100),
    grade VARCHAR2(70),
    time VARCHAR2(30),
    score NUMBER(3,1),
    showUser VARCHAR2(100),
    poster VARCHAR2(260) CONSTRAINT dm_poster_nn NOT NULL,
    story CLOB,
    CONSTRAINT dm_mno_pk PRIMARY KEY(mno)    
);
--�ڵ�������ȣ(MNO) ������
CREATE SEQUENCE daum_mno_seq
    START WITH 1
    INCREMENT BY 1
    NOCYCLE
    NOCACHE;

DESC daum_movie;
select count(*) from daum_movie;
select * from daum_movie order by cno,mno;
--@c:\Dev\oracleDev\DaumMovie.sql

--�ó��� => PRIVATE(SYSTEM) / PUBLIC(�Ϲ� USER ����) => hr
CREATE PUBLIC SYNONYM dm FOR DAUM_MOVIE;    --���̺�Ī
SELECT * FROM DM;
--����
DROP PUBLIC SYNONYM DM;

SELECT /*+ INDEX_ASC(dm dm_mno_pk) */* FROM dm;
select mno,title from dm order by mno;



--NAVER ��ȭ
--å (�˶��,����)