from datetime import datetime
from fastapi import APIRouter, Depends, HTTPException, Query
from sqlalchemy.orm import Session
from typing import List
from .. import crud, schemas
from ..database import SessionLocal

router = APIRouter()

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@router.post("/api/recurso5/", response_model=schemas.Reserva)
def create_reserva(reserva: schemas.ReservaCreate, db: Session = Depends(get_db)):
    return crud.create_reserva(db=db, reserva=reserva)

@router.get("/api/recurso5/all", response_model=List[schemas.Reserva])
def read_reservas(skip: int = Query(0, ge=0), limit: int = Query(10, ge=1), db: Session = Depends(get_db)):
    reservas = crud.get_all_reservas(db, skip=skip, limit=limit)
    return reservas

@router.get("/api/recurso5/{reserva_id}", response_model=schemas.Reserva)
def read_reserva(reserva_id: int, db: Session = Depends(get_db)):
    db_reserva = crud.get_reserva(db, reserva_id=reserva_id)
    if db_reserva is None:
        raise HTTPException(status_code=404, detail="Reserva not found")
    return db_reserva

@router.put("/api/recurso5/{reserva_id}", response_model=schemas.Reserva)
def update_reserva(reserva_id: int, reserva: schemas.ReservaCreate, db: Session = Depends(get_db)):
    db_reserva = crud.update_reserva(db, reserva_id=reserva_id, reserva=reserva)
    if db_reserva is None:
        raise HTTPException(status_code=404, detail="Reserva not found")
    return db_reserva

@router.delete("/api/recurso5/{reserva_id}", response_model=schemas.Reserva)
def delete_reserva(reserva_id: int, db: Session = Depends(get_db)):
    db_reserva = crud.delete_reserva(db, reserva_id=reserva_id)
    if db_reserva is None:
        raise HTTPException(status_code=404, detail="Reserva not found")
    return db_reserva

def validate_date(date_text: str):
    try:
        datetime.strptime(date_text, "%Y-%m-%d")
    except ValueError:
        raise HTTPException(status_code=400, detail="Invalid date format. Expected format: yyyy-MM-dd")

@router.get("/api/recurso5/", response_model=List[schemas.Reserva])
def get_reservas_by_date(data_reserva: str = Query(...), db: Session = Depends(get_db)):
    validate_date(data_reserva)
    reservas = crud.get_reservas_by_date(db, data_reserva)
    if not reservas:
        raise HTTPException(status_code=404, detail="No reservations found for this date")
    return reservas
