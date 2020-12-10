package com.solucionexpress.microservicios.app.cursos.controllers;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.solucionexpress.microservicios.app.cursos.models.entity.Curso;
import com.solucionexpress.microservicios.app.cursos.models.entity.CursoAlumno;
import com.solucionexpress.microservicios.app.cursos.services.CursoService;
import com.solucionexpress.microservicios.commons.alumnos.models.entity.Alumno;
import com.solucionexpress.microservicios.commons.controllers.CommonController;
import com.solucionexpress.microservicios.commons.examenes.models.entity.Examen;

import javax.validation.Valid;

@RestController
public class CursoController extends CommonController <Curso,CursoService> {
	
	@Value("${config.balanceador.test}")
	private String balanceadorTest;
	
	@DeleteMapping("/eliminar-alumno/{id}")
	public ResponseEntity<?> eliminarCursoAlumnoPorId(@PathVariable Long id){
		service.eliminarCursoAlumnoPorId(id);
		return ResponseEntity.noContent().build();
	}
	
	@GetMapping()
	@Override
    public ResponseEntity<?> index (){ 
		List<Curso> cursos = ((List<Curso>) service.findAll())
				.stream().map( c ->{
					c.getCursoAlumnos().forEach( ca->{
						Alumno alumno = new Alumno();
						alumno.setId(ca.getAlumnoId());
						c.addAlumno(alumno);
					});
					return c;
				}).collect(Collectors.toList());
		
        return ResponseEntity.ok().body(cursos);
    }
	
	@Override
	@GetMapping("/pagina")
	public ResponseEntity<?> index (Pageable pageable){
		Page<Curso> cursos = service.findAll(pageable)
				.map( curso ->{
					curso.getCursoAlumnos().forEach( ca->{
						Alumno alumno = new Alumno();
						alumno.setId(ca.getAlumnoId());
						curso.addAlumno(alumno);
					});
					return curso;
				});
	    return ResponseEntity.ok().body(cursos);
	}
	
	@Override
	@GetMapping("/{id}")
    public ResponseEntity<?> ver (@PathVariable Long id){
        Optional<Curso> alumno = service.findById(id);

        if( alumno.isEmpty()){
            return ResponseEntity.notFound().build();
        }
        Curso curso = alumno.get();
        if(curso.getCursoAlumnos().isEmpty() == false) {
        	List<Long> ids = curso.getCursoAlumnos().stream().map(ca-> ca.getAlumnoId() )
        			.collect(Collectors.toList());
        	List<Alumno> alumnos = (List<Alumno>) service.obtenerAlumnosPorCurso(ids); 
        	curso.setAlumnos(alumnos); 
        }
        
        return ResponseEntity.ok().body(curso);
    }
	
	@GetMapping("/balanceador-test")
	public ResponseEntity<?> balanceadorTest() {
		Map<String,Object>response = new HashMap<String,Object>();
		response.put("balanceador",balanceadorTest );
		response.put("cursos", service.findAll());
		return ResponseEntity.ok(response);
	}

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
			CursoAlumno cursoAlumno = new CursoAlumno();
			cursoAlumno.setAlumnoId( a.getId() );
			cursoAlumno.setCurso( dbCurso );
			
			dbCurso.addCursoAlumno(cursoAlumno);
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
		CursoAlumno cursoAlumno = new CursoAlumno();
		cursoAlumno.setAlumnoId(alumno.getId());
		dbCurso.removeCursoAlumno(cursoAlumno);
		return ResponseEntity.status(HttpStatus.CREATED).body( this.service.save(dbCurso));
	} 
	
	@GetMapping("/alumno/{id}")
	public ResponseEntity<?> buscarPorAlumnoId(@PathVariable Long id) {
		Curso curso = service.findCursoByAlumnoId(id);

		if (curso != null) {
			List<Long> examenesIds = (List<Long>) service.obtenerExamenesIdsConRespuestasAlumno(id);
			 
			if (examenesIds != null && examenesIds.size() > 0) {

				List<Examen> examenes = curso.getExamenes().stream().map(examen -> {
					if (examenesIds.contains(examen.getId())) {
						examen.setRespondido(true);
					}
					return examen;
				}).collect(Collectors.toList());
				curso.setExamenes(examenes);
			}
		}

		return ResponseEntity.ok(curso);
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
