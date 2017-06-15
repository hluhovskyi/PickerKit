package com.dewarder.pickerkit;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;

public final class Queries {

    public static QueryBuilder newBuilder(ContentResolver resolver) {
        return new QueryBuilder(resolver);
    }

    public static String descOrder(String orderBy) {
        return orderBy + " DESC";
    }

    public static class QueryBuilder {

        private final ContentResolver mContentResolver;
        private Uri mUri;
        private String[] mProjection;
        private String mSelection;
        private String[] mSelectionArgs;
        private String mOrderBy;

        private QueryBuilder(ContentResolver contentResolver) {
            mContentResolver = contentResolver;
        }

        public QueryBuilder uri(Uri uri) {
            mUri = uri;
            return this;
        }

        public QueryBuilder projection(String... projections) {
            mProjection = projections;
            return this;
        }

        public QueryBuilder selection(String selection) {
            mSelection = selection;
            return this;
        }

        public QueryBuilder selectionArgs(String... args) {
            mSelectionArgs = args;
            return this;
        }

        public QueryBuilder orderBy(String orderBy) {
            mOrderBy = orderBy;
            return this;
        }

        public QueryBuilder orderByDesc(String orderBy) {
            mOrderBy = descOrder(orderBy);
            return this;
        }

        public Cursor execute() {
            return mContentResolver.query(mUri, mProjection, mSelection, mSelectionArgs, mOrderBy);
        }
    }
}
