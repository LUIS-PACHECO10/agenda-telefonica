package com.example.kode.repository;

import com.example.kode.entity.Telefono;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TelefonoRepository extends JpaRepository<Telefono, Long> {

    Optional<Telefono> findByTipoIgnoreCaseAndNumero(String tipo, String numero);
}
