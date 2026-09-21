package com.example.kode.service;

import com.example.kode.entity.Contacto;

import java.util.List;

public interface ContactoService {

    Contacto crearContacto(Contacto contacto);
    List<Contacto> listarContactos();
    Contacto obtenerPorId(Long id);
    Contacto actualizarContacto(Long id, Contacto contacto);
    void eliminarContacto(Long id);

}
