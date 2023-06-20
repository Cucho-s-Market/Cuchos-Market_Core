package com.project.cuchosmarket;
import com.project.cuchosmarket.dto.DtItem;
import com.project.cuchosmarket.dto.DtOrder;
import com.project.cuchosmarket.enums.OrderStatus;
import com.project.cuchosmarket.enums.OrderType;
import com.project.cuchosmarket.exceptions.*;
import com.project.cuchosmarket.models.*;
import com.project.cuchosmarket.repositories.*;
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
import com.project.cuchosmarket.exceptions.*;

import java.util.Collections;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class OrderServiceTest {
    @MockBean
    private OrderRepository orderRepository;
    @Autowired
    private OrderService orderService;
    @MockBean
    private CustomerRepository customerRepository;
    @MockBean
    private BranchRepository branchRepository;

    @MockBean
    UserRepository userRepository;

    @MockBean
    private ProductRepository productRepository;

    @MockBean
    private StockRepository stockRepository;

    @Test
    public void testBuyProducts() {
        User customer = new Customer("ana","ape","ana@email.com","1234",null,12345678,44017427);
        Branch branch = new Branch(1l, "central","direccion",null, null);
        DtOrder dtOrder = new DtOrder(1l,5000,null,null,OrderStatus.PENDING,OrderType.DELIVERY);

        dtOrder.setProducts(Collections.singletonList(new DtItem()));

        when(userRepository.findByEmail(anyString())).thenReturn(customer);
        String user = userRepository.existsByEmail(customer.getEmail()).toString();
        when(branchRepository.findById(anyLong())).thenReturn(Optional.of(branch));
        when(productRepository.findById(anyString())).thenReturn(Optional.of(new Product()));
        when(stockRepository.findById(any(StockId.class))).thenReturn(Optional.of(new Stock()));
        when(orderRepository.save(any(Order.class))).thenReturn(new Order());

        // Llama al método a probar
        try {
            orderService.buyProducts("user@example.com", dtOrder);
            System.out.println("aca llego");
            Float respuesta = orderRepository.findById(dtOrder.getId()).get().getTotalPrice();
            System.out.println(respuesta);
            // Realiza las aserciones necesarias para verificar el comportamiento esperado
        } catch (Exception e) {
            System.out.println("El id de la nueva compra no es correcta");

            // Maneja las excepciones, si corresponde
        }
    }
}
