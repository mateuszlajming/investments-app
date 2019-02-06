package com.lajming.investmentsapp;

import lombok.NonNull;
import lombok.Value;

@Value(staticConstructor = "of")
public class Fund {
    private final long id;
    @NonNull private final FundType type;
    @NonNull private final String name;
}