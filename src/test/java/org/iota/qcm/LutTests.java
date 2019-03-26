package org.iota.qcm;

import org.junit.Assert;
import org.junit.Test;

public class LutTests {
  @Test
  public void testRows() {
    boolean f;
    byte r, q;
    for(r = 0, f = false; r < 1 << 6; r ++, f = false) {
      for(q = -41; q <= 40 && !f; q++ ) {
        f = r == Lut.tritQuadToRow(q);
      }
      Assert.assertTrue("row: " +r+ " should be found from trit quad " + (q - 1), f);
      Assert.assertEquals("some conversion works", r, Lut.tritQuadToRow(Lut.rowToTritQuad(r)));
    }
  }
}
