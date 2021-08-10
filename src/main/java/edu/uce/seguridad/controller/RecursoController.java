package edu.uce.seguridad.controller;

import edu.uce.seguridad.exception.NoEncontradoExcepcion;
import edu.uce.seguridad.model.Recurso;
import edu.uce.seguridad.service.service.RecursoService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/sgcnegocio/recursos")
@AllArgsConstructor
public class RecursoController {

    private RecursoService service;

    @GetMapping
    public ResponseEntity<List<Recurso>> listar() {
        List<Recurso> recursos = service.buscarTodos();
        return new ResponseEntity<List<Recurso>>(recursos, HttpStatus.OK);
    }

    @GetMapping("/buscarRecursoPorId/{id}")
    public ResponseEntity<Recurso> listarPorId(@PathVariable("id") String idRecurso) {
        Recurso recurso = service.buscaPorId(idRecurso);
        if (recurso == null) {
            throw new NoEncontradoExcepcion("respuesta", "Id no encontrado " + idRecurso);
        }
        return new ResponseEntity<Recurso>(recurso, HttpStatus.OK);
    }

    @GetMapping("/buscarRecursosPorUsuario/{usuario}")
    public ResponseEntity<List<Recurso>> buscarPorUsuario(@PathVariable("usuario") String nombreUsuario) {
        List<Recurso> recursos = service.buscarRecursosPorUsuario(nombreUsuario);
        return new ResponseEntity<List<Recurso>>(recursos, HttpStatus.OK);
    }

    @PostMapping(produces = "application/json", consumes = "application/json")
    public ResponseEntity<Object> guardar(@RequestBody Recurso recursoEnviado) {
        Recurso recurso = service.agregar(recursoEnviado);
        URI location = ServletUriComponentsBuilder.fromCurrentRequest().path("{/id}")
                .buildAndExpand(recurso.get_id()).toUri();
        return ResponseEntity.created(location).build();
    }

    @PutMapping
    public ResponseEntity<Object> modificar(@RequestBody Recurso recurso) {
        service.actualizar(recurso);
        return new ResponseEntity<Object>(HttpStatus.OK);
    }

    @DeleteMapping("/{usuario}")
    public ResponseEntity<Object> eliminarPorUsuario(@PathVariable("usuario") String nombreUsuario) {
        service.eliminarDocumento(nombreUsuario);
        return new ResponseEntity<Object>(HttpStatus.OK);
    }
}