-- Table: public.addressmodel

-- DROP TABLE IF EXISTS public.addressmodel;

CREATE TABLE IF NOT EXISTS public.addressmodel
(
    finalshippingcost double precision NOT NULL,
    riskarea boolean NOT NULL,
    deliverydate timestamp(6) without time zone,
    addressid uuid NOT NULL,
    address1 character varying(255) COLLATE pg_catalog."default",
    address2 character varying(255) COLLATE pg_catalog."default",
    city character varying(255) COLLATE pg_catalog."default",
    country character varying(255) COLLATE pg_catalog."default",
    firstname character varying(255) COLLATE pg_catalog."default",
    lastname character varying(255) COLLATE pg_catalog."default",
    phonenumber character varying(255) COLLATE pg_catalog."default",
    postacode character varying(255) COLLATE pg_catalog."default",
    uf character varying(255) COLLATE pg_catalog."default",
    userid character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT addressmodel_pkey PRIMARY KEY (addressid)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.addressmodel
    OWNER to postgres;

-- Table: public.shippingmodel

-- DROP TABLE IF EXISTS public.shippingmodel;

CREATE TABLE IF NOT EXISTS public.shippingmodel
(
    shippingmethod smallint,
    state smallint,
    address_model_id uuid,
    shippingid uuid NOT NULL,
    carriercode character varying(255) COLLATE pg_catalog."default",
    orderid character varying(255) COLLATE pg_catalog."default",
    trackingurl character varying(255) COLLATE pg_catalog."default",
    userid character varying(255) COLLATE pg_catalog."default",
    CONSTRAINT shippingmodel_pkey PRIMARY KEY (shippingid),
    CONSTRAINT fk3vj57oupjfpc5d2kvw7p15nn5 FOREIGN KEY (address_model_id)
        REFERENCES public.addressmodel (addressid) MATCH SIMPLE
        ON UPDATE NO ACTION
        ON DELETE NO ACTION,
    CONSTRAINT shippingmodel_shippingmethod_check CHECK (shippingmethod >= 0 AND shippingmethod <= 2),
    CONSTRAINT shippingmodel_state_check CHECK (state >= 0 AND state <= 6)
)

TABLESPACE pg_default;

ALTER TABLE IF EXISTS public.shippingmodel
    OWNER to postgres;