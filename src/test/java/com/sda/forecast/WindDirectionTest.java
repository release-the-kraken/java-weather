package com.sda.forecast;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

class WindDirectionTest {

    @Test
    void getWindDirectionSymbol_shouldReturnCorrectValueForGivenDegrees() {
        //when
        String n = WindDirection.getWindDirectionSymbol(360).toString();
        String ne = WindDirection.getWindDirectionSymbol(48).toString();
        String e = WindDirection.getWindDirectionSymbol(96).toString();
        String se = WindDirection.getWindDirectionSymbol(145).toString();
        String s= WindDirection.getWindDirectionSymbol(190).toString();
        String sw = WindDirection.getWindDirectionSymbol(236).toString();
        String w = WindDirection.getWindDirectionSymbol(279).toString();
        String nw = WindDirection.getWindDirectionSymbol(330).toString();
        //then
        assertThat(n).isEqualTo("N");
        assertThat(ne).isEqualTo("NE");
        assertThat(e).isEqualTo("E");
        assertThat(se).isEqualTo("SE");
        assertThat(s).isEqualTo("S");
        assertThat(sw).isEqualTo("SW");
        assertThat(w).isEqualTo("W");
        assertThat(nw).isEqualTo("NW");
    }
    @Test
    void getWindDirectionSymbol_shouldThrowExceptionForValueOutOfRange(){
        //then
        assertThrows(IllegalArgumentException.class, () -> WindDirection.getWindDirectionSymbol(-1));
        assertThrows(IllegalArgumentException.class, () -> WindDirection.getWindDirectionSymbol(361));
    }
}