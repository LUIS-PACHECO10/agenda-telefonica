package com.example.kode.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name ="contactos",
uniqueConstraints = {@UniqueConstraint(columnNames = "email")})
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Contacto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String apellido;

    @Column(nullable = false, unique = true)
    private String email;

    //un contacto puede tener uno o más teléfonos y un teléfono puede tener uno o más contactos
    @ManyToMany
    @JoinTable(name = "contacto_telefono",
    joinColumns = @JoinColumn(name ="contacto_id"),
    inverseJoinColumns = @JoinColumn(name ="telefono_id")
    )

    private List<Telefono> telefonos = new ArrayList<>();
    private LocalDateTime fechaCreacion;
    private Boolean activo;

}//fin
