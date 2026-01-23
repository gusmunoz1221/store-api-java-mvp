package com.store.product.service;

import com.store.category.entity.SubcategoryEntity;
import com.store.category.repository.SubcategoryRepository;
import com.store.exception.BusinessException;
import com.store.exception.ResourceNotFoundException;
import com.store.product.dto.ProductPatchRequestDTO;
import com.store.product.dto.ProductRequestDTO;
import com.store.product.dto.ProductResponseDTO;
import com.store.product.entity.ProductEntity;
import com.store.product.mapper.ProductMapper;
import com.store.product.repository.ProductRepository;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;

@Service
@AllArgsConstructor
@Transactional(readOnly = true)
public class ProductServiceImp implements ProductService {
    private final SubcategoryRepository subcategoryRepository;
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;


    /**
     * crea y persiste un nuevo producto a partir de la información recibida en el DTO.
     * <p>Antes de la creación, valida que:
     * <ul>
     *   <li>la subcategoría asociada exista.</li>
     *   <li>no exista otro producto con el mismo nombre.</li>
     * </ul>
     * el proceso se ejecuta dentro de una transacción para garantizar consistencia
     * entre validaciones y persistencia.
     * @param productRequest DTO con los datos necesarios para crear el producto
     * @return DTO del producto creado y persistido
     * @throws ResourceNotFoundException si la subcategoría no existe
     * @throws BusinessException si ya existe un producto con el mismo nombre
     */
    @Transactional
    @Override
    public ProductResponseDTO createProduct(ProductRequestDTO productRequest) {
        SubcategoryEntity subcategory = subcategoryRepository
                .findById(productRequest.getSubcategoryId())
                .orElseThrow(()-> new ResourceNotFoundException("subcategoria no encontrada"));

        if(productRepository.existsByName(productRequest.getName()))
            throw new BusinessException("producto con el nombre: "+productRequest.getName()+" ya existe");

        ProductEntity product = productMapper.dtoToEntity(productRequest,subcategory);
        productRepository.save(product);

        return  productMapper.entityToDto(product);
    }

    /**
     * actualiza parcialmente un producto existente utilizando un DTO tipo PATCH.
     * <p>Aplica únicamente los campos presentes en el request y valida:
     * <ul>
     *   <li>Que el producto exista.</li>
     *   <li>Que el nombre no esté duplicado (si se modifica).</li>
     *   <li>Que el stock y el precio no sean valores negativos.</li>
     *   <li>Que la subcategoría exista si se solicita un cambio.</li>
     * </ul>
     * la operación se ejecuta dentro de una transacción para garantizar
     * la consistencia del estado del producto.
     * @param request DTO con los campos a actualizar
     * @param productId identificador del producto a modificar
     * @return DTO del producto actualizado
     * @throws ResourceNotFoundException si el producto o la subcategoría no existen
     * @throws BusinessException si se violan reglas de negocio
     */
    @Transactional
    @Override
    public ProductResponseDTO updateProduct(@Valid ProductPatchRequestDTO request,
                                            @NotNull(message = "El id del producto es obligatorio") Long productId){

        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("El produ  cto con ID: " + productId + " no existe"));

        if (request.getName() != null &&
                !product.getName().equalsIgnoreCase(request.getName()) &&
                productRepository.existsByName(request.getName()))
            throw new BusinessException(
                    "El producto con nombre '" + request.getName() + "' ya existe");


        if (request.getStock() != null && request.getStock() < 0)
            throw new BusinessException("El stock no puede ser negativo");

        if (request.getPrice() != null &&
                request.getPrice().compareTo(BigDecimal.ZERO) < 0)
            throw new BusinessException("El precio no puede ser negativo");


        SubcategoryEntity subcategory = null;
        if (request.getSubcategoryId() != null &&
                !request.getSubcategoryId().equals(product.getSubcategory().getId())) {

            subcategory = subcategoryRepository.findById(request.getSubcategoryId())
                    .orElseThrow(() ->
                            new ResourceNotFoundException("Subcategoría no encontrada id: "
                                    + request.getSubcategoryId()));
        }

        productMapper.updateEntity(product, request, subcategory);

        return productMapper.entityToDto(product);
    }

    /**
     * elimina un producto existente por su identificador.
     * @param id identificador del producto a eliminar
     * @throws ResourceNotFoundException si el producto no existe
     */
    @Override
    public void deleteProduct(Long id) {
        ProductEntity product = productRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("el producto con ID: "+id+" no existe"));

        productRepository.delete(product);
    }


    public ProductResponseDTO findById(Long id){
        return productRepository.findById(id)
                .map(productMapper::entityToDto)
                .orElseThrow(() -> new ResourceNotFoundException("el producto con ID: "+id+" no existe"));
    }

                /**----------------------FILTROS DE BUSCQUEDA----------------**/


    @Override
    public Page<ProductResponseDTO> listProducts(Pageable pageable) {
        return productRepository
                .findAll(pageable)
                .map(productMapper::entityToDto);

    }

    @Override
    public Page<ProductResponseDTO> filterBySubcategory(Long subcategoryId, Pageable pageable) {
        return productRepository
                .findBySubcategoryId(subcategoryId, pageable)
                .map(productMapper::entityToDto);
    }

    @Override
    public Page<ProductResponseDTO> filterProductsByCategory(Long categoryId, Pageable pageable) {
        return productRepository
                .findBySubcategoryCategoryId(categoryId, pageable)
                .map(productMapper::entityToDto);
    }


    @Override
    public Page<ProductResponseDTO> filterByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        return productRepository
                .findByPriceBetween(minPrice, maxPrice, pageable)
                .map(productMapper::entityToDto);
    }


    @Override
    public Page<ProductResponseDTO> listInStock(Pageable pageable) {
        return productRepository
                .findByStockGreaterThan(0, pageable)
                .map(productMapper::entityToDto);
    }


    @Override
    public Page<ProductResponseDTO> listOutOfStock(Pageable pageable) {
        return productRepository
                .findByStockEquals(0, pageable)
                .map(productMapper::entityToDto);
    }
    /*-----------------------------------------------------------**/

    /**                     -ADMIN SEARCH-
     * busca productos por nombre utilizando coincidencia parcial
     * basada en expresiones regulares (entre palabras).
     */
    @Override
    public Page<ProductResponseDTO> searchProducts(String name, Pageable pageable) {
        if (name == null || name.isBlank()) return Page.empty();

        String regex = name.trim().replaceAll("\\s+", "|");

        return productRepository
                .searchByNameRegexAny(regex, pageable)
                .map(productMapper::entityToDto);
    }

    /**                     -USER SEARCH-
     * busca productos disponibles en stock cuyo nombre contenga
     * cualquiera de las palabras indicadas.
     * <p>La entrada se normaliza reemplazando espacios múltiples
     * por el operador regex {@code |}.
     * @param name texto de búsqueda ingresado por el usuario
     * @param pageable información de paginación
     * @return página de productos disponibles que coinciden con la búsqueda
     */
    @Override
    public Page<ProductResponseDTO> searchAvailableProducts(String name, Pageable pageable) {
        if (name == null || name.isBlank()) return Page.empty();

        String regex = name.trim().replaceAll("\\s+", "|");

        return productRepository
                .searchByNameRegexAvailable(regex, pageable)
                .map(productMapper::entityToDto);
    }
}