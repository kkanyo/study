package learningtest.template;

import static org.assertj.core.api.Assertions.assertThat;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import tobyspring.learningtest.template.Calculator;

public class CalcSumTest {
    Calculator calculator;
    String numFilepath;

    @BeforeEach
    void setUp() {
        this.calculator = new Calculator();
        this.numFilepath = getClass().getResource("/numbers.txt").getPath();
    }

    @Test
    public void sumOfNumbers() throws IOException {
        assertThat(this.calculator.calcSum(this.numFilepath)).isEqualTo(10);
    }

    @Test
    public void multipleOfNumbers() throws IOException {
        assertThat(this.calculator.calcMultiple(this.numFilepath)).isEqualTo(24);
    }

    @Test
    public void concatenateOfNumbers() throws IOException {
        assertThat(this.calculator.concatenate(this.numFilepath)).isEqualTo("1234");
    }
}
