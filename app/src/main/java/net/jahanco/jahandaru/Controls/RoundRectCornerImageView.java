package net.jahanco.jahandaru.Controls;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.support.v7.widget.AppCompatImageView;
import android.util.AttributeSet;

import net.jahanco.jahandaru.R;


public class RoundRectCornerImageView
  extends AppCompatImageView
{
  private Path path;
  private float radius = 18.0F;
  private RectF rect;
  
  public RoundRectCornerImageView(Context paramContext)
  {
    super(paramContext);
    init(paramContext, null);
  }
  
  public RoundRectCornerImageView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    init(paramContext, paramAttributeSet);
  }
  
  public RoundRectCornerImageView(Context paramContext, AttributeSet paramAttributeSet, int paramInt)
  {
    super(paramContext, paramAttributeSet, paramInt);
    init(paramContext, paramAttributeSet);
  }
  
  private void init(Context paramContext, AttributeSet paramAttributeSet)
  {
    this.radius = paramContext.obtainStyledAttributes(paramAttributeSet, R.styleable.RoundRectCornerImageView, 0, 0).getFloat(0, 0.0F);
    this.path = new Path();
  }
  
  protected void onDraw(Canvas paramCanvas)
  {
    this.rect = new RectF(0.0F, 0.0F, getWidth(), getHeight());
    this.path.addRoundRect(this.rect, this.radius, this.radius, Direction.CW);
    paramCanvas.clipPath(this.path);
    super.onDraw(paramCanvas);
  }
}


/* Location:              C:\Users\Mr R00t\Desktop\dec-dex2jar.jar!\biz\ejahan\lifestories\Views\Controls\RoundRectCornerImageView.class
 * Java compiler version: 6 (50.0)
 * JD-Core Version:       0.7.1
 */