package tahastudio.storybuilder.db;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import tahastudio.storybuilder.R;

/**
 * For the RecyclerView implementation to replace ListView across the app. The adapter
 * in the constructor will be a cursor working in tandem with the ContentProvider.
 */
public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder>{
    private static OnClickListener clickListener;
    private CursorAdapter cursorAdapter;
    private Context context;

    public interface OnClickListener {
        void onItemClick(int position, View view);
        void onItemLongClick(int position, View view);
    }

    public void setClickListener(OnClickListener clickListener) {
        StoryAdapter.clickListener = clickListener;
    }

    public StoryAdapter(Context context, Cursor cursor) {
        this.context = context;

        cursorAdapter = new CursorAdapter(context, cursor, 0) {
            @Override
            public View newView(Context context, Cursor cursor, ViewGroup parent) {
                return LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.tab_view, parent, false);
            }

            @Override
            public void bindView(View view, Context context, Cursor cursor) {
                cursorAdapter.bindView(view, context, cursor);
            }
        };
    }

    public static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnClickListener, View.OnLongClickListener {
        // The elements to find in the tab_view layout file
        TextView story_id;
        TextView element_id;
        TextView name;
        TextView extra;
        TextView desc;

        public ViewHolder(View view) {
            super(view); // The tab_view layout file passed in by the implementing activity

            story_id = (TextView) view.findViewById(R.id.story_id);
            element_id = (TextView) view.findViewById(R.id.element_id);
            name = (TextView) view.findViewById(R.id.name_info);
            extra = (TextView) view.findViewById(R.id.extra_info);
            desc = (TextView) view.findViewById(R.id.desc);

            view.setOnClickListener(this); // Make the layout file clickable
            view.setOnLongClickListener(this);
        }

        @Override
        public void onClick(View view) {
            clickListener.onItemClick(getAdapterPosition(), view);
        }

        @Override
        public boolean onLongClick(View view) {
            clickListener.onItemLongClick(getAdapterPosition(), view);
            return true;
        }
    }

    @Override
    public StoryAdapter.ViewHolder onCreateViewHolder(final ViewGroup parent, final int type) {
        View view = cursorAdapter.newView(context, cursorAdapter.getCursor(), parent);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(StoryAdapter.ViewHolder holder, int position) {
        cursorAdapter.getCursor().moveToPosition(position);
        cursorAdapter.bindView(holder.itemView, context, cursorAdapter.getCursor());
    }

    @Override
    public int getItemCount() {
        return cursorAdapter.getCount();
    }
}
