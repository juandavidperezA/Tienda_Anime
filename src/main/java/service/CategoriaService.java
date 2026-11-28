package service;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;
import tc.tiendaColeccionable.tiendaanime.dto.CategoriasRequest;
import tc.tiendaColeccionable.tiendaanime.dto.CategoriasResponse;
import tc.tiendaColeccionable.tiendaanime.entity.Categorias;
import tc.tiendaColeccionable.tiendaanime.repository.CategoriaRepository;

import java.util.List;
import java.util.Optional;

@Service
@Validated
@Transactional(readOnly = true)

public class CategoriaService {
    private final CategoriaRepository categoriaRepository;

    public CategoriaService(CategoriaRepository categoriaRepository) {
        this.categoriaRepository = categoriaRepository;
    }

    @Transactional
    public CategoriasResponse crear(@NotNull @Valid CategoriasRequest request){

        Categorias categorias= new Categorias(request.nombre().strip());

        Categorias guardada=categoriaRepository.save(categorias);

        return toResponse(guardada);
    }

    public List<CategoriasResponse> listar(){
        return categoriaRepository.findAll().stream().map(this::toResponse).toList();
    }

    public Optional<CategoriasResponse> buscarPorId(@NotNull @Positive Long id){
        return categoriaRepository.findById(id).map(this::toResponse);
    }

    private CategoriasResponse toResponse(Categorias categorias){
        return new CategoriasResponse(
                categorias.getId(),
                categorias.getNombre()
        );
    }
}
