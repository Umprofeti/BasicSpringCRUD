package com.skiatel.basiccrud.odata;


import com.skiatel.basiccrud.entity.User;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Repository;


import java.sql.*;
import java.util.ArrayList;
import java.util.List;


@Repository
public class Database {
    @Value("${spring.datasource.url}")
    private String UrlDatabase;


    Logger logger = LoggerFactory.getLogger(Database.class);

    private Connection con = null;

    public void MakeConection() throws SQLException {
        logger.info("El string de connexion es:{}", UrlDatabase);
        con = DriverManager.getConnection("jdbc:postgresql:api_database_reto_1?user=root&password=PostgreeChallenge2150$#");
        if (con != null) {
            logger.info("Conexión a la base de datos establecida");
        }
    }

    public void closeConnection() throws SQLException {
        if (con != null) {
            con.close();
            logger.info("Conexión cerrada correctamente");
        }
    }

    public void createUser(String p_nombre, String p_email) throws SQLException {
        if (con == null) {
            throw new SQLException("La Conexión no se ha establecido");
        }
        String sql = "{call crearusuario(?, ?)}";
        try (CallableStatement callStmt = con.prepareCall(sql)) {
            callStmt.setString(1, p_nombre);
            callStmt.setString(2, p_email);
            callStmt.execute();
        }catch (SQLException e){
            throw new SQLException("Error al crear el usuario", e);
        }
    }

    public User obtenerUsuarioPorId(Integer id_usuario) throws SQLException {
        if (con == null) {
            throw new SQLException("La Conexión no se ha establecido");
        }
        String sql = "{call obtenerusuarioporid(?)}";
        try (CallableStatement callStmt = con.prepareCall(sql)) {
            callStmt.setInt(1, id_usuario);
            callStmt.execute();
            ResultSet rs = callStmt.getResultSet();
            if (rs.next()) {
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                return new User(id, nombre, email);
            } else {
                return null;
            }
        } catch (SQLException e) {
            throw new SQLException("Error al encontrar el usuario", e);
        }
    }

    public List<User> obtenerTodosUsuarios() throws SQLException {
        List<User> usuarios = new ArrayList<>();
        if  (con == null) {
            throw new SQLException("La Conexión no se ha establecido");
        }
        String sql = "{call obtenertodoslosusuarios()}";
        try (CallableStatement callStmt = con.prepareCall(sql)) {
            callStmt.execute();
            ResultSet rs = callStmt.getResultSet();
            while (rs.next()) {
                // Suponiendo que la tabla tiene las columnas id, nombre y email
                int id = rs.getInt("id");
                String nombre = rs.getString("nombre");
                String email = rs.getString("email");
                User usuario = new User(id, nombre, email);
                usuarios.add(usuario);
            }
            rs.close();
            callStmt.close();
            return usuarios;
        }
    }

    public void actualizarUsuario(String p_nombre, String p_email) throws SQLException {
        if (con == null) {
            throw new SQLException("La Conexión no se ha establecido");
        }
        String sql = "{call actualizarusuario(?, ?)}";
        try (CallableStatement callStmt = con.prepareCall(sql)) {
            callStmt.setString(1, p_nombre);
            callStmt.setString(2, p_email);
            callStmt.execute();
        }
    }

    public void eliminarUsuario(Integer id_usuario) throws SQLException {
        if (con == null) {
            throw new SQLException("La Conexión no se ha establecido");
        }
        String sql = "{call eliminarusuario(?)}";
        try (CallableStatement callStmt = con.prepareCall(sql)) {
            callStmt.setInt(1, id_usuario);
            callStmt.execute();
        }
    }

}
