package com.project.cuchosmarket;
import com.project.cuchosmarket.dto.DtOrder;
import com.project.cuchosmarket.enums.OrderStatus;
import com.project.cuchosmarket.exceptions.*;
import com.project.cuchosmarket.models.*;
import com.project.cuchosmarket.repositories.*;
import lombok.SneakyThrows;
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

import java.time.LocalDate;
import java.util.*;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.testng.Assert.assertEquals;

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

        @Test
        public void test(){
            // Configuración
            User user = new Customer();
            String userEmail = "example@example.com";
            user.setEmail(userEmail);
            when(userRepository.findByEmail(userEmail)).thenReturn(user);

            Branch branch = new Branch();
            Long branchId = 123L;
            branch.setId(branchId);
            Long addressId = 456L;
            DtOrder dtOrder = new DtOrder();

            Product product = new Product();
            String productCode = "1L";
            product.setCode(productCode);
            Stock stock = new Stock();
            Address address = new Address();
            address.setId(addressId);

            when(branchRepository.findById(branchId)).thenReturn(Optional.of(branch));
            when(productRepository.findById(anyString())).thenReturn(Optional.of(product));
            when(stockRepository.findById(any(StockId.class))).thenReturn(Optional.of(stock));

            try {
                orderService.buyProducts(userEmail,dtOrder);
            } catch (BranchNotExistException e) {
                throw new RuntimeException(e);
            } catch (UserNotExistException e) {
                throw new RuntimeException(e);
            } catch (ProductNotExistException e) {
                throw new RuntimeException(e);
            } catch (NoStockException e) {
                throw new RuntimeException(e);
            } catch (InvalidOrderException e) {
                throw new RuntimeException(e);
            } catch (AddressNotExistException e) {
                throw new RuntimeException(e);
            }

        }
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
        String orderDirection = "calle 1234";
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
