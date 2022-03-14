package com.webstore.core.base;

import com.webstore.core.session.attribute.WatchHistory;

/**
 * Created by ALisimenko on 02.03.2018.
 */
public class WatchHistoryStorage {
    public WatchHistory watchHistory;
    public String note;
    public int count;

    public WatchHistoryStorage() {
    }

    public WatchHistoryStorage(WatchHistory watchHistory, String note) {
        this.setWatchHistory(watchHistory);
        this.note = note;
    }

    public WatchHistory getWatchHistory() {
        return watchHistory;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    public void setWatchHistory(WatchHistory watchHistory) {
        this.watchHistory = watchHistory;
        this.count = watchHistory.count();
    }
}
