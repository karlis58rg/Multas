package mx.ssg.multas.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;

import androidx.annotation.RequiresApi;

import java.util.HashMap;

public class KeyboardUtils implements ViewTreeObserver.OnGlobalLayoutListener {

    private final static int MAGIC_NUMBER = 200;

    private SoftKeyboardToggleListener mCallback;
    private View mRootView;
    private Boolean prevValue = null;
    private float mScreenDensity;
    private static HashMap<SoftKeyboardToggleListener, KeyboardUtils> sListenerMap = new HashMap<>();

    public interface SoftKeyboardToggleListener
    {
        void onToggleSoftKeyboard(boolean isVisible);

    }
    @Override
    public void onGlobalLayout() {

        Rect r = new Rect();
        mRootView.getWindowVisibleDisplayFrame(r);

        int heightDiff = mRootView.getRootView().getHeight() - (r.bottom - r.top);
        float dp = heightDiff/ mScreenDensity;
        boolean isVisible = dp > MAGIC_NUMBER;

        if (mCallback != null && (prevValue == null || isVisible != prevValue)) {
            prevValue = isVisible;
            mCallback.onToggleSoftKeyboard(isVisible);
        }

    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void addKeyboardToggleListener(Activity act, SoftKeyboardToggleListener listener)
    {
        removeKeyboardToggleListener(listener);

        sListenerMap.put(listener, new KeyboardUtils(act, listener));
    }

    /**
     * Remove a registered listener
     * @param listener {@link SoftKeyboardToggleListener}
     */
    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    public static void removeKeyboardToggleListener(SoftKeyboardToggleListener listener)
    {
        if(sListenerMap.containsKey(listener))
        {
            KeyboardUtils k = sListenerMap.get(listener);
            k.removeListener();

            sListenerMap.remove(listener);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN)
    private void removeListener()
    {
        mCallback = null;

        mRootView.getViewTreeObserver().removeOnGlobalLayoutListener(this);
    }

    private KeyboardUtils(Activity act, SoftKeyboardToggleListener listener)
    {
        mCallback = listener;

        mRootView = ((ViewGroup) act.findViewById(android.R.id.content)).getChildAt(0);
        mRootView.getViewTreeObserver().addOnGlobalLayoutListener(this);

        mScreenDensity = act.getResources().getDisplayMetrics().density;
    }
}
