package co.jp.simplex.siw.utils;

import org.bitcoinj.params.RegTestParams;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringRunner.class)
@SpringBootTest
public class AppUtilsTest {
    @Autowired
    AppUtils appUtils;

    @Test
    public void testGetNetWorkParam() throws Exception {
        assertEquals(RegTestParams.get(), appUtils.getNetWorkParam());
    }
}
