CREATE TABLE people
(
    id text PRIMARY KEY DEFAULT gen_random_uuid()::text,
    name text not null,
    date_of_birth date not null
);
