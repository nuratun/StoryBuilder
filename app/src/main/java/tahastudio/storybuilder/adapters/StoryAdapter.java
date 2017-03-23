package tahastudio.storybuilder.adapters;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tahastudio.storybuilder.R;
import tahastudio.storybuilder.db.Constants;
import tahastudio.storybuilder.helpers.ItemTouchHelperAdapter;
import tahastudio.storybuilder.tasks.ArchiveElementTask;

/**
 * For the RecyclerView implementation to replace ListView across the app. Since RecyclerView
 * does not implement a CursorAdapter, it will extend a special class, RVCursorAdapter to bind
 * the db to the CardView.
 */
public class StoryAdapter extends RVCursorAdapter<StoryAdapter.StoryAdapterViewHolder>
        implements View.OnClickListener, View.OnLongClickListener, ItemTouchHelperAdapter {

    private final LayoutInflater layoutInflater;

    private OnItemClickListener onItemClickListener;
    private OnItemLongClickListener onItemLongClickListener;
    private String title;
    private String extra;
    private String desc;

    public StoryAdapter(final Context context, String title, String extra, String desc) {
        super();
        this.layoutInflater = LayoutInflater.from(context);
        this.title = title;
        this.extra = extra;
        this.desc = desc;
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
        holder.bindData(cursor); // Pass the data from StoryAdapterViewHolder class
    }

    // On regular click
    @Override
    public void onClick(final View view) {
        if ( this.onItemClickListener != null ) {
            final RecyclerView recyclerView = (RecyclerView) view.getParent();
            final int position = recyclerView.getChildLayoutPosition(view); // The element clicked

            if ( position != RecyclerView.NO_POSITION) { // If something was actually clicked
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

            if ( position != RecyclerView.NO_POSITION) {
                final Cursor cursor = this.getItem(position);
                this.onItemLongClickListener.onItemLongClicked(cursor);
            }
        }
        return true;
    }

    @Override
    public void onItemDismiss(int position) {

        new ArchiveElementTask(Constants.STORY_TABLE, position).execute();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {

    }

    public class StoryAdapterViewHolder extends RecyclerView.ViewHolder {
        // The elements to find in the tab_view layout file
        TextView left;
        TextView right;
        TextView bottom;

        public StoryAdapterViewHolder(final View view) {
            super(view); // The tab_view layout file passed in by the implementing activity

            left = (TextView) view.findViewById(R.id.name_info);
            right = (TextView) view.findViewById(R.id.extra_info);
            bottom = (TextView) view.findViewById(R.id.desc);
        }

        public void bindData(final Cursor cursor) {
            String lString = cursor.getString(cursor.getColumnIndex(title));
            String rString = cursor.getString(cursor.getColumnIndex(extra));
            String bString = cursor.getString(cursor.getColumnIndex(desc));
            left.setText(lString);
            right.setText(rString);
            bottom.setText(bString);
        }
    }
}
