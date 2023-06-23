package com.project.cuchosmarket.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DtStatistics {

    @Getter
    @Setter
    @AllArgsConstructor
    public static class DtTopProduct {
        private String productName;
        private Long salesCount;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class DtSalesByBranch {
        private String branchName;
        private int totalSales;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class DtSales {
        private int totalSales;
        private int completedOrders;
        private int cancelledOrders;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class DtProfitByBranch {
        private String branchName;
        private int totalProfit;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class DtProfit {
        private double totalProfit;
        private double completedOrdersProfit;
        private double cancelledOrdersProfit;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class DtPopularBrand {
        private String brandName;
        private double popularityScore;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class DtSuccessfulPromotion {
        private String promotionName;
        private double successRate;
    }
}
