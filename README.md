# News Aggregator with JavaEE Back-End and Vue.JS Front-End

This project is a geolocation-based news aggregator composed of:

- A JavaEE back-end deployed on the Wildfly application server.
- A VueJS TypeScript front-end.

The back-end employs several noteworthy technologies including:

- **Context Dependency Injection (CDI):** CDI enhances flexibility, reusability, and manageability by providing a component's dependencies from external sources.
- **Object-Relational Mapping (ORM):** ORM facilitates integration between object-oriented programming systems and relational database management systems (RDBMS).
- **Server-Side Cache:** Stores frequently accessed data to speed up application performance by reducing the need for repeated data retrieval or computation.
- **REST with JSON Web Token (JWT):** Utilized for secure communication between the front-end and back-end.

The front-end is developed using Vue.js (Version 3) and incorporates elements of Material Design. Key technologies utilized in the front-end include:

- **State Management with Vuex:** Vuex is employed for efficient state management within the application.
- **Responsive Design:** Ensures optimal user experience across various devices and screen sizes.
- **Asynchronous Design with Axios and Promises:** Enables non-blocking, asynchronous requests to the back-end server for enhanced performance.

For styling, the CSS framework Tailwind is utilized.

## Demonstration video
[![Front end video](https://github.com/a-giorgi/NewsAggregator/blob/84efe8a0fabc95fe857e17da511b30b4b7d1a2a5/images/preview.png)](https://www.youtube.com/watch?v=9o_whrqZ7xM)

## Installation Instructions

### Back-end

1. Install Docker.
2. Go to the directory where the `docker-compose.yml` file is located.
3. Run the command `docker-compose up -d` to start the backend services in detached mode.
4. Copy the `backend.war` file into the directory `./workdir/deploy/wildfly`. This file is generated in the same directory as `docker-compose.yml`.
5. Wait until you see a file named `backend.war.deployed` appear in the directory `./workdir/deploy/wildfly`.
6. The backend will be available at: [http://localhost:8080/backend/rest/](http://localhost:8080/backend/rest/)

### Front-end

1. Install Node.js.
2. Navigate to the frontend directory.
3. Run the following commands in sequence:
    ```bash
    npm install
    npm run build
    npm run serve
    ```
4. The frontend will be available at: [http://localhost:3000](http://localhost:3000)
