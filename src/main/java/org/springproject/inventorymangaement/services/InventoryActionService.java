package org.springproject.inventorymangaement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.entity.InventoryAction;
import org.springproject.inventorymangaement.enums.StatusCode;
import org.springproject.inventorymangaement.repositoryimpl.InventoryActionRepositoryImpl;

import java.util.List;
import java.util.Optional;

@Service
public class InventoryActionService {

    @Autowired
    InventoryLedgerService inventoryLedgerService;

    @Autowired
    UserService userService;

    @Autowired
    InventoryActionRepositoryImpl inventoryActionRepository;

    public StatusSender addAction(InventoryAction inventoryAction){
                inventoryActionRepository.save(inventoryAction);
                return new StatusSender(StatusCode.SUCCESS ,"successfully added" ,inventoryAction);
    }

    public StatusSender addActions(List<InventoryAction> inventoryActions){
        inventoryActionRepository.saveAll(inventoryActions);
        return new StatusSender(StatusCode.SUCCESS ,"successfully added" ,inventoryActions);
    }

    public List<InventoryAction> getActions(){
            return inventoryActionRepository.findAll();
    }

    public Optional<InventoryAction> getActionsByEmail(String email){
        return inventoryActionRepository.findByUserEmail(email);
    }

}
