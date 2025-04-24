package com.example.testcenter.mapper;

import com.example.testcenter.model.db.entity.ClientOrder;
import com.example.testcenter.model.dto.response.ClientOrderInfoResp;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface ClientOrderMapper {
    ClientOrderInfoResp toClientOrderInfoResp (ClientOrder clientOrder);

    List<ClientOrderInfoResp> toClientOrderInfoRespList (List<ClientOrder> clientOrders);

}
