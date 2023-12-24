/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package api;

import com.sun.net.httpserver.HttpServer;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpExchange;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.List;
import org.json.JSONObject;

public class APIUsuarios {
    
 private static UsuarioDao usuarioDAO = new UsuarioDao();
    
    public static void main(String[] args) throws IOException {
        HttpServer server = HttpServer.create();
        server.bind(null, 3306);
        
        server.createContext("/usuarios", new UsuariosHandler());
        
        server.start();
    }
    
  static class UsuariosHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String method = exchange.getRequestMethod();
            String response = "";
            
            if (method.equalsIgnoreCase("GET")) {
                List<Usuario> usuarios = usuarioDAO.obtenerUsuarios();
                response = usuarios.toString();
            } else if (method.equalsIgnoreCase("POST")) {
                // Procesar la solicitud POST para crear un nuevo usuario
                if (exchange.getRequestHeaders().containsKey("Content-Type") &&
                    exchange.getRequestHeaders().getFirst("Content-Type").equalsIgnoreCase("application/json")) {
                    Usuario nuevoUsuario = leerJSON(exchange.getRequestBody(), Usuario.class);
                    if (nuevoUsuario != null) {
                        usuarioDAO.crearUsuario(nuevoUsuario);
                        response = "Usuario creado con éxito.";
                    } else {
                        response = "Error al procesar la solicitud POST.";
                    }
                } else {
                    response = "Solicitud POST incorrecta.";
                }
            } else if (method.equalsIgnoreCase("PUT")) {
                // Procesar la solicitud PUT para actualizar un usuario
                if (exchange.getRequestHeaders().containsKey("Content-Type") &&
                    exchange.getRequestHeaders().getFirst("Content-Type").equalsIgnoreCase("application/json")) {
                    Usuario usuarioActualizado = leerJSON(exchange.getRequestBody(), Usuario.class);
                    if (usuarioActualizado != null) {
                        usuarioDAO.actualizarUsuario(usuarioActualizado);
                        response = "Usuario actualizado con éxito.";
                    } else {
                        response = "Error al procesar la solicitud PUT.";
                    }
                } else {
                    response = "Solicitud PUT incorrecta.";
                }
            } else if (method.equalsIgnoreCase("DELETE")) {
                // Procesar la solicitud DELETE para eliminar un usuario
                String path = exchange.getRequestURI().getPath();
                String[] parts = path.split("/");
                if (parts.length == 3) {
                    int id = Integer.parseInt(parts[2]);
                    usuarioDAO.eliminarUsuario(id);
                    response = "Usuario eliminado con éxito.";
                } else {
                    response = "Solicitud DELETE incorrecta.";
                }
            }
            
            exchange.sendResponseHeaders(200, response.getBytes().length);
            OutputStream os = exchange.getResponseBody();
            os.write(response.getBytes());
            os.close();
        }
        
          private <T> T leerJSON(InputStream requestBody, Class<T> clase) {
        try {
            // Leer el cuerpo de la solicitud JSON
            BufferedReader reader = new BufferedReader(new InputStreamReader(requestBody));
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            String jsonStr = sb.toString();

            // Crear un objeto JSON a partir de la cadena JSON
            JSONObject jsonObject = new JSONObject(jsonStr);

            // Verificar si la clase deseada es Usuario
            if (clase.equals(Usuario.class)) {
                // Si es la clase Usuario, crear y llenar un objeto Usuario
                Usuario usuario = new Usuario();
                usuario.setId(jsonObject.getInt("id"));
                usuario.setNombre(jsonObject.getString("nombre"));

                // Devolver el objeto Usuario creado
                return (T) usuario;
            } else {
                // Si la clase deseada no es compatible, devolver null o manejar el error según sea necesario
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}}