package com.ptithcm2021.laptopshop.service.impl;

import com.ptithcm2021.laptopshop.exception.AppException;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.mapper.CartMapper;
import com.ptithcm2021.laptopshop.model.dto.request.CartRequest;
import com.ptithcm2021.laptopshop.model.dto.response.CartResponse;
import com.ptithcm2021.laptopshop.model.entity.*;
import com.ptithcm2021.laptopshop.repository.*;
import com.ptithcm2021.laptopshop.service.CartService;
import com.ptithcm2021.laptopshop.util.FetchUserIdUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final ProductDetailRepository productDetailRepository;
    private final CartMapper cartMapper;
    private final InventoryRepository inventoryRepository;
    private final GoodsReceiptNoteRepository goodsReceiptNoteRepository;

    @Override
    public CartResponse addCart(CartRequest cartRequest) {
        int quantity = goodsReceiptNoteRepository.countGRNByProductDetailId(cartRequest.getProductDetailId());
        if (quantity == 0)
            throw new AppException(ErrorCode.PRODUCT_NOT_AVAILABLE);

        Inventory inventory = inventoryRepository.findById(cartRequest.getProductDetailId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (inventory.getQuantity() < cartRequest.getQuantity())
            throw new AppException(ErrorCode.PRODUCT_IS_OUT_OF_QUANTITY);

        ProductDetail product = productDetailRepository.findById(cartRequest.getProductDetailId())
                .orElseThrow(() ->  new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        String userId = FetchUserIdUtil.fetchUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() ->  new AppException(ErrorCode.USER_NOT_FOUND));

        CartId cartId = CartId.builder()
                .productDetailId(product.getId())
                .userId(user.getId())
                .build();

        Cart cart = cartRepository.findById(cartId).orElse(null);
        if (cart == null) {
            cart = Cart.builder()
                    .id(cartId)
                    .user(user)
                    .productDetail(product)
                    .productPromotionId(cartRequest.getProductPromotionId())
                    .quantity(cartRequest.getQuantity())
                    .build();
        } else {
            cart.setQuantity(cart.getQuantity() + cartRequest.getQuantity());
            cart.setProductPromotionId(cartRequest.getProductPromotionId());
        }

        return cartMapper.toCartResponse(cartRepository.save(cart));
    }

    @Override
    public CartResponse updateCart(CartRequest cartRequest) {
        String userId = FetchUserIdUtil.fetchUserId();

        CartId cartId = CartId.builder()
                .productDetailId(cartRequest.getProductDetailId())
                .userId(userId)
                .build();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        Inventory inventory = inventoryRepository.findById(cartRequest.getProductDetailId())
                .orElseThrow(() -> new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        if (inventory.getQuantity() < cartRequest.getQuantity())
            throw new AppException(ErrorCode.PRODUCT_IS_OUT_OF_QUANTITY);

        cart.setQuantity(cartRequest.getQuantity());
        cart.setProductPromotionId(cartRequest.getProductPromotionId());

        return cartMapper.toCartResponse(cartRepository.save(cart));
    }

    @Override
    public void removeCart(long productDetailId) {
        String userId = FetchUserIdUtil.fetchUserId();

        CartId cartId = CartId.builder()
                .productDetailId(productDetailId)
                .userId(userId)
                .build();

        if(!cartRepository.existsById(cartId))
            throw new AppException(ErrorCode.CART_NOT_FOUND);

        try{
            cartRepository.deleteById(cartId);
        }catch (Exception e){
            log.debug(e.getMessage());
            throw new AppException(ErrorCode.CANNOT_DELETE);
        }
    }

    @Override
    public CartResponse getCart(long productDetailId) {
        String userId = FetchUserIdUtil.fetchUserId();

        CartId cartId = CartId.builder()
                .productDetailId(productDetailId)
                .userId(userId)
                .build();
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        return cartMapper.toCartResponse(cart);
    }

    @Override
    public List<CartResponse> getCartList() {
        String userId = FetchUserIdUtil.fetchUserId();

        return cartRepository.findAllByUserId(userId).stream()
                .map(cartMapper::toCartResponse).collect(Collectors.toList());
    }
}
