package com.solucionexpress.microservicios.app.cursos.models.services;


import com.solucionexpress.microservicios.app.cursos.models.entity.Curso;
import com.solucionexpress.microservicios.app.cursos.models.repository.CursoRepository;
import com.solucionexpress.microservicios.commons.services.CommonServiceImpl;


import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class CursoServiceImpl extends CommonServiceImpl<Curso,CursoRepository> implements CursoService{

	@Override
	@Transactional(readOnly = true)
	public Curso findCursoByAlumnoId(Long id) {
		return repository.findCursoByAlumnoId( id );
	}


}
 