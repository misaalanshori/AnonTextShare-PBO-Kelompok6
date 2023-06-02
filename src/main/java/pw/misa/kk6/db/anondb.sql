-- Dropping the public user
DROP USER public_user CASCADE;

---- Removing foreign keys from tables
--ALTER TABLE comment DROP CONSTRAINT fk_comment_document_code;
--ALTER TABLE collection_document DROP CONSTRAINT fk_collection_document_collection_code;
--ALTER TABLE collection_document DROP CONSTRAINT fk_collection_document_document_code;

-- Dropping the tables
DROP TABLE comment;
DROP TABLE collection_document;
DROP TABLE document;
DROP TABLE collection;


CREATE USER public_user IDENTIFIED BY helloanonymous;
GRANT CREATE SESSION TO public_user;

create table document (
    code VARCHAR2(64),
    password_hash VARCHAR2(64),
    title VARCHAR2(256),
    contents VARCHAR2(5120),
    visibility NUMBER(1)
)

create table comment (
    document_code VARCHAR2(64),
    subject VARCHAR2(256),
    contents VARCHAR2(1024)
)

create table collection (
    code VARCHAR2(64),
    password_hash VARCHAR2(64),
    title VARCHAR2(256)
)

create table collection_document (
    collection_code VARCHAR2(64),
    document_code VARCHAR2(64)
)

ALTER TABLE document ADD CONSTRAINT pk_document PRIMARY KEY (code);
ALTER TABLE document MODIFY (code NOT NULL);
ALTER TABLE document MODIFY (visibility NOT NULL);

ALTER TABLE collection ADD CONSTRAINT pk_collection PRIMARY KEY (code);
ALTER TABLE collection MODIFY (code NOT NULL);

ALTER TABLE comment ADD CONSTRAINT fk_comment_document_code FOREIGN KEY (document_code) REFERENCES document(code) ON DELETE CASCADE;
ALTER TABLE collection_document ADD CONSTRAINT fk_collection_document_collection_code FOREIGN KEY (collection_code) REFERENCES collection(code) ON DELETE CASCADE;
ALTER TABLE collection_document ADD CONSTRAINT fk_collection_document_document_code FOREIGN KEY (document_code) REFERENCES document(code) ON DELETE CASCADE;