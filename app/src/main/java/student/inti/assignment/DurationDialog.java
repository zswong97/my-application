package student.inti.assignment;

import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.NumberPicker;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatDialogFragment;
import androidx.fragment.app.DialogFragment;

public class DurationDialog extends DialogFragment {
        private NumberPicker.OnValueChangeListener valueChangeListener;

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {

            final NumberPicker numberPicker = new NumberPicker(getActivity());

            numberPicker.setMinValue(1);
            numberPicker.setMaxValue(999);
            numberPicker.setValue(90);
            AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
            builder.setTitle("Duration");

            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    valueChangeListener.onValueChange(numberPicker,
                            numberPicker.getValue(), numberPicker.getValue());
                }
            });

            builder.setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    valueChangeListener.onValueChange(numberPicker,
                            numberPicker.getValue(), numberPicker.getValue());
                }
            });

            builder.setView(numberPicker);
            return builder.create();
        }

        public NumberPicker.OnValueChangeListener getValueChangeListener() {
            return valueChangeListener;
        }

        public void setValueChangeListener(NumberPicker.OnValueChangeListener valueChangeListener) {
            this.valueChangeListener = valueChangeListener;
        }
    }
