package com.ptithcm2021.laptopshop.repository;

import com.ptithcm2021.laptopshop.model.entity.DeliveryNote;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface DeliveryNoteRepository extends JpaRepository<DeliveryNote, Long> {

    @Query("""
        select coalesce(sum (dnd.quantity), 0) from DeliveryNote dn join DeliveryNoteDetail dnd on dn.id = dnd .deliveryNote.id
        where dn.order.id = :orderId and dnd.productDetail.id =:productDetailId
""")
    int sumCountByOrderDetailId(Long productDetailId, Long orderId);

    int countByCodeStartingWith(String baseCode);

    Page<DeliveryNote> findAllByOrderCode(Pageable pageable, String orderId);

}
