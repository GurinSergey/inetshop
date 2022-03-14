package com.webstore.base;

import com.webstore.session.attribute.WatchHistory;

import java.io.Serializable;

/**
 * Created by ALisimenko on 02.03.2018.
 */
public class WatchHistoryStorage implements Serializable {
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
        if (this.watchHistory != null) {
            this.count = watchHistory.count();
        }
    }
}
