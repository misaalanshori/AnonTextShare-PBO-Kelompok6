-- Dropping the public user
DROP USER public_user CASCADE;

-- Dropping the procedures/functions
DROP PROCEDURE create_document;
DROP PROCEDURE create_document_nopass;
DROP FUNCTION check_document_password;
DROP PROCEDURE update_document;
DROP PROCEDURE delete_document;
DROP PROCEDURE increment_view_count;

-- Dropping the views
DROP VIEW latest_documents_view;
DROP VIEW documents_view;

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

CREATE VIEW documents_view AS
SELECT id, view_count, title, text, visibility, time_created
FROM document;

CREATE OR REPLACE PROCEDURE create_document
(
    id OUT document.id%TYPE,
    password IN VARCHAR2,
    title IN document.title%TYPE,
    text IN document.text%TYPE,
    visibility IN document.visibility%TYPE
) IS
    hashed_password VARCHAR2(64);
BEGIN
    id := SYS_GUID();
    hashed_password := LOWER(RAWTOHEX(DBMS_CRYPTO.HASH(UTL_I18N.STRING_TO_RAW(password, 'AL32UTF8'), DBMS_CRYPTO.HASH_SH1)));
    -- Insert data into the document table
    INSERT INTO document (id, password_hash, title, text, visibility, time_created)
    VALUES (id, hashed_password, title, text, visibility, CURRENT_TIMESTAMP);
    -- Commit the transaction
    COMMIT;
END create_document;
/

CREATE OR REPLACE PROCEDURE create_document_nopass
(
    id OUT document.id%TYPE,
    title IN document.title%TYPE,
    text IN document.text%TYPE,
    visibility IN document.visibility%TYPE
) IS
BEGIN
    id := SYS_GUID();
    -- Insert data into the document table
    INSERT INTO document (id, password_hash, title, text, visibility, time_created)
    VALUES (id, null, title, text, visibility, CURRENT_TIMESTAMP);
    -- Commit the transaction
    COMMIT;
END create_document_nopass;
/

CREATE OR REPLACE FUNCTION check_document_password
(
    p_id IN VARCHAR2,
    p_password IN VARCHAR2
) RETURN BOOLEAN IS
    v_password_hash VARCHAR2(64);
    v_hashed_password VARCHAR2(64);
BEGIN
    -- Retrieve the password hash for the given ID
    SELECT password_hash INTO v_password_hash
    FROM document
    WHERE id = p_id;

    -- Check if the ID is found and the password hash is not NULL
    IF v_password_hash IS NULL THEN
        RETURN FALSE; -- Password hash is NULL, indicating invalid ID
    END IF;

    -- Hash the provided password using SHA-1
    v_hashed_password := LOWER(RAWTOHEX(DBMS_CRYPTO.HASH(UTL_I18N.STRING_TO_RAW(p_password, 'AL32UTF8'), DBMS_CRYPTO.HASH_SH1)));
  
    -- Compare the hashed password with the stored password hash
    IF v_password_hash = v_hashed_password THEN
        RETURN TRUE; -- Password is verified
    ELSE
        RETURN FALSE; -- Password is not verified
    END IF;
END check_document_password;
/

CREATE OR REPLACE PROCEDURE update_document
(
    id IN document.id%TYPE,
    password IN VARCHAR2,
    title IN document.title%TYPE,
    text IN document.text%TYPE,
    visibility IN document.visibility%TYPE
) IS
    v_password_hash VARCHAR2(64);
BEGIN
    -- Check the password
    IF check_document_password(id, password) THEN
        -- Update the document table
        UPDATE document SET
            title = title,
            text = text,
            visibility = visibility
        WHERE id = id;
    ELSE
        RAISE_APPLICATION_ERROR(-21001, 'Incorrect Document password');
    END IF;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        -- Document with given ID not found
        RAISE_APPLICATION_ERROR(-21002, 'Document not found');
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-29999, 'An Exception Occured');
END update_document;
/

CREATE OR REPLACE PROCEDURE delete_document
(
    p_id IN document.id%TYPE,
    password IN VARCHAR2
) IS
    valid_password BOOLEAN;
BEGIN
    -- Check if the provided password matches the password_hash for the given id
    valid_password := check_document_password(p_id, password);

    IF valid_password THEN
        -- Delete the document if the password is valid
        DELETE FROM document WHERE id = p_id;
    ELSE
        RAISE_APPLICATION_ERROR(-21001, 'Incorrect Document password');
    END IF;
EXCEPTION
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-29999, 'An Exception Occured');
END delete_document;
/

CREATE OR REPLACE PROCEDURE increment_view_count
(
    document_id IN document.id%TYPE
) IS
BEGIN
    -- Increment view_count by 1
    UPDATE document SET
        view_count = view_count + 1
    WHERE id = document_id;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        -- Document with given ID not found
        RAISE_APPLICATION_ERROR(-21002, 'Document not found');
    WHEN OTHERS THEN
        RAISE_APPLICATION_ERROR(-29999, 'An Exception Occured');
END increment_view_count;
/