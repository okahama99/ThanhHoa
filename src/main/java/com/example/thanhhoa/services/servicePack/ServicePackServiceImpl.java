package com.example.thanhhoa.services.servicePack;

import com.example.thanhhoa.dtos.ServicePackModels.ShowServicePackModel;
import com.example.thanhhoa.entities.ServicePack;
import com.example.thanhhoa.enums.Status;
import com.example.thanhhoa.repositories.ServicePackRepository;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.List;

@Service
public class ServicePackServiceImpl implements ServicePackService {

    @Autowired
    private Util util;
    @Autowired
    private ServicePackRepository servicePackRepository;

    @Override
    public String create(String range, String unit, Integer percentage) {
        ServicePack servicePack = new ServicePack();
        ServicePack lastServicePack = servicePackRepository.findFirstByOrderByIdDesc();
        if(lastServicePack == null) {
            servicePack.setId(util.createNewID("SPK"));
        } else {
            servicePack.setId(util.createIDFromLastID("SPK", 3, lastServicePack.getId()));
        }
        servicePack.setRange(range);
        servicePack.setPercentage(percentage);
        servicePack.setUnit(unit);
        servicePack.setApplyDate(LocalDateTime.now(ZoneId.of("Asia/Ho_Chi_Minh")));
        servicePack.setStatus(Status.ACTIVE);
        servicePackRepository.save(servicePack);
        return "Tạo thành công.";
    }

    @Override
    public String delete(String id) {
        ServicePack servicePack = servicePackRepository.findByIdAndStatus(id, Status.ACTIVE);
        if(servicePack == null){
            return "Không tìm thấy Gói dịch vụ với ID là " + id + " đang được áp dụng.";
        }
        servicePack.setStatus(Status.INACTIVE);
        return "Xóa thành công.";
    }

    @Override
    public List<ShowServicePackModel> getAll() {
        List<ServicePack> servicePacks = servicePackRepository.findByStatus(Status.ACTIVE);
        if(servicePacks == null){
            return null;
        }
        List<ShowServicePackModel> listModel = new ArrayList<>();
        for(ServicePack servicePack : servicePacks){
            ShowServicePackModel model = new ShowServicePackModel();
            model.setId(servicePack.getId());
            model.setRange(servicePack.getRange());
            model.setUnit(servicePack.getUnit());
            model.setPercentage(servicePack.getPercentage());
            model.setApplyDate(servicePack.getApplyDate());
            model.setStatus(servicePack.getStatus());
            listModel.add(model);
        }
        return listModel;
    }
}
