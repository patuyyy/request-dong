CREATE TYPE "status" AS ENUM (
  'pending',
  'onprogress',
  'finish',
  'rejected'
);

CREATE TABLE "events" (
  "event_id" serial PRIMARY KEY,
  "name" varchar UNIQUE,
  "description" text,
  "time" date
);

CREATE TABLE "users" (
  "user_id" serial PRIMARY KEY,
  "username" varchar UNIQUE,
  "password" varchar,
  "name" varchar
);

CREATE TABLE "staff_acara" (
  "p_acara_id" serial PRIMARY KEY,
  "user_id" bigint,
  "event_id" bigint
);

CREATE TABLE "staff_operasional" (
  "p_ops_id" serial PRIMARY KEY,
  "user_id" bigint,
  "event_id" bigint
);

CREATE TABLE "requests" (
  "request_id" serial PRIMARY KEY,
  "request_by" bigint,
  "taken_by" bigint,
  "event" bigint,
  "requested_thing" varchar,
  "amount" integer,
  "deadline" date,
  "status" status DEFAULT 'pending'
);

CREATE TABLE "events_pivot" (
  "ep_id" serial PRIMARY KEY,
  "user_id" bigint,
  "event_id" bigint
);

CREATE TABLE "session" (
  "sid" varchar NOT NULL COLLATE "default",
  "sess" json NOT NULL,
  "expire" timestamp(6) NOT NULL
)
WITH (OIDS=FALSE);


ALTER TABLE "staff_acara" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("user_id") ON DELETE CASCADE;

ALTER TABLE "staff_acara" ADD FOREIGN KEY ("event_id") REFERENCES "events" ("event_id") ON DELETE CASCADE;

ALTER TABLE "staff_operasional" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("user_id") ON DELETE CASCADE;

ALTER TABLE "staff_operasional" ADD FOREIGN KEY ("event_id") REFERENCES "events" ("event_id") ON DELETE CASCADE;

ALTER TABLE "requests" ADD FOREIGN KEY ("request_by") REFERENCES "staff_acara" ("p_acara_id") ON DELETE CASCADE;

ALTER TABLE "requests" ADD FOREIGN KEY ("taken_by") REFERENCES "staff_operasional" ("p_ops_id") ON DELETE CASCADE;

ALTER TABLE "requests" ADD FOREIGN KEY ("event") REFERENCES "events" ("event_id") ON DELETE CASCADE;

ALTER TABLE "requests" ALTER COLUMN status SET DEFAULT "pending";

ALTER TABLE "events_pivot" ADD FOREIGN KEY ("user_id") REFERENCES "users" ("user_id") ON DELETE CASCADE;

ALTER TABLE "events_pivot" ADD FOREIGN KEY ("event_id") REFERENCES "events" ("event_id") ON DELETE CASCADE;

ALTER TABLE "session" ADD CONSTRAINT "session_pkey" PRIMARY KEY ("sid") NOT DEFERRABLE INITIALLY IMMEDIATE;

CREATE INDEX "IDX_session_expire" ON "session" ("expire");
