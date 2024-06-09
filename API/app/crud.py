from datetime import date
from fastapi import HTTPException
from sqlalchemy.orm import Session
from . import models, schemas

def get_professor(db: Session, professor_id: int):
    return db.query(models.Professor).filter(models.Professor.id == professor_id).first()

def get_professor_by_email(db: Session, email: str):
    return db.query(models.Professor).filter(models.Professor.email == email).first()

def get_all_professors(db: Session, skip: int = 0, limit: int = 10):
    return db.query(models.Professor).offset(skip).limit(limit).all()

def create_professor(db: Session, professor: schemas.ProfessorCreate):
    db_professor = models.Professor(**professor.dict())
    db.add(db_professor)
    db.commit()
    db.refresh(db_professor)
    return db_professor

def update_professor(db: Session, professor_id: int, professor: schemas.ProfessorCreate):
    db_professor = db.query(models.Professor).filter(models.Professor.id == professor_id).first()
    if db_professor:
        for key, value in professor.dict().items():
            setattr(db_professor, key, value)
        db.commit()
        db.refresh(db_professor)
    return db_professor

def delete_professor(db: Session, professor_id: int):
    db_professor = db.query(models.Professor).filter(models.Professor.id == professor_id).first()
    if db_professor:
        db.delete(db_professor)
        db.commit()
    return db_professor

def get_sala(db: Session, sala_id: int):
    return db.query(models.Sala).filter(models.Sala.id == sala_id).first()

def get_all_salas(db: Session, skip: int = 0, limit: int = 10):
    return db.query(models.Sala).offset(skip).limit(limit).all()

def create_sala(db: Session, sala: schemas.SalaCreate):
    db_sala = models.Sala(**sala.dict())
    db.add(db_sala)
    db.commit()
    db.refresh(db_sala)
    return db_sala

def update_sala(db: Session, sala_id: int, sala: schemas.SalaCreate):
    db_sala = db.query(models.Sala).filter(models.Sala.id == sala_id).first()
    if db_sala:
        for key, value in sala.dict().items():
            setattr(db_sala, key, value)
        db.commit()
        db.refresh(db_sala)
    return db_sala

def delete_sala(db: Session, sala_id: int):
    db_sala = db.query(models.Sala).filter(models.Sala.id == sala_id).first()
    if db_sala:
        db.delete(db_sala)
        db.commit()
    return db_sala

def get_reserva(db: Session, reserva_id: int):
    return db.query(models.Reserva).filter(models.Reserva.id == reserva_id).first()


def get_all_reservas(db: Session, skip: int = 0, limit: int = 10):
    return db.query(models.Reserva).offset(skip).limit(limit).all()

def get_reservas_by_date(db: Session, data_reserva: str):
    return db.query(models.Reserva).filter(models.Reserva.data_reserva == data_reserva).all()

def create_reserva(db: Session, reserva: schemas.ReservaCreate):
    professor = get_professor(db, reserva.professor_id)
    sala = get_sala(db, reserva.sala_id)
    if professor is None:
        raise HTTPException(status_code=400, detail="Professor not found")
    if sala is None:
        raise HTTPException(status_code=400, detail="Sala not found")

    db_reserva = models.Reserva(
        professor_id=reserva.professor_id,
        sala_id=reserva.sala_id,
        data_reserva=reserva.data_reserva,
        nome_professor=professor.nome,
        nome_sala=sala.nome
    )
    db.add(db_reserva)
    db.commit()
    db.refresh(db_reserva)
    return db_reserva

def update_reserva(db: Session, reserva_id: int, reserva: schemas.ReservaCreate):
    db_reserva = db.query(models.Reserva).filter(models.Reserva.id == reserva_id).first()
    if db_reserva:
        professor = get_professor(db, reserva.professor_id)
        sala = get_sala(db, reserva.sala_id)
        if professor is None:
            raise HTTPException(status_code=400, detail="Professor not found")
        if sala is None:
            raise HTTPException(status_code=400, detail="Sala not found")

        db_reserva.professor_id = reserva.professor_id
        db_reserva.sala_id = reserva.sala_id
        db_reserva.data_reserva = reserva.data_reserva
        db_reserva.nome_professor = professor.nome
        db_reserva.nome_sala = sala.nome
        db.commit()
        db.refresh(db_reserva)
    return db_reserva

def delete_reserva(db: Session, reserva_id: int):
    db_reserva = db.query(models.Reserva).filter(models.Reserva.id == reserva_id).first()
    if db_reserva:
        db.delete(db_reserva)
        db.commit()
    return db_reserva

def check_professor_exists(db: Session, professor_id: int):
    return db.query(models.Professor).filter(models.Professor.id == professor_id).first() is not None

def check_sala_exists(db: Session, sala_id: int):
    return db.query(models.Sala).filter(models.Sala.id == sala_id).first() is not None
