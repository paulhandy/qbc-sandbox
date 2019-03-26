package org.iota.qcm;

import org.iota.qbc.TritMath;
import sun.reflect.generics.reflectiveObjects.NotImplementedException;

import java.nio.BufferOverflowException;
import java.nio.ByteBuffer;
import java.nio.ShortBuffer;

import static org.iota.qbc.TritMath.TRITS_PER_SHORT;

public class TritBuffer {
  ShortBuffer shortBuffer;
  int buf;
  byte bufRem;
  int rem;


  public TritBuffer(short[] shorts, int count) {
    shortBuffer = ShortBuffer.wrap(shorts);
    rem = count;
    buf = 0;//(byte) Math.min(rem, TritMath.TRITS_PER_SHORT);
    //nextTrits();
  }

  public TritBuffer() {}

  public static TritBuffer cloneRange(TritBuffer toClone, int count) {
    TritBuffer tritBuffer = new TritBuffer();
    tritBuffer.shortBuffer = toClone.shortBuffer.duplicate();
    tritBuffer.rem = count;
    tritBuffer.bufRem = toClone.bufRem;
    tritBuffer.buf = toClone.buf;
    toClone.skip(count);
    return tritBuffer;
  }

  public void skip(int count) {
    for(int i = 0; i < count; i++) {
      nextTrit();
    }
  }

  public void checkRemaining() {
    if(rem == 0) {
      throw new BufferOverflowException();
    }
    if(bufRem == 0) {
      nextTrits();
    }
  }

  public byte nextTrit() {
    checkRemaining();
    rem--;
    bufRem--;
    return (byte) (buf & 0x3);
  }

  public int nextTrint() {
    int out, r;
    out = buf & 0x3FF;
    if (bufRem < TritMath.TRITS_PER_SHORT) {
      buf >>= bufRem;
      rem -= bufRem;
      r = bufRem;
      bufRem = 0;
      nextTrits();
      out |= (buf & (0x3ff >> ((TRITS_PER_SHORT - r) << 1))) << (r << 1);
      r = TRITS_PER_SHORT - r;
      buf >>= r << 1;
      bufRem -= r;
    }  else {
      buf >>= (TritMath.TRITS_PER_SHORT << 1);
    }
    return out;
  }

  public long next54() {
    int i;
    long out = 0;
    for(i = 0; i < 54; i += TRITS_PER_SHORT << 1) {
      out |= nextTrint() << i;
    }
    return out;
  }

  public void fillBuffer(short[] out, int count) {
    int i, j, r;
    short s;

    throw new NotImplementedException();
    /*
    for(checkRemaining(), i = 0; i < count; checkRemaining()) {
      out[i] = b;
      TritMath.bctValue(buf & 0x3ff);
      for(r = buf; r < TritMath.TRITS_PER_SHORT; r++) {
        switch (nextTrit()) {
          case 1: out[i] -= TritMath.short_radix[buf+1];
          break;
          case -1: out[i] += TritMath.short_radix[buf+1];
          break;
        }
      }
    }
    */
  }

  public int nextPositiveInteger() {
    int val = 0;
    int i = 0;
    byte b;
    while((b = nextTrit()) != 0) {
      if(b == 1) {
        val |= 1 << i++;
      }
    }
    return val;
  }

  private void nextTrits() {
    buf |= TritMath.shortBct(shortBuffer.get()) << (bufRem << 1);
    rem -= TritMath.TRITS_PER_SHORT;
    bufRem += TritMath.TRITS_PER_SHORT;
  }

  public int nextInteger(int nTrits) {
    return 0;
  }
}
