package com.ptithcm2021.laptopshop.service.impl;

import com.ptithcm2021.laptopshop.exception.AppException;
import com.ptithcm2021.laptopshop.exception.ErrorCode;
import com.ptithcm2021.laptopshop.model.dto.request.AddressRequest;
import com.ptithcm2021.laptopshop.model.dto.response.AddressResponse;
import com.ptithcm2021.laptopshop.model.entity.Address;
import com.ptithcm2021.laptopshop.model.entity.User;
import com.ptithcm2021.laptopshop.repository.AddressRepository;
import com.ptithcm2021.laptopshop.repository.UserRepository;
import com.ptithcm2021.laptopshop.service.AddressService;
import com.ptithcm2021.laptopshop.util.FetchUserIdUtil;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class AddressServiceImpl implements AddressService {
    private final AddressRepository addressRepository;
    private final UserRepository userRepository;

    @Override
    public AddressResponse getAddress(Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));

        return AddressResponse.builder()
                .id(address.getId())
                .address(address.getAddress())
                .phone(address.getPhone())
                .recipient(address.getRecipient())
                .isDefault(address.isDefault())
                .build();
    }

    @Override
    public List<AddressResponse> getListAddress() {
        String userId = FetchUserIdUtil.fetchUserId();
        List<Address> addresses = addressRepository.findAllByUserId(userId);

        return addresses.stream().map(address -> {return AddressResponse.builder()
                .id(address.getId())
                .address(address.getAddress())
                .phone(address.getPhone())
                .recipient(address.getRecipient())
                .isDefault(address.isDefault())
                .build();})
                .toList();
    }

    @Override
    @Transactional
    public AddressResponse addAddress(AddressRequest request) {
        String userId = FetchUserIdUtil.fetchUserId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new AppException(ErrorCode.USER_NOT_FOUND));

        if(request.isDefault()){
            addressRepository.clearDefaultForUser(userId);
        }

        Address address = Address.builder()
                .user(user)
                .address(request.getAddress())
                .phone(request.getPhone())
                .recipient(request.getRecipient())
                .isDefault(request.isDefault())
                .build();

        addressRepository.save(address);

        return AddressResponse.builder()
                .id(address.getId())
                .address(address.getAddress())
                .phone(address.getPhone())
                .recipient(address.getRecipient())
                .isDefault(address.isDefault())
                .build();
    }

    @Override
    public List<Address> addListAddress(List<AddressRequest> request, User user) {
        addressRepository.deleteAll(user.getAddress());
        user.getAddress().clear();
        List<Address> addresses = new ArrayList<>();
        request.forEach(address -> {
            Address temp = Address.builder()
                    .user(user)
                    .address(address.getAddress())
                    .phone(address.getPhone())
                    .recipient(address.getRecipient())
                    .isDefault(address.isDefault())
                    .build();
            addresses.add(temp);
        });


        addressRepository.saveAll(addresses);
        return addresses;
    }

    @Override
    @Transactional
    public AddressResponse updateAddress(AddressRequest request, Long id) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new AppException(ErrorCode.ADDRESS_NOT_FOUND));

        if(request.isDefault()){
            addressRepository.clearDefaultForUser(address.getUser().getId());
        }

        address.setAddress(request.getAddress());
        address.setPhone(request.getPhone());
        address.setRecipient(request.getRecipient());
        address.setDefault(request.isDefault());
        addressRepository.save(address);

        return AddressResponse.builder()
                .id(address.getId())
                .address(address.getAddress())
                .phone(address.getPhone())
                .recipient(address.getRecipient())
                .isDefault(address.isDefault())
                .build();
    }

    @Override
    @Transactional
    public void setDefaultAddress(Long id) {
        String userId = FetchUserIdUtil.fetchUserId();
        addressRepository.clearDefaultForUser(userId);
        addressRepository.setDefault(id);
    }

    @Override
    public void deleteAddress(Long id) {
        if(!addressRepository.existsById(id)) {
            throw new AppException(ErrorCode.ADDRESS_NOT_FOUND);
        }

        try {
            addressRepository.deleteById(id);
        }catch (Exception e) {
            log.warn(e.getMessage());
            throw new AppException((ErrorCode.CANNOT_DELETE));
        }
    }
}
