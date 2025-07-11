package com.ptithcm2021.laptopshop.service.impl;

import com.ptithcm2021.laptopshop.exception.AppException;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.mapper.CartMapper;
import com.ptithcm2021.laptopshop.model.dto.request.CartRequest;
import com.ptithcm2021.laptopshop.model.dto.response.CartResponse;
import com.ptithcm2021.laptopshop.model.entity.Cart;
import com.ptithcm2021.laptopshop.model.entity.CartId;
import com.ptithcm2021.laptopshop.model.entity.ProductDetail;
import com.ptithcm2021.laptopshop.model.entity.User;
import com.ptithcm2021.laptopshop.repository.CartRepository;
import com.ptithcm2021.laptopshop.repository.ProductDetailRepository;
import com.ptithcm2021.laptopshop.repository.UserRepository;
import com.ptithcm2021.laptopshop.service.CartService;
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
    @Override
    public CartResponse addCart(CartRequest cartRequest) {
        ProductDetail product = productDetailRepository.findById(cartRequest.getProductDetailId())
                .orElseThrow(() ->  new AppException(ErrorCode.PRODUCT_NOT_FOUND));

        User user = userRepository.findById(cartRequest.getUserId())
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
                    .quantity(cartRequest.getQuantity())
                    .build();
        } else cart.setQuantity(cart.getQuantity() + cartRequest.getQuantity());

        return cartMapper.toCartResponse(cartRepository.save(cart));
    }

    @Override
    public CartResponse updateCart(CartRequest cartRequest) {
        CartId cartId = CartId.builder()
                .productDetailId(cartRequest.getProductDetailId())
                .userId(cartRequest.getUserId())
                .build();

        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        cart.setQuantity(cartRequest.getQuantity());

        return cartMapper.toCartResponse(cartRepository.save(cart));
    }

    @Override
    public void removeCart(long productDetailId, String userId) {
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
    public CartResponse getCart(long productDetailId, String userId) {
        CartId cartId = CartId.builder()
                .productDetailId(productDetailId)
                .userId(userId)
                .build();
        Cart cart = cartRepository.findById(cartId)
                .orElseThrow(() -> new AppException(ErrorCode.CART_NOT_FOUND));

        return cartMapper.toCartResponse(cart);
    }

    @Override
    public List<CartResponse> getCartList(String userId) {
        return cartRepository.findAllByUserId(userId).stream()
                .map(cartMapper::toCartResponse).collect(Collectors.toList());
    }
}
