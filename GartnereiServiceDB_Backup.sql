--
-- PostgreSQL database dump
--

-- Dumped from database version 16.4
-- Dumped by pg_dump version 16.4

-- Started on 2024-09-28 15:33:37

SET statement_timeout = 0;
SET lock_timeout = 0;
SET idle_in_transaction_session_timeout = 0;
SET client_encoding = 'UTF8';
SET standard_conforming_strings = on;
SELECT pg_catalog.set_config('search_path', '', false);
SET check_function_bodies = false;
SET xmloption = content;
SET client_min_messages = warning;
SET row_security = off;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 225 (class 1259 OID 16479)
-- Name: bestellposition; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bestellposition (
    id integer NOT NULL,
    bestellung_id integer NOT NULL,
    produkt_id integer NOT NULL,
    menge integer NOT NULL
);


ALTER TABLE public.bestellposition OWNER TO postgres;

--
-- TOC entry 224 (class 1259 OID 16478)
-- Name: bestellposition_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.bestellposition_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.bestellposition_id_seq OWNER TO postgres;

--
-- TOC entry 4883 (class 0 OID 0)
-- Dependencies: 224
-- Name: bestellposition_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.bestellposition_id_seq OWNED BY public.bestellposition.id;


--
-- TOC entry 219 (class 1259 OID 16427)
-- Name: bestellung; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bestellung (
    bestellnummer integer NOT NULL,
    beschreibung character varying(255),
    preis numeric(10,2) NOT NULL,
    bestelldatum date NOT NULL,
    lieferstatus integer NOT NULL,
    kundennummer integer NOT NULL,
    bestellprozess_typ character varying(20)
);


ALTER TABLE public.bestellung OWNER TO postgres;

--
-- TOC entry 218 (class 1259 OID 16426)
-- Name: bestellung_bestellnummer_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.bestellung_bestellnummer_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.bestellung_bestellnummer_seq OWNER TO postgres;

--
-- TOC entry 4884 (class 0 OID 0)
-- Dependencies: 218
-- Name: bestellung_bestellnummer_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.bestellung_bestellnummer_seq OWNED BY public.bestellung.bestellnummer;


--
-- TOC entry 217 (class 1259 OID 16420)
-- Name: kunde; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.kunde (
    kundennummer integer NOT NULL,
    nachname character varying(50) NOT NULL,
    vorname character varying(50) NOT NULL,
    strasse character varying(50) NOT NULL,
    hausnummer character varying(10) NOT NULL,
    postleitzahl character varying(5) NOT NULL,
    email character varying(100) NOT NULL,
    telefonnummer character varying(20),
    kundentyp_id integer NOT NULL,
    dtype character varying(255)
);


ALTER TABLE public.kunde OWNER TO postgres;

--
-- TOC entry 216 (class 1259 OID 16419)
-- Name: kunde_kundennummer_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.kunde_kundennummer_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.kunde_kundennummer_seq OWNER TO postgres;

--
-- TOC entry 4885 (class 0 OID 0)
-- Dependencies: 216
-- Name: kunde_kundennummer_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.kunde_kundennummer_seq OWNED BY public.kunde.kundennummer;


--
-- TOC entry 221 (class 1259 OID 16440)
-- Name: kundentyp; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.kundentyp (
    id integer NOT NULL,
    typ integer NOT NULL,
    name character varying(50) NOT NULL,
    beschreibung character varying(255)
);


ALTER TABLE public.kundentyp OWNER TO postgres;

--
-- TOC entry 220 (class 1259 OID 16439)
-- Name: kundentyp_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.kundentyp_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.kundentyp_id_seq OWNER TO postgres;

--
-- TOC entry 4886 (class 0 OID 0)
-- Dependencies: 220
-- Name: kundentyp_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.kundentyp_id_seq OWNED BY public.kundentyp.id;


--
-- TOC entry 223 (class 1259 OID 16455)
-- Name: produkt; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.produkt (
    id integer NOT NULL,
    name character varying(100) NOT NULL,
    beschreibung character varying(255) NOT NULL,
    bestand integer NOT NULL,
    preis numeric(10,2) NOT NULL
);


ALTER TABLE public.produkt OWNER TO postgres;

--
-- TOC entry 222 (class 1259 OID 16454)
-- Name: produkt_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.produkt_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.produkt_id_seq OWNER TO postgres;

--
-- TOC entry 4887 (class 0 OID 0)
-- Dependencies: 222
-- Name: produkt_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.produkt_id_seq OWNED BY public.produkt.id;


--
-- TOC entry 4708 (class 2604 OID 16482)
-- Name: bestellposition id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bestellposition ALTER COLUMN id SET DEFAULT nextval('public.bestellposition_id_seq'::regclass);


--
-- TOC entry 4705 (class 2604 OID 16430)
-- Name: bestellung bestellnummer; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bestellung ALTER COLUMN bestellnummer SET DEFAULT nextval('public.bestellung_bestellnummer_seq'::regclass);


--
-- TOC entry 4704 (class 2604 OID 16423)
-- Name: kunde kundennummer; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kunde ALTER COLUMN kundennummer SET DEFAULT nextval('public.kunde_kundennummer_seq'::regclass);


--
-- TOC entry 4706 (class 2604 OID 16443)
-- Name: kundentyp id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kundentyp ALTER COLUMN id SET DEFAULT nextval('public.kundentyp_id_seq'::regclass);


--
-- TOC entry 4707 (class 2604 OID 16458)
-- Name: produkt id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.produkt ALTER COLUMN id SET DEFAULT nextval('public.produkt_id_seq'::regclass);


--
-- TOC entry 4877 (class 0 OID 16479)
-- Dependencies: 225
-- Data for Name: bestellposition; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.bestellposition VALUES (65, 34, 2, 7);
INSERT INTO public.bestellposition VALUES (66, 34, 7, 1);
INSERT INTO public.bestellposition VALUES (67, 34, 1, 1);
INSERT INTO public.bestellposition VALUES (68, 35, 10, 1);
INSERT INTO public.bestellposition VALUES (69, 35, 9, 2);
INSERT INTO public.bestellposition VALUES (70, 35, 8, 1);
INSERT INTO public.bestellposition VALUES (71, 36, 11, 1);
INSERT INTO public.bestellposition VALUES (72, 37, 1, 10);
INSERT INTO public.bestellposition VALUES (73, 37, 4, 20);
INSERT INTO public.bestellposition VALUES (74, 38, 5, 1);
INSERT INTO public.bestellposition VALUES (75, 39, 3, 1);
INSERT INTO public.bestellposition VALUES (76, 39, 6, 1);


--
-- TOC entry 4871 (class 0 OID 16427)
-- Dependencies: 219
-- Data for Name: bestellung; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.bestellung VALUES (34, 'Gartenblumen', 62.91, '2024-09-04', 2, 18, NULL);
INSERT INTO public.bestellung VALUES (35, 'Essen', 16.46, '2024-09-21', 1, 18, NULL);
INSERT INTO public.bestellung VALUES (36, 'Kakteen fürs Büro', 19.99, '2024-09-25', 0, 20, NULL);
INSERT INTO public.bestellung VALUES (37, 'Schnelle Bestellung für eine Hochzeit', 209.70, '2024-09-24', 2, 21, NULL);
INSERT INTO public.bestellung VALUES (38, 'Lavendel', 8.99, '2024-09-21', 3, 21, NULL);
INSERT INTO public.bestellung VALUES (39, 'Neue Bestellung', 26.48, '2024-09-23', 0, 22, NULL);


--
-- TOC entry 4869 (class 0 OID 16420)
-- Dependencies: 217
-- Data for Name: kunde; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.kunde VALUES (18, 'Schmidt', 'Peter', 'Musterstraße', '10', '12345', 'peter.schmidt@musterfirma.de', '+49 1234567890', 1, 'Privatkunde');
INSERT INTO public.kunde VALUES (20, 'Müller', 'Anna', 'Blumenweg', '5', '54321', 'anna.mueller@beispielfirma.com', '+49 9876543210', 2, 'Geschaeftskunde');
INSERT INTO public.kunde VALUES (21, 'Maier', 'Franz', 'Hauptstraße', '22', '98765', 'franz.maier@firma.com', '+49 0123456789', 3, 'Grosskunde');
INSERT INTO public.kunde VALUES (22, 'König', 'Sofia', 'Parkweg', '1B', '09876', 'sophia.koenig@firma-musterstadt.de', '+49 7412345678', 4, 'Neukunde');
INSERT INTO public.kunde VALUES (23, 'Becker', 'Michael', 'Amselweg', '9', '78954', 'michael.becker@firma-ag.de', '', 5, 'Stammkunde');
INSERT INTO public.kunde VALUES (24, 'Schmidt', 'Peter', 'Musterstraße', '10', '12345', 'peter.schmidt@musterfirma.de', '+49 1234567890', 2, 'Geschaeftskunde');


--
-- TOC entry 4873 (class 0 OID 16440)
-- Dependencies: 221
-- Data for Name: kundentyp; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.kundentyp VALUES (1, 1, 'Privatkunde', 'Einzelperson, die Produkte oder Dienstleistungen für den persönlichen Gebrauch kauft.');
INSERT INTO public.kundentyp VALUES (2, 2, 'Geschäftskunde', 'Unternehmen oder Organisation, die Produkte oder Dienstleistungen für geschäftliche Zwecke kauft.');
INSERT INTO public.kundentyp VALUES (3, 3, 'Großkunde', 'Kunde mit besonders hohem Bestellvolumen oder Umsatz, der möglicherweise spezielle Konditionen erhält.');
INSERT INTO public.kundentyp VALUES (4, 4, 'Neukunde', 'Kunde, der zum ersten Mal eine Bestellung aufgibt.');
INSERT INTO public.kundentyp VALUES (5, 5, 'Stammkunde', 'Kunde, der regelmäßig Bestellungen aufgibt und möglicherweise ein Treueprogramm nutzt.');


--
-- TOC entry 4875 (class 0 OID 16455)
-- Dependencies: 223
-- Data for Name: produkt; Type: TABLE DATA; Schema: public; Owner: postgres
--

INSERT INTO public.produkt VALUES (2, 'Tulpen', 'Bunte Tulpenzwiebeln', 41, 5.99);
INSERT INTO public.produkt VALUES (7, 'Petunien', 'Hängende Petunien in Ampel', 12, 7.99);
INSERT INTO public.produkt VALUES (10, 'Erdbeerpflanzen', 'Süße Erdbeeren im Topf', 32, 5.49);
INSERT INTO public.produkt VALUES (9, 'Basilikum', 'Frisches Basilikum im Topf', 69, 2.99);
INSERT INTO public.produkt VALUES (8, 'Tomatenpflanzen', 'Cherrytomaten im Topf', 52, 4.99);
INSERT INTO public.produkt VALUES (11, 'Kaktus', 'Aus der Wüste, braucht nur wenig Wasser.', 23, 19.99);
INSERT INTO public.produkt VALUES (1, 'Rosen', 'Rote Rosen im Topf', 6, 12.99);
INSERT INTO public.produkt VALUES (4, 'Sonnenblumen', 'Fröhliche Sonnenblumenkerne', 72, 3.99);
INSERT INTO public.produkt VALUES (5, 'Lavendel', 'Duftiger Lavendel im Topf', 21, 8.99);
INSERT INTO public.produkt VALUES (3, 'Orchideen', 'Elegante Orchideen im Glas', 7, 19.99);
INSERT INTO public.produkt VALUES (6, 'Geranien', 'Robuste Geranien in verschiedenen Farben', 31, 6.49);


--
-- TOC entry 4888 (class 0 OID 0)
-- Dependencies: 224
-- Name: bestellposition_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.bestellposition_id_seq', 76, true);


--
-- TOC entry 4889 (class 0 OID 0)
-- Dependencies: 218
-- Name: bestellung_bestellnummer_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.bestellung_bestellnummer_seq', 39, true);


--
-- TOC entry 4890 (class 0 OID 0)
-- Dependencies: 216
-- Name: kunde_kundennummer_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.kunde_kundennummer_seq', 24, true);


--
-- TOC entry 4891 (class 0 OID 0)
-- Dependencies: 220
-- Name: kundentyp_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.kundentyp_id_seq', 5, true);


--
-- TOC entry 4892 (class 0 OID 0)
-- Dependencies: 222
-- Name: produkt_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.produkt_id_seq', 12, true);


--
-- TOC entry 4720 (class 2606 OID 16484)
-- Name: bestellposition bestellposition_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bestellposition
    ADD CONSTRAINT bestellposition_pkey PRIMARY KEY (id);


--
-- TOC entry 4713 (class 2606 OID 16432)
-- Name: bestellung bestellung_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bestellung
    ADD CONSTRAINT bestellung_pkey PRIMARY KEY (bestellnummer);


--
-- TOC entry 4711 (class 2606 OID 16425)
-- Name: kunde kunde_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kunde
    ADD CONSTRAINT kunde_pkey PRIMARY KEY (kundennummer);


--
-- TOC entry 4716 (class 2606 OID 16445)
-- Name: kundentyp kundentyp_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kundentyp
    ADD CONSTRAINT kundentyp_pkey PRIMARY KEY (id);


--
-- TOC entry 4718 (class 2606 OID 16460)
-- Name: produkt produkt_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.produkt
    ADD CONSTRAINT produkt_pkey PRIMARY KEY (id);


--
-- TOC entry 4714 (class 1259 OID 16438)
-- Name: fki_fk_bestellung_kunde; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_fk_bestellung_kunde ON public.bestellung USING btree (kundennummer);


--
-- TOC entry 4709 (class 1259 OID 16451)
-- Name: fki_fk_kunde_kundentyp; Type: INDEX; Schema: public; Owner: postgres
--

CREATE INDEX fki_fk_kunde_kundentyp ON public.kunde USING btree (kundentyp_id);


--
-- TOC entry 4722 (class 2606 OID 16433)
-- Name: bestellung fk_bestellung_kunde; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bestellung
    ADD CONSTRAINT fk_bestellung_kunde FOREIGN KEY (kundennummer) REFERENCES public.kunde(kundennummer);


--
-- TOC entry 4723 (class 2606 OID 16485)
-- Name: bestellposition fk_bestellungsposition_bestellung; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bestellposition
    ADD CONSTRAINT fk_bestellungsposition_bestellung FOREIGN KEY (bestellung_id) REFERENCES public.bestellung(bestellnummer);


--
-- TOC entry 4724 (class 2606 OID 16490)
-- Name: bestellposition fk_bestellungsposition_produkt; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bestellposition
    ADD CONSTRAINT fk_bestellungsposition_produkt FOREIGN KEY (produkt_id) REFERENCES public.produkt(id);


--
-- TOC entry 4721 (class 2606 OID 16446)
-- Name: kunde fk_kunde_kundentyp; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.kunde
    ADD CONSTRAINT fk_kunde_kundentyp FOREIGN KEY (kundentyp_id) REFERENCES public.kundentyp(id) NOT VALID;


-- Completed on 2024-09-28 15:33:37

--
-- PostgreSQL database dump complete
--

