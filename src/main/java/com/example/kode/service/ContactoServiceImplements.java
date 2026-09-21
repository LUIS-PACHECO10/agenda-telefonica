package com.example.kode.service;

import com.example.kode.entity.Contacto;
import com.example.kode.entity.Telefono;
import com.example.kode.repository.ContactoRepository;
import com.example.kode.repository.TelefonoRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Service
public class ContactoServiceImplements implements ContactoService{

    //in
    private final ContactoRepository contactoRepository;
    private final TelefonoRepository telefonoRepository;

    public ContactoServiceImplements(
            ContactoRepository contactoRepository,
            TelefonoRepository telefonoRepository){
        this.contactoRepository = contactoRepository;
        this.telefonoRepository = telefonoRepository;
    }

    //métodos de ContactoService

    //Crear contacto nuevo
    @Override
    public Contacto crearContacto(Contacto contacto) {

        validarContacto(contacto);

        if (contactoRepository.existsByEmailIgnoreCase(contacto.getEmail())){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ya ha sido registrado");
        }

        contacto.setId(null);
        contacto.setEmail(contacto.getEmail().trim().toLowerCase());
        contacto.setTelefonos(prepararTelefonos(contacto.getTelefonos()));
        contacto.setFechaCreacion(LocalDateTime.now());
        contacto.setActivo(true);

        return contactoRepository.save(contacto);
    }

    //Listado de contactos--------------------------------------------
    @Override
    public List<Contacto> listarContactos() {
        return contactoRepository.findByActivoTrue();
    }

    //Obtener contacto por Id-------------------------------------
    @Override
    public Contacto obtenerPorId(Long id) {
        return contactoRepository.findByIdAndActivoTrue(id).orElseThrow(()->
                new ResponseStatusException(HttpStatus.NOT_FOUND, "No se encontró el contacto"));
    }

    //actualizar contactos--------------------------------------------
    @Override
    public Contacto actualizarContacto(Long id, Contacto contactoActualizado) {

        Contacto contactoExistente = obtenerPorId(id);
        validarContacto(contactoActualizado);

        if (contactoRepository.existsByEmailIgnoreCaseAndIdNot(contactoActualizado.getEmail(),id)){
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Email ya ha sido registrado");
        }
        contactoExistente.setNombre(contactoActualizado.getNombre().trim());
        contactoExistente.setApellido(contactoActualizado.getApellido().trim());
        contactoExistente.setEmail(contactoActualizado.getEmail().trim());
        contactoExistente.setTelefonos(prepararTelefonos(contactoActualizado.getTelefonos()));

        return contactoRepository.save(contactoExistente);
    }

    //Eliminar un contacto ------------------------------
    @Override
    public void eliminarContacto(Long id) {

        Contacto contacto = obtenerPorId(id);
        contacto.setActivo(false);
        contactoRepository.save(contacto);
    }

    //metodos validaciones-- validar contacto
    private void validarContacto(Contacto contacto){
        if (contacto.getNombre() == null || contacto.getNombre().isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El nombre de contacto es obligatorio");
        }
        if (contacto.getApellido() == null || contacto.getApellido().isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El apellido de contacto es obligatorio");
        }
        if (contacto.getEmail() == null || contacto.getEmail().isBlank()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El email es obligatorio");
        }
        if (contacto.getTelefonos() == null || contacto.getTelefonos().isEmpty()){
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El contacto debe tener un número");
        }

        for(Telefono telefono: contacto.getTelefonos()){
            if (telefono.getTipo() == null || telefono.getTipo().isBlank()){
                throw  new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tipo de Tel. es obligatorio");
            }

            String tipo = telefono.getTipo().trim().toUpperCase();
            if (!tipo.equals("CASA") && !tipo.equals("CELULAR") && !tipo.equals("TRABAJO")){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El tipo debe ser CASA, CELULAR O TRABAJO");
            }

            if (telefono.getNumero() == null || telefono.getNumero().isBlank()){
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El numero es obligatorio");
            }
        }//cierre for
    }// cierre metodo

    //metodo preparar telefonos
    private List<Telefono> prepararTelefonos(List<Telefono> telefonos){
        List<Telefono> telefonosFinales = new ArrayList<>();
        Set<String> telefonosAgregados = new HashSet<>();

        for (Telefono telefono : telefonos){
            String tipo = telefono.getTipo().trim().toLowerCase();
            String numero = telefono.getNumero().trim();
            String clave = tipo + "-" + numero;

            if (telefonosAgregados.contains(clave)){
                continue;
            }
            Telefono telefonoFinal = telefonoRepository.findByTipoIgnoreCaseAndNumero(tipo, numero).orElseGet(()->{
                Telefono nuevoTelefono = new Telefono();
                nuevoTelefono.setTipo(tipo);
                nuevoTelefono.setNumero(numero);
                return telefonoRepository.save(nuevoTelefono);
            });
            telefonosFinales.add(telefonoFinal);
            telefonosAgregados.add(clave);
        }//cierre for
        return telefonosFinales;
    }

}//fin metodo principal
