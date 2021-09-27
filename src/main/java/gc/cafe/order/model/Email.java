package gc.cafe.order.model;

import org.springframework.util.Assert;

public record Email(String address) {

    public Email {
        Assert.notNull(address,"address is not null");
    }

}
