package tc.tiendaColeccionable.tiendaanime.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.parameters.RequestBody;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import tc.tiendaColeccionable.tiendaanime.dto.PedidosRequest;
import tc.tiendaColeccionable.tiendaanime.dto.PedidosResponse;
import tc.tiendaColeccionable.tiendaanime.service.PedidoService;

import java.net.URI;

@RestController
@RequestMapping("/API/Pedidos")
@Tag(name = "Pedidos",description = "Creacion y consultas de pedidos")
public class PedidoController {
    private final PedidoService pedidoService;

    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @PostMapping
    @Operation(
            summary ="Crear un pedido",
            description = """
                    Recibe productos y cantidades.
                    El Servidor obtiene los ´recops deñ catalogo 
                    y calcula subtotal, descuento y total.
                    """
    )
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Recurso creado"
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos invalidos",
                    content = @Content
            )
    })
    public ResponseEntity<PedidosResponse> crear(@RequestBody @Valid PedidosRequest request){
        PedidosResponse pedido = pedidoService.crear(request);
        URI ubicacion = ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(pedido.id())
                .toUri();
        return ResponseEntity.created(ubicacion)
                .body(pedido);
    }

    
}
