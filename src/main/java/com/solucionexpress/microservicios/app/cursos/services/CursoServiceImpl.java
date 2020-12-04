package com.solucionexpress.microservicios.app.cursos.services;

import com.solucionexpress.microservicios.app.cursos.models.entity.Curso;
import com.solucionexpress.microservicios.app.cursos.models.repository.CursoRepository;
import org.springframework.stereotype.Service;

@Service
public class CursoServiceImpl extends CommonServiceImpl <Curso, CursoRepository> implements CursoService{

}
