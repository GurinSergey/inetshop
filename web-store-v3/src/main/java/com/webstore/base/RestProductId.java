package com.webstore.base;

import java.io.Serializable;
import java.util.Date;
import java.util.Objects;

public class RestProductId implements Serializable {
    private Date restDate;
    private long productId;
    private long warehouseId;

    public Date getRestDate() {
        return restDate;
    }

    public long getProductId() {
        return productId;
    }

    public long getWarehouseId() {
        return warehouseId;
    }

    public RestProductId() {
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null || getClass() != obj.getClass()) return false;
        RestProductId restProductId = (RestProductId) obj;

        return Objects.equals(getRestDate(), restProductId.getRestDate()) &&
                Objects.equals(getProductId(), restProductId.getProductId()) &&
                Objects.equals(getWarehouseId(), restProductId.getWarehouseId());
    }

    @Override
    public int hashCode() {
        return Objects.hash(restDate, productId, warehouseId);
    }
}
