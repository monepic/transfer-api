package com.rbs.casestudy.transferapi.rest;

import static com.rbs.casestudy.transferapi.model.TestUtils.OPEN_ACC_NO_ACCOUNT;
import static com.rbs.casestudy.transferapi.model.TestUtils.OPEN_ACC_NO_ACCOUNT_RESPONSE;
import static com.rbs.casestudy.transferapi.model.TestUtils.TEST_ACCOUNT_1;
import static com.rbs.casestudy.transferapi.model.TestUtils.TEST_ACCOUNT_1_JSON;
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
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.HttpStatus;
import org.springframework.test.web.servlet.MockMvc;

import com.rbs.casestudy.transferapi.model.TestUtils;
import com.rbs.casestudy.transferapi.service.AccountService;


@WebMvcTest(AccountResource.class)
public class AccountResourceTest {


    @Autowired private MockMvc mockMvc;
    @MockBean private AccountService service;

    @Test
    public void getOneShouldReturnAnAccount() throws Exception {
        when(service.getAccount("one"))
        .thenReturn(Optional.of(TEST_ACCOUNT_1));

        mockMvc.perform(get("/accounts/one"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(TEST_ACCOUNT_1_JSON));
    }

    @Test
    public void getMissingAccountShouldReturn404() throws Exception {
        when(service.getAccount(any()))
        .thenReturn(Optional.empty());

        mockMvc.perform(get("/accounts/missing"))
        .andExpect(status().is(HttpStatus.NOT_FOUND.value()));
    }

    @Test
    public void getListShouldReturnPageOfAccounts() throws Exception {
        when(service.listAccounts(any()))
        .thenReturn(new PageImpl<>(Collections.singletonList(TEST_ACCOUNT_1)));

        mockMvc.perform(get("/accounts"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString(TEST_ACCOUNT_1_JSON)))
        .andExpect(content().string(containsString("totalPages")));
    }

    @Test
    public void openAccountNegativeOBShouldReturnError() throws Exception {

        mockMvc.perform(post("/accounts")
                .contentType("application/json")
                .content(TestUtils.openAccount("new_account", "-100")))
        .andDo(print())
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()));
    }

    @Test
    public void openAccountNoAccountNumberShouldReturnError() throws Exception {
        mockMvc.perform(post("/accounts")
                .contentType("application/json")
                .content(OPEN_ACC_NO_ACCOUNT))
        .andDo(print())
        .andExpect(status().is(HttpStatus.BAD_REQUEST.value()))
        .andExpect(content().string(OPEN_ACC_NO_ACCOUNT_RESPONSE));
    }


    @Test
    public void openAccountValidRequestShouldWork() throws Exception {

        when(service.openAccount(any()))
        .thenReturn(TEST_ACCOUNT_1);

        mockMvc.perform(post("/accounts")
                .contentType("application/json")
                .content(TestUtils.openAccount("my_new_account", "100.00")))
        .andDo(print())
        .andExpect(status().is2xxSuccessful());
    }

}

