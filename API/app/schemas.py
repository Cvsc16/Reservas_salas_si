from pydantic import BaseModel
from datetime import date, time

class ProfessorBase(BaseModel):
    nome: str
    email: str

class ProfessorCreate(ProfessorBase):
    pass

class Professor(ProfessorBase):
    id: int

    class Config:
        from_attributes = True

class SalaBase(BaseModel):
    nome: str
    capacidade: int

class SalaCreate(SalaBase):
    pass

class Sala(SalaBase):
    id: int

    class Config:
        from_attributes = True

class ReservaBase(BaseModel):
    professor_id: int
    sala_id: int
    data_reserva: date

class ReservaCreate(ReservaBase):
    pass

class Reserva(ReservaBase):
    id: int
    nome_professor: str
    nome_sala: str

    class Config:
        from_attributes = True
