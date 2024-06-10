# RequestDong - Request Management System for Events

RequestDong adalah sebuah aplikasi yang berguna untuk melakukan manajemen terhadap request barang pada suatu event. Aplikasi ini memiliki fitur seperti user authentication, pembuatan request, perlakuan terhadap request yang ada seperti membuat request, menerima request, menolak request, dan menyelesaikan request. Pada aplikasi ini juga dapat melakukan pendaftaran sepagai panitia acara ataupun operasional yang memiliki tugas yang berbeda pada tiap event.

## API Documentation
https://documenter.postman.com/preview/31050359-2fd878eb-f423-4e7e-9406-addfbe2a81a0?environment=&versionTag=latest&apiName=CURRENT&version=latest&documentationLayout=classic-double-column&documentationTheme=light&logo=https%3A%2F%2Fres.cloudinary.com%2Fpostman%2Fimage%2Fupload%2Ft_team_logo%2Fv1%2Fteam%2Fanonymous_team&logoDark=https%3A%2F%2Fres.cloudinary.com%2Fpostman%2Fimage%2Fupload%2Ft_team_logo%2Fv1%2Fteam%2Fanonymous_team&right-sidebar=303030&top-bar=FFFFFF&highlight=FF6C37&right-sidebar-dark=303030&top-bar-dark=212121&highlight-dark=FF6C37

## Table Structure

The app utilizes a PostgreSQL database with the following tables:

### Users

The `Users` table stores information about regular users.

| Column     | Data Type         | Description                         |
|------------|------------------|-------------------------------------|
| user_id    | serial           | Primary key                         |
| username  | varchar(255)     | Username of the user               |
| password   | varchar(255)     | User's password                     |
| name    | varchar(255)      | User's real name                    |

### Events

The `Events` table stores information about all events avaible.

| Column        | Data Type        | Description                              |
|---------------|------------------|------------------------------------------|
| event_id      | serial           | Primary key                              |
| name          | varchar(255)     | Event name                               |
| description   | text             | Event's description                      |
| time          | date             | When the event is held                   |


### Staff_acara

The `Staff_acara` table contains details about users who's registered as staff acara.

| Column        | Data Type        | Description                              |
|---------------|------------------|------------------------------------------|
| p_acara_id    | serial           | Primary key                              |
| user_id       | bigint           | User_id                                  |
| event_id      | bigint           | Event_id                                 |


### Staff_operasional

The `Staff_operasional` table contains details about users who's registered as staff operasional.

| Column        | Data Type        | Description                              |
|---------------|------------------|------------------------------------------|
| p_ops_id      | serial           | Primary key                              |
| user_id       | bigint           | User_id                                  |
| event_id      | bigint           | Event_id                                 |

### Requests

The `User_Prefs` table contains user preferences for job categories.

| Column           | Data Type    | Description                               |
|------------------|--------------|-------------------------------------------|
| request_id       | serial       | Primary key                               |
| request_by       | bigint       | Foreign key referencing Users table       |
| taken_by         | bigint       | Preferred job category for the user       |
| event            | bigint       | Preferred job category for the user       |
| requested_thing  | text         | Preferred job category for the user       |
| amount           | int          | Preferred job category for the user       |
| deadline         | date         | Preferred job category for the user       |
| status           | status       | Preferred job category for the user       |


