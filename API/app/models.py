from sqlalchemy import Column, Integer, String, ForeignKey, Date, Time
from sqlalchemy.orm import relationship
from .database import Base

class Professor(Base):
    __tablename__ = "professores"
    
    id = Column(Integer, primary_key=True, index=True)
    nome = Column(String, index=True)
    email = Column(String, unique=True, index=True)

class Sala(Base):
    __tablename__ = "salas"
    
    id = Column(Integer, primary_key=True, index=True)
    nome = Column(String, index=True)
    capacidade = Column(Integer)

class Reserva(Base):
    __tablename__ = "reservas"
    
    id = Column(Integer, primary_key=True, index=True)
    professor_id = Column(Integer, ForeignKey("professores.id"))
    sala_id = Column(Integer, ForeignKey("salas.id"))
    data_reserva = Column(Date, index=True)
    nome_professor = Column(String)
    nome_sala = Column(String)

    professor = relationship("Professor")
    sala = relationship("Sala")
