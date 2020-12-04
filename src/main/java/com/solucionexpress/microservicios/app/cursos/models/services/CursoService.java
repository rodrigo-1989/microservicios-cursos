package com.solucionexpress.microservicios.app.cursos.models.services;



import com.solucionexpress.microservicios.app.cursos.models.entity.Curso;
import com.solucionexpress.microservicios.commons.alumnos.models.entity.Alumno;
import com.solucionexpress.microservicios.commons.services.CommonService;


public interface CursoService extends CommonService<Curso>{

	public Curso findCursoByAlumnoId(Long id);
    
    
}
