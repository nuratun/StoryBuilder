package tahastudio.storybuilder.adapters;

import android.database.Cursor;
import android.support.v7.widget.RecyclerView;

/**
 * As RecyclerView does not implement a native CursorAdapter, such as ListView uses,
 * we're required to implement one manually.
 */
public abstract class RVCursorAdapter<VH extends RecyclerView.ViewHolder>
        extends RecyclerView.Adapter<VH> {

    public Cursor cursor;

    public void swapCursor(final Cursor cursor) {
        this.cursor = cursor;
        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return this.cursor != null
                ? this.cursor.getCount() : 0;
    }

    public Cursor getItem(final int position) {
        if ( this.cursor != null && !this.cursor.isClosed() ) {
            this.cursor.moveToPosition(position);
        }
        return this.cursor;
    }

    public Cursor getCursor() {
        return this.cursor;
    }

    @Override
    public final void onBindViewHolder(final VH holder, final int position) {
        final Cursor cursor = getItem(position);
        this.onBindViewHolder(holder, cursor);
    }

    public abstract void onBindViewHolder(final VH holder, final Cursor cursor);
}
