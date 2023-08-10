package com.example.thanhhoa.services.statistic;

import com.example.thanhhoa.dtos.StatisticModels.ShowStatisticModel;

import java.time.LocalDateTime;

public interface StatisticService {

    ShowStatisticModel getStatistic(String storeID, LocalDateTime from, LocalDateTime to);
}
