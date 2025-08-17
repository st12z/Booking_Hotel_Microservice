INSERT INTO public.cities (
    id,
    name,
    image,
    created_at,
    created_by,
    updated_at,
    updated_by,
    slug,
    latitude_center,
    longitude_center,
    geog
) VALUES (
             1,
             'Hometown',
             'hometown.jpg',
             CURRENT_TIMESTAMP,
             'admin',
             CURRENT_TIMESTAMP,
             'admin',
             'hometown',
             10.762622,   -- ví dụ latitude
             106.660172,  -- ví dụ longitude
             ST_GeogFromText('SRID=4326;POINT(106.660172 10.762622)')  -- tạo geography point
         );
INSERT INTO public.properties (
    id,
    name,
    city_id,
    property_type,
    rating_star,
    address,
    latitude,
    longitude,
    overview,
    avg_review_score,
    created_at,
    updated_at,
    created_by,
    updated_by,
    slug,
    geog,
    distance_from_center,
    distance_from_trip
) VALUES (
             1,
             'Sunset Villa',
             1, -- giả sử city_id = 101
             'Villa',
             5,
             '123 Main Street, Hometown',
             10.762622,  -- ví dụ latitude
             106.660172, -- ví dụ longitude
             'A beautiful villa with pool and garden',
             4.8,
             CURRENT_TIMESTAMP,
             CURRENT_TIMESTAMP,
             'admin',
             'admin',
             'sunset-villa',
             ST_GeogFromText('SRID=4326;POINT(106.660172 10.762622)'), -- tạo geography point
             2.5, -- distance_from_center
             0.8  -- distance_from_trip
         );
INSERT INTO public.room_type (
    id,
    property_id,
    name,
    price,
    max_guests,
    num_beds,
    created_at,
    area,
    discount,
    created_by,
    updated_at,
    updated_by,
    status
) VALUES
      (
          1,
          1,
          'Deluxe Room',
          120,
          2,
          1,
          CURRENT_TIMESTAMP,
          30,
          10,
          'admin',
          CURRENT_TIMESTAMP,
          'admin',
          true
      );