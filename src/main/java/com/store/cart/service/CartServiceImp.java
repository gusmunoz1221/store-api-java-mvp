package com.store.cart.service;

import com.store.cart.dto.CartResponseDTO;
import com.store.cart.entity.CartEntity;
import com.store.cart.entity.CartItemEntity;
import com.store.cart.mapper.CartMapper;
import com.store.cart.repository.CartRepository;
import com.store.exception.BusinessException;
import com.store.exception.ResourceNotFoundException;
import com.store.product.entity.ProductEntity;
import com.store.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional
public class CartServiceImp implements CartService{
    private final CartRepository cartRepository;
    private final ProductRepository productRepository;
    private final CartMapper cartMapper;

    /**
     * Obtiene el carrito asociado a una sesión.
     * <p>Si no existe un carrito previo para la sesión indicada,
     * se crea uno nuevo con total inicial en cero.
     * @param sessionId identificador de la sesión del usuario
     * @return DTO del carrito
     */
    @Override
    public CartResponseDTO getCart(String sessionId) {
        CartEntity cart = getOrCreateCart(sessionId);
        return cartMapper.entityToDto(cart);
    }

    /**
     * Agrega un producto al carrito de una sesión.
     * <p>Comportamiento:
     * <ul>
     *   <li>Valida que la cantidad sea mayor a cero.</li>
     *   <li>Valida la existencia del producto y stock disponible.</li>
     *   <li>Si el producto ya existe en el carrito, incrementa su cantidad.</li>
     *   <li>Si no existe, crea un nuevo ítem asociado al carrito.</li>
     *   <li>Recalcula el total del carrito luego de la operación.</li>
     * </ul>
     * @param sessionId identificador de la sesión del usuario
     * @param productId identificador del producto
     * @param quantity cantidad a agregar
     * @return DTO del carrito actualizado
     * @throws BusinessException si la cantidad es inválida o no hay stock suficiente
     * @throws ResourceNotFoundException si el producto no existe
     */
    @Override
    public CartResponseDTO addToCart(String sessionId, Long productId, Integer quantity) {
        if (quantity <= 0) throw new BusinessException("La cantidad debe ser mayor a 0");
        ProductEntity product = productRepository.findById(productId)
                .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado"));

        if (product.getStock() < quantity) throw new BusinessException("Sin stock suficiente");

        CartEntity cart = getOrCreateCart(sessionId);

        Optional<CartItemEntity> existingItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(productId))
                .findFirst();

        if (existingItem.isPresent()) {
            // CASO A: Sumar cantidad
            CartItemEntity item = existingItem.get();
            int newQuantity = item.getQuantity() + quantity;

            if (product.getStock() < newQuantity)
                throw new BusinessException("Stock insuficiente para agregar más.");

            item.setQuantity(newQuantity);
        } else {
            // CASO B: Crear nuevo item
            CartItemEntity newItem = new CartItemEntity();
            newItem.setCart(cart); // Vinculación bidireccional importante
            newItem.setProduct(product);
            newItem.setQuantity(quantity);
            newItem.setUnitPrice(product.getPrice());

            cart.getItems().add(newItem); // Agregamos a la lista del padre
        }

        recalculateTotal(cart);

        CartEntity savedCart = cartRepository.save(cart);

        return cartMapper.entityToDto(savedCart);
    }

    /**
     * Elimina un producto del carrito de una sesión.
     * <p>Si el producto no se encuentra en el carrito,
     * se lanza una excepción.
     * @param sessionId identificador de la sesión del usuario
     * @param productId identificador del producto a eliminar
     * @return DTO del carrito actualizado
     * @throws ResourceNotFoundException si el producto no está presente en el carrito
     */
    @Override
    public CartResponseDTO removeItemFromCart(String sessionId, Long productId) {
        CartEntity cart = getOrCreateCart(sessionId);

        // si cumple la condicion lo elimina de la bd y devuelve true
        boolean removed = cart.getItems()
                .removeIf(item -> item.getProduct().getId().equals(productId));

        if (!removed)
            throw new ResourceNotFoundException("El producto no esta en el carrito");

        recalculateTotal(cart);
        CartEntity savedCart = cartRepository.save(cart);

        return cartMapper.entityToDto(savedCart);
    }

    /**
     * Vacía completamente el carrito asociado a una sesión.
     * <p>El total del carrito se recalcula y se persiste el estado vacío.
     * @param sessionId identificador de la sesión del usuario
     */
    @Override
    public void clearCart(String sessionId) {
        CartEntity cart = getOrCreateCart(sessionId);
        cart.getItems().clear();
        recalculateTotal(cart);
        cartRepository.save(cart);
    }

            /*-------------METODOS PRIVADOS AUXILIARES ------------*/

    /**
     * Obtiene el carrito asociado a una sesión o crea uno nuevo si no existe.
     * @param sessionId identificador de la sesión
     * @return entidad de carrito existente o recién creada
     */
    private CartEntity getOrCreateCart(String sessionId) {
        return cartRepository.findBySessionId(sessionId)
                .orElseGet(() -> {
                    CartEntity cart = new CartEntity();
                    cart.setSessionId(sessionId);
                    cart.setTotalAmount(BigDecimal.ZERO);
                    return cart;
                });
    }

    /**
     * Recalcula el total del carrito a partir de los ítems actuales.
     * <p>El total se obtiene multiplicando el precio unitario
     * por la cantidad de cada ítem.
     * @param cart carrito a recalcular
     */
    private void recalculateTotal(CartEntity cart) {
        BigDecimal total = cart.getItems()
                .stream()
                .map(item -> {
                    BigDecimal unitPrice = item.getUnitPrice() != null
                            ? item.getUnitPrice()
                            : BigDecimal.ZERO;

                    BigDecimal quantity = BigDecimal.valueOf(item.getQuantity());

                    return unitPrice.multiply(quantity);
                })
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        cart.setTotalAmount(total);
    }
}