-- Dropping the public user
DROP USER public_user CASCADE;

-- Dropping the views
DROP VIEW latest_documents_view;

-- Removing foreign keys from tables
ALTER TABLE document_comment DROP CONSTRAINT fk_comment_document_id;
ALTER TABLE collection_document DROP CONSTRAINT fk_col_doc_collection_id;
ALTER TABLE collection_document DROP CONSTRAINT fk_col_doc_document_id;

-- Dropping the tables
DROP TABLE document_comment;
DROP TABLE collection_document;
DROP TABLE document;
DROP TABLE collection;


CREATE USER public_user IDENTIFIED BY helloanonymous;
GRANT CREATE SESSION TO public_user;

create table document (
    id VARCHAR2(64),
    password_hash VARCHAR2(64),
    view_count NUMBER(20),
    title VARCHAR2(256),
    text VARCHAR2(3120),
    visibility NUMBER(1),
    time_created TIMESTAMP(3)
);

create table document_comment (
    document_id VARCHAR2(64),
    name VARCHAR2(256),
    text VARCHAR2(1024),
    time_created TIMESTAMP(3)
);

create table collection (
    id VARCHAR2(64),
    password_hash VARCHAR2(64),
    title VARCHAR2(256),
    time_created TIMESTAMP(3)
);

create table collection_document (
    collection_id VARCHAR2(64),
    document_id VARCHAR2(64),
    time_created TIMESTAMP(3)
);

ALTER TABLE document ADD CONSTRAINT pk_document PRIMARY KEY (id);
ALTER TABLE document MODIFY (id NOT NULL);
ALTER TABLE document MODIFY (visibility NOT NULL);

ALTER TABLE collection ADD CONSTRAINT pk_collection PRIMARY KEY (id);
ALTER TABLE collection MODIFY (id NOT NULL);

ALTER TABLE document_comment ADD CONSTRAINT fk_comment_document_id FOREIGN KEY (document_id) REFERENCES document(id) ON DELETE CASCADE;
ALTER TABLE collection_document ADD CONSTRAINT fk_col_doc_collection_id FOREIGN KEY (collection_id) REFERENCES collection(id) ON DELETE CASCADE;
ALTER TABLE collection_document ADD CONSTRAINT fk_col_doc_document_id FOREIGN KEY (document_id) REFERENCES document(id) ON DELETE CASCADE;


CREATE OR REPLACE VIEW latest_documents_view AS
SELECT *
FROM (
    SELECT id, view_count, title
    FROM document
    WHERE visibility = 1
    ORDER BY time_created DESC
)
WHERE ROWNUM <= 10;

