package tahastudio.storybuilder.db;

import android.content.Context;
import android.database.Cursor;
import android.support.v4.widget.CursorAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import tahastudio.storybuilder.R;

/**
 * For the RecyclerView implementation to replace ListView across the app. The adapter
 * in the constructor will be a cursor working in tandem with the ContentProvider.
 */
public class StoryAdapter extends RecyclerView.Adapter<StoryAdapter.ViewHolder>{
    private CursorAdapter cursorAdapter;
    private Context context;

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

    public static class ViewHolder extends RecyclerView.ViewHolder {
        View base;

        public ViewHolder(View view) {
            super(view);
            base = view.findViewById(R.id.tab_view);
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
