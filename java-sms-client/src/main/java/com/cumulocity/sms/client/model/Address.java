package com.cumulocity.sms.client.model;

import com.google.common.base.Joiner;
import com.google.common.base.Splitter;
import com.google.common.collect.Iterables;
import lombok.extern.slf4j.Slf4j;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Iterator;

import static com.google.common.base.Preconditions.checkArgument;
import static com.google.common.base.Strings.isNullOrEmpty;

@Slf4j
public class Address {
    public static final char PROTOCOL_SEPARATOR = ':';

    private Protocol protocol;

    private String number;

    public Address() {
    }

    public Address(Protocol protocol, String number) {
        this.protocol = protocol;
        this.number = number;
    }

    public Protocol getProtocol() {
        return protocol;
    }

    public void setProtocol(Protocol protocol) {
        this.protocol = protocol;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public static Address phoneNumber(String number) {
        return new Address(Protocol.MSISDN, number);
    }

    public static Address iccid(String number) {
        return new Address(Protocol.ICCID, number);
    }

    public static Address acr(String number) {
        return new Address(Protocol.ACR, number);
    }

    public static Address valueOf(String value) {
        checkArgument(!isNullOrEmpty(value));
        final Iterable<String> splited = Splitter.on(PROTOCOL_SEPARATOR).trimResults().trimResults().split(value);
        final int size = Iterables.size(splited);
        checkArgument(size == 2, "Too many protocol separators");
        checkArgument(size != 0, "Unable tor parse address from " + value);
        Iterator<String> parts = splited.iterator();
        if (hasProtocol(size)) {
            return new Address(Protocol.fromValue(parts.next()), parts.next());
        } else {
            return new Address(null, parts.next());
        }
    }

    @Override
    public String toString() {
        return asString(this.number);
    }

    public String asUrlParameter() {
        try {
            if (this.number == null) {
                return null;
            }
            return asString(URLEncoder.encode(this.number, "UTF-8"));
        } catch (final UnsupportedEncodingException e) {
            log.error(e.getMessage(), e);
            return "";
        }
    }

    public String asString(String number) {
        return protocol != null ? Joiner.on(PROTOCOL_SEPARATOR).join(protocol.value(), number) : number;
    }

    public String normalizedNumber() {
        if (number == null) {
            return null;
        }
        return number.replaceAll("[^\\d+]", "");
    }

    public Address normalize() {
        this.number = normalizedNumber();
        return this;
    }

    public static boolean hasProtocol(final int size) {
        return size == 2;
    }

    @Override
    public int hashCode() {
        final int prime = 31;
        int result = 1;
        result = prime * result + ((number == null) ? 0 : number.hashCode());
        result = prime * result + ((protocol == null) ? 0 : protocol.hashCode());
        return result;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())
            return false;
        Address other = (Address) obj;
        if (number == null) {
            if (other.number != null)
                return false;
        } else if (!number.equals(other.number))
            return false;
        if (protocol != other.protocol)
            return false;
        return true;
    }
}