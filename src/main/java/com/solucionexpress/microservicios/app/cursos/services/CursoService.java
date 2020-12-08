package com.solucionexpress.microservicios.app.cursos.services;



import com.solucionexpress.microservicios.app.cursos.models.entity.Curso;
import com.solucionexpress.microservicios.commons.alumnos.models.entity.Alumno;
import com.solucionexpress.microservicios.commons.services.CommonService;
import org.springframework.web.bind.annotation.PathVariable;


public interface CursoService extends CommonService<Curso>{

	public Curso findCursoByAlumnoId(Long id);
	public Iterable<Long>  obtenerExamenesIdsConRespuestasAlumno(Long alumnoId);

}
