from fastapi import APIRouter, Depends
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

@router.get("/api/recurso2/", response_model=list[schemas.Professor])
def read_professors(skip: int = 0, limit: int = 10, db: Session = Depends(get_db)):
    professors = crud.get_all_professors(db, skip=skip, limit=limit)
    return professors
