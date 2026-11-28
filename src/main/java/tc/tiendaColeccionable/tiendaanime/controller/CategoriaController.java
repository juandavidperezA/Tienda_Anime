package tc.tiendaColeccionable.tiendaanime.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import org.springframework.http.ProblemDetail;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import service.CategoriaService;
import tc.tiendaColeccionable.tiendaanime.dto.CategoriasRequest;
import tc.tiendaColeccionable.tiendaanime.dto.CategoriasResponse;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/categorias")
@Tag(name = "Categorias", description = "Creacion y consulta de categorias")

public class CategoriaController {

    private final CategoriaService categoriaService;

    public CategoriaController(CategoriaService categoriaService) {
        this.categoriaService = categoriaService;
    }

    @PostMapping
    @Operation(summary = "Crear Categorias")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "201",
                    description = "Recurso Creado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoriasResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Datos invalidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            )
    })

    public ResponseEntity<CategoriasResponse> crear(@Valid @RequestBody CategoriasRequest request){
        CategoriasResponse resultado=categoriaService.crear(request);

        URI ubicacion= ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/${id}")
                .buildAndExpand(resultado.id())
                .toUri();

        return ResponseEntity.created(ubicacion).body(resultado);

    }

    @GetMapping
    @Operation(summary = "Listar Categorias")
    public ResponseEntity<List<CategoriasResponse>> listar(){
        return ResponseEntity.ok(categoriaService.listar());
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar Categorias Por ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Recurso encontrado",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = CategoriasResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "ID invalidos",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ProblemDetail.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Recurso no encontrado",
                    content = @Content
            )
    })

    public ResponseEntity<CategoriasResponse> buscarPorId(@PathVariable @Positive Long id){
        return categoriaService.buscarPorId(id)
                .map(ResponseEntity::ok)
                .orElseGet(()->ResponseEntity.notFound().build());
    }
}
