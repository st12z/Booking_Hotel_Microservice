--
-- PostgreSQL database dump
--

-- Dumped from database version 17.2
-- Dumped by pg_dump version 17.2

-- Started on 2025-07-21 15:29:36

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

--
-- TOC entry 3 (class 3079 OID 27761)
-- Name: postgis; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA public;


--
-- TOC entry 6190 (class 0 OID 0)
-- Dependencies: 3
-- Name: EXTENSION postgis; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION postgis IS 'PostGIS geometry and geography spatial types and functions';


--
-- TOC entry 2 (class 3079 OID 24944)
-- Name: unaccent; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS unaccent WITH SCHEMA public;


--
-- TOC entry 6191 (class 0 OID 0)
-- Dependencies: 2
-- Name: EXTENSION unaccent; Type: COMMENT; Schema: -; Owner:
--

COMMENT ON EXTENSION unaccent IS 'text search dictionary that removes accents';


--
-- TOC entry 1762 (class 1247 OID 37626)
-- Name: car_booking_status; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.car_booking_status AS ENUM (
    'PENDING',
    'CONFIRMED',
    'CANCELLED'
);


ALTER TYPE public.car_booking_status OWNER TO postgres;

--
-- TOC entry 1756 (class 1247 OID 37610)
-- Name: car_status; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.car_status AS ENUM (
    'AVAILABLE',
    'BUSY',
    'INACTIVE'
);


ALTER TYPE public.car_status OWNER TO postgres;

--
-- TOC entry 1765 (class 1247 OID 37635)
-- Name: car_type; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.car_type AS ENUM (
    'BUS',
    'SEAT_4',
    'SEAT_7',
    'LIMOUSINE',
    'TAXI'
);


ALTER TYPE public.car_type OWNER TO postgres;

--
-- TOC entry 1759 (class 1247 OID 37618)
-- Name: driver_status; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.driver_status AS ENUM (
    'ACTIVE',
    'OFFLINE',
    'BUSY'
);


ALTER TYPE public.driver_status OWNER TO postgres;

--
-- TOC entry 1678 (class 1247 OID 16410)
-- Name: property_type_enum; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.property_type_enum AS ENUM (
    'Hotel',
    'Villa',
    'Apartment',
    'Resort',
    'Homestay'
);


ALTER TYPE public.property_type_enum OWNER TO postgres;

--
-- TOC entry 1693 (class 1247 OID 16662)
-- Name: room_status; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.room_status AS ENUM (
    'available',
    'booked'
);


ALTER TYPE public.room_status OWNER TO postgres;

--
-- TOC entry 1822 (class 1247 OID 54097)
-- Name: suspicious_type_enum; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.suspicious_type_enum AS ENUM (
    'AMOUNT',
    'FREQUENCY',
    'OTHER'
);


ALTER TYPE public.suspicious_type_enum OWNER TO postgres;

--
-- TOC entry 1807 (class 1247 OID 45866)
-- Name: transaction_type_enum; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.transaction_type_enum AS ENUM (
    'PAYMENT',
    'REFUND'
);


ALTER TYPE public.transaction_type_enum OWNER TO postgres;

--
-- TOC entry 1681 (class 1247 OID 16422)
-- Name: trip_type_enum; Type: TYPE; Schema: public; Owner: postgres
--

CREATE TYPE public.trip_type_enum AS ENUM (
    'Beach',
    'Mountain',
    'Outdoors'
);


ALTER TYPE public.trip_type_enum OWNER TO postgres;

SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- TOC entry 256 (class 1259 OID 45506)
-- Name: bill; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.bill (
                             id integer NOT NULL,
                             first_name character varying(255),
                             last_name character varying(255),
                             email character varying(255),
                             phone_number character varying(255),
                             property_id integer,
                             district character varying(255),
                             city character varying(255),
                             country character varying(255),
                             address_detail character varying(255),
                             origin_total_payment integer,
                             price_promotion integer,
                             discount_hotel integer,
                             discount_car integer,
                             created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                             updated_at timestamp without time zone,
                             special_message text,
                             user_email character varying(255),
                             booking_for_who integer NOT NULL,
                             is_business_trip integer NOT NULL,
                             is_shuttle_service integer NOT NULL,
                             bill_status character varying(255),
                             bill_code character varying(255),
                             discount_car_id integer NOT NULL,
                             discount_hotel_id integer NOT NULL,
                             new_total_payment integer,
                             created_by character varying(255),
                             updated_by character varying(255),
                             CONSTRAINT bill_bill_status_check CHECK (((bill_status)::text = ANY ((ARRAY['PENDING'::character varying, 'SUCCESS'::character varying, 'CANCEL'::character varying])::text[])))
);


ALTER TABLE public.bill OWNER TO postgres;

--
-- TOC entry 255 (class 1259 OID 45505)
-- Name: bill_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.bill_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.bill_id_seq OWNER TO postgres;

--
-- TOC entry 6192 (class 0 OID 0)
-- Dependencies: 255
-- Name: bill_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.bill_id_seq OWNED BY public.bill.id;


--
-- TOC entry 260 (class 1259 OID 45541)
-- Name: booking_cars; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.booking_cars (
                                     id integer NOT NULL,
                                     bill_id integer,
                                     vehicle_id integer,
                                     pickup_location character varying(255),
                                     dropoff_location character varying(255),
                                     pickup_time timestamp without time zone,
                                     created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                                     updated_at timestamp without time zone,
                                     price_booking integer NOT NULL,
                                     created_by character varying(255),
                                     updated_by character varying(255)
);


ALTER TABLE public.booking_cars OWNER TO postgres;

--
-- TOC entry 259 (class 1259 OID 45540)
-- Name: booking_cars_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.booking_cars_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.booking_cars_id_seq OWNER TO postgres;

--
-- TOC entry 6193 (class 0 OID 0)
-- Dependencies: 259
-- Name: booking_cars_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.booking_cars_id_seq OWNED BY public.booking_cars.id;


--
-- TOC entry 258 (class 1259 OID 45521)
-- Name: booking_rooms; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.booking_rooms (
                                      id integer NOT NULL,
                                      room_type_id integer,
                                      quantity_rooms integer,
                                      bill_id integer,
                                      check_in timestamp without time zone,
                                      check_out timestamp without time zone,
                                      day_stays integer,
                                      origin_payment integer,
                                      promotion integer,
                                      new_payment integer,
                                      created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                                      updated_at timestamp without time zone,
                                      num_rooms integer[],
                                      property_id integer,
                                      created_by character varying(255),
                                      updated_by character varying(255)
);


ALTER TABLE public.booking_rooms OWNER TO postgres;

--
-- TOC entry 257 (class 1259 OID 45520)
-- Name: booking_rooms_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.booking_rooms_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.booking_rooms_id_seq OWNER TO postgres;

--
-- TOC entry 6194 (class 0 OID 0)
-- Dependencies: 257
-- Name: booking_rooms_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.booking_rooms_id_seq OWNED BY public.booking_rooms.id;


--
-- TOC entry 269 (class 1259 OID 45636)
-- Name: chat_images; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.chat_images (
                                    chat_id integer NOT NULL,
                                    image_url character varying(255)
);


ALTER TABLE public.chat_images OWNER TO postgres;

--
-- TOC entry 268 (class 1259 OID 45626)
-- Name: chats; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.chats (
                              id integer NOT NULL,
                              content character varying(255),
                              images jsonb,
                              room_chat_id integer,
                              user_send integer NOT NULL,
                              created_at timestamp(6) without time zone,
                              created_by character varying(255),
                              updated_at timestamp(6) without time zone,
                              updated_by character varying(255)
);


ALTER TABLE public.chats OWNER TO postgres;

--
-- TOC entry 267 (class 1259 OID 45625)
-- Name: chats_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.chats_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.chats_id_seq OWNER TO postgres;

--
-- TOC entry 6195 (class 0 OID 0)
-- Dependencies: 267
-- Name: chats_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.chats_id_seq OWNED BY public.chats.id;


--
-- TOC entry 220 (class 1259 OID 16390)
-- Name: cities; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.cities (
                               id integer NOT NULL,
                               name character varying(255) NOT NULL,
                               image character varying(255),
                               created_at timestamp(6) without time zone,
                               created_by character varying(255),
                               updated_at timestamp(6) without time zone,
                               updated_by character varying(255),
                               slug character varying(255),
                               latitude_center numeric(50,10),
                               longitude_center numeric(50,10),
                               geog public.geography(Point,4326)
);


ALTER TABLE public.cities OWNER TO postgres;

--
-- TOC entry 219 (class 1259 OID 16389)
-- Name: cities_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.cities_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.cities_id_seq OWNER TO postgres;

--
-- TOC entry 6196 (class 0 OID 0)
-- Dependencies: 219
-- Name: cities_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.cities_id_seq OWNED BY public.cities.id;


--
-- TOC entry 230 (class 1259 OID 24897)
-- Name: discount; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.discount (
                                 id integer NOT NULL,
                                 code character varying(255),
                                 discount_type character varying(255) NOT NULL,
                                 discount_value integer NOT NULL,
                                 min_booking_amount integer DEFAULT 0,
                                 start_date timestamp without time zone NOT NULL,
                                 end_date timestamp without time zone NOT NULL,
                                 is_active boolean DEFAULT true,
                                 quantity integer,
                                 image character varying(255),
                                 created_at timestamp(6) without time zone,
                                 created_by character varying(255),
                                 updated_at timestamp(6) without time zone,
                                 updated_by character varying(255),
                                 CONSTRAINT discount_discount_type_check CHECK (((discount_type)::text = ANY (ARRAY[('PERCENT'::character varying)::text, ('FIXED'::character varying)::text])))
);


ALTER TABLE public.discount OWNER TO postgres;

--
-- TOC entry 248 (class 1259 OID 37415)
-- Name: discount_cars; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.discount_cars (
                                      id integer NOT NULL,
                                      code character varying(255) NOT NULL,
                                      description text,
                                      discount_value integer,
                                      start_date timestamp without time zone,
                                      end_date timestamp without time zone,
                                      images character varying(255),
                                      is_active boolean DEFAULT true,
                                      created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                                      updated_at timestamp without time zone,
                                      updated_by character varying(255),
                                      created_by character varying(255),
                                      quantity integer
);


ALTER TABLE public.discount_cars OWNER TO postgres;

--
-- TOC entry 247 (class 1259 OID 37414)
-- Name: discount_cars_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.discount_cars_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.discount_cars_id_seq OWNER TO postgres;

--
-- TOC entry 6197 (class 0 OID 0)
-- Dependencies: 247
-- Name: discount_cars_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.discount_cars_id_seq OWNED BY public.discount_cars.id;


--
-- TOC entry 229 (class 1259 OID 24896)
-- Name: discount_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.discount_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.discount_id_seq OWNER TO postgres;

--
-- TOC entry 6198 (class 0 OID 0)
-- Dependencies: 229
-- Name: discount_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.discount_id_seq OWNED BY public.discount.id;


--
-- TOC entry 254 (class 1259 OID 37516)
-- Name: drivers; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.drivers (
                                id integer NOT NULL,
                                name character varying(255),
                                phone_number character varying(255),
                                vehicle_id integer,
                                status character varying(255),
                                created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                                updated_at timestamp without time zone,
                                updated_by character varying(255),
                                created_by character varying(255)
);


ALTER TABLE public.drivers OWNER TO postgres;

--
-- TOC entry 253 (class 1259 OID 37515)
-- Name: drivers_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.drivers_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.drivers_id_seq OWNER TO postgres;

--
-- TOC entry 6199 (class 0 OID 0)
-- Dependencies: 253
-- Name: drivers_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.drivers_id_seq OWNED BY public.drivers.id;


--
-- TOC entry 275 (class 1259 OID 45698)
-- Name: facilities; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.facilities (
                                   id integer NOT NULL,
                                   name character varying(255)
);


ALTER TABLE public.facilities OWNER TO postgres;

--
-- TOC entry 274 (class 1259 OID 45697)
-- Name: facilities_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.facilities_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.facilities_id_seq OWNER TO postgres;

--
-- TOC entry 6200 (class 0 OID 0)
-- Dependencies: 274
-- Name: facilities_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.facilities_id_seq OWNED BY public.facilities.id;


--
-- TOC entry 271 (class 1259 OID 45650)
-- Name: notifications; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.notifications (
                                      id integer NOT NULL,
                                      content character varying(255),
                                      created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                                      created_by character varying(255),
                                      updated_at timestamp(6) without time zone,
                                      updated_by character varying(255)
);


ALTER TABLE public.notifications OWNER TO postgres;

--
-- TOC entry 270 (class 1259 OID 45649)
-- Name: notifications_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.notifications_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.notifications_id_seq OWNER TO postgres;

--
-- TOC entry 6201 (class 0 OID 0)
-- Dependencies: 270
-- Name: notifications_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.notifications_id_seq OWNED BY public.notifications.id;


--
-- TOC entry 261 (class 1259 OID 45568)
-- Name: num_rooms; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.num_rooms (
                                  booking_room_id integer NOT NULL,
                                  room_number integer
);


ALTER TABLE public.num_rooms OWNER TO postgres;

--
-- TOC entry 279 (class 1259 OID 45872)
-- Name: payment_transaction; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.payment_transaction (
                                            id integer NOT NULL,
                                            vnp_txn_ref character varying(255) NOT NULL,
                                            vnp_amount integer NOT NULL,
                                            vnp_transaction_no character varying(255) NOT NULL,
                                            vnp_transaction_date character varying(255) NOT NULL,
                                            vnp_response_code character varying(255),
                                            created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                                            created_by character varying(255),
                                            updated_at timestamp with time zone,
                                            updated_by character varying(255),
                                            transaction_type character varying(255),
                                            ip_address character varying(255),
                                            user_id integer
);


ALTER TABLE public.payment_transaction OWNER TO postgres;

--
-- TOC entry 278 (class 1259 OID 45871)
-- Name: payment_transaction_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.payment_transaction_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.payment_transaction_id_seq OWNER TO postgres;

--
-- TOC entry 6202 (class 0 OID 0)
-- Dependencies: 278
-- Name: payment_transaction_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.payment_transaction_id_seq OWNED BY public.payment_transaction.id;


--
-- TOC entry 241 (class 1259 OID 37212)
-- Name: permissions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.permissions (
                                    id integer NOT NULL,
                                    name character varying(255),
                                    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                                    updated_at timestamp without time zone,
                                    created_by character varying(255),
                                    updated_by character varying(255)
);


ALTER TABLE public.permissions OWNER TO postgres;

--
-- TOC entry 240 (class 1259 OID 37211)
-- Name: permissions_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.permissions_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.permissions_id_seq OWNER TO postgres;

--
-- TOC entry 6203 (class 0 OID 0)
-- Dependencies: 240
-- Name: permissions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.permissions_id_seq OWNED BY public.permissions.id;


--
-- TOC entry 222 (class 1259 OID 16430)
-- Name: properties; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.properties (
                                   id integer NOT NULL,
                                   name character varying(255) NOT NULL,
                                   city_id integer,
                                   property_type character varying(255) NOT NULL,
                                   rating_star integer,
                                   address text NOT NULL,
                                   latitude numeric(50,10) NOT NULL,
                                   longitude numeric(50,10) NOT NULL,
                                   overview text,
                                   avg_review_score double precision DEFAULT 0,
                                   created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                                   updated_at timestamp without time zone,
                                   created_by character varying(255),
                                   updated_by character varying(255),
                                   slug character varying(255),
                                   geog public.geography(Point,4326),
                                   distance_from_center double precision,
                                   distance_from_trip double precision
);


ALTER TABLE public.properties OWNER TO postgres;

--
-- TOC entry 221 (class 1259 OID 16429)
-- Name: properties_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.properties_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.properties_id_seq OWNER TO postgres;

--
-- TOC entry 6204 (class 0 OID 0)
-- Dependencies: 221
-- Name: properties_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.properties_id_seq OWNED BY public.properties.id;


--
-- TOC entry 283 (class 1259 OID 54081)
-- Name: property_facilities; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.property_facilities (
                                            facility_id integer NOT NULL,
                                            property_id integer NOT NULL
);


ALTER TABLE public.property_facilities OWNER TO postgres;

--
-- TOC entry 277 (class 1259 OID 45722)
-- Name: property_images; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.property_images (
                                        id integer NOT NULL,
                                        property_id integer,
                                        image character varying(255)
);


ALTER TABLE public.property_images OWNER TO postgres;

--
-- TOC entry 276 (class 1259 OID 45721)
-- Name: property_images_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.property_images_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.property_images_id_seq OWNER TO postgres;

--
-- TOC entry 6205 (class 0 OID 0)
-- Dependencies: 276
-- Name: property_images_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.property_images_id_seq OWNED BY public.property_images.id;


--
-- TOC entry 287 (class 1259 OID 54122)
-- Name: property_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.property_type (
                                      id integer NOT NULL,
                                      name character varying(255) NOT NULL,
                                      created_at timestamp(6) without time zone,
                                      created_by character varying(255),
                                      updated_at timestamp(6) without time zone,
                                      updated_by character varying(255)
);


ALTER TABLE public.property_type OWNER TO postgres;

--
-- TOC entry 286 (class 1259 OID 54121)
-- Name: property_type_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.property_type_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.property_type_id_seq OWNER TO postgres;

--
-- TOC entry 6206 (class 0 OID 0)
-- Dependencies: 286
-- Name: property_type_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.property_type_id_seq OWNED BY public.property_type.id;


--
-- TOC entry 281 (class 1259 OID 45881)
-- Name: refund_bills; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.refund_bills (
                                     id integer NOT NULL,
                                     created_at timestamp(6) without time zone,
                                     created_by character varying(255),
                                     updated_at timestamp(6) without time zone,
                                     updated_by character varying(255),
                                     vnp_amount integer NOT NULL,
                                     vnp_bank_code character varying(255),
                                     vnp_command character varying(255),
                                     vnp_message character varying(255),
                                     vnp_order_info character varying(255),
                                     vnp_pay_date character varying(255),
                                     vnp_response_code character varying(255),
                                     vnp_response_id character varying(255),
                                     vnp_secure_hash character varying(255),
                                     vnp_tmn_code character varying(255),
                                     vnp_transaction_no character varying(255),
                                     vnp_transaction_status character varying(255),
                                     vnp_transaction_type character varying(255),
                                     vnp_txn_ref character varying(255),
                                     email character varying(255)
);


ALTER TABLE public.refund_bills OWNER TO postgres;

--
-- TOC entry 280 (class 1259 OID 45880)
-- Name: refund_bills_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.refund_bills ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.refund_bills_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 264 (class 1259 OID 45605)
-- Name: review_images; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.review_images (
                                      review_id integer NOT NULL,
                                      image_url character varying(255)
);


ALTER TABLE public.review_images OWNER TO postgres;

--
-- TOC entry 263 (class 1259 OID 45579)
-- Name: reviews; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.reviews (
                                id integer NOT NULL,
                                content text,
                                rating_property double precision,
                                rating_staff double precision,
                                rating_facilities double precision,
                                rating_clean double precision,
                                rating_comfort double precision,
                                rating_location double precision,
                                rating_wifi double precision,
                                created_at timestamp(6) without time zone,
                                created_by character varying(255),
                                updated_at timestamp(6) without time zone,
                                updated_by character varying(255),
                                property_id integer,
                                user_id integer
);


ALTER TABLE public.reviews OWNER TO postgres;

--
-- TOC entry 262 (class 1259 OID 45578)
-- Name: reviews_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.reviews_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.reviews_id_seq OWNER TO postgres;

--
-- TOC entry 6207 (class 0 OID 0)
-- Dependencies: 262
-- Name: reviews_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.reviews_id_seq OWNED BY public.reviews.id;


--
-- TOC entry 244 (class 1259 OID 37286)
-- Name: role_permissions; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.role_permissions (
                                         role_id integer NOT NULL,
                                         permission_id integer NOT NULL,
                                         created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                                         updated_at timestamp without time zone
);


ALTER TABLE public.role_permissions OWNER TO postgres;

--
-- TOC entry 239 (class 1259 OID 37186)
-- Name: roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roles (
                              id integer NOT NULL,
                              name character varying(255),
                              created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                              updated_at timestamp without time zone,
                              created_by character varying(255),
                              updated_by character varying(255)
);


ALTER TABLE public.roles OWNER TO postgres;

--
-- TOC entry 238 (class 1259 OID 37185)
-- Name: roles_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.roles_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.roles_id_seq OWNER TO postgres;

--
-- TOC entry 6208 (class 0 OID 0)
-- Dependencies: 238
-- Name: roles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.roles_id_seq OWNED BY public.roles.id;


--
-- TOC entry 266 (class 1259 OID 45619)
-- Name: room_chats; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.room_chats (
                                   id integer NOT NULL,
                                   created_at timestamp(6) without time zone,
                                   created_by character varying(255),
                                   updated_at timestamp(6) without time zone,
                                   updated_by character varying(255),
                                   user_aid integer,
                                   user_bid integer
);


ALTER TABLE public.room_chats OWNER TO postgres;

--
-- TOC entry 265 (class 1259 OID 45618)
-- Name: room_chats_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.room_chats_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.room_chats_id_seq OWNER TO postgres;

--
-- TOC entry 6209 (class 0 OID 0)
-- Dependencies: 265
-- Name: room_chats_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.room_chats_id_seq OWNED BY public.room_chats.id;


--
-- TOC entry 226 (class 1259 OID 16483)
-- Name: room_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.room_type (
                                  id integer NOT NULL,
                                  property_id integer,
                                  name character varying(255),
                                  price integer,
                                  max_guests integer,
                                  num_beds integer,
                                  created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                                  area integer,
                                  discount integer,
                                  created_by character varying(255),
                                  updated_at timestamp(6) without time zone,
                                  updated_by character varying(255),
                                  status boolean DEFAULT false
);


ALTER TABLE public.room_type OWNER TO postgres;

--
-- TOC entry 225 (class 1259 OID 16482)
-- Name: room_type_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.room_type_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.room_type_id_seq OWNER TO postgres;

--
-- TOC entry 6210 (class 0 OID 0)
-- Dependencies: 225
-- Name: room_type_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.room_type_id_seq OWNED BY public.room_type.id;


--
-- TOC entry 228 (class 1259 OID 16668)
-- Name: rooms; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.rooms (
                              id integer NOT NULL,
                              room_number integer,
                              status character varying(255) DEFAULT 'available'::public.room_status NOT NULL,
                              property_id integer,
                              room_type_id integer,
                              created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                              created_by character varying(255),
                              updated_at timestamp(6) without time zone,
                              updated_by character varying(255),
                              check_in timestamp(6) without time zone,
                              check_out timestamp(6) without time zone
);


ALTER TABLE public.rooms OWNER TO postgres;

--
-- TOC entry 227 (class 1259 OID 16667)
-- Name: rooms_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.rooms_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.rooms_id_seq OWNER TO postgres;

--
-- TOC entry 6211 (class 0 OID 0)
-- Dependencies: 227
-- Name: rooms_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.rooms_id_seq OWNED BY public.rooms.id;


--
-- TOC entry 282 (class 1259 OID 45889)
-- Name: roomtypes_facilities; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.roomtypes_facilities (
                                             facility_id integer NOT NULL,
                                             room_type_id integer NOT NULL
);


ALTER TABLE public.roomtypes_facilities OWNER TO postgres;

--
-- TOC entry 285 (class 1259 OID 54104)
-- Name: suspicious_payment_log; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.suspicious_payment_log (
                                               id integer NOT NULL,
                                               user_id integer,
                                               amount integer,
                                               ip_address character varying(255),
                                               created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                                               suspicious_reason character varying(255),
                                               suspicious_type character varying(255),
                                               bill_code character varying(255),
                                               created_by character varying(255),
                                               updated_at timestamp(6) without time zone,
                                               updated_by character varying(255)
);


ALTER TABLE public.suspicious_payment_log OWNER TO postgres;

--
-- TOC entry 284 (class 1259 OID 54103)
-- Name: suspicious_payment_log_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.suspicious_payment_log_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.suspicious_payment_log_id_seq OWNER TO postgres;

--
-- TOC entry 6212 (class 0 OID 0)
-- Dependencies: 284
-- Name: suspicious_payment_log_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.suspicious_payment_log_id_seq OWNED BY public.suspicious_payment_log.id;


--
-- TOC entry 224 (class 1259 OID 16449)
-- Name: trip; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trip (
                             id integer NOT NULL,
                             name character varying(255) NOT NULL,
                             trip_type character varying(255) NOT NULL,
                             city_id integer,
                             latitude numeric(50,10) NOT NULL,
                             longitude numeric(50,10) NOT NULL,
                             image character varying(255),
                             created_at timestamp(6) without time zone,
                             created_by character varying(255),
                             updated_at timestamp(6) without time zone,
                             updated_by character varying(255),
                             geog public.geography(Point,4326),
                             slug character varying(255)
);


ALTER TABLE public.trip OWNER TO postgres;

--
-- TOC entry 223 (class 1259 OID 16448)
-- Name: trip_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trip_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.trip_id_seq OWNER TO postgres;

--
-- TOC entry 6213 (class 0 OID 0)
-- Dependencies: 223
-- Name: trip_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trip_id_seq OWNED BY public.trip.id;


--
-- TOC entry 289 (class 1259 OID 54143)
-- Name: trip_type; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.trip_type (
                                  id integer NOT NULL,
                                  name character varying(255) NOT NULL,
                                  icon character varying(255) NOT NULL,
                                  created_at timestamp(6) without time zone,
                                  created_by character varying(255),
                                  updated_at timestamp(6) without time zone,
                                  updated_by character varying(255)
);


ALTER TABLE public.trip_type OWNER TO postgres;

--
-- TOC entry 288 (class 1259 OID 54142)
-- Name: trip_type_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.trip_type_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.trip_type_id_seq OWNER TO postgres;

--
-- TOC entry 6214 (class 0 OID 0)
-- Dependencies: 288
-- Name: trip_type_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.trip_type_id_seq OWNED BY public.trip_type.id;


--
-- TOC entry 250 (class 1259 OID 37465)
-- Name: user_discount_cars; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_discount_cars (
                                           id integer NOT NULL,
                                           discount_car_id integer,
                                           email character varying(255)
);


ALTER TABLE public.user_discount_cars OWNER TO postgres;

--
-- TOC entry 249 (class 1259 OID 37464)
-- Name: user_discount_cars_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

ALTER TABLE public.user_discount_cars ALTER COLUMN id ADD GENERATED BY DEFAULT AS IDENTITY (
    SEQUENCE NAME public.user_discount_cars_id_seq
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1
);


--
-- TOC entry 245 (class 1259 OID 37303)
-- Name: user_discounts; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_discounts (
                                       id integer NOT NULL,
                                       created_at timestamp(6) without time zone,
                                       created_by character varying(255),
                                       updated_at timestamp(6) without time zone,
                                       updated_by character varying(255),
                                       discount_id integer,
                                       email character varying(255)
);


ALTER TABLE public.user_discounts OWNER TO postgres;

--
-- TOC entry 246 (class 1259 OID 37310)
-- Name: user_discounts_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_discounts_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.user_discounts_seq OWNER TO postgres;

--
-- TOC entry 243 (class 1259 OID 37268)
-- Name: user_roles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_roles (
                                   id integer NOT NULL,
                                   role_id integer,
                                   user_id integer,
                                   created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                                   updated_at timestamp without time zone
);


ALTER TABLE public.user_roles OWNER TO postgres;

--
-- TOC entry 242 (class 1259 OID 37267)
-- Name: user_roles_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_roles_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.user_roles_id_seq OWNER TO postgres;

--
-- TOC entry 6215 (class 0 OID 0)
-- Dependencies: 242
-- Name: user_roles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.user_roles_id_seq OWNED BY public.user_roles.id;


--
-- TOC entry 273 (class 1259 OID 45673)
-- Name: user_visits; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.user_visits (
                                    id integer NOT NULL,
                                    user_id integer,
                                    accessed_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


ALTER TABLE public.user_visits OWNER TO postgres;

--
-- TOC entry 272 (class 1259 OID 45672)
-- Name: user_visits_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.user_visits_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.user_visits_id_seq OWNER TO postgres;

--
-- TOC entry 6216 (class 0 OID 0)
-- Dependencies: 272
-- Name: user_visits_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.user_visits_id_seq OWNED BY public.user_visits.id;


--
-- TOC entry 237 (class 1259 OID 37168)
-- Name: users; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.users (
                              id integer NOT NULL,
                              email character varying(255),
                              password character varying(512),
                              first_name character varying(255),
                              last_name character varying(255),
                              address character varying(255),
                              avatar text,
                              created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                              updated_at timestamp without time zone,
                              created_by character varying(255),
                              updated_by character varying(255),
                              birthday date,
                              city character varying(255),
                              district character varying(255),
                              gender character varying(255),
                              phone_number character varying(255),
                              village character varying(255),
                              send_email boolean
);


ALTER TABLE public.users OWNER TO postgres;

--
-- TOC entry 236 (class 1259 OID 37167)
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.users_id_seq OWNER TO postgres;

--
-- TOC entry 6217 (class 0 OID 0)
-- Dependencies: 236
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- TOC entry 252 (class 1259 OID 37504)
-- Name: vehicles; Type: TABLE; Schema: public; Owner: postgres
--

CREATE TABLE public.vehicles (
                                 id integer NOT NULL,
                                 license_plate character varying(255) NOT NULL,
                                 car_type character varying(255) NOT NULL,
                                 images text,
                                 latitude double precision,
                                 longitude double precision,
                                 discount integer,
                                 price integer,
                                 created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                                 updated_at timestamp without time zone,
                                 updated_by character varying(255),
                                 created_by character varying(255),
                                 driver_id integer,
                                 status character varying(255),
                                 quantity integer,
                                 star integer,
                                 CONSTRAINT vehicles_status_check CHECK (((status)::text = ANY ((ARRAY['AVAILABLE'::character varying, 'BUSY'::character varying, 'INACTIVE'::character varying])::text[])))
);


ALTER TABLE public.vehicles OWNER TO postgres;

--
-- TOC entry 251 (class 1259 OID 37503)
-- Name: vehicles_id_seq; Type: SEQUENCE; Schema: public; Owner: postgres
--

CREATE SEQUENCE public.vehicles_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


ALTER SEQUENCE public.vehicles_id_seq OWNER TO postgres;

--
-- TOC entry 6218 (class 0 OID 0)
-- Dependencies: 251
-- Name: vehicles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: postgres
--

ALTER SEQUENCE public.vehicles_id_seq OWNED BY public.vehicles.id;


--
-- TOC entry 5840 (class 2604 OID 45509)
-- Name: bill id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bill ALTER COLUMN id SET DEFAULT nextval('public.bill_id_seq'::regclass);


--
-- TOC entry 5844 (class 2604 OID 45544)
-- Name: booking_cars id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.booking_cars ALTER COLUMN id SET DEFAULT nextval('public.booking_cars_id_seq'::regclass);


--
-- TOC entry 5842 (class 2604 OID 45524)
-- Name: booking_rooms id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.booking_rooms ALTER COLUMN id SET DEFAULT nextval('public.booking_rooms_id_seq'::regclass);


--
-- TOC entry 5848 (class 2604 OID 45629)
-- Name: chats id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chats ALTER COLUMN id SET DEFAULT nextval('public.chats_id_seq'::regclass);


--
-- TOC entry 5810 (class 2604 OID 16393)
-- Name: cities id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cities ALTER COLUMN id SET DEFAULT nextval('public.cities_id_seq'::regclass);


--
-- TOC entry 5821 (class 2604 OID 24900)
-- Name: discount id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discount ALTER COLUMN id SET DEFAULT nextval('public.discount_id_seq'::regclass);


--
-- TOC entry 5833 (class 2604 OID 37418)
-- Name: discount_cars id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discount_cars ALTER COLUMN id SET DEFAULT nextval('public.discount_cars_id_seq'::regclass);


--
-- TOC entry 5838 (class 2604 OID 37519)
-- Name: drivers id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.drivers ALTER COLUMN id SET DEFAULT nextval('public.drivers_id_seq'::regclass);


--
-- TOC entry 5853 (class 2604 OID 45701)
-- Name: facilities id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.facilities ALTER COLUMN id SET DEFAULT nextval('public.facilities_id_seq'::regclass);


--
-- TOC entry 5849 (class 2604 OID 45653)
-- Name: notifications id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notifications ALTER COLUMN id SET DEFAULT nextval('public.notifications_id_seq'::regclass);


--
-- TOC entry 5855 (class 2604 OID 45875)
-- Name: payment_transaction id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment_transaction ALTER COLUMN id SET DEFAULT nextval('public.payment_transaction_id_seq'::regclass);


--
-- TOC entry 5828 (class 2604 OID 37215)
-- Name: permissions id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.permissions ALTER COLUMN id SET DEFAULT nextval('public.permissions_id_seq'::regclass);


--
-- TOC entry 5811 (class 2604 OID 16433)
-- Name: properties id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.properties ALTER COLUMN id SET DEFAULT nextval('public.properties_id_seq'::regclass);


--
-- TOC entry 5854 (class 2604 OID 45725)
-- Name: property_images id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.property_images ALTER COLUMN id SET DEFAULT nextval('public.property_images_id_seq'::regclass);


--
-- TOC entry 5859 (class 2604 OID 54125)
-- Name: property_type id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.property_type ALTER COLUMN id SET DEFAULT nextval('public.property_type_id_seq'::regclass);


--
-- TOC entry 5846 (class 2604 OID 45582)
-- Name: reviews id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reviews ALTER COLUMN id SET DEFAULT nextval('public.reviews_id_seq'::regclass);


--
-- TOC entry 5826 (class 2604 OID 37189)
-- Name: roles id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles ALTER COLUMN id SET DEFAULT nextval('public.roles_id_seq'::regclass);


--
-- TOC entry 5847 (class 2604 OID 45622)
-- Name: room_chats id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room_chats ALTER COLUMN id SET DEFAULT nextval('public.room_chats_id_seq'::regclass);


--
-- TOC entry 5815 (class 2604 OID 16486)
-- Name: room_type id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room_type ALTER COLUMN id SET DEFAULT nextval('public.room_type_id_seq'::regclass);


--
-- TOC entry 5818 (class 2604 OID 16671)
-- Name: rooms id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rooms ALTER COLUMN id SET DEFAULT nextval('public.rooms_id_seq'::regclass);


--
-- TOC entry 5857 (class 2604 OID 54107)
-- Name: suspicious_payment_log id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.suspicious_payment_log ALTER COLUMN id SET DEFAULT nextval('public.suspicious_payment_log_id_seq'::regclass);


--
-- TOC entry 5814 (class 2604 OID 16452)
-- Name: trip id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trip ALTER COLUMN id SET DEFAULT nextval('public.trip_id_seq'::regclass);


--
-- TOC entry 5860 (class 2604 OID 54146)
-- Name: trip_type id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trip_type ALTER COLUMN id SET DEFAULT nextval('public.trip_type_id_seq'::regclass);


--
-- TOC entry 5830 (class 2604 OID 37271)
-- Name: user_roles id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles ALTER COLUMN id SET DEFAULT nextval('public.user_roles_id_seq'::regclass);


--
-- TOC entry 5851 (class 2604 OID 45676)
-- Name: user_visits id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_visits ALTER COLUMN id SET DEFAULT nextval('public.user_visits_id_seq'::regclass);


--
-- TOC entry 5824 (class 2604 OID 37171)
-- Name: users id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- TOC entry 5836 (class 2604 OID 37507)
-- Name: vehicles id; Type: DEFAULT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vehicles ALTER COLUMN id SET DEFAULT nextval('public.vehicles_id_seq'::regclass);


--
-- TOC entry 6151 (class 0 OID 45506)
-- Dependencies: 256
-- Data for Name: bill; Type: TABLE DATA; Schema: public; Owner: postgres
--


--
-- TOC entry 6164 (class 0 OID 45636)
-- Dependencies: 269
-- Data for Name: chat_images; Type: TABLE DATA; Schema: public; Owner: postgres
--

--
-- TOC entry 6163 (class 0 OID 45626)
-- Dependencies: 268
-- Data for Name: chats; Type: TABLE DATA; Schema: public; Owner: postgres
--
--
-- TOC entry 6120 (class 0 OID 16390)
-- Dependencies: 220
-- Data for Name: cities; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.cities (id, name, image, created_at, created_by, updated_at, updated_by, slug, latitude_center, longitude_center, geog) FROM stdin;
1	Hà Nội	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752488855/ecdf72e2-ffb5-47ea-8aec-f2b9c46db1e8?_a=DAGAACAWZAA0	2025-04-01 00:00:00	\N	2025-07-14 17:27:36.597777	manager@gmail.com	ha-noi	21.0330731767	105.8373925968	0101000020E6100000C94A1ED797755A40244DD47B77083540
7	Hà Giang	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752488880/8bef8713-18af-44f7-8319-5c600cb589ad?_a=DAGAACAWZAA0	2025-07-14 16:54:33.896573	manager@gmail.com	2025-07-14 17:28:02.144803	manager@gmail.com	ha-giang	22.8084537869	104.9770649959	0101000020E610000017EDCED3F6CE36404B2261C477C15240
2	Hồ Chí Minh	https://cf.bstatic.com/xdata/images/city/600x600/688893.jpg?k=d32ef7ff94e5d02b90908214fb2476185b62339549a1bd7544612bdac51fda31&o=	2025-04-01 00:00:00	\N	\N	\N	ho-chi-minh	16.0422635259	108.1994611431	0101000020E6100000FC9BABF8C30C5B40DD8D4DC8D10A3040
3	Đà Nẵng	https://vcdn1-dulich.vnecdn.net/2022/06/03/cau-vang-jpeg-mobile-4171-1654247848.jpg?w=0&h=0&q=100&dpr=1&fit=crop&s=xrjEn1shZLiHomFix1sHNQ	2025-04-01 00:00:00	\N	\N	\N	da-nang	12.2514058846	109.1891627966	0101000020E61000001541463E1B4C5B4006C2A845B8802840
4	Đà Lạt	https://namthientravel.com.vn/wp-content/uploads/2024/09/da-lat.jpg	2025-04-01 00:00:00	\N	\N	\N	da-lat	11.9408497291	108.4565548745	0101000020E6100000A4B3EF31381D5B40B3DE410EB7E12740
5	Nha Trang	https://images2.thanhnien.vn/zoom/1200_630/528068263637045248/2025/3/24/nha-trang-17428130131821343548929-60-0-1310-2000-crop-1742813226474598199942.jpg	2025-04-01 00:00:00	\N	\N	\N	nha-trang	10.8135268620	106.6041129155	0101000020E61000007ACA37C9A9A65A403E6AC59786A02540
\.


--
-- TOC entry 6130 (class 0 OID 24897)
-- Dependencies: 230
-- Data for Name: discount; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.discount (id, code, discount_type, discount_value, min_booking_amount, start_date, end_date, is_active, quantity, image, created_at, created_by, updated_at, updated_by) FROM stdin;
4	FIXED100K	FIXED	100000	80000	2025-04-01 00:00:00	2025-11-01 00:00:00	t	4	https://d1csarkz8obe9u.cloudfront.net/posterpreviews/hotel-airbnb-instagram-social-media-design-template-97a6e7529cee6913fa1dd744d39d6e95.jpg?ts=1698303516	2025-04-01 00:00:00	\N	2025-07-16 21:10:38.995968	\N
8	NEWUSER50	FIXED	50000	0	2025-04-01 00:00:00	2025-11-01 00:00:00	t	4	https://cdn.grabon.in/gograbon/images/web-images/uploads/1617092437646/hotel-offers.jpg	2025-04-01 00:00:00	\N	2025-07-16 21:10:40.289555	\N
2	DISCOUNT20	PERCENT	20	70000	2025-04-01 00:00:00	2025-11-01 00:00:00	t	4	https://i.pinimg.com/736x/44/7f/d4/447fd44e5a24ca4776de5f3782dc2e6e.jpg	2025-04-01 00:00:00	\N	2025-07-16 21:10:46.593507	\N
1	DISCOUNT10	PERCENT	10	50000	2025-04-01 00:00:00	2025-11-01 00:00:00	t	5	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752762709/f5296919-cd18-4ea4-a704-633f121cccc0?_a=DAGAACAWZAA0	2025-04-01 00:00:00	\N	2025-07-17 21:31:49.229673	manager@gmail.com
9	VIP100	FIXED	100000	120000	2025-04-01 00:00:00	2025-11-01 00:00:00	t	5	https://cdn.grabon.in/gograbon/images/web-images/uploads/1617092437646/hotel-offers.jpg	2025-04-01 00:00:00	\N	\N	\N
5	HOLIDAY30	PERCENT	30	100000	2025-04-05 00:00:00	2025-11-01 00:00:00	t	5	https://d1csarkz8obe9u.cloudfront.net/posterpreviews/hotel-airbnb-instagram-social-media-design-template-97a6e7529cee6913fa1dd744d39d6e95.jpg?ts=1698303516	2025-04-01 00:00:00	\N	\N	\N
10	FLASHSALE	PERCENT	50	150000	2025-04-15 00:00:00	2025-11-01 00:00:00	t	5	https://cdn.grabon.in/gograbon/images/web-images/uploads/1617092437646/hotel-offers.jpg	2025-04-01 00:00:00	\N	\N	\N
3	FIXED50K	FIXED	50000	40000	2025-04-01 00:00:00	2025-11-01 00:00:00	t	5	https://d1csarkz8obe9u.cloudfront.net/posterpreviews/hotel-airbnb-instagram-social-media-design-template-97a6e7529cee6913fa1dd744d39d6e95.jpg?ts=1698303516	2025-04-01 00:00:00	\N	\N	\N
7	SUMMER20	PERCENT	20	75000	2025-05-01 00:00:00	2025-11-01 00:00:00	t	5	https://img.freepik.com/free-psd/hotel-template-design_23-2151647862.jpg	2025-04-01 00:00:00	\N	2025-06-27 15:15:40.800743	\N
6	SPRINGSALE	PERCENT	15	6000	2025-04-10 00:00:00	2025-11-01 00:00:00	t	5	https://img.freepik.com/free-psd/hotel-template-design_23-2151647862.jpg	2025-04-01 00:00:00	\N	2025-06-27 15:15:41.917976	\N
11	DISCOUNT15	PERCENT	15	50000	2025-04-01 00:00:00	2025-11-01 00:00:00	t	5	https://marketplace.canva.com/EAGGejRWDp8/1/0/1131w/canva-red-and-cream-geometric-hotel-promotion-with-facilities-flyer-UGMYMCwL1ic.jpg	2025-04-01 00:00:00	\N	2025-07-16 21:01:52.850123	\N
12	DISCOUNT30	PERCENT	30	\N	2025-07-15 00:00:00	2025-07-17 00:00:00	t	5	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752762974/395ca886-259f-4cbf-a94e-71c4278722e4?_a=DAGAACAWZAA0	2025-07-17 21:36:13.948432	manager@gmail.com	\N	\N
\.


--
-- TOC entry 6143 (class 0 OID 37415)
-- Dependencies: 248
-- Data for Name: discount_cars; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.discount_cars (id, code, description, discount_value, start_date, end_date, images, is_active, created_at, updated_at, updated_by, created_by, quantity) FROM stdin;
2	DISCOUNT15	15% off for 4-seat cars	15	2025-02-01 00:00:00	2025-08-31 23:59:59	https://media.karousell.com/media/photos/products/2022/1/31/50_grabcar_and_grabtaxi_vouche_1643587281_56e981d0_progressive.jpg	t	2025-04-22 23:05:48.343219	2025-04-22 23:05:48.343219	\N	\N	5
3	DISCOUNT5	5% off for Limousine	5	2025-03-01 00:00:00	2025-06-30 23:59:59	https://media.karousell.com/media/photos/products/2023/2/12/grab_car_voucher_1676208781_d7f44356.jpg	t	2025-04-22 23:05:48.343219	2025-04-22 23:05:48.343219	\N	\N	2
4	SUMMER20	20% off for all cars in summer	20	2025-06-01 00:00:00	2025-08-31 23:59:59	https://media.karousell.com/media/photos/products/2021/1/18/grab_food_voucher_20_off_1610939407_da687a4a.jpg	t	2025-04-22 23:05:48.343219	2025-04-22 23:05:48.343219	\N	\N	3
5	NEWYEAR25	25% off for all bookings in New Year	25	2025-12-01 00:00:00	2025-12-31 23:59:59	https://tse1.mm.bing.net/th/id/OIP.qUlpceiXtdnlAJdCd7HjigHaHa?rs=1&pid=ImgDetMain	t	2025-04-22 23:05:48.343219	2025-04-22 23:05:48.343219	\N	\N	6
6	BIRTHDAY25	25% off for all bookings in BIRTHDAY	25	2025-12-01 00:00:00	2025-12-31 23:59:59	https://tse1.mm.bing.net/th/id/OIP.qUlpceiXtdnlAJdCd7HjigHaHa?rs=1&pid=ImgDetMain	t	2025-04-23 08:37:47.092175	2025-04-23 08:37:47.092175	\N	\N	3
7	SUMMER15	15% off for all bookings in SUMMER	15	2025-06-01 00:00:00	2025-08-31 23:59:59	https://tse1.mm.bing.net/th/id/OIP.qUlpceiXtdnlAJdCd7HjigHaHa?rs=1&pid=ImgDetMain	t	2025-04-23 08:37:47.092175	2025-04-23 08:37:47.092175	\N	\N	2
8	SUMMER10	10% off for all bookings in SUMMER	10	2025-06-01 00:00:00	2025-08-31 23:59:59	https://tse1.mm.bing.net/th/id/OIP.qUlpceiXtdnlAJdCd7HjigHaHa?rs=1&pid=ImgDetMain	t	2025-04-23 08:37:47.092175	2025-04-23 08:37:47.092175	\N	\N	6
9	SPRING15	15% off for all bookings in Spring	15	2025-03-01 00:00:00	2025-05-31 23:59:59	https://tse1.mm.bing.net/th/id/OIP.qUlpceiXtdnlAJdCd7HjigHaHa?rs=1&pid=ImgDetMain	t	2025-04-23 08:37:47.092175	2025-04-23 08:37:47.092175	\N	\N	5
10	AUTUMN15	15% off for all bookings in Autumn	15	2025-09-01 00:00:00	2025-11-30 23:59:59	https://tse1.mm.bing.net/th/id/OIP.qUlpceiXtdnlAJdCd7HjigHaHa?rs=1&pid=ImgDetMain	t	2025-04-23 08:37:47.092175	2025-04-23 08:37:47.092175	\N	\N	4
1	DISCOUNT10	\N	10	2025-12-31 23:59:59	2025-01-01 00:00:00	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752773137/60e98f7b-1074-4caa-b35d-417e864b25a0?_a=DAGAACAWZAA0	t	2025-04-22 23:05:48.343219	2025-07-18 00:25:37.805793	manager@gmail.com	\N	4
11	TETHOLIDAY10	\N	10	2025-07-15 00:00:00	2025-07-17 00:00:00	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752807000/8bd6ba3a-09a3-4144-849f-aebd695945e0?_a=DAGAACAWZAA0	\N	2025-07-18 09:50:00.666388	\N	\N	manager@gmail.com	0
\.


--
-- TOC entry 6149 (class 0 OID 37516)
-- Dependencies: 254
-- Data for Name: drivers; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.drivers (id, name, phone_number, vehicle_id, status, created_at, updated_at, updated_by, created_by) FROM stdin;
2	Tran Thi B	0912345678	2	OFFLINE	2025-04-22 23:48:10.989752	2025-04-22 23:48:10.989752	\N	\N
3	Le Hoang C	0931234567	3	BUSY	2025-04-22 23:48:10.989752	2025-04-22 23:48:10.989752	\N	\N
4	Pham Minh D	0945678901	4	ACTIVE	2025-04-22 23:48:10.989752	2025-04-22 23:48:10.989752	\N	\N
5	Hoang Thanh E	0901234567	5	ACTIVE	2025-04-22 23:48:10.989752	2025-04-22 23:48:10.989752	\N	\N
6	Nguyen Thi F	0987651234	6	OFFLINE	2025-04-22 23:48:10.989752	2025-04-22 23:48:10.989752	\N	\N
7	Tran Minh G	0912348765	7	BUSY	2025-04-22 23:48:10.989752	2025-04-22 23:48:10.989752	\N	\N
8	Le Thi H	0931239876	8	ACTIVE	2025-04-22 23:48:10.989752	2025-04-22 23:48:10.989752	\N	\N
9	Pham Hoang I	0945673210	9	OFFLINE	2025-04-22 23:48:10.989752	2025-04-22 23:48:10.989752	\N	\N
10	Hoang Minh J	0901238765	10	ACTIVE	2025-04-22 23:48:10.989752	2025-04-22 23:48:10.989752	\N	\N
1	Nguyen Van A	0987654321	1	ACTIVE	2025-04-22 23:48:10.989752	2025-04-22 23:48:10.989752	\N	\N
\.


--
-- TOC entry 6170 (class 0 OID 45698)
-- Dependencies: 275
-- Data for Name: facilities; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.facilities (id, name) FROM stdin;
2	Bể bơi ngoài trời
3	Bể bơi trong nhà
4	Trung tâm thể dục
5	Nhà hàng
6	Quầy bar
7	Dịch vụ phòng 24/7
8	Lễ tân 24/7
9	Bãi đỗ xe miễn phí
10	Dịch vụ đưa đón sân bay
11	Phòng không hút thuốc
12	Phòng gia đình
13	Dịch vụ giặt là
14	Spa & chăm sóc sức khỏe
15	Máy lạnh
16	Két an toàn
17	Tivi màn hình phẳng
18	Máy pha cà phê
19	Bàn làm việc
20	Ban công riêng
21	View biển
22	Bữa sáng miễn phí
1	Wi-Fi miễn phí
23	Sân golf
24	Sân đá bóng
\.


--
-- TOC entry 6166 (class 0 OID 45650)
-- Dependencies: 271
-- Data for Name: notifications; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6156 (class 0 OID 45568)
-- Dependencies: 261
-- Data for Name: num_rooms; Type: TABLE DATA; Schema: public; Owner: postgres
--




--
-- TOC entry 6174 (class 0 OID 45872)
-- Dependencies: 279
-- Data for Name: payment_transaction; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6136 (class 0 OID 37212)
-- Dependencies: 241
-- Data for Name: permissions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.permissions (id, name, created_at, updated_at, created_by, updated_by) FROM stdin;
1	READ	2025-04-07 20:52:01.473122	\N	\N	\N
2	CREATE	2025-04-07 20:52:01.473122	\N	\N	\N
3	UPDATE	2025-04-07 20:52:37.176618	\N	\N	\N
                                                                                                                         4	DELETE	2025-04-07 20:52:37.176618	\N	\N	\N
\.


--
-- TOC entry 6122 (class 0 OID 16430)
-- Dependencies: 222
-- Data for Name: properties; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.properties (id, name, city_id, property_type, rating_star, address, latitude, longitude, overview, avg_review_score, created_at, updated_at, created_by, updated_by, slug, geog, distance_from_center, distance_from_trip) FROM stdin;
12	Khách sạn & Spa Paradise Center Hà Nội	1	Hotel	3	22/5 Hang Voi Street, Ly Thai To Ward, Hoan Kiem District, Vietnam, Hoan Kiem, Hanoi, Vietnam	21.0308041931	105.8560292112	Tọa lạc tại thành phố Hà Nội, cách Nhà hát múa rối nước Thăng Long 400 m, Hanoi Paradise Center Hotel & Spa cung cấp chỗ nghỉ với sảnh khách chung, chỗ đỗ xe riêng, sân hiên và quầy bar. Với dịch vụ mát-xa, khách sạn này nằm gần một số điểm tham quan nổi tiếng, cách Hồ Hoàn Kiếm chưa đầy 1 km, cách Ô Quan Chưởng 12 phút đi bộ và cách trung tâm thương mại Tràng Tiền Plaza chưa đầy 1 km. Chỗ nghỉ có lễ tân 24 giờ, dịch vụ đưa đón sân bay, dịch vụ phòng và WiFi miễn phí. Khách sạn cung cấp phòng nghỉ lắp máy điều hòa với bàn làm việc, ấm đun nước, tủ lạnh, minibar, két an toàn, TV màn hình phẳng và phòng tắm riêng đi kèm chậu rửa vệ sinh (bidet). Các phòng nghỉ tại Hanoi Paradise Center Hotel & Spa được trang bị đồ vệ sinh cá nhân miễn phí và ổ cắm cho iPod. Chỗ nghỉ phục vụ bữa sáng tự chọn, gọi món hoặc bữa sáng đầy đủ kiểu Anh/Ailen. Tại chỗ nghỉ, du khách sẽ tìm thấy nhà hàng phục vụ ẩm thực châu Phi, Mỹ và Argentina. Du khách cũng có thể yêu cầu các lựa chọn không có sữa, halal và kosher. Khu vực này nổi tiếng với hoạt động đạp xe và dịch vụ cho thuê xe đạp có tại khách sạn 4 sao này. Nhà hát lớn Hà Nội cách Hanoi Paradise Center Hotel & Spa 1,1 km, Nhà thờ lớn Hà Nội cách đó 1,2 km. Sân bay quốc tế Nội Bài cách đó 24 km.	7.714285714285714	2025-03-25 16:40:24.676405	2025-05-22 15:50:41.380553	\N	\N	khach-san-spa-paradise-center-ha-noi	0101000020E6100000D7A3703D0A775A4048E17A14AE073540	2.3745114092899997	\N
45	Splendid Premium Hotel	1	Hotel	3	Số 36, Quận Hồ Hoàn Kiếm, Hà Nội	21.0318137626	105.8546718345	Khách sạn sang trọng	4.714285714285714	2025-07-14 18:01:14.984108	2025-07-18 10:58:28.411335	manager@gmail.com	ckp2004vn@gmail.com	splendid-premium-hotel	0101000020E6100000D8777EF1B2765A402B225EF224083540	1801.54515126	\N
20	Nature Hotel City Center	4	Hotel	0	77/4 đường Phan Bội Châu, phường 1, Da Lat, Vietnam\nExcellent location — rated 9.1/10!(score from 189 reviews)\nReal guests • Real stays • Real opinions\n	11.9475542348	108.4422065179	Giảm giá Genius tại cơ sở lưu trú này tùy thuộc vào ngày đặt phòng, ngày lưu trú và các ưu đãi khác có sẵn.\n\nNằm cách Quảng trường Lâm Viên 1,5 km, Nature Hotel City Center cung cấp chỗ nghỉ 3 sao tại thành phố Đà Lạt và có sân hiên. Chỗ nghỉ này cách Vườn hoa Đà Lạt khoảng 2,2 km, cách CLB chơi golf Dalat Palace 2,3 km và cách Thiền viện Trúc Lâm 6,1 km. Chỗ nghỉ cung cấp dịch vụ lễ tân 24 giờ, dịch vụ đưa đón sân bay, dịch vụ phòng và WiFi miễn phí trong toàn bộ khuôn viên. Tất cả các phòng nghỉ đều được trang bị tủ lạnh, minibar, ấm đun nước, vòi sen, máy sấy tóc và bàn làm việc. Với phòng tắm riêng được trang bị chậu rửa vệ sinh (bidet) cùng đồ vệ sinh cá nhân miễn phí, một số phòng nghỉ tại khách sạn cũng cung cấp cho khách tầm nhìn ra quang cảnh thành phố. Mỗi phòng nghỉ tại Nature Hotel City Center đều được trang bị TV màn hình phẳng và két an toàn. Các điểm tham quan nổi tiếng gần chỗ nghỉ bao gồm Hồ Xuân Hương, Công viên Yersin Đà Lạt và Biệt thự Hằng Nga. Sân bay Liên Khương cách đó 32 km.	0	2025-03-25 21:03:47.492585	\N	\N	\N	nature-hotel-city-center	0101000020E61000005C8FC2F5281C5B406666666666E62740	2.0679055711300003	\N
4	Silk Hotel near Tan Son Nhat Airport	2	Resort	0	54/35 Bach Dang, Ward 2, Tan Binh, Tan Binh, Ho Chi Minh City, Vietnam	10.8135821792	106.6718286497	Giảm giá Genius tại cơ sở lưu trú này tùy thuộc vào ngày đặt phòng, ngày lưu trú và các ưu đãi khác có sẵn.\n\nTọa lạc tại vị trí thuận tiện ở quận Tân Bình của Thành phố Hồ Chí Minh, Silk Hotel near Tan Son Nhat Airport nằm cách Chợ Tân Định 4,5 km, Bảo tàng Chứng tích Chiến tranh 5,8 km và Dinh Thống Nhất 5,9 km. Với WiFi miễn phí, khách sạn 3 sao này có lễ tân 24 giờ. Khách sạn có các phòng gia đình. Các phòng nghỉ tại đây được trang bị máy lạnh, TV màn hình phẳng với các kênh truyền hình vệ tinh, ấm đun nước, vòi sen, đồ vệ sinh cá nhân miễn phí và tủ quần áo. Tất cả các phòng đều được trang bị phòng tắm riêng với máy sấy tóc, trong khi một số phòng nhìn ra quang cảnh thành phố. Chùa Giác Lâm cách khách sạn 6 km, trong khi trung tâm thương mại Diamond Plaza cách đó 6 km. Sân bay quốc tế Tân Sơn Nhất cách đó 1 km và chỗ nghỉ cung cấp dịch vụ đưa đón sân bay có tính phí.\n\nCác cặp đôi đặc biệt thích địa điểm tuyệt vời này — họ đánh giá 9,0 điểm cho chuyến đi dành cho hai người.	0	2025-03-25 16:40:24.676405	\N	\N	\N	silk-hotel-near-tan-son-nhat-airport	0101000020E61000003D0AD7A370AD5A401F85EB51B89E2540	600.90683616391	\N
15	Amunra Ocean Hotel	3	Hotel	0	06 An Thượng 30, phường Mỹ An, quận Ngũ Hành Sơn, thành phố Đà Nẵng, Việt Nam, Da Nang, Vietnam	16.0517381695	108.2466843823	Nằm trong bán kính 300 m từ Bãi biển Mỹ Khê và chưa đầy 1 km từ Bãi biển Bắc Mỹ An, Amunra Ocean Hotel cung cấp phòng nghỉ tại thành phố Đà Nẵng. Khách sạn 3 sao này có lễ tân 24 giờ, dịch vụ trợ giúp đặc biệt và WiFi miễn phí. Các món ăn Mỹ, Anh, Pháp và Đức được phục vụ tại nhà hàng trong khuôn viên. Khách sạn cung cấp cho khách các phòng nghỉ máy lạnh với bàn làm việc, ấm đun nước, minibar, két an toàn, TV màn hình phẳng và phòng tắm riêng đi kèm chậu rửa vệ sinh (bidet). Một số phòng được trang bị bếp với lò vi sóng và bếp nấu. Các phòng nghỉ có tủ quần áo. Amunra Ocean Hotel phục vụ bữa sáng gọi món hoặc kiểu Á. Cầu khóa tình yêu Đà Nẵng cách chỗ nghỉ 3,2 km trong khi Bảo tàng Chăm cách đó 3,8 km. Sân bay quốc tế Đà Nẵng cách chỗ nghỉ 7 km.	0	2025-03-25 17:02:53.113603	\N	\N	\N	amunra-ocean-hotel	0101000020E61000000000000000105B40CDCCCCCCCC0C3040	432.33327454535	2213.18613129
7	25's homestay	1	Homestay	2	26 Phố Vạn Phúc, Ba Dinh, 100000 Hanoi, Vietnam	21.0283390514	105.8392981152	Tọa lạc tại quận Ba Đình của thành phố Hà Nội, 25's homestay có máy lạnh, sân hiên và tầm nhìn ra khu vườn. Chỗ nghỉ này cách trung tâm thương mại Vincom Center Nguyễn Chí Thanh 1,8 km và có thang máy. Căn hộ cũng có WiFi miễn phí, chỗ đỗ xe riêng miễn phí và tiện nghi cho khách khuyết tật. Căn hộ có 1 phòng ngủ, TV màn hình phẳng với dịch vụ phát trực tuyến, bếp đầy đủ tiện nghi với máy rửa chén và lò nướng, máy giặt và 1 phòng tắm đi kèm chậu rửa vệ sinh (bidet). Căn hộ này cũng có ban công kiêm khu vực ăn uống ngoài trời. Phòng thay đồ, dịch vụ giặt là và an ninh 24 giờ cũng có sẵn. Du khách có thể thư giãn trong khu vườn tại chỗ nghỉ. Chùa Một Cột cách căn hộ 2,1 km trong khi Lăng Chủ tịch Hồ Chí Minh cách đó 2,7 km. Sân bay quốc tế Nội Bài cách chỗ nghỉ 23 km và chỗ nghỉ cung cấp dịch vụ đưa đón sân bay có tính phí.	8.571428571428571	2025-03-25 16:40:24.676405	2025-05-20 18:08:11.813722	\N	\N	25s-homestay	0101000020E610000014AE47E17A745A4048E17A14AE073540	1.8396745567200001	\N
5	Fuji Apartment 2	1	Apartment	3	165/49 Dương Quảng Hàm, Cau Giay, 122000 Hanoi, Vietnam	21.0364141875	105.7987246806	Nằm trong bán kính 1 km từ Bảo tàng Dân tộc học Việt Nam và 2,8 km từ trung tâm thương mại Vincom Center Nguyễn Chí Thanh, Fuji Apartment 2 cung cấp các phòng nghỉ với máy lạnh và phòng tắm riêng tại thành phố Hà Nội. Chỗ nghỉ này cách Chùa Một Cột khoảng 4,1 km, Bảo tàng Mỹ thuật Việt Nam 4,2 km và Văn Miếu - Quốc Tử Giám 4,3 km. Lăng Chủ tịch Hồ Chí Minh cách đó 4,7 km và Hoàng thành Thăng Long cách căn hộ 4,9 km. Tất cả các phòng nghỉ trong khu phức hợp căn hộ đều được trang bị máy pha cà phê. Với phòng tắm riêng được trang bị chậu rửa vệ sinh (bidet) và dép đi trong nhà, các phòng nghỉ tại khu phức hợp căn hộ cũng có WiFi miễn phí trong khi một số phòng nghỉ có sân hiên. Mỗi phòng nghỉ tại khu phức hợp căn hộ đều được trang bị khăn trải giường và khăn tắm. Đền Quán Thánh cách căn hộ 5,3 km trong khi Sân vận động Mỹ Đình cách đó 5,6 km. Sân bay quốc tế Nội Bài cách đó 23 km.	8.571428571428571	2025-03-25 16:40:24.676405	2025-05-20 18:14:37.872429	\N	\N	fuji-apartment-2	0101000020E61000003D0AD7A370AD5A408FC2F5285C8F2540	1138.4362720725399	\N
3	La Bonita Hotel and Apartments	2	Apartment	0	42 Hưng Phước 4, Tân Phong, District 7, Ho Chi Minh City, Vietnam, District 7, Ho Chi Minh City, Vietnam	10.7815367962	106.6948078421	Tọa lạc tại Thành phố Hồ Chí Minh, cách Trung tâm Hội nghị và Triển lãm Sài Gòn 2 km, La Bonita Hotel and Apartments cung cấp chỗ nghỉ với sân hiên, chỗ đỗ xe riêng miễn phí, nhà hàng và quầy bar. Với WiFi miễn phí, khách sạn 3 sao này cung cấp dịch vụ phòng và lễ tân 24 giờ. Chỗ nghỉ không gây dị ứng và cách Bến Nhà Rồng 5,9 km. Mỗi phòng nghỉ tại khách sạn đều có tủ quần áo và TV màn hình phẳng. Tất cả các phòng đều có ấm đun nước và phòng tắm riêng với vòi sen cùng đồ vệ sinh cá nhân miễn phí, trong khi một số phòng chọn lọc sẽ cung cấp cho bạn bếp được trang bị lò vi sóng. Các phòng nghỉ tại La Bonita Hotel and Apartments có máy lạnh và bàn làm việc. Khách nghỉ tại chỗ nghỉ có thể thưởng thức bữa sáng gọi món. Bảo tàng Mỹ thuật cách La Bonita Hotel and Apartments 6,1 km, trong khi trung tâm thương mại Takashimaya Việt Nam cách đó 6,5 km. Sân bay quốc tế Tân Sơn Nhất cách chỗ nghỉ 13 km.	0	2025-03-25 16:40:24.676405	\N	\N	\N	la-bonita-hotel-and-apartments	0101000020E6100000CDCCCCCCCCAC5A4014AE47E17A942540	603.3302853005599	\N
6	Sao Mai Boutique Hotel	1	Hotel	3	23 Ngõ Thông Phong, Dong Da, 67337 Hanoi, Vietnam	21.0260560965	105.8342878094	Tọa lạc tại thành phố Hà Nội, cách Bảo tàng Mỹ thuật Việt Nam 800 m, Sao Mai Boutique Hotel cung cấp chỗ nghỉ với xe đạp cho khách sử dụng miễn phí, chỗ đỗ xe riêng miễn phí, sảnh khách chung và sân hiên. Khách sạn 4 sao này cung cấp dịch vụ trợ giúp đặc biệt và bàn đặt tour. Chỗ nghỉ cung cấp dịch vụ lễ tân 24 giờ, dịch vụ đưa đón sân bay, dịch vụ phòng và WiFi miễn phí trong toàn bộ khuôn viên. Mỗi phòng nghỉ tại khách sạn đều được trang bị máy điều hòa, khu vực ghế ngồi, TV màn hình phẳng với các kênh truyền hình vệ tinh, bếp nhỏ, khu vực ăn uống, két an toàn và phòng tắm riêng với chậu rửa vệ sinh (bidet), đồ vệ sinh cá nhân miễn phí cùng máy sấy tóc. Tất cả các phòng đều được trang bị ấm đun nước trong khi một số phòng chọn lọc có ban công và những phòng khác cũng cung cấp cho khách tầm nhìn ra quang cảnh thành phố. Tất cả các phòng đều được trang bị tủ lạnh. Chỗ nghỉ phục vụ bữa sáng tự chọn, kiểu lục địa hoặc kiểu Mỹ. Tại Sao Mai Boutique Hotel, du khách sẽ tìm thấy nhà hàng phục vụ ẩm thực Trung Quốc, Việt Nam và Châu Á. Du khách cũng có thể yêu cầu lựa chọn ăn chay. Các điểm tham quan nổi tiếng gần chỗ nghỉ bao gồm Văn Miếu - Quốc Tử Giám, Lăng Chủ tịch Hồ Chí Minh và Hoàng thành Thăng Long. Sân bay quốc tế Nội Bài cách đó 24 km.	8.571428571428571	2025-03-25 16:40:24.676405	2025-05-20 18:07:17.873918	\N	\N	sao-mai-boutique-hotel	0101000020E610000085EB51B81E755A4048E17A14AE073540	0.84040935374	\N
11	The Flower Boutique Hotel & Travel	1	Villa	4	055 Nguyễn Trường Tộ, Ba Dinh, Hanoi, Vietnam\n	21.0423790618	105.8456573112	Tọa lạc tại vị trí hấp dẫn ở quận Ba Đình của thành phố Hà Nội, The Flower Boutique Hotel & Travel nằm cách Đền Quán Thánh 1,2 km, cách Ô Quan Chưởng 1,1 km và cách Lăng Chủ tịch Hồ Chí Minh 1,7 km. Khách sạn 4 sao này có bàn đặt tour và chỗ để hành lý. Chỗ nghỉ có lễ tân 24 giờ, dịch vụ đưa đón sân bay, dịch vụ trợ giúp đặc biệt và WiFi miễn phí. Khách sạn cung cấp phòng nghỉ gắn máy điều hòa với bàn làm việc, ấm đun nước, tủ lạnh, minibar, két an toàn, TV màn hình phẳng và phòng tắm riêng đi kèm chậu rửa vệ sinh (bidet). Các phòng nghỉ đều có tủ quần áo. Du khách có thể thưởng thức bữa sáng tự chọn, gọi món hoặc kiểu lục địa tại chỗ nghỉ. Các điểm tham quan nổi tiếng gần The Flower Boutique Hotel & Travel bao gồm Hồ Tây, Nhà hát múa rối nước Thăng Long và Hoàng thành Thăng Long. Sân bay quốc tế Nội Bài cách chỗ nghỉ 22 km.	6.714285714285714	2025-03-25 16:40:24.676405	2025-05-20 18:11:37.391729	\N	\N	the-flower-boutique-hotel-travel	0101000020E61000006666666666765A400AD7A3703D0A3540	1.51839338927	\N
17	The Now boutique Hotel & Apartment	3	Hotel	0	K55/15 Ngũ Hành Sơn, Bắc Mỹ Phú, Ngũ Hành Sơn, Đà Nẵng, Da Nang, Vietnam	16.0491606021	108.2390477958	Tọa lạc tại thành phố Đà Nẵng, cách Bãi biển Mỹ Khê 1,2 km và Bãi biển Bắc Mỹ An 1,6 km, The Now boutique Hotel & Apartment tự hào có hồ bơi ngoài trời quanh năm, phòng xông hơi khô và dịch vụ làm đẹp. Khách sạn căn hộ 4 sao này có lễ tân 24 giờ và thang máy. Khách sạn căn hộ cũng cung cấp WiFi miễn phí, chỗ đỗ xe riêng miễn phí và tiện nghi cho khách khuyết tật. Khách sạn căn hộ sẽ cung cấp cho khách các phòng nghỉ máy lạnh với bàn làm việc, ấm đun nước, minibar, két an toàn, TV màn hình phẳng và phòng tắm riêng với góc tắm vòi sen mở. Một số phòng còn có bếp đầy đủ tiện nghi được trang bị lò vi sóng, tủ lạnh và đồ dùng nhà bếp. Các tiện nghi bổ sung trong phòng bao gồm rượu vang hoặc rượu sâm panh. Các lựa chọn bữa sáng kiểu lục địa và kiểu Mỹ với các món ăn nóng, đặc sản địa phương và bánh ngọt tươi ngon cũng có sẵn. Tại khách sạn căn hộ, nhà hàng thân thiện với gia đình mở cửa phục vụ bữa tối, bữa trưa và bữa nửa buổi, chuyên về ẩm thực Việt Nam. Đối với những khách đi cùng trẻ em, The Now boutique Hotel & Apartment có khu vui chơi trong nhà và cổng an toàn cho trẻ em. Chỗ nghỉ có sân hiên tắm nắng và lò sưởi ngoài trời. Cầu khóa tình yêu Đà Nẵng cách The Now boutique Hotel & Apartment 2,3 km, Bảo tàng Chăm cách đó 2,9 km. Sân bay quốc tế Đà Nẵng cách chỗ nghỉ 6 km và chỗ nghỉ cung cấp dịch vụ đưa đón sân bay có tính phí.	0	2025-03-25 21:03:47.492585	\N	\N	\N	the-now-boutique-hotel-apartment	0101000020E61000008FC2F5285C0F5B40CDCCCCCCCC0C3040	432.5876424335	2458.28067827
14	CN Ocean Hotel	3	Hotel	0	9 An Thượng 26, Da Nang, Vietnam	16.0557229225	108.2452530958	CN Ocean Hotel tọa lạc tại thành phố Đà Nẵng và có hồ bơi trên tầng mái cùng tầm nhìn ra khu vườn. Trong số các tiện nghi của chỗ nghỉ này có nhà hàng, lễ tân 24 giờ và thang máy cùng WiFi miễn phí trong toàn bộ khuôn viên. Nhà nghỉ nông thôn 4 sao này có lối vào riêng. Tất cả các phòng nghỉ tại nhà nghỉ nông thôn này đều có máy lạnh, khu vực ghế ngồi, TV màn hình phẳng với các kênh truyền hình vệ tinh, két an toàn và phòng tắm riêng với góc tắm vòi sen mở, áo choàng tắm và dép. Tủ lạnh và minibar cũng có sẵn, cũng như ấm đun nước. Mỗi phòng nghỉ tại nhà nghỉ nông thôn này đều được trang bị khăn trải giường và khăn tắm. Các lựa chọn bữa sáng theo kiểu gọi món và kiểu lục địa với các món ăn nóng và đặc sản địa phương cũng có sẵn. Nhà nghỉ nông thôn này có khu vực dã ngoại và sân hiên. Bãi biển Mỹ Khê cách CN Ocean Hotel 500 m, trong khi Bãi biển Bắc Mỹ An cách đó 1,5 km. Sân bay quốc tế Đà Nẵng cách đó 6 km.	0	2025-03-25 16:54:52.731583	\N	\N	\N	cn-ocean-hotel	0101000020E61000008FC2F5285C0F30408FC2F5285C0F3040	9973.002070533279	9757622.87247177
13	Investland in Truc Bach & serviced apartment	1	Apartment	0	24 Lac Chinh, Ba Dinh, Ba Dinh, Hanoi, Vietnam\n	21.0461749645	105.8415568266	Với tầm nhìn ra khu vườn, Investland in Truc Bach & serviced apartment là chỗ nghỉ nằm tại thành phố Hà Nội, cách Đền Quán Thánh 1,1 km và cách Hồ Tây chưa đến 1 km. Căn hộ cung cấp miễn phí cả WiFi và chỗ đỗ xe trong khuôn viên. Chỗ nghỉ có thang máy và dịch vụ giặt khô cho khách. Căn hộ lắp máy điều hòa này bao gồm 1 phòng ngủ, phòng khách, bếp đầy đủ tiện nghi với lò vi sóng cùng ấm đun nước và 1 phòng tắm đi kèm bồn tắm cùng dép. TV màn hình phẳng với các kênh truyền hình cáp cũng có sẵn. Dịch vụ giặt là cũng được cung cấp. Các điểm tham quan nổi tiếng gần Investland in Truc Bach & serviced apartment bao gồm Lăng Chủ tịch Hồ Chí Minh, Hoàng thành Thăng Long và Ô Quan Chưởng. Sân bay quốc tế Nội Bài cách chỗ nghỉ 22 km.	0	2025-03-25 16:40:24.676405	\N	\N	\N	investland-in-truc-bach-serviced-apartment	0101000020E6100000F6285C8FC2755A40CDCCCCCCCC0C3540	1.89359157153	\N
46	Hanoi Tirant Hotel	1	Hotel	0	36-38-40 Gia Ngu Street, Old Quarter, Hoan Kiem, Hanoi	21.0334399165	105.8521497228	\N	0	2025-07-14 18:31:42.109618	2025-07-14 18:31:42.135826	manager@gmail.com	manager@gmail.com	hanoi-tirant-hotel	0101000020E61000006CB7FD9E89765A404095B3848F083540	1534.50150905	\N
43	JW Marriott Hotel Hanoi	1	Hotel	3	No 8 Do Duc Duc Road ME Tri, Nam Từ Liêm, Hà Nội 100000, Việt Nam	21.0071361855	105.7831650112	Khách sạn đẹp	6	2025-07-02 16:46:03.461953	2025-07-18 11:03:27.987526	manager@gmail.com	lam@gmail.com	jw-marriott-hotel-hanoi	0101000020E6100000B89C23601F725A40953853ADD3013540	6326.59101529	\N
19	Dalat Wind Hotel	4	Hotel	0	Lot R2 03-04. Golf Valley, Ward 2, Da Lat, Vietnam	11.9478586309	108.4413696687	Bạn có thể đủ điều kiện để được giảm giá Genius tại Dalat Wind Hotel. Để kiểm tra xem có giảm giá Genius cho những ngày bạn đã chọn hay không, hãy đăng nhập .\n\nGiảm giá Genius tại cơ sở lưu trú này tùy thuộc vào ngày đặt phòng, ngày lưu trú và các ưu đãi khác có sẵn.\n\nDalat Wind Hotel tọa lạc tại thành phố Đà Lạt, cách Hồ Xuân Hương 500 m. Khách sạn 2 sao này có lễ tân 24 giờ và sảnh khách chung. Chỗ nghỉ này cung cấp cả WiFi miễn phí và chỗ đỗ xe riêng. Tất cả các phòng nghỉ tại khách sạn đều có bàn làm việc và TV màn hình phẳng. Dalat Wind Hotel có một số phòng có ban công và các phòng được trang bị ấm đun nước. Tất cả các phòng đều có phòng tắm riêng với chậu rửa vệ sinh (bidet). Bữa sáng gọi món được phục vụ hàng ngày tại chỗ nghỉ. Du khách có thể dùng bữa tại nhà hàng trong khuôn viên, chuyên về ẩm thực nướng/bbq. Chỗ nghỉ có sân hiên tắm nắng. Các điểm tham quan nổi tiếng gần Dalat Wind Hotel bao gồm Vườn hoa Đà Lạt, Quảng trường Lâm Viên và Công viên Yersin Đà Lạt. Sân bay gần nhất là Sân bay Liên Khương, cách chỗ nghỉ 23 km.	0	2025-03-25 21:03:47.492585	\N	\N	\N	dalat-wind-hotel	0101000020E61000005C8FC2F5281C5B406666666666E62740	2.0679055711300003	\N
9	Amanda Boutique Hotel & Travel	1	Hotel	5	62E Phố Cầu Gỗ, Hoan Kiem, Hanoi, Vietnam	21.0325057039	105.8525204517	Tọa lạc tại vị trí hấp dẫn ở trung tâm thành phố Hà Nội, Amanda Boutique Hotel & Travel nằm trong bán kính 100 m từ Nhà hát múa rối nước Thăng Long và 600 m từ Hồ Hoàn Kiếm. Khách sạn 3 sao này có nhà hàng và các phòng nghỉ lắp máy điều hòa với WiFi miễn phí, mỗi phòng đều đi kèm phòng tắm riêng. Chỗ nghỉ cung cấp dịch vụ phòng, lễ tân 24 giờ và dịch vụ thu đổi ngoại tệ cho khách. Tất cả các phòng đều được trang bị tủ lạnh, minibar, ấm đun nước, chậu rửa vệ sinh (bidet), đồ vệ sinh cá nhân miễn phí và tủ quần áo. Các phòng được trang bị TV màn hình phẳng và một số phòng tại khách sạn có tầm nhìn ra quang cảnh thành phố. Khách sạn phục vụ bữa sáng tự chọn, kiểu lục địa hoặc kiểu Á. Các điểm tham quan nổi tiếng gần Amanda Boutique Hotel & Travel bao gồm Ô Quan Chưởng, trung tâm thương mại Tràng Tiền Plaza và Nhà hát Lớn Hà Nội. Sân bay quốc tế Nội Bài cách chỗ nghỉ 24 km và chỗ nghỉ cung cấp dịch vụ đưa đón sân bay có tính phí.	5.714285714285714	2025-03-25 16:40:24.676405	2025-05-20 18:10:47.978724	\N	\N	amanda-boutique-hotel-travel	0101000020E61000006666666666765A4048E17A14AE073540	1.3539715149	\N
16	Hadana Boutique Hotel Danang	3	Hotel	0	H1-04,05,06 đường Phạm Văn Đồng, phường An Hải Bắc, quận Sơn Trà, Thành Phố Đà Nẵng, Việt Nam, Da Nang, Vietnam	16.0717058130	108.2354813958	Hadana Boutique Hotel Danang có hồ bơi ngoài trời, khu vườn, sảnh khách chung và nhà hàng tại thành phố Đà Nẵng. Mỗi phòng nghỉ tại khách sạn 4 sao này đều có tầm nhìn ra quang cảnh thành phố và du khách có thể sử dụng phòng xông hơi khô. Chỗ nghỉ có lễ tân 24 giờ, dịch vụ đưa đón sân bay, dịch vụ phòng và WiFi miễn phí trong toàn bộ khuôn viên. Khách sạn cung cấp phòng nghỉ lắp máy điều hòa với bàn làm việc, ấm đun nước, két an toàn, TV màn hình phẳng và phòng tắm riêng đi kèm chậu rửa vệ sinh (bidet). Tất cả các phòng đều có tủ quần áo. Du khách tại Hadana Boutique Hotel Danang có thể thưởng thức bữa sáng tự chọn hoặc bữa sáng kiểu Á. Các điểm tham quan nổi tiếng gần chỗ nghỉ bao gồm Bãi biển Mỹ Khê, Cầu Sông Hàn và Trung tâm thương mại Indochina Riverside. Sân bay quốc tế Đà Nẵng cách chỗ nghỉ 6 km.	0	2025-03-25 21:03:47.492585	\N	\N	\N	hadana-boutique-hotel-danang	0101000020E61000008FC2F5285C0F5B4052B81E85EB113040	434.7370658253	1069.97041126
18	TTR Moonstone Apart Hotel	4	Apartment	0	86 Đường Lý Tự Trọng, Da Lat, Vietnam	11.9461912214	108.4386659957	You might be eligible for a Genius discount at TTR Moonstone Apart Hotel. To check if a Genius discount is available for your selected dates sign in.\n\nGenius discounts at this property are subject to book dates, stay dates and other available deals.\n\nSituated in Da Lat, 1.9 km from Dalat Palace Golf Club, TTR Moonstone Apart Hotel features accommodation with free bikes, free private parking, a garden and a terrace. This 2-star hotel offers a restaurant. The accommodation offers a 24-hour front desk, airport transfers, room service and free WiFi.\n\nAll rooms are fitted with a flat-screen TV with cable channels, fridge, a kettle, a bidet, free toiletries and a desk. The rooms come with a private bathroom fitted with a shower and a hairdryer, while selected rooms also feature a kitchen equipped with a microwave. The units have a safety deposit box.\n\nThe area is popular for cycling, and car hire is available at the hotel.\n\nLam Vien Square is 1.9 km from TTR Moonstone Apart Hotel, while Xuan Huong Lake is 2.2 km from the property. Lien Khuong Airport is 28 km away.	0	2025-03-25 21:03:47.492585	\N	\N	\N	ttr-moonstone-apart-hotel	0101000020E61000005C8FC2F5281C5B406666666666E62740	2.0679055711300003	\N
10	Peaceful Corner in Old Quarter	1	Villa	1	3 Phố Phan Huy Ích 6, Ba Dinh, 100000 Hanoi, Vietnam	21.0325790608	105.8500387310	Peaceful Corner in Old Quarter là một căn hộ nằm tại quận Ba Đình của thành phố Hà Nội. Du khách có thể vào căn hộ qua lối vào riêng. Tất cả các phòng trong khu phức hợp căn hộ đều được trang bị ấm đun nước. Với phòng tắm riêng đi kèm góc tắm vòi sen và dép, các phòng tại khu phức hợp căn hộ cũng có WiFi miễn phí. Các phòng được trang bị tiện nghi sưởi ấm. Sân bay quốc tế Nội Bài cách chỗ nghỉ 22 km.	6.142857142857143	2025-03-25 16:40:24.676405	2025-05-20 18:11:13.498645	\N	\N	peaceful-corner-in-old-quarter	0101000020E61000006666666666765A4048E17A14AE073540	1.3539715149	\N
21	Maris Hotel Nha Trang	5	Hotel	0	27 Tran Quang Khai Street, Loc Tho Ward, Nha Trang, Vietnam\nExcellent location — rated 9.5/10!(score from 456 reviews)\nReal guests • Real stays • Real opinions\n	12.2335797035	109.1963702957	Maris Hotel Nha Trang có hồ bơi ngoài trời, trung tâm thể dục, nhà hàng và quầy bar tại Nha Trang. Khách sạn 4 sao này cung cấp câu lạc bộ trẻ em, dịch vụ phòng và WiFi miễn phí. Khách sạn có bồn tắm nước nóng và lễ tân 24 giờ. Khách sạn sẽ cung cấp cho khách các phòng nghỉ máy lạnh với bàn làm việc, ấm đun nước, minibar, két an toàn, TV màn hình phẳng và phòng tắm riêng với vòi sen. Tất cả các phòng đều có tủ quần áo. Các lựa chọn bữa sáng tự chọn và kiểu lục địa có tại Maris Hotel Nha Trang. Các điểm tham quan nổi tiếng gần chỗ nghỉ bao gồm Bãi biển Nha Trang, Tháp Trầm Hương và Trung tâm mua sắm Nha Trang Centre. Sân bay quốc tế Cam Ranh cách đó 34 km.	0	2025-03-25 21:03:47.492585	\N	\N	\N	maris-hotel-nha-trang	0101000020E6100000CDCCCCCCCC4C5B40F6285C8FC2752840	323.63759376533	1106.24160866
8	Granda Central Apartment	1	Apartment	5	Lot A14/D21, Lane 100, Dich Vong Hau Street, Cau Giay District, Hanoi, Cau Giay, Hanoi, Vietnam	21.0291945805	105.7843757959	Nằm trong bán kính 2,6 km từ Bảo tàng Dân tộc học Việt Nam và 4,2 km từ Sân vận động Mỹ Đình tại thành phố Hà Nội, Granda Central Apartment cung cấp chỗ nghỉ với bếp. Cả WiFi miễn phí và chỗ đỗ xe trong khuôn viên đều được cung cấp miễn phí tại khách sạn căn hộ này. Khách sạn căn hộ này có các phòng gia đình. Khách sạn căn hộ này cung cấp cho khách các phòng nghỉ lắp máy điều hòa với bàn làm việc, ấm đun nước, lò vi sóng, tủ lạnh, két an toàn, TV màn hình phẳng và phòng tắm riêng đi kèm chậu rửa vệ sinh (bidet). Tất cả các phòng nghỉ tại khách sạn căn hộ này đều không gây dị ứng và cách âm. Tất cả các phòng nghỉ tại khách sạn căn hộ này đều có ga trải giường và khăn tắm. Trung tâm thương mại Vincom Center Nguyễn Chí Thanh cách Granda Central Apartment 4,4 km trong khi Bảo tàng Mỹ thuật Việt Nam cách đó 6,8 km. Sân bay quốc tế Nội Bài cách đó 25 km và khách sạn cung cấp dịch vụ đưa đón sân bay có tính phí.	8.571428571428571	2025-03-25 16:40:24.676405	2025-05-20 18:08:38.147757	\N	\N	granda-central-apartment	0101000020E610000052B81E85EB715A4048E17A14AE073540	5.97557202768	\N
2	Hotel Indigo Saigon the City, an IHG Hotel	2	Hotel	0	9-11 Ly Tu Trong Street, District 1, 700000 Ho Chi Minh City, Vietnam	10.7826080900	106.7052030822	Tọa lạc tại Thành phố Hồ Chí Minh, Hotel Indigo Saigon the City, an IHG Hotel cách Nhà hát lớn Sài Gòn chưa đầy 1 km và cung cấp nhiều tiện nghi khác nhau, chẳng hạn như trung tâm thể dục, sân hiên và nhà hàng. Với hồ bơi ngoài trời, khách sạn 5 sao này cung cấp các phòng nghỉ máy lạnh với WiFi miễn phí, mỗi phòng đều có phòng tắm riêng. Chỗ nghỉ cung cấp dịch vụ phòng và lễ tân 24 giờ cho khách. Tất cả các phòng nghỉ tại khách sạn đều được trang bị ấm đun nước. Mỗi phòng đều có TV và một số phòng tại Hotel Indigo Saigon the City, an IHG Hotel có tầm nhìn ra quang cảnh thành phố. Tất cả các phòng đều được trang bị tủ lạnh. Khách nghỉ tại chỗ nghỉ có thể thưởng thức bữa sáng tự chọn. Hotel Indigo Saigon the City, an IHG Hotel có trung tâm dịch vụ doanh nhân phục vụ khách. Các điểm tham quan nổi tiếng gần khách sạn bao gồm Trụ sở Ủy ban Nhân dân Thành phố Hồ Chí Minh, Trung tâm Thương mại Union Square Saigon và Bưu điện Trung tâm Sài Gòn. Sân bay Quốc tế Tân Sơn Nhất cách đó 7 km và chỗ nghỉ cung cấp dịch vụ đưa đón sân bay có tính phí.	0	2025-03-25 16:40:24.676405	2025-07-14 17:13:31.34328	\N	manager@gmail.com	hotel-indigo-saigon-the-city-an-ihg-hotel	0101000020E61000003D0AD7A370AD5A408FC2F5285C8F2540	604.10667559849	\N
44	Royal Hotel Ha Giang	7	Hotel	0	89 Lê Quý Đôn, Hà Giang	22.8288286248	104.9814181229	Hotel đẹp	0	2025-07-14 17:34:35.422203	2025-07-14 17:34:35.449698	manager@gmail.com	manager@gmail.com	royal-hotel-ha-giang	0101000020E6100000C25FF58DCF3E5A4023ADDD1C2ED43640	7342924.03313549	\N
1	Grand Lee Ran Hotel	2	Hotel	5	137/8 Đường Bình Quới , Binh Thanh, Ho Chi Minh City, Vietnam	10.8194184136	106.7235456803	Nằm trong bán kính 5,2 km từ Bảo tàng Lịch sử Việt Nam và 6,3 km từ trung tâm thương mại Diamond Plaza, Grand Lee Hotel cung cấp các phòng nghỉ với máy lạnh và phòng tắm riêng tại Thành phố Hồ Chí Minh.\nVới WiFi miễn phí, khách sạn 3 sao này cung cấp dịch vụ phòng và lễ tân 24 giờ. Chỗ nghỉ cung cấp dịch vụ trợ giúp đặc biệt, bàn đặt tour và dịch vụ giữ hành lý cho khách. Tại khách sạn, các phòng nghỉ đều có bàn làm việc. Với phòng tắm riêng được trang bị chậu rửa vệ sinh (bidet) và đồ vệ sinh cá nhân miễn phí, một số phòng tại Grand Lee Hotel cũng có tầm nhìn ra quang cảnh thành phố. Các phòng nghỉ tại đây được trang bị TV màn hình phẳng và máy sấy tóc. Bưu điện Trung tâm Sài Gòn cách Grand Lee Hotel 6,4 km, trong khi Chợ Tân Định cách đó 6,5 km. Sân bay Quốc tế Tân Sơn Nhất cách đó 8 km.\nCác cặp đôi đặc biệt thích vị trí rất tốt này — họ đánh giá 8,3 điểm cho chuyến đi dành cho hai người.	8.571428571428571	2025-03-25 15:44:50.210341	2025-07-18 10:56:02.123248	\N	ckp2004vn@gmail.com	grand-lee-hotel	0101000020E6100000AE47E17A14AE5A40A4703D0AD7A32540	599.55044095197	\N
\.


--
-- TOC entry 6178 (class 0 OID 54081)
-- Dependencies: 283
-- Data for Name: property_facilities; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.property_facilities (facility_id, property_id) FROM stdin;
8	44
12	44
11	44
18	44
17	45
14	44
14	45
19	45
2	1
2	1
2	1
2	1
2	46
5	1
5	1
5	1
5	1
5	46
7	44
7	46
1	43
1	44
1	43
3	43
3	43
4	43
4	44
4	1
4	1
4	1
4	1
4	43
\.


--
-- TOC entry 6172 (class 0 OID 45722)
-- Dependencies: 277
-- Data for Name: property_images; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.property_images (id, property_id, image) FROM stdin;
7	3	https://cf.bstatic.com/xdata/images/hotel/max1024x768/640188623.jpg?k=01ce7246c113347b939582afc07670373e12d6aa650cec5ea5d2e3e68a55964d&o=
8	3	https://cf.bstatic.com/xdata/images/hotel/max500/387952228.jpg?k=73a05b2a1c14507d92f10893e9687aac5e9c5becca50c24691c9aa0ea8397b94&o=
9	3	https://cf.bstatic.com/xdata/images/hotel/max300/387952264.jpg?k=82386ff964b4b6a160c7580dd1e4728c452c64c1f57259d49f038621bb3174cc&o=
10	4	https://cf.bstatic.com/xdata/images/hotel/max1024x768/410501327.jpg?k=9739cfac54136f826899c8d9c92523ee9551cc424eb39dc33fac7586bbd346af&o=
11	4	https://cf.bstatic.com/xdata/images/hotel/max500/410501328.jpg?k=d2b9fa0892bb00277c5e7d1c21ccfa4dee03b0e5b405142c21b982fb854bea8c&o=
12	4	https://cf.bstatic.com/xdata/images/hotel/max300/161544807.jpg?k=37b348b1d4b5d810d80f853280d5da9531094889bf5381e9ba1238eb5585cbab&o=
16	5	https://cf.bstatic.com/xdata/images/hotel/max1024x768/663320174.jpg?k=3562b6a569ec2ea83f58bcb94ce4b2d9f9d9b6d7af44b4537f1422444afb2523&o=
17	5	https://cf.bstatic.com/xdata/images/hotel/max500/663325273.jpg?k=9e55413fd1adeb890e3d5bf2459af7c86f6812a0367206fa09018b8f5d78f863&o=
18	5	https://cf.bstatic.com/xdata/images/hotel/max300/651462844.jpg?k=bcc722b74b35e7162563649672213eb77be8541a7a7fb009b86abb3d49ddba35&o=
19	6	https://cf.bstatic.com/xdata/images/hotel/max1024x768/628127287.jpg?k=08e5124830a453f4a2045f6d1d2b05bc6634515fd3258777546aadb873eb95c7&o=
20	6	https://cf.bstatic.com/xdata/images/hotel/max500/628120696.jpg?k=8f7a73d200471fc842798c457fae9be06552b98e08c13f197a9173aff54accb2&o=
21	6	https://cf.bstatic.com/xdata/images/hotel/max300/633128136.jpg?k=74502b77f71c00502450c09962a396b120ee2f36802b12bf0e12a3e4a761312b&o=
22	7	https://cf.bstatic.com/xdata/images/hotel/max1024x768/660976477.jpg?k=01145d915af7016dba4f46bd3e5b5251d6693b510f1125292cd57c93639db467&o=
23	7	https://cf.bstatic.com/xdata/images/hotel/max500/660976390.jpg?k=c2902d4bc6c2b83390a9aab3946dbf3f7c71cf5fdb57f5acb99ee4092bc120d6&o=
24	7	https://cf.bstatic.com/xdata/images/hotel/max300/660976404.jpg?k=231d0d084a5cb1a0597722e711b1f0ee6e42588897549cd1d23626fb3848357c&o=
25	8	https://cf.bstatic.com/xdata/images/hotel/max1024x768/149537773.jpg?k=eff368e14c293cd3d77a57e75a55ec3214ddeaf131f8c74af260349e3d95302c&o=
26	8	https://cf.bstatic.com/xdata/images/hotel/max500/149537628.jpg?k=277b27d68054d14fc4388c55b4e0fa94ff5d7d777f96baf8dd8164c19b3e57d2&o=
27	8	https://cf.bstatic.com/xdata/images/hotel/max300/149537775.jpg?k=d3531a10265bdc3987ba08d147d1d72bf4529f6c2b452e167e4c43363d518d84&o=
28	9	https://cf.bstatic.com/xdata/images/hotel/max1024x768/650206950.jpg?k=c69cb7117de33d77ffb71333bae089d663c1fc9774e347e56f1c14fd4ea43839&o=
29	9	https://cf.bstatic.com/xdata/images/hotel/max500/651181660.jpg?k=dc954edcadb050d900f108a4e27826ad86f8058bccfb9cb55bb640bf1b47093a&o=
30	9	https://cf.bstatic.com/xdata/images/hotel/max300/625876441.jpg?k=ebe9a9d97a1ebcc5fad3ad3c4195b303dfeff462d4278e9bdbcb817625b2ba48&o=
31	10	https://cf.bstatic.com/xdata/images/hotel/max1024x768/651974535.jpg?k=d4c6d30e95e4a3311c063ed78e2ffc444d31cf54bb94a4e49fb325039bd82142&o=
32	10	https://cf.bstatic.com/xdata/images/hotel/max500/652416172.jpg?k=76b6e4131a8ba886d8abbf85c43bcd53bd202f38665fd6e48bd6a4a3ddafccb8&o=
33	11	https://cf.bstatic.com/xdata/images/hotel/max1024x768/416041038.jpg?k=5b30cb6cf5bcc3640c46afed65a8afc7f438757ea73149e92df49fc64aaf2459&o=
34	11	https://cf.bstatic.com/xdata/images/hotel/max500/416041019.jpg?k=86389e217eb327b24bd5ef541c54caf129a14f250f7f31d15e8b3e127326e6b3&o=
35	11	https://cf.bstatic.com/xdata/images/hotel/max300/416040625.jpg?k=ea85fa54b7b8454b00e445f5e2c14f9c428f0bbc1bbe7f5ed30e7a764c9c3d06&o=
36	12	https://cf.bstatic.com/xdata/images/hotel/max1024x768/219028902.jpg?k=28c35961b941d825304d44408f7d3febc5cef6b07b2fa02bb76ab344f3c770f9&o=
37	12	https://cf.bstatic.com/xdata/images/hotel/max500/346331803.jpg?k=733d085b359c0ab7999804865dfef5f2151dcf2e3128082c7306ea9907ca9f26&o=
38	12	https://cf.bstatic.com/xdata/images/hotel/max500/346327027.jpg?k=facc13e29dc3641aa944090d14892f8755be858763da9789c0d4fc523d39fe8a&o=
39	13	https://cf.bstatic.com/xdata/images/hotel/max1024x768/499949834.jpg?k=c4c46d5a6ccf0f4c1a0617f28d87830ae56f5040b4a26f355b59ca423f428c7d&o=
40	13	https://cf.bstatic.com/xdata/images/hotel/max500/502442581.jpg?k=798cba3e5b0977eaafc979a5fd099d7adcd1e1c549c1e18dd35e688e1cf33c7b&o=
41	13	https://cf.bstatic.com/xdata/images/hotel/max300/502442619.jpg?k=ff83c3618b248f44d6ea1daa85fd60afedb5f68408203ed0f7c66d7418c6677d&o=
42	14	https://cf.bstatic.com/xdata/images/hotel/max1024x768/655707794.jpg?k=c842df9f450263ac2dc5bb3700d4939f881cd204d99ebb8920a9898670e28a10&o=
43	14	https://cf.bstatic.com/xdata/images/hotel/max500/655707594.jpg?k=b7309dfc6f2e2675ecd0f3c583a22ad44de457f4a4fd54333b0a3b3bf85ce398&o=
44	14	https://cf.bstatic.com/xdata/images/hotel/max300/598559770.jpg?k=f37cd5d3d662085566020b2a6060d265c3dead64f1dd138b6c4ee21190632a0f&o=
45	15	https://cf.bstatic.com/xdata/images/hotel/max1024x768/519106003.jpg?k=580831df791c4244808036ed0034b13bedc292f7a0c8e4af3daa272a816529e7&o=
46	15	https://cf.bstatic.com/xdata/images/hotel/max300/519105489.jpg?k=e341b9e7123ee35e15efe462f272dfeeb79e5137b9e044d3d5ccf3f52cbe3c1d&o=
47	15	https://cf.bstatic.com/xdata/images/hotel/max500/519106001.jpg?k=ed521dfdc01524fcf2e0637a3c65b77a3a84bee3096937d871021dc92441fcd9&o=
48	16	https://cf.bstatic.com/xdata/images/hotel/max1024x768/517294894.jpg?k=2e4585385378c2927dafd093294128225fdda255a04478b1eea2725351353d0d&o=
49	16	https://cf.bstatic.com/xdata/images/hotel/max500/517297078.jpg?k=2ad53b47c53cf0ac3f025ce5cd9c6d13b6419a23a56bc75a6a9a386cc5c00024&o=
210	2	https://res.cloudinary.com/dcerbz3nm/image/upload/c_fit,g_center,h_400,w_300/v1752488005/797ea9e0-5614-41f6-a662-f5171bdec8df?_a=DAGAACAWZAA0
50	16	https://cf.bstatic.com/xdata/images/hotel/max300/517294910.jpg?k=c6e0ac1daafe8c93e6d28894901968f17c5162cc9f39ddeb715f3583934861ca&o=
51	17	https://cf.bstatic.com/xdata/images/hotel/max1024x768/565227044.jpg?k=aa98c201757cd526b979df84c8e981e19b58dcd5330d3e84743022e93d822709&o=
52	17	https://cf.bstatic.com/xdata/images/hotel/max500/565230406.jpg?k=8d23eb0f95ea2647b5ef320f07e8a926fbf2cbf6af9a4ed86583ff56fec2797a&o=
53	17	https://cf.bstatic.com/xdata/images/hotel/max300/565229700.jpg?k=70711a5cdffcc7d8b58c4725d7895a3968e7c9510dd479a3d774f90e9451f453&o=
54	18	https://cf.bstatic.com/xdata/images/hotel/max500/497696103.jpg?k=1a2bdcfbf78a1fb61187daa613eb9522488876b5f3a1e6e91b5f143fb51741d4&o=
55	18	https://cf.bstatic.com/xdata/images/hotel/max500/497697922.jpg?k=2d00594d331009472ea7dc0741a0d767827de9288a069b3f8a0080d6ebb79ed9&o=
56	18	https://cf.bstatic.com/xdata/images/hotel/max300/497698292.jpg?k=11e3eb06ec82a29e803b0224bc94f885104152529b98a1a55f9dbebc7bfe39c1&o=
57	19	https://cf.bstatic.com/xdata/images/hotel/max1024x768/402605354.jpg?k=ae420689441bae26e7875787e2285cffe5246d9cfb6ade1be6c3022d7a9fd17e&o=
58	19	https://cf.bstatic.com/xdata/images/hotel/max500/402605567.jpg?k=9706ce76a8054613f6b02568af65fca39a50b86b1e1a8006efa07aab85308f08&o=
59	19	https://cf.bstatic.com/xdata/images/hotel/max300/402605742.jpg?k=f7449d89eedc98506d87a690c5be7d9da7d09eb55ab157b08a3fedd22362674b&o=
60	20	https://cf.bstatic.com/xdata/images/hotel/max1024x768/654321743.jpg?k=a1e75b8d2a7a358ab03f6fee3b5d14251f8d55748851d1a8c9314f245db421b0&o=
61	20	https://cf.bstatic.com/xdata/images/hotel/max500/654320357.jpg?k=88f12c1f5f4d35c9daa5dcdeb69ce231a5671b673a574d3885c5d629cc1bd316&o=
62	20	https://cf.bstatic.com/xdata/images/hotel/max300/638962386.jpg?k=c981638feca18737563139d2cade18a70e5298d0584a77fb6bab1270f665dec7&o=
63	21	https://cf.bstatic.com/xdata/images/hotel/max500/441655627.jpg?k=1384cfa8c00a31a3bcba0e798aa42c0460d76dee9109fe36f80a03ba891a5caf&o=
64	21	https://cf.bstatic.com/xdata/images/hotel/max300/441650359.jpg?k=149e34b3dfcabf6a5431fa6dbaad19305f1a2816c081d85602d4f3c60e4633ef&o=
65	21	https://cf.bstatic.com/xdata/images/hotel/max300/441639571.jpg?k=425028d569363b77abecccf02b5fed2c5c8a09dd848178870c81af17e6ecb51f&o=
211	2	https://res.cloudinary.com/dcerbz3nm/image/upload/c_fit,g_center,h_400,w_300/v1752488007/8c6060e8-d9b7-4505-ae09-d5a7efb7ab10?_a=DAGAACAWZAA0
212	2	https://res.cloudinary.com/dcerbz3nm/image/upload/c_fit,g_center,h_400,w_300/v1752488010/dbc586c2-41d6-45b5-90bf-1eeb1a0d4b0d?_a=DAGAACAWZAA0
219	45	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752490869/5ac875d5-53c9-491d-97b4-04b51b332ea5?_a=DAGAACAWZAA0
220	45	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752490871/074fb625-8862-47dd-b76b-60b9b3c1787a?_a=DAGAACAWZAA0
221	45	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752490873/e617bebf-5d2c-4232-8346-9292c2f4934b?_a=DAGAACAWZAA0
225	43	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752571793/b8eb7ff2-c1c3-4a83-89f4-f7b20d6af90b?_a=DAGAACAWZAA0
226	43	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752571795/0510767e-1281-43dc-bd7e-8804551905b4?_a=DAGAACAWZAA0
227	43	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752571798/3a44f0ee-46c1-416a-9cec-2593f96796f0?_a=DAGAACAWZAA0
213	1	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752488915/2bdb722b-7419-47bc-a9e0-12b3b6f67fe3?_a=DAGAACAWZAA0
214	1	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752488917/9337bf4e-6add-4bd1-80aa-05d504089ec0?_a=DAGAACAWZAA0
215	1	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752488919/a7926af9-e246-4e56-827d-f66844ef9d60?_a=DAGAACAWZAA0
216	44	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752489269/ff891fb9-6f16-4d17-9fd1-57a361fb3842?_a=DAGAACAWZAA0
217	44	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752489271/4728b2a9-8bdc-4056-b93a-c5fd16cbb4a2?_a=DAGAACAWZAA0
218	44	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752489274/6ad46bd5-e7b0-4648-aae6-27f812c75e84?_a=DAGAACAWZAA0
222	46	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752492695/d221485c-3b07-4e5c-9376-f0fb10974933?_a=DAGAACAWZAA0
223	46	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752492698/93173f48-141c-4500-ba14-e5799b91894a?_a=DAGAACAWZAA0
224	46	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752492700/9275aae8-7faa-4d16-afec-7d3e3d9bee99?_a=DAGAACAWZAA0
\.


--
-- TOC entry 6182 (class 0 OID 54122)
-- Dependencies: 287
-- Data for Name: property_type; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.property_type (id, name, created_at, created_by, updated_at, updated_by) FROM stdin;
2	Apartment	2025-03-25 15:44:50.210341	\N	\N	\N
3	Resort	2025-03-25 15:44:50.210341	\N	\N	\N
4	Villa	2025-03-25 15:44:50.210341	\N	\N	\N
5	Homestay	2025-03-25 15:44:50.210341	\N	\N	\N
1	Hotel	2025-03-25 15:44:50.210341	\N	2025-07-16 17:26:58.23724	manager@gmail.com
6	Bungalow	2025-07-16 17:33:35.129027	manager@gmail.com	\N	\N
\.


--
-- TOC entry 6176 (class 0 OID 45881)
-- Dependencies: 281
-- Data for Name: refund_bills; Type: TABLE DATA; Schema: public; Owner: postgres
--




--
-- TOC entry 6159 (class 0 OID 45605)
-- Dependencies: 264
-- Data for Name: review_images; Type: TABLE DATA; Schema: public; Owner: postgres
--




--
-- TOC entry 6158 (class 0 OID 45579)
-- Dependencies: 263
-- Data for Name: reviews; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6139 (class 0 OID 37286)
-- Dependencies: 244
-- Data for Name: role_permissions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.role_permissions (role_id, permission_id, created_at, updated_at) FROM stdin;
1	1	2025-04-07 20:54:46.250167	\N
1	2	2025-04-07 22:04:18.934378	\N
1	3	2025-04-07 22:04:18.934378	\N
1	4	2025-04-07 22:04:18.934378	\N
2	1	2025-04-07 22:04:18.934378	\N
2	2	2025-04-07 22:04:18.934378	\N
2	3	2025-04-07 22:04:18.934378	\N
2	4	2025-04-07 22:04:18.934378	\N
4	1	2025-05-14 15:01:16.488628	\N
4	2	2025-05-14 15:01:16.488628	\N
4	3	2025-05-14 15:01:16.488628	\N
4	4	2025-05-14 15:01:16.488628	\N
8	1	2025-07-20 17:36:29.142499	\N
8	2	2025-07-20 17:36:29.142499	\N
8	3	2025-07-20 17:36:29.142499	\N
8	4	2025-07-20 17:36:29.142499	\N
\.


--
-- TOC entry 6134 (class 0 OID 37186)
-- Dependencies: 239
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.roles (id, name, created_at, updated_at, created_by, updated_by) FROM stdin;
1	USER	2025-04-07 20:49:01.425205	\N	\N	\N
2	ADMIN	2025-04-07 20:49:01.425205	\N	\N	\N
3	STAFF	2025-04-07 20:49:01.425205	\N	\N	\N
4	MANAGER	2025-04-07 20:49:01.425205	\N	\N	\N
8	ADVISORY	2025-07-20 17:36:29.141076	\N	\N	\N
\.


--
-- TOC entry 6161 (class 0 OID 45619)
-- Dependencies: 266
-- Data for Name: room_chats; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6126 (class 0 OID 16483)
-- Dependencies: 226
-- Data for Name: room_type; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.room_type (id, property_id, name, price, max_guests, num_beds, created_at, area, discount, created_by, updated_at, updated_by, status) FROM stdin;
17	8	Garden Suite	2500000	3	2	2025-03-25 21:51:45.373497	55	7	\N	\N	\N	t
19	9	Luxury King Suite	2700000	3	2	2025-03-25 21:51:45.373497	70	8	\N	\N	\N	t
23	11	Premium Suite	3500000	4	2	2025-03-25 21:51:45.373497	75	9	\N	\N	\N	t
27	13	Family Suite with Balcony	1800000	4	2	2025-03-25 21:51:45.373497	50	7	\N	\N	\N	t
29	14	Presidential Suite with Sea View	5500000	4	2	2025-03-25 21:51:45.373497	85	12	\N	\N	\N	t
1	1	Deluxe Double Room	1168750	2	1	2025-03-25 21:25:34.829707	20	4	\N	\N	\N	t
2	1	Twin Double Room	660000	2	1	2025-03-25 21:51:45.373497	20	5	\N	\N	\N	t
3	1	Economy Double Room	1320000	2	1	2025-03-25 21:51:45.373497	30	4	\N	\N	\N	t
4	2	Deluxe King Room	880000	2	1	2025-03-25 21:51:45.373497	25	5	\N	\N	\N	t
5	2	Family Suite	1540000	4	2	2025-03-25 21:51:45.373497	45	7	\N	\N	\N	t
6	3	Standard Double Room	600000	2	1	2025-03-25 21:51:45.373497	18	3	\N	\N	\N	t
7	3	Luxury Suite	2200000	3	2	2025-03-25 21:51:45.373497	50	8	\N	\N	\N	t
8	4	Single Room	400000	1	1	2025-03-25 21:51:45.373497	15	2	\N	\N	\N	t
9	4	Presidential Suite	5000000	4	2	2025-03-25 21:51:45.373497	80	10	\N	\N	\N	t
10	5	Superior Twin Room	770000	2	2	2025-03-25 21:51:45.373497	22	4	\N	\N	\N	t
11	5	Budget Room	550000	2	1	2025-03-25 21:51:45.373497	18	2	\N	\N	\N	t
12	6	Executive Room	2100000	2	1	2025-03-25 21:51:45.373497	35	6	\N	\N	\N	t
13	6	Penthouse Suite	4200000	4	3	2025-03-25 21:51:45.373497	100	12	\N	\N	\N	t
14	7	Classic Room	780000	2	1	2025-03-25 21:51:45.373497	24	4	\N	\N	\N	t
15	7	Junior Suite	1650000	3	2	2025-03-25 21:51:45.373497	48	6	\N	\N	\N	t
16	8	Double Room with View	990000	2	1	2025-03-25 21:51:45.373497	30	5	\N	\N	\N	t
18	9	Economy Room	480000	1	1	2025-03-25 21:51:45.373497	20	2	\N	\N	\N	t
20	10	Standard Twin Room	650000	2	1	2025-03-25 21:51:45.373497	22	3	\N	\N	\N	t
21	10	Royal Suite	3900000	4	2	2025-03-25 21:51:45.373497	90	10	\N	\N	\N	t
22	11	Luxury Studio	1950000	2	1	2025-03-25 21:51:45.373497	38	6	\N	\N	\N	t
24	12	Single Budget Room	380000	1	1	2025-03-25 21:51:45.373497	14	2	\N	\N	\N	t
25	12	Double Luxury Room	990000	2	1	2025-03-25 21:51:45.373497	28	4	\N	\N	\N	t
26	13	Executive Twin Room	880000	2	1	2025-03-25 21:51:45.373497	26	4	\N	\N	\N	t
28	14	Cozy Double Room	720000	2	1	2025-03-25 21:51:45.373497	22	4	\N	\N	\N	t
30	15	Superior King Room	1100000	2	1	2025-03-25 21:51:45.373497	30	5	\N	\N	\N	t
31	15	Deluxe Family Suite	2000000	4	2	2025-03-25 21:51:45.373497	50	8	\N	\N	\N	t
32	16	Premium Room	990000	2	1	2025-03-25 21:51:45.373497	28	5	\N	\N	\N	t
33	16	Luxury Villa	7200000	6	3	2025-03-25 21:51:45.373497	150	15	\N	\N	\N	t
34	17	Cozy Single Room	500000	1	1	2025-03-25 21:51:45.373497	18	3	\N	\N	\N	t
35	17	Suite with Jacuzzi	2700000	3	2	2025-03-25 21:51:45.373497	60	9	\N	\N	\N	t
38	19	Standard Family Room	1300000	4	2	2025-03-25 21:51:45.373497	40	6	\N	\N	\N	t
39	19	Executive Suite with Pool View	4600000	4	2	2025-03-25 21:51:45.373497	100	12	\N	\N	\N	t
40	20	Simple Double Room	690000	2	1	2025-03-25 21:51:45.373497	22	3	\N	\N	\N	t
41	20	Royal Penthouse Suite	8800000	6	3	2025-03-25 21:51:45.373497	200	20	\N	\N	\N	t
36	18	Deluxe Twin Room	820000	2	1	2025-03-25 21:51:45.373497	27	4	\N	\N	\N	t
37	18	Luxury Apartment	3500000	4	2	2025-03-25 21:51:45.373497	80	10	\N	\N	\N	t
50	1	Conference Room	1260000	8	2	2025-06-28 22:26:50.86892	40	6	manager@gmail.com	\N	\N	t
53	43	Super Vip Room	1400000	2	1	2025-07-04 10:37:25.66113	30	3	manager@gmail.com	\N	\N	t
\.


--
-- TOC entry 6128 (class 0 OID 16668)
-- Dependencies: 228
-- Data for Name: rooms; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.rooms (id, room_number, status, property_id, room_type_id, created_at, created_by, updated_at, updated_by, check_in, check_out) FROM stdin;
125	105	available	4	8	2025-06-25 10:15:40.750753	manager@gmail.com	2025-07-09 16:15:22.100261	\N	2025-07-09 07:00:00	2025-07-11 07:00:00
124	104	available	4	8	2025-06-25 10:13:58.358147	manager@gmail.com	2025-07-08 17:05:45.589278	\N	2025-07-08 07:00:00	2025-07-09 07:00:00
114	103	available	19	38	2025-03-27 11:31:14.281776	\N	2025-07-09 10:35:08.174352	\N	2025-07-09 07:00:00	2025-07-10 07:00:00
109	201	available	18	37	2025-03-27 11:31:14.281776	\N	2025-05-04 22:59:28.844154	\N	2025-05-15 07:00:00	2025-05-17 07:00:00
110	202	available	18	37	2025-03-27 11:31:14.281776	\N	2025-05-04 22:59:28.850155	\N	2025-05-15 07:00:00	2025-05-17 07:00:00
4	201	available	1	2	2025-03-27 11:31:14.281776	\N	2025-05-17 22:33:45.755856	\N	2025-05-14 07:00:00	2025-05-16 07:00:00
118	101	available	20	40	2025-03-27 11:31:14.281776	\N	2025-05-23 15:28:25.814384	\N	2025-05-24 07:00:00	2025-05-25 07:00:00
106	101	available	18	36	2025-03-27 11:31:14.281776	\N	2025-05-23 15:40:54.789672	\N	2025-05-29 07:00:00	2025-05-30 07:00:00
107	102	available	18	36	2025-03-27 11:31:14.281776	\N	2025-06-17 21:28:03.164417	\N	2025-06-25 07:00:00	2025-06-28 07:00:00
119	102	available	20	40	2025-03-27 11:31:14.281776	\N	2025-06-17 21:29:41.197817	\N	2025-06-27 07:00:00	2025-06-28 07:00:00
115	201	available	19	39	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
116	202	available	19	39	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
117	203	available	19	39	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
121	201	available	20	41	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
120	103	available	20	40	2025-03-27 11:31:14.281776	\N	2025-07-04 10:03:33.951916	\N	2025-07-25 07:00:00	2025-07-26 07:00:00
112	101	available	19	38	2025-03-27 11:31:14.281776	\N	2025-07-04 10:45:17.037252	\N	2025-07-25 07:00:00	2025-07-26 07:00:00
113	102	available	19	38	2025-03-27 11:31:14.281776	\N	2025-07-04 10:48:27.6105	\N	2025-07-25 07:00:00	2025-07-26 07:00:00
122	202	available	20	41	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
123	203	available	20	41	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
111	203	available	18	37	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
108	103	available	18	36	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
5	202	available	1	2	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
55	201	available	9	19	2025-03-27 11:31:14.281776	\N	2025-07-08 17:09:37.746333	\N	2025-07-09 07:00:00	2025-07-10 07:00:00
43	201	available	7	15	2025-03-27 11:31:14.281776	\N	\N	\N	2025-05-01 07:00:00	2025-05-02 07:00:00
42	103	available	7	14	2025-03-27 11:31:14.281776	\N	2025-07-08 10:25:18.775309	\N	2025-07-08 07:00:00	2025-07-09 07:00:00
10	101	available	2	4	2025-03-27 11:31:14.281776	\N	2025-07-08 16:26:24.291098	\N	2025-07-09 07:00:00	2025-07-10 07:00:00
31	201	available	5	11	2025-03-27 11:31:14.281776	\N	2025-05-17 21:56:57.13172	\N	2025-05-27 07:00:00	2025-05-28 07:00:00
29	102	available	5	10	2025-03-27 11:31:14.281776	\N	2025-07-08 16:48:10.576297	\N	2025-07-08 07:00:00	2025-07-09 07:00:00
33	203	available	5	11	2025-03-27 11:31:14.281776	\N	2025-07-08 17:10:54.267458	\N	2025-07-08 07:00:00	2025-07-09 07:00:00
22	101	available	4	8	2025-03-27 11:31:14.281776	\N	2025-07-08 17:13:16.567481	\N	2025-07-09 07:00:00	2025-07-10 07:00:00
23	102	available	4	8	2025-03-27 11:31:14.281776	\N	2025-07-08 17:18:26.762115	\N	2025-07-08 07:00:00	2025-07-09 07:00:00
28	101	available	5	10	2025-03-27 11:31:14.281776	\N	2025-07-09 10:37:11.648072	\N	2025-07-09 07:00:00	2025-07-10 07:00:00
146	101	available	43	53	2025-07-04 10:37:25.70305	manager@gmail.com	2025-07-09 16:17:18.91281	\N	2025-07-09 07:00:00	2025-07-11 07:00:00
145	102	available	43	53	2025-07-04 10:37:25.698033	manager@gmail.com	2025-07-09 16:17:18.938813	\N	2025-07-09 07:00:00	2025-07-11 07:00:00
46	101	available	8	16	2025-03-27 11:31:14.281776	\N	2025-07-09 16:26:20.443211	\N	2025-07-09 07:00:00	2025-07-10 07:00:00
13	201	available	2	5	2025-03-27 11:31:14.281776	\N	2025-07-09 16:30:24.696442	\N	2025-07-09 07:00:00	2025-07-11 07:00:00
14	202	available	2	5	2025-03-27 11:31:14.281776	\N	2025-07-09 16:30:24.701474	\N	2025-07-09 07:00:00	2025-07-11 07:00:00
15	203	available	2	5	2025-03-27 11:31:14.281776	\N	2025-07-09 16:30:24.705423	\N	2025-07-09 07:00:00	2025-07-11 07:00:00
51	203	available	8	17	2025-03-27 11:31:14.281776	\N	2025-07-09 17:02:24.460517	\N	2025-07-09 07:00:00	2025-07-12 07:00:00
138	100	available	1	50	2025-06-28 22:26:50.91331	manager@gmail.com	\N	\N	\N	\N
49	201	available	8	17	2025-03-27 11:31:14.281776	\N	2025-07-11 17:51:01.057394	\N	2025-07-17 07:00:00	2025-07-19 07:00:00
50	202	available	8	17	2025-03-27 11:31:14.281776	\N	2025-07-11 17:51:01.108033	\N	2025-07-17 07:00:00	2025-07-19 07:00:00
40	101	available	7	14	2025-03-27 11:31:14.281776	\N	\N	\N	2025-05-01 07:00:00	2025-05-03 07:00:00
45	203	available	7	15	2025-03-27 11:31:14.281776	\N	\N	\N	2025-05-01 07:00:00	2025-05-03 07:00:00
30	103	available	5	10	2025-03-27 11:31:14.281776	\N	2025-07-13 20:20:53.264985	\N	2025-07-13 07:00:00	2025-07-15 07:00:00
44	202	available	7	15	2025-03-27 11:31:14.281776	\N	\N	\N	2025-05-01 07:00:00	2025-05-03 07:00:00
41	102	available	7	14	2025-03-27 11:31:14.281776	\N	\N	\N	2025-05-01 07:00:00	2025-05-02 07:00:00
32	202	available	5	11	2025-03-27 11:31:14.281776	\N	\N	\N	2025-05-01 07:00:00	2025-05-03 07:00:00
6	203	available	1	2	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
7	301	available	1	3	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
8	302	available	1	3	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
9	303	available	1	3	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
11	102	available	2	4	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
12	103	available	2	4	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
16	101	available	3	6	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
17	102	available	3	6	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
18	103	available	3	6	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
19	201	available	3	7	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
20	202	available	3	7	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
21	203	available	3	7	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
24	103	available	4	8	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
25	201	available	4	9	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
26	202	available	4	9	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
27	203	available	4	9	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
34	101	available	6	12	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
35	102	available	6	12	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
36	103	available	6	12	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
37	201	available	6	13	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
38	202	available	6	13	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
39	203	available	6	13	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
47	102	available	8	16	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
48	103	available	8	16	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
52	101	available	9	18	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
53	102	available	9	18	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
54	103	available	9	18	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
56	202	available	9	19	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
57	203	available	9	19	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
58	101	available	10	20	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
59	102	available	10	20	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
60	103	available	10	20	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
61	201	available	10	21	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
62	202	available	10	21	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
63	203	available	10	21	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
64	101	available	11	22	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
65	102	available	11	22	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
66	103	available	11	22	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
73	201	available	12	25	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
74	202	available	12	25	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
75	203	available	12	25	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
76	101	available	13	26	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
77	102	available	13	26	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
78	103	available	13	26	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
79	201	available	13	27	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
80	202	available	13	27	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
81	203	available	13	27	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
82	101	available	14	28	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
83	102	available	14	28	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
84	103	available	14	28	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
86	202	available	14	29	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
87	203	available	14	29	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
89	102	available	15	30	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
90	103	available	15	30	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
91	201	available	15	31	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
92	202	available	15	31	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
93	203	available	15	31	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
94	101	available	16	32	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
95	102	available	16	32	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
96	103	available	16	32	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
97	201	available	16	33	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
98	202	available	16	33	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
99	203	available	16	33	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
100	101	available	17	34	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
101	102	available	17	34	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
102	103	available	17	34	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
103	201	available	17	35	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
104	202	available	17	35	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
105	203	available	17	35	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
71	102	available	12	24	2025-03-27 11:31:14.281776	\N	2025-07-13 21:35:46.775294	\N	2025-07-13 07:00:00	2025-07-14 07:00:00
85	201	available	14	29	2025-03-27 11:31:14.281776	\N	2025-05-17 10:52:08.505691	\N	2025-05-16 07:00:00	2025-05-17 07:00:00
88	101	available	15	30	2025-03-27 11:31:14.281776	\N	2025-05-17 11:02:50.806398	\N	2025-05-16 07:00:00	2025-05-17 07:00:00
72	103	available	12	24	2025-03-27 11:31:14.281776	\N	2025-05-22 22:59:18.269463	\N	2025-05-09 07:00:00	2025-05-10 07:00:00
70	101	available	12	24	2025-03-27 11:31:14.281776	\N	2025-05-23 00:03:42.549616	\N	2025-05-30 07:00:00	2025-05-31 07:00:00
69	203	available	11	23	2025-03-27 11:31:14.281776	\N	2025-05-23 14:50:05.434793	\N	2025-05-30 07:00:00	2025-05-31 07:00:00
140	101	available	1	1	2025-06-29 00:18:24.789683	manager@gmail.com	\N	\N	\N	\N
141	102	available	1	1	2025-06-29 00:18:24.791689	manager@gmail.com	\N	\N	\N	\N
67	201	available	11	23	2025-03-27 11:31:14.281776	\N	2025-07-04 10:41:58.568929	\N	2025-07-29 07:00:00	2025-07-30 07:00:00
139	103	available	1	1	2025-06-29 00:18:24.778265	manager@gmail.com	2025-07-08 11:42:26.636202	\N	2025-07-08 07:00:00	2025-07-09 07:00:00
68	202	available	11	23	2025-03-27 11:31:14.281776	\N	2025-07-08 16:13:38.348587	\N	2025-07-08 07:00:00	2025-07-09 07:00:00
\.


--
-- TOC entry 6177 (class 0 OID 45889)
-- Dependencies: 282
-- Data for Name: roomtypes_facilities; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.roomtypes_facilities (facility_id, room_type_id) FROM stdin;
8	50
6	1
7	1
7	50
7	53
4	50
4	53
2	53
1	53
5	53
\.


--
-- TOC entry 5809 (class 0 OID 28083)
-- Dependencies: 232
-- Data for Name: spatial_ref_sys; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.spatial_ref_sys (srid, auth_name, auth_srid, srtext, proj4text) FROM stdin;
\.


--
-- TOC entry 6180 (class 0 OID 54104)
-- Dependencies: 285
-- Data for Name: suspicious_payment_log; Type: TABLE DATA; Schema: public; Owner: postgres
--




--
-- TOC entry 6124 (class 0 OID 16449)
-- Dependencies: 224
-- Data for Name: trip; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.trip (id, name, trip_type, city_id, latitude, longitude, image, created_at, created_by, updated_at, updated_by, geog, slug) FROM stdin;
13	Cột Cờ Lũng Cú	Mountain	7	23.3631182814	105.3149034009	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752596290/d911a55f-1ab2-4d91-82fe-cad3aeecc52a?_a=DAGAACAWZAA0	2025-07-15 23:18:11.951042	manager@gmail.com	\N	\N	\N	cot-co-lung-cu
14	Đèo Mã Pí Lèng	Mountain	7	23.2410245235	105.4002145112	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752596418/180a1923-ea03-46bb-a5c3-3922b3fd5a1c?_a=DAGAACAWZAA0	2025-07-15 23:20:19.48159	manager@gmail.com	\N	\N	\N	eo-ma-pi-leng
15	Văn Miếu Quốc Tử Giám	Historical Trip	1	21.0282476670	105.8357121112	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752657101/ec1b3bf4-bace-4b04-ba7b-00b087dc2ebb?_a=DAGAACAWZAA0	2025-07-16 16:11:43.724655	manager@gmail.com	\N	\N	\N	van-mieu-quoc-tu-giam
16	Lễ hội hoa Đà Lạt	Festival Trip	4	11.9512032178	108.4484323137	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752657581/f3ab464f-e458-4b43-9da1-344fad2def2e?_a=DAGAACAWZAA0	2025-07-16 16:19:43.548031	manager@gmail.com	\N	\N	\N	le-hoi-hoa-a-lat
2	Hồ Tây	Outdoors	1	21.0531446601	105.8246799503	https://lh5.googleusercontent.com/p/AF1QipNMXIxamQP1T5Y8-MWHHivPt109t7KUeS3Fra_c=w408-h271-k-no	2025-03-25 15:44:50.210341	\N	\N	\N	0101000020E610000014AE47E17A745A40CDCCCCCCCC0C3540	\N
3	Hồ Hoàn Kiếm	Outdoors	1	21.0280837424	105.8521715769	https://lh5.googleusercontent.com/p/AF1QipN3-_0wrzFsf30vYg5nR6mwLroFyNp-qYsnV6B6=w408-h246-k-no	2025-03-25 15:44:50.210341	\N	\N	\N	0101000020E61000006666666666765A4048E17A14AE073540	\N
4	Phố đường tàu	Outdoors	1	21.0336403064	105.8453911495	https://lh5.googleusercontent.com/p/AF1QipMML6HcF8W-K9js2Bo2pr5aRVip57Fn7bTlPswY=w408-h510-k-no	2025-03-25 15:44:50.210341	\N	\N	\N	0101000020E6100000F6285C8FC2755A4048E17A14AE073540	\N
5	Biển Nha Trang	Beach	5	12.2392408962	109.1991084765	https://lh3.googleusercontent.com/p/AF1QipNhKiLpbzC-Sa2JyfXz95YKUBdxhHfIeHiYuw3O=w426-h240-k-no	2025-03-25 15:44:50.210341	\N	\N	\N	0101000020E6100000CDCCCCCCCC4C5B407B14AE47E17A2840	\N
6	Bãi biển Thụy Khê	Beach	3	16.0636534937	108.2482253592	https://lh3.googleusercontent.com/p/AF1QipPgpkvaWeKD9pejm2Org-oEx-SWXLyGH_qSUneu=s294-w294-h220-k-no	2025-03-25 15:44:50.210341	\N	\N	\N	0101000020E61000000000000000105B4052B81E85EB113040	\N
7	Cầu rồng	Outdoors	3	16.0613874028	108.2296927391	https://lh5.googleusercontent.com/p/AF1QipORTOLHKjRkpcwmf9QE6qs_smZwgf-tPEdkwxlL=w408-h272-k-no	2025-03-25 15:44:50.210341	\N	\N	\N	0101000020E61000001F85EB51B80E5B408FC2F5285C0F3040	\N
8	Ba Na Hills	Outdoors	3	16.0268767431	108.0396891553	https://lh5.googleusercontent.com/p/AF1QipMW717XyWoWQH5nR6KQ1ukbcSczJNGcbU-On08r=w426-h240-k-no	2025-03-25 15:44:50.210341	\N	\N	\N	0101000020E61000000000000000005B400000000000003040	\N
9	Đà Lạt	Outdoors	4	11.9324522531	108.4644512975	https://nhuytravel.net/wp-content/uploads/2023/08/samten-hills-da-lat-savingbooking1.jpg	2025-03-25 15:44:50.210341	\N	\N	\N	0101000020E61000008FC2F5285C1F5B4048E17A14AEC72740	\N
1	Lăng chủ tịch	Outdoors	1	21.0370775281	105.8348061708	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752593525/742a934a-cc02-4112-b3b5-7fdc83c4b772?_a=DAGAACAWZAA0	2025-03-25 15:44:50.210341	\N	2025-07-15 22:32:06.663006	manager@gmail.com	0101000020E610000085EB51B81E755A400AD7A3703D0A3540	\N
\.


--
-- TOC entry 6184 (class 0 OID 54143)
-- Dependencies: 289
-- Data for Name: trip_type; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.trip_type (id, name, icon, created_at, created_by, updated_at, updated_by) FROM stdin;
2	Outdoors	https://th.bing.com/th/id/OIP.Sz5Ql26kUfBgxY7EQ9cx6gHaHa?w=1920&h=1920&rs=1&pid=ImgDetMain	2025-07-15 22:32:06.663006	\N	\N	\N
3	Mountain	https://th.bing.com/th/id/OIP.zj8ZqZUmyGR4g88LV7tIQAHaHa?rs=1&pid=ImgDetMain	2025-07-15 22:32:06.663006	\N	\N	\N
1	Beach	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752655614/54d0b269-f7fd-460b-a0a5-b68eb5086982?_a=DAGAACAWZAA0	2025-07-15 22:32:06.663006	\N	2025-07-16 15:46:56.756145	manager@gmail.com
4	City Trip	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752656800/a3320280-7ff6-4581-a5a6-1a39cbe35c3c?_a=DAGAACAWZAA0	2025-07-16 16:06:42.254063	manager@gmail.com	\N	\N
5	Historical Trip	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752656904/8eca99dc-9e6e-4688-ae89-b48e0794b057?_a=DAGAACAWZAA0	2025-07-16 16:08:26.97275	manager@gmail.com	\N	\N
6	Festival Trip	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752657453/9ec9d0ee-0827-4e24-acb5-ca364dc0d861?_a=DAGAACAWZAA0	2025-07-16 16:17:35.654971	manager@gmail.com	\N	\N
\.


--
-- TOC entry 6145 (class 0 OID 37465)
-- Dependencies: 250
-- Data for Name: user_discount_cars; Type: TABLE DATA; Schema: public; Owner: postgres
--


--
-- TOC entry 6140 (class 0 OID 37303)
-- Dependencies: 245
-- Data for Name: user_discounts; Type: TABLE DATA; Schema: public; Owner: postgres
--



--
-- TOC entry 6138 (class 0 OID 37268)
-- Dependencies: 243
-- Data for Name: user_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--


-- TOC entry 6168 (class 0 OID 45673)
-- Dependencies: 273
-- Data for Name: user_visits; Type: TABLE DATA; Schema: public; Owner: postgres
--


--
-- TOC entry 6132 (class 0 OID 37168)
-- Dependencies: 237
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--


--
-- TOC entry 6147 (class 0 OID 37504)
-- Dependencies: 252
-- Data for Name: vehicles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.vehicles (id, license_plate, car_type, images, latitude, longitude, discount, price, created_at, updated_at, updated_by, created_by, driver_id, status, quantity, star) FROM stdin;
10	VWX-6541	TAXI	https://th.bing.com/th/id/OIP.GO845nN3RCxQzQEuKeT0AgHaEo?rs=1&pid=ImgDetMain	10.812622	106.710172	20	180000	2025-04-22 23:47:38.645694	2025-04-22 23:47:38.645694	\N	\N	10	INACTIVE	2	5
3	LMN-9876	SEAT_4	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752944895/52cc514b-f4df-4e5b-be76-510e2b75a626?_a=DAGAACAWZAA0	10.762622	106.660172	5	400000	2025-04-22 23:47:38.645694	2025-07-20 00:08:15.736818	manager@gmail.com	\N	3	INACTIVE	5	4
11	QH-1234	BUS	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752978667/be025283-d59a-49b1-acaa-7070c1f989f9?_a=DAGAACAWZAA0	\N	\N	5	50000	2025-07-20 09:31:08.388072	\N	\N	manager@gmail.com	\N	AVAILABLE	6	3
2	XYZ-5678	SEAT_4	https://th.bing.com/th/id/OIP.bWrR3rouX6qybc67nosWqQAAAA?rs=1&pid=ImgDetMain	10.762622	106.660172	15	300000	2025-04-22 23:47:38.645694	2025-04-22 23:47:38.645694	\N	\N	2	AVAILABLE	1	3
5	TAX-3210	TAXI	https://th.bing.com/th/id/OIP.RjDv4d9VadyiPHCvFaYQpgHaE4?rs=1&pid=ImgDetMain	10.762622	106.660172	10	150000	2025-04-22 23:47:38.645694	2025-04-22 23:47:38.645694	\N	\N	5	AVAILABLE	1	4
1	ABC-1234	BUS	https://res.cloudinary.com/dcerbz3nm/image/upload/g_center,q_100/v1752944175/501a7323-cf22-447c-8dd0-82a472cbdf77?_a=DAGAACAWZAA0	10.762622	106.660172	10	500000	2025-04-22 23:47:38.645694	2025-07-19 23:56:15.911715	manager@gmail.com	\N	1	AVAILABLE	5	2
4	DEF-4321	LIMOUSINE	https://flamingoresortdailai.com/wp-content/uploads/2017/01/thue-xe-limousine-16-cho-tai-ha-noi.jpg	10.762622	106.660172	20	1000000	2025-04-22 23:47:38.645694	2025-04-22 23:47:38.645694	\N	\N	4	INACTIVE	3	3
6	JKL-3456	SEAT_4	https://th.bing.com/th/id/OIP.-fCbQxDhOvNfPt65_JDWfwHaE8?rs=1&pid=ImgDetMain	10.773622	106.672172	5	350000	2025-04-22 23:47:38.645694	2025-04-22 23:47:38.645694	\N	\N	6	AVAILABLE	4	3
7	MNO-6543	SEAT_7	https://th.bing.com/th/id/OIP.Ho_xcLEbvubh-hyw2KXn1AHaEg?rs=1&pid=ImgDetMain	10.782622	106.680172	10	450000	2025-04-22 23:47:38.645694	2025-04-22 23:47:38.645694	\N	\N	7	AVAILABLE	5	2
8	PQR-8765	LIMOUSINE	https://th.bing.com/th/id/R.2fd0f99d11179dea5a638b74202eee8d?rik=Zrg9ZyeYQuYQiw&riu=http%3a%2f%2fwww.absolutelimos.ie%2fwp-content%2fuploads%2f2013%2f08%2fThe-16-Seater-Party-Limo-Bus-1.jpg&ehk=kUCZAbOsangNxggLwY48%2bK4PralBHTzW4iDzSkUpksU%3d&risl=&pid=ImgRaw&r=0	10.792622	106.690172	15	1200000	2025-04-22 23:47:38.645694	2025-04-22 23:47:38.645694	\N	\N	8	BUSY	2	4
9	STU-9870	BUS	https://th.bing.com/th/id/OIP.cAko7iWNwEE87_DGYA7LgQHaFj?w=234&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7	10.802622	106.700172	5	550000	2025-04-22 23:47:38.645694	2025-04-22 23:47:38.645694	\N	\N	9	AVAILABLE	3	3
\.


--
-- TOC entry 6219 (class 0 OID 0)
-- Dependencies: 255
-- Name: bill_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.bill_id_seq', 206, true);


--
-- TOC entry 6220 (class 0 OID 0)
-- Dependencies: 259
-- Name: booking_cars_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.booking_cars_id_seq', 81, true);


--
-- TOC entry 6221 (class 0 OID 0)
-- Dependencies: 257
-- Name: booking_rooms_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.booking_rooms_id_seq', 228, true);


--
-- TOC entry 6222 (class 0 OID 0)
-- Dependencies: 267
-- Name: chats_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.chats_id_seq', 101, true);


--
-- TOC entry 6223 (class 0 OID 0)
-- Dependencies: 219
-- Name: cities_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.cities_id_seq', 7, true);


--
-- TOC entry 6224 (class 0 OID 0)
-- Dependencies: 247
-- Name: discount_cars_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.discount_cars_id_seq', 11, true);


--
-- TOC entry 6225 (class 0 OID 0)
-- Dependencies: 229
-- Name: discount_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.discount_id_seq', 12, true);


--
-- TOC entry 6226 (class 0 OID 0)
-- Dependencies: 253
-- Name: drivers_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.drivers_id_seq', 10, true);


--
-- TOC entry 6227 (class 0 OID 0)
-- Dependencies: 274
-- Name: facilities_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.facilities_id_seq', 24, true);


--
-- TOC entry 6228 (class 0 OID 0)
-- Dependencies: 270
-- Name: notifications_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.notifications_id_seq', 158, true);


--
-- TOC entry 6229 (class 0 OID 0)
-- Dependencies: 278
-- Name: payment_transaction_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.payment_transaction_id_seq', 125, true);


--
-- TOC entry 6230 (class 0 OID 0)
-- Dependencies: 240
-- Name: permissions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.permissions_id_seq', 5, true);


--
-- TOC entry 6231 (class 0 OID 0)
-- Dependencies: 221
-- Name: properties_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.properties_id_seq', 46, true);


--
-- TOC entry 6232 (class 0 OID 0)
-- Dependencies: 276
-- Name: property_images_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.property_images_id_seq', 227, true);


--
-- TOC entry 6233 (class 0 OID 0)
-- Dependencies: 286
-- Name: property_type_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.property_type_id_seq', 6, true);


--
-- TOC entry 6234 (class 0 OID 0)
-- Dependencies: 280
-- Name: refund_bills_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.refund_bills_id_seq', 18, true);


--
-- TOC entry 6235 (class 0 OID 0)
-- Dependencies: 262
-- Name: reviews_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.reviews_id_seq', 44, true);


--
-- TOC entry 6236 (class 0 OID 0)
-- Dependencies: 238
-- Name: roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.roles_id_seq', 8, true);


--
-- TOC entry 6237 (class 0 OID 0)
-- Dependencies: 265
-- Name: room_chats_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.room_chats_id_seq', 16, true);


--
-- TOC entry 6238 (class 0 OID 0)
-- Dependencies: 225
-- Name: room_type_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.room_type_id_seq', 53, true);


--
-- TOC entry 6239 (class 0 OID 0)
-- Dependencies: 227
-- Name: rooms_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.rooms_id_seq', 146, true);


--
-- TOC entry 6240 (class 0 OID 0)
-- Dependencies: 284
-- Name: suspicious_payment_log_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.suspicious_payment_log_id_seq', 25, true);


--
-- TOC entry 6241 (class 0 OID 0)
-- Dependencies: 223
-- Name: trip_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.trip_id_seq', 16, true);


--
-- TOC entry 6242 (class 0 OID 0)
-- Dependencies: 288
-- Name: trip_type_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.trip_type_id_seq', 6, true);


--
-- TOC entry 6243 (class 0 OID 0)
-- Dependencies: 249
-- Name: user_discount_cars_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_discount_cars_id_seq', 10, true);


--
-- TOC entry 6244 (class 0 OID 0)
-- Dependencies: 246
-- Name: user_discounts_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_discounts_seq', 801, true);


--
-- TOC entry 6245 (class 0 OID 0)
-- Dependencies: 242
-- Name: user_roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_roles_id_seq', 67, true);


--
-- TOC entry 6246 (class 0 OID 0)
-- Dependencies: 272
-- Name: user_visits_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_visits_id_seq', 767, true);


--
-- TOC entry 6247 (class 0 OID 0)
-- Dependencies: 236
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 91, true);


--
-- TOC entry 6248 (class 0 OID 0)
-- Dependencies: 251
-- Name: vehicles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.vehicles_id_seq', 11, true);


--
-- TOC entry 5910 (class 2606 OID 45514)
-- Name: bill bill_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bill
    ADD CONSTRAINT bill_pkey PRIMARY KEY (id);


--
-- TOC entry 5914 (class 2606 OID 45549)
-- Name: booking_cars booking_cars_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.booking_cars
    ADD CONSTRAINT booking_cars_pkey PRIMARY KEY (id);


--
-- TOC entry 5912 (class 2606 OID 45529)
-- Name: booking_rooms booking_rooms_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.booking_rooms
    ADD CONSTRAINT booking_rooms_pkey PRIMARY KEY (id);


--
-- TOC entry 5920 (class 2606 OID 45633)
-- Name: chats chats_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chats
    ADD CONSTRAINT chats_pkey PRIMARY KEY (id);


--
-- TOC entry 5866 (class 2606 OID 16399)
-- Name: cities cities_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cities
    ADD CONSTRAINT cities_name_key UNIQUE (name);


--
-- TOC entry 5868 (class 2606 OID 16397)
-- Name: cities cities_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cities
    ADD CONSTRAINT cities_pkey PRIMARY KEY (id);


--
-- TOC entry 5898 (class 2606 OID 37463)
-- Name: discount_cars discount_cars_code_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discount_cars
    ADD CONSTRAINT discount_cars_code_key UNIQUE (code);


--
-- TOC entry 5900 (class 2606 OID 37424)
-- Name: discount_cars discount_cars_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discount_cars
    ADD CONSTRAINT discount_cars_pkey PRIMARY KEY (id);


--
-- TOC entry 5880 (class 2606 OID 24926)
-- Name: discount discount_code_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discount
    ADD CONSTRAINT discount_code_key UNIQUE (code);


--
-- TOC entry 5882 (class 2606 OID 24907)
-- Name: discount discount_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discount
    ADD CONSTRAINT discount_pkey PRIMARY KEY (id);


--
-- TOC entry 5908 (class 2606 OID 37524)
-- Name: drivers drivers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.drivers
    ADD CONSTRAINT drivers_pkey PRIMARY KEY (id);


--
-- TOC entry 5926 (class 2606 OID 45703)
-- Name: facilities facilities_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.facilities
    ADD CONSTRAINT facilities_pkey PRIMARY KEY (id);


--
-- TOC entry 5922 (class 2606 OID 45656)
-- Name: notifications notifications_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notifications
    ADD CONSTRAINT notifications_pkey PRIMARY KEY (id);


--
-- TOC entry 5930 (class 2606 OID 45878)
-- Name: payment_transaction payment_transaction_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment_transaction
    ADD CONSTRAINT payment_transaction_pkey PRIMARY KEY (id);


--
-- TOC entry 5890 (class 2606 OID 37218)
-- Name: permissions permissions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.permissions
    ADD CONSTRAINT permissions_pkey PRIMARY KEY (id);


--
-- TOC entry 5870 (class 2606 OID 16442)
-- Name: properties properties_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.properties
    ADD CONSTRAINT properties_pkey PRIMARY KEY (id);


--
-- TOC entry 5928 (class 2606 OID 45729)
-- Name: property_images property_images_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.property_images
    ADD CONSTRAINT property_images_pkey PRIMARY KEY (id);


--
-- TOC entry 5936 (class 2606 OID 54156)
-- Name: property_type property_type_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.property_type
    ADD CONSTRAINT property_type_name_key UNIQUE (name);


--
-- TOC entry 5938 (class 2606 OID 54127)
-- Name: property_type property_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.property_type
    ADD CONSTRAINT property_type_pkey PRIMARY KEY (id);


--
-- TOC entry 5932 (class 2606 OID 45887)
-- Name: refund_bills refund_bills_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.refund_bills
    ADD CONSTRAINT refund_bills_pkey PRIMARY KEY (id);


--
-- TOC entry 5916 (class 2606 OID 45586)
-- Name: reviews reviews_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reviews
    ADD CONSTRAINT reviews_pkey PRIMARY KEY (id);


--
-- TOC entry 5894 (class 2606 OID 54167)
-- Name: role_permissions role_permissions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role_permissions
    ADD CONSTRAINT role_permissions_pkey PRIMARY KEY (role_id, permission_id);


--
-- TOC entry 5888 (class 2606 OID 37192)
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- TOC entry 5918 (class 2606 OID 45624)
-- Name: room_chats room_chats_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room_chats
    ADD CONSTRAINT room_chats_pkey PRIMARY KEY (id);


--
-- TOC entry 5876 (class 2606 OID 16491)
-- Name: room_type room_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room_type
    ADD CONSTRAINT room_type_pkey PRIMARY KEY (id);


--
-- TOC entry 5878 (class 2606 OID 16675)
-- Name: rooms rooms_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rooms
    ADD CONSTRAINT rooms_pkey PRIMARY KEY (id);


--
-- TOC entry 5934 (class 2606 OID 54112)
-- Name: suspicious_payment_log suspicious_payment_log_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.suspicious_payment_log
    ADD CONSTRAINT suspicious_payment_log_pkey PRIMARY KEY (id);


--
-- TOC entry 5872 (class 2606 OID 16456)
-- Name: trip trip_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trip
    ADD CONSTRAINT trip_pkey PRIMARY KEY (id);


--
-- TOC entry 5940 (class 2606 OID 54165)
-- Name: trip_type trip_type_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trip_type
    ADD CONSTRAINT trip_type_name_key UNIQUE (name);


--
-- TOC entry 5942 (class 2606 OID 54150)
-- Name: trip_type trip_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trip_type
    ADD CONSTRAINT trip_type_pkey PRIMARY KEY (id);


--
-- TOC entry 5874 (class 2606 OID 28971)
-- Name: trip uk6kuu1nwvxvyi4kdg6f8m43t2h; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trip
    ADD CONSTRAINT uk6kuu1nwvxvyi4kdg6f8m43t2h UNIQUE (slug);


--
-- TOC entry 5904 (class 2606 OID 37578)
-- Name: vehicles ukstdy3n8bjv14qchxmw9u8vluq; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vehicles
    ADD CONSTRAINT ukstdy3n8bjv14qchxmw9u8vluq UNIQUE (driver_id);


--
-- TOC entry 5902 (class 2606 OID 37469)
-- Name: user_discount_cars user_discount_cars_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_discount_cars
    ADD CONSTRAINT user_discount_cars_pkey PRIMARY KEY (id);


--
-- TOC entry 5896 (class 2606 OID 37309)
-- Name: user_discounts user_discounts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_discounts
    ADD CONSTRAINT user_discounts_pkey PRIMARY KEY (id);


--
-- TOC entry 5892 (class 2606 OID 37274)
-- Name: user_roles user_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_pkey PRIMARY KEY (id);


--
-- TOC entry 5924 (class 2606 OID 45679)
-- Name: user_visits user_visits_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_visits
    ADD CONSTRAINT user_visits_pkey PRIMARY KEY (id);


--
-- TOC entry 5886 (class 2606 OID 37176)
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- TOC entry 5906 (class 2606 OID 37513)
-- Name: vehicles vehicles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vehicles
    ADD CONSTRAINT vehicles_pkey PRIMARY KEY (id);


--
-- TOC entry 5954 (class 2606 OID 45515)
-- Name: bill bill_property_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bill
    ADD CONSTRAINT bill_property_id_fkey FOREIGN KEY (property_id) REFERENCES public.properties(id);


--
-- TOC entry 5957 (class 2606 OID 45550)
-- Name: booking_cars booking_cars_bill_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.booking_cars
    ADD CONSTRAINT booking_cars_bill_id_fkey FOREIGN KEY (bill_id) REFERENCES public.bill(id);


--
-- TOC entry 5958 (class 2606 OID 45555)
-- Name: booking_cars booking_cars_vehicle_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.booking_cars
    ADD CONSTRAINT booking_cars_vehicle_id_fkey FOREIGN KEY (vehicle_id) REFERENCES public.vehicles(id);


--
-- TOC entry 5955 (class 2606 OID 45535)
-- Name: booking_rooms booking_rooms_bill_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.booking_rooms
    ADD CONSTRAINT booking_rooms_bill_id_fkey FOREIGN KEY (bill_id) REFERENCES public.bill(id);


--
-- TOC entry 5956 (class 2606 OID 45530)
-- Name: booking_rooms booking_rooms_room_type_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.booking_rooms
    ADD CONSTRAINT booking_rooms_room_type_id_fkey FOREIGN KEY (room_type_id) REFERENCES public.room_type(id);


--
-- TOC entry 5953 (class 2606 OID 37525)
-- Name: drivers drivers_vehicle_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.drivers
    ADD CONSTRAINT drivers_vehicle_id_fkey FOREIGN KEY (vehicle_id) REFERENCES public.vehicles(id);


--
-- TOC entry 5965 (class 2606 OID 45897)
-- Name: roomtypes_facilities fk23mioxsuric8ecd30ml2srxlm; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roomtypes_facilities
    ADD CONSTRAINT fk23mioxsuric8ecd30ml2srxlm FOREIGN KEY (facility_id) REFERENCES public.facilities(id);


--
-- TOC entry 5961 (class 2606 OID 45608)
-- Name: review_images fk3aayo5bjciyemf3bvvt987hkr; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.review_images
    ADD CONSTRAINT fk3aayo5bjciyemf3bvvt987hkr FOREIGN KEY (review_id) REFERENCES public.reviews(id);


--
-- TOC entry 5963 (class 2606 OID 45639)
-- Name: chat_images fk5ylie1y7p0lnpgyfd0j0es3fw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat_images
    ADD CONSTRAINT fk5ylie1y7p0lnpgyfd0j0es3fw FOREIGN KEY (chat_id) REFERENCES public.chats(id);


--
-- TOC entry 5959 (class 2606 OID 45571)
-- Name: num_rooms fk7me1md53nllnsgsk870sgmoxo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.num_rooms
    ADD CONSTRAINT fk7me1md53nllnsgsk870sgmoxo FOREIGN KEY (booking_room_id) REFERENCES public.booking_rooms(id);


--
-- TOC entry 5960 (class 2606 OID 45613)
-- Name: reviews fk83j25x4ukm1vhf1336h1tt882; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reviews
    ADD CONSTRAINT fk83j25x4ukm1vhf1336h1tt882 FOREIGN KEY (property_id) REFERENCES public.properties(id);


--
-- TOC entry 5952 (class 2606 OID 37579)
-- Name: vehicles fkaashphrwfd4ts511y8vj785ia; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vehicles
    ADD CONSTRAINT fkaashphrwfd4ts511y8vj785ia FOREIGN KEY (driver_id) REFERENCES public.drivers(id);


--
-- TOC entry 5967 (class 2606 OID 54084)
-- Name: property_facilities fkey23yutk37chxd08wr4yeuovw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.property_facilities
    ADD CONSTRAINT fkey23yutk37chxd08wr4yeuovw FOREIGN KEY (property_id) REFERENCES public.properties(id);


--
-- TOC entry 5968 (class 2606 OID 54089)
-- Name: property_facilities fkoerw2j7q2ua1lsrrbfg4fd329; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.property_facilities
    ADD CONSTRAINT fkoerw2j7q2ua1lsrrbfg4fd329 FOREIGN KEY (facility_id) REFERENCES public.facilities(id);


--
-- TOC entry 5966 (class 2606 OID 45892)
-- Name: roomtypes_facilities fkqqw5vt4g5hy3jv9u7ufogvqqu; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roomtypes_facilities
    ADD CONSTRAINT fkqqw5vt4g5hy3jv9u7ufogvqqu FOREIGN KEY (room_type_id) REFERENCES public.room_type(id);


--
-- TOC entry 5962 (class 2606 OID 45644)
-- Name: chats fktc45u649g3ihssymc7rhv3ea1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chats
    ADD CONSTRAINT fktc45u649g3ihssymc7rhv3ea1 FOREIGN KEY (room_chat_id) REFERENCES public.room_chats(id);


--
-- TOC entry 5943 (class 2606 OID 16443)
-- Name: properties properties_city_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.properties
    ADD CONSTRAINT properties_city_id_fkey FOREIGN KEY (city_id) REFERENCES public.cities(id) ON DELETE CASCADE;


--
-- TOC entry 5964 (class 2606 OID 45730)
-- Name: property_images property_images_property_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.property_images
    ADD CONSTRAINT property_images_property_id_fkey FOREIGN KEY (property_id) REFERENCES public.properties(id);


--
-- TOC entry 5950 (class 2606 OID 37298)
-- Name: role_permissions role_permissions_permission_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role_permissions
    ADD CONSTRAINT role_permissions_permission_id_fkey FOREIGN KEY (permission_id) REFERENCES public.permissions(id);


--
-- TOC entry 5951 (class 2606 OID 37293)
-- Name: role_permissions role_permissions_role_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role_permissions
    ADD CONSTRAINT role_permissions_role_id_fkey FOREIGN KEY (role_id) REFERENCES public.roles(id);


--
-- TOC entry 5945 (class 2606 OID 16492)
-- Name: room_type room_type_property_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room_type
    ADD CONSTRAINT room_type_property_id_fkey FOREIGN KEY (property_id) REFERENCES public.properties(id);


--
-- TOC entry 5946 (class 2606 OID 16676)
-- Name: rooms rooms_property_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rooms
    ADD CONSTRAINT rooms_property_id_fkey FOREIGN KEY (property_id) REFERENCES public.properties(id);


--
-- TOC entry 5947 (class 2606 OID 16681)
-- Name: rooms rooms_room_type_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rooms
    ADD CONSTRAINT rooms_room_type_id_fkey FOREIGN KEY (room_type_id) REFERENCES public.room_type(id);


--
-- TOC entry 5944 (class 2606 OID 16457)
-- Name: trip trip_city_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trip
    ADD CONSTRAINT trip_city_id_fkey FOREIGN KEY (city_id) REFERENCES public.cities(id) ON DELETE CASCADE;


--
-- TOC entry 5948 (class 2606 OID 37275)
-- Name: user_roles user_roles_role_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_role_id_fkey FOREIGN KEY (role_id) REFERENCES public.roles(id);


--
-- TOC entry 5949 (class 2606 OID 37280)
-- Name: user_roles user_roles_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


-- Completed on 2025-07-21 15:29:36

--
-- PostgreSQL database dump complete
--

