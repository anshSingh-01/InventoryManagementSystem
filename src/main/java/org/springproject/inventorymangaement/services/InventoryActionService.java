package org.springproject.inventorymangaement.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springproject.inventorymangaement.dtos.DtoImpl;
import org.springproject.inventorymangaement.dtos.InventoryActionDto;
import org.springproject.inventorymangaement.dtos.StatusSender;
import org.springproject.inventorymangaement.entity.InventoryAction;
import org.springproject.inventorymangaement.entity.InventoryLedger;
import org.springproject.inventorymangaement.enums.StatusCode;
import org.springproject.inventorymangaement.repositoryimpl.InventoryActionRepositoryImpl;
import org.springproject.inventorymangaement.users.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class InventoryActionService implements DtoImpl<InventoryAction,InventoryActionDto> {

    @Autowired
    InventoryLedgerService inventoryLedgerService;

    @Autowired
    UserService userService;

    @Autowired
    InventoryActionRepositoryImpl inventoryActionRepository;

    public StatusSender addAction(InventoryActionDto inventoryActionDto){

                InventoryAction inventoryAction = DtoToEntity(inventoryActionDto);
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

    @Override
    public InventoryAction DtoToEntity(InventoryActionDto inventoryActionDto) {

        UUID user_id = inventoryActionDto.getUser_id();
        UUID log_id = inventoryActionDto.getLog_id();
        User user = null;
        if (user_id != null) user = userService.getUserById(user_id);

        // Fetch the managed persistent Entity directly from the database session:
        InventoryLedger inventoryLedger = inventoryLedgerService.getLedgerEntityById(log_id);
        return new InventoryAction(inventoryLedger, user);
    }

    @Override
    public InventoryActionDto EntityToDto(InventoryAction inventoryAction) {
        return null;
    }
}
