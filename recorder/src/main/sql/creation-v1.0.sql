CREATE TABLE PROBE_DATA (
	PROBE_ENTRY_ID INT NOT NULL GENERATED ALWAYS AS IDENTITY,
	PROBE_ID VARCHAR(36) NOT NULL,
	PROBE_TYPE VARCHAR(36) NOT NULL,
	PROBE_VALUE VARCHAR(32) NOT NULL,
	PROBE_VALUE_DATE TIMESTAMP NOT NULL
);

CREATE TABLE PARAMETER_DATA (
	DATA_ENTRY_ID VARCHAR(255) NOT NULL PRIMARY KEY,
	DATA_ITEM VARCHAR(32672) NOT NULL
);

INSERT INTO PARAMETER_DATA VALUES('version', '1.0');