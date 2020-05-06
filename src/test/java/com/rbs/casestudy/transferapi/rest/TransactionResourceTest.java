package com.rbs.casestudy.transferapi.rest;


import static com.rbs.casestudy.transferapi.model.TestUtils.TEST_TRANSACTION_1;
import static com.rbs.casestudy.transferapi.model.TestUtils.TEST_TRANSACTION_1_JSON;
import static com.rbs.casestudy.transferapi.model.TestUtils.TR_NO_SOURCE_ACCOUNT;
import static com.rbs.casestudy.transferapi.model.TestUtils.TR_NO_SOURCE_ACCOUNT_RESPONSE;
import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Collections;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Bean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;

import com.rbs.casestudy.transferapi.model.TestUtils;
import com.rbs.casestudy.transferapi.service.TransactionService;

@WebMvcTest(TransactionResource.class)
public class TransactionResourceTest {

    @Autowired private MockMvc mockMvc;
    @MockBean  private TransactionService service;

    @TestConfiguration
    static class TestContextConfiguration {
        @Bean
        public MethodValidationPostProcessor bean() {
            return new MethodValidationPostProcessor();
        }
    }

    @Test
    public void getOneShouldReturnATransaction() throws Exception {
        when(service.getTransaction(1L))
        .thenReturn(Optional.of(TEST_TRANSACTION_1));

        mockMvc.perform(get("/transactions/1"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(TEST_TRANSACTION_1_JSON));
    }

    @Test
    public void getMissingTransactionShouldReturn404() throws Exception {
        when(service.getTransaction(any()))
        .thenReturn(Optional.empty());

        mockMvc.perform(get("/transaction/missing"))
        .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void getListShouldReturnPageOfTransactions() throws Exception {
        when(service.listTransactions(any()))
        .thenReturn(new PageImpl<>(Collections.singletonList(TEST_TRANSACTION_1)));

        mockMvc.perform(get("/transactions"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(TEST_TRANSACTION_1_JSON)))
        .andExpect(content().string(containsString("totalPages")));
    }

    @Test
    public void transferRequestNegativeAmountShouldReturnError() throws Exception {

        mockMvc.perform(post("/transfer")
                .contentType("application/json")
                .content(TestUtils.transferRequest("from", "to", "-200")))
        .andDo(print())
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void transferRequestMissingAccountShouldReturnError() throws Exception {
        mockMvc.perform(post("/transfer")
                .contentType("application/json")
                .content(TR_NO_SOURCE_ACCOUNT))
        .andDo(print())
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(content().string(TR_NO_SOURCE_ACCOUNT_RESPONSE));
    }

    @Test
    public void transferRequestSameSrcDestShouldReturnError() throws Exception {
        mockMvc.perform(post("/transfer")
                .contentType("application/json")
                .content(TestUtils.transferRequest("same", "same", "100")))
        .andDo(print())
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void transferRequestNonsenseShouldReturnError() throws Exception {
        mockMvc.perform(post("/transfer")
                .contentType("application/json")
                .content("nonsense"))
        .andDo(print())
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void transferRequestValidRequestShouldWork() throws Exception {

        when(service.transfer(any()))
        .thenReturn(TEST_TRANSACTION_1);

        mockMvc.perform(post("/transfer")
                .contentType("application/json")
                .content(TestUtils.transferRequest("from", "to", "100")))
        .andDo(print())
        .andExpect(status().is2xxSuccessful());
    }

}
