package com.solucionexpress.microservicios.app.cursos.services;


import com.solucionexpress.microservicios.app.cursos.clients.RespuestaFeignClient;
import com.solucionexpress.microservicios.app.cursos.models.entity.Curso;
import com.solucionexpress.microservicios.app.cursos.models.repository.CursoRepository;
import com.solucionexpress.microservicios.app.cursos.services.CursoService;
import com.solucionexpress.microservicios.commons.services.CommonServiceImpl;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;


@Service
public class CursoServiceImpl extends CommonServiceImpl<Curso,CursoRepository> implements CursoService {
	@Autowired
	private RespuestaFeignClient client;

	@Override
	@Transactional(readOnly = true)
	public Curso findCursoByAlumnoId(Long id) {
		return repository.findCursoByAlumnoId( id );
	}

	@Override
	public Iterable<Long>  obtenerExamenesIdsConRespuestasAlumno(Long alumnoId){
		return client.obtenerExamenesIdsConRespuestasAlumno(alumnoId);
	}


}
 