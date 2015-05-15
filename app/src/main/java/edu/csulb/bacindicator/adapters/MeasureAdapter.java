package edu.csulb.bacindicator.adapters;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

import edu.csulb.bacindicator.R;
import edu.csulb.bacindicator.models.Measure;

/**
 * Created by Johan
 */
public class MeasureAdapter extends ArrayAdapter<Measure> {

    public MeasureAdapter(Context context, List<Measure> list) {
        super(context, android.R.layout.select_dialog_item, list);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Measure measure = getItem(position);
        ViewHolder holder;

        if (convertView == null) {
            LayoutInflater inflater = ((Activity) getContext()).getLayoutInflater();

            convertView = inflater.inflate(R.layout.single_string_list_item, parent, false);
            holder = new ViewHolder(convertView);

            holder.measure = (TextView) convertView.findViewById(R.id.string);

            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.measure.setText(measure.name);
        return convertView;
    }

    // And here is when the "chooser" is popped up
    // Normally is the same view, but you can customize it if you want
    @Override
    public View getDropDownView(int position, View convertView,
                                ViewGroup parent) {
        return getView(position, convertView, parent);
    }

    private static class ViewHolder {
        TextView measure;

        ViewHolder(View layout) {
            measure = (TextView) layout.findViewById(R.id.string);
        }
    }
}
