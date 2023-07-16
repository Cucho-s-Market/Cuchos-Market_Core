package com.project.cuchosmarket;
import com.project.cuchosmarket.dto.DtItem;
import com.project.cuchosmarket.dto.DtOrder;
import com.project.cuchosmarket.dto.DtPromotion;
import com.project.cuchosmarket.enums.OrderStatus;
import com.project.cuchosmarket.enums.OrderType;
import com.project.cuchosmarket.exceptions.*;
import com.project.cuchosmarket.models.*;
import com.project.cuchosmarket.repositories.*;
import lombok.SneakyThrows;
import org.hibernate.mapping.Any;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import com.project.cuchosmarket.models.Branch;
import com.project.cuchosmarket.models.Order;
import com.project.cuchosmarket.models.StockId;
import com.project.cuchosmarket.models.User;
import com.project.cuchosmarket.services.OrderService;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.*;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNotEquals;

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
    private AddressNotExistException addressNotExistExceptionMock;


//        @Test
//        public void testBuyProducts() throws NoStockException, InvalidOrderException, BranchNotExistException, ProductNotExistException, AddressNotExistException, UserNotExistException {
//            // Configuración
//            User user = new Customer();
//            String userEmail = "example@example.com";
//            when(userRepository.findByEmail(userEmail)).thenReturn(user);
//
//            List<DtItem> productos = Arrays.asList(
//                new DtItem("alfajor", 42, 48, 3)
//            );
//            DtOrder dtOrder = new DtOrder();
//            dtOrder.setBranchId(123l);
//            dtOrder.setStatus(OrderStatus.PENDING);
//            dtOrder.setType(OrderType.PICK_UP);
//            dtOrder.setProducts(productos);
//            user.setEmail(userEmail);
//
//            Branch branch = new Branch();
//            branch.setId(123l);
//            when(branchRepository.findById(dtOrder.getBranchId())).thenReturn(Optional.of(branch));
//            Product product = new Product();
//            product.setName("alfajor");
//
//            when(productRepository.findById(productos.get(0).getName())).thenReturn(Optional.of(product));
//
//            StockId stockId = new StockId(product, branch);
//            Stock stock = new Stock();
//            stock.setQuantity(500);
//            stock.setId(stockId);
//           // when(stockRepository.findById(stockId)).thenReturn(Optional.of(stock));
//            when(stockRepository.findById(any(StockId.class))).thenReturn(Optional.of(stock));
//
/////////////ok ahora applypromotion sin promo
//            Order order = new Order();
//            DtItem dtItem = dtOrder.getProducts().get(0);
//            Item item = new Item(dtItem.getName(), product.getPrice(), product.getPrice(),
//                    dtItem.getQuantity(), product);
//            List<Promotion> productPromotions = new ArrayList<>();
//            when(promotionRepository.findPromotionsByProduct(product)).thenReturn(productPromotions);
////////////////////////
//            orderService.buyProducts(userEmail,dtOrder);
//        }
    @SneakyThrows
    @Test
    public void testGetOrder() {
        Long orderId = 1l;
        Order order = new Order();
        order.setId(orderId);
        Customer customer = new Customer("adriana", "pisano","adri@email.com", "1111",LocalDate.of(1988,10,01),12345678, 44017427 );
        order.setCustomer(customer);
        when(orderRepository.findById(orderId)).thenReturn(Optional.of(order));
        Order salida = orderService.getOrder(orderId);
        assertNotNull(salida);

    }

    @Test
    public void testGetPurchasesByCustomer() throws InvalidOrderException, UserNotExistException {
        String userEmail = "adri@email.com";
        int pageNumber = 2;
        int pageSize = 3;
        String orderStatus = "PENDING";
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
        System.out.println(salida.getTotalElements());
        assertNotNull(salida);
        assertEquals(expectedOrders.size(), salida.getContent().size());
    }
@Test
    public void testUpdateStatus() throws EmployeeNotWorksInException, OrderNotExistException, InvalidOrderException {
        String userEmail = "adri@email.com";
        DtOrder dtOrder = new DtOrder();
        Branch branchE = new Branch();
        Long marketBranchId = 1l;
        branchE.setId(marketBranchId);
        Employee userEWIB = new Employee();
        User user = new Employee();
        user.setEmail(userEmail);
        user.setId(1l);
        when(userRepository.findByEmail(userEmail)).thenReturn(user);
        when(employeeRepository.findById(1l)).thenReturn(Optional.of(userEWIB));
        dtOrder.setBranchId(branchE.getId());
        dtOrder.setId(2l);
        userEWIB.setId(1l);
        userEWIB.setBranch(branchE);

        Order orderVO = new Order();
        orderVO.setId(2l);
        orderVO.setStatus(OrderStatus.PREPARING);
        when(orderRepository.findById(2l)).thenReturn(Optional.of(orderVO));
        dtOrder.setStatus(OrderStatus.PREPARING);
        orderService.updateStatus(userEmail,dtOrder);
        verify(orderRepository).save(any(Order.class));
    }
    @Test
    public void cancelOrder() throws InvalidOrderException, OrderNotExistException {
        String userEmail = "adri@email.com";
        Long order_id = 1l;

        Customer user = new Customer();
        user.setEmail(userEmail);
        user.setId(11l);
        when(userRepository.findByEmail(userEmail)).thenReturn(user);
        Order order = new Order();
        order.setId(order_id);
        order.setStatus(OrderStatus.PENDING);
        when(orderRepository.findById(order_id)).thenReturn(Optional.of(order));
        order.setCustomer(user);
        orderService.cancelOrder(userEmail,order_id);
    }
}
