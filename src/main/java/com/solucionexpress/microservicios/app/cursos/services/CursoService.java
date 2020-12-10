package com.solucionexpress.microservicios.app.cursos.services;



import com.solucionexpress.microservicios.app.cursos.models.entity.Curso;
import com.solucionexpress.microservicios.commons.alumnos.models.entity.Alumno;
import com.solucionexpress.microservicios.commons.services.CommonService;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;


public interface CursoService extends CommonService<Curso>{

	public Curso findCursoByAlumnoId(Long id);
	public Iterable<Long>  obtenerExamenesIdsConRespuestasAlumno(Long alumnoId);
	
	public Iterable<Alumno> obtenerAlumnosPorCurso (Iterable<Long> ids);
	
	public void eliminarCursoAlumnoPorId(Long id);

}
