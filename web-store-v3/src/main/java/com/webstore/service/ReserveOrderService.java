package com.webstore.service;

import com.webstore.domain.ReservOrderDetail;
import com.webstore.repository.ReservOrderRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class ReserveOrderService {
    @Autowired
    ReservOrderRepo reservOrderRepo;

    @Autowired
    ProductRestService productRestService;

    @Transactional
    public void saveReserve(ReservOrderDetail reservOrderDetail) {
        productRestService.addPlanSales(reservOrderDetail.getDate(),
                reservOrderDetail.getWarehouseId(),
                reservOrderDetail.getProductId(),
                reservOrderDetail.getQuantity());
        reservOrderRepo.save(reservOrderDetail);

    }

    public List<ReservOrderDetail> findAllReservByOrderId(Long id) {
        return reservOrderRepo.findAllByOrderId(id);
    }

    public List<ReservOrderDetail> findAllReserveByOrderDetailId(Long orderDetailId){
       return reservOrderRepo.getAllByOrderDetailId(orderDetailId);
    }

    @Transactional
    public void removeReserve(List<ReservOrderDetail> reservOrderDetails) {
        for (ReservOrderDetail reservOrderDetail : reservOrderDetails) {

            productRestService.removePlanSales(reservOrderDetail.getDate(),
                    reservOrderDetail.getWarehouseId(),
                    reservOrderDetail.getProductId(),
                    reservOrderDetail.getQuantity());

        }

        reservOrderRepo.delete(reservOrderDetails);

    }

    @Transactional
    public void closeReserveByInvoiceId(Long orderId) {
        reservOrderRepo.closeReserve(orderId);
    }
}
