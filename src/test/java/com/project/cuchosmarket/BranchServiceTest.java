package com.project.cuchosmarket;

import com.project.cuchosmarket.dto.DtBranch;
import com.project.cuchosmarket.repositories.BranchRepository;
import com.project.cuchosmarket.services.BranchService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
@AutoConfigureMockMvc
public class BranchServiceTest {
    @Autowired
    private BranchService branchService;

    @MockBean
    private BranchRepository branchRepository;

    @DisplayName("Listar sucursales")
    @Test
    void testGetMarketBranches() {
        List<DtBranch> branches = List.of(new DtBranch(1L, "Sucursal", "Ejemplo"));

        when(branchRepository.findAllSelected()).thenReturn(branches);

        List<DtBranch> resultado = branchService.getMarketBranches();

        assertEquals(resultado.size(), branches.size());
        assertEquals(resultado.get(0).getName(), branches.get(0).getName());
    }
}