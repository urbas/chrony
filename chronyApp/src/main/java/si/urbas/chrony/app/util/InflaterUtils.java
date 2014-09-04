package si.urbas.chrony.app.util;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

public class InflaterUtils {

  public static LayoutInflater getLayoutInflater(Context context) {
    return (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
  }

  public static View inflateLayout(Context context, int layoutId) {
    return getLayoutInflater(context).inflate(layoutId, null);
  }
}
