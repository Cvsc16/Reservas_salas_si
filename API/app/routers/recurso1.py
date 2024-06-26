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

@router.post("/api/recurso1/", response_model=schemas.Professor)
def create_professor(professor: schemas.ProfessorCreate, db: Session = Depends(get_db)):
    db_professor = crud.get_professor_by_email(db, email=professor.email)
    if db_professor:
        raise HTTPException(status_code=400, detail="Email already registered")
    return crud.create_professor(db=db, professor=professor)

@router.get("/api/recurso1/{professor_id}", response_model=schemas.Professor)
def read_professor(professor_id: int, db: Session = Depends(get_db)):
    db_professor = crud.get_professor(db, professor_id=professor_id)
    if db_professor is None:
        raise HTTPException(status_code=404, detail="Professor not found")
    return db_professor
