package com.wswright.words.demo.service;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;

import static org.assertj.core.api.Assertions.assertThat;


public class WordValidityServiceTest {
    private WordValidityService wordValidityService;

    @Before
    public void setUp() throws Exception {
        wordValidityService = new WordValidityService();
    }

    @Test
    public void isValid() {
        assertThat(wordValidityService.isValid("face,")).isFalse();
    }
}