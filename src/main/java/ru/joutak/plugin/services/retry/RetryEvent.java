package ru.joutak.plugin.services.retry;

import ru.joutak.plugin.model.KillEvent;

import java.util.List;

public record RetryEvent(List<KillEvent> events, int attempt) {}
