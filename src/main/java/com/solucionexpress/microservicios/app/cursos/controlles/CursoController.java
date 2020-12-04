package com.solucionexpress.microservicios.app.cursos.controlles;

import com.solucionexpress.microservicios.app.cursos.models.entity.Curso;
import com.solucionexpress.microservicios.app.cursos.services.CursoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
public class CursoController extends CommonController<Curso, CursoService>{
    @PutMapping("/{id}")
    public ResponseEntity<?> editar (@RequestBody Curso curso, @PathVariable Long id){
        Optional<Curso> o = this.service.findById(id);

        if (!o.isPresent()){
            return ResponseEntity.notFound().build();
        }
        Curso dbCurso = o.get();
        dbCurso.setNombre(o.get().getNombre());
        return ResponseEntity.status(HttpStatus.CREATED).body(this.service.save(dbCurso));
    }

}
