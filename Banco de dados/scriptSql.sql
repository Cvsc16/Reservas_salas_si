CREATE TABLE Professores (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    email VARCHAR(100) NOT NULL UNIQUE
);

CREATE TABLE Salas (
    id SERIAL PRIMARY KEY,
    nome VARCHAR(100) NOT NULL,
    capacidade INT NOT NULL
);

CREATE TABLE Reservas (
    id SERIAL PRIMARY KEY,
    professor_id INT NOT NULL,
    sala_id INT NOT NULL,
    data_reserva DATE NOT NULL,
    CONSTRAINT fk_professor
        FOREIGN KEY(professor_id) 
        REFERENCES Professores(id),
    CONSTRAINT fk_sala
        FOREIGN KEY(sala_id)
        REFERENCES Salas(id)
);
