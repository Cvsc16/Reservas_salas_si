from fastapi import APIRouter, Depends, HTTPException
from sqlalchemy.orm import Session
from .. import crud, schemas
from ..database import SessionLocal

router = APIRouter()

def get_db():
    db = SessionLocal()
    try:
        yield db
    finally:
        db.close()

@router.post("/api/recurso4/", response_model=schemas.Sala)
def create_sala(sala: schemas.SalaCreate, db: Session = Depends(get_db)):
    return crud.create_sala(db=db, sala=sala)

@router.get("/api/recurso4/{sala_id}", response_model=schemas.Sala)
def read_sala(sala_id: int, db: Session = Depends(get_db)):
    db_sala = crud.get_sala(db, sala_id=sala_id)
    if db_sala is None:
        raise HTTPException(status_code=404, detail="Sala not found")
    return db_sala

@router.get("/api/recurso4/", response_model=list[schemas.Sala])
def read_salas(skip: int = 0, limit: int = 10, db: Session = Depends(get_db)):
    salas = crud.get_all_salas(db, skip=skip, limit=limit)
    return salas

@router.put("/api/recurso4/{sala_id}", response_model=schemas.Sala)
def update_sala(sala_id: int, sala: schemas.SalaCreate, db: Session = Depends(get_db)):
    db_sala = crud.update_sala(db, sala_id=sala_id, sala=sala)
    if db_sala is None:
        raise HTTPException(status_code=404, detail="Sala not found")
    return db_sala

@router.delete("/api/recurso4/{sala_id}", response_model=schemas.Sala)
def delete_sala(sala_id: int, db: Session = Depends(get_db)):
    db_sala = crud.delete_sala(db, sala_id=sala_id)
    if db_sala is None:
        raise HTTPException(status_code=404, detail="Sala not found")
    return db_sala
