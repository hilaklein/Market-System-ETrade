package com.workshop.ETrade.Domain.Stores;

import com.workshop.ETrade.Controller.Forms.ComponentPredicateForm;
import com.workshop.ETrade.Controller.Forms.PredicateForm;
import com.workshop.ETrade.Domain.Stores.Discounts.DiscountType;
import com.workshop.ETrade.Domain.Stores.Policies.PolicyType;
import com.workshop.ETrade.Domain.Stores.Predicates.OperatorLeaf;
import com.workshop.ETrade.Domain.Stores.Predicates.PredicateBuilder;
import com.workshop.ETrade.Domain.Users.CreditCard;
import com.workshop.ETrade.Domain.Users.SupplyAddress;
import com.workshop.ETrade.Domain.Users.User;
import com.workshop.ETrade.Domain.purchaseOption;
import com.workshop.ETrade.Persistance.Stores.StoreDTO;
import com.workshop.ETrade.AllRepos;

import java.util.*;
import java.util.logging.FileHandler;
import java.util.logging.Handler;
import java.util.logging.Logger;

public class StoresFacade {
    private Logger logger = Logger.getLogger("stores");
    private List<Store> stores;

    public StoresFacade(){
        stores = Collections.synchronizedList(new ArrayList<Store>());
        try {
            Handler fileHandler = new FileHandler(System.getProperty("user.dir") + "/storesLog/stores.log", 2000, 5);
            logger.addHandler(fileHandler);
        } catch (Exception e) {
            System.out.println("error while creating logger for stores");
        }
    }

    public void init() {
        long i = AllRepos.getStoreRepo().count();
        List<StoreDTO> dtos = AllRepos.getStoreRepo().findAll();
        for(StoreDTO storeDTO : dtos)  {
            stores.add(new Store(storeDTO));
        }
    }

    public int addPolicy(String userName, String storeName, String policyOn, String description, PolicyType policyType, List<PredicateForm> predicateForms, String connectionType){
        Store store = getStoreByName(storeName);
        if(store == null) {
            return -1;
        }
        List<com.workshop.ETrade.Domain.Stores.Predicates.Predicate> pres = new LinkedList<>();
        for(PredicateForm p: predicateForms) {
            switch (p.predicateType){
                case "amount":
                    pres.add(PredicateBuilder.getProductAmountPredicate(p.preProduct, (int)p.minAmount, (int)p.maxAmount));
                    break;
                case "basket":
                    pres.add(PredicateBuilder.getBasketValuePredicate(p.minAmount, p.maxAmount));
                    break;
                case "time":
                    pres.add(PredicateBuilder.getTimePredicate(p.startTime, p.endTime, true));
                    break;
                default:
                    break;
            }
        }
        OperatorLeaf ol = new OperatorLeaf(connectionType, pres);
        int ans = store.addPolicy(userName,policyOn, description, policyType, ol);
        AllRepos.getStoreRepo().save(new StoreDTO(store));
        return ans;
    }
    public boolean addStore(String storeName, String founderName,int card) {
        Store store = getStoreByName(storeName);
        if(store != null) {
            return false;
        }
        Store newStore = new Store(storeName, founderName, card);
        stores.add(newStore);
        AllRepos.getStoreRepo().save(new StoreDTO(newStore));
        logger.info("store - " + storeName + "added by - " + founderName);
        return true;
    }

    public String displayAllStores() {
        String result = "";
        for(Store store : stores) {
            result = result + store.toString();
        }
        return result;
    }

    public boolean adminCloseStore(String storeName) {
        Store store = getStoreByName(storeName);
        if(store != null) {
            if(store.adminCloseStore()) {
                logger.info("store - " + storeName + "closed by admin");
                return true;
            }
            AllRepos.getStoreRepo().save(new StoreDTO(store));
        }
        return false;
    }

    public boolean canAddProduct(String productName, String storeName, int quantity) {
        Store store = getStoreByName(storeName);
        return store.canAddProduct(productName, quantity);
    }

    public boolean deleteStore(String storeName) {
        Store store = getStoreByName(storeName);
        if(store == null) {
            return false;
        }
        stores.remove(store);
        AllRepos.getStoreRepo().delete(new StoreDTO(store));
        return true;
    }

    public List<String> searchByKeyword(String keyword) {
        List<String> result = new LinkedList<>();
        for(Store store : stores) {
            result.add(store.searchByKeyword(keyword));
        }
        return result;
    }

    public List<String> searchByName(String name) {
        List<String> ans = new LinkedList<>();
        for(Store store : stores) {
            ans.add(store.searchByName(name));
        }
        return ans;
    }

    public List<String> searchByCategory(String category) {
        List<String> result = new LinkedList<>();
        for(Store store : stores) {
            result.addAll(store.searchByCategory(category));
        }
        return result;
    }

    private Store getStoreByName(String storeName) {
        for(Store store : stores) {
            if(store.getName().equals(storeName)) {
                return store;
            }
        }
        return null;
    }

    public List<Product> displayStore(String storeName) {
        Store store = getStoreByName(storeName);
        if(store != null && !store.isClosed()) {
            return store.getProducts();
        }
        else {
            return null;
        }
    }

    public boolean addProductToStore(String ownerName, String storeName, String productName, int amount, double price, String category) {
        Store store = getStoreByName(storeName);
        if(store == null) {
            return false;
        }
        if(store.addProduct(ownerName,productName,amount,price,category)) {
            AllRepos.getStoreRepo().save(new StoreDTO(store));
            logger.info("product - " + productName + " was added to store");
            return true;
        }
        return false;
    }

    public boolean removeProductFromStore(String userName, String storeName, String productName) {
        Store store = getStoreByName(storeName);
        if(store == null) {
            return false;
        }
        if(store.removeProduct(userName ,productName)) {
            logger.info("product - " + productName + " was removed from store");
            AllRepos.getStoreRepo().save(new StoreDTO(store));
            return true;
        }
        return false;
    }

    public boolean editProductName(String userName, String storeName, String oldProductName, String newProductName) {
        Store store = getStoreByName(storeName);
        if(store == null) {
            return false;
        }
        if(store.changeProductName(userName ,oldProductName, newProductName)) {
            logger.info("product - " + oldProductName + " is now called - " + newProductName + " in store - " + storeName);
        }
        return false;
    }

    public boolean editProductPrice(String userName, String storeName, String productName, double newPrice) {
        Store store = getStoreByName(storeName);
        if(store == null) {
            return false;
        }
        if(store.changeProductPrice(userName ,productName, newPrice)) {
            logger.info("product - " + productName + " now costs - " + newPrice + " in store - " + storeName);
            return true;
        }
        return false;
    }

    public boolean editProductQuantity(String userName, String storeName, String productName, int newQuantity) {
        Store store = getStoreByName(storeName);
        if(store == null) {
            return false;
        }
        return store.changeProductQuantity(userName ,productName, newQuantity);
    }

    public boolean changePurchaseOption(String userName, String storeName, String productName, purchaseOption newOption) {
        Store store = getStoreByName(storeName);
        if(store == null) {
            return false;
        }
        if(store.setPurchaseOption(userName, productName ,newOption)) {
            logger.info("new purchase option for store - " + storeName);
            return true;
        }
        return false;
    }

    public String appointStoreOwner(String userName, String storeName, String newOwner) {
        Store store = getStoreByName(storeName);
        if(store == null) {
            return "no such store " + storeName;
        }
        String ans = store.addOwner(userName, newOwner);
        if(ans.equals(newOwner + " has been added as store owner")) {
            logger.info("new store owner for store - " + storeName);
            AllRepos.getStoreRepo().save(new StoreDTO(store));
        }
        return ans;
    }

    public String approveOwner(String ownersName,String storeName, String nameToApprove, boolean approve) {
        Store store = getStoreByName(storeName);
        if(store == null) {
            return "no such store";
        }
        return store.approveOwner(ownersName,nameToApprove, approve);
    }

    public boolean appointStoreManager(String userName, String storeName, String newManager) {
        Store store = getStoreByName(storeName);
        if(store == null) {
            return false;
        }
        if(store.addManager(userName, newManager)) {
            logger.info("new store manager for store - " + storeName);
            AllRepos.getStoreRepo().save(new StoreDTO(store));
            return true;
        }
        return false;
    }
    public Store getStore(String storeName){
        for(Store s : stores){
            if(s.getName().equals(storeName))
                return s;
        }
        return null;
    }
    public int getProductAmount(String storeName,String prodName){
        return getStore(storeName).getProductAmount(prodName);
    }

    public String getStorePurchaseHistory(String userName, String storeName) {
        Store store = getStoreByName(storeName);
        if(store != null) {
            return store.getHistory(userName);
        }
        return null;
    }

    public String adminGetStorePurchaseHistory(String storeName) {
        Store store = getStoreByName(storeName);
        if(store != null) {
            return store.getHistory();
        }
        return null;
    }

    public Set<String> getStoresManagement(String userName, String storeName) {
        Store store = getStoreByName(storeName);
        if(store != null) {
            return store.getAllManagement(userName);
        }
        return null;
    }

    public boolean closeStore(String storeName, String userName) {
        Store store = getStoreByName(storeName);
        if(store != null) {
            if(store.closeStore(userName)) {
                logger.info("store - " + storeName + " is now closed");
                AllRepos.getStoreRepo().save(new StoreDTO(store));
                return true;
            }
        }
        return false;
    }

    public boolean changeStoreManagersPermission(String userName, String storeName, String managerName, managersPermission newPermission) {
        Store store = getStoreByName(storeName);
        if(store != null) {
            if(store.changeStoreManagersPermission(userName, managerName, newPermission)) {
                logger.info("changed permission for manger - " + managerName);
                AllRepos.getStoreRepo().save(new StoreDTO(store));
                return true;
            }
        }
        return false;
    }

    public boolean addKeyword(String userName, String storeName, String productName, String keyWord) {
        Store store = getStoreByName(storeName);
        if(store != null) {
            return store.addKeywordToProduct(userName, productName, keyWord);
        }
        return false;
    }
    public int addDiscount(String userName,String store,String discountOn, int discountPercentage, String description, DiscountType discountType){
        Store s = getStoreByName(store);
        if(s == null)
            return -1;
        int ans = s.addDiscount(userName,discountOn, discountPercentage, description, discountType);
        AllRepos.getStoreRepo().save(new StoreDTO(s));
        return ans;
    }

    public List<Store> getStores() {
        return stores;
    }

    public List<String> getStoresOfUser(String userName) {
        List<String> res = new LinkedList<>();
        for(Store store : stores) {
            Set<String> management = store.getAllManagement(userName);
            if(management != null && management.contains(userName)) {
                res.add(store.getName());
            }
        }
        return res;
    }
    public int getProdQuantity(String store,String prod){
        Store s = getStore(store);
        if(s == null)
            return -1;
        return s.getProductAmount(prod);
    }

    public boolean removeStoreOwner(String userName, String storeName, String ownerToRemove) {
        Store store = getStoreByName(storeName);
        if(store == null) {
            return false;
        }
        if(store.removeOwner(userName, ownerToRemove)) {
            AllRepos.getStoreRepo().save(new StoreDTO(store));
            return true;
        }
        return false;
    }

    public boolean removeStoreManager(String userName, String storeName, String managerToRemove) {
        Store store = getStoreByName(storeName);
        if(store == null) {
            return false;
        }
        if(store.removeManager(userName, managerToRemove)) {
            AllRepos.getStoreRepo().save(new StoreDTO(store));
            return true;
        }
        return false;
    }

    public int addPreDiscount(String userName, String storeName, String discountOn, int discountPercentage, String description, DiscountType discountType, List<PredicateForm> predicateForms, String connectionType) {
        Store store = getStoreByName(storeName);
        if(store == null) {
            return -1;
        }
        List<com.workshop.ETrade.Domain.Stores.Predicates.Predicate> pres = new LinkedList<>();
        for(PredicateForm p: predicateForms) {
            switch (p.predicateType){
                case "amount":
                    pres.add(PredicateBuilder.getProductAmountPredicate(p.preProduct, (int)p.minAmount, (int)p.maxAmount));
                    break;
                case "basket":
                    pres.add(PredicateBuilder.getBasketValuePredicate(p.minAmount, p.maxAmount));
                    break;
                case "time":
                    pres.add(PredicateBuilder.getTimePredicate(p.startTime, p.endTime, true));
                    break;
                default:
                    break;
            }
        }
        OperatorLeaf ol = new OperatorLeaf(connectionType, pres);
        int ans = store.addPredicateDiscount(discountOn, discountPercentage, description,discountType, ol);
        AllRepos.getStoreRepo().save(new StoreDTO(store));
        return ans;
    }

    public boolean addBid(String userName, String storeName, String productName, double bidAmount, CreditCard creditCard, SupplyAddress supplyAddress) {
        Store store = getStoreByName(storeName);
        if(store == null) {
            return false;
        }
        boolean res = store.addBid(productName, bidAmount, userName, creditCard, supplyAddress, storeName);
        if(res) {
            AllRepos.getStoreRepo().save(new StoreDTO(store));
            return true;
        }
        return false;
    }

    public List<Bid> getStoreBids(String storeName) {
        Store store = getStoreByName(storeName);
        if(store == null) {
            return null;
        }
        return store.getBids();
    }

    public Bid reviewBid(String userName, String storeName, int bidId, boolean approve) {
        Store store = getStoreByName(storeName);
        if(store == null) {
            return null;
        }
        AllRepos.getStoreRepo().save(new StoreDTO(store));
        return store.reviewBid(userName, bidId, approve);
    }

    public boolean counterBid(String storeName, int bidId, double newOffer) {
        Store store = getStoreByName(storeName);
        if(store == null) {
            return false;
        }
        AllRepos.getStoreRepo().save(new StoreDTO(store));
        return store.counterBid(bidId, newOffer);
    }

    public void purchaseBid(String storeName, String productName, User user) {
        Store store = getStoreByName(storeName);
        if(store == null) {
            return ;
        }
        HashMap<String, Integer> prods = new HashMap<>();
        prods.put(productName, 1);
        store.purchaseBid(prods,user);
        AllRepos.getStoreRepo().save(new StoreDTO(store));
    }

    public List<Bid> userBids(String userName) {
        List<Bid> bids = new LinkedList<>();
        for(Store store : stores) {
            bids.addAll(store.userBids(userName));
        }
        return bids;
    }

    public Bid counterBidReview(String storeName, int bidId, boolean approve) {
        Store store = getStoreByName(storeName);
        if(store == null) {
            return null;
        }
        AllRepos.getStoreRepo().save(new StoreDTO(store));
        return store.counterBidReview(bidId, approve);
    }

    public int addComplexDiscount(String userName, String storeName, String discountOn, int discountPercentage, String description, DiscountType discountType, ComponentPredicateForm predicateForm, String connectionType) {
        Store store = getStoreByName(storeName);
        if(store == null) {
            return -1;
        }
        int ans = store.addComplexDiscount(discountOn, discountPercentage, description,discountType, predicateForm.getComponent());
        AllRepos.getStoreRepo().save(new StoreDTO(store));
        return ans;
    }

    public int addComplexPolicy(String userName, String storeName, String policyOn, String description, PolicyType policyType, ComponentPredicateForm predicateForms, String connectionType) {
        Store store = getStoreByName(storeName);
        if(store == null) {
            return -1;
        }
        int ans = store.addPolicy(userName,policyOn, description, policyType, predicateForms.getComponent());
        AllRepos.getStoreRepo().save(new StoreDTO(store));
        return ans;
    }

    public Map<String, Map<String, Boolean>> getOwnersWaitingForApprove(String userName, String storeName) {
        Store store = getStoreByName(storeName);
        if(store == null) {
            return null;
        }
        return store.getOwnersWaitingForApprove();
    }
}
