package com.solucionexpress.microservicios.app.cursos.controllers;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.solucionexpress.microservicios.app.cursos.models.entity.Curso;
import com.solucionexpress.microservicios.app.cursos.services.CursoService;
import com.solucionexpress.microservicios.commons.alumnos.models.entity.Alumno;
import com.solucionexpress.microservicios.commons.controllers.CommonController;
import com.solucionexpress.microservicios.commons.examenes.models.entity.Examen;

import javax.validation.Valid;

@RestController
public class CursoController extends CommonController <Curso,CursoService> {
	
	@PutMapping("/{id}")
	public ResponseEntity<?> editar (@Valid @RequestBody Curso curso, BindingResult result, @PathVariable Long id){
		if (result.hasErrors() ){
			return this.validar(result);
		}
		Optional<Curso> o = this.service.findById(id);
		if ( !o.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Curso dbCurso = o.get();
		dbCurso.setNombre(curso.getNombre());
		
		return ResponseEntity.status(HttpStatus.CREATED).body( this.service.save(dbCurso));
	}
	
	@PutMapping("{id}/asignar-alumnos")
	public ResponseEntity<?>aignarAlumnos(@RequestBody List<Alumno> alumnos,@PathVariable Long id){
		Optional<Curso> o = this.service.findById(id);
		if ( !o.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Curso dbCurso = o.get();
		alumnos.forEach(a->{
			dbCurso.addAlumno(a);
		});
		return ResponseEntity.status(HttpStatus.CREATED).body( this.service.save(dbCurso));
	}

	@PutMapping("{id}/eliminar-alumno")
	public ResponseEntity<?>eliminarAlumnos(@RequestBody Alumno alumno,@PathVariable Long id){
		Optional<Curso> o = this.service.findById(id);
		if ( !o.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Curso dbCurso = o.get();
		dbCurso.removeAlumno(alumno);
		return ResponseEntity.status(HttpStatus.CREATED).body( this.service.save(dbCurso));
	} 
	
	@GetMapping("/alumno/{id}")
	public ResponseEntity <?> buscarPorAlumnoId(@PathVariable Long id){
		Curso curso = service.findCursoByAlumnoId(id);

		if (curso != null){
			List<Long> examenesIds = (List<Long>) service.obtenerExamenesIdsConRespuestasAlumno(id);

			List<Examen> examenes = curso.getExamenes().stream().map( examen ->{
				if (examenesIds.contains(examen.getId())){
					examen.setRespondido(true);
				}
				return examen;
			}).collect(Collectors.toList());
			curso.setExamenes(examenes);
		}
		
		return ResponseEntity.ok( curso );
	}
	
	@PutMapping("{id}/asignar-examenes")
	public ResponseEntity<?>aignarExamenes(@RequestBody List<Examen> examenes,@PathVariable Long id){
		Optional<Curso> o = this.service.findById(id);
		if ( !o.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Curso dbCurso = o.get();
		examenes.forEach( dbCurso::addExamen );
		return ResponseEntity.status(HttpStatus.CREATED).body( this.service.save(dbCurso));
	}

	@PutMapping("{id}/eliminar-examen")
	public ResponseEntity<?>eliminarExamen(@RequestBody Examen examen,@PathVariable Long id){
		Optional<Curso> o = this.service.findById(id);
		if ( !o.isPresent()) {
			return ResponseEntity.notFound().build();
		}
		Curso dbCurso = o.get();
		dbCurso.removeExamen(examen);
		return ResponseEntity.status(HttpStatus.CREATED).body( this.service.save(dbCurso));
	} 
}
