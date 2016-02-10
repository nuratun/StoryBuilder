package tahastudio.storybuilder.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tahastudio.storybuilder.R;

/**
 * For the RecyclerView implementation to replace ListView across the app. Since RecyclerView
 * does not implement a CursorAdapter, it will extenda special class, RVCursorAdapter to bind
 * the db to the CardView.
 */
public class StoryAdapter extends RVCursorAdapter<StoryAdapter.StoryAdapterViewHolder>
        implements View.OnClickListener, View.OnLongClickListener {

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private final LayoutInflater layoutInflater;

    // For the bindData method
    String header;
    String description;
    String info;

    public StoryAdapter(final Context context, String header, String description, String info) {
        super();
        this.layoutInflater = LayoutInflater.from(context);
        this.header = header;
        this.description = description;
        this.info = info;
    }

    public interface OnItemClickListener {
        void onItemClicked(Cursor cursor); // Override this method in the activity/fragment
    }

    public interface OnItemLongClickListener {
        void onItemLongClicked(Cursor cursor); // Override this method in the activity/fragment
    }

    public void setOnItemClickListener(final OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(final OnItemLongClickListener onItemLongClickListener) {
        this.onItemLongClickListener = onItemLongClickListener;
    }

    @Override
    public StoryAdapterViewHolder onCreateViewHolder(final ViewGroup parent, final int type) {
        final View view = this.layoutInflater.inflate(R.layout.tab_view, parent, false);
        view.setOnClickListener(this); // Regular click: load the story
        view.setOnLongClickListener(this); // Long click: bring up delete dialog box

        return new StoryAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StoryAdapterViewHolder holder, final Cursor cursor) {
        holder.bindData(cursor); // Pass the data from StoryAdapterViewHolder static class
    }

    // On regular click
    @Override
    public void onClick(final View view) {
        if ( this.onItemClickListener != null ) {
            final RecyclerView recyclerView = (RecyclerView) view.getParent();
            final int position = recyclerView.getChildLayoutPosition(view); // The story clicked

            if ( position != recyclerView.NO_POSITION ) { // If something was actually clicked
                final Cursor cursor = this.getItem(position); // Get the cursor for that db entry
                this.onItemClickListener.onItemClicked(cursor);
            }
        }
    }

    // On long click
    @Override
    public boolean onLongClick(final View view) {
        if ( this.onItemLongClickListener != null ) {
            final RecyclerView recyclerView = (RecyclerView) view.getParent();
            final int position = recyclerView.getChildLayoutPosition(view);

            if ( position != recyclerView.NO_POSITION ) {
                final Cursor cursor = this.getItem(position);
                this.onItemLongClickListener.onItemLongClicked(cursor);
            }
        }
        return true;
    }

    public class StoryAdapterViewHolder extends RecyclerView.ViewHolder {
        // The elements to find in the tab_view layout file
        TextView name;
        TextView extra;
        TextView desc;

        public StoryAdapterViewHolder(final View view) {
            super(view); // The tab_view layout file passed in by the implementing activity

            name = (TextView) view.findViewById(R.id.name_info);
            extra = (TextView) view.findViewById(R.id.extra_info);
            desc = (TextView) view.findViewById(R.id.desc);
        }

        public void bindData(final Cursor cursor) {
            String title = cursor.getString(cursor.getColumnIndex(header));
            String genre = cursor.getString(cursor.getColumnIndex(description));
            String more = cursor.getString(cursor.getColumnIndex(info));
            name.setText(title);
            extra.setText(genre);
            desc.setText(more);
        }
    }
}
