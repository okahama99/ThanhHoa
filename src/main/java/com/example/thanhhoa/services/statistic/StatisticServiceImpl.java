package com.example.thanhhoa.services.statistic;

import com.example.thanhhoa.dtos.StatisticModels.ShowStatisticModel;
import com.example.thanhhoa.dtos.StatisticModels.StoreContractModel;
import com.example.thanhhoa.dtos.StatisticModels.StoreOrderModel;
import com.example.thanhhoa.dtos.StoreModels.ShowStoreModel;
import com.example.thanhhoa.entities.Store;
import com.example.thanhhoa.entities.StoreEmployee;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.ContractRepository;
import com.example.thanhhoa.repositories.OrderRepository;
import com.example.thanhhoa.repositories.StoreEmployeeRepository;
import com.example.thanhhoa.repositories.StoreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
public class StatisticServiceImpl implements StatisticService {

    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private OrderRepository orderRepository;
    @Autowired
    private StoreRepository storeRepository;
    @Autowired
    private StoreEmployeeRepository storeEmployeeRepository;

    @Override
    public ShowStatisticModel getStatistic(String storeID, LocalDateTime fromDate, LocalDateTime toDate) {
        // contract
        Integer numOfCWorking = contractRepository.countContractByStatusAndCreatedDateBetween(Status.WORKING, fromDate, toDate);
        Integer numOfCDone = contractRepository.countContractByStatusAndCreatedDateBetween(Status.DONE, fromDate, toDate);
        Integer numOfContract = numOfCWorking + numOfCDone;
        String sumOfContract = null;
        Double sumTotalContract = contractRepository.sumTotal(fromDate, toDate);
        if(sumTotalContract != null) {
            sumOfContract = String.format("%.2f", sumTotalContract);
        }


        Integer numOfStoreCWorking = 0;
        Integer numOfStoreCDone = 0;
        Integer numOfStoreContract = 0;
        String sumOfStoreContract = null;

        // order
        Integer numOfApproved = orderRepository.countByProgressStatusAndCreatedDateBetween(Status.APPROVED, fromDate, toDate);
        Integer numOfPackaging = orderRepository.countByProgressStatusAndCreatedDateBetween(Status.PACKAGING, fromDate, toDate);
        Integer numOfDelivering = orderRepository.countByProgressStatusAndCreatedDateBetween(Status.DELIVERING, fromDate, toDate);
        Integer numOfReceived = orderRepository.countByProgressStatusAndCreatedDateBetween(Status.RECEIVED, fromDate, toDate);
        Integer numOfOrder = numOfApproved + numOfPackaging + numOfDelivering + numOfReceived;
        Double sumOfTotalOrder = orderRepository.sumTotal(fromDate, toDate);
        String sumOfOrder = null;
        if(sumOfTotalOrder != null) {
            sumOfOrder = String.format("%.2f", sumOfTotalOrder);
        }

        Integer numOfStoreOApproved = 0;
        Integer numOfStoreOPackaging = 0;
        Integer numOfStoreODelivering = 0;
        Integer numOfStoreOReceived = 0;
        Integer numOfStoreOrder = 0;
        String sumOfStoreOrder = null;

        ShowStoreModel model = new ShowStoreModel();

        if(storeID != null) {
            // contract
            numOfStoreCWorking = contractRepository.countContractByStore_IdAndStatusAndCreatedDateBetween(storeID, Status.WORKING, fromDate, toDate);
            numOfStoreCDone = contractRepository.countContractByStore_IdAndStatusAndCreatedDateBetween(storeID, Status.DONE, fromDate, toDate);
            numOfStoreContract = numOfStoreCWorking + numOfStoreCDone;
            Double sumOfTotalStoreContract = contractRepository.sumTotalOfAStore(storeID, fromDate, toDate);
            if(sumOfTotalStoreContract != null) {
                sumOfStoreContract = String.format("%.2f", sumOfTotalStoreContract);
            }

            //order
            numOfStoreOApproved = orderRepository.countByStore_IdAndProgressStatusAndCreatedDateBetween(storeID, Status.APPROVED, fromDate, toDate);
            numOfStoreOPackaging = orderRepository.countByStore_IdAndProgressStatusAndCreatedDateBetween(storeID, Status.PACKAGING, fromDate, toDate);
            numOfStoreODelivering = orderRepository.countByStore_IdAndProgressStatusAndCreatedDateBetween(storeID, Status.DELIVERING, fromDate, toDate);
            numOfStoreOReceived = orderRepository.countByStore_IdAndProgressStatusAndCreatedDateBetween(storeID, Status.RECEIVED, fromDate, toDate);
            numOfStoreOrder = numOfStoreOApproved + numOfStoreOPackaging + numOfStoreODelivering + numOfStoreOReceived;
            Double sumOfTotalStoreOrder = orderRepository.sumTotalOfAStore(storeID, fromDate, toDate);
            if(sumOfTotalStoreOrder != null) {
                sumOfStoreOrder = String.format("%.2f", sumOfTotalStoreOrder);
            }

            // store
            Store store = storeRepository.getById(storeID);
            StoreEmployee manager = storeEmployeeRepository.findByStore_IdAndAccount_Role_RoleName(storeID, "Manager");

            model.setId(store.getId());
            model.setStatus(store.getStatus());
            model.setStoreName(store.getStoreName());
            model.setAddress(store.getAddress());
            model.setDistrictID(store.getDistrict().getId());
            model.setDistrictName(store.getDistrict().getDistrictName());
            model.setPhone(store.getPhone());
            if(manager != null) {
                model.setManagerID(manager.getAccount().getId());
                model.setManagerName(manager.getAccount().getFullName());
            }
        }

        StoreContractModel contractModel = new StoreContractModel();
        contractModel.setNumOfWorking(numOfStoreCWorking);
        contractModel.setNumOfDone(numOfStoreCDone);
        contractModel.setNumOfContract(numOfStoreContract);
        contractModel.setSumOfContract(sumOfStoreContract);

        StoreOrderModel orderModel = new StoreOrderModel();
        orderModel.setNumOfApproved(numOfStoreOApproved);
        orderModel.setNumOfPackaging(numOfStoreOPackaging);
        orderModel.setNumOfDelivering(numOfStoreODelivering);
        orderModel.setNumOfReceived(numOfStoreOReceived);
        orderModel.setNumOfOrder(numOfStoreOrder);
        orderModel.setSumOfOrder(sumOfStoreOrder);

        ShowStatisticModel statisticModel = new ShowStatisticModel();
        statisticModel.setNumOfContractWorking(numOfCWorking);
        statisticModel.setNumOfContractDone(numOfCDone);
        statisticModel.setNumOfOrderApproved(numOfApproved);
        statisticModel.setNumOfOrderPackaging(numOfPackaging);
        statisticModel.setNumOfOrderDelivering(numOfDelivering);
        statisticModel.setNumOfOrderReceived(numOfReceived);
        statisticModel.setNumOfContract(numOfContract);
        statisticModel.setNumOfOrder(numOfOrder);
        statisticModel.setSumOfContract(sumOfContract);
        statisticModel.setSumOfOrder(sumOfOrder);
        statisticModel.setShowStoreModel(model);
        statisticModel.setStoreContractModel(contractModel);
        statisticModel.setStoreOrderModel(orderModel);

        return statisticModel;
    }

    @Override
    public List<ShowStatisticModel> getStatisticOfAllStore(LocalDateTime fromDate, LocalDateTime toDate) {
        List<Store> storeList = storeRepository.findAll();
        List<ShowStatisticModel> modelList = new ArrayList<>();
        if(storeList != null && !storeList.isEmpty()) {
            for(Store store : storeList) {
                // contract
                Integer numOfCWorking = contractRepository.countContractByStatusAndCreatedDateBetween(Status.WORKING, fromDate, toDate);
                Integer numOfCDone = contractRepository.countContractByStatusAndCreatedDateBetween(Status.DONE, fromDate, toDate);
                Integer numOfContract = numOfCWorking + numOfCDone;
                String sumOfContract = null;
                Double sumTotalContract = contractRepository.sumTotal(fromDate, toDate);
                if(sumTotalContract != null) {
                    sumOfContract = String.format("%.2f", sumTotalContract);
                }

                // order
                Integer numOfApproved = orderRepository.countByProgressStatusAndCreatedDateBetween(Status.APPROVED, fromDate, toDate);
                Integer numOfPackaging = orderRepository.countByProgressStatusAndCreatedDateBetween(Status.PACKAGING, fromDate, toDate);
                Integer numOfDelivering = orderRepository.countByProgressStatusAndCreatedDateBetween(Status.DELIVERING, fromDate, toDate);
                Integer numOfReceived = orderRepository.countByProgressStatusAndCreatedDateBetween(Status.RECEIVED, fromDate, toDate);
                Integer numOfOrder = numOfApproved + numOfPackaging + numOfDelivering + numOfReceived;
                Double sumOfTotalOrder = orderRepository.sumTotal(fromDate, toDate);
                String sumOfOrder = null;
                if(sumOfTotalOrder != null) {
                    sumOfOrder = String.format("%.2f", sumOfTotalOrder);
                }

                Integer numOfStoreCWorking = 0;
                Integer numOfStoreCDone = 0;
                Integer numOfStoreContract = 0;
                String sumOfStoreContract = null;

                ShowStoreModel model = new ShowStoreModel();
                String storeID = store.getId();
                // contract
                numOfStoreCWorking = contractRepository.countContractByStore_IdAndStatusAndCreatedDateBetween(storeID, Status.WORKING, fromDate, toDate);
                numOfStoreCDone = contractRepository.countContractByStore_IdAndStatusAndCreatedDateBetween(storeID, Status.DONE, fromDate, toDate);
                numOfStoreContract = numOfStoreCWorking + numOfStoreCDone;
                Double sumOfTotalStoreContract = contractRepository.sumTotalOfAStore(storeID, fromDate, toDate);
                if(sumOfTotalStoreContract != null) {
                    sumOfStoreContract = String.format("%.2f", sumOfTotalStoreContract);
                }

                Integer numOfStoreOApproved = 0;
                Integer numOfStoreOPackaging = 0;
                Integer numOfStoreODelivering = 0;
                Integer numOfStoreOReceived = 0;
                Integer numOfStoreOrder = 0;
                String sumOfStoreOrder = null;

                //order
                numOfStoreOApproved = orderRepository.countByStore_IdAndProgressStatusAndCreatedDateBetween(storeID, Status.APPROVED, fromDate, toDate);
                numOfStoreOPackaging = orderRepository.countByStore_IdAndProgressStatusAndCreatedDateBetween(storeID, Status.PACKAGING, fromDate, toDate);
                numOfStoreODelivering = orderRepository.countByStore_IdAndProgressStatusAndCreatedDateBetween(storeID, Status.DELIVERING, fromDate, toDate);
                numOfStoreOReceived = orderRepository.countByStore_IdAndProgressStatusAndCreatedDateBetween(storeID, Status.RECEIVED, fromDate, toDate);
                numOfStoreOrder = numOfStoreOApproved + numOfStoreOPackaging + numOfStoreODelivering + numOfStoreOReceived;
                Double sumOfTotalStoreOrder = orderRepository.sumTotalOfAStore(storeID, fromDate, toDate);
                if(sumOfTotalStoreOrder != null) {
                    sumOfStoreOrder = String.format("%.2f", sumOfTotalStoreOrder);
                }

                // store
                StoreEmployee manager = storeEmployeeRepository.findByStore_IdAndAccount_Role_RoleName(storeID, "Manager");

                model.setId(store.getId());
                model.setStatus(store.getStatus());
                model.setStoreName(store.getStoreName());
                model.setAddress(store.getAddress());
                model.setDistrictID(store.getDistrict().getId());
                model.setDistrictName(store.getDistrict().getDistrictName());
                model.setPhone(store.getPhone());
                if(manager != null) {
                    model.setManagerID(manager.getAccount().getId());
                    model.setManagerName(manager.getAccount().getFullName());
                }

                StoreContractModel contractModel = new StoreContractModel();
                contractModel.setNumOfWorking(numOfStoreCWorking);
                contractModel.setNumOfDone(numOfStoreCDone);
                contractModel.setNumOfContract(numOfStoreContract);
                contractModel.setSumOfContract(sumOfStoreContract);

                StoreOrderModel orderModel = new StoreOrderModel();
                orderModel.setNumOfApproved(numOfStoreOApproved);
                orderModel.setNumOfPackaging(numOfStoreOPackaging);
                orderModel.setNumOfDelivering(numOfStoreODelivering);
                orderModel.setNumOfReceived(numOfStoreOReceived);
                orderModel.setNumOfOrder(numOfStoreOrder);
                orderModel.setSumOfOrder(sumOfStoreOrder);

                ShowStatisticModel statisticModel = new ShowStatisticModel();
                statisticModel.setNumOfContractWorking(numOfCWorking);
                statisticModel.setNumOfContractDone(numOfCDone);
                statisticModel.setNumOfOrderApproved(numOfApproved);
                statisticModel.setNumOfOrderPackaging(numOfPackaging);
                statisticModel.setNumOfOrderDelivering(numOfDelivering);
                statisticModel.setNumOfOrderReceived(numOfReceived);
                statisticModel.setNumOfContract(numOfContract);
                statisticModel.setNumOfOrder(numOfOrder);
                statisticModel.setSumOfContract(sumOfContract);
                statisticModel.setSumOfOrder(sumOfOrder);
                statisticModel.setShowStoreModel(model);
                statisticModel.setStoreContractModel(contractModel);
                statisticModel.setStoreOrderModel(orderModel);

                modelList.add(statisticModel);
            }

            return modelList;
        }
        return null;
    }
}
