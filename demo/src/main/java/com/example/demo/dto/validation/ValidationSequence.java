package com.example.demo.dto.validation;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

@GroupSequence({Default.class, NotEmptyGroup.class, SizeCheckGroup.class})
public interface ValidationSequence {
}
