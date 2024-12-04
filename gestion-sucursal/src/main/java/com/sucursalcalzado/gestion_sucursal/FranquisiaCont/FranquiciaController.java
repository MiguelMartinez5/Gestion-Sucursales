package com.sucursalcalzado.gestion_sucursal.FranquisiaCont;

import com.sucursalcalzado.gestion_sucursal.FranquiciaRepo.FranquiciaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.List;
import java.util.Map;


@RestController
@RequestMapping("/api/franquicia")
public class FranquiciaController {

    private final FranquiciaRepository franquiciaRepository;

    public FranquiciaController(FranquiciaRepository franquiciaRepository) {
        this.franquiciaRepository = franquiciaRepository;
    }

    @PostMapping
    public ResponseEntity<String> agregarFranquicia(@RequestBody Map<String, String> request) {
        String codigoFranquicia = request.get("codigo");
        String nombre = request.get("nombre");

        franquiciaRepository.agregarFranquicia(codigoFranquicia, nombre);
        return ResponseEntity.ok("Franquicia agregada o actualizada exitosamente");
    }


    // creo una sucursal a una franquicia con codigo
    @PostMapping("/{franquiciaId}/sucursales")
    public ResponseEntity<String> agregarSucursal(
            @PathVariable Long franquiciaId,
            @RequestBody Map<String, String> request) {
        String codigoSucursal = request.get("codigo");
        String nombre = request.get("nombre");

        franquiciaRepository.agregarSucursal(codigoSucursal, nombre, franquiciaId);
        return ResponseEntity.ok("Sucursal agregada o actualizada exitosamente");
    }



    // agrego un producto a una sucursal especifca con codigo
    @PostMapping("/sucursales/{sucursalId}/productos")
    public ResponseEntity<String> agregarProducto(
            @PathVariable Long sucursalId,
            @RequestBody Map<String, Object> request) {
        String codigoProducto = (String) request.get("codigo");
        String nombre = (String) request.get("nombre");
        Integer stock = (Integer) request.get("stock");

        franquiciaRepository.agregarProducto(codigoProducto, nombre, stock, sucursalId);
        return ResponseEntity.ok("Producto agregado o actualizado exitosamente");
    }



    // elimino un producto de una sucursal
    @DeleteMapping("/productos/{productoId}/sucursales/{sucursalId}")
    public ResponseEntity<String> eliminarProducto(@PathVariable Long productoId, @PathVariable Long sucursalId) {
        try {
            franquiciaRepository.eliminarProducto(productoId, sucursalId);
            return ResponseEntity.ok("Producto eliminado exitosamente de la sucursal.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al eliminar el producto.");
        }
    }

    // modifico el stock de un producto
    @PutMapping("/productos/{productoId}/stock/sucursales/{sucursalId}")
    public ResponseEntity<String> modificarStock(
            @PathVariable Long productoId,
            @PathVariable Long sucursalId,
            @RequestBody int nuevoStock) {
        try {
            franquiciaRepository.modificarStock(productoId, nuevoStock, sucursalId);
            return ResponseEntity.ok("Stock actualizado exitosamente para el producto en la sucursal.");
        } catch (IllegalArgumentException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error al actualizar el stock.");
        }
    }

    // obtengo el producto con mayor stock de una sucursal
    @GetMapping("/{franquiciaId}/productos-mayor-stock")
    public ResponseEntity<List<Map<String, Object>>> obtenerProductoMayorStock(
            @PathVariable Long franquiciaId) {
        List<Map<String, Object>> productos = franquiciaRepository.obtenerProductoMayorStock(franquiciaId);
        return ResponseEntity.ok(productos);
    }
}

