package com.cinema;

import com.cinema.application.service.TicketService;
import com.cinema.infra.repository.TicketRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@Transactional
public class PessimisticLockTest {

    @Autowired
    private TicketRepository ticketRepository;

    @Autowired
    private TicketService ticketService;

    @Test
    void testPessimisticLock() throws InterruptedException {
    }
}
