package com.skiatel.basiccrud.controller;

import java.sql.SQLException;
import java.util.List;


import com.skiatel.basiccrud.entity.User;
import com.skiatel.basiccrud.entity.UserRequest;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import com.skiatel.basiccrud.odata.Database;

@RestController
@RequestMapping("/users")
@Validated
public class UsuarioControler {
    Database database = new Database();

    @GetMapping("/")
    public ResponseEntity<List<User>> get() throws SQLException {
        try{
            database.MakeConection();
            List<User> Usuarios = database.obtenerTodosUsuarios();
            database.closeConnection();
            return ResponseEntity.ok(Usuarios);
        }catch (SQLException e){
            return ResponseEntity.status(500).build();
        }
    }

    @PostMapping("/")
    public ResponseEntity<String> post(@Valid @RequestBody UserRequest userRequest) throws SQLException {
        try {
            database.MakeConection();
            database.createUser(userRequest.getNombre(), userRequest.getEmail());
            database.closeConnection();
            return ResponseEntity.ok("Usuario Creado Correctamente");
        } catch (SQLException e) {
            return ResponseEntity.status(500).body("Error al crear el usuario");
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> get(@PathVariable Integer id) throws SQLException {
        database.MakeConection();
        User usuario = new User(0, "", "");
        usuario = database.obtenerUsuarioPorId(id);
        database.closeConnection();
        if(usuario == null){
            return ResponseEntity.status(404).build();
        }else{
            return ResponseEntity.ok(usuario);
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<String> put(@PathVariable Integer id, @Valid @RequestBody UserRequest userRequest) throws SQLException {
        database.MakeConection();
        database.actualizarUsuario(id, userRequest.getNombre(), userRequest.getEmail());
        database.closeConnection();
        return ResponseEntity.ok("Usuario Actualizado correctamente");
    }

    @DeleteMapping("/eliminar/{id}")
    public ResponseEntity<String> delete(@PathVariable Integer id) throws SQLException {
        database.MakeConection();
        database.eliminarUsuario(id);
        database.closeConnection();
        return ResponseEntity.ok("Usuario Eliminado");
    }

}
