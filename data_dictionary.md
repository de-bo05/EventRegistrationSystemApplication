# Data Dictionary -- Event Registration System

## users

-   id (INT, PK)
-   name (VARCHAR)
-   email (VARCHAR, UNIQUE)
-   password (VARCHAR)
-   role (VARCHAR)

## events

-   id (INT, PK)
-   title (VARCHAR)
-   description (TEXT)
-   date (DATE)
-   location (VARCHAR)

## registrations

-   id (INT, PK)
-   user_id (INT, FK)
-   event_id (INT, FK)
-   qr_code (VARCHAR)
-   status (VARCHAR)
