package com.yy.opensource.coreanimation;

/**
 * Created by cuiminghui on 2016/10/31.
 */

public class CATransform3D extends CGAffineTransform {

    float[] request3DMatrix() {
        return new float[] {
                a,      c,       0.0f,      0.0f,
                b,      d,       0.0f,      0.0f,
                0.0f,   0.0f,    1.0f,      0.0f,
                tx,     ty,      0.0f,      1.0f
        };
    }

}
