-- Dropping the public user
DROP USER public_user CASCADE;

-- Dropping the procedures/functions
DROP PROCEDURE create_document;
DROP PROCEDURE create_document_nopass;
DROP FUNCTION check_document_password;
DROP PROCEDURE update_document;
DROP PROCEDURE delete_document;
DROP PROCEDURE increment_view_count;
DROP PROCEDURE insert_document_comment;

DROP PROCEDURE create_collection;
DROP PROCEDURE start_create_collection_nopass;
DROP PROCEDURE end_create_collection_nopass;
DROP FUNCTION check_collection_password;
DROP PROCEDURE update_collection;
DROP PROCEDURE delete_collection;
DROP PROCEDURE insert_collection_document;
DROP PROCEDURE delete_collection_document;

-- Dropping the views
DROP VIEW latest_documents_view;
DROP VIEW documents_view;

DROP VIEW collections_view;

-- Removing foreign keys from tables
ALTER TABLE document_comment DROP CONSTRAINT fk_comment_document_id;
ALTER TABLE collection_document DROP CONSTRAINT fk_col_doc_collection_id;
ALTER TABLE collection_document DROP CONSTRAINT fk_col_doc_document_id;

-- Dropping the tables
DROP TABLE document_comment;
DROP TABLE collection_document;
DROP TABLE document;
DROP TABLE collection;

-- Creating the Public User
CREATE USER public_user IDENTIFIED BY helloanonymous;
GRANT CREATE SESSION TO public_user;


-- Creating the Table
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


-- Creating Views
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

CREATE VIEW collections_view AS
SELECT id, title, time_created
FROM collection
WHERE time_created IS NOT NULL;

-- Creating Document Access Procedures
CREATE OR REPLACE PROCEDURE create_document
(
    p_id OUT document.id%TYPE,
    p_password IN VARCHAR2,
    p_title IN document.title%TYPE,
    p_text IN document.text%TYPE,
    p_visibility IN document.visibility%TYPE
) IS
    hashed_password VARCHAR2(64);
BEGIN
    p_id := SYS_GUID();
    hashed_password := LOWER(RAWTOHEX(DBMS_CRYPTO.HASH(UTL_I18N.STRING_TO_RAW(p_password, 'AL32UTF8'), DBMS_CRYPTO.HASH_SH1)));
    -- Insert data into the document table
    INSERT INTO document (id, password_hash, title, text, visibility, time_created, view_count)
    VALUES (p_id, hashed_password, p_title, p_text, p_visibility, CURRENT_TIMESTAMP, 0);
    -- Commit the transaction
    COMMIT;
END create_document;
/

CREATE OR REPLACE PROCEDURE create_document_nopass
(
    p_id OUT document.id%TYPE,
    p_title IN document.title%TYPE,
    p_text IN document.text%TYPE,
    p_visibility IN document.visibility%TYPE
) IS
BEGIN
    p_id := SYS_GUID();
    -- Insert data into the document table
    INSERT INTO document (id, password_hash, title, text, visibility, time_created, view_count)
    VALUES (p_id, null, p_title, p_text, p_visibility, CURRENT_TIMESTAMP, 0);
    -- Commit the transaction
    COMMIT;
END create_document_nopass;
/

CREATE OR REPLACE FUNCTION check_document_password
(
    p_id IN document.id%TYPE,
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
    END IF;
    RETURN FALSE; -- Password is not verified
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        -- Document with given ID not found
        RAISE_APPLICATION_ERROR(-20012, 'Document not found');
END check_document_password;
/

CREATE OR REPLACE PROCEDURE update_document
(
    p_id IN document.id%TYPE,
    p_password IN VARCHAR2,
    p_title IN document.title%TYPE,
    p_text IN document.text%TYPE,
    p_visibility IN document.visibility%TYPE
) IS
    v_password_hash VARCHAR2(64);
    valid_password BOOLEAN;
--    a NUMBER(20);
BEGIN
    valid_password := check_document_password(p_id, p_password);
    -- Check the password
    IF valid_password THEN
--        select count(*) into a from dual;
--         Update the document table
        UPDATE document SET
            title = p_title,
            text = p_text,
            visibility = p_visibility
        WHERE id = p_id;
    ELSE
        RAISE_APPLICATION_ERROR(-20011, 'Incorrect Document password');
    END IF;
    COMMIT;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        -- Document with given ID not found
        RAISE_APPLICATION_ERROR(-20012, 'Document not found');
--    WHEN OTHERS THEN
--        RAISE_APPLICATION_ERROR(-20999, 'An Exception Occured');
END update_document;
/

CREATE OR REPLACE PROCEDURE delete_document
(
    p_id IN document.id%TYPE,
    p_password IN VARCHAR2
) IS
    valid_password BOOLEAN;
BEGIN
    -- Check if the provided password matches the password_hash for the given id
    valid_password := check_document_password(p_id, p_password);

    IF valid_password THEN
        -- Delete the document if the password is valid
        DELETE FROM document WHERE id = p_id;
    ELSE
        RAISE_APPLICATION_ERROR(-20011, 'Incorrect Document password');
    END IF;
    COMMIT;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        -- Document with given ID not found
        RAISE_APPLICATION_ERROR(-20012, 'Document not found');
--    WHEN OTHERS THEN
--        RAISE_APPLICATION_ERROR(-20999, 'An Exception Occured');
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
    COMMIT;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        -- Document with given ID not found
        RAISE_APPLICATION_ERROR(-20012, 'Document not found');
--    WHEN OTHERS THEN
--        RAISE_APPLICATION_ERROR(-20999, 'An Exception Occured');
END increment_view_count;
/

CREATE OR REPLACE PROCEDURE insert_document_comment
(
    p_document_id IN document.id%TYPE,
    p_name in document_comment.name%TYPE,
    p_text in document_comment.text%TYPE
) IS
BEGIN 
    INSERT INTO document_comment (document_id, name, text, time_created)
    values (p_document_id, p_name, p_text, CURRENT_TIMESTAMP);
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        -- Document with given ID not found
        RAISE_APPLICATION_ERROR(-20012, 'Document not found');
END insert_document_comment;
/

-- Creating Collection Access Procedures
CREATE OR REPLACE PROCEDURE create_collection
(
    p_id OUT collection.id%TYPE,
    p_password IN VARCHAR2,
    p_title IN collection.title%TYPE
) IS
    hashed_password VARCHAR2(64);
BEGIN
    p_id := SYS_GUID();
    hashed_password := LOWER(RAWTOHEX(DBMS_CRYPTO.HASH(UTL_I18N.STRING_TO_RAW(p_password, 'AL32UTF8'), DBMS_CRYPTO.HASH_SH1)));
    -- Insert data into the document table
    INSERT INTO collection (id, password_hash, title, time_created)
    VALUES (p_id, hashed_password, p_title, CURRENT_TIMESTAMP);
    -- Commit the transaction
    COMMIT;
END create_collection;
/

CREATE OR REPLACE PROCEDURE start_create_collection_nopass
(
    p_id OUT collection.id%TYPE,
    p_title IN collection.title%TYPE
) IS
BEGIN
    p_id := SYS_GUID();
    -- Insert data into the document table
    INSERT INTO collection (id, password_hash, title, time_created)
    VALUES (p_id, null, p_title, null);
    -- Commit the transaction
    COMMIT;
END start_create_collection_nopass;
/

CREATE OR REPLACE PROCEDURE end_create_collection_nopass
(
    p_id in collection.id%TYPE
) IS
BEGIN
    -- Insert data into the document table
    UPDATE collection SET time_created = CURRENT_TIMESTAMP WHERE id = p_id;
    -- Commit the transaction
    COMMIT;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        -- Collection with given ID not found
        RAISE_APPLICATION_ERROR(-20022, 'Collection not found');
END end_create_collection_nopass;
/

CREATE OR REPLACE FUNCTION check_collection_password
(
    p_id IN collection.id%TYPE,
    p_password IN VARCHAR2
) RETURN BOOLEAN IS
    v_password_hash VARCHAR2(64);
    v_hashed_password VARCHAR2(64);
BEGIN
    -- Retrieve the password hash for the given ID
    SELECT password_hash INTO v_password_hash
    FROM collection
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
    END IF;
    RETURN FALSE; -- Password is not verified
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        -- Collection with given ID not found
        RAISE_APPLICATION_ERROR(-20022, 'Collection not found');
END check_collection_password;
/

CREATE OR REPLACE PROCEDURE update_collection
(
    p_id IN collection.id%TYPE,
    p_password IN VARCHAR2,
    p_title IN collection.title%TYPE
) IS
    v_password_hash VARCHAR2(64);
    valid_password BOOLEAN;
BEGIN
    valid_password := check_collection_password(p_id, p_password);
    -- Check the password
    IF valid_password THEN
        UPDATE collection SET
            title = p_title
        WHERE id = p_id;
    ELSE
        RAISE_APPLICATION_ERROR(-20021, 'Incorrect Collection password');
    END IF;
    COMMIT;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        -- Collection with given ID not found
        RAISE_APPLICATION_ERROR(-20022, 'Collection not found');
--    WHEN OTHERS THEN
--        RAISE_APPLICATION_ERROR(-20999, 'An Exception Occured');
END update_collection;
/

CREATE OR REPLACE PROCEDURE delete_collection
(
    p_id IN collection.id%TYPE,
    p_password IN VARCHAR2
) IS
    valid_password BOOLEAN;
BEGIN
    -- Check if the provided password matches the password_hash for the given id
    valid_password := check_collection_password(p_id, p_password);

    IF valid_password THEN
        -- Delete the Collection if the password is valid
        DELETE FROM collection WHERE id = p_id;
    ELSE
        RAISE_APPLICATION_ERROR(-20021, 'Incorrect Collection password');
    END IF;
    COMMIT;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        -- Collection with given ID not found
        RAISE_APPLICATION_ERROR(-20022, 'Collection not found');
END delete_collection;
/

CREATE OR REPLACE PROCEDURE insert_collection_document
(
    p_collection_id IN collection_document.collection_id%TYPE,
    p_document_id IN collection_document.document_id%TYPE,
    p_password IN VARCHAR2
) IS
    valid_password BOOLEAN;
    collection_created collection.time_created%TYPE;
BEGIN 
    -- Check if the provided password matches the password_hash for the given id
    SELECT time_created INTO collection_created 
    FROM collection 
    WHERE id = p_collection_id;
    
    IF collection_created IS NOT NULL THEN
        valid_password := check_collection_password(p_collection_id, p_password);
    ELSE
        valid_password := true;
    END IF;
    
    IF valid_password THEN
        INSERT INTO collection_document (collection_id, document_id, time_created)
        values (p_collection_id, p_document_id, CURRENT_TIMESTAMP);
    ELSE
        RAISE_APPLICATION_ERROR(-20021, 'Incorrect Collection password');
    END IF;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        -- Collection with given ID not found
        RAISE_APPLICATION_ERROR(-20022, 'Collection not found');
END insert_collection_document;
/

CREATE OR REPLACE PROCEDURE delete_collection_document
(
    p_collection_id IN collection_document.collection_id%TYPE,
    p_document_id IN collection_document.document_id%TYPE,
    p_password IN VARCHAR2
) IS
    valid_password BOOLEAN;
BEGIN 
    -- Check if the provided password matches the password_hash for the given id
    valid_password := check_collection_password(p_collection_id, p_password);

    IF valid_password THEN
        DELETE FROM collection_document 
        WHERE collection_id = p_collection_id AND document_id = p_document_id;
    ELSE
        RAISE_APPLICATION_ERROR(-20021, 'Incorrect Collection password');
    END IF;
EXCEPTION
    WHEN NO_DATA_FOUND THEN
        -- Collection with given ID not found
        RAISE_APPLICATION_ERROR(-20023, 'Collection-Document not found');
END delete_collection_document;
/

-- Granting privileges
GRANT EXECUTE ON create_document TO public_user;
GRANT EXECUTE ON create_document_nopass TO public_user;
GRANT EXECUTE ON check_document_password TO public_user;
GRANT EXECUTE ON update_document TO public_user;
GRANT EXECUTE ON delete_document TO public_user;
GRANT EXECUTE ON increment_view_count TO public_user;
GRANT EXECUTE ON insert_document_comment TO public_user;

GRANT EXECUTE ON create_collection TO public_user;
GRANT EXECUTE ON start_create_collection_nopass TO public_user;
GRANT EXECUTE ON end_create_collection_nopass TO public_user;
GRANT EXECUTE ON check_collection_password TO public_user;
GRANT EXECUTE ON update_collection TO public_user;
GRANT EXECUTE ON delete_collection TO public_user;
GRANT EXECUTE ON insert_collection_document TO public_user;
GRANT EXECUTE ON delete_collection_document TO public_user;

GRANT SELECT ON latest_documents_view TO public_user;
GRANT SELECT ON documents_view TO public_user;
GRANT SELECT ON document_comment TO public_user;

GRANT SELECT ON collections_view TO public_user;
GRANT SELECT ON collection_document TO public_user;

COMMIT;
