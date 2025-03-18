package org.example.loja_das_coisas_api.models.mappers

import java.time.Instant
import java.time.OffsetDateTime
import java.time.ZoneOffset

fun Instant.toDateTime(): OffsetDateTime = Instant.ofEpochMilli(this.epochSecond).atOffset(ZoneOffset.UTC)