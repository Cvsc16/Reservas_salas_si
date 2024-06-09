from fastapi import FastAPI
from .routers import recurso1, recurso2, recurso3, recurso4, recurso5

app = FastAPI()

app.include_router(recurso1.router)
app.include_router(recurso2.router)
app.include_router(recurso3.router)
app.include_router(recurso4.router)
app.include_router(recurso5.router)
