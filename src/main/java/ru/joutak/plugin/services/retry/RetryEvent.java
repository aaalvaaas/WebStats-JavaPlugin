package ru.joutak.plugin.services.retry;

import ru.joutak.plugin.model.KillEvent;

public record RetryEvent(KillEvent event, int attempt) {}
