package jpabook.jpashop.Service;

import jpabook.jpashop.domain.Address;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;
import jpabook.jpashop.domain.OrderStatus;
import jpabook.jpashop.domain.item.Book;
import jpabook.jpashop.domain.item.Item;
import jpabook.jpashop.exception.NotEnoughStockException;
import jpabook.jpashop.service.OrderService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.EntityManager;

@ExtendWith({SpringExtension.class})
@SpringBootTest
@Transactional
class OrderServiceTest {
    @Autowired
    EntityManager em;
    @Autowired
    OrderService orderService;
    @Autowired
    OrderRepository orderRepository;

    OrderServiceTest() {
    }

    @Test
    public void 상품주문() throws Exception {
        Member member = this.createMember();
        Item item = this.createBook("시골 JPA", 10000, 10);
        int orderCount = 2;
        Long orderId = this.orderService.order(member.getId(), item.getId(), orderCount);
        Order getOrder = this.orderRepository.findOne(orderId);
        Assertions.assertEquals(OrderStatus.ORDER, getOrder.getStatus(), "상품 주문시 상태는 ORDER");
        Assertions.assertEquals(getOrder.getOrderItems().size(), 1, "주문한 상품 종류 수가 정확해야 한다.");
        Assertions.assertEquals(getOrder.getTotalPrice(), 20000, "주문 가격은 가격 * 수량이다.");
        Assertions.assertEquals(item.getStockQuantity(), 8, "주문 수량만큼 재고가 줄어야 한다.");
    }

    @Test
    public void 주문취소() throws Exception {
        Member member = this.createMember();
        Item item = this.createBook("시골 JPA", 10000, 10);
        int orderCount = 2;
        Long orderId = this.orderService.order(member.getId(), item.getId(), orderCount);
        this.orderService.cancelOrder(orderId);
        Order getOrder = this.orderRepository.findOne(orderId);
        Assertions.assertEquals(getOrder.getStatus(), OrderStatus.CANCEL, "주문 취소시 상태는 CANCEL 이다.");
        Assertions.assertEquals(item.getStockQuantity(), 10, "주문이 취소된 상품은 그만큼 재고가 증가해야 한다.");
    }

    @Test
    public void 상품주문_재고수량초과() throws Exception {
        Member member = this.createMember();
        Item item = this.createBook("시골 JPA", 10000, 10);
        int orderCount = 11;
        Assertions.assertThrows(NotEnoughStockException.class, () -> {
            this.orderService.order(member.getId(), item.getId(), orderCount);
            Assertions.fail("재고 수량 부족 예외가 발생해야 한다.");
        });
    }

    private Member createMember() {
        Member member = new Member();
        member.setName("회원1");
        member.setAddress(new Address("서울", "강가", "123-123"));
        this.em.persist(member);
        return member;
    }

    private Book createBook(String name, int price, int stockQuantity) {
        Book book = new Book();
        book.setName(name);
        book.setStockQuantity(stockQuantity);
        book.setPrice(price);
        this.em.persist(book);
        return book;
    }
}
