package com.yy.opensource.coreanimation;

import android.graphics.Bitmap;
import javax.microedition.khronos.opengles.GL10;

/**
 * Created by cuiminghui on 2016/10/31.
 */

public class CALayer {

    /**
     * Describe a layer's position and size. x and y relate to Left-Top coordinate.
     */
    public CGRect frame = new CGRect(0,0,0,0);

    /**
     * Describe layer content's transforming. Base on 3D matrix.
     * Use it for content scale, skew, rotate, translate.
     */
    public CATransform3D transform = new CATransform3D();

    /**
     * Describe the transform anchor point.
     */
    public CGPoint anchorPoint = new CGPoint(0.5f, 0.5f);

    /**
     * If true, layer will not be rendered.
     */
    public boolean hidden = false;

    /**
     * A hint marking that the layer contents provided by -drawInContext:
     * is completely opaque. Defaults to NO. Note that this does not affect
     * the interpretation of the `contents' property directly.
     */
    public boolean opaque = false;

    /**
     * The opacity of the layer, as a value between zero and one. Defaults
     * to one. Specifying a value outside the [0,1] range will give undefined
     * results.
     */
    public float opacity = 1.0f;

    /**
     * The background color of the layer. Default value is nil. Colors
     * created from tiled patterns are supported.
     */
    public CGColor backgroundColor = null;

    /**
     * When positive, the background of the layer will be drawn with
     * rounded corners. Also effects the mask generated by the
     * `masksToBounds' property. Defaults to zero.
     */
    public float cornerRadius = 0.0f; // todo

    /**
     * The width of the layer's border, inset from the layer bounds. The
     * border is composited above the layer's content and sublayers and
     * includes the effects of the `cornerRadius' property. Defaults to
     * zero.
     */
    public float borderWidth = 0.0f; // todo

    /**
     * The color of the layer's border. Defaults to opaque black. Colors
     * created from tiled patterns are supported.
     */
    public CGColor borderColor = null; // todo

    /**
     * A string defining how the contents of the layer is mapped into its
     * bounds rect. Options are `resize', `resizeAspect', `resizeAspectFill'.
     */
    public String contentsGravity = "resize";

    /**
     * When true an implicit mask matching the layer bounds is applied to
     * the layer (including the effects of the `cornerRadius' property). If
     * both `mask' and `masksToBounds' are non-nil the two masks are
     * multiplied to get the actual mask values. Defaults to NO.
     */
    public Boolean masksToBounds = false;

    public void setContents(Bitmap bitmap) {
        this.contents = bitmap;
        this.contentSize.width = (float)bitmap.getWidth();
        this.contentSize.height = (float)bitmap.getHeight();
//        textureLoaded = false;
    }

    public CALayer[] getSubLayers() {
        return subLayers;
    }

    public void addSublayer(CALayer layer) {
        CALayer[] oldValues = subLayers;
        subLayers = new CALayer[oldValues.length + 1];
        for (int i = 0; i < oldValues.length; i++) {
            subLayers[i] = oldValues[i];
        }
        subLayers[oldValues.length] = layer;
        layer.superLayer = this;
    }

    public void removeFromSuperLayer() {
        if (superLayer != null) {
            CALayer[] newElements = new CALayer[superLayer.subLayers.length - 1];
            int j = 0;
            boolean found = false;
            for (int i = 0; i < superLayer.subLayers.length; i++) {
                CALayer element = superLayer.subLayers[i];
                if (element != this) {
                    newElements[j] = element;
                    j++;
                }
                else {
                    found = true;
                }
            }
            if (found) {
                superLayer.subLayers = newElements;
            }
        }
    }

    /**
     * Private props and methods!!!
     */

    /**
     * Describe root window bounds.
     */
    protected CGRect windowBounds = new CGRect(0, 0, 0, 0);

    /**
     * Provide a bitmap, so layer will render this bitmap as content.
     */
    protected Bitmap contents = null;

    /**
     * Describe the size of content.
     */
    protected CGSize contentSize = new CGSize(0, 0);

    /**
     * Gets superLayer here.
     */
    protected CALayer superLayer = null;

    /**
     * Gets subLayers here.
     */
    protected CALayer[] subLayers = new CALayer[0];

    protected void draw(GL10 gl) {
        if (hidden || CALayerHelper.combineOpacity(this) <= 0.0) {
            return;
        }
        gl.glFrontFace(GL10.GL_CW);
        CALayerMask.drawMask(this, gl);
        CALayerBackground.drawBackgroundColor(this, gl);
        CALayerTexture.drawTextures(this, gl);
        gl.glDisable(GL10.GL_STENCIL_TEST);
        for (int i = 0; i < subLayers.length; i++) {
            CALayer layer = subLayers[i];
            layer.windowBounds = windowBounds;
            layer.draw(gl);
        }
    }

}
