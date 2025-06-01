--
-- PostgreSQL database dump
--

-- Dumped from database version 17.2
-- Dumped by pg_dump version 17.2

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
-- Name: postgis; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS postgis WITH SCHEMA public;


--
-- Name: EXTENSION postgis; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION postgis IS 'PostGIS geometry and geography spatial types and functions';


--
-- Name: unaccent; Type: EXTENSION; Schema: -; Owner: -
--

CREATE EXTENSION IF NOT EXISTS unaccent WITH SCHEMA public;


--
-- Name: EXTENSION unaccent; Type: COMMENT; Schema: -; Owner: 
--

COMMENT ON EXTENSION unaccent IS 'text search dictionary that removes accents';


--
-- Name: car_booking_status; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public.car_booking_status AS ENUM (
    'PENDING',
    'CONFIRMED',
    'CANCELLED'
    );


--
-- Name: car_status; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public.car_status AS ENUM (
    'AVAILABLE',
    'BUSY',
    'INACTIVE'
    );


--
-- Name: car_type; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public.car_type AS ENUM (
    'BUS',
    'SEAT_4',
    'SEAT_7',
    'LIMOUSINE',
    'TAXI'
    );


--
-- Name: driver_status; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public.driver_status AS ENUM (
    'ACTIVE',
    'OFFLINE',
    'BUSY'
    );


--
-- Name: property_type_enum; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public.property_type_enum AS ENUM (
    'Hotel',
    'Villa',
    'Apartment',
    'Resort',
    'Homestay'
    );


--
-- Name: room_status; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public.room_status AS ENUM (
    'available',
    'booked'
    );


--
-- Name: transaction_type_enum; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public.transaction_type_enum AS ENUM (
    'PAYMENT',
    'REFUND'
    );


--
-- Name: trip_type_enum; Type: TYPE; Schema: public; Owner: -
--

CREATE TYPE public.trip_type_enum AS ENUM (
    'Beach',
    'Mountain',
    'Outdoors'
    );


SET default_tablespace = '';

SET default_table_access_method = heap;

--
-- Name: bill; Type: TABLE; Schema: public; Owner: -
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


--
-- Name: bill_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.bill_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: bill_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.bill_id_seq OWNED BY public.bill.id;


--
-- Name: booking_cars; Type: TABLE; Schema: public; Owner: -
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


--
-- Name: booking_cars_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.booking_cars_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: booking_cars_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.booking_cars_id_seq OWNED BY public.booking_cars.id;


--
-- Name: booking_rooms; Type: TABLE; Schema: public; Owner: -
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


--
-- Name: booking_rooms_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.booking_rooms_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: booking_rooms_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.booking_rooms_id_seq OWNED BY public.booking_rooms.id;


--
-- Name: chat_images; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.chat_images (
                                    chat_id integer NOT NULL,
                                    image_url character varying(255)
);


--
-- Name: chats; Type: TABLE; Schema: public; Owner: -
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


--
-- Name: chats_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.chats_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: chats_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.chats_id_seq OWNED BY public.chats.id;


--
-- Name: cities; Type: TABLE; Schema: public; Owner: -
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


--
-- Name: cities_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.cities_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: cities_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.cities_id_seq OWNED BY public.cities.id;


--
-- Name: discount; Type: TABLE; Schema: public; Owner: -
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


--
-- Name: discount_cars; Type: TABLE; Schema: public; Owner: -
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


--
-- Name: discount_cars_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.discount_cars_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: discount_cars_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.discount_cars_id_seq OWNED BY public.discount_cars.id;


--
-- Name: discount_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.discount_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: discount_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.discount_id_seq OWNED BY public.discount.id;


--
-- Name: drivers; Type: TABLE; Schema: public; Owner: -
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


--
-- Name: drivers_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.drivers_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: drivers_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.drivers_id_seq OWNED BY public.drivers.id;


--
-- Name: facilities; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.facilities (
                                   id integer NOT NULL,
                                   name character varying(255)
);


--
-- Name: facilities_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.facilities_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: facilities_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.facilities_id_seq OWNED BY public.facilities.id;


--
-- Name: notifications; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.notifications (
                                      id integer NOT NULL,
                                      content character varying(255),
                                      created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                                      created_by character varying(255),
                                      updated_at timestamp(6) without time zone,
                                      updated_by character varying(255)
);


--
-- Name: notifications_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.notifications_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: notifications_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.notifications_id_seq OWNED BY public.notifications.id;


--
-- Name: num_rooms; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.num_rooms (
                                  booking_room_id integer NOT NULL,
                                  room_number integer
);


--
-- Name: payment_transaction; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.payment_transaction (
                                            id integer NOT NULL,
                                            vnp_txn_ref character varying(100) NOT NULL,
                                            vnp_amount integer NOT NULL,
                                            vnp_transaction_no character varying(50) NOT NULL,
                                            vnp_transaction_date character varying(14) NOT NULL,
                                            vnp_response_code character varying(10),
                                            created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                                            created_by character varying(100),
                                            updated_at timestamp with time zone,
                                            updated_by character varying(100),
                                            transaction_type character varying(50)
);


--
-- Name: payment_transaction_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.payment_transaction_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: payment_transaction_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.payment_transaction_id_seq OWNED BY public.payment_transaction.id;


--
-- Name: permissions; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.permissions (
                                    id integer NOT NULL,
                                    name character varying(255),
                                    created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                                    updated_at timestamp without time zone,
                                    created_by character varying(255),
                                    updated_by character varying(255)
);


--
-- Name: permissions_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.permissions_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: permissions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.permissions_id_seq OWNED BY public.permissions.id;


--
-- Name: properties; Type: TABLE; Schema: public; Owner: -
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


--
-- Name: properties_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.properties_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: properties_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.properties_id_seq OWNED BY public.properties.id;


--
-- Name: property_facilities; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.property_facilities (
                                            id integer NOT NULL,
                                            property_id integer NOT NULL,
                                            facility_id integer NOT NULL
);


--
-- Name: property_facilities_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.property_facilities_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: property_facilities_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.property_facilities_id_seq OWNED BY public.property_facilities.id;


--
-- Name: property_images; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.property_images (
                                        id integer NOT NULL,
                                        property_id integer,
                                        image character varying(255)
);


--
-- Name: property_images_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.property_images_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: property_images_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.property_images_id_seq OWNED BY public.property_images.id;


--
-- Name: refund_bills; Type: TABLE; Schema: public; Owner: -
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


--
-- Name: refund_bills_id_seq; Type: SEQUENCE; Schema: public; Owner: -
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
-- Name: review_images; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.review_images (
                                      review_id integer NOT NULL,
                                      image_url character varying(255)
);


--
-- Name: reviews; Type: TABLE; Schema: public; Owner: -
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


--
-- Name: reviews_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.reviews_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: reviews_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.reviews_id_seq OWNED BY public.reviews.id;


--
-- Name: role_permissions; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.role_permissions (
                                         id integer NOT NULL,
                                         role_id integer,
                                         permission_id integer,
                                         created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                                         updated_at timestamp without time zone
);


--
-- Name: role_permissions_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.role_permissions_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: role_permissions_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.role_permissions_id_seq OWNED BY public.role_permissions.id;


--
-- Name: roles; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.roles (
                              id integer NOT NULL,
                              name character varying(255),
                              created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                              updated_at timestamp without time zone,
                              created_by character varying(255),
                              updated_by character varying(255)
);


--
-- Name: roles_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.roles_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: roles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.roles_id_seq OWNED BY public.roles.id;


--
-- Name: room_chats; Type: TABLE; Schema: public; Owner: -
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


--
-- Name: room_chats_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.room_chats_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: room_chats_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.room_chats_id_seq OWNED BY public.room_chats.id;


--
-- Name: room_type; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.room_type (
                                  id integer NOT NULL,
                                  property_id integer,
                                  name character varying(255),
                                  price integer,
                                  max_guests integer,
                                  num_beds integer,
                                  free_services jsonb,
                                  total_rooms integer,
                                  created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                                  area integer,
                                  discount integer,
                                  created_by character varying(255),
                                  updated_at timestamp(6) without time zone,
                                  updated_by character varying(255),
                                  facilities jsonb,
                                  free jsonb,
                                  status boolean DEFAULT false,
                                  remain integer
);


--
-- Name: room_type_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.room_type_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: room_type_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.room_type_id_seq OWNED BY public.room_type.id;


--
-- Name: rooms; Type: TABLE; Schema: public; Owner: -
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


--
-- Name: rooms_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.rooms_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: rooms_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.rooms_id_seq OWNED BY public.rooms.id;


--
-- Name: trip; Type: TABLE; Schema: public; Owner: -
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


--
-- Name: trip_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.trip_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: trip_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.trip_id_seq OWNED BY public.trip.id;


--
-- Name: user_discount_cars; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_discount_cars (
                                           id integer NOT NULL,
                                           discount_car_id integer,
                                           email character varying(255)
);


--
-- Name: user_discount_cars_id_seq; Type: SEQUENCE; Schema: public; Owner: -
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
-- Name: user_discounts; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_discounts (
                                       id integer NOT NULL,
                                       created_at timestamp(6) without time zone,
                                       created_by character varying(255),
                                       updated_at timestamp(6) without time zone,
                                       updated_by character varying(255),
                                       discount_id integer,
                                       email character varying(255),
                                       status boolean NOT NULL
);


--
-- Name: user_discounts_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.user_discounts_seq
    START WITH 1
    INCREMENT BY 50
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: user_roles; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_roles (
                                   id integer NOT NULL,
                                   role_id integer,
                                   user_id integer,
                                   created_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP,
                                   updated_at timestamp without time zone
);


--
-- Name: user_roles_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.user_roles_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: user_roles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.user_roles_id_seq OWNED BY public.user_roles.id;


--
-- Name: user_visits; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.user_visits (
                                    id integer NOT NULL,
                                    user_id integer,
                                    accessed_at timestamp without time zone DEFAULT CURRENT_TIMESTAMP
);


--
-- Name: user_visits_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.user_visits_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: user_visits_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.user_visits_id_seq OWNED BY public.user_visits.id;


--
-- Name: users; Type: TABLE; Schema: public; Owner: -
--

CREATE TABLE public.users (
                              id integer NOT NULL,
                              email character varying(255),
                              password character varying(255),
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


--
-- Name: users_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.users_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: users_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.users_id_seq OWNED BY public.users.id;


--
-- Name: vehicles; Type: TABLE; Schema: public; Owner: -
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
                                 star integer
);


--
-- Name: vehicles_id_seq; Type: SEQUENCE; Schema: public; Owner: -
--

CREATE SEQUENCE public.vehicles_id_seq
    AS integer
    START WITH 1
    INCREMENT BY 1
    NO MINVALUE
    NO MAXVALUE
    CACHE 1;


--
-- Name: vehicles_id_seq; Type: SEQUENCE OWNED BY; Schema: public; Owner: -
--

ALTER SEQUENCE public.vehicles_id_seq OWNED BY public.vehicles.id;


--
-- Name: bill id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.bill ALTER COLUMN id SET DEFAULT nextval('public.bill_id_seq'::regclass);


--
-- Name: booking_cars id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.booking_cars ALTER COLUMN id SET DEFAULT nextval('public.booking_cars_id_seq'::regclass);


--
-- Name: booking_rooms id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.booking_rooms ALTER COLUMN id SET DEFAULT nextval('public.booking_rooms_id_seq'::regclass);


--
-- Name: chats id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.chats ALTER COLUMN id SET DEFAULT nextval('public.chats_id_seq'::regclass);


--
-- Name: cities id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.cities ALTER COLUMN id SET DEFAULT nextval('public.cities_id_seq'::regclass);


--
-- Name: discount id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.discount ALTER COLUMN id SET DEFAULT nextval('public.discount_id_seq'::regclass);


--
-- Name: discount_cars id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.discount_cars ALTER COLUMN id SET DEFAULT nextval('public.discount_cars_id_seq'::regclass);


--
-- Name: drivers id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.drivers ALTER COLUMN id SET DEFAULT nextval('public.drivers_id_seq'::regclass);


--
-- Name: facilities id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.facilities ALTER COLUMN id SET DEFAULT nextval('public.facilities_id_seq'::regclass);


--
-- Name: notifications id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.notifications ALTER COLUMN id SET DEFAULT nextval('public.notifications_id_seq'::regclass);


--
-- Name: payment_transaction id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.payment_transaction ALTER COLUMN id SET DEFAULT nextval('public.payment_transaction_id_seq'::regclass);


--
-- Name: permissions id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.permissions ALTER COLUMN id SET DEFAULT nextval('public.permissions_id_seq'::regclass);


--
-- Name: properties id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.properties ALTER COLUMN id SET DEFAULT nextval('public.properties_id_seq'::regclass);


--
-- Name: property_facilities id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.property_facilities ALTER COLUMN id SET DEFAULT nextval('public.property_facilities_id_seq'::regclass);


--
-- Name: property_images id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.property_images ALTER COLUMN id SET DEFAULT nextval('public.property_images_id_seq'::regclass);


--
-- Name: reviews id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.reviews ALTER COLUMN id SET DEFAULT nextval('public.reviews_id_seq'::regclass);


--
-- Name: role_permissions id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.role_permissions ALTER COLUMN id SET DEFAULT nextval('public.role_permissions_id_seq'::regclass);


--
-- Name: roles id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.roles ALTER COLUMN id SET DEFAULT nextval('public.roles_id_seq'::regclass);


--
-- Name: room_chats id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.room_chats ALTER COLUMN id SET DEFAULT nextval('public.room_chats_id_seq'::regclass);


--
-- Name: room_type id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.room_type ALTER COLUMN id SET DEFAULT nextval('public.room_type_id_seq'::regclass);


--
-- Name: rooms id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.rooms ALTER COLUMN id SET DEFAULT nextval('public.rooms_id_seq'::regclass);


--
-- Name: trip id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.trip ALTER COLUMN id SET DEFAULT nextval('public.trip_id_seq'::regclass);


--
-- Name: user_roles id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_roles ALTER COLUMN id SET DEFAULT nextval('public.user_roles_id_seq'::regclass);


--
-- Name: user_visits id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.user_visits ALTER COLUMN id SET DEFAULT nextval('public.user_visits_id_seq'::regclass);


--
-- Name: users id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.users ALTER COLUMN id SET DEFAULT nextval('public.users_id_seq'::regclass);


--
-- Name: vehicles id; Type: DEFAULT; Schema: public; Owner: -
--

ALTER TABLE ONLY public.vehicles ALTER COLUMN id SET DEFAULT nextval('public.vehicles_id_seq'::regclass);


COPY public.bill (id, first_name, last_name, email, phone_number, property_id, district, city, country, address_detail, origin_total_payment, price_promotion, discount_hotel, discount_car, created_at, updated_at, special_message, user_email, booking_for_who, is_business_trip, is_shuttle_service, bill_status, bill_code, discount_car_id, discount_hotel_id, new_total_payment, created_by, updated_by) FROM stdin;
119	nguyen tien	nam	ckp2004vn@gmail.com	0969716421	20	Quốc Oai	Hà Nội	Vietnam	\N	1380000	20700	0	0	2025-05-23 15:03:15.513073	2025-05-23 15:06:20.870286		ckp2004vn@gmail.com	1	0	0	CANCEL	85724676	0	0	1338600	\N	\N
120	nguyen tien	nam	ckp2004vn@gmail.com	0969716421	5	Quốc Oai	Hà Nội	Vietnam	quoc oai	770000	30800	0	0	2025-05-23 15:03:59.602137	2025-05-23 15:07:17.286151	\N	ckp2004vn@gmail.com	1	1	0	CANCEL	22933029	0	0	739200	\N	\N
123	nguyen tien	nam	ckp2004vn@gmail.com	0969716421	20	Quốc Oai	Hà Nội	Vietnam	\N	690000	20700	0	0	2025-05-23 15:28:17.238575	2025-05-23 15:31:26.42215	\N	ckp2004vn@gmail.com	0	0	0	CANCEL	88165914	0	0	669300	\N	\N
122	nguyen tien	nam	ckp2004vn@gmail.com	0969716421	18	Quốc Oai	Hà Nội	Vietnam	\N	820000	32800	0	0	2025-05-23 15:27:00.496386	2025-05-23 15:34:05.509056	\N	ckp2004vn@gmail.com	0	0	0	CANCEL	62608948	0	0	787200	\N	\N
121	nguyen tien	nam	ckp2004vn@gmail.com	0969716421	20	Quốc Oai	Hà Nội	Vietnam	\N	690000	20700	0	0	2025-05-23 15:25:58.951033	2025-05-23 15:39:02.341203	\N	ckp2004vn@gmail.com	0	0	0	CANCEL	21201811	0	0	669300	\N	\N
124	nguyen tien	nam	ckp2004vn@gmail.com	0969716421	18	Quốc Oai	Hà Nội	Vietnam	\N	820000	32800	0	0	2025-05-23 15:40:40.754048	2025-05-23 15:40:54.703082	\N	ckp2004vn@gmail.com	1	0	0	SUCCESS	97828161	0	0	787200	\N	\N
126	nguyen tien	nam	ckp2004vn@gmail.com	0969716421	8	Quốc Oai	Hà Nội	Vietnam	hay	2500000	175000	0	0	2025-05-23 15:43:51.317913	2025-05-23 15:56:52.172342	\N	ckp2004vn@gmail.com	1	0	0	CANCEL	58744898	0	0	2325000	\N	\N
125	nguyen tien	nam	ckp2004vn@gmail.com	0969716421	5	Quốc Oai	Hà Nội	Vietnam	\N	770000	30800	0	0	2025-05-23 15:41:28.623799	2025-05-23 15:57:42.998394	\N	ckp2004vn@gmail.com	1	0	0	CANCEL	77424057	0	0	739200	\N	\N
\.


--
-- Data for Name: booking_cars; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.booking_cars (id, bill_id, vehicle_id, pickup_location, dropoff_location, pickup_time, created_at, updated_at, price_booking, created_by, updated_by) FROM stdin;
\.


--
-- Data for Name: booking_rooms; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.booking_rooms (id, room_type_id, quantity_rooms, bill_id, check_in, check_out, day_stays, origin_payment, promotion, new_payment, created_at, updated_at, num_rooms, property_id, created_by, updated_by) FROM stdin;
136	40	1	119	2025-05-22 07:00:00	2025-05-24 07:00:00	2	1380000	20700	1338600	2025-05-23 15:03:15.561584	\N	\N	20	\N	\N
137	10	1	120	2025-05-16 07:00:00	2025-05-17 07:00:00	1	770000	30800	739200	2025-05-23 15:03:59.63539	\N	\N	5	\N	\N
138	40	1	121	2025-05-23 07:00:00	2025-05-24 07:00:00	1	690000	20700	669300	2025-05-23 15:25:59.06176	\N	\N	20	\N	\N
139	36	1	122	2025-05-24 07:00:00	2025-05-25 07:00:00	1	820000	32800	787200	2025-05-23 15:27:00.526898	\N	\N	18	\N	\N
140	40	1	123	2025-05-24 07:00:00	2025-05-25 07:00:00	1	690000	20700	669300	2025-05-23 15:28:17.271822	\N	\N	20	\N	\N
141	36	1	124	2025-05-29 07:00:00	2025-05-30 07:00:00	1	820000	32800	787200	2025-05-23 15:40:40.830573	\N	\N	18	\N	\N
142	10	1	125	2025-05-29 07:00:00	2025-05-30 07:00:00	1	770000	30800	739200	2025-05-23 15:41:28.645213	\N	\N	5	\N	\N
143	17	1	126	2025-05-29 07:00:00	2025-05-30 07:00:00	1	2500000	175000	2325000	2025-05-23 15:43:51.351386	\N	\N	8	\N	\N
\.


--
-- Data for Name: chat_images; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.chat_images (chat_id, image_url) FROM stdin;
\.


--
-- Data for Name: chats; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.chats (id, content, images, room_chat_id, user_send, created_at, created_by, updated_at, updated_by) FROM stdin;
52	hello	\N	4	68	2025-05-15 21:57:47.511227	\N	\N	\N
53	hi	\N	5	69	2025-05-15 21:58:11.56255	\N	\N	\N
54	tôi đang có thắc mắc	\N	5	69	2025-05-15 21:58:46.065796	\N	\N	\N
55	bảo gì	\N	4	70	2025-05-15 22:34:36.099781	\N	\N	\N
56	tại sao khách sạn không có vùi sịt đít	\N	4	68	2025-05-15 22:34:51.651382	\N	\N	\N
57	tao không biết=)))	\N	4	70	2025-05-15 22:35:01.674999	\N	\N	\N
58	thắc mắc nói mọe đi	\N	5	70	2025-05-15 22:35:19.188244	\N	\N	\N
59	gì mà căng thắng vậy man	\N	5	69	2025-05-15 22:35:53.370507	\N	\N	\N
60	🥲	\N	4	70	2025-05-17 10:14:37.70687	\N	\N	\N
61	????	\N	4	68	2025-05-17 10:14:50.328651	\N	\N	\N
62	hii	\N	6	74	2025-05-17 10:39:24.115352	\N	\N	\N
63	haha	\N	6	74	2025-05-17 10:39:57.508125	\N	\N	\N
64	hello	\N	4	68	2025-05-20 10:45:02.351424	\N	\N	\N
65	thối ăn cơm chưa	\N	11	82	2025-05-20 21:33:47.192344	\N	\N	\N
66	tớ chưa	\N	11	70	2025-05-20 21:34:54.699371	\N	\N	\N
67	thối đang học hả	\N	11	82	2025-05-20 21:35:11.07582	\N	\N	\N
68	tớ vẽ tiếp đây nhó 😉	\N	11	82	2025-05-20 21:53:29.654716	\N	\N	\N
69	tớ nhớ cậu nhiều lắm	\N	11	70	2025-05-20 21:54:15.344558	\N	\N	\N
70	hi	\N	4	68	2025-05-20 22:10:15.842015	\N	\N	\N
71	hehge	\N	4	68	2025-05-20 22:13:15.507033	\N	\N	\N
72	ừ	\N	11	82	2025-05-20 22:13:41.04329	\N	\N	\N
73	tớ ghét cậu	\N	11	82	2025-05-20 22:16:59.883218	\N	\N	\N
74	hi	\N	4	68	2025-05-20 22:18:03.074888	\N	\N	\N
75	aaaaaa	\N	4	68	2025-05-20 22:19:53.938247	\N	\N	\N
76	123	\N	11	82	2025-05-20 22:20:10.716385	\N	\N	\N
77	afffff	\N	4	68	2025-05-20 22:27:40.968823	\N	\N	\N
78	123	\N	11	82	2025-05-20 22:39:09.968501	\N	\N	\N
79	tao	\N	4	68	2025-05-20 22:40:23.952468	\N	\N	\N
80	hehe	\N	11	82	2025-05-20 22:46:11.397392	\N	\N	\N
81	hâ	\N	4	68	2025-05-20 22:49:45.188269	\N	\N	\N
\.


--
-- Data for Name: cities; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.cities (id, name, image, created_at, created_by, updated_at, updated_by, slug, latitude_center, longitude_center, geog) FROM stdin;
1	Hà Nội	https://cf.bstatic.com/xdata/images/city/600x600/981517.jpg?k=2268f51ad34ab94115ea9e42155bc593aa8d48ffaa6fc58432a8760467dc4ea6&o=	\N	\N	\N	\N	ha-noi	21.0330731767	105.8373925968	0101000020E6100000C94A1ED797755A40244DD47B77083540
2	Hồ Chí Minh	https://cf.bstatic.com/xdata/images/city/600x600/688893.jpg?k=d32ef7ff94e5d02b90908214fb2476185b62339549a1bd7544612bdac51fda31&o=	\N	\N	\N	\N	ho-chi-minh	16.0422635259	108.1994611431	0101000020E6100000FC9BABF8C30C5B40DD8D4DC8D10A3040
3	Đà Nẵng	https://vcdn1-dulich.vnecdn.net/2022/06/03/cau-vang-jpeg-mobile-4171-1654247848.jpg?w=0&h=0&q=100&dpr=1&fit=crop&s=xrjEn1shZLiHomFix1sHNQ	\N	\N	\N	\N	da-nang	12.2514058846	109.1891627966	0101000020E61000001541463E1B4C5B4006C2A845B8802840
4	Đà Lạt	https://namthientravel.com.vn/wp-content/uploads/2024/09/da-lat.jpg	\N	\N	\N	\N	da-lat	11.9408497291	108.4565548745	0101000020E6100000A4B3EF31381D5B40B3DE410EB7E12740
5	Nha Trang	https://images2.thanhnien.vn/zoom/1200_630/528068263637045248/2025/3/24/nha-trang-17428130131821343548929-60-0-1310-2000-crop-1742813226474598199942.jpg	\N	\N	\N	\N	nha-trang	10.8135268620	106.6041129155	0101000020E61000007ACA37C9A9A65A403E6AC59786A02540
\.


--
-- Data for Name: discount; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.discount (id, code, discount_type, discount_value, min_booking_amount, start_date, end_date, is_active, quantity, image, created_at, created_by, updated_at, updated_by) FROM stdin;
7	SUMMER20	PERCENT	20	75000	2025-05-01 00:00:00	2025-05-31 23:59:59	t	4	https://img.freepik.com/free-psd/hotel-template-design_23-2151647862.jpg	\N	\N	\N	\N
9	VIP100	FIXED	100000	120000	2025-04-01 00:00:00	2025-05-15 23:59:59	t	2	https://cdn.grabon.in/gograbon/images/web-images/uploads/1617092437646/hotel-offers.jpg	\N	\N	\N	\N
5	HOLIDAY30	PERCENT	30	100000	2025-04-05 00:00:00	2025-04-25 23:59:59	t	2	https://d1csarkz8obe9u.cloudfront.net/posterpreviews/hotel-airbnb-instagram-social-media-design-template-97a6e7529cee6913fa1dd744d39d6e95.jpg?ts=1698303516	\N	\N	\N	\N
10	FLASHSALE	PERCENT	50	150000	2025-04-15 00:00:00	2025-04-15 23:59:59	t	1	https://cdn.grabon.in/gograbon/images/web-images/uploads/1617092437646/hotel-offers.jpg	\N	\N	\N	\N
6	SPRINGSALE	PERCENT	15	6000	2025-04-10 00:00:00	2025-04-28 23:59:59	t	7	https://img.freepik.com/free-psd/hotel-template-design_23-2151647862.jpg	\N	\N	\N	\N
8	NEWUSER50	FIXED	50000	0	2025-04-01 00:00:00	2025-06-30 23:59:59	t	3	https://cdn.grabon.in/gograbon/images/web-images/uploads/1617092437646/hotel-offers.jpg	\N	\N	\N	\N
1	DISCOUNT10	PERCENT	10	50000	2025-04-01 00:00:00	2025-04-30 23:59:59	t	0	https://marketplace.canva.com/EAGGejRWDp8/1/0/1131w/canva-red-and-cream-geometric-hotel-promotion-with-facilities-flyer-UGMYMCwL1ic.jpg	\N	\N	\N	\N
2	DISCOUNT20	PERCENT	20	70000	2025-04-01 00:00:00	2025-04-30 23:59:59	t	0	https://i.pinimg.com/736x/44/7f/d4/447fd44e5a24ca4776de5f3782dc2e6e.jpg	\N	\N	\N	\N
3	FIXED50K	FIXED	50000	40000	2025-04-01 00:00:00	2025-04-15 23:59:59	t	6	https://d1csarkz8obe9u.cloudfront.net/posterpreviews/hotel-airbnb-instagram-social-media-design-template-97a6e7529cee6913fa1dd744d39d6e95.jpg?ts=1698303516	\N	\N	\N	\N
4	FIXED100K	FIXED	100000	80000	2025-04-01 00:00:00	2025-04-20 23:59:59	t	6	https://d1csarkz8obe9u.cloudfront.net/posterpreviews/hotel-airbnb-instagram-social-media-design-template-97a6e7529cee6913fa1dd744d39d6e95.jpg?ts=1698303516	\N	\N	\N	\N
\.


--
-- Data for Name: discount_cars; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.discount_cars (id, code, description, discount_value, start_date, end_date, images, is_active, created_at, updated_at, updated_by, created_by, quantity) FROM stdin;
1	DISCOUNT10	10% off for all vehicles	10	2025-01-01 00:00:00	2025-12-31 23:59:59	https://tse4.mm.bing.net/th/id/OIP.ZA9Bft_irvFr5QrAe7MEMwHaHa?rs=1&pid=ImgDetMain	t	2025-04-22 23:05:48.343219	2025-04-22 23:05:48.343219	\N	\N	4
2	DISCOUNT15	15% off for 4-seat cars	15	2025-02-01 00:00:00	2025-08-31 23:59:59	https://media.karousell.com/media/photos/products/2022/1/31/50_grabcar_and_grabtaxi_vouche_1643587281_56e981d0_progressive.jpg	t	2025-04-22 23:05:48.343219	2025-04-22 23:05:48.343219	\N	\N	5
3	DISCOUNT5	5% off for Limousine	5	2025-03-01 00:00:00	2025-06-30 23:59:59	https://media.karousell.com/media/photos/products/2023/2/12/grab_car_voucher_1676208781_d7f44356.jpg	t	2025-04-22 23:05:48.343219	2025-04-22 23:05:48.343219	\N	\N	2
4	SUMMER20	20% off for all cars in summer	20	2025-06-01 00:00:00	2025-08-31 23:59:59	https://media.karousell.com/media/photos/products/2021/1/18/grab_food_voucher_20_off_1610939407_da687a4a.jpg	t	2025-04-22 23:05:48.343219	2025-04-22 23:05:48.343219	\N	\N	3
5	NEWYEAR25	25% off for all bookings in New Year	25	2025-12-01 00:00:00	2025-12-31 23:59:59	https://tse1.mm.bing.net/th/id/OIP.qUlpceiXtdnlAJdCd7HjigHaHa?rs=1&pid=ImgDetMain	t	2025-04-22 23:05:48.343219	2025-04-22 23:05:48.343219	\N	\N	6
6	BIRTHDAY25	25% off for all bookings in BIRTHDAY	25	2025-12-01 00:00:00	2025-12-31 23:59:59	https://tse1.mm.bing.net/th/id/OIP.qUlpceiXtdnlAJdCd7HjigHaHa?rs=1&pid=ImgDetMain	t	2025-04-23 08:37:47.092175	2025-04-23 08:37:47.092175	\N	\N	3
7	SUMMER15	15% off for all bookings in SUMMER	15	2025-06-01 00:00:00	2025-08-31 23:59:59	https://tse1.mm.bing.net/th/id/OIP.qUlpceiXtdnlAJdCd7HjigHaHa?rs=1&pid=ImgDetMain	t	2025-04-23 08:37:47.092175	2025-04-23 08:37:47.092175	\N	\N	2
8	SUMMER10	10% off for all bookings in SUMMER	10	2025-06-01 00:00:00	2025-08-31 23:59:59	https://tse1.mm.bing.net/th/id/OIP.qUlpceiXtdnlAJdCd7HjigHaHa?rs=1&pid=ImgDetMain	t	2025-04-23 08:37:47.092175	2025-04-23 08:37:47.092175	\N	\N	6
9	SPRING15	15% off for all bookings in Spring	15	2025-03-01 00:00:00	2025-05-31 23:59:59	https://tse1.mm.bing.net/th/id/OIP.qUlpceiXtdnlAJdCd7HjigHaHa?rs=1&pid=ImgDetMain	t	2025-04-23 08:37:47.092175	2025-04-23 08:37:47.092175	\N	\N	5
10	AUTUMN15	15% off for all bookings in Autumn	15	2025-09-01 00:00:00	2025-11-30 23:59:59	https://tse1.mm.bing.net/th/id/OIP.qUlpceiXtdnlAJdCd7HjigHaHa?rs=1&pid=ImgDetMain	t	2025-04-23 08:37:47.092175	2025-04-23 08:37:47.092175	\N	\N	4
\.


--
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
-- Data for Name: facilities; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.facilities (id, name) FROM stdin;
1	Wi-Fi miễn phí
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
\.


--
-- Data for Name: notifications; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.notifications (id, content, created_at, created_by, updated_at, updated_by) FROM stdin;
114	ckp2004vn@gmail.com đã hoàn thành hóa đơn 87621104!	2025-05-23 07:43:31.948264	\N	\N	\N
115	ckp2004vn@gmail.com đã hoàn thành hóa đơn 53630142!	2025-05-23 14:43:02.969216	\N	\N	\N
116	ckp2004vn@gmail.com đã hoàn thành hóa đơn 95479541!	2025-05-23 14:49:25.838592	\N	\N	\N
117	ckp2004vn@gmail.com đã hoàn thành hóa đơn 16550336!	2025-05-23 14:50:06.33076	\N	\N	\N
118	ckp2004vn@gmail.com đã hoàn thành hóa đơn 59293327!	2025-05-23 14:50:49.440706	\N	\N	\N
119	ckp2004vn@gmail.com đã hoàn thành hóa đơn 85724676!	2025-05-23 15:03:25.785031	\N	\N	\N
120	ckp2004vn@gmail.com đã hoàn thành hóa đơn 22933029!	2025-05-23 15:04:10.602073	\N	\N	\N
121	ckp2004vn@gmail.com đã hủy hóa đơn 85724676!	2025-05-23 15:06:20.918478	\N	\N	\N
122	ckp2004vn@gmail.com đã hủy hóa đơn 22933029!	2025-05-23 15:07:17.34736	\N	\N	\N
123	ckp2004vn@gmail.com đã hoàn thành hóa đơn 21201811!	2025-05-23 15:26:15.832804	\N	\N	\N
124	ckp2004vn@gmail.com đã hoàn thành hóa đơn 62608948!	2025-05-23 15:27:17.604771	\N	\N	\N
125	ckp2004vn@gmail.com đã hoàn thành hóa đơn 88165914!	2025-05-23 15:28:26.476499	\N	\N	\N
126	ckp2004vn@gmail.com đã hủy hóa đơn 88165914!	2025-05-23 15:31:26.470999	\N	\N	\N
127	ckp2004vn@gmail.com đã hủy hóa đơn 62608948!	2025-05-23 15:34:05.555904	\N	\N	\N
128	ckp2004vn@gmail.com đã hủy hóa đơn 21201811!	2025-05-23 15:39:02.423476	\N	\N	\N
129	ckp2004vn@gmail.com đã hoàn thành hóa đơn 97828161!	2025-05-23 15:40:56.150398	\N	\N	\N
130	ckp2004vn@gmail.com đã hoàn thành hóa đơn 77424057!	2025-05-23 15:41:48.855724	\N	\N	\N
131	ckp2004vn@gmail.com đã hoàn thành hóa đơn 58744898!	2025-05-23 15:44:00.801879	\N	\N	\N
132	ckp2004vn@gmail.com đã hủy hóa đơn 58744898!	2025-05-23 15:56:52.288345	\N	\N	\N
133	ckp2004vn@gmail.com đã hủy hóa đơn 77424057!	2025-05-23 15:57:43.124501	\N	\N	\N
\.


--
-- Data for Name: num_rooms; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.num_rooms (booking_room_id, room_number) FROM stdin;
136	102
137	102
138	103
139	102
140	101
141	101
142	101
143	201
\.


--
-- Data for Name: payment_transaction; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.payment_transaction (id, vnp_txn_ref, vnp_amount, vnp_transaction_no, vnp_transaction_date, vnp_response_code, created_at, created_by, updated_at, updated_by, transaction_type) FROM stdin;
42	85724676	1338600	14975190	20250523150356	00	2025-05-23 15:03:24.632016	\N	\N	\N	PAYMENT
43	22933029	739200	14975191	20250523150441	00	2025-05-23 15:04:09.797962	\N	\N	\N	PAYMENT
44	85724676	1338600	14975197	20250523150624	00	2025-05-23 15:06:20.849804	\N	\N	\N	REFUND
45	22933029	739200	14975203	20250523150720	00	2025-05-23 15:07:17.260084	\N	\N	\N	REFUND
48	88165914	669300	14975266	20250523152857	00	2025-05-20 15:28:25.728094	\N	\N	\N	PAYMENT
49	88165914	334650	14975274	20250523153129	00	2025-05-23 15:31:26.325185	\N	\N	\N	REFUND
47	62608948	787200	14975263	20250523152747	00	2025-05-20 15:27:16.877643	\N	\N	\N	PAYMENT
50	62608948	393600	14975283	20250523153408	00	2025-05-23 15:34:05.329535	\N	\N	\N	REFUND
46	21201811	669300	14975260	20250523152641	00	2025-05-20 15:26:14.428549	\N	\N	\N	PAYMENT
51	21201811	334650	14975292	20250523153905	00	2025-05-23 15:39:02.282778	\N	\N	\N	REFUND
52	97828161	787200	14975298	20250523154126	00	2025-05-23 15:40:54.603473	\N	\N	\N	PAYMENT
53	77424057	739200	14975302	20250523154215	00	2025-05-23 15:41:47.71977	\N	\N	\N	PAYMENT
54	58744898	2325000	14975306	20250523154431	00	2025-05-23 15:44:00.096107	\N	\N	\N	PAYMENT
55	58744898	2325000	14975335	20250523155655	00	2025-05-23 15:56:52.080476	\N	\N	\N	REFUND
56	77424057	739200	14975338	20250523155746	00	2025-05-23 15:57:42.965136	\N	\N	\N	REFUND
\.


--
-- Data for Name: permissions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.permissions (id, name, created_at, updated_at, created_by, updated_by) FROM stdin;
1	READ	2025-04-07 20:52:01.473122	\N	\N	\N
2	CREATE	2025-04-07 20:52:01.473122	\N	\N	\N
3	UPDATE	2025-04-07 20:52:37.176618	\N	\N	\N
4	DELETE	2025-04-07 20:52:37.176618	\N	\N	\N
\.


--
-- Data for Name: properties; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.properties (id, name, city_id, property_type, rating_star, address, latitude, longitude, overview, avg_review_score, created_at, updated_at, created_by, updated_by, slug, geog, distance_from_center, distance_from_trip) FROM stdin;
2	Hotel Indigo Saigon the City, an IHG Hotel	2	Hotel	0	9-11 Ly Tu Trong Street, District 1, 700000 Ho Chi Minh City, Vietnam	10.7826080900	106.7052030822	Tọa lạc tại Thành phố Hồ Chí Minh, Hotel Indigo Saigon the City, an IHG Hotel cách Nhà hát lớn Sài Gòn chưa đầy 1 km và cung cấp nhiều tiện nghi khác nhau, chẳng hạn như trung tâm thể dục, sân hiên và nhà hàng. Với hồ bơi ngoài trời, khách sạn 5 sao này cung cấp các phòng nghỉ máy lạnh với WiFi miễn phí, mỗi phòng đều có phòng tắm riêng. Chỗ nghỉ cung cấp dịch vụ phòng và lễ tân 24 giờ cho khách. Tất cả các phòng nghỉ tại khách sạn đều được trang bị ấm đun nước. Mỗi phòng đều có TV và một số phòng tại Hotel Indigo Saigon the City, an IHG Hotel có tầm nhìn ra quang cảnh thành phố. Tất cả các phòng đều được trang bị tủ lạnh. Khách nghỉ tại chỗ nghỉ có thể thưởng thức bữa sáng tự chọn. Hotel Indigo Saigon the City, an IHG Hotel có trung tâm dịch vụ doanh nhân phục vụ khách. Các điểm tham quan nổi tiếng gần khách sạn bao gồm Trụ sở Ủy ban Nhân dân Thành phố Hồ Chí Minh, Trung tâm Thương mại Union Square Saigon và Bưu điện Trung tâm Sài Gòn. Sân bay Quốc tế Tân Sơn Nhất cách đó 7 km và chỗ nghỉ cung cấp dịch vụ đưa đón sân bay có tính phí.	0	2025-03-25 16:40:24.676405	\N	\N	\N	hotel-indigo-saigon-the-city-an-ihg-hotel	0101000020E61000003D0AD7A370AD5A408FC2F5285C8F2540	604.10667559849	\N
12	Khách sạn & Spa Paradise Center Hà Nội	1	Hotel	3	22/5 Hang Voi Street, Ly Thai To Ward, Hoan Kiem District, Vietnam, Hoan Kiem, Hanoi, Vietnam	21.0308041931	105.8560292112	Tọa lạc tại thành phố Hà Nội, cách Nhà hát múa rối nước Thăng Long 400 m, Hanoi Paradise Center Hotel & Spa cung cấp chỗ nghỉ với sảnh khách chung, chỗ đỗ xe riêng, sân hiên và quầy bar. Với dịch vụ mát-xa, khách sạn này nằm gần một số điểm tham quan nổi tiếng, cách Hồ Hoàn Kiếm chưa đầy 1 km, cách Ô Quan Chưởng 12 phút đi bộ và cách trung tâm thương mại Tràng Tiền Plaza chưa đầy 1 km. Chỗ nghỉ có lễ tân 24 giờ, dịch vụ đưa đón sân bay, dịch vụ phòng và WiFi miễn phí. Khách sạn cung cấp phòng nghỉ lắp máy điều hòa với bàn làm việc, ấm đun nước, tủ lạnh, minibar, két an toàn, TV màn hình phẳng và phòng tắm riêng đi kèm chậu rửa vệ sinh (bidet). Các phòng nghỉ tại Hanoi Paradise Center Hotel & Spa được trang bị đồ vệ sinh cá nhân miễn phí và ổ cắm cho iPod. Chỗ nghỉ phục vụ bữa sáng tự chọn, gọi món hoặc bữa sáng đầy đủ kiểu Anh/Ailen. Tại chỗ nghỉ, du khách sẽ tìm thấy nhà hàng phục vụ ẩm thực châu Phi, Mỹ và Argentina. Du khách cũng có thể yêu cầu các lựa chọn không có sữa, halal và kosher. Khu vực này nổi tiếng với hoạt động đạp xe và dịch vụ cho thuê xe đạp có tại khách sạn 4 sao này. Nhà hát lớn Hà Nội cách Hanoi Paradise Center Hotel & Spa 1,1 km, Nhà thờ lớn Hà Nội cách đó 1,2 km. Sân bay quốc tế Nội Bài cách đó 24 km.	7.714285714285714	2025-03-25 16:40:24.676405	2025-05-22 15:50:41.380553	\N	\N	khach-san-spa-paradise-center-ha-noi	0101000020E6100000D7A3703D0A775A4048E17A14AE073540	2.3745114092899997	\N
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
19	Dalat Wind Hotel	4	Hotel	0	Lot R2 03-04. Golf Valley, Ward 2, Da Lat, Vietnam	11.9478586309	108.4413696687	Bạn có thể đủ điều kiện để được giảm giá Genius tại Dalat Wind Hotel. Để kiểm tra xem có giảm giá Genius cho những ngày bạn đã chọn hay không, hãy đăng nhập .\n\nGiảm giá Genius tại cơ sở lưu trú này tùy thuộc vào ngày đặt phòng, ngày lưu trú và các ưu đãi khác có sẵn.\n\nDalat Wind Hotel tọa lạc tại thành phố Đà Lạt, cách Hồ Xuân Hương 500 m. Khách sạn 2 sao này có lễ tân 24 giờ và sảnh khách chung. Chỗ nghỉ này cung cấp cả WiFi miễn phí và chỗ đỗ xe riêng. Tất cả các phòng nghỉ tại khách sạn đều có bàn làm việc và TV màn hình phẳng. Dalat Wind Hotel có một số phòng có ban công và các phòng được trang bị ấm đun nước. Tất cả các phòng đều có phòng tắm riêng với chậu rửa vệ sinh (bidet). Bữa sáng gọi món được phục vụ hàng ngày tại chỗ nghỉ. Du khách có thể dùng bữa tại nhà hàng trong khuôn viên, chuyên về ẩm thực nướng/bbq. Chỗ nghỉ có sân hiên tắm nắng. Các điểm tham quan nổi tiếng gần Dalat Wind Hotel bao gồm Vườn hoa Đà Lạt, Quảng trường Lâm Viên và Công viên Yersin Đà Lạt. Sân bay gần nhất là Sân bay Liên Khương, cách chỗ nghỉ 23 km.	0	2025-03-25 21:03:47.492585	\N	\N	\N	dalat-wind-hotel	0101000020E61000005C8FC2F5281C5B406666666666E62740	2.0679055711300003	\N
9	Amanda Boutique Hotel & Travel	1	Hotel	5	62E Phố Cầu Gỗ, Hoan Kiem, Hanoi, Vietnam	21.0325057039	105.8525204517	Tọa lạc tại vị trí hấp dẫn ở trung tâm thành phố Hà Nội, Amanda Boutique Hotel & Travel nằm trong bán kính 100 m từ Nhà hát múa rối nước Thăng Long và 600 m từ Hồ Hoàn Kiếm. Khách sạn 3 sao này có nhà hàng và các phòng nghỉ lắp máy điều hòa với WiFi miễn phí, mỗi phòng đều đi kèm phòng tắm riêng. Chỗ nghỉ cung cấp dịch vụ phòng, lễ tân 24 giờ và dịch vụ thu đổi ngoại tệ cho khách. Tất cả các phòng đều được trang bị tủ lạnh, minibar, ấm đun nước, chậu rửa vệ sinh (bidet), đồ vệ sinh cá nhân miễn phí và tủ quần áo. Các phòng được trang bị TV màn hình phẳng và một số phòng tại khách sạn có tầm nhìn ra quang cảnh thành phố. Khách sạn phục vụ bữa sáng tự chọn, kiểu lục địa hoặc kiểu Á. Các điểm tham quan nổi tiếng gần Amanda Boutique Hotel & Travel bao gồm Ô Quan Chưởng, trung tâm thương mại Tràng Tiền Plaza và Nhà hát Lớn Hà Nội. Sân bay quốc tế Nội Bài cách chỗ nghỉ 24 km và chỗ nghỉ cung cấp dịch vụ đưa đón sân bay có tính phí.	5.714285714285714	2025-03-25 16:40:24.676405	2025-05-20 18:10:47.978724	\N	\N	amanda-boutique-hotel-travel	0101000020E61000006666666666765A4048E17A14AE073540	1.3539715149	\N
16	Hadana Boutique Hotel Danang	3	Hotel	0	H1-04,05,06 đường Phạm Văn Đồng, phường An Hải Bắc, quận Sơn Trà, Thành Phố Đà Nẵng, Việt Nam, Da Nang, Vietnam	16.0717058130	108.2354813958	Hadana Boutique Hotel Danang có hồ bơi ngoài trời, khu vườn, sảnh khách chung và nhà hàng tại thành phố Đà Nẵng. Mỗi phòng nghỉ tại khách sạn 4 sao này đều có tầm nhìn ra quang cảnh thành phố và du khách có thể sử dụng phòng xông hơi khô. Chỗ nghỉ có lễ tân 24 giờ, dịch vụ đưa đón sân bay, dịch vụ phòng và WiFi miễn phí trong toàn bộ khuôn viên. Khách sạn cung cấp phòng nghỉ lắp máy điều hòa với bàn làm việc, ấm đun nước, két an toàn, TV màn hình phẳng và phòng tắm riêng đi kèm chậu rửa vệ sinh (bidet). Tất cả các phòng đều có tủ quần áo. Du khách tại Hadana Boutique Hotel Danang có thể thưởng thức bữa sáng tự chọn hoặc bữa sáng kiểu Á. Các điểm tham quan nổi tiếng gần chỗ nghỉ bao gồm Bãi biển Mỹ Khê, Cầu Sông Hàn và Trung tâm thương mại Indochina Riverside. Sân bay quốc tế Đà Nẵng cách chỗ nghỉ 6 km.	0	2025-03-25 21:03:47.492585	\N	\N	\N	hadana-boutique-hotel-danang	0101000020E61000008FC2F5285C0F5B4052B81E85EB113040	434.7370658253	1069.97041126
18	TTR Moonstone Apart Hotel	4	Apartment	0	86 Đường Lý Tự Trọng, Da Lat, Vietnam	11.9461912214	108.4386659957	You might be eligible for a Genius discount at TTR Moonstone Apart Hotel. To check if a Genius discount is available for your selected dates sign in.\n\nGenius discounts at this property are subject to book dates, stay dates and other available deals.\n\nSituated in Da Lat, 1.9 km from Dalat Palace Golf Club, TTR Moonstone Apart Hotel features accommodation with free bikes, free private parking, a garden and a terrace. This 2-star hotel offers a restaurant. The accommodation offers a 24-hour front desk, airport transfers, room service and free WiFi.\n\nAll rooms are fitted with a flat-screen TV with cable channels, fridge, a kettle, a bidet, free toiletries and a desk. The rooms come with a private bathroom fitted with a shower and a hairdryer, while selected rooms also feature a kitchen equipped with a microwave. The units have a safety deposit box.\n\nThe area is popular for cycling, and car hire is available at the hotel.\n\nLam Vien Square is 1.9 km from TTR Moonstone Apart Hotel, while Xuan Huong Lake is 2.2 km from the property. Lien Khuong Airport is 28 km away.	0	2025-03-25 21:03:47.492585	\N	\N	\N	ttr-moonstone-apart-hotel	0101000020E61000005C8FC2F5281C5B406666666666E62740	2.0679055711300003	\N
10	Peaceful Corner in Old Quarter	1	Villa	1	3 Phố Phan Huy Ích 6, Ba Dinh, 100000 Hanoi, Vietnam	21.0325790608	105.8500387310	Peaceful Corner in Old Quarter là một căn hộ nằm tại quận Ba Đình của thành phố Hà Nội. Du khách có thể vào căn hộ qua lối vào riêng. Tất cả các phòng trong khu phức hợp căn hộ đều được trang bị ấm đun nước. Với phòng tắm riêng đi kèm góc tắm vòi sen và dép, các phòng tại khu phức hợp căn hộ cũng có WiFi miễn phí. Các phòng được trang bị tiện nghi sưởi ấm. Sân bay quốc tế Nội Bài cách chỗ nghỉ 22 km.	6.142857142857143	2025-03-25 16:40:24.676405	2025-05-20 18:11:13.498645	\N	\N	peaceful-corner-in-old-quarter	0101000020E61000006666666666765A4048E17A14AE073540	1.3539715149	\N
1	Grand Lee Hotel	2	Hotel	0	137/8 Đường Bình Quới , Binh Thanh, Ho Chi Minh City, Vietnam	10.8194184136	106.7235456803	Nằm trong bán kính 5,2 km từ Bảo tàng Lịch sử Việt Nam và 6,3 km từ trung tâm thương mại Diamond Plaza, Grand Lee Hotel cung cấp các phòng nghỉ với máy lạnh và phòng tắm riêng tại Thành phố Hồ Chí Minh. Với WiFi miễn phí, khách sạn 3 sao này cung cấp dịch vụ phòng và lễ tân 24 giờ. Chỗ nghỉ cung cấp dịch vụ trợ giúp đặc biệt, bàn đặt tour và dịch vụ giữ hành lý cho khách. Tại khách sạn, các phòng nghỉ đều có bàn làm việc. Với phòng tắm riêng được trang bị chậu rửa vệ sinh (bidet) và đồ vệ sinh cá nhân miễn phí, một số phòng tại Grand Lee Hotel cũng có tầm nhìn ra quang cảnh thành phố. Các phòng nghỉ tại đây được trang bị TV màn hình phẳng và máy sấy tóc. Bưu điện Trung tâm Sài Gòn cách Grand Lee Hotel 6,4 km, trong khi Chợ Tân Định cách đó 6,5 km. Sân bay Quốc tế Tân Sơn Nhất cách đó 8 km.\n\nCác cặp đôi đặc biệt thích vị trí rất tốt này — họ đánh giá 8,3 điểm cho chuyến đi dành cho hai người.	0	2025-03-25 15:44:50.210341	\N	\N	\N	grand-lee-hotel	0101000020E6100000AE47E17A14AE5A40A4703D0AD7A32540	599.55044095197	\N
21	Maris Hotel Nha Trang	5	Hotel	0	27 Tran Quang Khai Street, Loc Tho Ward, Nha Trang, Vietnam\nExcellent location — rated 9.5/10!(score from 456 reviews)\nReal guests • Real stays • Real opinions\n	12.2335797035	109.1963702957	Maris Hotel Nha Trang có hồ bơi ngoài trời, trung tâm thể dục, nhà hàng và quầy bar tại Nha Trang. Khách sạn 4 sao này cung cấp câu lạc bộ trẻ em, dịch vụ phòng và WiFi miễn phí. Khách sạn có bồn tắm nước nóng và lễ tân 24 giờ. Khách sạn sẽ cung cấp cho khách các phòng nghỉ máy lạnh với bàn làm việc, ấm đun nước, minibar, két an toàn, TV màn hình phẳng và phòng tắm riêng với vòi sen. Tất cả các phòng đều có tủ quần áo. Các lựa chọn bữa sáng tự chọn và kiểu lục địa có tại Maris Hotel Nha Trang. Các điểm tham quan nổi tiếng gần chỗ nghỉ bao gồm Bãi biển Nha Trang, Tháp Trầm Hương và Trung tâm mua sắm Nha Trang Centre. Sân bay quốc tế Cam Ranh cách đó 34 km.	0	2025-03-25 21:03:47.492585	\N	\N	\N	maris-hotel-nha-trang	0101000020E6100000CDCCCCCCCC4C5B40F6285C8FC2752840	323.63759376533	1106.24160866
8	Granda Central Apartment	1	Apartment	5	Lot A14/D21, Lane 100, Dich Vong Hau Street, Cau Giay District, Hanoi, Cau Giay, Hanoi, Vietnam	21.0291945805	105.7843757959	Nằm trong bán kính 2,6 km từ Bảo tàng Dân tộc học Việt Nam và 4,2 km từ Sân vận động Mỹ Đình tại thành phố Hà Nội, Granda Central Apartment cung cấp chỗ nghỉ với bếp. Cả WiFi miễn phí và chỗ đỗ xe trong khuôn viên đều được cung cấp miễn phí tại khách sạn căn hộ này. Khách sạn căn hộ này có các phòng gia đình. Khách sạn căn hộ này cung cấp cho khách các phòng nghỉ lắp máy điều hòa với bàn làm việc, ấm đun nước, lò vi sóng, tủ lạnh, két an toàn, TV màn hình phẳng và phòng tắm riêng đi kèm chậu rửa vệ sinh (bidet). Tất cả các phòng nghỉ tại khách sạn căn hộ này đều không gây dị ứng và cách âm. Tất cả các phòng nghỉ tại khách sạn căn hộ này đều có ga trải giường và khăn tắm. Trung tâm thương mại Vincom Center Nguyễn Chí Thanh cách Granda Central Apartment 4,4 km trong khi Bảo tàng Mỹ thuật Việt Nam cách đó 6,8 km. Sân bay quốc tế Nội Bài cách đó 25 km và khách sạn cung cấp dịch vụ đưa đón sân bay có tính phí.	8.571428571428571	2025-03-25 16:40:24.676405	2025-05-20 18:08:38.147757	\N	\N	granda-central-apartment	0101000020E610000052B81E85EB715A4048E17A14AE073540	5.97557202768	\N
\.


--
-- Data for Name: property_facilities; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.property_facilities (id, property_id, facility_id) FROM stdin;
1	1	14
2	1	6
3	1	22
4	1	10
5	1	17
6	1	11
7	1	4
8	2	3
9	2	4
10	2	6
11	2	16
12	2	18
13	2	1
14	2	19
15	3	11
16	3	6
17	3	13
18	3	5
19	3	9
20	3	2
21	3	17
22	4	11
23	4	6
24	4	3
25	4	13
26	4	16
27	4	9
28	4	19
29	5	16
30	5	12
31	5	22
32	5	7
33	5	5
34	5	1
35	5	2
36	5	14
37	6	11
38	6	22
39	6	6
40	6	20
41	6	9
42	6	21
43	6	4
44	6	13
45	7	2
46	7	20
47	7	19
48	7	4
49	7	9
50	7	5
51	7	21
52	8	15
53	8	19
54	8	21
55	8	1
56	8	5
57	8	11
58	8	13
59	9	22
60	9	2
61	9	5
62	9	14
63	9	10
64	9	13
65	9	1
66	9	18
67	10	6
68	10	19
69	10	11
70	10	8
71	10	22
72	10	21
73	10	10
74	10	15
75	11	1
76	11	19
77	11	5
78	11	8
79	11	9
80	11	16
81	11	18
82	12	22
83	12	8
84	12	7
85	12	20
86	12	17
87	12	19
88	12	16
89	13	11
90	13	15
91	13	2
92	13	5
93	13	19
94	13	14
95	13	3
96	14	6
97	14	2
98	14	17
99	14	22
100	14	14
101	14	13
102	14	11
103	14	10
104	15	7
105	15	2
106	15	3
107	15	16
108	15	20
109	15	8
110	15	19
111	16	3
112	16	18
113	16	11
114	16	5
115	16	6
116	16	20
117	16	17
118	16	8
119	17	3
120	17	13
121	17	12
122	17	22
123	17	19
124	17	16
125	17	11
126	17	18
127	18	2
128	18	15
129	18	17
130	18	14
131	18	3
132	18	22
133	18	4
134	19	19
135	19	15
136	19	5
137	19	1
138	19	20
139	19	7
140	19	18
141	19	10
142	20	22
143	20	1
144	20	13
145	20	6
146	20	18
147	20	8
148	20	21
149	20	7
150	21	10
151	21	16
152	21	11
153	21	20
154	21	18
155	21	7
156	21	6
157	21	13
\.


--
-- Data for Name: property_images; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.property_images (id, property_id, image) FROM stdin;
1	1	https://cf.bstatic.com/xdata/images/hotel/max1024x768/274543555.jpg?k=94e5eecc58931591e05c6e65bef9bc878820145cf008a28bf586f40946d228cd&o=
2	1	https://cf.bstatic.com/xdata/images/hotel/max500/274543815.jpg?k=f6c0dc4d0d1d175a8de643d6e23c80b620147156fb01a220938577872782d384&o=
3	1	https://cf.bstatic.com/xdata/images/hotel/max300/274543826.jpg?k=2af0fe3feb1418def8854092e59a5f9623ef35e1581fb1ae4108c89d780362db&o=
4	2	https://cf.bstatic.com/xdata/images/hotel/max1024x768/650206950.jpg?k=c69cb7117de33d77ffb71333bae089d663c1fc9774e347e56f1c14fd4ea43839&o=
5	2	https://cf.bstatic.com/xdata/images/hotel/max500/651181660.jpg?k=dc954edcadb050d900f108a4e27826ad86f8058bccfb9cb55bb640bf1b47093a&o=
6	2	https://cf.bstatic.com/xdata/images/hotel/max300/625876441.jpg?k=ebe9a9d97a1ebcc5fad3ad3c4195b303dfeff462d4278e9bdbcb817625b2ba48&o=
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
\.


--
-- Data for Name: refund_bills; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.refund_bills (id, created_at, created_by, updated_at, updated_by, vnp_amount, vnp_bank_code, vnp_command, vnp_message, vnp_order_info, vnp_pay_date, vnp_response_code, vnp_response_id, vnp_secure_hash, vnp_tmn_code, vnp_transaction_no, vnp_transaction_status, vnp_transaction_type, vnp_txn_ref, email) FROM stdin;
4	2025-05-23 15:06:20.873057	\N	\N	\N	1338600	NCB	refund	Refund success	Refund	20250523150624	00	934f8e45c01b401f9c654bc42207af1f	c01173c079f7f54117724f587feaa1f42e9d69e032d3f93b7718c3360d649756d13fbc5684d3fec373f4241bdabad8c459620076a303c57bcb5b9110089dda68	58X4B4HP	14975190	05	02	85724676	ckp2004vn@gmail.com
5	2025-05-23 15:07:17.288834	\N	\N	\N	739200	NCB	refund	Refund success	Refund	20250523150720	00	1f81ab1735d547b9a5e408a5e59d3b2d	07e8763c82e300328d08bc981fae98a7f4637cc96adb324e0cee6eeeaf10aeda88b1bf993d4059876cdd3f181268aa5444380cf29b4490022c81d157254a64d7	58X4B4HP	14975191	05	02	22933029	ckp2004vn@gmail.com
6	2025-05-23 15:31:26.446999	\N	\N	\N	334650	NCB	refund	Refund success	Refund	20250523153129	00	b510f1f0a27b48beb2fe508944053dee	7252bc89eac908b3e7f52861a91ed8bb9a6241b04aaee9ff9bfc0632ef1d4200516a5bc72357ad356e6922dbb390a4ac2ae6f28170ebc79d186e2bdc9094d4bc	58X4B4HP	14975266	05	03	88165914	ckp2004vn@gmail.com
7	2025-05-23 15:34:05.522153	\N	\N	\N	393600	NCB	refund	Refund success	Refund	20250523153408	00	23c33b13510f4257b69b20c6bdeb294d	3ccf8f6f8bdba6ba971fd021ba3cb882eaa9c2be11ed668de3770f4c5cf8a2e8817542cdacc2b585ad07f7cc3721caa726c9958972a84a14917b9d59316bf1a9	58X4B4HP	14975263	05	03	62608948	ckp2004vn@gmail.com
8	2025-05-23 15:39:02.371633	\N	\N	\N	334650	NCB	refund	Refund success	Refund	20250523153905	00	3bb650994d5c4296a6b92bdf64d9d49c	5130451d99bcc3992bcef1529f4e7314c0f96bc76ccc3fff457d4fcc0f0e8c35dee9ffd082ebb8696be3b6b46740e509dc9b37278c7adefd709ae6b6e301d471	58X4B4HP	14975260	05	03	21201811	ckp2004vn@gmail.com
9	2025-05-23 15:56:52.186348	\N	\N	\N	2325000	NCB	refund	Refund success	Refund	20250523155655	00	15f735031b164c239f06bd2f52df116e	406faed3cfa16389ac94357126a18358966d233a95667cc91ba96db2fe1ea26c20e6597d396b4e335f3a4520e0b1fb638c8de2f24257575a25aeee0e669d6809	58X4B4HP	14975306	05	02	58744898	ckp2004vn@gmail.com
10	2025-05-23 15:57:43.015399	\N	\N	\N	739200	NCB	refund	Refund success	Refund	20250523155746	00	afebb6e451604f5cafb2a1c56ab90e8f	80009385263380839e5ea61c25f4d3e7e1dcc195f81d04363456090b6dc9013c10e1b0b3e7b1531d5c4582ef36e95e997a92e6d75e31f27e98d0cb5f90986449	58X4B4HP	14975302	05	02	77424057	ckp2004vn@gmail.com
\.


--
-- Data for Name: review_images; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.review_images (review_id, image_url) FROM stdin;
\.


--
-- Data for Name: reviews; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.reviews (id, content, rating_property, rating_staff, rating_facilities, rating_clean, rating_comfort, rating_location, rating_wifi, created_at, created_by, updated_at, updated_by, property_id, user_id) FROM stdin;
32	hehe	3	10	10	10	10	10	10	2025-05-20 18:07:17.861903	\N	\N	\N	6	68
33	cucu	3	10	10	10	10	10	10	2025-05-20 18:07:44.312557	\N	\N	\N	6	68
34	haha	2	10	10	10	10	10	10	2025-05-20 18:08:11.80872	\N	\N	\N	7	68
35	Khách sạn đẹp	5	10	10	10	10	10	10	2025-05-20 18:08:38.14376	\N	\N	\N	8	68
36	hay	5	4	3	10	5	8	10	2025-05-20 18:10:47.972725	\N	\N	\N	9	68
37	aaaaaaaaaaaa	1	5	8	10	9	1	10	2025-05-20 18:11:13.49365	\N	\N	\N	10	68
38	hehe	4	6	10	10	1	10	10	2025-05-20 18:11:37.387732	\N	\N	\N	11	68
39	hahaa	3	10	10	10	10	10	10	2025-05-20 18:14:37.857617	\N	\N	\N	5	68
40	khách sạn đẹp	3	7	10	10	10	10	7	2025-05-22 15:50:41.315543	\N	\N	\N	12	68
\.


--
-- Data for Name: role_permissions; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.role_permissions (id, role_id, permission_id, created_at, updated_at) FROM stdin;
1	1	1	2025-04-07 20:54:46.250167	\N
4	1	2	2025-04-07 22:04:18.934378	\N
5	1	3	2025-04-07 22:04:18.934378	\N
6	1	4	2025-04-07 22:04:18.934378	\N
7	2	1	2025-04-07 22:04:18.934378	\N
8	2	2	2025-04-07 22:04:18.934378	\N
9	2	3	2025-04-07 22:04:18.934378	\N
10	2	4	2025-04-07 22:04:18.934378	\N
11	4	1	2025-05-14 15:01:16.488628	\N
12	4	2	2025-05-14 15:01:16.488628	\N
13	4	3	2025-05-14 15:01:16.488628	\N
14	4	4	2025-05-14 15:01:16.488628	\N
\.


--
-- Data for Name: roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.roles (id, name, created_at, updated_at, created_by, updated_by) FROM stdin;
1	USER	2025-04-07 20:49:01.425205	\N	\N	\N
2	ADMIN	2025-04-07 20:49:01.425205	\N	\N	\N
3	STAFF	2025-04-07 20:49:01.425205	\N	\N	\N
4	MANAGER	2025-04-07 20:49:01.425205	\N	\N	\N
\.


--
-- Data for Name: room_chats; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.room_chats (id, created_at, created_by, updated_at, updated_by, user_aid, user_bid) FROM stdin;
3	2025-05-15 16:25:33.971538	\N	\N	\N	71	70
6	2025-05-17 10:38:20.480546	\N	\N	\N	74	70
7	2025-05-19 10:44:43.503386	\N	\N	\N	75	70
8	2025-05-20 08:42:01.623725	\N	\N	\N	76	70
9	2025-05-20 09:13:46.502544	\N	\N	\N	80	70
10	2025-05-20 09:17:18.546686	\N	\N	\N	81	70
11	2025-05-20 21:28:45.751683	\N	\N	\N	82	70
4	2025-05-15 16:25:33.971538	\N	\N	\N	68	70
5	2025-05-15 16:25:33.971538	\N	\N	\N	69	70
\.


--
-- Data for Name: room_type; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.room_type (id, property_id, name, price, max_guests, num_beds, free_services, total_rooms, created_at, area, discount, created_by, updated_at, updated_by, facilities, free, status, remain) FROM stdin;
17	8	Garden Suite	2500000	3	2	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	55	7	\N	\N	\N	\N	\N	t	\N
19	9	Luxury King Suite	2700000	3	2	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	70	8	\N	\N	\N	\N	\N	t	\N
23	11	Premium Suite	3500000	4	2	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	75	9	\N	\N	\N	\N	\N	t	\N
27	13	Family Suite with Balcony	1800000	4	2	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	50	7	\N	\N	\N	\N	\N	t	\N
29	14	Presidential Suite with Sea View	5500000	4	2	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	85	12	\N	\N	\N	\N	\N	t	\N
1	1	Deluxe Double Room	1168750	2	1	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:25:34.829707	20	4	\N	\N	\N	\N	\N	t	\N
2	1	Twin Double Room	660000	2	1	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	20	5	\N	\N	\N	\N	\N	t	\N
3	1	Economy Double Room	1320000	2	1	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	30	4	\N	\N	\N	\N	\N	t	\N
4	2	Deluxe King Room	880000	2	1	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	25	5	\N	\N	\N	\N	\N	t	\N
5	2	Family Suite	1540000	4	2	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	45	7	\N	\N	\N	\N	\N	t	\N
6	3	Standard Double Room	600000	2	1	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	18	3	\N	\N	\N	\N	\N	t	\N
7	3	Luxury Suite	2200000	3	2	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	50	8	\N	\N	\N	\N	\N	t	\N
8	4	Single Room	400000	1	1	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	15	2	\N	\N	\N	\N	\N	t	\N
9	4	Presidential Suite	5000000	4	2	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	80	10	\N	\N	\N	\N	\N	t	\N
10	5	Superior Twin Room	770000	2	2	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	22	4	\N	\N	\N	\N	\N	t	\N
11	5	Budget Room	550000	2	1	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	18	2	\N	\N	\N	\N	\N	t	\N
12	6	Executive Room	2100000	2	1	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	35	6	\N	\N	\N	\N	\N	t	\N
13	6	Penthouse Suite	4200000	4	3	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	100	12	\N	\N	\N	\N	\N	t	\N
14	7	Classic Room	780000	2	1	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	24	4	\N	\N	\N	\N	\N	t	\N
15	7	Junior Suite	1650000	3	2	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	48	6	\N	\N	\N	\N	\N	t	\N
16	8	Double Room with View	990000	2	1	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	30	5	\N	\N	\N	\N	\N	t	\N
18	9	Economy Room	480000	1	1	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	20	2	\N	\N	\N	\N	\N	t	\N
20	10	Standard Twin Room	650000	2	1	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	22	3	\N	\N	\N	\N	\N	t	\N
21	10	Royal Suite	3900000	4	2	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	90	10	\N	\N	\N	\N	\N	t	\N
22	11	Luxury Studio	1950000	2	1	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	38	6	\N	\N	\N	\N	\N	t	\N
24	12	Single Budget Room	380000	1	1	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	14	2	\N	\N	\N	\N	\N	t	\N
25	12	Double Luxury Room	990000	2	1	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	28	4	\N	\N	\N	\N	\N	t	\N
26	13	Executive Twin Room	880000	2	1	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	26	4	\N	\N	\N	\N	\N	t	\N
28	14	Cozy Double Room	720000	2	1	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	22	4	\N	\N	\N	\N	\N	t	\N
30	15	Superior King Room	1100000	2	1	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	30	5	\N	\N	\N	\N	\N	t	\N
31	15	Deluxe Family Suite	2000000	4	2	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	50	8	\N	\N	\N	\N	\N	t	\N
32	16	Premium Room	990000	2	1	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	28	5	\N	\N	\N	\N	\N	t	\N
33	16	Luxury Villa	7200000	6	3	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	150	15	\N	\N	\N	\N	\N	t	\N
34	17	Cozy Single Room	500000	1	1	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	18	3	\N	\N	\N	\N	\N	t	\N
35	17	Suite with Jacuzzi	2700000	3	2	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	60	9	\N	\N	\N	\N	\N	t	\N
38	19	Standard Family Room	1300000	4	2	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	40	6	\N	\N	\N	\N	\N	t	\N
39	19	Executive Suite with Pool View	4600000	4	2	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	100	12	\N	\N	\N	\N	\N	t	\N
40	20	Simple Double Room	690000	2	1	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	22	3	\N	\N	\N	\N	\N	t	\N
41	20	Royal Penthouse Suite	8800000	6	3	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	200	20	\N	\N	\N	\N	\N	t	\N
36	18	Deluxe Twin Room	820000	2	1	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	27	4	\N	\N	\N	\N	\N	t	\N
37	18	Luxury Apartment	3500000	4	2	["Wi-Fi miễn phí", "Bể bơi ngoài trời", "Bể bơi trong nhà", "Trung tâm thể dục", "Nhà hàng", "Quầy bar", "Dịch vụ phòng 24/7", "Lễ tân 24/7", "Bãi đỗ xe miễn phí", "Dịch vụ đưa đón sân bay", "Phòng không hút thuốc", "Phòng gia đình", "Dịch vụ giặt là", "Spa & chăm sóc sức khỏe", "Máy lạnh", "Két an toàn", "Tivi màn hình phẳng", "Máy pha cà phê", "Bàn làm việc", "Ban công riêng", "View biển", "Bữa sáng miễn phí"]	3	2025-03-25 21:51:45.373497	80	10	\N	\N	\N	\N	\N	t	\N
\.


--
-- Data for Name: rooms; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.rooms (id, room_number, status, property_id, room_type_id, created_at, created_by, updated_at, updated_by, check_in, check_out) FROM stdin;
2	102	available	1	1	2025-03-27 11:31:14.281776	\N	2025-05-23 00:04:30.16571	\N	2025-05-08 07:00:00	2025-05-09 07:00:00
1	101	available	1	1	2025-03-27 11:31:14.281776	\N	2025-05-23 14:50:48.505362	\N	2025-05-22 07:00:00	2025-05-24 07:00:00
109	201	available	18	37	2025-03-27 11:31:14.281776	\N	2025-05-04 22:59:28.844154	\N	2025-05-15 07:00:00	2025-05-17 07:00:00
110	202	available	18	37	2025-03-27 11:31:14.281776	\N	2025-05-04 22:59:28.850155	\N	2025-05-15 07:00:00	2025-05-17 07:00:00
4	201	available	1	2	2025-03-27 11:31:14.281776	\N	2025-05-17 22:33:45.755856	\N	2025-05-14 07:00:00	2025-05-16 07:00:00
119	102	available	20	40	2025-03-27 11:31:14.281776	\N	2025-05-23 08:28:41.073155	\N	2025-05-22 07:00:00	2025-05-24 07:00:00
120	103	available	20	40	2025-03-27 11:31:14.281776	\N	2025-05-23 15:26:14.629841	\N	2025-05-23 07:00:00	2025-05-24 07:00:00
107	102	available	18	36	2025-03-27 11:31:14.281776	\N	2025-05-23 15:27:16.970099	\N	2025-05-24 07:00:00	2025-05-25 07:00:00
118	101	available	20	40	2025-03-27 11:31:14.281776	\N	2025-05-23 15:28:25.814384	\N	2025-05-24 07:00:00	2025-05-25 07:00:00
106	101	available	18	36	2025-03-27 11:31:14.281776	\N	2025-05-23 15:40:54.789672	\N	2025-05-29 07:00:00	2025-05-30 07:00:00
112	101	available	19	38	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
113	102	available	19	38	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
114	103	available	19	38	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
115	201	available	19	39	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
116	202	available	19	39	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
117	203	available	19	39	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
121	201	available	20	41	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
122	202	available	20	41	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
123	203	available	20	41	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
111	203	available	18	37	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
108	103	available	18	36	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
3	103	available	1	1	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
5	202	available	1	2	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
43	201	available	7	15	2025-03-27 11:31:14.281776	\N	\N	\N	2025-05-01 07:00:00	2025-05-02 07:00:00
13	201	available	2	5	2025-03-27 11:31:14.281776	\N	\N	\N	2025-05-16 07:00:00	2025-05-17 07:00:00
42	103	available	7	14	2025-03-27 11:31:14.281776	\N	2025-05-04 23:10:00.639521	\N	2025-05-01 07:00:00	2025-05-03 07:00:00
33	203	available	5	11	2025-03-27 11:31:14.281776	\N	2025-05-17 21:56:57.098998	\N	2025-05-27 07:00:00	2025-05-28 07:00:00
31	201	available	5	11	2025-03-27 11:31:14.281776	\N	2025-05-17 21:56:57.13172	\N	2025-05-27 07:00:00	2025-05-28 07:00:00
10	101	available	2	4	2025-03-27 11:31:14.281776	\N	2025-05-23 07:43:30.522038	\N	2025-05-15 07:00:00	2025-05-17 07:00:00
30	103	available	5	10	2025-03-27 11:31:14.281776	\N	2025-05-23 14:49:24.70855	\N	2025-05-29 07:00:00	2025-05-30 07:00:00
29	102	available	5	10	2025-03-27 11:31:14.281776	\N	2025-05-23 15:04:09.875895	\N	2025-05-16 07:00:00	2025-05-17 07:00:00
28	101	available	5	10	2025-03-27 11:31:14.281776	\N	2025-05-23 15:41:47.791462	\N	2025-05-29 07:00:00	2025-05-30 07:00:00
49	201	available	8	17	2025-03-27 11:31:14.281776	\N	2025-05-23 15:44:00.162685	\N	2025-05-29 07:00:00	2025-05-30 07:00:00
40	101	available	7	14	2025-03-27 11:31:14.281776	\N	\N	\N	2025-05-01 07:00:00	2025-05-03 07:00:00
45	203	available	7	15	2025-03-27 11:31:14.281776	\N	\N	\N	2025-05-01 07:00:00	2025-05-03 07:00:00
44	202	available	7	15	2025-03-27 11:31:14.281776	\N	\N	\N	2025-05-01 07:00:00	2025-05-03 07:00:00
41	102	available	7	14	2025-03-27 11:31:14.281776	\N	\N	\N	2025-05-01 07:00:00	2025-05-02 07:00:00
32	202	available	5	11	2025-03-27 11:31:14.281776	\N	\N	\N	2025-05-01 07:00:00	2025-05-03 07:00:00
6	203	available	1	2	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
7	301	available	1	3	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
8	302	available	1	3	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
9	303	available	1	3	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
11	102	available	2	4	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
12	103	available	2	4	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
14	202	available	2	5	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
15	203	available	2	5	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
16	101	available	3	6	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
17	102	available	3	6	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
18	103	available	3	6	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
19	201	available	3	7	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
20	202	available	3	7	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
21	203	available	3	7	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
22	101	available	4	8	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
23	102	available	4	8	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
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
46	101	available	8	16	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
47	102	available	8	16	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
48	103	available	8	16	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
50	202	available	8	17	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
51	203	available	8	17	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
52	101	available	9	18	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
53	102	available	9	18	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
54	103	available	9	18	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
55	201	available	9	19	2025-03-27 11:31:14.281776	\N	\N	\N	\N	\N
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
71	102	available	12	24	2025-03-27 11:31:14.281776	\N	\N	\N	2025-04-01 07:00:00	2025-04-02 07:00:00
67	201	available	11	23	2025-03-27 11:31:14.281776	\N	\N	\N	2025-05-01 07:00:00	2025-05-02 07:00:00
85	201	available	14	29	2025-03-27 11:31:14.281776	\N	2025-05-17 10:52:08.505691	\N	2025-05-16 07:00:00	2025-05-17 07:00:00
88	101	available	15	30	2025-03-27 11:31:14.281776	\N	2025-05-17 11:02:50.806398	\N	2025-05-16 07:00:00	2025-05-17 07:00:00
68	202	available	11	23	2025-03-27 11:31:14.281776	\N	2025-05-22 16:06:07.219166	\N	2025-05-29 07:00:00	2025-05-30 07:00:00
72	103	available	12	24	2025-03-27 11:31:14.281776	\N	2025-05-22 22:59:18.269463	\N	2025-05-09 07:00:00	2025-05-10 07:00:00
70	101	available	12	24	2025-03-27 11:31:14.281776	\N	2025-05-23 00:03:42.549616	\N	2025-05-30 07:00:00	2025-05-31 07:00:00
69	203	available	11	23	2025-03-27 11:31:14.281776	\N	2025-05-23 14:50:05.434793	\N	2025-05-30 07:00:00	2025-05-31 07:00:00
\.


--
-- Data for Name: spatial_ref_sys; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.spatial_ref_sys (srid, auth_name, auth_srid, srtext, proj4text) FROM stdin;
\.


--
-- Data for Name: trip; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.trip (id, name, trip_type, city_id, latitude, longitude, image, created_at, created_by, updated_at, updated_by, geog, slug) FROM stdin;
1	Lăng chủ tịch	Outdoors	1	21.0370775281	105.8348061708	https://lh5.googleusercontent.com/p/AF1QipNk0hiKji1aeJiKGmyWvsQkokDlNbXQyJufTMiV=w408-h307-k-no	\N	\N	\N	\N	0101000020E610000085EB51B81E755A400AD7A3703D0A3540	\N
2	Hồ Tây	Outdoors	1	21.0531446601	105.8246799503	https://lh5.googleusercontent.com/p/AF1QipNMXIxamQP1T5Y8-MWHHivPt109t7KUeS3Fra_c=w408-h271-k-no	\N	\N	\N	\N	0101000020E610000014AE47E17A745A40CDCCCCCCCC0C3540	\N
3	Hồ Hoàn Kiếm	Outdoors	1	21.0280837424	105.8521715769	https://lh5.googleusercontent.com/p/AF1QipN3-_0wrzFsf30vYg5nR6mwLroFyNp-qYsnV6B6=w408-h246-k-no	\N	\N	\N	\N	0101000020E61000006666666666765A4048E17A14AE073540	\N
4	Phố đường tàu	Outdoors	1	21.0336403064	105.8453911495	https://lh5.googleusercontent.com/p/AF1QipMML6HcF8W-K9js2Bo2pr5aRVip57Fn7bTlPswY=w408-h510-k-no	\N	\N	\N	\N	0101000020E6100000F6285C8FC2755A4048E17A14AE073540	\N
5	Biển Nha Trang	Beach	5	12.2392408962	109.1991084765	https://lh3.googleusercontent.com/p/AF1QipNhKiLpbzC-Sa2JyfXz95YKUBdxhHfIeHiYuw3O=w426-h240-k-no	\N	\N	\N	\N	0101000020E6100000CDCCCCCCCC4C5B407B14AE47E17A2840	\N
6	Bãi biển Thụy Khê	Beach	3	16.0636534937	108.2482253592	https://lh3.googleusercontent.com/p/AF1QipPgpkvaWeKD9pejm2Org-oEx-SWXLyGH_qSUneu=s294-w294-h220-k-no	\N	\N	\N	\N	0101000020E61000000000000000105B4052B81E85EB113040	\N
7	Cầu rồng	Outdoors	3	16.0613874028	108.2296927391	https://lh5.googleusercontent.com/p/AF1QipORTOLHKjRkpcwmf9QE6qs_smZwgf-tPEdkwxlL=w408-h272-k-no	\N	\N	\N	\N	0101000020E61000001F85EB51B80E5B408FC2F5285C0F3040	\N
8	Ba Na Hills	Outdoors	3	16.0268767431	108.0396891553	https://lh5.googleusercontent.com/p/AF1QipMW717XyWoWQH5nR6KQ1ukbcSczJNGcbU-On08r=w426-h240-k-no	\N	\N	\N	\N	0101000020E61000000000000000005B400000000000003040	\N
9	Đà Lạt	Outdoors	4	11.9324522531	108.4644512975	https://nhuytravel.net/wp-content/uploads/2023/08/samten-hills-da-lat-savingbooking1.jpg	\N	\N	\N	\N	0101000020E61000008FC2F5285C1F5B4048E17A14AEC72740	\N
\.


--
-- Data for Name: user_discount_cars; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_discount_cars (id, discount_car_id, email) FROM stdin;
6	1	ckp2004vn@gmail.com
7	2	ckp2004vn@gmail.com
\.


--
-- Data for Name: user_discounts; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_discounts (id, created_at, created_by, updated_at, updated_by, discount_id, email, status) FROM stdin;
\.


--
-- Data for Name: user_roles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_roles (id, role_id, user_id, created_at, updated_at) FROM stdin;
40	1	68	2025-04-30 09:23:02.757682	\N
41	1	69	2025-04-30 22:19:49.822145	\N
42	4	70	2025-05-14 14:59:07.014228	\N
44	1	71	2025-05-15 16:25:32.344825	\N
47	1	74	2025-05-17 10:38:20.333698	\N
48	1	75	2025-05-19 10:44:40.138203	\N
49	1	76	2025-05-20 08:41:59.637924	\N
50	1	77	2025-05-20 09:08:47.528575	\N
51	1	78	2025-05-20 09:09:07.382005	\N
52	1	79	2025-05-20 09:09:14.067481	\N
53	1	80	2025-05-20 09:13:46.204537	\N
54	1	81	2025-05-20 09:17:18.191493	\N
55	1	82	2025-05-20 21:28:44.398512	\N
\.


--
-- Data for Name: user_visits; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.user_visits (id, user_id, accessed_at) FROM stdin;
1	0	2025-05-17 09:35:21.601066
2	0	2025-05-17 09:49:04.867123
3	0	2025-05-16 09:35:21.601066
4	68	2025-05-17 10:05:26.729626
5	68	2025-05-17 10:05:39.504231
6	68	2025-05-17 10:05:52.005216
7	68	2025-05-17 10:06:27.877622
8	68	2025-05-17 10:06:53.863709
9	68	2025-05-17 10:07:03.463
10	68	2025-05-17 10:23:30.456061
11	68	2025-05-17 10:23:38.403834
12	68	2025-05-17 10:26:28.928964
13	72	2025-05-17 10:32:50.059727
14	74	2025-05-17 10:38:38.712814
15	70	2025-05-17 10:39:05.058561
16	70	2025-05-17 10:40:03.943947
17	74	2025-05-17 10:50:38.963933
18	74	2025-05-17 11:01:35.997284
19	74	2025-05-17 11:01:40.163927
20	70	2025-05-17 20:08:59.717327
21	0	2025-05-17 20:09:06.325721
22	70	2025-05-17 20:09:15.755061
23	0	2025-05-17 20:19:12.603278
24	68	2025-05-17 20:19:20.787964
25	68	2025-05-17 20:19:24.555096
26	68	2025-05-17 20:42:15.748058
27	68	2025-05-17 20:44:05.965835
28	70	2025-05-17 21:14:00.886486
29	0	2025-05-17 21:52:19.723767
30	0	2025-05-17 21:52:27.853425
31	68	2025-05-17 21:55:39.149706
32	70	2025-05-17 22:20:31.581698
33	68	2025-05-17 22:30:59.721903
34	68	2025-05-17 22:31:04.234769
35	68	2025-05-17 22:51:20.352184
36	70	2025-05-18 15:12:27.51783
37	70	2025-05-18 15:12:35.474617
38	70	2025-05-18 16:14:58.728856
39	0	2025-05-18 16:17:24.508171
40	70	2025-05-18 16:17:42.310655
41	0	2025-05-18 22:58:59.692398
42	70	2025-05-18 22:59:36.000596
43	70	2025-05-18 23:59:57.839988
44	70	2025-05-19 09:05:58.439775
45	70	2025-05-19 09:06:03.453658
46	70	2025-05-19 10:19:34.402935
47	70	2025-05-19 10:21:16.0224
48	0	2025-05-19 10:22:12.96959
49	68	2025-05-19 10:22:20.983183
50	68	2025-05-19 10:27:14.773177
51	68	2025-05-19 10:27:33.64342
52	68	2025-05-19 10:28:57.658952
53	68	2025-05-19 10:29:28.118763
54	68	2025-05-19 10:33:03.154454
55	68	2025-05-19 10:36:52.196428
56	68	2025-05-19 10:37:02.536798
57	68	2025-05-19 10:38:02.282894
58	68	2025-05-19 10:38:17.946467
59	68	2025-05-19 10:38:19.785192
60	68	2025-05-19 10:38:37.093735
61	68	2025-05-19 10:40:21.6005
62	68	2025-05-19 10:40:49.18983
63	68	2025-05-19 10:42:21.105654
64	68	2025-05-19 10:43:37.195735
65	70	2025-05-20 08:26:57.830107
66	0	2025-05-20 08:27:31.208546
67	68	2025-05-20 08:27:37.799724
68	68	2025-05-20 08:27:41.210363
69	70	2025-05-20 08:27:55.25725
70	68	2025-05-20 08:28:00.919234
71	68	2025-05-20 08:28:05.64843
72	68	2025-05-20 08:33:25.482809
73	68	2025-05-20 08:35:59.097442
74	68	2025-05-20 08:41:08.669931
75	68	2025-05-20 08:41:19.933146
76	68	2025-05-20 09:06:35.611812
77	81	2025-05-20 09:19:46.552192
78	70	2025-05-20 09:31:13.65812
79	81	2025-05-20 09:31:19.413199
80	81	2025-05-20 09:51:46.613325
81	68	2025-05-20 10:44:22.078226
82	70	2025-05-20 10:45:09.55522
83	68	2025-05-20 10:45:24.922368
84	68	2025-05-20 11:24:31.029386
85	68	2025-05-20 11:37:07.234477
86	68	2025-05-20 11:40:58.118634
87	70	2025-05-20 14:20:56.14441
88	70	2025-05-20 14:24:08.743058
89	70	2025-05-20 15:55:02.810293
90	0	2025-05-20 16:01:58.331654
91	68	2025-05-20 16:02:04.077945
92	68	2025-05-20 16:19:39.16907
93	70	2025-05-20 17:15:48.244286
94	68	2025-05-20 17:15:52.205702
95	68	2025-05-20 17:16:41.427816
96	68	2025-05-20 18:06:39.39809
97	68	2025-05-20 18:07:53.549344
98	68	2025-05-20 18:08:43.565365
99	70	2025-05-20 18:16:35.183463
100	68	2025-05-20 18:28:58.97097
101	70	2025-05-20 18:52:54.935136
102	70	2025-05-20 18:55:10.484653
103	70	2025-05-20 21:25:10.036998
104	70	2025-05-20 21:25:59.763523
105	0	2025-05-20 21:27:09.998677
106	68	2025-05-20 21:27:15.640392
107	82	2025-05-20 21:29:06.420314
108	82	2025-05-20 21:32:20.322331
109	82	2025-05-20 21:32:22.088461
110	82	2025-05-20 21:33:30.064182
111	68	2025-05-20 22:10:03.398747
112	0	2025-05-20 22:13:19.645709
113	82	2025-05-20 22:13:34.776024
114	0	2025-05-20 22:17:41.543555
115	68	2025-05-20 22:17:52.335852
116	0	2025-05-20 22:19:56.440452
117	82	2025-05-20 22:20:05.724733
118	70	2025-05-20 22:26:43.964281
119	0	2025-05-20 22:27:19.825003
120	68	2025-05-20 22:27:28.537434
121	0	2025-05-20 22:38:52.314471
122	82	2025-05-20 22:39:03.224867
123	0	2025-05-20 22:40:12.520454
124	68	2025-05-20 22:40:19.227096
125	0	2025-05-20 22:45:49.958466
126	82	2025-05-20 22:46:00.755142
127	0	2025-05-20 22:49:34.403048
128	68	2025-05-20 22:49:40.945648
129	0	2025-05-21 21:06:58.939638
130	70	2025-05-21 21:07:15.563
131	70	2025-05-21 22:08:28.071898
132	70	2025-05-21 23:29:43.669839
133	70	2025-05-22 14:45:41.581012
134	0	2025-05-22 14:45:48.410665
135	0	2025-05-22 14:45:50.547664
136	70	2025-05-22 15:01:06.779808
137	68	2025-05-22 15:01:16.73492
138	68	2025-05-22 15:50:13.651338
139	68	2025-05-22 15:54:14.5456
140	68	2025-05-22 15:57:09.58569
141	68	2025-05-22 16:00:02.034471
142	68	2025-05-22 16:05:14.986788
143	68	2025-05-22 16:05:20.887506
144	68	2025-05-22 17:10:32.261211
145	68	2025-05-22 22:04:53.127858
146	68	2025-05-22 22:04:58.27025
147	68	2025-05-22 22:15:09.967337
148	68	2025-05-22 22:18:22.184613
149	68	2025-05-22 22:52:12.430663
150	68	2025-05-22 23:05:33.202147
151	68	2025-05-22 23:19:04.655748
152	68	2025-05-22 23:36:07.416727
153	68	2025-05-22 23:41:23.46805
154	68	2025-05-22 23:51:19.841577
155	68	2025-05-22 23:55:08.412968
156	68	2025-05-22 23:57:29.477682
157	68	2025-05-23 00:03:03.357023
158	68	2025-05-23 00:03:44.454624
159	68	2025-05-23 00:04:32.224981
160	0	2025-05-23 07:27:54.801444
161	0	2025-05-23 07:28:12.333391
162	68	2025-05-23 07:28:31.045608
163	68	2025-05-23 07:32:53.206786
164	0	2025-05-23 07:35:49.869508
165	70	2025-05-23 07:35:59.317662
166	68	2025-05-23 07:42:36.68113
167	68	2025-05-23 08:27:36.026883
168	0	2025-05-23 08:28:44.591241
169	68	2025-05-23 08:28:55.009942
170	68	2025-05-23 14:42:08.473421
171	68	2025-05-23 14:48:35.852643
172	68	2025-05-23 14:49:29.888337
173	68	2025-05-23 14:50:09.255913
174	68	2025-05-23 14:50:58.731806
175	68	2025-05-23 15:03:34.34878
176	68	2025-05-23 15:04:12.150102
177	0	2025-05-23 15:05:21.67976
178	70	2025-05-23 15:05:27.651958
179	68	2025-05-23 15:25:13.395066
180	68	2025-05-23 15:25:31.051394
181	68	2025-05-23 15:26:21.941967
182	68	2025-05-23 15:27:50.469637
183	68	2025-05-23 15:28:37.656624
184	68	2025-05-23 15:40:12.594939
185	68	2025-05-23 15:41:00.424858
186	68	2025-05-23 15:42:23.988153
187	68	2025-05-23 15:42:46.490112
188	68	2025-05-23 15:42:53.202705
189	68	2025-05-23 15:44:03.676062
\.


--
-- Data for Name: users; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.users (id, email, password, first_name, last_name, address, avatar, created_at, updated_at, created_by, updated_by, birthday, city, district, gender, phone_number, village, send_email) FROM stdin;
77	mai@gmail.com	$2a$12$w3uElT9UmtT2Y/iRPVtjOOnVmtEUnhtIXaxFNBbAjIhREA/lGzC9i	Nguyễn Mai 	Anh	Xóm 3	https://as2.ftcdn.net/v2/jpg/03/49/49/79/1000_F_349497933_Ly4im8BDmHLaLzgyKg2f2yZOvJjBtlw5.jpg	2025-05-20 09:08:47.524833	\N	\N	\N	\N	Hà Nội	Quốc Oai	Nữ	0969716421	Yên Nội	t
78	mai123@gmail.com	$2a$12$0fNkcGfw0PbCtRPv/yNee.g0/nky5H4IUcxteulHwXPif613rdpTK	Nguyễn Mai 	Anh	Xóm 3	https://as2.ftcdn.net/v2/jpg/03/49/49/79/1000_F_349497933_Ly4im8BDmHLaLzgyKg2f2yZOvJjBtlw5.jpg	2025-05-20 09:09:07.381244	\N	\N	\N	\N	Hà Nội	Quốc Oai	Nữ	0969716421	Yên Nội	t
68	ckp2004vn@gmail.com	$2a$12$D6IC53PCQrEGzdYLLubwZus86hJCu3kISrBHvQ5bQlVa50ECGYjwC	nguyen tien	thuc	Xóm 3	https://th.bing.com/th/id/OIP.I7c_UhRmzUW6dJebLJWbggHaHY?cb=iwp1&rs=1&pid=ImgDetMain	\N	\N	\N	\N	\N	Hà Nội	Quốc Oai	Nam	0969716421	Yên Nội	\N
79	mai1234@gmail.com	$2a$12$qgfov.UBberajg.ZJhyQhO6mwItHCV1mSV5cBiD9haIks9BLd/k/y	Nguyễn Mai 	Anh	Xóm 3	https://as2.ftcdn.net/v2/jpg/03/49/49/79/1000_F_349497933_Ly4im8BDmHLaLzgyKg2f2yZOvJjBtlw5.jpg	2025-05-20 09:09:14.066179	\N	\N	\N	\N	Hà Nội	Quốc Oai	Nữ	0969716421	Yên Nội	t
69	acmilan2k4@gmail.com	$2a$12$SWO37GPiWwDXN8xV2Zhnk.k.I9domu2gkONv3p3G56mhfA.nCHXuC	nguyen tien	nam	Xóm 3	https://haycafe.vn/wp-content/uploads/2023/04/Hinh-anh-avatar-cute-TikTok.jpg	\N	\N	\N	\N	\N	Hà Nội	Quốc Oai	Nam	0969716421	Yên Nội	\N
70	manager@gmail.com	$2a$12$CVpiyfIifsCo60itCZdwFO1KEg1hQ0CdYiKGJ.qMzIrwFnwpAAThy	Nguyễn Văn	A	Xóm 3	https://th.bing.com/th/id/OIP.TtnDZHwNtGChxOMpsbXrngHaFK?o=7&cb=iwp2rm=3&rs=1&pid=ImgDetMain	2025-05-14 14:59:06.765062	\N	\N	\N	\N	Hà Nội	Quốc Oai	Nam	0969716421	Yên Nội	t
71	hung@gmail.com	$2a$12$VLR7/UCknINdfHCWE6mO4uoPeSWB1Gd4lCnLRTB3YheFecIXZ5nCO	nguyen tien	hùng	Xóm 3	\N	2025-05-15 16:25:32.306839	\N	\N	\N	\N	Hà Nội	Quốc Oai	Nam	0969716421	Yên Nội	t
80	tu@gmail.com	$2a$12$nvOP7F5wYekU/RZcG48zUem.YxgGAbPMRnTgRmwTya2.NUy43wvJq	nguyen tien	tu	Xóm 3	https://as2.ftcdn.net/v2/jpg/03/49/49/79/1000_F_349497933_Ly4im8BDmHLaLzgyKg2f2yZOvJjBtlw5.jpg	2025-05-20 09:13:46.202863	\N	\N	\N	\N	Hà Nội	Quốc Oei	Nam	0969716421	Yên Nội	t
74	hoang@gmail.com	$2a$12$hXsjj6QsM6aY9eD5mciFZuEWc9c4lf9Iz3hoyGyOGRArN0RNJCAT2	nguyen tien	hoang	Xóm 3	https://as2.ftcdn.net/v2/jpg/03/49/49/79/1000_F_349497933_Ly4im8BDmHLaLzgyKg2f2yZOvJjBtlw5.jpg	2025-05-17 10:38:20.33197	\N	\N	\N	\N	Hà Nội	Quốc Oai	Nam	0969716421	Yên Nội	t
75	azzz@gmail.com	$2a$12$rhYd.nitAF/e6qJ1wvJ2c.YXRwXuKx3RlR19MM4Rir7V30Yewo6VO	nguyen tien	thuc	Xóm 3	https://as2.ftcdn.net/v2/jpg/03/49/49/79/1000_F_349497933_Ly4im8BDmHLaLzgyKg2f2yZOvJjBtlw5.jpg	2025-05-19 10:44:40.103872	\N	\N	\N	\N	Hà Nội	Quốc Oai	Nam	0969716421	Yên Nội	t
76	nam@gmail.com	$2a$12$B.079pBPDfsaLJUc60gVDuFP3jycSEsnemTnM9CGOi5CU.1K18I06	Nguyễn Văn	Nam	Xóm 3	https://as2.ftcdn.net/v2/jpg/03/49/49/79/1000_F_349497933_Ly4im8BDmHLaLzgyKg2f2yZOvJjBtlw5.jpg	2025-05-20 08:41:59.616038	\N	\N	\N	\N	Hà Nội	Quốc Oai	Nam	0969716421	Yên Nội	t
81	namhv@gmail.com	$2a$12$ZkgCC7z1wMfetSDDteJSoOrLNZtXA3SJ26AUp4fM7F6zSLIDDvaOe	Hoàng Văn	Nam	Xóm 3	https://as2.ftcdn.net/v2/jpg/03/49/49/79/1000_F_349497933_Ly4im8BDmHLaLzgyKg2f2yZOvJjBtlw5.jpg	2025-05-20 09:17:18.18995	\N	\N	\N	\N	Hà Nội	Quốc Oai	Nam	0969716421	Yên Nội	t
82	ha@gmail.com	$2a$12$w.x46gK6wZ3Wl1vG4ZrFn.MPW7ZRQuFCuLtvClBApOWokFCEbzE9O	Ha	Nguyen	Xóm 3	https://res.cloudinary.com/dcerbz3nm/image/upload/b_auto:predominant,c_pad,h_400,w_300/v1747751479/file?_a=DAGAACAWZAA0	2025-05-20 21:28:44.377978	\N	\N	\N	\N	Hà Nội	Quốc Oai	Nữ	0969716421	Yên Nội	t
\.


--
-- Data for Name: vehicles; Type: TABLE DATA; Schema: public; Owner: postgres
--

COPY public.vehicles (id, license_plate, car_type, images, latitude, longitude, discount, price, created_at, updated_at, updated_by, created_by, driver_id, status, quantity, star) FROM stdin;
10	VWX-6541	TAXI	https://th.bing.com/th/id/OIP.GO845nN3RCxQzQEuKeT0AgHaEo?rs=1&pid=ImgDetMain	10.812622	106.710172	20	180000	2025-04-22 23:47:38.645694	2025-04-22 23:47:38.645694	\N	\N	10	INACTIVE	2	5
2	XYZ-5678	SEAT_4	https://th.bing.com/th/id/OIP.bWrR3rouX6qybc67nosWqQAAAA?rs=1&pid=ImgDetMain	10.762622	106.660172	15	300000	2025-04-22 23:47:38.645694	2025-04-22 23:47:38.645694	\N	\N	2	AVAILABLE	1	3
1	ABC-1234	BUS	https://th.bing.com/th/id/OIP.cAko7iWNwEE87_DGYA7LgQHaFj?w=234&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7	10.762622	106.660172	10	500000	2025-04-22 23:47:38.645694	2025-04-22 23:47:38.645694	\N	\N	1	AVAILABLE	1	2
5	TAX-3210	TAXI	https://th.bing.com/th/id/OIP.RjDv4d9VadyiPHCvFaYQpgHaE4?rs=1&pid=ImgDetMain	10.762622	106.660172	10	150000	2025-04-22 23:47:38.645694	2025-04-22 23:47:38.645694	\N	\N	5	AVAILABLE	1	4
3	LMN-9876	SEAT_7	https://th.bing.com/th/id/OIP.-zxE82BjY7gp6gIgEuqDTQHaE7?rs=1&pid=ImgDetMain	10.762622	106.660172	5	400000	2025-04-22 23:47:38.645694	2025-04-22 23:47:38.645694	\N	\N	3	BUSY	5	4
4	DEF-4321	LIMOUSINE	https://flamingoresortdailai.com/wp-content/uploads/2017/01/thue-xe-limousine-16-cho-tai-ha-noi.jpg	10.762622	106.660172	20	1000000	2025-04-22 23:47:38.645694	2025-04-22 23:47:38.645694	\N	\N	4	INACTIVE	3	3
6	JKL-3456	SEAT_4	https://th.bing.com/th/id/OIP.-fCbQxDhOvNfPt65_JDWfwHaE8?rs=1&pid=ImgDetMain	10.773622	106.672172	5	350000	2025-04-22 23:47:38.645694	2025-04-22 23:47:38.645694	\N	\N	6	AVAILABLE	4	3
7	MNO-6543	SEAT_7	https://th.bing.com/th/id/OIP.Ho_xcLEbvubh-hyw2KXn1AHaEg?rs=1&pid=ImgDetMain	10.782622	106.680172	10	450000	2025-04-22 23:47:38.645694	2025-04-22 23:47:38.645694	\N	\N	7	AVAILABLE	5	2
8	PQR-8765	LIMOUSINE	https://th.bing.com/th/id/R.2fd0f99d11179dea5a638b74202eee8d?rik=Zrg9ZyeYQuYQiw&riu=http%3a%2f%2fwww.absolutelimos.ie%2fwp-content%2fuploads%2f2013%2f08%2fThe-16-Seater-Party-Limo-Bus-1.jpg&ehk=kUCZAbOsangNxggLwY48%2bK4PralBHTzW4iDzSkUpksU%3d&risl=&pid=ImgRaw&r=0	10.792622	106.690172	15	1200000	2025-04-22 23:47:38.645694	2025-04-22 23:47:38.645694	\N	\N	8	BUSY	2	4
9	STU-9870	BUS	https://th.bing.com/th/id/OIP.cAko7iWNwEE87_DGYA7LgQHaFj?w=234&h=180&c=7&r=0&o=5&dpr=1.3&pid=1.7	10.802622	106.700172	5	550000	2025-04-22 23:47:38.645694	2025-04-22 23:47:38.645694	\N	\N	9	AVAILABLE	3	3
\.


--
-- Name: bill_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.bill_id_seq', 126, true);


--
-- Name: booking_cars_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.booking_cars_id_seq', 76, true);


--
-- Name: booking_rooms_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.booking_rooms_id_seq', 143, true);


--
-- Name: chats_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.chats_id_seq', 81, true);


--
-- Name: cities_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.cities_id_seq', 1, false);


--
-- Name: discount_cars_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.discount_cars_id_seq', 10, true);


--
-- Name: discount_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.discount_id_seq', 10, true);


--
-- Name: drivers_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.drivers_id_seq', 10, true);


--
-- Name: facilities_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.facilities_id_seq', 22, true);


--
-- Name: notifications_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.notifications_id_seq', 133, true);


--
-- Name: payment_transaction_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.payment_transaction_id_seq', 56, true);


--
-- Name: permissions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.permissions_id_seq', 5, true);


--
-- Name: properties_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.properties_id_seq', 4, true);


--
-- Name: property_facilities_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.property_facilities_id_seq', 157, true);


--
-- Name: property_images_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.property_images_id_seq', 65, true);


--
-- Name: refund_bills_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.refund_bills_id_seq', 10, true);


--
-- Name: reviews_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.reviews_id_seq', 40, true);


--
-- Name: role_permissions_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.role_permissions_id_seq', 10, true);


--
-- Name: roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.roles_id_seq', 4, true);


--
-- Name: room_chats_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.room_chats_id_seq', 11, true);


--
-- Name: room_type_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.room_type_id_seq', 41, true);


--
-- Name: rooms_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.rooms_id_seq', 123, true);


--
-- Name: trip_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.trip_id_seq', 9, true);


--
-- Name: user_discount_cars_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_discount_cars_id_seq', 7, true);


--
-- Name: user_discounts_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_discounts_seq', 701, true);


--
-- Name: user_roles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_roles_id_seq', 55, true);


--
-- Name: user_visits_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.user_visits_id_seq', 189, true);


--
-- Name: users_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.users_id_seq', 82, true);


--
-- Name: vehicles_id_seq; Type: SEQUENCE SET; Schema: public; Owner: postgres
--

SELECT pg_catalog.setval('public.vehicles_id_seq', 10, true);


--
-- Name: bill bill_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bill
    ADD CONSTRAINT bill_pkey PRIMARY KEY (id);


--
-- Name: booking_cars booking_cars_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.booking_cars
    ADD CONSTRAINT booking_cars_pkey PRIMARY KEY (id);


--
-- Name: booking_rooms booking_rooms_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.booking_rooms
    ADD CONSTRAINT booking_rooms_pkey PRIMARY KEY (id);


--
-- Name: chats chats_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chats
    ADD CONSTRAINT chats_pkey PRIMARY KEY (id);


--
-- Name: cities cities_name_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cities
    ADD CONSTRAINT cities_name_key UNIQUE (name);


--
-- Name: cities cities_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.cities
    ADD CONSTRAINT cities_pkey PRIMARY KEY (id);


--
-- Name: discount_cars discount_cars_code_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discount_cars
    ADD CONSTRAINT discount_cars_code_key UNIQUE (code);


--
-- Name: discount_cars discount_cars_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discount_cars
    ADD CONSTRAINT discount_cars_pkey PRIMARY KEY (id);


--
-- Name: discount discount_code_key; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discount
    ADD CONSTRAINT discount_code_key UNIQUE (code);


--
-- Name: discount discount_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.discount
    ADD CONSTRAINT discount_pkey PRIMARY KEY (id);


--
-- Name: drivers drivers_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.drivers
    ADD CONSTRAINT drivers_pkey PRIMARY KEY (id);


--
-- Name: facilities facilities_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.facilities
    ADD CONSTRAINT facilities_pkey PRIMARY KEY (id);


--
-- Name: notifications notifications_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.notifications
    ADD CONSTRAINT notifications_pkey PRIMARY KEY (id);


--
-- Name: payment_transaction payment_transaction_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.payment_transaction
    ADD CONSTRAINT payment_transaction_pkey PRIMARY KEY (id);


--
-- Name: permissions permissions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.permissions
    ADD CONSTRAINT permissions_pkey PRIMARY KEY (id);


--
-- Name: properties properties_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.properties
    ADD CONSTRAINT properties_pkey PRIMARY KEY (id);


--
-- Name: property_facilities property_facilities_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.property_facilities
    ADD CONSTRAINT property_facilities_pkey PRIMARY KEY (id);


--
-- Name: property_images property_images_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.property_images
    ADD CONSTRAINT property_images_pkey PRIMARY KEY (id);


--
-- Name: refund_bills refund_bills_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.refund_bills
    ADD CONSTRAINT refund_bills_pkey PRIMARY KEY (id);


--
-- Name: reviews reviews_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reviews
    ADD CONSTRAINT reviews_pkey PRIMARY KEY (id);


--
-- Name: role_permissions role_permissions_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role_permissions
    ADD CONSTRAINT role_permissions_pkey PRIMARY KEY (id);


--
-- Name: roles roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.roles
    ADD CONSTRAINT roles_pkey PRIMARY KEY (id);


--
-- Name: room_chats room_chats_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room_chats
    ADD CONSTRAINT room_chats_pkey PRIMARY KEY (id);


--
-- Name: room_type room_type_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room_type
    ADD CONSTRAINT room_type_pkey PRIMARY KEY (id);


--
-- Name: rooms rooms_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rooms
    ADD CONSTRAINT rooms_pkey PRIMARY KEY (id);


--
-- Name: trip trip_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trip
    ADD CONSTRAINT trip_pkey PRIMARY KEY (id);


--
-- Name: trip uk6kuu1nwvxvyi4kdg6f8m43t2h; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trip
    ADD CONSTRAINT uk6kuu1nwvxvyi4kdg6f8m43t2h UNIQUE (slug);


--
-- Name: vehicles ukstdy3n8bjv14qchxmw9u8vluq; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vehicles
    ADD CONSTRAINT ukstdy3n8bjv14qchxmw9u8vluq UNIQUE (driver_id);


--
-- Name: user_discount_cars user_discount_cars_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_discount_cars
    ADD CONSTRAINT user_discount_cars_pkey PRIMARY KEY (id);


--
-- Name: user_discounts user_discounts_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_discounts
    ADD CONSTRAINT user_discounts_pkey PRIMARY KEY (id);


--
-- Name: user_roles user_roles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_pkey PRIMARY KEY (id);


--
-- Name: user_visits user_visits_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_visits
    ADD CONSTRAINT user_visits_pkey PRIMARY KEY (id);


--
-- Name: users users_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.users
    ADD CONSTRAINT users_pkey PRIMARY KEY (id);


--
-- Name: vehicles vehicles_pkey; Type: CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vehicles
    ADD CONSTRAINT vehicles_pkey PRIMARY KEY (id);


--
-- Name: bill bill_property_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.bill
    ADD CONSTRAINT bill_property_id_fkey FOREIGN KEY (property_id) REFERENCES public.properties(id);


--
-- Name: booking_cars booking_cars_bill_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.booking_cars
    ADD CONSTRAINT booking_cars_bill_id_fkey FOREIGN KEY (bill_id) REFERENCES public.bill(id);


--
-- Name: booking_cars booking_cars_vehicle_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.booking_cars
    ADD CONSTRAINT booking_cars_vehicle_id_fkey FOREIGN KEY (vehicle_id) REFERENCES public.vehicles(id);


--
-- Name: booking_rooms booking_rooms_bill_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.booking_rooms
    ADD CONSTRAINT booking_rooms_bill_id_fkey FOREIGN KEY (bill_id) REFERENCES public.bill(id);


--
-- Name: booking_rooms booking_rooms_room_type_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.booking_rooms
    ADD CONSTRAINT booking_rooms_room_type_id_fkey FOREIGN KEY (room_type_id) REFERENCES public.room_type(id);


--
-- Name: drivers drivers_vehicle_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.drivers
    ADD CONSTRAINT drivers_vehicle_id_fkey FOREIGN KEY (vehicle_id) REFERENCES public.vehicles(id);


--
-- Name: property_facilities fk36rhtx0as6f6t968iivpa0qja; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.property_facilities
    ADD CONSTRAINT fk36rhtx0as6f6t968iivpa0qja FOREIGN KEY (property_id) REFERENCES public.facilities(id);


--
-- Name: review_images fk3aayo5bjciyemf3bvvt987hkr; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.review_images
    ADD CONSTRAINT fk3aayo5bjciyemf3bvvt987hkr FOREIGN KEY (review_id) REFERENCES public.reviews(id);


--
-- Name: chat_images fk5ylie1y7p0lnpgyfd0j0es3fw; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chat_images
    ADD CONSTRAINT fk5ylie1y7p0lnpgyfd0j0es3fw FOREIGN KEY (chat_id) REFERENCES public.chats(id);


--
-- Name: num_rooms fk7me1md53nllnsgsk870sgmoxo; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.num_rooms
    ADD CONSTRAINT fk7me1md53nllnsgsk870sgmoxo FOREIGN KEY (booking_room_id) REFERENCES public.booking_rooms(id);


--
-- Name: reviews fk83j25x4ukm1vhf1336h1tt882; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.reviews
    ADD CONSTRAINT fk83j25x4ukm1vhf1336h1tt882 FOREIGN KEY (property_id) REFERENCES public.properties(id);


--
-- Name: vehicles fkaashphrwfd4ts511y8vj785ia; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.vehicles
    ADD CONSTRAINT fkaashphrwfd4ts511y8vj785ia FOREIGN KEY (driver_id) REFERENCES public.drivers(id);


--
-- Name: chats fktc45u649g3ihssymc7rhv3ea1; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.chats
    ADD CONSTRAINT fktc45u649g3ihssymc7rhv3ea1 FOREIGN KEY (room_chat_id) REFERENCES public.room_chats(id);


--
-- Name: properties properties_city_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.properties
    ADD CONSTRAINT properties_city_id_fkey FOREIGN KEY (city_id) REFERENCES public.cities(id) ON DELETE CASCADE;


--
-- Name: property_facilities property_facilities_facility_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.property_facilities
    ADD CONSTRAINT property_facilities_facility_id_fkey FOREIGN KEY (facility_id) REFERENCES public.facilities(id);


--
-- Name: property_facilities property_facilities_property_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.property_facilities
    ADD CONSTRAINT property_facilities_property_id_fkey FOREIGN KEY (property_id) REFERENCES public.properties(id);


--
-- Name: property_images property_images_property_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.property_images
    ADD CONSTRAINT property_images_property_id_fkey FOREIGN KEY (property_id) REFERENCES public.properties(id);


--
-- Name: role_permissions role_permissions_permission_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role_permissions
    ADD CONSTRAINT role_permissions_permission_id_fkey FOREIGN KEY (permission_id) REFERENCES public.permissions(id);


--
-- Name: role_permissions role_permissions_role_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.role_permissions
    ADD CONSTRAINT role_permissions_role_id_fkey FOREIGN KEY (role_id) REFERENCES public.roles(id);


--
-- Name: room_type room_type_property_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.room_type
    ADD CONSTRAINT room_type_property_id_fkey FOREIGN KEY (property_id) REFERENCES public.properties(id);


--
-- Name: rooms rooms_property_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rooms
    ADD CONSTRAINT rooms_property_id_fkey FOREIGN KEY (property_id) REFERENCES public.properties(id);


--
-- Name: rooms rooms_room_type_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.rooms
    ADD CONSTRAINT rooms_room_type_id_fkey FOREIGN KEY (room_type_id) REFERENCES public.room_type(id);


--
-- Name: trip trip_city_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.trip
    ADD CONSTRAINT trip_city_id_fkey FOREIGN KEY (city_id) REFERENCES public.cities(id) ON DELETE CASCADE;


--
-- Name: user_roles user_roles_role_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_role_id_fkey FOREIGN KEY (role_id) REFERENCES public.roles(id);


--
-- Name: user_roles user_roles_user_id_fkey; Type: FK CONSTRAINT; Schema: public; Owner: postgres
--

ALTER TABLE ONLY public.user_roles
    ADD CONSTRAINT user_roles_user_id_fkey FOREIGN KEY (user_id) REFERENCES public.users(id);


--
-- PostgreSQL database dump complete
--

