package com.sucursalcalzado.gestion_sucursal.FranquiciaRepo;

import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Map;

@Repository
public class FranquiciaRepository {
    private final JdbcTemplate jdbcTemplate;

    public FranquiciaRepository(JdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
    }

    public void agregarFranquicia(String codigoFranquicia, String nombre) {

        String sqlVerificarFranquicia = "SELECT COUNT(*) FROM franquicia WHERE codigo = ?";
        int count = jdbcTemplate.queryForObject(sqlVerificarFranquicia, Integer.class, codigoFranquicia);

        if (count > 0) {

            String sqlActualizarFranquicia = "UPDATE franquicia SET nombre = ? WHERE codigo = ?";
            jdbcTemplate.update(sqlActualizarFranquicia, nombre, codigoFranquicia);
        } else {

            String sqlInsertarFranquicia = "INSERT INTO franquicia (codigo, nombre) VALUES (?, ?)";
            jdbcTemplate.update(sqlInsertarFranquicia, codigoFranquicia, nombre);
        }
    }

    public void agregarSucursal(String codigoSucursal, String nombre, Long franquiciaId) {

        String sqlVerificarSucursal = "SELECT COUNT(*) FROM sucursal WHERE codigo = ?";
        int count = jdbcTemplate.queryForObject(sqlVerificarSucursal, Integer.class, codigoSucursal);

        if (count > 0) {

            String sqlActualizarSucursal = "UPDATE sucursal SET nombre = ?, franquicia_id = ? WHERE codigo = ?";
            jdbcTemplate.update(sqlActualizarSucursal, nombre, franquiciaId, codigoSucursal);
        } else {

            String sqlInsertarSucursal = "INSERT INTO sucursal (codigo, nombre, franquicia_id) VALUES (?, ?, ?)";
            jdbcTemplate.update(sqlInsertarSucursal, codigoSucursal, nombre, franquiciaId);
        }
    }

    public void agregarProducto(String codigoProducto, String nombre, int stock, Long sucursalId) {

        String sqlVerificarProducto = "SELECT COUNT(*) FROM producto WHERE codigo = ?";
        int count = jdbcTemplate.queryForObject(sqlVerificarProducto, Integer.class, codigoProducto);

        if (count > 0) {
            String sqlActualizarProducto = "UPDATE producto SET nombre = ?, stock = ?, sucursal_id = ? WHERE codigo = ?";
            jdbcTemplate.update(sqlActualizarProducto, nombre, stock, sucursalId, codigoProducto);
        } else {
            String sqlInsertarProducto = "INSERT INTO producto (codigo, nombre, stock, sucursal_id) VALUES (?, ?, ?, ?)";
            jdbcTemplate.update(sqlInsertarProducto, codigoProducto, nombre, stock, sucursalId);
        }
    }


    public void eliminarProducto(Long productoId, Long sucursalId) {
        // Verificar que el producto pertenece a la sucursal específica
        String sqlVerificarProducto = "SELECT COUNT(*) FROM producto WHERE id = ? AND sucursal_id = ?";
        int count = jdbcTemplate.queryForObject(sqlVerificarProducto, Integer.class, productoId, sucursalId);

        if (count == 0) {
            throw new IllegalArgumentException("El producto no pertenece a la sucursal especificada.");
        }

        // Eliminar el producto de la sucursal
        String sqlEliminarProducto = "DELETE FROM producto WHERE id = ? AND sucursal_id = ?";
        jdbcTemplate.update(sqlEliminarProducto, productoId, sucursalId);
    }

    public void modificarStock(Long productoId, int nuevoStock, Long sucursalId) {
        String sql = "UPDATE producto " +
                "SET stock = ? " +
                "WHERE id = ? AND sucursal_id = ?";
        int rowsUpdated = jdbcTemplate.update(sql, nuevoStock, productoId, sucursalId);

        if (rowsUpdated == 0) {
            throw new IllegalArgumentException("El producto no pertenece a la sucursal indicada o no existe.");
        }
    }

    public List<Map<String, Object>> obtenerProductoMayorStock(Long franquiciaId) {
        String sql = """
        SELECT s.nombre AS sucursal, p.nombre AS producto, p.stock
        FROM sucursal s
        JOIN producto p ON s.id = p.sucursal_id
        WHERE s.franquicia_id = ?
        AND p.stock = (
            SELECT MAX(stock) FROM producto WHERE sucursal_id = s.id
        )
        """;
        return jdbcTemplate.queryForList(sql, franquiciaId);
    }
}

