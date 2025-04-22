package com.example.testcenter.mapper;

import com.example.testcenter.model.db.entity.OrderItem;
import com.example.testcenter.model.dto.response.OrderItemInfoResp;
import org.mapstruct.Mapper;
import org.mapstruct.MappingConstants;

import java.util.List;

@Mapper(componentModel = MappingConstants.ComponentModel.SPRING)
public interface OrderItemMapper {

    OrderItemInfoResp toOrderItemInfoResp(OrderItem orderItem);

    List<OrderItemInfoResp> toOrderItemInfoRespList(List<OrderItem> orderItems);


}
