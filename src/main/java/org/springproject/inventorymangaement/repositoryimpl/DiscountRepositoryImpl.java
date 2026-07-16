package org.springproject.inventorymangaement.repositoryimpl;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springproject.inventorymangaement.entity.Discount;

import java.util.List;
import java.util.UUID;

public interface DiscountRepositoryImpl extends JpaRepository<Discount, UUID> {

    @Query("select d.sku.id , d.discount from Discount d where d.active=true")
    List<Object[]> getDiscountDetails();
    @Modifying
    @Query("delete from Discount d where d.active=false")
    void deleteInactiveDiscounts();

    @Query("select d from Discount d where d.sku.id=?1")
    Discount getDiscountBySkuId(UUID sku_id);

}
