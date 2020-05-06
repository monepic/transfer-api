package com.rbs.casestudy.transferapi.rest.serializers;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers;
import com.fasterxml.jackson.databind.deser.std.NumberDeserializers.BigDecimalDeserializer;

/**
 * Deserialize monetary values to two decimal places, ignoring any further decimal digits
 * @author ed
 */
public class MoneyDeserializer extends JsonDeserializer<BigDecimal> {

    private NumberDeserializers.BigDecimalDeserializer delegate = BigDecimalDeserializer.instance;

    @Override
    public BigDecimal deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return delegate.deserialize(p, ctxt).setScale(2, RoundingMode.FLOOR);
    }

}