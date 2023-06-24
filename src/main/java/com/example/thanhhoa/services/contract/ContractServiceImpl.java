package com.example.thanhhoa.services.contract;

import com.example.thanhhoa.dtos.ContractModels.ShowContractDetailModel;
import com.example.thanhhoa.dtos.ContractModels.ShowContractModel;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ContractServiceImpl implements ContractService{

    @Override
    public List<ShowContractModel> getAllContractByUsername(String username, Pageable pageable) {
        return null;
    }

    @Override
    public List<ShowContractDetailModel> getContractDetailByContractID(String contractID) {
        return null;
    }
}
