package ru.joutak.plugin.model;

public record RetryEvent(KillEvent event, int attempt) {}
