package com.rbs.casestudy.transferapi.rest.serializers;

import java.io.IOException;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.rbs.casestudy.transferapi.model.Account;

/**
 * Serialize an {@link Account} as its <b>accountNumber</b>
 * @author ed
 *
 */
public class TerseAccountSerializer extends JsonSerializer<Account> {

    @Override
    public void serialize(Account value, JsonGenerator gen, SerializerProvider serializers) throws IOException {
        serializers.defaultSerializeValue(value.getAccountNumber(), gen);
    }

}
