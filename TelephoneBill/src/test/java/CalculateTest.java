import org.junit.Before;
import org.junit.Test;

import java.math.BigDecimal;

import static org.junit.Assert.*;

public class CalculateTest {

    @Before
    public void setUp() {
    }

    @Test
    public void testOfCalculate() {
        Calculate calculate = new Calculate();
        assertEquals(calculate.calculate("420774577457,13-01-2020 18:10:15,13-01-2020 18:12:57\n" +
                "420774577457,18-01-2020 08:59:20,18-01-2020 09:10:00\n" +
                "420774577457,20-01-2020 07:58:20,20-01-2020 08:10:00\n" +
                "420776562357,20-01-2020 15:59:20,20-01-2020 16:15:00\n" +
                "420776562377,20-01-2020 15:59:20,20-01-2020 16:15:00\n" +
                "420776562377,20-01-2020 18:59:20,20-01-2020 19:15:00\n" +
                "420776562377,20-01-2020 20:59:20,20-01-2020 21:05:00\n"), BigDecimal.valueOf(17.2));
    }

}
