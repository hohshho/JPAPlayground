package jpabook.jpashop.api;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryDto;
import jpabook.jpashop.repository.OrderRepository;
import jpabook.jpashop.repository.OrderSearch;
import jpabook.jpashop.repository.order.simplequery.OrderSimpleQueryRepository;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.List;

import static java.util.stream.Collectors.toList;


/**
 * xToOne(ManyToOne, OneToOne)
 * Order
 * Order -> Memeber
 * Order -> Delivery
 */
@RestController
@RequiredArgsConstructor
public class OrderSimpleApiController {

    private final OrderRepository orderRepository;
    private final OrderSimpleQueryRepository orderSimpleQueryRepository;

    /*
     * V1. 엔티티 직접 노출
     * - Hibernate5Module 모듈 등록, LAZY=null 처리 * - 양방향 관계 문제 발생 -> @JsonIgnore
     */
    @GetMapping("/api/v1/simple-orders")
    public List<Order> ordersV1() {
        List<Order> all = orderRepository.findAllByString(new OrderSearch());
        for (Order order : all) {
            order.getMember().getName(); //Lazy 강제 초기화
            order.getDelivery().getAddress(); //Lazy 강제 초기환 }
        }
        return all;
    }

    @GetMapping("/api/v2/simple-orders")
    public List<OrderSimpleDto> orderV2() {
        List<Order> orders = orderRepository.findAllByString(new OrderSearch());
        List<OrderSimpleDto> result = orders.stream()
                .map(o -> new OrderSimpleDto(o))
                .collect(toList());

        return result;
    }

    // 1. 엔티티를 DTO로 변환
    // 패치 조인으로 내부에 원하는 부분만 가져옴
    @GetMapping("/api/v3/simple-orders")
    public List<OrderSimpleDto> ordersV3() {
        List<Order> orders = orderRepository.findAllWithMemberDelivery();
        List<OrderSimpleDto> result = orders.stream()
                .map(o-> new OrderSimpleDto(o))
                .collect(toList());

        return result;
    }

    // 2. DTO로 직접 조회
    // 재사용성이 떨어진다.
    // API 스펙에 맞춘 코드가 리포지토리에 들어감
    @GetMapping("/api/v4/simple-orders")
    public List<OrderSimpleQueryDto> orderV4() {
        return orderSimpleQueryRepository.findOrderSimpleDtos();
    }

    @Data
    static class OrderSimpleDto {
        private Long orderId;
        private String name;
        private LocalDateTime orderDate; //주문시간
        private OrderStatus orderStatus;
        private Address address;

        public OrderSimpleDto(Order order) {
            orderId = order.getId();
            name = order.getMember().getName();
            orderDate = order.getOrderDate();
            orderStatus = order.getStatus();
            address = order.getDelivery().getAddress();
        }
    }

}