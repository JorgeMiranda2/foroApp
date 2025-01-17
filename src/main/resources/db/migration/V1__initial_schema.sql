-- Tabla Profile
CREATE TABLE profile (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL
);

-- Tabla "user" (en comillas dobles para evitar conflicto con palabra reservada)
CREATE TABLE "user" (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(45) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE,
    password VARCHAR(255) NOT NULL,
    profile_id BIGINT NOT NULL,
    CONSTRAINT fk_user_profile FOREIGN KEY (profile_id) REFERENCES profile(id)
);

-- Índice adicional para email (ya es único, pero por claridad)
CREATE INDEX idx_user_email ON "user"(email);

-- Tabla User_Profile (relación ManyToMany)
CREATE TABLE user_profile (
    user_id BIGINT NOT NULL,
    profile_id BIGINT NOT NULL,
    PRIMARY KEY (user_id, profile_id),
    CONSTRAINT fk_user_profile_user FOREIGN KEY (user_id) REFERENCES "user"(id),
    CONSTRAINT fk_user_profile_profile FOREIGN KEY (profile_id) REFERENCES profile(id)
);

-- Tabla Course
CREATE TABLE course (
    id BIGSERIAL PRIMARY KEY,
    name VARCHAR(255) NOT NULL,
    category VARCHAR(255) NOT NULL
);

-- Tabla Topic
CREATE TABLE topic (
    id BIGSERIAL PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    message TEXT NOT NULL,
    creation_date DATE NOT NULL,
    status BOOLEAN NOT NULL,
    course_id BIGINT NOT NULL,
    user_id BIGINT NOT NULL,
    CONSTRAINT fk_topic_course FOREIGN KEY (course_id) REFERENCES course(id),
    CONSTRAINT fk_topic_user FOREIGN KEY (user_id) REFERENCES "user"(id)
);

-- Tabla Response
CREATE TABLE response (
    id BIGSERIAL PRIMARY KEY,
    message TEXT NOT NULL,
    creation_date DATE DEFAULT CURRENT_DATE,
    solution TEXT NOT NULL,
    user_id BIGINT NOT NULL,
    topic_id BIGINT NOT NULL,
    CONSTRAINT fk_response_user FOREIGN KEY (user_id) REFERENCES "user"(id),
    CONSTRAINT fk_response_topic FOREIGN KEY (topic_id) REFERENCES topic(id)
);

-- Índices para claves foráneas (opcional, mejora consultas)
CREATE INDEX idx_topic_course_id ON topic(course_id);
CREATE INDEX idx_topic_user_id ON topic(user_id);
CREATE INDEX idx_response_user_id ON response(user_id);
CREATE INDEX idx_response_topic_id ON response(topic_id);
