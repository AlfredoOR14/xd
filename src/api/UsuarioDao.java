/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UsuarioDao {
    private static final String INSERTAR_USUARIO = "INSERT INTO usuario (nombre) VALUES (?)";
    private static final String OBTENER_USUARIOS = "SELECT * FROM usuario";
    private static final String OBTENER_USUARIO_POR_ID = "SELECT * FROM usuario WHERE id=?";
    private static final String ACTUALIZAR_USUARIO = "UPDATE usuario SET nombre=? WHERE id=?";
    private static final String ELIMINAR_USUARIO = "DELETE FROM usuario WHERE id=?";
    
    public void crearUsuario(Usuario usuario) {
        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement preparedStatement = conexion.prepareStatement(INSERTAR_USUARIO)) {
            preparedStatement.setString(1, usuario.getNombre());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public List<Usuario> obtenerUsuarios() {
        List<Usuario> usuarios = new ArrayList<>();
        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement preparedStatement = conexion.prepareStatement(OBTENER_USUARIOS);
             ResultSet resultSet = preparedStatement.executeQuery()) {
            while (resultSet.next()) {
                Usuario usuario = new Usuario();
                usuario.setId(resultSet.getInt("id"));
                usuario.setNombre(resultSet.getString("nombre"));
                usuarios.add(usuario);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuarios;
    }
    
    public Usuario obtenerUsuarioPorId(int id) {
        Usuario usuario = null;
        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement preparedStatement = conexion.prepareStatement(OBTENER_USUARIO_POR_ID)) {
            preparedStatement.setInt(1, id);
            try (ResultSet resultSet = preparedStatement.executeQuery()) {
                if (resultSet.next()) {
                    usuario = new Usuario();
                    usuario.setId(resultSet.getInt("id"));
                    usuario.setNombre(resultSet.getString("nombre"));
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return usuario;
    }
    
    public void actualizarUsuario(Usuario usuario) {
        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement preparedStatement = conexion.prepareStatement(ACTUALIZAR_USUARIO)) {
            preparedStatement.setString(1, usuario.getNombre());
            preparedStatement.setInt(2, usuario.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    
    public void eliminarUsuario(int id) {
        try (Connection conexion = ConexionBD.getConnection();
             PreparedStatement preparedStatement = conexion.prepareStatement(ELIMINAR_USUARIO)) {
            preparedStatement.setInt(1, id);
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public static void main(String[] args) {
        
        UsuarioDao a = new UsuarioDao();
        
       Usuario u = new Usuario();
       u.setNombre("jose");
        a.eliminarUsuario(u.getNombre().charAt(0));
        
        
    }
}