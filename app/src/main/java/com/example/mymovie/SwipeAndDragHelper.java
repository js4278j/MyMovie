package com.example.mymovie;

import android.graphics.Canvas;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.recyclerview.widget.ItemTouchHelper.Callback.makeMovementFlags;

public class SwipeAndDragHelper extends ItemTouchHelper.Callback {

    private ActionCompletionContract contract;

    public SwipeAndDragHelper(ActionCompletionContract contract){
        this.contract = contract;
    }

    @Override
    public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
        /*if (viewHolder instanceof SectionHeaderViewHolder) {
            return 0;
        }*/
        int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN;
        int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT;
        return makeMovementFlags(dragFlags, swipeFlags);
    }


    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
        contract.onViewMoved(viewHolder.getAdapterPosition(), target.getAdapterPosition());
        return true;
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        contract.onViewSwiped(viewHolder.getAdapterPosition());
    }

    //isLong , isItem 둘다 false Swipe 기능x
    @Override
    public boolean isLongPressDragEnabled(){
        return false;
    }

    @Override
    public boolean isItemViewSwipeEnabled(){
        return false;
    }


    @Override
    public void onChildDraw(Canvas c,
                            RecyclerView recyclerView,
                            RecyclerView.ViewHolder viewHolder,
                            float dX,
                            float dY,
                            int actionState,
                            boolean isCurrentlyActive) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            float alpha = 1 - (Math.abs(dX) / recyclerView.getWidth());
            viewHolder.itemView.setAlpha(alpha);
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    public interface ActionCompletionContract {
        void onViewMoved(int oldPosition, int newPosition);
        void onViewSwiped(int position);
    }
}
