package com.project.cuchosmarket;

import com.project.cuchosmarket.dto.DtItem;
import com.project.cuchosmarket.dto.DtOrder;
import com.project.cuchosmarket.enums.OrderStatus;
import com.project.cuchosmarket.enums.OrderType;
import com.project.cuchosmarket.exceptions.*;
import com.project.cuchosmarket.models.*;
import com.project.cuchosmarket.repositories.*;
import com.project.cuchosmarket.services.EmailService;
import com.project.cuchosmarket.services.OrderService;
import jakarta.mail.MessagingException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderServiceTest {
    @MockBean
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;

    @MockBean
    private BranchRepository branchRepository;
    @MockBean
    private UserRepository userRepository;
    @MockBean
    private ProductRepository productRepository;
    @MockBean
    private StockRepository stockRepository;
    @MockBean
    private PromotionRepository promotionRepository;
    @MockBean
    private EmployeeRepository employeeRepository;

    @MockBean
    private EmailService emailService;

    @DisplayName("Comprar productos")
    @Test
    public void testBuyProducts() throws NoStockException, InvalidOrderException, BranchNotExistException, ProductNotExistException, AddressNotExistException, UserNotExistException, MessagingException {
        User user = new Customer();
        String userEmail = "example@example.com";
        user.setEmail(userEmail);
        when(userRepository.findByEmail(userEmail)).thenReturn(user);

        List<DtItem> productos = List.of(
                new DtItem("alfajor", 42, 48, 3)
        );
        DtOrder dtOrder = new DtOrder();
        dtOrder.setBranchId(123L);
        dtOrder.setStatus(OrderStatus.PENDING);
        dtOrder.setType(OrderType.PICK_UP);
        dtOrder.setProducts(productos);


        Branch branch = Branch.builder()
                .id(123L)
                .orders(new ArrayList<>())
                .build();
        when(branchRepository.findById(dtOrder.getBranchId())).thenReturn(Optional.of(branch));

        Product product = new Product();
        product.setName("alfajor");
        when(productRepository.findById(any())).thenReturn(Optional.of(product));

        Stock stock = new Stock();
        stock.setQuantity(500);
        when(stockRepository.findById(any(StockId.class))).thenReturn(Optional.of(stock));

        when(promotionRepository.findPromotionsByProduct(any())).thenReturn(new ArrayList<>());

        orderService.buyProducts(userEmail, dtOrder);
        verify(orderRepository, times(1)).save(any());
        verify(branchRepository, times(1)).save(any());
    }

    @DisplayName("Obtener informacion de pedido")
    @Test
    public void testGetOrder() throws OrderNotExistException {
        Long orderId = 1l;
        Order order = new Order();
        order.setId(orderId);
        Customer customer = new Customer("adriana", "pisano","adri@email.com", "1111",LocalDate.of(1988,10,01),12345678, 44017427 );
        order.setCustomer(customer);

        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        when(branchRepository.findByOrdersContains(any())).thenReturn(Branch.builder().id(1L).build());
        Map<String, Object> salida = orderService.getOrder(orderId);
        assertNotNull(salida);
    }

    @DisplayName("Historico de compras de un cliente - Filtrado por status y fecha")
    @Test
    public void testGetPurchasesByCustomer() throws InvalidOrderException, UserNotExistException {
        String userEmail = "adri@email.com";

        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(3);
        User user = new Customer();
        when(userRepository.findByEmail(userEmail)).thenReturn(user);

        List<DtOrder> expectedOrders = new ArrayList<>();
        Page<DtOrder> entrada = new PageImpl<>(expectedOrders);

        when(orderRepository.findCustomerOrders(eq(user.getId()), eq(OrderStatus.PENDING), eq(startDate), eq(endDate), any(Pageable.class))).thenReturn(entrada);
        Page<DtOrder> salida = orderService.getPurchasesByCustomer("adri@email.com",2,3,"PENDING",LocalDate.now(),LocalDate.now().plusDays(3),"calle 1234");
        assertNotNull(salida);

    }

    @DisplayName("Historico de ventas de una sucursal")
    @Test
    public void testGetOrdersBy() throws InvalidOrderException, EmployeeNotWorksInException, UserNotExistException {
        String userEmail = "adri@email.com";
        int pageNumber = 0;
        int pageSize = 1;
        String orderStatus = "PENDING";
        LocalDate startDate = LocalDate.now();
        LocalDate endDate = LocalDate.now().plusDays(3);
        String orderDirection = "calle 1234";
        Long marketBranchId = 1l;

        Employee user = new Employee("adriana", "pisano", userEmail, "1111",  Branch.builder().id(1l).build() );
        when(userRepository.findByEmail(userEmail)).thenReturn(user);
        when(employeeRepository.findById(any())).thenReturn(Optional.of(user));

        List<DtOrder> expectedOrders = new ArrayList<>();


        Page<DtOrder> entrada = new PageImpl<>(expectedOrders);
        when(branchRepository.findOrders(eq(marketBranchId), eq(OrderStatus.PENDING), eq(startDate), eq(endDate), any(Pageable.class))).thenReturn(entrada);

        Page<DtOrder> salida = orderService.getOrdersBy(userEmail,pageNumber,pageSize,marketBranchId,orderStatus,startDate, endDate, orderDirection);

        assertNotNull(salida);
        assertEquals(expectedOrders.size(), salida.getContent().size());
    }

    @DisplayName("Cambiar estado de una compra")
    @Test
    public void testUpdateStatus() throws EmployeeNotWorksInException, OrderNotExistException, InvalidOrderException, MessagingException {
        String userEmail = "adri@email.com";
        DtOrder dtOrder = new DtOrder();
        dtOrder.setBranchId(1l);
        dtOrder.setId(2l);
        dtOrder.setStatus(OrderStatus.PREPARING);

        Branch branchE = new Branch();
        branchE.setId(1l);

        Employee userEWIB = new Employee();
        User user = new Employee();
        user.setEmail(userEmail);
        user.setId(1l);
        userEWIB.setId(1l);
        userEWIB.setBranch(branchE);

        when(userRepository.findByEmail(userEmail)).thenReturn(user);
        when(employeeRepository.findById(1l)).thenReturn(Optional.of(userEWIB));

        Customer customer = new Customer();
        customer.setEmail("customer@example.com");

        Order orderVO = new Order();
        orderVO.setId(2l);
        orderVO.setStatus(OrderStatus.PENDING);
        orderVO.setCustomer(customer);

        when(orderRepository.findById(2l)).thenReturn(Optional.of(orderVO));

        orderService.updateStatus(userEmail, dtOrder);
        verify(orderRepository, times(1)).save(any());
    }

    @DisplayName("Cliente cancela compra")
    @Test
    public void cancelOrder() throws InvalidOrderException, OrderNotExistException, MessagingException {
        String userEmail = "adri@email.com";
        Long order_id = 1l;

        Customer user = new Customer();
        user.setEmail(userEmail);
        user.setId(11l);

        when(userRepository.findByEmail(userEmail)).thenReturn(user);
        Order order = new Order();
        order.setId(order_id);
        order.setStatus(OrderStatus.PENDING);
        order.setCustomer(user);

        when(orderRepository.findById(order_id)).thenReturn(Optional.of(order));

        orderService.cancelOrder(userEmail,order_id);
        verify(orderRepository, times(1)).save(any());
    }
}
