package org.iota.qcm;

import org.iota.qbc.TritMath;

public class Lut implements Branch {
  long value;
  byte[] table;

  public Lut(TritBuffer buffer) {
    int nCells, nIn, i, j;
    long cell;
    byte s, t;
    table = new byte[64];

    t = buffer.nextTrit();
    nCells = buffer.nextPositiveInteger();

    switch (t) {
      case 0:
        nIn = 1;
        break;
      case 1:
        nIn = 2;
        break;
      case -1:
        nIn = 3;
        break;
      default: throw new ArrayIndexOutOfBoundsException();
    }

    value = 0;

    for(i = 0; i < nCells; i++) {
      s = TritMath.tritBct(buffer.nextTrit());
      for(j = 1; j < nIn; j++) {
        s <<= 2;
        s |= TritMath.tritBct(buffer.nextTrit());
      }
      cell = TritMath.tritBct(buffer.nextTrit());
      value |= cell << s;
    }
  }

  public static byte rowToTritQuad(byte row) {
    byte quad = 0;
    quad += (byte) ((row / 27) % 3) * 27;
    quad += (byte) ((row / 9) % 3) * 9;
    quad += (byte) ((row / 3) % 3) * 3;
    quad += (byte) (row % 3);
    return (byte) (quad - 40);
  }

  public static byte tritQuadToRow(byte quad) {
    quad += 40;
    byte r = (byte) (quad % 4);
    r |= ((quad / 4) % 4) << 2;
    r |= ((quad / 16) % 4) << 4;
    return (byte) (r & 63);
  }

  @Override
  public int inputLength() {
    return 0;
  }

  @Override
  public void compile(BranchInstance b, int d) {
    b.f.lazySet((in, out) -> out.value[0] |= 0x3 & value >> in.value[0]);
  }
}
