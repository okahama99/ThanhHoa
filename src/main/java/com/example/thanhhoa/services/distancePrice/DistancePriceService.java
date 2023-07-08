package com.example.thanhhoa.services.distancePrice;

import com.example.thanhhoa.dtos.DistancePriceModels.ShowDistancePriceModel;

public interface DistancePriceService {
    String create(Double pricePerKm);

    ShowDistancePriceModel getNewestDistancePrice();
}
