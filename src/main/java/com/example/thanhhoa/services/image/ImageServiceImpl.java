package com.example.thanhhoa.services.image;

import com.example.thanhhoa.entities.Contract;
import com.example.thanhhoa.entities.ContractIMG;
import com.example.thanhhoa.entities.OrderFeedback;
import com.example.thanhhoa.entities.OrderFeedbackIMG;
import com.example.thanhhoa.entities.Plant;
import com.example.thanhhoa.entities.PlantIMG;
import com.example.thanhhoa.entities.Service;
import com.example.thanhhoa.entities.ServiceIMG;
import com.example.thanhhoa.repositories.ContractIMGRepository;
import com.example.thanhhoa.repositories.ContractRepository;
import com.example.thanhhoa.repositories.OrderFeedbackIMGRepository;
import com.example.thanhhoa.repositories.OrderFeedbackRepository;
import com.example.thanhhoa.repositories.PlantIMGRepository;
import com.example.thanhhoa.repositories.PlantRepository;
import com.example.thanhhoa.repositories.ServiceIMGRepository;
import com.example.thanhhoa.repositories.ServiceRepository;
import com.example.thanhhoa.services.firebaseIMG.FirebaseImageService;
import com.example.thanhhoa.utils.Util;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Optional;

@org.springframework.stereotype.Service
public class ImageServiceImpl implements ImageService {

    @Autowired
    private ContractIMGRepository contractIMGRepository;
    @Autowired
    private PlantIMGRepository plantIMGRepository;
    @Autowired
    private OrderFeedbackIMGRepository orderFeedbackIMGRepository;
    @Autowired
    private ServiceIMGRepository serviceIMGRepository;
    @Autowired
    private FirebaseImageService firebaseImageService;
    @Autowired
    private PlantRepository plantRepository;
    @Autowired
    private ServiceRepository serviceRepository;
    @Autowired
    private ContractRepository contractRepository;
    @Autowired
    private OrderFeedbackRepository orderFeedbackRepository;
    @Autowired
    private Util util;

    @Override
    public String convertFileToImageURL(MultipartFile file) throws IOException {
        String fileName = firebaseImageService.save(file);
        String imgName = firebaseImageService.getImageUrl(fileName);
        return imgName;
    }

    @Override
    public String addImage(String entityName, String entityID, MultipartFile file) throws IOException {
        switch(entityName) {
            case "PLANT":
                Optional<Plant> plant = plantRepository.findById(entityID);
                if(plant == null) {
                    return "Không tìm thấy entity với ID là " + entityID + ".";
                }
                String pFileName = firebaseImageService.save(file);
                String imgPURL = firebaseImageService.getImageUrl(pFileName);
                PlantIMG plantIMG = new PlantIMG();
                PlantIMG lastPlantIMG = plantIMGRepository.findFirstByOrderByIdDesc();
                if(lastPlantIMG == null) {
                    plantIMG.setId(util.createNewID("PIMG"));
                } else {
                    plantIMG.setId(util.createIDFromLastID("PIMG", 4, lastPlantIMG.getId()));
                }
                plantIMG.setPlant(plant.get());
                plantIMG.setImgURL(imgPURL);
                plantIMGRepository.save(plantIMG);
                return "Thêm thành công.";
            case "CONTRACT":
                Optional<Contract> contract = contractRepository.findById(entityID);
                if(contract == null) {
                    return "Không tìm thấy entity với ID là " + entityID + ".";
                }
                String cFileName = firebaseImageService.save(file);
                String imgCURL = firebaseImageService.getImageUrl(cFileName);
                ContractIMG contractIMG = new ContractIMG();
                ContractIMG lastContractIMG = contractIMGRepository.findFirstByOrderByIdDesc();
                if(lastContractIMG == null) {
                    contractIMG.setId(util.createNewID("CIMG"));
                } else {
                    contractIMG.setId(util.createIDFromLastID("CIMG", 4, lastContractIMG.getId()));
                }
                contractIMG.setContract(contract.get());
                contractIMG.setImgURL(imgCURL);
                contractIMGRepository.save(contractIMG);
                return "Thêm thành công.";
            case "SERVICE":
                Optional<Service> entity = serviceRepository.findById(entityID);
                if(entity == null) {
                    return "Không tìm thấy entity với ID là " + entityID + ".";
                }
                String sFileName = firebaseImageService.save(file);
                String imgSURL = firebaseImageService.getImageUrl(sFileName);
                ServiceIMG serviceIMG = new ServiceIMG();
                ServiceIMG lastServiceIMG = serviceIMGRepository.findFirstByOrderByIdDesc();
                if(lastServiceIMG == null) {
                    serviceIMG.setId(util.createNewID("SIMG"));
                } else {
                    serviceIMG.setId(util.createIDFromLastID("SIMG", 4, lastServiceIMG.getId()));
                }
                serviceIMG.setService(entity.get());
                serviceIMG.setImgURL(imgSURL);
                serviceIMGRepository.save(serviceIMG);

                return "Thêm thành công.";
            case "ORDERFEEDBACK":
                Optional<OrderFeedback> orderFeedback = orderFeedbackRepository.findById(entityID);
                if(orderFeedback == null) {
                    return "Không tìm thấy entity với ID là " + entityID + ".";
                }
                String ofFileName = firebaseImageService.save(file);
                String imgOFURL = firebaseImageService.getImageUrl(ofFileName);
                OrderFeedbackIMG orderFeedbackIMG = new OrderFeedbackIMG();
                OrderFeedbackIMG lastOrderFeedbackIMG = orderFeedbackIMGRepository.findFirstByOrderByIdDesc();
                if(lastOrderFeedbackIMG == null) {
                    orderFeedbackIMG.setId(util.createNewID("OFIMG"));
                } else {
                    orderFeedbackIMG.setId(util.createIDFromLastID("OFIMG", 5, lastOrderFeedbackIMG.getId()));
                }
                orderFeedbackIMG.setOrderFeedback(orderFeedback.get());
                orderFeedbackIMG.setImgURL(imgOFURL);
                orderFeedbackIMGRepository.save(orderFeedbackIMG);

                return "Thêm thành công.";
        }
        return "Thêm thất bại, kiểm tra lại EntityName.";
    }

    @Override
    public String deleteImage(String entityName, String entityID, String imageID) throws IOException {
        switch(entityName) {
            case "PLANT":
                Optional<Plant> plant = plantRepository.findById(entityID);
                if(plant == null) {
                    return "Không tìm thấy entity với ID là " + entityID + ".";
                }
                Optional<PlantIMG> plantIMG = plantIMGRepository.findById(imageID);
                if(plantIMG == null) {
                    return "Không tìm thấy ImageIMG với ID là " + imageID + ".";
                }

                String[] plantArr;
                plantArr = plantIMG.get().getImgURL().split("[/;?]");
                firebaseImageService.delete(plantArr[7]);

                plantIMG.get().setPlant(null);
                plantIMGRepository.save(plantIMG.get());
                return "Xóa thành công.";
            case "CONTRACT":
                Optional<Contract> contract = contractRepository.findById(entityID);
                if(contract == null) {
                    return "Không tìm thấy entity với ID là " + entityID + ".";
                }
                Optional<ContractIMG> contractIMG = contractIMGRepository.findById(imageID);
                if(contractIMG == null) {
                    return "Không tìm thấy ContractIMG với ID là " + imageID + ".";
                }

                String[] contractArr;
                contractArr = contractIMG.get().getImgURL().split("[/;?]");
                firebaseImageService.delete(contractArr[7]);

                contractIMG.get().setContract(null);
                contractIMGRepository.save(contractIMG.get());
                return "Xóa thành công.";
            case "SERVICE":
                Optional<Service> entity = serviceRepository.findById(entityID);
                if(entity == null) {
                    return "Không tìm thấy entity với ID là " + entityID + ".";
                }
                Optional<ServiceIMG> serviceIMG = serviceIMGRepository.findById(imageID);
                if(serviceIMG == null) {
                    return "Không tìm thấy ServiceIMG với ID là " + imageID + ".";
                }

                String[] serviceArr;
                serviceArr = serviceIMG.get().getImgURL().split("[/;?]");
                firebaseImageService.delete(serviceArr[7]);

                serviceIMG.get().setService(null);
                serviceIMGRepository.save(serviceIMG.get());
                return "Xóa thành công.";
            case "ORDERFEEDBACK":
                Optional<OrderFeedback> orderFeedback = orderFeedbackRepository.findById(entityID);
                if(orderFeedback == null) {
                    return "Không tìm thấy entity với ID là " + entityID + ".";
                }
                Optional<OrderFeedbackIMG> orderFeedbackIMG = orderFeedbackIMGRepository.findById(imageID);
                if(orderFeedbackIMG == null) {
                    return "Không tìm thấy OrderFeedbackIMG với ID là " + imageID + ".";
                }

                String[] orderFbArr;
                orderFbArr = orderFeedbackIMG.get().getImgURL().split("[/;?]");
                firebaseImageService.delete(orderFbArr[7]);

                orderFeedbackIMG.get().setOrderFeedback(null);
                orderFeedbackIMGRepository.save(orderFeedbackIMG.get());
                return "Xóa thành công.";
        }
        return "Xóa thất bại, kiểm tra lại EntityName và ImageID.";
    }
}
