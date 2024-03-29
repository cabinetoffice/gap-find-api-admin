CREATE TABLE
	api_key (
		api_key_id integer NOT NULL GENERATED BY DEFAULT AS IDENTITY (
			INCREMENT 1 START 1 MINVALUE 1 MAXVALUE 2147483647 CACHE 1
		),
		funder_id integer NOT NULL,
		api_key_value VARCHAR(128) NOT NULL,
		api_key_name VARCHAR(1024) NOT NULL,
		api_key_description TEXT,
		CONSTRAINT api_key_pkey PRIMARY KEY (api_key_id),
		CONSTRAINT grant_funding_organisation_fk FOREIGN KEY (funder_id) REFERENCES public.grant_funding_organisation (funder_id) MATCH SIMPLE ON UPDATE NO ACTION ON DELETE NO ACTION
	);

CREATE INDEX api_key_value_index on api_key (api_key_value);

ALTER TABLE api_key
ADD COLUMN created_date TIMESTAMP WITHOUT TIME ZONE,
ADD COLUMN is_revoked BOOLEAN,
ADD COLUMN revocation_date TIMESTAMP WITHOUT TIME ZONE,
ADD COLUMN revoked_by integer;

ALTER TABLE api_key
ADD COLUMN api_gateway_id VARCHAR(50) NOT NULL;