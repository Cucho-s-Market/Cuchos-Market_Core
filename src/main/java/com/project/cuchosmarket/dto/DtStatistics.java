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
        private Long totalSales;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class DtSalesInBranch {
        private Long totalSales;
        private Long completedOrders;
        private Long cancelledOrders;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class DtProfitByBranch {
        private String branchName;
        private Long totalProfit;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class DtProfitInBranch {
        private double completedOrdersProfit;
        private double cancelledOrdersProfit;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class DtPopularBrand {
        private String brandName;
        private Long salesCount;
    }

    @Getter
    @Setter
    @AllArgsConstructor
    public static class DtSuccessfulPromotion {
        private String promotionName;
        private double successRate;
    }
}
