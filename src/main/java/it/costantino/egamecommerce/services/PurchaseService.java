package it.costantino.egamecommerce.services;

import it.costantino.egamecommerce.dto.PurchaseDTO;
import it.costantino.egamecommerce.dto.PurchasedGameDTO;
import it.costantino.egamecommerce.entities.Purchase;
import it.costantino.egamecommerce.entities.PurchasedGame;
import it.costantino.egamecommerce.entities.User;
import it.costantino.egamecommerce.repositories.PurchaseRepository;
import it.costantino.egamecommerce.repositories.PurchasedGameRepository;
import it.costantino.egamecommerce.repositories.UserRepository;
import it.costantino.egamecommerce.support.exception.UsernameNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedList;
import java.util.List;

@Service
public class PurchaseService {
    @Autowired
    private UserRepository userRep;
    @Autowired
    private PurchaseRepository purchaseRep;
    @Autowired
    private PurchasedGameRepository purchasedGameRep;

    @Transactional(isolation= Isolation.READ_COMMITTED, readOnly = true)
    public List<PurchaseDTO> getAllPurchasesByUser(String username){
        if (!userRep.existsByUsername(username)) throw new UsernameNotFoundException();
        User user=userRep.findByUsername(username);
        List<PurchaseDTO> l=new LinkedList<>();
        for(Purchase purchase: purchaseRep.findAllByBuyer(user)){
            PurchaseDTO purchaseDTO=new PurchaseDTO(purchase);
            List<PurchasedGame> pgs=purchasedGameRep.findAllByRelatedPurchase(purchase);
            for (PurchasedGame pg: pgs)
                purchaseDTO.addPurchasedGameDTO(new PurchasedGameDTO(pg));
            l.add(purchaseDTO);
        }
        return l;
    }//getAllBookingsByUser


}