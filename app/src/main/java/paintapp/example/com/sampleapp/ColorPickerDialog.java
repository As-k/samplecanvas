package paintapp.example.com.sampleapp;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

public class ColorPickerDialog extends AlertDialog {

    private final OnColorSelectedListener onColorSelectedListener;

    private ColorPicker colorPickerView;

    private OnClickListener onClickListener = new OnClickListener() {

        public void onClick(DialogInterface dialog, int which) {

            switch (which) {
                case DialogInterface.BUTTON_NEGATIVE:
                    dialog.dismiss();
                    return;
                case DialogInterface.BUTTON_POSITIVE:
                    ColorPickerDialog.this.onColorSelectedListener.onColorSelected(ColorPickerDialog.this.colorPickerView.getColor());
                    return;
                default:
                    return;
            }
        }
    };

    public ColorPickerDialog(Context context, int initialColor, OnColorSelectedListener onColorSelectedListener) {
        super(context);
        this.onColorSelectedListener = onColorSelectedListener;
        RelativeLayout relativeLayout = new RelativeLayout(context);
        LayoutParams layoutParams = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
        layoutParams.addRule(RelativeLayout.CENTER_IN_PARENT);
        this.colorPickerView = new ColorPicker(context);
        this.colorPickerView.setColor(initialColor);
        relativeLayout.addView(this.colorPickerView, layoutParams);
        setButton(DialogInterface.BUTTON_POSITIVE, "OK", this.onClickListener);
        setButton(DialogInterface.BUTTON_NEGATIVE, "CANCEL", this.onClickListener);
        setView(relativeLayout);
    }

    public interface OnColorSelectedListener {
        void onColorSelected(int i);
    }
}
