package client.control;

import static org.junit.Assert.assertArrayEquals;

import org.junit.Before;
import org.junit.Test;

public class ClientControllerTest {
    ClientController controller;

    @Before
    public void init() {
        controller = new ClientController();
    }

    @Test
    public void testSendTextMessage() {
        var messages = new String[] {"Hello", "World!"};
        for (String message : messages) controller.sendMessage(message);

        assertArrayEquals(messages, controller.getTextMessages().toArray());
    }
}
