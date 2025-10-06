package org.example.jobs;

import org.example.service.AccountService;
import org.example.service.ClientService;
import org.example.service.CreditService;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class Scheduler {
    public static void start(ClientService clientService,
                             AccountService accountService,
                             CreditService creditService) {

        ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2);

        // Pour les tests → exécution toutes les 10 secondes
        scheduler.scheduleAtFixedRate(new SalaryJob(clientService, accountService),
                0, 10, TimeUnit.SECONDS);

        scheduler.scheduleAtFixedRate(new CreditPaymentJob(creditService, accountService),
                0, 10, TimeUnit.SECONDS);
    }
}