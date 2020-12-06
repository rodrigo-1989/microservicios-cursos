package com.solucionexpress.microservicios.app.cursos.models.repository;

import com.solucionexpress.microservicios.app.cursos.models.entity.Curso;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CursoRepository extends PagingAndSortingRepository<Curso,Long> {
	
	@Query("select c from Curso c join fetch c.alumnos a where a.id=?1")
	public Curso findCursoByAlumnoId(Long id);
}
