package com.example.kode.controller;

import com.example.kode.entity.Contacto;
import com.example.kode.service.ContactoService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/contactos")
public class ContactoController {

    private final ContactoService contactoService;
    public ContactoController(ContactoService contactoService){
        this.contactoService = contactoService;
    }

    //crear contacto PSOT
    @PostMapping
    public ResponseEntity<Contacto> crearContacto(@RequestBody Contacto contacto){
        Contacto contactoCreado = contactoService.crearContacto(contacto);
        return ResponseEntity.status(HttpStatus.CREATED).body(contactoCreado);
    }

    //listado GEt
    @GetMapping
    public ResponseEntity<List<Contacto>> listarContactos(){
        return ResponseEntity.ok(contactoService.listarContactos());
    }

    //obtener por id
    @GetMapping("/{id}")
    public ResponseEntity<Contacto> obtenerPorId(@PathVariable Long id){
        return ResponseEntity.ok(contactoService.obtenerPorId(id));
    }

    //actualizar Put
    @PutMapping("/{id}")
    public ResponseEntity<Contacto> actualizarContacto(@PathVariable Long id, @RequestBody Contacto contacto){
        return ResponseEntity.ok(contactoService.actualizarContacto(id, contacto));
    }

    //eliminar
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminarContacto(@PathVariable Long id){
        contactoService.eliminarContacto(id);
        return ResponseEntity.noContent().build();
    }

}
