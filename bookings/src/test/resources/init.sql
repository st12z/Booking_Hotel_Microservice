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