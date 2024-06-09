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

@router.put("/api/recurso3/{professor_id}", response_model=schemas.Professor)
def update_professor(professor_id: int, professor: schemas.ProfessorCreate, db: Session = Depends(get_db)):
    db_professor = crud.update_professor(db, professor_id=professor_id, professor=professor)
    if db_professor is None:
        raise HTTPException(status_code=404, detail="Professor not found")
    return db_professor

@router.delete("/api/recurso3/{professor_id}", response_model=schemas.Professor)
def delete_professor(professor_id: int, db: Session = Depends(get_db)):
    db_professor = crud.delete_professor(db, professor_id=professor_id)
    if db_professor is None:
        raise HTTPException(status_code=404, detail="Professor not found")
    return db_professor
